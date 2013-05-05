/**
 * filename  : TestService.java
 * compiler  : eclipse 3.7.1
 * remark    : TODO 
 * created   : 2012-5-5 weeds
 * changed   : 后来者居下
 */

package service.activity.com;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class TestService extends Service {
	final Messenger mMessenger = new Messenger(new IncomeHandler());

	@Override
	public IBinder onBind(Intent intent) {
		return mMessenger.getBinder();// 这一步很重要
	}

	class IncomeHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				Bundle ma = msg.getData();// 接收activity传来的信息
				Log.v("data", "from Activity:  " + ma.getString("dd"));
				Message ms = Message.obtain(null, 2);
				ms.replyTo = mMessenger;// 设置回复的hander消息
				Bundle data = new Bundle();
				data.putString("id", "从Service传递给Activity的消息");
				ms.setData(data);
				try {
					msg.replyTo.send(ms);
				} catch (Exception e) {
					break;
				}
			}
		}
	}

	public void onStart(Intent intent, int startId) {

	}
}
