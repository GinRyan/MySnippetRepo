/**
 * $id$
 */
package com.renren.api.connect.android.demo;

import android.app.Activity;
import android.content.Intent;

import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.demo.activity.StatusPublishActivity;
import com.renren.api.connect.android.status.StatusSetRequestParam;

/**
 * Demos the use of Status-related APIs
 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com)
 */
public class StatusDemo {
	
	/**
	 * Start a publish activity for the user to publish status
	 * 
	 * @param activity
	 * @param renren
	 */
	public static void publishStatus(final Activity activity, final Renren renren) {
		Intent intent = new Intent(activity, StatusPublishActivity.class);
		intent.putExtra(Renren.RENREN_LABEL, renren);
		activity.startActivity(intent);
	}
	
	public static void publishStatusOneClick(final Activity activity, final Renren renren) {
		renren.publishStatus(activity, new StatusSetRequestParam(""));
	}
}
