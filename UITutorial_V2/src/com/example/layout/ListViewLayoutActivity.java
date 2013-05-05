package com.example.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ListViewLayoutActivity extends Activity {
	ListView m_listView1;
	ListView m_listView2;
	ListView m_listView3;
	private String[] m_names=new String[]{"shi","wen","bin"};
	private String[] m_tels=new String[]{"name","tel"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(com.example.R.layout.listviewlayout);
		
		m_listView1=(ListView)findViewById(com.example.R.id.listviewlayout_listview1);
		
		ListAdapter adapter= new ArrayAdapter<String>(this,com.example.R.layout.listview_item_array_layout,m_names);
		m_listView1.setAdapter(adapter);
		
		m_listView2=(ListView)findViewById(com.example.R.id.listviewlayout_listview2);
		adapter=new SimpleAdapter(this,getData(),com.example.R.layout.listview_item_simple_layout,
				m_tels,
				new int[]{com.example.R.id.listview_item_simple_layout_name,
			com.example.R.id.listview_item_simple_layout_mobile
		});
		m_listView2.setAdapter(adapter);
		m_listView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(ListViewLayoutActivity.this,
						((Map<String,String>) getData().get(position)).get("tel"),
						Toast.LENGTH_SHORT
						).show();
			}
		});
		
		m_listView3=(ListView)findViewById(com.example.R.id.listviewlayout_listview3);
	}
	private List<Map<String,String>> getData(){
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		Map<String,String> map=new HashMap<String,String>();
		map.put("name", "swb");
		map.put("tel", "1111111111");
		list.add(map);
		
		map=new HashMap<String,String>();
		map.put("name", "qqqqqq");
		map.put("tel", "2222222");
		list.add(map);
		
		map=new HashMap<String,String>();
		map.put("name", "ttttttt");
		map.put("tel", "3333333333333");
		list.add(map);
		
		return list;
	}
}
