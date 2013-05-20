package com.ljp.laucher.itemcontent;

import java.util.List;

import weibo4android.Comment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ljp.laucher.R;
import com.ljp.laucher.util.MathOperation;

public class Comments_Pop_ListAdapter extends BaseAdapter {

	private List<Comment> comments;
	private Context mContext;
	
	public Comments_Pop_ListAdapter(Context c,List<Comment> cs){
		this.comments = cs;
		this.mContext = c;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return comments.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView tv_user,tv_content,tv_time;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_categoty_pop,
					null);
		}
		tv_user = (TextView) convertView.findViewById(R.id.p_list_user);
		tv_content = (TextView) convertView.findViewById(R.id.p_list_text);
		tv_time = (TextView) convertView.findViewById(R.id.p_list_time);

		
		Comment comment = comments.get(position);
		tv_user.setText(comment.getUser().getName());
		tv_time.setText(MathOperation.getDateDifferFromNow(comment.getCreatedAt()));
		tv_content.setText("    "+comment.getText());
		return convertView;
	}
}
























