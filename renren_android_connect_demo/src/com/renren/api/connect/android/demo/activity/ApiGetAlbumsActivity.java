package com.renren.api.connect.android.demo.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.renren.api.connect.android.AsyncRenren;
import com.renren.api.connect.android.common.AbstractRequestListener;
import com.renren.api.connect.android.demo.R;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.photos.AlbumGetRequestParam;
import com.renren.api.connect.android.photos.AlbumGetResponseBean;

/**
 * 获取相册Activity
 * 
 * @author Sunting ting.sun@renren-inc.com
 */
public class ApiGetAlbumsActivity extends BaseActivity {
	/**
	 * 调用获取相册API接口需传入的参数
	 */
	AlbumGetRequestParam albumGetRequestParam = new AlbumGetRequestParam();
	/**
	 * 要查询的相册拥有者的uid，为空则查询当前登录用户的相册信息
	 */
	EditText albumUid;
	/**
	 * 要查询的相册的aid，以逗号隔开，同时查询的相册不能超过10个
	 */
	EditText albumAids;
	/**
	 * 查询按钮
	 */
	Button search;
	/**
	 * 显示查询结果
	 */
	EditText showResult;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 设定布局
		LinearLayout albumsGetLayout = (LinearLayout) LayoutInflater.from(
				this.getBaseContext())
				.inflate(R.layout.albums_get_layout, null);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		root.addView(albumsGetLayout, layoutParams);

		// 初始化标题文字
		titlebarText.setText("获取相册");

		// 获取intent中的renren和file对象
		if (renren == null) {
			showTip("renren对象为空，无法获取相册！");
		}

		albumUid = (EditText) findViewById(R.id.renren_demo_get_albums_uid);
		albumAids = (EditText) findViewById(R.id.renren_demo_get_albums_aids);

		search = (Button) findViewById(R.id.renren_demo_get_albums_button);

		showResult = (EditText) findViewById(R.id.renren_demo_get_albums_result);

		search.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 设置调用获取相册API的参数
				// 先判断输入数据的合法性，是否为long型数据
				CharSequence uid = albumUid.getText();
				CharSequence aids = albumAids.getText();

				// 如果输入的uid都是数字，那么将输入值转换为Long型，并设置到参数中
				if (TextUtils.isDigitsOnly(albumUid.getText())) {
					try {
						albumGetRequestParam.setUid(Long.valueOf(uid.toString()));
					} catch (NumberFormatException e) {
						// 如果转换过程出现异常，则不处理，也不抛出异常，默认参数uid为null
					}
				}

				// 设置aids参数
				albumGetRequestParam.setAids(aids.toString());

				// 调用SDK异步接口获取相册
				new AsyncRenren(renren).getAlbums(albumGetRequestParam,
						new AbstractRequestListener<AlbumGetResponseBean>() {
							@Override
							public void onRenrenError(
									final RenrenError renrenError) {
								ApiGetAlbumsActivity.this
										.runOnUiThread(new Runnable() {
											@Override
											public void run() {
												// 查询出错，结束进度框，显示错误信息
												dismissProgress();

												showResult.setText(renrenError
														.getMessage());
											}
										});
							}

							@Override
							public void onFault(final Throwable fault) {
								ApiGetAlbumsActivity.this
										.runOnUiThread(new Runnable() {
											@Override
											public void run() {
												// 查询出错，结束进度框，显示错误信息
												dismissProgress();

												showResult.setText(fault
														.getMessage());
											}
										});
							}

							@Override
							public void onComplete(
									final AlbumGetResponseBean bean) {
								ApiGetAlbumsActivity.this
										.runOnUiThread(new Runnable() {
											@Override
											public void run() {
												// 查询完成，结束进度框，然后显示结果
												dismissProgress();

												showResult.setText(bean
														.toString());
											}
										});
							}
						});

				// 正在查询相册信息
				showProgress("正在查询", "Wating...");
			}
		});
	}

}
