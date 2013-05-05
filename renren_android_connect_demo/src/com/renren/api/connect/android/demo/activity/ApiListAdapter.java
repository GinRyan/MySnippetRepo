/**
 * $id$
 */
package com.renren.api.connect.android.demo.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.renren.api.connect.android.demo.ApiDemoInvoker;
import com.renren.api.connect.android.demo.R;

/**
 * The adapter for api list
 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com)
 */
public class ApiListAdapter extends BaseAdapter {
	
	private ArrayList<ApiItemGroup> apiGroups;
	
	private Activity activity;
	
	public ApiListAdapter(Activity activity, ArrayList<ApiItemGroup> apiGroups) {
		super();
		this.activity = activity;
		this.apiGroups = apiGroups;
	}

	@Override
	public int getCount() {
		if(apiGroups != null) {
			return apiGroups.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(apiGroups != null && position >= 0 && position < apiGroups.size()) {
			return apiGroups.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(apiGroups != null && position >= 0 && position < apiGroups.size()) {
			//设置title
			ApiItemGroup group = apiGroups.get(position);
			LinearLayout apiItemGroup = 
					(LinearLayout)LayoutInflater.from(parent.getContext())
						.inflate(R.layout.api_item_group, null);
			View titleView = LayoutInflater.from(activity).inflate(R.layout.api_list_item, null); 
			TextView titleGroupTitle = (TextView) titleView.findViewById(R.id.api_group_title);
			View infoView = (View)titleView.findViewById(R.id.api_info);
			infoView.setVisibility(View.GONE);
			titleGroupTitle.setText(group.getTitle());
			apiItemGroup.addView(titleView);
			//添加子项
			ArrayList<ApiItem> apiItems = group.getApiItems();
			for(final ApiItem item : apiItems) {
				View itemView = LayoutInflater.from(activity).inflate(R.layout.api_list_item, null); 
				TextView groupTitle = (TextView) itemView.findViewById(R.id.api_group_title);
				TextView apiLabel = (TextView) itemView.findViewById(R.id.api_label);
				TextView apiName = (TextView) itemView.findViewById(R.id.api_name);
				apiLabel.setText(item.getLabel());
				apiName.setText(item.getName());
				groupTitle.setVisibility(View.GONE);
				itemView.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						ApiDemoInvoker.invoke(activity, item.getInvokeName());
					}
				});
				
				apiItemGroup.addView(itemView);
			}
			
			convertView = apiItemGroup;
		}
		return convertView;
	}
	
}
