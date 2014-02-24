package org.music.cs.ui;

import org.music.cs.R;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);

		Resources res = getResources();
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;
		intent = new Intent().setClass(this, ListActivity.class);
		spec = tabHost.newTabSpec("音乐").setIndicator("音乐", res.getDrawable(R.drawable.item)).setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, ArtistsActivity.class);
		spec = tabHost.newTabSpec("艺术家").setIndicator("艺术家", res.getDrawable(R.drawable.artist)).setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, AlbumsActivity.class);
		spec = tabHost.newTabSpec("专辑").setIndicator("专辑", res.getDrawable(R.drawable.album)).setContent(intent);
		tabHost.addTab(spec);
		// 没有做最近播放功能
		// intent = new Intent().setClass(this, SongsActivity.class);
		// spec = tabHost.newTabSpec("最近播放").setIndicator("最近播放",
		// res.getDrawable(R.drawable.album)).setContent(intent);
		// tabHost.addTab(spec);

		tabHost.setCurrentTab(0);

	}
}