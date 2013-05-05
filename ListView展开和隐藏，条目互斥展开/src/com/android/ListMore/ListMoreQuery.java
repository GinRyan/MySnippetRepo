package com.android.ListMore;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ListMoreQuery extends ListActivity {
	ListMoreAdapter adapter;
	
	int[] image = {
		R.drawable.beijing1_s,R.drawable.beijing2_s,R.drawable.beijing3_s,R.drawable.beijing4_s,R.drawable.beijing5_s
	};
	String[] text = {
		"±´±´","¾§¾§","»¶»¶","Ó¯Ó¯","ÄÝÄÝ"
	};
	
	int ID = 0;
	int last = 0;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        
        adapter = new ListMoreAdapter(this);
        
        this.setListAdapter(adapter);
        
        this.getListView().setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				ID = arg2;
				
				adapter.notifyDataSetChanged();
				
				last = arg2;
			}
        	
        });
    }
    
    
    
    
    
    public class ListMoreAdapter extends BaseAdapter {
    	Activity activity;
    	LayoutInflater lInflater;
    	
    	public ListMoreAdapter(Activity a){
    		activity = a;
    		
    		lInflater = activity.getLayoutInflater();
    	}
    	
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return image.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			LinearLayout layout = new LinearLayout(activity);
	        layout.setOrientation(LinearLayout.VERTICAL);
	        
	        layout.addView(addTitleView(position));
	        
	        
	        if(ID==position){
	        	layout.addView(addCustomView(position));
	        }
	        
			return layout;
		}
    	
		
		public View addTitleView(int i){
			LinearLayout layout = new LinearLayout(activity);
	        layout.setOrientation(LinearLayout.HORIZONTAL);
	        
	        ImageView iv = new ImageView(activity);
	        iv.setImageResource(image[i]);
	        
	        layout.addView(iv,
	        		new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
	        
	        
	        TextView tv = new TextView(activity);
	        tv.setText(text[i]);
	        
	        layout.addView(tv,
	        		new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
	        
	        layout.setGravity(Gravity.CENTER);
	        return layout;
		}
		
		
		public View addCustomView(int i){
			View view = new View(activity);
			
			switch(i){
			case 0:
				ImageView iv = new ImageView(activity);
				iv.setImageResource(R.drawable.beijing4_b);
				
				view = iv;
				break;
				
			case 1:
				view = lInflater.inflate(R.layout.layout1, null);
				break;
				
			case 2:
				
				break;
			}
			
			return view;
			
		}
    }
}