package com.ljp.laucher.path;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.KeyEvent;

import com.ljp.laucher.R;

public class SetActivity extends PreferenceActivity {
	SharedPreferences mPreferences;String[] array;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		array = getResources().getStringArray(R.array.entriesvalue_list_preference);
	
		mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
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











