package com.sunnybrook;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SyslogActivity extends ListActivity{
	private ArrayAdapter<String> mAdapter = null;
	private String[] mItems = new String [] {"Android","iPhone"};

	@Override
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	//setContentView(R.layout.syslogactivity);
    	//ListView listview = (ListView) findViewById(R.id.lv_syslog);
//    	mItems = new ArrayList<String> ();
//    	mItems.add("Android");
//    	mItems.add("iPhone");
    	mAdapter = new ArrayAdapter<String>(this,R.layout.list_syslog,mItems);
    	setListAdapter(mAdapter);
    	ListView listview = getListView();
    	listview.setTextFilterEnabled(true);
    }

	/*
	public class SyslogAdapter extends ArrayAdapter<String> {
		private String[] items;

		public SyslogAdapter(Context context, int textViewResourceId, ArrayList<String> items) {
			super(context, textViewResourceId);
			this.items = items;
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			View v = convertView;
			if(v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_syslog, null);
			}
			String mS = items.get(position);
			if(mS != null) {
				TextView t = (TextView) v.findViewById(R.id.logid);
				if(t!= null) t.setText("ID:" + Integer.toString(position));
				t = (TextView) v.findViewById(R.id.logtime);
				if(t!=null) t.setText("Name:" + mS);
			}
			return v;
		}
	}
*/
}
