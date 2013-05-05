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
import com.renren.api.connect.android.users.UsersGetInfoRequestParam;
import com.renren.api.connect.android.users.UsersGetInfoResponseBean;

/**
 * 
 * @author hecao (he.cao@renren-inc.com)
 * 
 * users.getInfo接口的测试界面
 */
public class ApiUsersInfoActivity extends BaseActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout layout = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.usrs_info_layout, null);
		root.addView(layout);
	
		titlebarText.setText(getString(R.string.user_info_group));		
		
		Button runButton = (Button) layout.findViewById(R.id.run);
		final EditText editTextUids = (EditText) layout.findViewById(R.id.ids);
		final EditText editTextLog = (EditText) layout.findViewById(R.id.log);
		runButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				if (renren != null) {
					AsyncRenren asyncRenren = new AsyncRenren(renren);
					String[] uids = parseCommaIds(editTextUids.getText().toString());
					if (uids == null) {
						return;
					}
					showProgress();
					UsersGetInfoRequestParam param = new UsersGetInfoRequestParam(uids);
					AbstractRequestListener<UsersGetInfoResponseBean> listener = new AbstractRequestListener<UsersGetInfoResponseBean>() {

						@Override
						public void onComplete(final UsersGetInfoResponseBean bean) {
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
					asyncRenren.getUsersInfo(param, listener );
					
				}
				
			}
		});
	}

}
