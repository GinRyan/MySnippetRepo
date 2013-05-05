package com.example.listoflist;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class InsListActivity extends Activity {
	private Context context;
	private ListView lv;
	private List<String> list = new LinkedList<String>();

	public static int ID = 0;
	public static int last = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		context = InsListActivity.this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ins_list);
		lv = (ListView) findViewById(R.id.lv);

		for (int i = 1; i <= 30; i++) {
			list.add("---" + i + "---");
		}
		final ListExpandedMoreAdapter ilia = new ListExpandedMoreAdapter(context, list);
		lv.setAdapter(ilia);
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View view,
					int arg2, long arg3) {
				ID = arg2;
				ilia.notifyDataSetChanged();
				last = arg2;
				return true;
			}
		});

	}

}
