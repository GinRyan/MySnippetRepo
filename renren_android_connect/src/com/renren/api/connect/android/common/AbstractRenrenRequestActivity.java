/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.common;

import com.renren.api.connect.android.Renren;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

/**
 * 请求窗口的父类，其中包含了对Renren对象的初始化<br>
 * 使用前，需要将Renren对象已Parcelable序列化方式传入intent中，例如
 * 
 * Intent intent = new Intent(xxxActivity, ARRActivity.class);<br>
 * Bundle bundle = new Bundle();<br>
 * bundle.putParcelable(Renren.RENREN_LABEL, renren);<br>
 * intent.putExtras(bundle);<br>
 * startActivity(intent); <br>
 * 
 * 或：
 * 
 * Intent intent = new Intent(xxxActivity, ARRActivity.class);<br>
 * intent.putExtra(Renren.RENREN_LABEL, this);
 * activity.startActivity(intent);
 * 
 * <p>也可调用Renren提供的startRenrenRequestActivity方法，如：<br>
 * Intent intent = new Intent(xxxActivity, ARRActivity.class);<br>
 * renren.startRenrenRequestActivity(xxxActivity, intent);<br>
 * 该方法会辅助完整Renren以Parcel方式传入Activity的操作
 * 
 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com)
 *
 */
public class AbstractRenrenRequestActivity extends Activity {
	
	/**
	 * 用于发送请求的对象
	 */
	protected Renren renren;
	
	/**
	 * 异步上传显示进度框
	 */
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initRenren();
	}
	
	/**
	 * 初始化Renren对象
	 */
	private void initRenren() {
		// 读取人人对象
		Intent intent = getIntent();
		if (intent.hasExtra(Renren.RENREN_LABEL)) {
			renren = intent.getParcelableExtra(Renren.RENREN_LABEL);
			renren.init(this);
		} else {
			Bundle bundle = intent.getExtras();
			if (bundle != null && bundle.containsKey(Renren.RENREN_LABEL)) {
				renren = bundle.getParcelable(Renren.RENREN_LABEL);
				renren.init(this);
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		/*
		 * 解决横竖屏切换时的FC bug
		 * 在Activity销毁的时候，进度框如果不显示销毁则会继续存在，当再次调用进度框的dismiss方法时会出现view not
		 * attached to window manager异常
		 */
		finishProgress();
		super.onDestroy();
	}
	
	/**
	 * 显示异步上传时的等待进度框
	 * 
	 * @param message
	 * 				进度框内容文字
	 */
	protected void showProgress(String message) {
		progressDialog = new ProgressDialog(this);
		progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		progressDialog.setMessage(message);
		progressDialog.show();
	}
	
	/**
	 * 结束进度框
	 * 
	 * @param progress
	 */
	protected void finishProgress() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}
	
	/**
	 * 显示Toast提示
	 * 
	 * @param message
	 */
	protected void showTip(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT)
				.show();
	}
}
