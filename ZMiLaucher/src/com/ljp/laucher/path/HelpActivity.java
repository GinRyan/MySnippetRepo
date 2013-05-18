package com.ljp.laucher.path;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;

import com.ljp.laucher.R;
import com.ljp.laucher.myview.GalleryFlow;
import com.ljp.laucher.util.Configure;

public class HelpActivity extends Activity {
	ImageButton back;GalleryFlow galleryFlow ;Button btn;
	int position=0;SharedPreferences sp;
	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);

		setContentView(R.layout.layout_more_help);
		sp=getSharedPreferences("mysetup", 0);
		
		ImageAdapter adapter = new ImageAdapter(HelpActivity.this,Configure.images);

		galleryFlow = (GalleryFlow) findViewById(R.id.bg_gallery);
		galleryFlow.setFadingEdgeLength(0);
		galleryFlow.setSpacing(50); // 图片之间的间距
		galleryFlow.setAdapter(adapter);
		galleryFlow.setSelection(sp.getInt("bg_id", 0));
		galleryFlow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int positions, long arg3) {
				// TODO Auto-generated method stub
				position=positions;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		btn = (Button) findViewById(R.id.btn_sel);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sp.edit().putInt("bg_id", position).commit();
				System.out.println(position+"=="+getSharedPreferences("mysetup", 0).getInt("bg_id", 0)+"==");
				Intent intent = new Intent();
				intent.setAction("intentToBgChange");
				sendBroadcast(intent);
				finish();
				overridePendingTransition(R.anim.anim_fromleft_toup6,
						R.anim.anim_down_toright6);
			}
		});
		back = (ImageButton) findViewById(R.id.topleftimage);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.anim_fromleft_toup6,
						R.anim.anim_down_toright6);
			}
		});
	}

	// 退出
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.anim_fromleft_toup6,
					R.anim.anim_down_toright6);
			return false;
		}
		return false;
	}
}
