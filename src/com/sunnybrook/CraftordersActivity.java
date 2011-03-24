package com.sunnybrook;

import java.util.Iterator;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class CraftordersActivity extends ListActivity  implements OnItemClickListener,OnItemSelectedListener{

	static private craftOrdersAdapter mOrderAdapter;
	static private ProgressDialog mProgressDialog;
	static private RefreshOrderListThread mRefreshOrderListThread;
	static private String mCraft = "";
	static private String mOrderby = "wonum";
	
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);

    	mProgressDialog  = new ProgressDialog(this);

    	setContentView(R.layout.craftorderactivity);

    	Spinner mSpinner;
    	
    	mSpinner = (Spinner) findViewById(R.id.spinner);
    	ArrayAdapter <CharSequence> adapter = new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
    	
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	mSpinner.setAdapter(adapter);
    	mSpinner.setOnItemSelectedListener(this);
		Iterator<String> itr = WIFIApp.myConfig.getCraft_list().iterator();
		while (itr.hasNext()) {
			adapter.add(itr.next());
		}
		
    	mSpinner = (Spinner) findViewById(R.id.spinner_sort);
    	adapter = ArrayAdapter.createFromResource(
    			this, R.array.craft_order_array, android.R.layout.simple_spinner_item);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	mSpinner.setAdapter(adapter);
    	mSpinner.setOnItemSelectedListener(this);

    	mOrderAdapter = new craftOrdersAdapter(this,R.layout.list_workorder);
    	setListAdapter(mOrderAdapter);
    	getListView().setOnItemClickListener(this);
    	
    	
    }

	private class RefreshOrderListThread extends Thread {
		private Handler mHandler;
		private String mCraft;
		private String mOrderby;
		private List<craftorder> mItems;
		
		public RefreshOrderListThread(String _craft, String _orderby, Handler _handler) {
			mCraft=_craft;
			mOrderby = _orderby;
			mHandler = _handler;
		}
		
		public void run() {
			Message msg = mHandler.obtainMessage();
			msg.arg1 = 0;
			mHandler.sendMessage(msg);
	    	mItems = WIFIApp.localdb.getCraftOrderList(mCraft,mOrderby);
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
    
	public class craftOrdersAdapter extends ArrayAdapter<craftorder> {

		public craftOrdersAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if(v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_workorder, null);
			}
			craftorder mS = getItem(position);
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
				t = (TextView) v.findViewById(R.id.laborcode);
				if(t!=null) t.setText(mS.getLaborCode());
				t = (TextView) v.findViewById(R.id.laborname);
				if(t!=null) t.setText(mS.getLaborName());
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
    				mOrderAdapter.add((craftorder) msg.obj);
    	    		break;
    			default:
    				if(mProgressDialog.isShowing()) mProgressDialog.dismiss();
    		}
    	}
    };

    private void refreshOrderList(String _craft,String _orderby) {
    	if (mRefreshOrderListThread != null)
    		if(mRefreshOrderListThread.isAlive())
    			return;
    	mRefreshOrderListThread = new RefreshOrderListThread(_craft,_orderby,mHandler);
		mOrderAdapter.clear();
		mRefreshOrderListThread.start();
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemSelected(AdapterView<?> _AdapterView, View _View, int _pos, long _row) {
		switch(_AdapterView.getId()) {
			case R.id.spinner:
				mCraft = _AdapterView.getItemAtPosition(_pos).toString();
				refreshOrderList(mCraft,mOrderby);
				break;
			case R.id.spinner_sort:				
				mOrderby = _AdapterView.getItemAtPosition(_pos).toString();
				CraftorderComparator mComparator = new CraftorderComparator(mOrderby);
				mOrderAdapter.sort(mComparator);
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
}
