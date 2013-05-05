/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.photos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
 * 上传照片的界面
 * 
 * @author sunting ting.sun@renren-inc.com
 * 
 */
public class UploadPhotoActivity extends AbstractRenrenRequestActivity {
	/**
	 * 标识数据状态的消息值，依次为发送成功，出现异常，出现严重错误
	 */
	private final static int DATA_COMPLETE = 0xffff;
	private final static int DATA_ERROR = 0xfffe;
	private final static int DATA_FAULT = 0xfffd;

	/**
	 * bundle中错误信息的标识符
	 */
	private final static String ERROR_MSG = "error_message";

	/**
	 * 上传的文件
	 */
	File file;
	/**
	 * 照片描述
	 */
	String caption;

	/**
	 * 相册aid
	 */
	TextView photoAidValue;
	/**
	 * 照片的描述
	 */
	EditText photoCaptionValue;
	/**
	 * 照片描述的字数计数器
	 */
	TextView photoCaptionCounter;
	/**
	 * 照片的缩略图
	 */
	ImageView photoViewImage;

	/**
	 * 提交按钮，上传照片
	 */
	Button submit;
	/**
	 * 取消上传
	 */
	Button cancel;
	/**
	 * 上传的照片请求参数实体
	 */
	PhotoUploadRequestParam photoParam = new PhotoUploadRequestParam();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// 隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		super.onCreate(savedInstanceState);

		// 获取intent传来的文件数据和renren对象
		Intent intent = getIntent();
		file = (File) intent.getSerializableExtra("file");
		if (intent.hasExtra("caption")) {
			caption = intent.getStringExtra("caption");
		}

		if (renren == null) {
			// 人人对象获取失败，无法执行创建相册的操作，直接退出本Activity
			Util.logger("renren object is null, can't upload photo!");
			showTip("无法上传照片，请稍后重试！");

			finish();
		}
		renren.init(this);

		// 权限验证，如果用户已经登录并且权限满足，则初始化界面，否则结束Activity
		authorize();
	}

	private void initComponents() {
		setContentView(R.layout.renren_sdk_upload_photo);

		// 初始化头像和名字控件
		ProfilePhotoView profilePhotoView = (ProfilePhotoView) findViewById(R.id.renren_sdk_profile_photo);
		profilePhotoView.setUid(renren.getCurrentUid());

		ProfileNameView profileNameView = (ProfileNameView) findViewById(R.id.renren_sdk_profile_name);
		profileNameView.setUid(renren.getCurrentUid(), renren);

		// 用renren创建相册帮助类，方便后面调用API接口
		final PhotoHelper photoHelper = new PhotoHelper(renren);

		photoCaptionValue = (EditText) findViewById(R.id.renren_sdk_photo_caption_value);
		photoCaptionCounter = (TextView) findViewById(R.id.renren_sdk_photo_caption_counter);

		// 显示默认的照片描述和字数统计
		if (caption != null) {
			int length = caption.length();
			if (length > PhotoUploadRequestParam.CAPTION_MAX_LENGTH) {
				caption = caption.substring(0, 140);
			}
			photoCaptionValue.setText(caption);
			int index = caption.length();
			photoCaptionValue.setSelection(index);
			photoCaptionCounter.setText(length + "/"
					+ PhotoUploadRequestParam.CAPTION_MAX_LENGTH);
		}

		// 增加相片描述文本框的监听事件
		photoCaptionValue.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 设置计数器
				photoCaptionCounter.setText(s.length() + "/"
						+ PhotoUploadRequestParam.CAPTION_MAX_LENGTH);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		photoViewImage = (ImageView) findViewById(R.id.renren_sdk_photo_view_image);
		// 设置缩略图
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			Util.logger("exception in setting thumbnail: " + e.getMessage());
		}
		photoViewImage.setImageBitmap(bitmap);

		submit = (Button) findViewById(R.id.renren_sdk_upload_photo_submit);
		cancel = (Button) findViewById(R.id.renren_sdk_upload_photo_cancel);

		submit.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 设置caption参数
				String caption = photoCaptionValue.getText().toString();
				if (!"".equals(caption)) {
					photoParam.setCaption(caption);
				}

				photoParam.setFile(file);
				photoHelper.asyncUploadPhoto(photoParam,
						new AbstractRequestListener<PhotoUploadResponseBean>() {
							@Override
							public void onRenrenError(RenrenError renrenError) {
								if (renrenError != null) {
									// 传递消息
									Message message = new Message();
									Bundle bundle = new Bundle();
									bundle.putString(ERROR_MSG,
											renrenError.getMessage());
									message.what = DATA_ERROR;
									message.setData(bundle);
									handler.sendMessage(message);

									Util.logger("exception in uploading photo: "
											+ renrenError.getMessage());
								}
							}

							@Override
							public void onFault(Throwable fault) {
								if (fault != null) {
									handler.sendEmptyMessage(DATA_FAULT);

									Util.logger("fault in uploading photo: "
											+ fault.getMessage());
								}
							}

							@Override
							public void onComplete(
									PhotoUploadResponseBean photoResponse) {
								if (photoResponse != null) {
									handler.sendEmptyMessage(DATA_COMPLETE);

									Util.logger("sucess uploading photo! \n"
											+ photoResponse);
								}
							}
						});

				// 正在上传照片，显示进度框
				showProgress("上传中...");
			}
		});

		cancel.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	/**
	 * 调用AuthorizationHelper.check进行验证
	 */
	private void authorize() {
		AuthorizationHelper.check(renren, UploadPhotoActivity.this,
				new String[] { PhotoHelper.UPLOAD_PHPTO_PERMISSION },
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

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case DATA_COMPLETE:
				// 界面显示完成，结束进度框
				finishProgress();

				// 上传成功，直接显示成功
				showTip("上传成功");

				UploadPhotoActivity.this.finish();
				break;
			case DATA_ERROR:
				finishProgress();

				Bundle bundle = msg.getData();
				String errorMessage = "上传失败";
				if (bundle != null) {
					String error = bundle.getString(ERROR_MSG);
					if (error != null || !"".equals(error)) {
						errorMessage = "，" + error;
					}
				}
				showTip(errorMessage);

				break;
			case DATA_FAULT:
				finishProgress();

				// 上传出现未知系统错误，直接显示失败
				showTip("上传失败");

				break;
			default:
				finishProgress();
				break;
			}
		}
	};

}
