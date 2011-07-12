package com.sunnybrook;

import java.util.Iterator;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class CraftordersActivity extends ListActivity  implements OnItemClickListener,OnItemSelectedListener,OnItemLongClickListener{
	static final int OWNORDER_ACTIVITY_ID = 1;
	static final int CRAFTORDER_ACTIVITY_ID = 2;
	final static int RESULT_CLOSE_ID = 0;
	final static int RESULT_OPEN_ID = 1;
	
	static private craftOrdersAdapter mOrderAdapter;
	static private ProgressDialog mProgressDialog;
	static private RefreshOrderListThread mRefreshOrderListThread;
	static private String mCraft = "";
	static private String mOrderby = "wonum";
	
	private WIFIApp mParent;
	
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	mParent = (WIFIApp) getParent();

    	mProgressDialog  = new ProgressDialog(this);

    	setContentView(R.layout.craftorderactivity);

    	Spinner mSpinner;
    	
    	mSpinner = (Spinner) findViewById(R.id.spinner);
    	ArrayAdapter <CharSequence> adapter = new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
    	
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	mSpinner.setAdapter(adapter);
    	mSpinner.setOnItemSelectedListener(this);
		Iterator<String> itr = mParent.myConfig.getCraft_list().iterator();
		while (itr.hasNext()) {
			adapter.add(itr.next());
		}
		
    	mSpinner = (Spinner) findViewById(R.id.spinner_sort);
    	adapter = ArrayAdapter.createFromResource(
    			this, R.array.craft_order_array, android.R.layout.simple_spinner_item);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	mSpinner.setAdapter(adapter);
    	mSpinner.setOnItemSelectedListener(this);

    	mOrderAdapter = new craftOrdersAdapter(this,R.layout.list_craftorder);
    	setListAdapter(mOrderAdapter);
    	getListView().setOnItemClickListener(this);
    	getListView().setOnItemLongClickListener(this);    	
    	
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
	    	mItems = mParent.localdb.getCraftOrderList(mCraft,mOrderby);
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
                v = vi.inflate(R.layout.list_craftorder, null);
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

	@Override
	public void onItemClick(AdapterView<?> _AdapterView, View arg1, int _pos, long arg3) {
		switch(_AdapterView.getId()) {
		case android.R.id.list:
			craftorder mItem = (craftorder) _AdapterView.getItemAtPosition(_pos);
			Intent mIntent = new Intent(this,CraftOrderDetailActivity.class);
			mIntent.putExtra("craftorder", mItem);
			mIntent.putExtra("fontsize", mParent.myConfig.getFont_size());
			mIntent.putExtra("descfontsize", mParent.myConfig.getDesc_font_size());
			this.startActivityForResult(mIntent,CRAFTORDER_ACTIVITY_ID);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> _AdapterView, View arg1, int _pos,long arg3) {
		switch(_AdapterView.getId()) {
		case android.R.id.list:
			craftorder mItem = (craftorder) _AdapterView.getItemAtPosition(_pos);
			showMessage(mItem.getComments());
			break;
		default:
			break;
		}
		return true;
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
	public void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		switch (_requestCode) {
			case CRAFTORDER_ACTIVITY_ID:
				if(_resultCode == RESULT_OPEN_ID){
					craftorder mCraftOrder = (craftorder) _data.getSerializableExtra("craftorder");
					ownorder mOwnOrder = mParent.localdb.Craft2Own(mCraftOrder, mParent.myConfig.getLabor_code());
					if(mOwnOrder!= null) {
						Intent mIntent = new Intent(this,OwnOrderDetailActivity.class);
						mIntent.putExtra("ownorder", mOwnOrder);
						this.startActivityForResult(mIntent, OWNORDER_ACTIVITY_ID);
					}
				}
				break;
			case OWNORDER_ACTIVITY_ID:
				refreshOrderList(mCraft,mOrderby);
				break;
			default: break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
