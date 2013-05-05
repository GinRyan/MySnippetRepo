/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;

import com.renren.api.connect.android.exception.RenrenAuthError;
import com.renren.api.connect.android.view.RenrenAuthListener;

/**
 * 提供方法检查是否有调用相关接口的权限以及认证信息等
 * 
 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com)
 */
public class AuthorizationHelper {
		
	private static final String PERMISSIONS = "permissions";
	
	//用于在验证Activity与调用者间传递结果的key值	
	private static final String KEY_RESPONSE_TYPE = "type";
	
	private static final String AUTH_ERROR = "auth_error";
	
	private static final String AUTH_COMPLETE = "auth_complete";
	
	private static final String AUTH_CANCEL_LOGIN = "auth_cancel_login"; 
	
	private static final String AUTH_CANCEL = "key_auth_cancel";
	
	private static final String KEY_AUTH_ERROR = "error";
	
	private static final String KEY_AUTH_ERROR_DESCRIPTION = "error_description";
	
	private static final String KEY_AUTH_ERROR_URI = "error_uri";
	
	private static final String KEY_RESPONSE_BUNDLE = "response_bundle";
	
	private static final String ACTION = "com.renren.api.connect.android" 
			+ ".AuthorizationHelper.RenrenAuthAdapter";

	private static Renren renren;
	
	/**
	 * 检查当前是否有权限发送相关请求，若无权限，引导用户进行认证
	 * <p>注意：
	 * <p>使用者需要在AndroidManifest.xml中声明Activity如下：
	 * <p>
	 * &ltactivity android:name=
	 * "com.renren.api.connect.android.AuthorizationHelper$BlockActivity" 
	 * android:theme="@android:style/Theme.Dialog"
	 * &gt&lt/activity&gt
	 * @param renren
	 * 			  {@link Renren}的对象
	 * @param activity
	 * 			     当前Activity
	 * @param permissions
	 *            访问的api需要的权限列表
	 * @param listener
	 *            认证结果的监听器
	 */
	public synchronized static void check(Renren renren, Activity activity, 
			String[] permissions, RenrenAuthListener listener) {
		//首先检查当前sessionKey是否正确
		if(renren.isSessionKeyValid()) {
			if(listener != null) {
				Bundle bundle = new Bundle();
				bundle.putString("session_key", renren.getSessionKey());
				bundle.putString("access_token", renren.getAccessToken());
				bundle.putLong("user_id", renren.getCurrentUid());
				listener.onComplete(bundle);
			}
			return;
		}
		
		//若当前sessionKey 不正确，则启动认证过程
		AuthorizationHelper.renren = renren;
		
		int requestCode = Math.abs((int)System.currentTimeMillis());
		
		//注册BroadcastReceiver
		if(listener != null) {
			RenrenAuthAdapter adapter = new RenrenAuthAdapter(listener);
			IntentFilter filter = new IntentFilter();
			filter.addAction(ACTION);
			activity.registerReceiver(adapter, filter);
		}
		
		//启动验证
		Intent intent = new Intent(activity, BlockActivity.class);
		Bundle bundle = new Bundle();
		bundle.putStringArray(PERMISSIONS, permissions);
		intent.putExtras(bundle);
		activity.startActivityForResult(intent, requestCode);
	}
	
	/**
	 * 拦截认证的返回信息，并将结果转发至调用者传入的listener。开发者不直接使用该类型。
	 * 
	 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com)
	 */
 	private static class RenrenAuthAdapter extends BroadcastReceiver{
 	
