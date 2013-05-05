/**
 * $id$
 */
package com.renren.api.connect.android.demo.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.renren.api.connect.android.demo.R;

/**
 * API List
 * 
 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com)
 */
public class ApiListActivity extends BaseActivity {

	private LinearLayout apiListLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		apiListLayout = (LinearLayout) getLayoutInflater().inflate(
				R.layout.api_list, null);
		root.addView(apiListLayout);
		initApiList();

		// 设置标题栏左边按钮
		titlebarLeftButton.setText(getString(R.string.logout));
		titlebarLeftButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				renren.logout(ApiListActivity.this);
				ApiListActivity.this.finish();
			}
		});
		// 设置按钮背景图片
		titlebarLeftButton
				.setBackgroundResource(R.drawable.renren_demo_titlebar_right_button);

		// 设置标题栏文字
		titlebarText.setText(renren.getSessionKey().substring(0, 10) + "...");
	}

	/**
	 * Initialize the api list
	 */
	private void initApiList() {
		ArrayList<ApiItemGroup> groups = new ArrayList<ApiItemGroup>();
		// User info api group
		ApiItemGroup userInfoGroup = new ApiItemGroup(
				getString(R.string.user_info_group));
		userInfoGroup.addApiItem(new ApiItem(getString(R.string.albums_info),
				getString(R.string.albums_info_api),
				getString(R.string.albums_info_invoke)));
		userInfoGroup.addApiItem(new ApiItem(getString(R.string.users_getinfo),
				getString(R.string.users_getinfo_api),
				getString(R.string.users_getinfo_invoke)));
		userInfoGroup.addApiItem(new ApiItem(getString(R.string.friends_get),
				getString(R.string.friends_get_api),
				getString(R.string.friends_get_invoke)));
		userInfoGroup.addApiItem(new ApiItem(
				getString(R.string.friends_get_friends),
				getString(R.string.friends_get_friends_api),
				getString(R.string.friends_get_friends_invoke)));
		groups.add(userInfoGroup);

		// Publish content group
		ApiItemGroup publishGroup = new ApiItemGroup(
				getString(R.string.publish_group));
		publishGroup.addApiItem(new ApiItem(getString(R.string.publish_status),
				getString(R.string.publish_status_api),
				getString(R.string.publish_status_invoke)));
		publishGroup.addApiItem(new ApiItem(getString(R.string.publish_feed),
				getString(R.string.publish_feed_api),
				getString(R.string.publish_feed_invoke)));
		publishGroup.addApiItem(new ApiItem(getString(R.string.publish_photo),
				getString(R.string.publish_photo_api),
				getString(R.string.publish_photo_invoke)));
		publishGroup.addApiItem(new ApiItem(getString(R.string.create_album),
				getString(R.string.create_album_api),
				getString(R.string.create_album_invoke)));
		groups.add(publishGroup);

		// one-click api group
		ApiItemGroup oneClickGroup = new ApiItemGroup(
				getString(R.string.one_click_publish_group));
		oneClickGroup.addApiItem(new ApiItem(
				getString(R.string.one_click_status),
				getString(R.string.one_click_status_api),
				getString(R.string.one_click_status_invoke)));
		oneClickGroup.addApiItem(new ApiItem(
				getString(R.string.one_click_photo),
				getString(R.string.one_click_photo_api),
				getString(R.string.one_click_photo_invoke)));
		groups.add(oneClickGroup);

		// dialog api group
		ApiItemGroup dialogGroup = new ApiItemGroup(
				getString(R.string.dialog_group));
		dialogGroup.addApiItem(new ApiItem(getString(R.string.dialog_feed),
				getString(R.string.dialog_feed),
				getString(R.string.dialog_feed_invoke)));
		dialogGroup.addApiItem(new ApiItem(getString(R.string.dialog_status),
				getString(R.string.dialog_status),
				getString(R.string.dialog_status_invoke)));
		groups.add(dialogGroup);

		ApiItemGroup extensionsGroup = new ApiItemGroup(
				getString(R.string.extensions_group));
		extensionsGroup.addApiItem(new ApiItem(
				getString(R.string.extensions_pay),
				getString(R.string.extensions_pay_api),
				getString(R.string.extensions_pay_invoke)));
		groups.add(extensionsGroup);
		
		// add to api list
		ApiListAdapter adapter = new ApiListAdapter(this, groups);
		ListView apiList = (ListView) apiListLayout.findViewById(R.id.api_list);
		apiList.setFadingEdgeLength(0);
		apiList.setAdapter(adapter);
	}
}
