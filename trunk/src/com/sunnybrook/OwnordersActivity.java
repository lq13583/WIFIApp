package com.sunnybrook;

import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class OwnordersActivity extends ListActivity  implements  OnClickListener,OnItemClickListener,OnItemSelectedListener,OnItemLongClickListener{
	static final int OWNORDER_ACTIVITY_ID = 1;
	static private ownOrdersAdapter mOrderAdapter;
	static private ProgressDialog mProgressDialog;
	static private RefreshOrderListThread mRefreshOrderListThread;
	static private String mLaborCode;
	static private String mOrderby = "wonum";
	private String mOrderId = "";
	private ownorder mOrder = null;
	private WIFIApp mParent;
	public void onCreate(Bundle savedInstanceState) {

    	super.onCreate(savedInstanceState);
    	mParent = (WIFIApp) getParent();
    	mLaborCode = mParent.myConfig.getLabor_code();
    	mProgressDialog  = new ProgressDialog(this);

    	setContentView(R.layout.ownorderactivity);

/* Initialise the OrdersAdapter */
    	mOrderAdapter = new ownOrdersAdapter(this,R.layout.list_ownorder);
    	setListAdapter(mOrderAdapter);

/* Initialise the OrderBy Spinner */    	
    	Spinner mSpinner = (Spinner) findViewById(R.id.spinner_sort);
    	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
    			this, R.array.own_order_array, android.R.layout.simple_spinner_item);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	mSpinner.setAdapter(adapter);
    	mSpinner.setOnItemSelectedListener(this);
    	getListView().setOnItemClickListener(this);
    	getListView().setOnItemLongClickListener(this);
    	getListView().setOnItemSelectedListener(this);
    	
    	Button mButton = (Button) findViewById(R.id.btnRefresh);
    	mButton.setOnClickListener(this);
    	mButton = (Button) findViewById(R.id.btnSync);
    	mButton.setOnClickListener(this);
    	mButton = (Button) findViewById(R.id.btnOpen);
    	mButton.setOnClickListener(this);
    	
    	refreshOrderList(mLaborCode,mOrderby);
    }
	
	public class ownOrdersAdapter extends ArrayAdapter<ownorder> {

		public ownOrdersAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if(v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_ownorder, null);
			}
			ownorder mS = getItem(position);
			if(mS != null) {
				TextView t = (TextView) v.findViewById(R.id.wonum);
				if(t!= null) t.setText(mS.getOrderId());
				t = (TextView) v.findViewById(R.id.reportdate);
				if(t!=null)
					t.setText(new MyDateFormat().myFormat(mS.getReportdate()));
				t = (TextView) v.findViewById(R.id.location);
				if(t!=null) t.setText(mS.getLocation());
				t = (TextView) v.findViewById(R.id.wopriority);
				if(t!=null) t.setText(Integer.toString(mS.getWopriority()));
				t = (TextView) v.findViewById(R.id.status);
				if(t!=null) t.setText(mS.getStatus());
				t = (TextView) v.findViewById(R.id.readstatus);
				if(t!=null) t.setText(mS.getReadStatus());
			}
			return v;
		}
	}

	final Handler mHandler = new Handler() {
    	public void handleMessage(Message msg) {
    		String tmpMsg;
    		switch(msg.arg1)
    		{
    			case 0:
    				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    				tmpMsg = (String) msg.obj;
    				mProgressDialog.setMessage(tmpMsg);
    				mProgressDialog.show();
    				break;
    			case 1:
    				break;
    			case 2:
    				ownorder tmpOrder = (ownorder) msg.obj;
    				if(!tmpOrder.getStatus().equals("COMP"))
    					mOrderAdapter.add(tmpOrder);
    	    		break;
    			case datasync.DATASYNC_RUNNING:
    				tmpMsg = (String) msg.obj;
    				mProgressDialog.setMessage(tmpMsg);
    				if(!mProgressDialog.isShowing()) {
        				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        				mProgressDialog.show();
    				}
    				break;
    			case datasync.DATASYNC_FINISHED:
    				if(mProgressDialog.isShowing())
    					mProgressDialog.dismiss();
    				if(msg.arg2> 0) {
    					notifyNewOrders(Integer.toString(msg.arg2) + " new orders received!");
    				}
    				else {
        				tmpMsg = (String) msg.obj;
//        				showMessage(tmpMsg);
    				}
					refreshOrderList(mLaborCode,mOrderby);
    				break;
    			default:
    				if(mProgressDialog.isShowing()){
    					mProgressDialog.dismiss();
        				locateOrder();
    				}
    				mParent.updateCountsOutstanding();
    				mParent.updateCountsUpdates();
    		}
    	}
    };

    private void locateOrder() {
    	if(mOrderId.length() == 0) return;
		ListView lv = (ListView) findViewById(android.R.id.list);
		mOrder = null;
    	for (int i=0; i< lv.getCount(); i++) {
    		if(((ownorder) lv.getItemAtPosition(i)).getOrderId().equals(mOrderId)) {
    			lv.requestFocusFromTouch();
    			lv.setSelection(i);
    			break;
    		}
    	}
    }
    
	private class RefreshOrderListThread extends Thread {
		private Handler mHandler;
		private String mLaborcode;
		private String mOrderby;
		private List<ownorder> mItems;
		
		public RefreshOrderListThread(String _laborcode, String _orderby, Handler _handler) {
			mLaborcode=_laborcode;
			mOrderby = _orderby;
			mHandler = _handler;
		}
		
		public void run() {
			Message msg = mHandler.obtainMessage();
			msg.arg1 = 0;
			msg.obj = "Loading data ........";
			mHandler.sendMessage(msg);
	    	mItems = mParent.localdb.getOwnOrderList(mLaborcode,mOrderby);
	    	msg = mHandler.obtainMessage();
	    	msg.arg1 = 1;
	    	msg.arg2 = mItems.size() - 1;
	    	mHandler.sendMessage(msg);

	    	for(int i=0;i<mItems.size();i++) {
	    		msg = mHandler.obtainMessage();
	    		msg.arg1 = 2;
	    		msg.arg2 = i;
	    		msg.obj = mItems.get(i);
	    		mHandler.sendMessage(msg);
	    	}
	    	msg = mHandler.obtainMessage();	    	
	    	msg.arg1 = 3;
	    	mHandler.sendMessage(msg);
		}
	}

	private void refreshOrderList(String _laborcode,String _orderby) {
    	if (mRefreshOrderListThread != null)
    		if(mRefreshOrderListThread.isAlive())
    			return;
    	mRefreshOrderListThread = new RefreshOrderListThread(_laborcode,_orderby,mHandler);
		mOrderAdapter.clear();
		mRefreshOrderListThread.start();
		mParent.mRefreshOwnOrder = false;
	}
	
	@Override
	public void onItemClick(AdapterView<?> _AdapterView, View arg1, int _pos, long arg3) {
		switch(_AdapterView.getId()) {
		case android.R.id.list:
			ListView lv = (ListView) _AdapterView;
			lv.requestFocusFromTouch();
			lv.setSelection(_pos);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		if(_requestCode == OWNORDER_ACTIVITY_ID) {
	    	refreshOrderList(mLaborCode,mOrderby);
	    	return;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> _AdapterView, View _View, int _pos, long _row) {
		switch(_AdapterView.getId()) {
			case R.id.spinner_sort:				
				mOrderby = _AdapterView.getItemAtPosition(_pos).toString();
				OwnorderComparator mComparator = new OwnorderComparator(mOrderby);
				mOrderAdapter.sort(mComparator);
				locateOrder();
				break;
			case android.R.id.list:
				mOrder = (ownorder) _AdapterView.getItemAtPosition(_pos);
				mOrderId = mOrder.getOrderId();
				TextView mText = (TextView) findViewById(R.id.txtDescription);
				mText.setText(mOrder.getComments());
				break;
			default:
				break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void refreshOrderList() {
    	refreshOrderList(mLaborCode,mOrderby);
	}
	
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()) {
			case R.id.btnRefresh:
		    	refreshOrderList(mLaborCode,mOrderby);
				break;
			case R.id.btnSync:
				WifiManager mWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
				new datasync(mHandler,mParent.myConfig,mParent.localdb,mWifi).start();
				break;
			case R.id.btnOpen:
				if(mOrder==null) return;
				if(datasync.is_running) {
					showMessage("Data Sync is currently running, please wait until it is finished!");
					return;
				}
    			Intent mIntent = new Intent(this,OwnOrderDetailActivity.class);
    			mIntent.putExtra("ownorder", mOrder);
    			this.startActivityForResult(mIntent, OWNORDER_ACTIVITY_ID);
    			break;
			default:	
				break;
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> _AdapterView, View arg1, int _pos,long arg3) {
		switch(_AdapterView.getId()) {
		case android.R.id.list:
			ListView lv = (ListView) _AdapterView;
			lv.requestFocusFromTouch();
			lv.setSelection(_pos);
			mOrder = (ownorder) _AdapterView.getItemAtPosition(_pos);
			Intent mIntent = new Intent(this,OwnOrderDetailActivity.class);
			mIntent.putExtra("ownorder", mOrder);
			this.startActivityForResult(mIntent, OWNORDER_ACTIVITY_ID);
			break;
		default:
			break;
		}
		return true;
	}

	private void notifyNewOrders(String _msg) {
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification mNotification = new Notification();
    	Context context = getApplicationContext();
    	CharSequence contentTitle = "My notification";
    	CharSequence contentText = _msg;
    	Intent notificationIntent = new Intent(this, WIFIApp.class);
    	PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
    	mNotification.defaults = Notification.DEFAULT_ALL;
    	mNotification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
    	mNotificationManager.notify(1,mNotification);
    }
    
    private void showMessage(String txtMsg) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	
   		builder.setTitle(R.string.app_name)
   			   .setCancelable(false)
   			   .setMessage(txtMsg)
   			   .setPositiveButton("Close", new DialogInterface.OnClickListener() {
   				   public void onClick(DialogInterface dlg, int sumthin) {
   					   dlg.cancel();
   				   }
   			   })
			   .show();
    }	
}
