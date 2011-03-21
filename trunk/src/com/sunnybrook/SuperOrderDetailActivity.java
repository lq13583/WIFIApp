package com.sunnybrook;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SuperOrderDetailActivity extends Activity {
	private superorder mOrder;
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.superorderdetailactivity);
    	mOrder = (superorder) getIntent().getSerializableExtra("superorder");
    	TextView mTextView = (TextView) findViewById(R.id.wonum);
    	mTextView.setText(mOrder.getOrderId());
    }
}