 		/**
 		 * 调用者传入的listener引用
 		 */
 		private RenrenAuthListener listener;
 		
		
		public RenrenAuthAdapter(RenrenAuthListener listener) {
			this.listener = listener;
		}
			  
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if(bundle != null) {
				if(bundle.containsKey(KEY_RESPONSE_TYPE)) {
					String code = bundle.getString(KEY_RESPONSE_TYPE);
					if(code.equals(AUTH_COMPLETE)) {
						Bundle responseBundle = bundle.getBundle(KEY_RESPONSE_BUNDLE);
						listener.onComplete(responseBundle);
					} else if(code.equals(AUTH_ERROR)) {
						String error = bundle.getString(KEY_AUTH_ERROR);
						String errorDescription = bundle.getString(KEY_AUTH_ERROR_DESCRIPTION);
						String errorUri = bundle.getString(KEY_AUTH_ERROR_URI);
						listener.onRenrenAuthError(new RenrenAuthError(
								error, errorDescription, errorUri));
					} else if(code.equals(AUTH_CANCEL_LOGIN)) {
						listener.onCancelLogin();
					} else if(code.equals(AUTH_CANCEL)) {
						Bundle responseBundle = bundle.getBundle(KEY_RESPONSE_BUNDLE);
						listener.onCancelAuth(responseBundle);
					} else {
						
					}
				}
			}
			try {
				context.unregisterReceiver(this);
			} catch (Exception e) {
				Util.logger(e.getMessage());
			}
		}
	}
	
	/**
	 * 拦截SSO以及OAuth认证的结果，开发者不直接使用该类型
	 */
	public static final class BlockActivity extends Activity {
		
		private static final String KEY_PARCEL_RENREN = "renren";
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			if (savedInstanceState != null) {
				renren = savedInstanceState.getParcelable(KEY_PARCEL_RENREN);
				renren.init(this);
			}
			this.setVisible(false);
			authorize();
		}		

		@Override
		protected void onRestoreInstanceState(Bundle savedInstanceState) {
			if (renren == null) {
				renren = savedInstanceState.getParcelable(KEY_PARCEL_RENREN);
				renren.init(this);
			}
			super.onRestoreInstanceState(savedInstanceState);
		}



		@Override
		protected void onSaveInstanceState(Bundle outState) {
			outState.putParcelable(KEY_PARCEL_RENREN, renren);
			super.onSaveInstanceState(outState);
		}



		@Override
		protected void onStart() {
			super.onStart();
		}
	
		@Override
		protected void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			renren.authorizeCallback(requestCode, resultCode, data);
		}

		@Override
		public void onConfigurationChanged(Configuration newConfig) {
			super.onConfigurationChanged(newConfig);
		}	
		
		/**
		 * 使用SSO验证方式
		 */
		private void authorize() {

			Bundle bundle = getIntent().getExtras();
			String[] permissions = bundle.getStringArray(PERMISSIONS);
			renren.authorize(this, permissions, new RenrenAuthListener() {
				
				@Override
				public void onRenrenAuthError(RenrenAuthError renrenAuthError) {
					Bundle bundle = new Bundle();
					bundle.putString(KEY_RESPONSE_TYPE, AUTH_ERROR);
					if(renrenAuthError != null) {
						bundle.putString(KEY_AUTH_ERROR, renrenAuthError.getError());
						bundle.putString(KEY_AUTH_ERROR_DESCRIPTION, 
								renrenAuthError.getErrorDescription());
						bundle.putString(KEY_AUTH_ERROR_URI, renrenAuthError.getErrorUri());
					}
					Intent intent = new Intent();
					intent.setAction(ACTION);
					intent.putExtras(bundle);
					sendBroadcast(intent);
					finish();
				}
				
				@Override
				public void onComplete(Bundle values) {
					values.putString(KEY_RESPONSE_TYPE, AUTH_COMPLETE);
					Intent intent = new Intent();
					intent.putExtras(values);
					intent.setAction(ACTION);
					sendBroadcast(intent);
					finish();
				}
				
				@Override
				public void onCancelLogin() {
					Bundle bundle = new Bundle();
					bundle.putString(KEY_RESPONSE_TYPE, AUTH_CANCEL_LOGIN);
					Intent intent = new Intent();
					intent.putExtras(bundle);
					intent.setAction(ACTION);
					sendBroadcast(intent);
					finish();
				}
				
				@Override
				public void onCancelAuth(Bundle values) {
					values.putString(KEY_RESPONSE_TYPE, AUTH_CANCEL);
					Intent intent = new Intent();
					intent.putExtras(values);
					intent.setAction(ACTION);
					sendBroadcast(intent);
					finish();
				}
			}, 1);
		}		
	};
}
