package com.sunnybrook;

import android.app.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SuperOrderDetailActivity extends Activity implements OnClickListener{
	private superorder mOrder;
	private float mFontSize = 0;
	private float mDescFontSize = 0;

	private MainApp mApp;
	private sysconfig myConfig;
	public void onCreate(Bundle savedInstanceState) {
    	setTitle("Super Order Detail");
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.superorderdetailactivity);
    	mApp = (MainApp) getApplication();
    	myConfig = mApp.getMySysConfig();
    	mFontSize = myConfig.getFont_size();
    	mDescFontSize = myConfig.getDesc_font_size();
    	mOrder = (superorder) getIntent().getSerializableExtra("superorder");
    	TextView mTextView = (TextView) findViewById(R.id.wonum);
    	mTextView.setText(mOrder.getOrderId());
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mFontSize);
    	mTextView = (TextView) findViewById(R.id.reportdate);
    	mTextView.setText(new MyDateFormat().myFormat(mOrder.getReportdate()));
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mFontSize);
    	mTextView = (TextView) findViewById(R.id.reportedby);
    	mTextView.setText(mOrder.getReportedby());
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mFontSize);
    	mTextView = (TextView) findViewById(R.id.wo3);
    	mTextView.setText(mOrder.getWo3());
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mFontSize);
    	mTextView = (TextView) findViewById(R.id.phone);
    	mTextView.setText(mOrder.getPhone());
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mFontSize);
    	mTextView = (TextView) findViewById(R.id.status);
    	mTextView.setText(mOrder.getStatus());
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mFontSize);
    	mTextView = (TextView) findViewById(R.id.location);
    	mTextView.setText(mOrder.getLocation());
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mFontSize);
    	mTextView = (TextView) findViewById(R.id.locationdesc);
    	mTextView.setText(mOrder.getLocationdesc());
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mFontSize);
    	mTextView = (TextView) findViewById(R.id.comments);
    	mTextView.setText(mOrder.getComments());
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mDescFontSize);
    	mTextView = (TextView) findViewById(R.id.khname);
    	mTextView.setText(mOrder.getKhname());
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mFontSize);
    	mTextView = (TextView) findViewById(R.id.ktitle);
    	mTextView.setText(mOrder.getKtitle());
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mFontSize);
    	mTextView = (TextView) findViewById(R.id.kdept);
    	mTextView.setText(mOrder.getKdept());
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mFontSize);
    	mTextView = (TextView) findViewById(R.id.kcamp);
    	mTextView.setText(mOrder.getKcamp());
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mFontSize);
    	mTextView = (TextView) findViewById(R.id.kcost);
    	mTextView.setText(mOrder.getKcost());
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mFontSize);
    	mTextView = (TextView) findViewById(R.id.kcod1);
    	mTextView.setText(mOrder.getKcod1());
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mFontSize);
    	mTextView = (TextView) findViewById(R.id.kcod2);
    	mTextView.setText(mOrder.getKcod2());
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mFontSize);
    	mTextView = (TextView) findViewById(R.id.kcod3);
    	mTextView.setText(mOrder.getKcod3());
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mFontSize);
    	mTextView = (TextView) findViewById(R.id.kcod4);
    	mTextView.setText(mOrder.getKcod4());
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mFontSize);
    	mTextView = (TextView) findViewById(R.id.kcodr1);
    	mTextView.setText(mOrder.getKcodr1());
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mFontSize);
    	mTextView = (TextView) findViewById(R.id.kcodr2);
    	mTextView.setText(mOrder.getKcodr2());
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mFontSize);
    	mTextView = (TextView) findViewById(R.id.kcodr3);
    	mTextView.setText(mOrder.getKcodr3());
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mFontSize);
    	mTextView = (TextView) findViewById(R.id.kcodr4);
    	mTextView.setText(mOrder.getKcodr4());
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mFontSize);
    	mTextView = (TextView) findViewById(R.id.kcor1);
    	mTextView.setText(mOrder.getKcor1());
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mFontSize);
    	mTextView = (TextView) findViewById(R.id.kcor2);
    	mTextView.setText(mOrder.getKcor2());
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mFontSize);
    	mTextView = (TextView) findViewById(R.id.kcor3);
    	mTextView.setText(mOrder.getKcor3());
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mFontSize);
    	mTextView = (TextView) findViewById(R.id.kcor4);
    	mTextView.setText(mOrder.getKcor4());
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mFontSize);
    	mTextView = (TextView) findViewById(R.id.kq1);
    	mTextView.setText(mOrder.getKq1());
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mFontSize);
    	mTextView = (TextView) findViewById(R.id.kq2);
    	mTextView.setText(mOrder.getKq2());
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mFontSize);
    	mTextView = (TextView) findViewById(R.id.kq3);
    	mTextView.setText(mOrder.getKq3());
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mFontSize);
    	mTextView = (TextView) findViewById(R.id.kq4);
    	mTextView.setText(mOrder.getKq4());
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mFontSize);
    	mTextView = (TextView) findViewById(R.id.kcom);
    	mTextView.setText(mOrder.getKcom());
    	mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP,mFontSize);

    	Button mButton = (Button) findViewById(R.id.btnClose);
    	mButton.setOnClickListener(this);
    }

    @Override
	public void onClick(View _view) {
		// TODO Auto-generated method stub
    	this.finish();
	}
    
}
