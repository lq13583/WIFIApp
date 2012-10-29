package com.sunnybrook;

import java.util.Date;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
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
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class UpdateordersActivity extends ListActivity  implements  OnClickListener,OnItemClickListener,OnItemSelectedListener,OnItemLongClickListener{
	static final int UPDATEORDER_ACTIVITY_ID = 2;
	private ownOrdersAdapter mOrderAdapter;
	private ProgressDialog mProgressDialog;
	private RefreshOrderListThread mRefreshOrderListThread;
	private String mLaborCode;
	private String mOrderby = "wonum";
	private String mOrderId = "";
	private ownorder mOrder = null;
	private WIFIApp mParent;
	private TextView mStatusBar;
	public void onCreate(Bundle savedInstanceState) {

    	super.onCreate(savedInstanceState);
    	mParent = (WIFIApp) getParent();
    	mOrderby = mParent.myConfig.getOrder_update();
    	
    	mLaborCode = mParent.myConfig.getLabor_code();
    	mProgressDialog  = new ProgressDialog(this);

    	setContentView(R.layout.updateorderactivity);

/* Initialise the OrdersAdapter */
    	mOrderAdapter = new ownOrdersAdapter(this,R.layout.list_updateorder);
    	setListAdapter(mOrderAdapter);
    	

/* Initialise the OrderBy Spinner */    	
    	Spinner mSpinner = (Spinner) findViewById(R.id.spinner_sort);
    	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
    			this, R.array.own_order_array, android.R.layout.simple_spinner_item);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	mSpinner.setAdapter(adapter);
    	for(int i = 0; i< adapter.getCount(); i++) {
    		if(adapter.getItem(i).toString().equals(mOrderby))
    			mSpinner.setSelection(i);
    	}
    	mSpinner.setOnItemSelectedListener(this);
    	getListView().setOnItemClickListener(this);
    	getListView().setOnItemSelectedListener(this);
    	getListView().setOnItemLongClickListener(this);
    	
    	Button mButton = (Button) findViewById(R.id.btnRefresh);
    	mButton.setOnClickListener(this);
    	mButton = (Button) findViewById(R.id.btnOpen);
    	mButton.setOnClickListener(this);

    	mStatusBar = (TextView) findViewById(R.id.txtStatus);

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
                v = vi.inflate(R.layout.list_updateorder, null);
			}
			ownorder mS = getItem(position);
			if(mS != null) {
				TextView t = (TextView) v.findViewById(R.id.wonum);
				if(t!= null) {
					t.setText(mS.getOrderId());
					t.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mParent.myConfig.getList_font_size());
				}
				t = (TextView) v.findViewById(R.id.reportdate);
				if(t!=null) {
					t.setText(new MyDateFormat().myFormat(mS.getReportdate()));
					t.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mParent.myConfig.getList_font_size());
				}
				t = (TextView) v.findViewById(R.id.location);
				if(t!=null) {
					t.setText(mS.getLocation());
					t.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mParent.myConfig.getList_font_size());
				}
				t = (TextView) v.findViewById(R.id.wopriority);
				if(t!=null) {
					t.setText(Integer.toString(mS.getWopriority()));
					t.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mParent.myConfig.getList_font_size());
				}
				t = (TextView) v.findViewById(R.id.status);
				if(t!=null) {
					t.setText(mS.getStatus());
					t.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mParent.myConfig.getList_font_size());
				}
				t = (TextView) v.findViewById(R.id.readstatus);
				if(t!=null) {
					t.setText(mS.getReadStatus());
					t.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mParent.myConfig.getList_font_size());
				}
			}
			return v;
		}
	}

	final Handler mHandler = new Handler() {
    	public void handleMessage(Message msg) {
    		switch(msg.arg1)
    		{
    			case 0:
    				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    				mProgressDialog.setMessage("Loading data ........");
    				mProgressDialog.show();
    				break;
    			case 1:
    				break;
    			case 2:
    				mOrderAdapter.add((ownorder) msg.obj);
    	    		break;
    			default:
    				if(mProgressDialog.isShowing()){
    					mProgressDialog.dismiss();
        				locateOrder();
    				}
    				mParent.updateCountsOutstanding();
    				mParent.updateCountsUpdates();
    				if(!mParent.localdb.isDailyUpdateRequired()) {
    					TabWidget tabs = mParent.getTabWidget();
    					for(int i=0; i<tabs.getChildCount(); i++)
    						tabs.getChildAt(i).setEnabled(true);
    					mStatusBar.setTextColor(Color.WHITE);
        				mStatusBar.setText("Completed update workorders, other tabs are enabled.");
    				}
    				else {
    					mStatusBar.setTextColor(Color.RED);
    					mStatusBar.setText("Please complete workorder updates to enable other tabs!");
    				}
    		}
    	}
    };

    private void locateOrder() {
    	if(mOrderId.length() == 0) return;
		ListView lv = (ListView) findViewById(android.R.id.list);
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
			mHandler.sendMessage(msg);
	    	mItems = mParent.localdb.getOwnOrderList(mLaborcode,mOrderby);
	    	msg = mHandler.obtainMessage();
	    	msg.arg1 = 1;
	    	msg.arg2 = mItems.size() - 1;
	    	mHandler.sendMessage(msg);

	    	for(int i=0;i<mItems.size();i++) {
				if(mItems.get(i).isNeedsupdate() && !mItems.get(i).getStatus().equals("COMP")) {
					if ((mItems.get(i).getEdcompletion()== null) || mItems.get(i).getEdcompletion().before(new Date())){
						msg = mHandler.obtainMessage();
						msg.arg1 = 2;
						msg.arg2 = i;
						msg.obj = mItems.get(i);
						mHandler.sendMessage(msg);
					}
				}
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
			mOrder = (ownorder) _AdapterView.getItemAtPosition(_pos);
			mOrderId = mOrder.getOrderId();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		if(_requestCode == UPDATEORDER_ACTIVITY_ID) {
	    	refreshOrderList(mLaborCode,mOrderby);
	    	return;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> _AdapterView, View _View, int _pos, long _row) {
		switch(_AdapterView.getId()) {
			case R.id.spinner_sort:				
				mOrderby = _AdapterView.getItemAtPosition(_pos).toString();
				mParent.myConfig.setOrder_update(mOrderby);
				mParent.myConfig.saveOrderUpdateToDB(mParent.localdb);
				OwnorderComparator mComparator = new OwnorderComparator(mOrderby);
				mOrderAdapter.sort(mComparator);
				locateOrder();
				break;
			case android.R.id.list:
				mOrder = (ownorder) _AdapterView.getItemAtPosition(_pos);
				mOrderId = mOrder.getOrderId();
				TextView mText = (TextView) findViewById(R.id.txtDescription);
				mText.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mParent.myConfig.getDesc_font_size());
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

	private void openOrder(){
		if(mOrder==null) return;
/*		
		if(datasync.is_running) {
			showMessage("Data Sync is currently running, please wait until it is finished!");
			return;
		}
*/
		Intent mIntent = new Intent(this,UpdateOrderDetailActivity.class);
		mIntent.putExtra("ownorder", mOrder);
		this.startActivityForResult(mIntent, UPDATEORDER_ACTIVITY_ID);
	}
	
	public void refreshOrderList() {
    	refreshOrderList(mLaborCode,mOrderby);
	}
	
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
			case R.id.btnRefresh:
				refreshOrderList(mLaborCode,mOrderby);
				break;
			case R.id.btnOpen:
				openOrder();
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
			mOrderId = mOrder.getOrderId();
			openOrder();
			break;
		default:
			break;
		}
		return true;
	}

/*
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
*/
}
