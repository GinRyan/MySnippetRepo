package com.pocketdigi.Notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class main extends Activity {
    /** Called when the activity is first created. */
	int notification_id=19172439;
	NotificationManager nm;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Button bt1=(Button)findViewById(R.id.bt1);
        bt1.setOnClickListener(bt1lis);
        Button bt2=(Button)findViewById(R.id.bt2);
        bt2.setOnClickListener(bt2lis);
        
    }
    OnClickListener bt1lis=new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showNotification(R.drawable.home,"图标边的文字","标题","内容");
		}
    	
    };
    OnClickListener bt2lis=new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//showNotification(R.drawable.home,"图标边的文字","标题","内容");
			nm.cancel(notification_id);
		}
    	
    };
    public void showNotification(int icon,String tickertext,String title,String content){
    	//设置一个唯一的ID，随便设置
    	
    	//Notification管理器
    	Notification notification=new Notification(icon,tickertext,System.currentTimeMillis());
    	//后面的参数分别是显示在顶部通知栏的小图标，小图标旁的文字（短暂显示，自动消失）系统当前时间（不明白这个有什么用）
    	notification.defaults=Notification.DEFAULT_ALL; 
    	//这是设置通知是否同时播放声音或振动，声音为Notification.DEFAULT_SOUND
    	//振动为Notification.DEFAULT_VIBRATE;
    	//Light为Notification.DEFAULT_LIGHTS，在我的Milestone上好像没什么反应
    	//全部为Notification.DEFAULT_ALL
    	//如果是振动或者全部，必须在AndroidManifest.xml加入振动权限
    	PendingIntent pt=PendingIntent.getActivity(this, 0, new Intent(this,main.class), 0);
    	//点击通知后的动作，这里是转回main 这个Acticity
    	notification.setLatestEventInfo(this,title,content,pt);
    	nm.notify(notification_id, notification);
    	
    }
}