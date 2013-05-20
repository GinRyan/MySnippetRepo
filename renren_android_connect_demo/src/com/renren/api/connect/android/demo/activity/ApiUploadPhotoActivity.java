package com.renren.api.connect.android.demo.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.renren.api.connect.android.AsyncRenren;
import com.renren.api.connect.android.common.AbstractRequestListener;
import com.renren.api.connect.android.demo.R;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.photos.PhotoUploadRequestParam;
import com.renren.api.connect.android.photos.PhotoUploadResponseBean;

/**
 * 用户自己实现界面上传照片的Activity
 * 
 * @author Sunting ting.sun@renren-inc.com
 */
public class ApiUploadPhotoActivity extends BaseActivity {
	/**
	 * 标识数据状态的消息值，依次为发送成功，出现异常，出现严重错误
	 */
	private final static int DATA_COMPLETE = 0xffff;
	private final static int DATA_ERROR = 0xfffe;
	private final static int DATA_FAULT = 0xfffd;

	/**
	 * Bundle参数设置中的字符串标识符
	 */
	private final static String BEAN_LABEL = "bean";
	private final static String ERROR_LABEL = "error";

	/**
	 * 上传的文件对象
	 */
	File file;
	/**
	 * 相册描述
	 */
	EditText photoCaptionValue;
	/**
	 * 相册缩略图
	 */
	ImageView photoViewImage;
	/**
	 * 上传按钮
	 */
	Button uploadButton;
	/**
	 * 显示调用返回结果的EditText
	 */
	EditText showResult;
	/**
	 * 调用上传API的参数
	 */
	PhotoUploadRequestParam photoParam = new PhotoUploadRequestParam();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 设置布局
		LinearLayout photoUploadLayout = (LinearLayout) LayoutInflater.from(
				this.getBaseContext()).inflate(R.layout.photo_upload_layout,
				null);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		root.addView(photoUploadLayout, layoutParams);

		// 设置标题栏文字
		titlebarText.setText("上传照片");

		// 获取intent中的renren和file对象
		file = (File) getIntent().getSerializableExtra("file");
		// 使用BaseActivity的renren对象
		if (renren == null) {
			showTip("renren对象为空，无法上传照片！");
		}
		photoCaptionValue = (EditText) findViewById(R.id.renren_demo_photo_caption);

		photoViewImage = (ImageView) findViewById(R.id.renren_demo_photo_image);
		// 设置缩略图
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			showTip("设置缩略图出现异常");
		}
		photoViewImage.setImageBitmap(bitmap);

		uploadButton = (Button) findViewById(R.id.renren_demo_photo_upload_button);

		uploadButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 设置caption参数
				String caption = photoCaptionValue.getText().toString();
				if (caption != null && !"".equals(caption.trim())) {
					photoParam.setCaption(caption);
				}
				// 设置file参数
				photoParam.setFile(file);
				// 调用SDK异步上传照片的接口
				new AsyncRenren(renren).publishPhoto(photoParam,
						new AbstractRequestListener<PhotoUploadResponseBean>() {
							@Override
							public void onRenrenError(RenrenError renrenError) {
								if (renrenError != null) {
									Message message = new Message();
									Bundle bundle = new Bundle();
									bundle.putString(ERROR_LABEL,
											renrenError.getMessage());
									message.what = DATA_ERROR;
									message.setData(bundle);
									handler.sendMessage(message);
								}
							}

							@Override
							public void onFault(Throwable fault) {
								if (fault != null) {
									Message message = new Message();
									Bundle bundle = new Bundle();
									bundle.putString(ERROR_LABEL,
											fault.getMessage());
									message.what = DATA_FAULT;
									message.setData(bundle);
									handler.sendMessage(message);
								}
							}

							@Override
							public void onComplete(PhotoUploadResponseBean bean) {
								if (bean != null) {
									Message message = new Message();
									Bundle bundle = new Bundle();
									bundle.putParcelable(BEAN_LABEL, bean);
									message.what = DATA_COMPLETE;
									message.setData(bundle);
									handler.sendMessage(message);
								}
							}
						});

				// 正在上传照片，显示进度框
				showProgress("正在上传照片", "Wating...");
			}
		});
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			// 显示结果的EditText
			showResult = (EditText) findViewById(R.id.renren_demo_photo_upload_result);
			Bundle bundle = null;

			switch (msg.what) {
			case DATA_COMPLETE:
				// 照片上传成功，结束进度框
				dismissProgress();
				// 显示结果
				bundle = msg.getData();
				if (bundle != null) {
					PhotoUploadResponseBean bean = bundle
							.getParcelable(BEAN_LABEL);

					if (bean != null) {
						showResult.setText(bean.toString());
					}
				}
				bundle = null;

				break;
			case DATA_ERROR:
				dismissProgress();
				// 显示错误信息
				bundle = msg.getData();
				if (bundle != null) {
					String errorMessage = bundle.getString(ERROR_LABEL);

					if (errorMessage != null) {
						showResult.setText(errorMessage);
					}
				}
				bundle = null;

				break;
			case DATA_FAULT:
				dismissProgress();
				// 显示错误信息
				bundle = msg.getData();
				if (bundle != null) {
					String errorMessage = bundle.getString(ERROR_LABEL);

					if (errorMessage != null) {
						showResult.setText(errorMessage);
					}
				}
				bundle = null;

				break;
			default:
				dismissProgress();
				break;
			}
		}
	};

}
