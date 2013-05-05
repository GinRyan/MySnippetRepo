package com.renren.api.connect.android.demo.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.renren.api.connect.android.AsyncRenren;
import com.renren.api.connect.android.common.AbstractRequestListener;
import com.renren.api.connect.android.demo.R;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.friends.FriendsGetFriendsRequestParam;
import com.renren.api.connect.android.friends.FriendsGetFriendsResponseBean;

/**
 * 
 * @author hecao (he.cao@renren-inc.com)
 * 
 * friends.getFriends接口的测试界面
 */
public class FriendsGetFriendsActivity extends BaseActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout layout = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.friends_get_layout, null);
		root.addView(layout);
	
		titlebarText.setText(getString(R.string.user_info_group));		
		
		Button runButton = (Button) layout.findViewById(R.id.run);
		final EditText editTextLog = (EditText) layout.findViewById(R.id.log);
		runButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				if (renren != null) {
					AsyncRenren asyncRenren = new AsyncRenren(renren);
					showProgress();
					FriendsGetFriendsRequestParam param = new FriendsGetFriendsRequestParam();
					AbstractRequestListener<FriendsGetFriendsResponseBean> listener = new AbstractRequestListener<FriendsGetFriendsResponseBean>() {

						@Override
						public void onComplete(final FriendsGetFriendsResponseBean bean) {
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									editTextLog.setText(bean.toString());
									dismissProgress();
								}
							});
						}

						@Override
						public void onRenrenError(final RenrenError renrenError) {
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									editTextLog.setText(renrenError.getMessage());
									dismissProgress();
								}
							});
						}

						@Override
						public void onFault(final Throwable fault) {
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									editTextLog.setText(fault.getMessage());
									dismissProgress();
								}
							});
						}
					};
					asyncRenren.getFriends(param, listener);
					
				}
				
			}
		});
	}

}
