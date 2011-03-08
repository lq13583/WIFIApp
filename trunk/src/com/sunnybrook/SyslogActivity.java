package com.sunnybrook;

import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class SyslogActivity extends ListActivity  implements OnClickListener{
	private ArrayAdapter<loginfo> mAdapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.syslogactivity);
    	mAdapter = new SyslogAdapter(this,R.layout.list_syslog);
    	setListAdapter(mAdapter);
    	refreshList(SysLog.getLogList());

    	Button mBtn=(Button) findViewById(R.id.btnClear);
    	mBtn.setOnClickListener(this);
    	mBtn=(Button) findViewById(R.id.btnRefresh);
    	mBtn.setOnClickListener(this);
    }
	
	private void refreshList(List<loginfo> _Items) {
    	mAdapter.clear();
    	for(int i=0;i<_Items.size();i++)
    		mAdapter.add(_Items.get(i));
		
	}

	public class SyslogAdapter extends ArrayAdapter<loginfo> {

		public SyslogAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
			
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			View v = convertView;
			if(v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_syslog, null);
			}
			loginfo mS = getItem(position);
			if(mS != null) {
				TextView t = (TextView) v.findViewById(R.id.logid);
				if(t!= null) t.setText("ID:" + Integer.toString(mS.getLogid()));
				t = (TextView) v.findViewById(R.id.logtime);
				if(t!=null) t.setText("Time:" + mS.getLogtime());
				t = (TextView) v.findViewById(R.id.logact);
				if(t!=null) t.setText("Activity:" + mS.getLogact());
				t=(TextView) v.findViewById(R.id.logdesc);
				if(t!=null) t.setText(mS.getLogdesc());
			}
			return v;
		}
	}

	@Override
	public void onClick(View _view) {
		switch(_view.getId()) {
			case R.id.btnClear:
				SysLog.ClearLog();
			case R.id.btnRefresh:
		    	refreshList(SysLog.getLogList());
				break;
		}
		// TODO Auto-generated method stub
		
	}
}
