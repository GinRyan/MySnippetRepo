/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.photos;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
 * 创建相册的界面<br>
 * 
 * @author sunting ting.sun@renren-inc.com
 * 
 */
public class CreateAlbumActivity extends AbstractRenrenRequestActivity {
	/**
	 * 标识数据状态的消息值，依次为发送成功，出现异常，出现严重错误
	 */
	private final static int DATA_COMPLETE = 0xffff;
	private final static int DATA_ERROR = 0xfffe;
	private final static int DATA_FAULT = 0xfffd;

	/**
	 * 调用createAlbum方法要传递的参数
	 */
	AlbumCreateRequestParam albumRequest = new AlbumCreateRequestParam();

	/**
	 * 相册隐私设置，不支持设置密码
	 */
	Spinner albumVisibleValue = null;
	/**
	 * 相册名称
	 */
	EditText albumNameValue = null;
	/**
	 * 相册拍照的地点
	 */
	EditText albumLocationValue = null;
	/**
	 * 相册描述
	 */
	EditText albumDescriptionValue = null;
	/**
	 * 提交按钮
	 */
	Button submit = null;
	/**
	 * 取消按钮
	 */
	Button cancel = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// 隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);

		// 使用父类的renren对象
		if (renren == null) {
			// renren对象获取失败，无法执行创建相册的操作，直接退出本Activity
			Util.logger("renren object is null, can't create album!");
			showTip("无法创建相册，请稍后重试!");
			
			finish();
		}
		renren.init(this);

		// 权限验证，如果用户已经登录并且权限满足，则初始化界面，否则结束Activity
		AuthorizationHelper.check(renren, CreateAlbumActivity.this,
				new String[] { PhotoHelper.CREATE_ALBUM_PERMISSION },
				new RenrenAuthListener() {

					@Override
					public void onRenrenAuthError(
							RenrenAuthError renrenAuthError) {
						finish();
					}

					@Override
					public void onComplete(Bundle values) {
						initComponents();
					}

					@Override
					public void onCancelLogin() {
						finish();
					}

					@Override
					public void onCancelAuth(Bundle values) {
						finish();
					}
				});
	}

	private void initComponents() {
		setContentView(R.layout.renren_sdk_create_album);
		
		// 初始化头像和名字控件
		ProfilePhotoView profilePhotoView = (ProfilePhotoView) findViewById(R.id.renren_sdk_profile_photo);
		profilePhotoView.setUid(renren.getCurrentUid());

		ProfileNameView profileNameView = (ProfileNameView) findViewById(R.id.renren_sdk_profile_name);
		profileNameView.setUid(renren.getCurrentUid(), renren);

		// 设置隐私的选项，由于不支持设置相册密码，所以“用密码访问”那一项要去掉
		final List<AlbumPrivacyType> spinnerValues = new ArrayList<AlbumPrivacyType>();
		AlbumPrivacyType[] albumPrivacyTypes = AlbumPrivacyType.values();

		for (int i = 0; i < albumPrivacyTypes.length; i++) {
			if (albumPrivacyTypes[i].getOunces() != AlbumPrivacyType.PASSWORD
					.getOunces()) {
				spinnerValues.add(albumPrivacyTypes[i]);
			}
		}

		albumVisibleValue = (Spinner) findViewById(R.id.renren_sdk_album_visible_value);
		ArrayAdapter<AlbumPrivacyType> adapter = new ArrayAdapter<AlbumPrivacyType>(
				this, android.R.layout.simple_spinner_item, spinnerValues);
		adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		albumVisibleValue.setAdapter(adapter);

		albumVisibleValue
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						albumRequest.setVisible(spinnerValues.get(position));
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}

				});

		albumNameValue = (EditText) findViewById(R.id.renren_sdk_album_name_value);
		albumLocationValue = (EditText) findViewById(R.id.renren_sdk_album_location_value);
		albumDescriptionValue = (EditText) findViewById(R.id.renren_sdk_album_description_value);

		submit = (Button) findViewById(R.id.renren_sdk_create_album_submit);
		cancel = (Button) findViewById(R.id.renren_sdk_create_album_cancel);

		submit.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = albumNameValue.getText().toString();
				String location = albumLocationValue.getText().toString();
				String description = albumDescriptionValue.getText().toString();

				if (name == null || TextUtils.getTrimmedLength(name) == 0) {
					showTip("相册名称不能为空！");
				} else {
					albumRequest.setName(name);

					if (!"".equals(location)) {
						albumRequest.setLocation(location);
					}
					if (!"".equals(description)) {
						albumRequest.setDescription(description);
					}

					// 调用接口完成创建相册的工作
					new AsyncRenren(renren)
							.createAlbum(
									albumRequest,
									new AbstractRequestListener<AlbumCreateResponseBean>() {

										@Override
										public void onRenrenError(
												RenrenError renrenError) {
											if (renrenError != null) {
												handler.sendEmptyMessage(DATA_ERROR);

												Util.logger("exception in creating album!: "
														+ renrenError.getMessage());
											}
										}

										@Override
										public void onFault(Throwable fault) {
											if (fault != null) {
												handler.sendEmptyMessage(DATA_FAULT);

												Util.logger("exception in creating album!: "
														+ fault.getMessage());
											}
										}

										@Override
										public void onComplete(
												AlbumCreateResponseBean albumResponse) {
											if (albumResponse != null) {
												handler.sendEmptyMessage(DATA_COMPLETE);

												Util.logger("success creating an album!\n"
														+ albumResponse);
											}
										}
									});

					// 正在创建相册，显示进度框
					showProgress("创建中...");
				}
			}
		});

		cancel.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case DATA_COMPLETE:
				// 界面显示完成，结束进度框
				finishProgress();

				CreateAlbumActivity.this.finish();
				break;
			case DATA_ERROR:
				finishProgress();

				CreateAlbumActivity.this.finish();
				break;
			case DATA_FAULT:
				finishProgress();

				CreateAlbumActivity.this.finish();
				break;
			default:
				finishProgress();
				
				CreateAlbumActivity.this.finish();
				break;
			}
		}
	};
	
}
