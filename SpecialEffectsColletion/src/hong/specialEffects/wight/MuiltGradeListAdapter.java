package hong.specialEffects.wight;


import hong.specialEffects.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MuiltGradeListAdapter extends BaseExpandableListAdapter {
      private LayoutInflater layoutInflater;
      
      private Context mContext;
      
      private List<Map<String,Object>> parentList = new ArrayList<Map<String,Object>>();
      
      private List<List<Map<String,Object>>> childList = new ArrayList<List<Map<String,Object>>>();
      
      
	public MuiltGradeListAdapter(Context mContext,List<Map<String,Object>> parentList,List<List<Map<String,Object>>> childList){
		
		
		this.mContext = mContext;
		
		this.parentList = parentList;
		
		this.childList = childList;
		
		layoutInflater = LayoutInflater.from(mContext);
	}
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childList.get(groupPosition).get(childPosition).get("Title").toString();
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if(convertView ==null){
			convertView = layoutInflater.inflate(R.layout.muilt_grade_list_child, null);
		}
		final ImageView head=(ImageView)convertView.findViewById(R.id.headImage);
		   head.setImageResource(Integer.valueOf(childList.get(groupPosition).get(childPosition).get("Head").toString()));
		final TextView title=(TextView)convertView.findViewById(R.id.title);
		  title.setText(childList.get(groupPosition).get(childPosition).get("Title").toString());
	    final TextView mood =(TextView)convertView.findViewById(R.id.mood);
	      mood.setText(childList.get(groupPosition).get(childPosition).get("Mood").toString());
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return childList.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return parentList.get(groupPosition).get("List").toString();
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return parentList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		
		if(convertView==null){
			convertView=layoutInflater.inflate(R.layout.muilt_grade_list_parent, null);
		}
		final TextView list = (TextView) convertView.findViewById(R.id.list);
		  list.setText(parentList.get(groupPosition).get("List").toString());
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		Toast.makeText(mContext,"nihao",Toast.LENGTH_SHORT).show();
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		Toast.makeText(mContext, "这是第"+groupPosition+"组，第"+childPosition+"个", Toast.LENGTH_SHORT).show();
		return true;
	}

}
	
