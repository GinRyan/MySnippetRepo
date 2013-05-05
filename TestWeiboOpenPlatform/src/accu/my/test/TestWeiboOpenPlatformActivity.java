package accu.my.test;

import com.weibo.net.RequestToken;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboException;

import android.app.Activity;
import android.os.Bundle;

/*
 *Œ“ «Ω¥”Õƒ–
 * */
public class TestWeiboOpenPlatformActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Weibo weibo = Weibo.getInstance();
		String callback_url = "";
		try {
			RequestToken requestToken = weibo.getRequestToken(getApplication(),
					weibo.getAppKey(), weibo.getAppSecret(), callback_url);

		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
}