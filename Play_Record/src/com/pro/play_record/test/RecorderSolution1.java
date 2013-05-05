package com.pro.play_record.test;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.pro.play_record.R;

public class RecorderSolution1 extends Activity {
	private Button record_btn;
	private Button playback_btn;
	private TextView tip;
	public Activity activity = this;
	private String audioFileHolder = "/mnt/sdcard/tmp/juice.3gp";
	ImageView iv_vu;

	private void initViews() {
		iv_vu = (ImageView) findViewById(R.id.iv_vu);
		record_btn = (Button) this.findViewById(R.id.record);
		playback_btn = (Button) this.findViewById(R.id.play);
		tip = (TextView) this.findViewById(R.id.tip);
		record_btn.setOnTouchListener(new ButtonTouch());
		playback_btn.setOnTouchListener(new ButtonTouch());
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
	}

	final StringBuffer sb = new StringBuffer("");
	MediaRecorder recorder;
	MediaPlayer mp = new MediaPlayer();
	// 录音代码，省略30 line
	// public static MediaRecorder mRecorder;

	// 启动一线程，来更新ui状态
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 65535) {
				int vuSize = msg.arg1;
				tip.setText("输入音量    " + vuSize);

				if (vuSize == 0) {
					iv_vu.setImageResource(R.drawable.amp_land_1);
				} else if (vuSize == 1) {
					iv_vu.setImageResource(R.drawable.amp_land_2);
				} else if (vuSize == 2) {
					iv_vu.setImageResource(R.drawable.amp_land_3);
				} else if (vuSize == 3) {
					iv_vu.setImageResource(R.drawable.amp_land_4);
				} else if (vuSize == 4) {
					iv_vu.setImageResource(R.drawable.amp_land_5);
				} else if (vuSize == 5) {
					iv_vu.setImageResource(R.drawable.amp_land_6);
				} else if (vuSize == 6) {
					iv_vu.setImageResource(R.drawable.amp_land_7);
				} else if (vuSize == 7) {
					iv_vu.setImageResource(R.drawable.amp_land_8);
				} else if (vuSize == 8) {
					iv_vu.setImageResource(R.drawable.amp_land_9);
				} else if (vuSize == 9) {
					iv_vu.setImageResource(R.drawable.amp_land_10);
				} else if (vuSize == 10) {
					iv_vu.setImageResource(R.drawable.amp_land_11);
				}

			}
		}
	};
	private Runnable mUpdateMicStatusTimer = new Runnable() {
		public void run() {
			updateMicStatus();
		}
	};

	/**
	 * 更新话筒状态
	 */
	private void updateMicStatus() {
		if (recorder != null) {
			int vuSize = 10 * recorder.getMaxAmplitude() / 32768;
			mHandler.postDelayed(mUpdateMicStatusTimer, 100);
			Message msg = mHandler.obtainMessage();
			msg.what = 65535;
			msg.arg1 = vuSize;
			mHandler.sendMessage(msg);
		}
	}

	class ButtonTouch implements View.OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			if (v.getId() == R.id.record) {
				File file = new File("/mnt/sdcard/tmp/");
				if (!file.exists()) {
					file.mkdirs();
				}
				if (MotionEvent.ACTION_DOWN == event.getAction()) {
					sb.append("开始录制……\n");
					tip.setText(sb);
					recorder = new MediaRecorder();
					recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
					recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
					recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
					recorder.setOutputFile(audioFileHolder);

					try {
						recorder.prepare();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					new Thread(mUpdateMicStatusTimer).start();
					recorder.start();
				} else if (MotionEvent.ACTION_UP == event.getAction()) {
					recorder.stop();
					recorder.release();
					recorder = null;
					sb.append("停止录制！\n");
					tip.setText(sb);
					/**
					 * 开始播放
					 */
					// try {
					// mp.setDataSource(audioFileHolder);
					// mp.prepare();
					// mp.start();
					// } catch (IllegalArgumentException e) {
					// e.printStackTrace();
					// } catch (IllegalStateException e) {
					// e.printStackTrace();
					// } catch (IOException e) {
					// e.printStackTrace();
					// }

				}
			} else if (v.getId() == R.id.play) {
				if (MotionEvent.ACTION_DOWN == event.getAction()) {
				} else if (MotionEvent.ACTION_UP == event.getAction()) {
				}
			}
			return false;
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (mp.isPlaying()) {
			mp.stop();
		}
		recorder.stop();
		recorder.release();
		recorder = null;
	}
}
