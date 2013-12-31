package com.androidthirdparty.appops;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
			Intent intent = new Intent();
			intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$AppOpsSummaryActivity"));
			startActivity(intent);
			finish();
		} else {
			new AlertDialog.Builder(this).setTitle("Warning：").setMessage("Android系统版本需要在4.3以及以上才能启用权限管理").setNegativeButton("知道了", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			}).show();
		}
	}

}
