package com.renren.api.connect.android.demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.renren.api.connect.android.PasswordFlowRequestParam;
import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.demo.activity.ApiListActivity;
import com.renren.api.connect.android.demo.activity.BaseActivity;
import com.renren.api.connect.android.exception.RenrenAuthError;
import com.renren.api.connect.android.likeButton.view.RenrenDialog;
import com.renren.api.connect.android.view.RenrenAuthListener;

public class RenrenSDKDemo extends BaseActivity {
	
	private static final String API_KEY = "";
	
	private static final String SECRET_KEY = "";
	
	private static final String APP_ID = "";
	
	private Renren renren;
	
	private Handler handler;
	
	private Button uploadbButton;
	
	private Button oAuthButton;
	
	private Button ssoButton;
	
	private Button pwdFlowButton;
	
	private LinearLayout mainLayout;
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.main, null);
        root.addView(mainLayout);
        // 左边的返回按钮不显示
        titlebarLeftButton.setVisibility(View.GONE);
        // 设置标题
        titlebarText.setText("登录");
        
        renren = new Renren(API_KEY, SECRET_KEY, APP_ID, this);
        //Initialize the demo invoker
        ApiDemoInvoker.init(renren);
        handler = new Handler();
        
        initButtons();
    }
    
    @Override
	protected void onResume() {
		super.onResume();
		renren.init(this);
	}

	/**
     * start the api list activity
     */
    private void startApiList() {
    	Intent intent = new Intent(this, ApiListActivity.class);
    	intent.putExtra(Renren.RENREN_LABEL, renren);
    	startActivity(intent);
    }
    
    /**
     * initialize the buttons and events
     */
    private void initButtons() {
    	final RenrenAuthListener listener = new RenrenAuthListener() {

			@Override
			public void onComplete(Bundle values) {
				Log.d("test",values.toString());
				startApiList();
			}

			@Override
			public void onRenrenAuthError(
					RenrenAuthError renrenAuthError) {
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(RenrenSDKDemo.this, 
								RenrenSDKDemo.this.getString(R.string.auth_failed), 
								Toast.LENGTH_SHORT).show();
					}
				});
			}

			@Override
			public void onCancelLogin() {
			}

			@Override
			public void onCancelAuth(Bundle values) {
			}
			
		};
		
		// 未登录一键上传照片
		uploadbButton = (Button) findViewById(R.id.renren_demo_upload_photo_without_login);
		uploadbButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				PhotoDemo.uploadPhotoWithActivity(RenrenSDKDemo.this, renren);
			}
		});
    	
    	oAuthButton = (Button)mainLayout.findViewById(R.id.auth_site_mode);
    	oAuthButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				renren.authorize(RenrenSDKDemo.this, listener);
			}
		});
    	
    	ssoButton = (Button)mainLayout.findViewById(R.id.sso_mode);
    	ssoButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//use default permissions here, see Renren class for detail
				renren.authorize(RenrenSDKDemo.this, null, listener, 1);
			}
		});
    	
    	pwdFlowButton = (Button)mainLayout.findViewById(R.id.password_flow_mode);
    	pwdFlowButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PasswordFlowRequestParam param = new PasswordFlowRequestParam(getString(R.string.username), 
																				getString(R.string.password));
				renren.authorize(RenrenSDKDemo.this, param, listener);
			}
		});
    }


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (renren != null) {
			renren.authorizeCallback(requestCode, resultCode, data);
		}
	}
    
    
    
    
}