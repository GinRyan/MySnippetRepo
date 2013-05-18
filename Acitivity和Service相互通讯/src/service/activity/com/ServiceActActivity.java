package service.activity.com;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class ServiceActActivity extends Activity {
	Messenger mess;
	Messenger mService = new Messenger(new IncomeHandler());
	IncomeHandler hander = new IncomeHandler();
	boolean isbind;
	final int msgid = 1;

	private ServiceConnection serviceConn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			mess = null;
			isbind = false;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mess = new Messenger(service);
			isbind = true;
			sendMsgtoService();
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	public void sendMsgtoService() {
		try {
			Message m = Message.obtain(null, msgid, 0, 0);
			m.replyTo = mService;// mService用于接收service传来的hander.
			Bundle db = new Bundle();
			db.putString("dd", "从Activity发送到Service的消息");// 向service传递值
			m.setData(db);
			mess.send(m);
		} catch (Exception e) {

		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		bindService(new Intent(this, TestService.class), this.serviceConn,
				Context.BIND_AUTO_CREATE);// 绑定服务
	}

	protected void onStop() {
		super.onStop();
		// 解绑
		if (isbind) {
			unbindService(serviceConn);
			isbind = false;
		}
	}

	// 内部类
	class IncomeHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle da = msg.getData();
			if (msg.what == 2) {// 处理service接收来的信息
				Log.i("data", "from Service :  " + da.getString("id"));
			}
		}
	}
}