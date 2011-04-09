package com.sunnybrook;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class OwnOrderDetailActivity extends Activity implements OnClickListener,OnItemSelectedListener,OnItemClickListener{
	static final int DATE_START_ID = 0;
	static final int DATE_FINISH_ID = 1;
	static final int DATE_TRANS_ID = 2;
	private MyDateFormat mDateFormat = new MyDateFormat("yyyy-MM-dd");
	private DatePickerDialog mDatePicker;
	private ownorder mOrder;
	private int mDateType = 0;
	private int mYear;
	private int mMonth;
	private int mDay;

	private labtrans mLabTrans;
	private LabTransAdapter mLabTransAdapter;
	private int mPos = -1;
	
	public void onCreate(Bundle savedInstanceState) {
		SyncDataTask.mEnabled = false;	//Disable the Sync Data Task;
    	setTitle("Own Order Detail");
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.ownorderdetailactivity);
    	mOrder = (ownorder) getIntent().getSerializableExtra("ownorder");
    	TextView mTextView = (TextView) findViewById(R.id.wonum);
    	mTextView.setText(mOrder.getOrderId());
    	mTextView = (TextView) findViewById(R.id.reportdate);
    	mTextView.setText(WIFIApp.myDateFormat.myFormat(mOrder.getReportdate()));
    	mTextView = (TextView) findViewById(R.id.reportedby);
    	mTextView.setText(mOrder.getReportedby());
    	mTextView = (TextView) findViewById(R.id.wo3);
    	mTextView.setText(mOrder.getWo3());
    	mTextView = (TextView) findViewById(R.id.phone);
    	mTextView.setText(mOrder.getPhone());
    	mTextView = (TextView) findViewById(R.id.location);
    	mTextView.setText(mOrder.getLocation());
    	mTextView = (TextView) findViewById(R.id.locationdesc);
    	mTextView.setText(mOrder.getLocationdesc());
    	mTextView = (TextView) findViewById(R.id.comments);
    	mTextView.setText(mOrder.getComments());
    	mTextView = (TextView) findViewById(R.id.empcomments);
    	mTextView.setText(mOrder.getEmpcomments());
    	mTextView = (TextView) findViewById(R.id.khname);
    	mTextView.setText(mOrder.getKhname());
    	mTextView = (TextView) findViewById(R.id.ktitle);
    	mTextView.setText(mOrder.getKtitle());
    	mTextView = (TextView) findViewById(R.id.kdept);
    	mTextView.setText(mOrder.getKdept());
    	mTextView = (TextView) findViewById(R.id.kcamp);
    	mTextView.setText(mOrder.getKcamp());
    	mTextView = (TextView) findViewById(R.id.kcost);
    	mTextView.setText(mOrder.getKcost());
    	mTextView = (TextView) findViewById(R.id.kcod1);
    	mTextView.setText(mOrder.getKcod1());
    	mTextView = (TextView) findViewById(R.id.kcod2);
    	mTextView.setText(mOrder.getKcod2());
    	mTextView = (TextView) findViewById(R.id.kcod3);
    	mTextView.setText(mOrder.getKcod3());
    	mTextView = (TextView) findViewById(R.id.kcod4);
    	mTextView.setText(mOrder.getKcod4());
    	mTextView = (TextView) findViewById(R.id.kcodr1);
    	mTextView.setText(mOrder.getKcodr1());
    	mTextView = (TextView) findViewById(R.id.kcodr2);
    	mTextView.setText(mOrder.getKcodr2());
    	mTextView = (TextView) findViewById(R.id.kcodr3);
    	mTextView.setText(mOrder.getKcodr3());
    	mTextView = (TextView) findViewById(R.id.kcodr4);
    	mTextView.setText(mOrder.getKcodr4());
    	mTextView = (TextView) findViewById(R.id.kcor1);
    	mTextView.setText(mOrder.getKcor1());
    	mTextView = (TextView) findViewById(R.id.kcor2);
    	mTextView.setText(mOrder.getKcor2());
    	mTextView = (TextView) findViewById(R.id.kcor3);
    	mTextView.setText(mOrder.getKcor3());
    	mTextView = (TextView) findViewById(R.id.kcor4);
    	mTextView.setText(mOrder.getKcor4());
    	mTextView = (TextView) findViewById(R.id.kq1);
    	mTextView.setText(mOrder.getKq1());
    	mTextView = (TextView) findViewById(R.id.kq2);
    	mTextView.setText(mOrder.getKq2());
    	mTextView = (TextView) findViewById(R.id.kq3);
    	mTextView.setText(mOrder.getKq3());
    	mTextView = (TextView) findViewById(R.id.kq4);
    	mTextView.setText(mOrder.getKq4());
    	mTextView = (TextView) findViewById(R.id.kcom);
    	mTextView.setText(mOrder.getKcom());
    	
    	EditText mEditText = (EditText) findViewById(R.id.mycomment);
    	mEditText.setText(mOrder.getMyComments());
    	mEditText = (EditText) findViewById(R.id.labor_code);
    	mEditText.setText(WIFIApp.myConfig.getLabor_code());

    	Spinner mSpinner = (Spinner) findViewById(R.id.status);
    	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
    			this, R.array.order_status_array, android.R.layout.simple_spinner_item);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	mSpinner.setAdapter(adapter);
    	mSpinner.setSelection(adapter.getPosition(mOrder.getStatus()));
    	mSpinner.setOnItemSelectedListener(this);

    	mSpinner = (Spinner) findViewById(R.id.readstatus);
    	adapter = ArrayAdapter.createFromResource(
    			this, R.array.order_readstatus_array, android.R.layout.simple_spinner_item);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	mSpinner.setAdapter(adapter);
    	mSpinner.setSelection(adapter.getPosition(mOrder.getReadStatus()));
    	mSpinner.setOnItemSelectedListener(this);

    	Button mButton = (Button) findViewById(R.id.btnClose);
    	mButton.setOnClickListener(this);
    	mButton = (Button) findViewById(R.id.btnAdd);
    	mButton.setOnClickListener(this);
    	mButton = (Button) findViewById(R.id.btnMod);
    	mButton.setOnClickListener(this);
    	mButton = (Button) findViewById(R.id.btnDel);
    	mButton.setOnClickListener(this);
    	mButton = (Button) findViewById(R.id.btnActstart);
    	mButton.setText("Start Date: " + mDateFormat.myFormat(mOrder.getActstart()));
    	mButton.setOnClickListener(this);
    	mButton = (Button) findViewById(R.id.btnActfinish);
    	mButton.setText("Complete Date: " + mDateFormat.myFormat(mOrder.getActfinish()));
    	mButton.setOnClickListener(this);
    	mButton = (Button) findViewById(R.id.btnTransdate);
    	mButton.setText(mDateFormat.myFormat(new Date()));
    	mButton.setOnClickListener(this);
    	
    	mLabTransAdapter = new LabTransAdapter(this,R.layout.list_labtrans);    	
    	ListView mListView = (ListView) findViewById(R.id.lvlabtrans);
    	mListView.setAdapter(mLabTransAdapter);
    	mListView.setOnItemClickListener(this);
    	
    	refreshLabTransList();
    	
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, 
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDate();
                }

            };

    private void updateDate(){
    	Button mButton;
    	Date mDate = new Date(mYear-1900,mMonth,mDay);
    	switch(mDateType) {
    		case DATE_START_ID:
    			mButton =(Button) findViewById(R.id.btnActstart);
    			mButton.setText("Start Date: " + mDateFormat.myFormat(mDate));
    			mOrder.setActstart(mDate);
    			WIFIApp.localdb.updateActstart(mOrder);
    			break;
    		case DATE_FINISH_ID:
    			mButton =(Button) findViewById(R.id.btnActfinish);
    			mButton.setText("Complete Date: " + mDateFormat.myFormat(mDate));
    			mOrder.setActfinish(mDate);
    			WIFIApp.localdb.updateActfinish(mOrder);
    			break;
    		case DATE_TRANS_ID:
    			mButton =(Button) findViewById(R.id.btnTransdate);
    			mButton.setText(mDateFormat.myFormat(mDate));
    			break;
    	}
    }

    @Override
	public void onClick(View _view) {
		// TODO Auto-generated method stub
    	Date mDate;
    	EditText mEditText;
    	String mLaborcode;
    	float mHrs;
    	Button mButton;
		Date mTransDate;
    	
    	switch(_view.getId()) {
    		case R.id.btnClose :
    			this.finish();
    			break;
    		case R.id.btnDel:
    			if(mPos < 0) return;
    			if(mLabTrans == null) return;
				mOrder.getTranslist().remove(mPos);
				WIFIApp.localdb.deleteLabTrans(mLabTrans);
				mLabTrans = null;
				refreshLabTransList();
    			break;
    		case R.id.btnMod:
    			mEditText = (EditText) findViewById(R.id.labor_code);
    			mLaborcode = mEditText.getText().toString();
    			if(mLaborcode.equals("")) return;
    			mEditText = (EditText) findViewById(R.id.hrs);

    			mHrs = Float.parseFloat(mEditText.getText().toString());
    			if (mHrs == 0) return;
    			mButton = (Button) findViewById(R.id.btnTransdate);
    			try {
    				mTransDate = mDateFormat.myParse(mButton.getText().toString());
    			} catch (ParseException e1) {
    				// TODO Auto-generated catch block
    				return;
    			}
    			if(mLabTrans == null)
    				return;
    			else {
    				mLabTrans.setLaborCode(mLaborcode);
    				mLabTrans.setStartDate(mTransDate);
    				mLabTrans.setRegularHrs(mHrs);
    			}
    			refreshLabTransList();
    			break;
    		case R.id.btnAdd:
    			mEditText = (EditText) findViewById(R.id.labor_code);
    			mLaborcode = mEditText.getText().toString();
    			if(mLaborcode.equals("")) return;
    			mEditText = (EditText) findViewById(R.id.hrs);

    			mHrs = Float.parseFloat(mEditText.getText().toString());
    			if (mHrs == 0) return;
    			mButton = (Button) findViewById(R.id.btnTransdate);
    			try {
    				mTransDate = mDateFormat.myParse(mButton.getText().toString());
    			} catch (ParseException e1) {
    				// TODO Auto-generated catch block
    				return;
    			}
    			if(mLabTrans == null)
    				mLabTrans = new labtrans(mLaborcode, mTransDate, mHrs, mOrder.getOrderId(), mOrder.getLocation());
    			else {
    				mLabTrans.setLaborCode(mLaborcode);
    				mLabTrans.setStartDate(mTransDate);
    				mLabTrans.setRegularHrs(mHrs);
    			}
    				
    			mOrder.addLabTrans(mLabTrans);
    			mLabTrans = null;
    			refreshLabTransList();
    			break;
    		case R.id.btnActstart:
    			mDateType = DATE_START_ID;
    			mDate = mOrder.getActstart();
    			if(mDate == null) {
    		        final Calendar c = Calendar.getInstance();
    		        mYear = c.get(Calendar.YEAR);
    		        mMonth = c.get(Calendar.MONTH);
    		        mDay = c.get(Calendar.DAY_OF_MONTH);
       			}
    			else {
    				mYear = mDate.getYear() + 1900;
    				mMonth = mDate.getMonth();
    				mDay = mDate.getDate();
    			}
    			if(mDatePicker == null)
    				mDatePicker = new DatePickerDialog(this,mDateSetListener,mYear,mMonth,mDay);
    			else
    				mDatePicker.updateDate(mYear, mMonth, mDay);
    			mDatePicker.show();
    			break;
    		case R.id.btnActfinish:
    			mDateType = DATE_FINISH_ID;
    			mDate = mOrder.getActfinish();
    			if(mDate == null) {
    		        final Calendar c = Calendar.getInstance();
    		        mYear = c.get(Calendar.YEAR);
    		        mMonth = c.get(Calendar.MONTH);
    		        mDay = c.get(Calendar.DAY_OF_MONTH);
       			}
    			else {
    				mYear = mDate.getYear() + 1900;
    				mMonth = mDate.getMonth();
    				mDay = mDate.getDate();
    			}
    			if(mDatePicker == null)
    				mDatePicker = new DatePickerDialog(this,mDateSetListener,mYear,mMonth,mDay);
    			else
    				mDatePicker.updateDate(mYear, mMonth, mDay);
    			mDatePicker.show();
//    			showDialog(DATE_DIALOG_ID);
    			break;
    		case R.id.btnTransdate:
    			mDateType = DATE_TRANS_ID;
    			try {
					mDate = mDateFormat.myParse(((Button) _view).getText().toString());
				} catch (ParseException e) {
				// TODO Auto-generated catch block
					mDate = null;
				}
    			if(mDate == null) {
    		        final Calendar c = Calendar.getInstance();
    		        mYear = c.get(Calendar.YEAR);
    		        mMonth = c.get(Calendar.MONTH);
    		        mDay = c.get(Calendar.DAY_OF_MONTH);
       			}
    			else {
    				mYear = mDate.getYear() + 1900;
    				mMonth = mDate.getMonth();
    				mDay = mDate.getDate();
    			}
    			if(mDatePicker == null)
    				mDatePicker = new DatePickerDialog(this,mDateSetListener,mYear,mMonth,mDay);
    			else
    				mDatePicker.updateDate(mYear, mMonth, mDay);
    			mDatePicker.show();
    			break;
    		default:
    			break;
    	}
	}

