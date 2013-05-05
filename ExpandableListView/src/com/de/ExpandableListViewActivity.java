package com.de;

import java.util.ArrayList;
import java.util.List;

import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListViewActivity extends ExpandableListActivity {

	List<String> group; // 组列表
	List<List<String>> child; // 子列表
	ContactsInfoAdapter adapter; // 数据适配器

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置为无标题
		setContentView(R.layout.main);
		getExpandableListView().setBackgroundResource(R.drawable.xd2);

		initializeData();
		getExpandableListView().setAdapter(new ContactsInfoAdapter());
		getExpandableListView().setCacheColorHint(0); // 设置拖动列表的时候防止出现黑色背景
	}

	/**
	 * 初始化组、子列表数据
	 */
	private void initializeData() {
		group = new ArrayList<String>();
		child = new ArrayList<List<String>>();

		addInfo("Andy", new String[] { "male", "138123***", "GuangZhou" });
		addInfo("Fairy", new String[] { "female", "138123***", "GuangZhou" });
		addInfo("Jerry", new String[] { "male", "138123***", "ShenZhen" });
		addInfo("Tom", new String[] { "female", "138123***", "ShangHai" });
		addInfo("Bill", new String[] { "male", "138231***", "ZhanJiang" });

	}

	/**
	 * 模拟给组、子列表添加数据
	 * 
	 * @param g
	 *            -group
	 * @param c
	 *            -child
	 */
	private void addInfo(String g, String[] c) {
		group.add(g);
		List<String> childitem = new ArrayList<String>();
		for (int i = 0; i < c.length; i++) {
			childitem.add(c[i]);
		}
		child.add(childitem);
	}

	class ContactsInfoAdapter extends BaseExpandableListAdapter {

		// -----------------Child----------------//
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return child.get(groupPosition).get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return child.get(groupPosition).size();
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			String string = child.get(groupPosition).get(childPosition);
			return getGenericView(string);
		}

		// ----------------Group----------------//
		@Override
		public Object getGroup(int groupPosition) {
			return group.get(groupPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public int getGroupCount() {
			return group.size();
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			String string = group.get(groupPosition);
			return getGenericView(string);
		}

		// 创建组/子视图
		public TextView getGenericView(String s) {
			// Layout parameters for the ExpandableListView
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, 40);

			TextView text = new TextView(ExpandableListViewActivity.this);
			text.setLayoutParams(lp);
			// Center the text vertically
			text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			// Set the text starting position
			text.setPadding(36, 0, 0, 0);

			text.setText(s);
			return text;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return true;
		}

	}
}