package com.example.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class NotificationActivity extends Activity {
	String[]m_colors=new String[]{"red","yellow","white"};
	Button m_notificationlayout_btnSend;
	Button m_btnToast;
	Button m_btnAlert;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(com.example.R.layout.notificationlayout);
		
		m_btnAlert=(Button)findViewById(com.example.R.id.notificationlayout_btnAlert);
		m_btnAlert.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			AlertDialog.Builder builder=new Builder(NotificationActivity.this);
			builder.setTitle("提示");
			//builder.setMessage("确定要退出吗？");
			builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(Intent.ACTION_VIEW,
							Uri.parse("http://www.android.com"));
					startActivity(intent);
				}

				
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			})
			.setSingleChoiceItems(m_colors, -1, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Toast.makeText(NotificationActivity.this, m_colors[which], Toast.LENGTH_LONG)
						 .show();
				}
			})
			;
			
			builder.show();
			}
		});
		
		m_btnToast=(Button)findViewById(com.example.R.id.notificationlayout_btnToast);
		m_btnToast.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast toast=Toast.makeText(NotificationActivity.this, "提示的内容", Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP|Gravity.LEFT, 50, 50);
				LayoutInflater inflater=getLayoutInflater();
				View view=inflater.inflate(com.example.R.layout.custom_toastlayout,
						(ViewGroup)findViewById( com.example.R.id.custom_toastlayout_id));
				((TextView)view.findViewById(com.example.R.id.custom_toastlayout_lblMsg))
				.setText("自定义Toast");
				toast.setView(view);
				toast.show();
			}
		});
		
		m_notificationlayout_btnSend=(Button)findViewById(com.example.R.id.notificationlayout_btnSend);
		m_notificationlayout_btnSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				
				int icon=android.R.drawable.sym_action_email;
				long when =System.currentTimeMillis();
				
				Notification notification=new Notification(icon, "开会", when);
				
				notification.defaults=Notification.DEFAULT_SOUND;
				PendingIntent contentIntent=PendingIntent.getActivity(NotificationActivity.this, 0, null, 0);
				notification.setLatestEventInfo(
						NotificationActivity.this, 
						"开会通知",
						"今天下午四点开会，请准时参加！！",
						contentIntent
						);
				notificationManager.notify(0, notification);
				
			}
		});
		
		
		
	}
}
