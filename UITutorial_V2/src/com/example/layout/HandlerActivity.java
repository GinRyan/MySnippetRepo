package com.example.layout;

import java.util.Calendar;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class HandlerActivity extends Activity {
	TextView m_lblHandler;
	Handler m_handler;
	int m_hour;
	int m_minute;
	int m_second;
	static final int m_what = 100000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(com.example.R.layout.handler_layout);

		m_lblHandler = (TextView) findViewById(com.example.R.id.handler_layout_lblHandler);

		m_handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub

				switch (msg.what) {
				case m_what:
					m_lblHandler.setText(m_hour+":"+m_minute+":"+m_second);
					break;
				}
			}
		};
		
		//方法1
		//Timer timer=new Timer();
		//timer.scheduleAtFixedRate(new MyTimerTask(),1,100);
		
		//方法2
		Thread thread=new LooperThread();
		thread.start();

	}

	class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Calendar calendar = Calendar.getInstance();
			m_hour = calendar.getTime().getHours();
			m_minute =calendar.getTime().getMinutes();
			m_second = calendar.getTime().getSeconds();
			Message m = new Message();		
			m.what = HandlerActivity.this.m_what;
			m_handler.sendMessage(m);
		}

	}

	class LooperThread extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub

			while (true) {
				Calendar calendar = Calendar.getInstance();
				
				m_hour = calendar.getTime().getHours();
				m_minute =calendar.getTime().getMinutes();
				m_second = calendar.getTime().getSeconds();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message m = new Message();
				m.what = HandlerActivity.this.m_what;
				m_handler.sendMessage(m);
			}

		}
	}
}
