package com.sunnybrook;

import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class UpdateordersActivity extends ListActivity  implements  OnClickListener,OnItemClickListener,OnItemSelectedListener,OnItemLongClickListener{
	static final int UPDATEORDER_ACTIVITY_ID = 2;
	static private ownOrdersAdapter mOrderAdapter;
	static private ProgressDialog mProgressDialog;
	static private RefreshOrderListThread mRefreshOrderListThread;
	static private String mLaborCode;
	static private String mOrderby = "wonum";
	private String mOrderId = "";
	private WIFIApp mParent;
	public void onCreate(Bundle savedInstanceState) {

    	super.onCreate(savedInstanceState);
    	mParent = (WIFIApp) getParent();
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
    	mSpinner.setOnItemSelectedListener(this);
    	getListView().setOnItemClickListener(this);
    	getListView().setOnItemLongClickListener(this);
    	
    	Button mButton = (Button) findViewById(R.id.btnRefresh);
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
                v = vi.inflate(R.layout.list_updateorder, null);
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
				if(mItems.get(i).isNeedsupdate()) {
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
			ownorder mItem = (ownorder) _AdapterView.getItemAtPosition(_pos);
			mOrderId = mItem.getOrderId();
			Intent mIntent = new Intent(this,UpdateOrderDetailActivity.class);
			mIntent.putExtra("ownorder", mItem);
			this.startActivityForResult(mIntent, UPDATEORDER_ACTIVITY_ID);
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
				OwnorderComparator mComparator = new OwnorderComparator(mOrderby);
				mOrderAdapter.sort(mComparator);
				locateOrder();
				break;
			case android.R.id.list:
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
    	refreshOrderList(mLaborCode,mOrderby);
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> _AdapterView, View arg1, int _pos,long arg3) {
		switch(_AdapterView.getId()) {
		case android.R.id.list:
			ownorder mItem = (ownorder) _AdapterView.getItemAtPosition(_pos);
			showMessage(mItem.getComments());
			break;
		default:
			break;
		}
		return true;
	}
}
