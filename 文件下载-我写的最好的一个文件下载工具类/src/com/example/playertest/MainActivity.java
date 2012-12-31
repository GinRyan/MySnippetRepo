package com.example.playertest;

import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	Button bt_play;
	Button bt_stop;
	MediaPlayer mp = new MediaPlayer();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bt_play = (Button) findViewById(R.id.play);
		bt_stop = (Button) findViewById(R.id.stop);
		bt_play.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				try {
					mp.setDataSource("http://zhangmenshiting2.baidu.com/data2/music/13915002/13915002.mp3?xcode=723f2bbc70a1e8c506edad312dee4049&mid=0.44009246423650");
					mp.prepare();
					mp.start();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		bt_stop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mp.stop();
				mp.reset();
				mp.release();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
