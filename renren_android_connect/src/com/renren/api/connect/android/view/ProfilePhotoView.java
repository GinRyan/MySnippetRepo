/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.renren.api.connect.android.Util;

/**
 * 
 * @author hecao (he.cao@renren-inc.com)
 * 
 * 用来显示当前用户头像的view
 *
 */
public class ProfilePhotoView extends LinearLayout {
	
	private static final String IMG_URL_PREFIX = "http://ic.m.renren.com/gn?op=resize&w=50&h=50&p=";

	public ProfilePhotoView(Context context) {
		super(context);
		init();
	}

	public ProfilePhotoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private Bitmap profilePhoto;

	private Handler handler;

	private ProgressBar progressBar;

	private void init() {
		handler = new Handler();
		progressBar = new ProgressBar(this.getContext());
		progressBar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		progressBar.setVisibility(VISIBLE);
		progressBar.setPadding(5, 5, 5, 5);
		this.addView(progressBar);
	}

	/**
	 * 传入用户的uid，此控件会下载用户的头像并显示
	 * @param uid 用户的uid
	 */
	public void setUid(final long uid) {

		new Thread() {

			public void run() {
				
				try {
					byte[] data = Util.getBytes(IMG_URL_PREFIX + uid, null);
					if (data != null) {
						profilePhoto = BitmapFactory.decodeByteArray(data, 0, data.length);
						final BitmapDrawable drawable = new BitmapDrawable(profilePhoto);
						handler.post(new Runnable() {

							@Override
							public void run() {
								setBackgroundDrawable(drawable);
								removeAllViews();
							}
						});
					}
				} catch (Exception e) {
					handler.post(new Runnable() {

						@Override
						public void run() {
							removeAllViews();
						}
					});
				}
			}

		}.start();

	}

}
