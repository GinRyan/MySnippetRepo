/**
 * $id$
 */
package com.renren.api.connect.android.demo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.demo.R;

/**
 * Base class for request Activity in the demo application
 * 
 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com)
 */
public class BaseActivity extends Activity {
	/**
	 * 根布局对象
	 */
	protected LinearLayout root;

	/**
	 * 标题栏左边按钮
	 */
	protected Button titlebarLeftButton;

	/**
	 * 标题栏中间文字
	 */
	protected TextView titlebarText;

	/**
	 * 标题栏右边按钮
	 */
	protected Button titlebarRightButton;

	private ProgressDialog progressDialog;

	/**
	 * 调用SDK接口的Renren对象
	 */
	protected Renren renren;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置不显示标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置布局
		setContentView(R.layout.base_layout);
		// 初始化控件
		root = (LinearLayout) findViewById(R.id.renren_demo_root);
		titlebarLeftButton = (Button) findViewById(R.id.renren_demo_titlebar_left_button);
		titlebarRightButton = (Button) findViewById(R.id.renren_demo_titlebar_right_button);
		titlebarText = (TextView) findViewById(R.id.renren_demo_titlebar_title_text);

		// 注册“查看Log”按钮事件
		titlebarRightButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BaseActivity.this, LogActivity.class);
				BaseActivity.this.startActivity(intent);
			}
		});

		// 注册“返回”按钮事件(子类若有不同的处理，则需覆盖此事件)
		titlebarLeftButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		Intent intent = getIntent();
		renren = intent.getParcelableExtra(Renren.RENREN_LABEL);
		if (renren != null) {
			renren.init(this);
		}
	}

	/**
	 * 解析逗号分割的字符串
	 * 
	 * @return
	 */
	protected String[] parseCommaIds(String s) {
		if (s == null) {
			return null;
		}
		String[] ids = s.split(",");
		return ids;
	}

	/**
	 * 显示等待框
	 */
	protected void showProgress() {
		showProgress("Please wait", "progressing");
	}

	/**
	 * 显示等待框
	 * 
	 * @param title
	 * @param message
	 */
	protected void showProgress(String title, String message) {
		progressDialog = ProgressDialog.show(this, title, message);
	}

	/**
	 * 取消等待框
	 */
	protected void dismissProgress() {
		if (progressDialog != null) {
			try {
				progressDialog.dismiss();
			} catch (Exception e) {

			}
		}
	}

	/**
	 * 显示Toast提示
	 * 
	 * @param message
	 */
	protected void showTip(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 不销毁ProgressDialog会出现view not attached to window manager异常
		dismissProgress();
	}

}
