/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.status;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.renren.api.connect.android.AsyncRenren;
import com.renren.api.connect.android.AuthorizationHelper;
import com.renren.api.connect.android.R;
import com.renren.api.connect.android.Util;
import com.renren.api.connect.android.common.AbstractRenrenRequestActivity;
import com.renren.api.connect.android.common.AbstractRequestListener;
import com.renren.api.connect.android.exception.RenrenAuthError;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.view.ProfileNameView;
import com.renren.api.connect.android.view.ProfilePhotoView;
import com.renren.api.connect.android.view.RenrenAuthListener;

/**
 * 发布新鲜事的窗口
 * 
 * 使用时需要在AndroidManifest.xml中声明此Activity，
 * 并在使用前，将renren对象以Parcel方式传递给该activity，例如：
 *  <p>Intent intent = new Intent(activityA, StatusPubActivity.class);
 *  <p>Bundle bundle = new Bundle();
 *  <p>bundle.putParcelable(Renren.RENREN_LABEL, renren);
 *  <p>...
 *  <p>intent.putExtras(bundle);
 *  <p>activityA.startActivity(intent);
 * 若需传入Status对象进行编辑和发送，则需要使用类似上述方式将Status对象以Parcel方式
 * 传递给该activity
 * 
 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com) 
 */
public class StatusPubActivity extends AbstractRenrenRequestActivity {

	private static final int PROGRESS_DIALOG = 1;
	/**
	 * 取消按钮
	 */
	private Button cancelButton;
	
	/**
	 * 发布按钮
	 */
	private Button publishButton;
	
	/**
	 * 字数统计
	 */
	private TextView chCounterText;
	
	/**
	 * 状态编辑区域
	 */
	private EditText statusEdit;

	/**
	 * 待编辑和发布的状态
	 */
	private StatusSetRequestParam status;
	
	private ProgressDialog progressDialog;
	
	private Handler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.renren_sdk_status_pub);  		
		
		handler = new Handler(getMainLooper());
		
		initStatus();
		if (renren == null) {
			showToast(this.getString(R.string.renren_sdk_object_init_error));
			return;
		}

		AuthorizationHelper.check(renren, this,
				StatusHelper.PUBLISH_STATUS_PERMISSIONS,
				new RenrenAuthListener() {

					@Override
					public void onComplete(Bundle values) {
						initComponents();
					}

					@Override
					public void onRenrenAuthError(
							RenrenAuthError renrenAuthError) {
						showToast(StatusPubActivity.this
								.getString(R.string.renren_sdk_status_publish_failed));
						StatusPubActivity.this.finish();
					}

					@Override
					public void onCancelLogin() {
						StatusPubActivity.this.finish();
					}

					@Override
					public void onCancelAuth(Bundle values) {
						StatusPubActivity.this.finish();
					}

				});
	}
				
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//响应返回键
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		} 
				
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id) {
		case PROGRESS_DIALOG:
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage(this.getString(R.string.renren_sdk_publish_status_hint));
			
		}
		return super.onCreateDialog(id);
	}

	/**
	 * 初始化状态信息
	 */
	private void initStatus() {
		Intent intent = getIntent();
		if (intent.hasExtra(StatusSetRequestParam.STATUS_LABEL)) {
			status = intent.getParcelableExtra(StatusSetRequestParam.STATUS_LABEL);
		} else {
			Bundle bundle = intent.getExtras();
			if (bundle != null && bundle.containsKey(StatusSetRequestParam.STATUS_LABEL)) {
				status = bundle.getParcelable(StatusSetRequestParam.STATUS_LABEL);
			} else {
				status = new StatusSetRequestParam("");
			}
		}
	}
	
		
	/**
	 * 初始化界面各元素及其响应事件
	 */
	private void initComponents() {		
        ProfilePhotoView profilePhotoView = (ProfilePhotoView) findViewById(R.id.renren_sdk_profile_photo);
        profilePhotoView.setUid(renren.getCurrentUid());
        
        ProfileNameView profileNameView = (ProfileNameView) findViewById(R.id.renren_sdk_profile_name);
        profileNameView.setUid(renren.getCurrentUid(), renren);
		
		chCounterText = (TextView)findViewById(R.id.renren_sdk_status_ch_counter);
		statusEdit = (EditText) findViewById(R.id.renren_sdk_status_edit_text);
		statusEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {				
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				status.setStatus(statusEdit.getText().toString());
				chCounterText.setText(status.getStatus().length() + "/" + StatusSetRequestParam.MAX_LENGTH);
			}
			
		});

		if(status != null) {
			statusEdit.setText(status.getStatus());
			chCounterText.setText(status.getStatus().length() + "/" + StatusSetRequestParam.MAX_LENGTH);
		}
		
		cancelButton = (Button)findViewById(R.id.renren_sdk_status_cancel);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		publishButton = (Button)findViewById(R.id.renren_sdk_status_publish);
		publishButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(status == null || status.getStatus() == null || status.getStatus().trim().length() == 0) {
					showToast(StatusPubActivity.this.getString(R.string.renren_sdk_publish_null_status_alert));
				} else {
					publish();
				}
			}
		});
	}
	
	/**
	 * 发布状态
	 */
	private void publish() {
		showDialog(PROGRESS_DIALOG);
		AsyncRenren aRr = new AsyncRenren(renren);
		
		//若超出长度则截短到240字
		if(status.getStatus()!= null && status.getStatus().length() > StatusSetRequestParam.MAX_LENGTH) {
			status = status.trim();
		}
		
		aRr.publishStatus(status, new AbstractRequestListener<StatusSetResponseBean>() {
			
			@Override
			public void onRenrenError(RenrenError renrenError) {			
				if(renrenError != null) {
					Util.logger(renrenError.getMessage());
				} else {
					Util.logger("Unknown Error in status publish process.");
				}
				showToast(StatusPubActivity.this
						.getString(R.string.renren_sdk_status_publish_failed));
				StatusPubActivity.this.finish();
			}
			
			@Override
			public void onFault(Throwable fault) {				
				if(fault != null) {
					Log.i("DEBUG", "onFault");
					Util.logger(fault.getMessage());
				} else {
					Util.logger("Unknown fault in status publish process.");
				}
				showToast(StatusPubActivity.this
						.getString(R.string.renren_sdk_status_publish_failed));
				StatusPubActivity.this.finish();
			}
			
			@Override
			public void onComplete(StatusSetResponseBean bean) {
				showToast(StatusPubActivity.this
						.getString(R.string.renren_sdk_status_publish_success));
				StatusPubActivity.this.finish();
			}

		}, true);
	}

	private void showToast(final String message) {
		handler.post(new Runnable() {			
			@Override
			public void run() {
				Toast.makeText(StatusPubActivity.this, message, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	@Override
	protected void onStop() {		
		removeDialog(PROGRESS_DIALOG);
		super.onStop();
	}
}
