package com.sunnybrook;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CraftOrderDetailActivity extends Activity implements OnClickListener{
	final static int RESULT_CLOSE_ID = 0;
	final static int RESULT_OPEN_ID = 1;
	private float mFontSize = 0;
	private float mDescFontSize = 0;
	
	private craftorder mOrder;
    public void onCreate(Bundle savedInstanceState) {
    	setTitle("Craft Order Detail");
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.craftorderdetailactivity);
    	mOrder = (craftorder) getIntent().getSerializableExtra("craftorder");
    	mFontSize = getIntent().getIntExtra("fontsize", 10);
    	mDescFontSize = getIntent().getIntExtra("descfontsize", 12);
    	TextView mTextView = (TextView) findViewById(R.id.wonum);
    	mTextView.setText(mOrder.getOrderId());
    	mTextView.setTextSize(mFontSize);
    	mTextView = (TextView) findViewById(R.id.reportdate);
    	mTextView.setText(new MyDateFormat().myFormat(mOrder.getReportdate()));
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
    	mTextView = (TextView) findViewById(R.id.status);
    	mTextView.setText(mOrder.getStatus());
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
/*
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
*/
    	Button mButton = (Button) findViewById(R.id.btnClose);
    	mButton.setOnClickListener(this);
    	mButton = (Button) findViewById(R.id.btnOpen);
    	mButton.setOnClickListener(this);
    }

    @Override
	public void onClick(View _view) {
		// TODO Auto-generated method stub
    	switch(_view.getId()) {
    		case R.id.btnOpen:
    	    	Intent resultIntent = new Intent();
    	    	resultIntent.putExtra("craftorder", mOrder);
    	    	setResult(RESULT_OPEN_ID,resultIntent);
    			break;
    		default: break;
    	}
    	this.finish();
	}
    
}
