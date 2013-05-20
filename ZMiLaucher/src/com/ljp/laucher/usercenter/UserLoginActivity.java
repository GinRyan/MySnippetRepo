package com.ljp.laucher.usercenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ljp.laucher.R;


public class UserLoginActivity extends Activity 
{
	private WebView webView;
	private Intent intent = null;
	public static UserLoginActivity webInstance = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.layout_userlogin);
		setTitle("载入中");
		
		webInstance = this;
		webView  = (WebView)findViewById(R.id.user_login);  
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
    /*    webSettings.setSaveFormData(true);*/
        webSettings.setSavePassword(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setCacheMode( WebSettings.LOAD_NO_CACHE );
        
        webView.setOnTouchListener(new OnTouchListener()
        {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				webView.requestFocus();
				return false;
			}
        });
        
		intent = this.getIntent();
		if(!intent.equals(null))
		{
			Bundle b=intent.getExtras();
		    if(b!=null&&b.containsKey("url"))
		    {  
		    	webView.loadUrl(b.getString("url"));
		    	webView.setWebChromeClient(new WebChromeClient() {            
		    		  public void onProgressChanged(WebView view, int progress)               
		    		  {                   
		    			  setTitle("正在为您载入..." + progress + "%");
		    			  setProgress(progress * 100);

		    			  if (progress == 100)	setTitle(R.string.app_name);
		    		  }
		    	});
		    }
		}
	}
	
	
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {	
		if ( event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 )
		{
			
			finish();
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
}