/*
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_DIALOG_ID:
        	if(mDatePicker == null) {
              mDatePicker = new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
        	}
        	else {
        		mDatePicker.updateDate(mYear, mMonth, mDay);
        	}
        	return mDatePicker;
        }
        return null;
    }
*/
    
    @Override
    public void finish() {
    	EditText mText = (EditText) findViewById(R.id.mycomment);
    	if(!mOrder.getMyComments().equals(mText.getText().toString())) {
    		mOrder.setMyComments(mText.getText().toString());
    		WIFIApp.localdb.updateMyComment(mOrder);
    	}
    	WIFIApp.localdb.updateLabTransList(mOrder);
    	Intent resultIntent = new Intent();
    	resultIntent.putExtra("ownorder", mOrder);
    	setResult(0,resultIntent);
    	SyncDataTask.mEnabled = true;	//Re-enable the Sync Data Task
    	super.finish();
    }
    
	@Override
	public void onItemSelected(AdapterView<?> _AdapterView, View _View, int _pos, long _row) {
		switch(_AdapterView.getId()) {
			case R.id.status:
				mOrder.setStatus(_AdapterView.getItemAtPosition(_pos).toString());
				WIFIApp.localdb.updateStatus(mOrder);
				break;
			case R.id.readstatus:				
				mOrder.setReadStatus(_AdapterView.getItemAtPosition(_pos).toString());
				WIFIApp.localdb.updateReadStatus(mOrder);
				break;
			default:
				break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public class LabTransAdapter extends ArrayAdapter<labtrans> {

		public LabTransAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if(v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_labtrans, null);
			}
			labtrans mS = getItem(position);
			if(mS != null) {
				TextView t = (TextView) v.findViewById(R.id.labcode);
				if(t!= null) t.setText(mS.getLaborCode());
				t = (TextView) v.findViewById(R.id.labdate);
				if(t!=null)
					t.setText(mDateFormat.myFormat(mS.getStartDate()));
				t = (TextView) v.findViewById(R.id.labhrs);
				if(t!=null) t.setText(Float.toString(mS.getRegularHrs()));
			}
			return v;
		}
	}
    
	public void refreshLabTransList(){
		mPos = -1;
		List<labtrans> mItems = mOrder.getTranslist();
		if(mItems==null) return;
		mLabTransAdapter.clear();
		for(int i=0; i < mItems.size();i++) {
			mLabTransAdapter.add(mItems.get(i));
		}
	}

	@Override
	public void onItemClick(AdapterView<?> _AdapterView, View _View, int _pos, long arg3) {
		switch(_AdapterView.getId()) {
			case R.id.lvlabtrans:
				ListView lv = (ListView) findViewById(R.id.lvlabtrans);
				lv.requestFocusFromTouch();
				lv.setSelection(_pos);
				mLabTrans = (labtrans) _AdapterView.getItemAtPosition(_pos);
    			EditText mEditText = (EditText) findViewById(R.id.labor_code);
    			mEditText.setText(mLabTrans.getLaborCode());
    			mEditText = (EditText) findViewById(R.id.hrs);
    			mEditText.setText(Float.toString(mLabTrans.getRegularHrs()));
    			Button mButton = (Button) findViewById(R.id.btnTransdate);
    			mButton.setText(mDateFormat.myFormat(mLabTrans.getStartDate()));
    			mPos = _pos;
/*			
				mOrder.getTranslist().remove(_pos);
				refreshLabTransList();
*/
			default: break;
		}
		// TODO Auto-generated method stub
		
	}
}
