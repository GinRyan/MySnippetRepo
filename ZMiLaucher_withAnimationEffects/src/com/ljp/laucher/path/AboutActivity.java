package com.ljp.laucher.path;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

import com.ljp.laucher.R;

public class AboutActivity extends Activity {
	protected void onCreate(Bundle paramBundle) {

		super.onCreate(paramBundle);
		setContentView(R.layout.layout_more_about);

		ImageButton back = (ImageButton) findViewById(R.id.topleftimage);
		back.setOnClickListener(new ImageButton.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AboutActivity.this.setResult(RESULT_OK, getIntent());
				AboutActivity.this.finish();
				overridePendingTransition(R.anim.anim_fromleft_toup6, R.anim.anim_down_toright6);
			}
		});
	}
	// 退出
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.anim_fromleft_toup6, R.anim.anim_down_toright6);
			return false;
		}
		return false;
	}
}
