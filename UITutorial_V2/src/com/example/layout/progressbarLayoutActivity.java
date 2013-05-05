package com.example.layout;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

public class progressbarLayoutActivity extends Activity {
	TextView m_progressbarlayout_barstatus;
	ProgressBar m_progressbar1;
	ProgressBar m_progressbar2;
	int m_barstatus;
	SeekBar m_progressbarlayout_seekBar1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(com.example.R.layout.progressbarlayout);
		m_progressbarlayout_seekBar1 = (SeekBar) findViewById(com.example.R.id.progressbarlayout_seekBar1);
		
		m_progressbarlayout_barstatus = (TextView) findViewById(com.example.R.id.progressbarlayout_barstatus);
		m_barstatus = 0;
		m_progressbar2 = (ProgressBar) findViewById(com.example.R.id.progressbarlayout_progressBar2);

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (m_barstatus++ <= 100) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					m_progressbar2.setProgress(m_barstatus);

				}
			}
		}).start();
		// fresh();
	}

	void fresh() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (m_barstatus++ <= 100) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					m_progressbarlayout_barstatus.setText(m_barstatus);
				}
			}
		}).start();
	}
}
