package com.renren.api.connect.android.demo.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.renren.api.connect.android.demo.R;
import com.renren.api.connect.android.exception.RenrenException;
import com.renren.api.connect.android.photos.AlbumCreateRequestParam;
import com.renren.api.connect.android.photos.AlbumCreateResponseBean;
import com.renren.api.connect.android.photos.AlbumPrivacyType;

/**
 * 用户自己实现界面新建相册的Activity
 * 
 * @author Sunting ting.sun@renren-inc.com
 */
public class ApiCreateAlbumActivity extends BaseActivity {

	/**
	 * 调用createAlbum方法要传递的参数
	 */
	AlbumCreateRequestParam requestAlbum = new AlbumCreateRequestParam();

	/**
	 * 相册隐私设置，不支持设置密码
	 */
	Spinner albumViewPrivilege;
	/**
	 * 相册名称
	 */
	EditText albumName;
	/**
	 * 相册拍照的地点
	 */
	EditText albumLocation;
	/**
	 * 相册描述
	 */
	EditText albumDescription;
	/**
	 * 提交按钮
	 */
	Button submit;
	/**
	 * 返回结果EdtiText
	 */
	EditText showResult;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 设置布局
		LinearLayout albumCreateLayout = (LinearLayout) LayoutInflater.from(
				this.getBaseContext()).inflate(R.layout.album_create_layout,
				null);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		root.addView(albumCreateLayout, layoutParams);

		// 初始化标题文字
		titlebarText.setText("新建相册");

		// 初始化renren对象
		if (renren == null) {
			showTip("renren对象为空，无法创建相册!");
		}

		// 初始化Spinner，由于不支持设置相册密码，所以“用密码访问”那一项要去掉
		final List<AlbumPrivacyType> spinnerValues = new ArrayList<AlbumPrivacyType>();
		AlbumPrivacyType[] albumPrivacyTypes = AlbumPrivacyType.values();

		for (int i = 0; i < albumPrivacyTypes.length; i++) {
			if (albumPrivacyTypes[i].getOunces() != AlbumPrivacyType.PASSWORD
					.getOunces()) {
				spinnerValues.add(albumPrivacyTypes[i]);
			}
		}

		albumViewPrivilege = (Spinner) findViewById(R.id.renren_demo_album_view_privilege);
		ArrayAdapter<AlbumPrivacyType> adapter = new ArrayAdapter<AlbumPrivacyType>(
				this, android.R.layout.simple_spinner_item, spinnerValues);
		adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		albumViewPrivilege.setAdapter(adapter);

		albumName = (EditText) findViewById(R.id.renren_demo_create_album_name);
		albumLocation = (EditText) findViewById(R.id.renren_demo_create_album_location);
		albumDescription = (EditText) findViewById(R.id.renren_demo_create_album_description);

		showResult = (EditText) findViewById(R.id.renren_demo_create_album_result);

		submit = (Button) findViewById(R.id.renren_demo_create_album_button);

		submit.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = albumName.getText().toString();
				String location = albumLocation.getText().toString();
				String description = albumDescription.getText().toString();

				if (name == null || TextUtils.getTrimmedLength(name) == 0) {
					showTip("相册名称不能为空！");
				} else {
					requestAlbum.setName(name);

					if (!"".equals(location)) {
						requestAlbum.setLocation(location);
					}
					if (!"".equals(description)) {
						requestAlbum.setDescription(description);
					}

					// 调用接口完成创建相册的工作
					try {
						AlbumCreateResponseBean albumBean = renren
								.createAlbum(requestAlbum);
						if (albumBean != null) {
							showTip("创建相册成功！");

							// 显示结果
							showResult.setText(albumBean.toString());
						}
					} catch (RenrenException e) {
						String errorMessage = e.getMessage();

						showTip("创建相册失败！ " + errorMessage);
						showResult.setText(errorMessage);
					} catch (Throwable fault) {
						String errorMessage = fault.getMessage();

						showTip("创建相册失败！ " + fault.getMessage());
						showResult.setText(errorMessage);
					}
				}
			}
		});
	}

}
