package com.sunnybrook;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class UpdateOrderDetailActivity extends Activity implements OnClickListener,OnItemSelectedListener{
	static final int DATE_EDCOMPLETE_ID = 0;
	private MyDateFormat mDateFormat = new MyDateFormat("yyyy-MM-dd");
	private MyDateFormat myDateFormat = new MyDateFormat();
	private DatePickerDialog mDatePicker;
	private ownorder mOrder;
	private int mYear;
	private int mMonth;
	private int mDay;
	private float mFontSize = 0;
	private float mDescFontSize = 0;

	private MainApp mApp;
	private sysconfig myConfig;
	private localDB mLocalDB;

	private Spinner mSpinner;

	public void onCreate(Bundle savedInstanceState) {
		SyncDataTask.mEnabled = false;	//Disable the Sync Data Task;
    	setTitle("Update Outstanding Order Detail");
    	super.onCreate(savedInstanceState);
    	
    	mApp = (MainApp) getApplication();
    	myConfig = mApp.getMySysConfig();
    	mLocalDB = mApp.getMyLocalDB(); 
    	mFontSize = myConfig.getFont_size();
    	mDescFontSize = myConfig.getDesc_font_size();
    	
    	setContentView(R.layout.updateorderdetailactivity);
    	mOrder = (ownorder) getIntent().getSerializableExtra("ownorder");
    	TextView mTextView = (TextView) findViewById(R.id.wonum);
    	mTextView.setText(mOrder.getOrderId());
    	mTextView.setTextSize(mFontSize);
    	mTextView = (TextView) findViewById(R.id.reportdate);
    	mTextView.setText(myDateFormat.myFormat(mOrder.getReportdate()));
    	mTextView.setTextSize(mFontSize);
    	mTextView = (TextView) findViewById(R.id.reportedby);
    	mTextView.setText(mOrder.getReportedby());
    	mTextView.setTextSize(mFontSize);
    	mTextView = (TextView) findViewById(R.id.wo3);
    	mTextView.setText(mOrder.getWo3());
    	mTextView.setTextSize(mFontSize);
    	mTextView = (TextView) findViewById(R.id.phone);
    	mTextView.setText(mOrder.getPhone());
    	mTextView.setTextSize(mFontSize);
    	mTextView = (TextView) findViewById(R.id.location);
    	mTextView.setText(mOrder.getLocation());
    	mTextView.setTextSize(mFontSize);
    	mTextView = (TextView) findViewById(R.id.locationdesc);
    	mTextView.setText(mOrder.getLocationdesc());
    	mTextView.setTextSize(mFontSize);
    	mTextView = (TextView) findViewById(R.id.comments);
    	mTextView.setText(mOrder.getComments());
    	mTextView.setTextSize(mDescFontSize);
    	mTextView = (TextView) findViewById(R.id.empcomments);
    	mTextView.setText(mOrder.getEmpcomments());
    	mTextView.setTextSize(mFontSize);
    	mTextView = (TextView) findViewById(R.id.khname);
    	mTextView.setText(mOrder.getKhname());
    	mTextView.setTextSize(mFontSize);
    	mTextView = (TextView) findViewById(R.id.ktitle);
    	mTextView.setText(mOrder.getKtitle());
    	mTextView.setTextSize(mFontSize);
    	mTextView = (TextView) findViewById(R.id.kdept);
    	mTextView.setText(mOrder.getKdept());
    	mTextView.setTextSize(mFontSize);
    	mTextView = (TextView) findViewById(R.id.kcamp);
    	mTextView.setText(mOrder.getKcamp());
    	mTextView.setTextSize(mFontSize);
    	mTextView = (TextView) findViewById(R.id.kcost);
    	mTextView.setText(mOrder.getKcost());
    	mTextView.setTextSize(mFontSize);
    	mTextView = (TextView) findViewById(R.id.kcod1);
    	mTextView.setText(mOrder.getKcod1());
    	mTextView.setTextSize(mFontSize);
    	mTextView = (TextView) findViewById(R.id.kcod2);
    	mTextView.setText(mOrder.getKcod2());
    	mTextView.setTextSize(mFontSize);
    	mTextView = (TextView) findViewById(R.id.kcod3);
    	mTextView.setText(mOrder.getKcod3());
    	mTextView.setTextSize(mFontSize);
    	mTextView = (TextView) findViewById(R.id.kcod4);
    	mTextView.setText(mOrder.getKcod4());
    	mTextView.setTextSize(mFontSize);
    	mTextView = (TextView) findViewById(R.id.kcodr1);
    	mTextView.setText(mOrder.getKcodr1());
    	mTextView.setTextSize(mFontSize);
    	mTextView = (TextView) findViewById(R.id.kcodr2);
    	mTextView.setText(mOrder.getKcodr2());
    	mTextView.setTextSize(mFontSize);
    	mTextView = (TextView) findViewById(R.id.kcodr3);
    	mTextView.setText(mOrder.getKcodr3());
    	mTextView.setTextSize(mFontSize);
    	mTextView = (TextView) findViewById(R.id.kcodr4);
    	mTextView.setText(mOrder.getKcodr4());
    	mTextView.setTextSize(mFontSize);
    	mTextView = (TextView) findViewById(R.id.kcor1);
    	mTextView.setText(mOrder.getKcor1());
    	mTextView.setTextSize(mFontSize);
    	mTextView = (TextView) findViewById(R.id.kcor2);
    	mTextView.setText(mOrder.getKcor2());
    	mTextView.setTextSize(mFontSize);
    	mTextView = (TextView) findViewById(R.id.kcor3);
    	mTextView.setText(mOrder.getKcor3());
    	mTextView.setTextSize(mFontSize);
    	mTextView = (TextView) findViewById(R.id.kcor4);
    	mTextView.setText(mOrder.getKcor4());
    	mTextView.setTextSize(mFontSize);
    	mTextView = (TextView) findViewById(R.id.kq1);
    	mTextView.setText(mOrder.getKq1());
    	mTextView.setTextSize(mFontSize);
    	mTextView = (TextView) findViewById(R.id.kq2);
    	mTextView.setText(mOrder.getKq2());
    	mTextView.setTextSize(mFontSize);
    	mTextView = (TextView) findViewById(R.id.kq3);
    	mTextView.setText(mOrder.getKq3());
    	mTextView.setTextSize(mFontSize);
    	mTextView = (TextView) findViewById(R.id.kq4);
    	mTextView.setText(mOrder.getKq4());
    	mTextView.setTextSize(mFontSize);
    	mTextView = (TextView) findViewById(R.id.kcom);
    	mTextView.setText(mOrder.getKcom());
    	mTextView.setTextSize(mFontSize);
    	
    	EditText mEditText = (EditText) findViewById(R.id.mycomment);
    	mEditText.setText(mOrder.getRfdcomments());

    	mSpinner = (Spinner) findViewById(R.id.spinRFD);
    	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
    			this, R.array.delay_reason_array, android.R.layout.simple_spinner_item);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	mSpinner.setAdapter(adapter);
    	mSpinner.setSelection(adapter.getPosition(mOrder.getDelayreason()));
    	mSpinner.setOnItemSelectedListener(this);


    	Button mButton = (Button) findViewById(R.id.btnClose);
    	mButton.setOnClickListener(this);
    	mButton = (Button) findViewById(R.id.btnEdComplete);
    	mButton.setText(mDateFormat.myFormat(mOrder.getEdcompletion()));
    	mButton.setOnClickListener(this);
    	
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
		mButton =(Button) findViewById(R.id.btnEdComplete);
		mButton.setText(mDateFormat.myFormat(mDate));
		mOrder.setEdcompletion(mDate);
    }

    @Override
	public void onClick(View _view) {
		// TODO Auto-generated method stub
    	Date mDate;
    	
    	switch(_view.getId()) {
    		case R.id.btnClose :
    			this.finish();
    			break;
    		case R.id.btnEdComplete:
    			mDate = mOrder.getEdcompletion();
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

    @Override
    public void finish() {
    	EditText mText = (EditText) findViewById(R.id.mycomment);
    	if(!mOrder.getRfdcomments().equals(mText.getText().toString()))
    		mOrder.setRfdcomments(mText.getText().toString());
    	
   		mLocalDB.updateRFDInfo(mOrder);
    	Intent resultIntent = new Intent();
    	resultIntent.putExtra("ownorder", mOrder);
    	setResult(0,resultIntent);
    	SyncDataTask.mEnabled = true;	//Re-enable the Sync Data Task
    	super.finish();
    }
    
	@Override
	public void onItemSelected(AdapterView<?> _AdapterView, View _View, int _pos, long _row) {
		switch(_AdapterView.getId()) {
			case R.id.spinRFD:
				mOrder.setDelayreason(_AdapterView.getItemAtPosition(_pos).toString());
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
