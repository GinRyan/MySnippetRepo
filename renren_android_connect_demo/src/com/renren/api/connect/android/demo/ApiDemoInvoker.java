/**
 * $id$
 */
package com.renren.api.connect.android.demo;

import android.app.Activity;
import android.content.Intent;

import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.demo.activity.ApiFeedPublishActivity;
import com.renren.api.connect.android.demo.activity.ApiUsersInfoActivity;
import com.renren.api.connect.android.demo.activity.FriendsGetActivity;
import com.renren.api.connect.android.demo.activity.FriendsGetFriendsActivity;
import com.renren.api.connect.android.demo.activity.PayActivity;

/**
 * Demos the how to use Renren APIs
 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com)
 */
public final class ApiDemoInvoker {
	
	private static Renren renren;
	
	/**
	 * Initialize the invoker. This method must be called before calling 'invoke'
	 * 
	 * @param renren
	 */
	public static void init(Renren renren) {
		ApiDemoInvoker.renren = renren;
	}
	
	/**
	 * Call the api demo methods specified by its invoke name
	 * 
	 * @param activity The activity in which the method is called
	 * @param invokeName The name used to specify which demo method to be called
	 */
	public static void invoke(Activity activity, String invokeName) {
		if(invokeName.equals(activity.getString(R.string.publish_status_invoke))) {
			StatusDemo.publishStatus(activity, renren);
		} else if(invokeName.equals(activity.getString(R.string.one_click_status_invoke))) {
			StatusDemo.publishStatusOneClick(activity, renren);
		} else if(invokeName.equals(activity.getString(R.string.one_click_photo_invoke))) {
			PhotoDemo.uploadPhotoWithActivity(activity, renren);
		} else if(invokeName.equals(activity.getString(R.string.publish_photo_invoke))) {
			PhotoDemo.uploadPhoto(activity, renren);
		} else if(invokeName.equals(activity.getString(R.string.create_album_invoke))) {
			PhotoDemo.createAlbum(activity, renren);
		} else if(invokeName.equals(activity.getString(R.string.albums_info_invoke))) {
			PhotoDemo.getAlbums(activity, renren);
		} else if(invokeName.equals(activity.getString(R.string.dialog_feed_invoke))) {
			WidgetDialogDemo.showFeedDialog(activity, renren);
		} else if(invokeName.equals(activity.getString(R.string.dialog_status_invoke))) {
			WidgetDialogDemo.showStatusDialog(activity, renren);
		} else if(invokeName.equals(activity.getString(R.string.publish_feed_invoke))){
			Intent intent = new Intent(activity, ApiFeedPublishActivity.class);
			intent.putExtra(Renren.RENREN_LABEL, renren);
			activity.startActivity(intent);
		} else if(invokeName.equals(activity.getString(R.string.users_getinfo_invoke))){
			Intent intent = new Intent(activity, ApiUsersInfoActivity.class);
			intent.putExtra(Renren.RENREN_LABEL, renren);
			activity.startActivity(intent);
		} else if(invokeName.equals(activity.getString(R.string.friends_get_invoke))){
			Intent intent = new Intent(activity, FriendsGetActivity.class);
			intent.putExtra(Renren.RENREN_LABEL, renren);
			activity.startActivity(intent);
		} else if(invokeName.equals(activity.getString(R.string.friends_get_friends_invoke))){
			Intent intent = new Intent(activity, FriendsGetFriendsActivity.class);
			intent.putExtra(Renren.RENREN_LABEL, renren);
			activity.startActivity(intent);
		} else if(invokeName.equals(activity.getString(R.string.extensions_pay_invoke))){
			Intent intent  = new Intent(activity,PayActivity.class);
			intent.putExtra(Renren.RENREN_LABEL,renren);
			activity.startActivity(intent);
		}
	}
	
}
