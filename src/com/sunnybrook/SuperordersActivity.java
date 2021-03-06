package com.sunnybrook;

import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
//import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class SuperordersActivity  extends ListActivity  implements OnClickListener,OnItemClickListener,OnItemSelectedListener,OnItemLongClickListener{
	private superLaborsAdapter mLaborAdapter;
	private superOrdersAdapter mOrderAdapter;
	private ProgressDialog mProgressDialog;
	private RefreshOrderListThread mRefreshOrderListThread;
	private String mLaborCode = "";
	private String mOrderby = "wonum";
	private superorder mOrder = null;
	private WIFIApp mParent;
	
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	mParent = (WIFIApp) getParent();
    	mOrderby = mParent.myConfig.getOrder_super();

    	mProgressDialog  = new ProgressDialog(this);
		
    	setContentView(R.layout.superorderactivity);
    	
    	Spinner mSpinner = (Spinner) findViewById(R.id.spinner);
    	mLaborAdapter = new superLaborsAdapter(this,R.layout.list_superlabors);
    	mLaborAdapter.setDropDownViewResource(R.layout.list_superlabors);
    	mSpinner.setAdapter(mLaborAdapter);
    	mSpinner.setOnItemSelectedListener(this);
    	
    	mSpinner = (Spinner) findViewById(R.id.spinner_sort);
    	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
    			this, R.array.super_order_array, android.R.layout.simple_spinner_item);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	mSpinner.setAdapter(adapter);
    	for(int i = 0; i< adapter.getCount(); i++) {
    		if(adapter.getItem(i).toString().equals(mOrderby))
    			mSpinner.setSelection(i);
    	}
    	mSpinner.setOnItemSelectedListener(this);
    	
    	mOrderAdapter = new superOrdersAdapter(this,R.layout.list_superorder);
    	setListAdapter(mOrderAdapter);
    	getListView().setOnItemClickListener(this);
    	getListView().setOnItemSelectedListener(this);
    	getListView().setOnItemLongClickListener(this);
    	refreshLaborList(mParent.localdb.getLaborList());

    	Button mButton = (Button) findViewById(R.id.btnOpen);
    	mButton.setOnClickListener(this);
    	
    	}

	private void refreshLaborList(List<labor> _Items) {
    	mLaborAdapter.clear();
    	for(int i=0;i<_Items.size();i++)
    		mLaborAdapter.add(_Items.get(i));
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
    				mOrderAdapter.add((superorder) msg.obj);
    	    		break;
    			default:
    				if(mProgressDialog.isShowing()) mProgressDialog.dismiss();
    		}
    	}
    };

    public void refreshOrderList() {
    	if(mLaborCode.equals("")) return;
    	refreshOrderList(mLaborCode,mOrderby);
    }
    
    private void refreshOrderList(String _laborcode,String _orderby) {
    	if (mRefreshOrderListThread != null)
    		if(mRefreshOrderListThread.isAlive())
    			return;
    	mRefreshOrderListThread = new RefreshOrderListThread(_laborcode,_orderby,mHandler);
		mOrderAdapter.clear();
		mRefreshOrderListThread.start();
	}

	private class RefreshOrderListThread extends Thread {
		private Handler mHandler;
		private String mLaborcode;
		private String mOrderby;
		private List<superorder> mItems;
		
		public RefreshOrderListThread(String _laborcode, String _orderby, Handler _handler) {
			mLaborcode=_laborcode;
			mOrderby = _orderby;
			mHandler = _handler;
		}
		
		public void run() {
			Message msg = mHandler.obtainMessage();
			msg.arg1 = 0;
			mHandler.sendMessage(msg);
	    	mItems = mParent.localdb.getSuperOrderList(mLaborcode,mOrderby);
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
	
	public class superLaborsAdapter extends ArrayAdapter<labor> {

		public superLaborsAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
			
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			return getCustomView(position, convertView, parent);
		}
		
		@Override
		public View getDropDownView(int position, View convertView,   ViewGroup parent)
		{  
			return getCustomView(position, convertView, parent);
		}
		
		public View getCustomView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if(v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_superlabors, null);
			}
			labor mS = getItem(position);
			if(mS != null) {
				TextView t = (TextView) v.findViewById(R.id.laborcode);
				if(t!= null) t.setText(mS.getCode());
				t = (TextView) v.findViewById(R.id.laborname);
				if(t!=null) t.setText(mS.getName());
			}
			return v;
		}
	}
	
	public class superOrdersAdapter extends ArrayAdapter<superorder> {

		public superOrdersAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if(v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_superorder, null);
			}
			workorder mS = getItem(position);
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
			}
			return v;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> _AdapterView, View _View, int _pos, long _row) {
		switch(_AdapterView.getId()) {
			case R.id.spinner:
				labor mItem = (labor) _AdapterView.getItemAtPosition(_pos);
				mLaborCode = mItem.getCode();
				refreshOrderList(mLaborCode,mOrderby);
				break;
			case R.id.spinner_sort:				
				mOrderby = _AdapterView.getItemAtPosition(_pos).toString();
				mParent.myConfig.setOrder_super(mOrderby);
				mParent.myConfig.saveOrderSuperToDB(mParent.localdb);
				WorkorderComparator mComparator = new WorkorderComparator(mOrderby);
				mOrderAdapter.sort(mComparator);
				break;
			case android.R.id.list:
				mOrder = (superorder) _AdapterView.getItemAtPosition(_pos);
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
	
	@Override
	public void onItemClick(AdapterView<?> _AdapterView, View arg1, int _pos, long arg3) {
		switch(_AdapterView.getId()) {
		case android.R.id.list:
			ListView lv = (ListView) _AdapterView;
			lv.requestFocusFromTouch();
			lv.setSelection(_pos);
			mOrder = (superorder) _AdapterView.getItemAtPosition(_pos);
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
			mOrder = (superorder) _AdapterView.getItemAtPosition(_pos);
			Intent mIntent = new Intent(this,SuperOrderDetailActivity.class);
			mIntent.putExtra("superorder", mOrder);
			this.startActivity(mIntent);
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()) {
			case R.id.btnOpen:
				if(mOrder==null) return;
				Intent mIntent = new Intent(this,SuperOrderDetailActivity.class);
				mIntent.putExtra("superorder", mOrder);
				this.startActivity(mIntent);
				break;
			default:	
				break;
		}
	}
	
}

