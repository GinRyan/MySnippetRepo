package com.open.platform;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.weibo.net.AccessToken;
import com.weibo.net.DialogError;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboDialogListener;
import com.weibo.net.WeiboException;
import com.weibo.net.WeiboParameters;

public class Com_weibo_openplatformActivity extends Activity {
	/** Called when the activity is first created. */
	private TextView tv1;
	private Button bt1;
	public static final String URL_ACTIVITY_CALLBACK = "http://weibo.com/u/1879583355";
	
	Weibo weibo = Weibo.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		tv1 = (TextView) findViewById(R.id.tv1);
		bt1 = (Button) findViewById(R.id.bt1);
		bt1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				 weibo.setupConsumerConfig(Weibo.getAppKey(),
				 Weibo.getAppSecret());
				 weibo.setRedirectUrl(URL_ACTIVITY_CALLBACK);
				 weibo.authorize(Com_weibo_openplatformActivity.this,new
				 AuthListener());
				

			}
		});

	}

	class AuthListener implements WeiboDialogListener {

		@Override
		public void onComplete(Bundle values) {
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			AccessToken accessToken = new AccessToken(token,
					Weibo.getAppSecret());
			accessToken.setExpiresIn(expires_in);
			Weibo.getInstance().setAccessToken(accessToken);
			try {
				tv1.setText("Token：" + token + "\nExpiresIn：" + expires_in
						+ "\n");
				System.out.println("微博返回JSON：" + getFriendsTimeLine(weibo));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 运行发送微博接口

		}

		@Override
		public void onWeiboException(WeiboException e) {

		}

		@Override
		public void onError(DialogError e) {

		}

		@Override
		public void onCancel() {

		}

	}

	private String getFriendsTimeLine(Weibo weibo) throws Exception {
		String url = Weibo.SERVER + "statuses/friends_timeline.json";
		WeiboParameters weiboPara = new WeiboParameters();
		weiboPara.add("source", Weibo.getAppKey());
		String result = weibo.request(this, url, weiboPara, "GET",
				weibo.getAccessToken());
		return result;
	}
}
