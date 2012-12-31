package com.riderim.test;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.playertest.R;

public class TestEntry extends Activity {
	Context context = this;
	Button btn_play;
	Button btn_stop;
	TextView tv_time;
	String url = "http://192.168.2.166:8081/audio/moli.amr";
	String url2 = "http://zhangmenshiting2.baidu.com/data2/music/5334074/5334074.mp3?xcode=ac53678a0be857d4be4ea870fa86170e&mid=0.58843699094599";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_entry);
		btn_play = (Button) super.findViewById(R.id.btn_play);
		btn_stop = (Button) super.findViewById(R.id.btn_stop);
		tv_time = (TextView) super.findViewById(R.id.time_per);

		btn_play.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					FileDownloader fd = new FileDownloader();
					String filepath = fd.downloadFile(url2,
							new FileDownloader.OnFinishedDownloadingDoing() {

								@Override
								public void Start(String filepath) {
									startUpload(filepath);
								}
							});
					System.out.println("文件：" + filepath + "  已经完成下载，大小"
							+ filepath.length() / 1024 + "KB");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		btn_stop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				stopplaying();
			}
		});
	}

	MediaPlayer mp = new MediaPlayer();

	public void startUpload(String filepath) {
		long start = System.currentTimeMillis();
		try {
			long end = System.currentTimeMillis();
			Log.v("url", "用时：" + (end - start) + "ms");
			if (mp != null) {
				mp.reset();
				mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
				mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
					@Override
					public void onBufferingUpdate(MediaPlayer mp, int percent) {
						int millis = mp.getCurrentPosition();
						int min = (millis / 1000) / 60;
						int sec = 0;
						if ((sec = millis / 1000) >= 60) {
							sec -= 60;
						}
						String time_x = min + ":" + sec;
						tv_time.setText(time_x + "  " + percent + "%");
					}
				});
				mp.setDataSource(filepath);
				mp.prepare();
				mp.start();
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stopplaying() {
		if (mp.isPlaying()) {
			mp.stop();
			mp.reset();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mp.release();
	}
}
