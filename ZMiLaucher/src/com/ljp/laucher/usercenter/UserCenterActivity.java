package com.ljp.laucher.usercenter;

import java.util.List;

import weibo4android.Weibo;
import weibo4android.WeiboException;
import weibo4android.http.AccessToken;
import weibo4android.http.RequestToken;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ljp.laucher.R;
import com.ljp.laucher.util.Configure;
import com.ljp.laucher.util.OAuthConstantBean;

public class UserCenterActivity extends Activity {

	ProgressDialog progressDialog;
	SharedPreferences sp_user;

	private TextView u_name;
	Button log_button,btn_back;RelativeLayout relate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_usercenter);

		sp_user = getSharedPreferences("sp_users", 0);
		
		u_name = (TextView) findViewById(R.id.sinauser);
		log_button = (Button) findViewById(R.id.sinaload);
		btn_back = (Button) findViewById(R.id.btn_back);
		relate = (RelativeLayout) findViewById(R.id.sina_relate);
		if (Configure.N_USER_NAME==null||Configure.N_USER_NAME.equals("")) {
			u_name.setText("新浪微博");
			log_button.setText("登录");
			log_button.setTextColor(Color.WHITE);
			log_button.setBackgroundResource(R.drawable.more_userset_register);
		} else {
			log_button.setText("注销");
			log_button.setTextColor(Color.GRAY);
			log_button.setBackgroundResource(R.drawable.more_userset_login);
			u_name.setText(Configure.N_USER_NAME);
		}
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				overridePendingTransition(R.anim.anim_fromleft_toup6, R.anim.anim_down_toright6);
			}
		});
		relate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (Configure.N_USER_NAME ==null ||Configure.N_USER_NAME.equals("")) {
					progressDialog = ProgressDialog.show(UserCenterActivity.this,
							"请稍等片刻...", "马上为您准备登录", true, true);
					new Thread() {
						public void run() {

							System.setProperty("weibo4j.oauth.consumerKey",
									Weibo.CONSUMER_KEY);
							System.setProperty("weibo4j.oauth.consumerSecret",
									Weibo.CONSUMER_SECRET);
							String authUrl =null;
							Weibo weibo = new Weibo();
							RequestToken requestToken;
							try {
								requestToken = weibo
										.getOAuthRequestToken("life://LoginActivity");
								OAuthConstantBean.getInstance()
										.setRequestToken(requestToken);
								authUrl = requestToken
										.getAuthenticationURL()
										+ "&display=mobile";
							
							} catch (WeiboException e) {
								e.printStackTrace();
							}
							Message msg = loginHandler.obtainMessage();
							msg.obj=authUrl;
							loginHandler.sendMessage(msg);
						}
					}.start();
				} else {
					sp_user.edit().clear().commit();
					Configure.userRegister();
					Toast.makeText(getApplicationContext(), "注销成功",
							Toast.LENGTH_SHORT).show();
					log_button.setText("登录");
					log_button.setTextColor(Color.WHITE);
					log_button
							.setBackgroundResource(R.drawable.more_userset_register);
					u_name.setText("新浪微博");
				}
			}
		});
	}
	Handler loginHandler = new Handler() {
		public void handleMessage(Message msg) {
			progressDialog.dismiss();
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("url",(String) msg.obj);
			intent.putExtras(bundle);
			intent.setClass(UserCenterActivity.this,
					UserLoginActivity.class);
			startActivity(intent);
		}
	};

	// 退出
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.anim_fromleft_toup6, R.anim.anim_down_toright6);
			return false;
		}
		return false;
	}
	Handler UIHandler = new Handler() {
		public void handleMessage(Message msg) {
			progressDialog.dismiss();
			if (Configure.N_USER_NAME !=null && !Configure.N_USER_NAME.equals("")) {
				log_button.setText("注销");
				log_button.setTextColor(Color.GRAY);
				log_button.setBackgroundResource(R.drawable.more_userset_login);
				u_name.setText(Configure.N_USER_NAME);
			}
		}
	};

	@Override
	protected void onNewIntent(final Intent intent) {
		progressDialog = ProgressDialog.show(UserCenterActivity.this,
				"请稍等片刻...", "授权验证中，马上为您跳转", true, true);
		new Thread() {
			public void run() {
				try {
					Uri uri = intent.getData();
					if (uri == null) {
						return;
					}
					Weibo weibo = OAuthConstantBean.getInstance().getWeibo();
					RequestToken requestToken = OAuthConstantBean.getInstance()
							.getRequestToken();
					String nulls = uri.getQueryParameter("oauth_verifier");
					if (nulls != null) {
						AccessToken accessToken = requestToken
								.getAccessToken(nulls);
						weibo.setToken(accessToken.getToken(),
								accessToken.getTokenSecret());

						List<weibo4android.Status> statuses = null;

						statuses = weibo.getUserTimeline();
						// statuses = weibo.get
						if (statuses.size() != 0) {
							SharedPreferences refreshtime = getSharedPreferences(
									"sp_users", 0);
							refreshtime.edit().putLong("UserId",accessToken.getUserId()).commit();
							refreshtime.edit().putString("UserName",statuses.get(0).getUser().getName()).commit();
							refreshtime.edit().putString("Token", accessToken.getToken()).commit();
							refreshtime.edit().putString("TokenSecret",accessToken.getTokenSecret()).commit();
							Configure.setUserWeibo(accessToken.getUserId(),statuses.get(0).getUser().getName(),accessToken.getToken(),accessToken.getTokenSecret());
						} else {
							Toast.makeText(getApplicationContext(), "授权失败,请重试",
									Toast.LENGTH_LONG).show();
						}
					}
				} catch (WeiboException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				Message msg = UIHandler.obtainMessage();
				UIHandler.sendMessage(msg);
			}
		}.start();

	}

}
