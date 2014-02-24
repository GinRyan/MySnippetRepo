package com.example2.sync;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * <pre>
 * 该范例参考了安卓开发者官网的教程编写，创建一个系统账户并启动同步适配器进行数据同步
 * 官网教程：
 * http://developer.android.com/intl/zh-cn/training/sync-adapters/index.html
 * http://developer.android.com/training/sync-adapters/creating-authenticator.html
 * http://developer.android.com/training/sync-adapters/creating-stub-provider.html
 * http://developer.android.com/training/sync-adapters/creating-sync-adapter.html
 * http://developer.android.com/training/sync-adapters/running-sync-adapter.html
 * 
 * 当然还有一个参考数据来源：
 * http://yarin.blog.51cto.com/1130898/479032
 * 
 * <pre>
 * @author Gin
 * 
 */
public class MainActivity extends AccountAuthenticatorActivity {
	Activity context = this;
	EditText username;
	EditText server;
	EditText password;
	Button commit;
	private AccountManager accountManager;
	String account_type = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		commit = (Button) findViewById(R.id.commit);
		username = (EditText) findViewById(R.id.username);
		server = (EditText) findViewById(R.id.server);
		password = (EditText) findViewById(R.id.password);
		accountManager = AccountManager.get(context);
		Account[] accounts = accountManager.getAccounts();
		for (int i = 0; i < accounts.length; i++) {
			System.out.println("账户名称：" + accounts[i].name + " 账户类型： " + accounts[i].type);
		}
		account_type = getString(R.string.com_ginryan_mycast);
		commit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Account account = new Account(username.getText().toString().trim(), account_type);
				Bundle userdata = new Bundle();
				userdata.putString("server", server.getText().toString().trim());
				if (accountManager.addAccountExplicitly(account, password.getText().toString(), userdata)) {
					Bundle result = new Bundle();
					result.putString(AccountManager.KEY_ACCOUNT_NAME, username.getText().toString().trim());
					result.putString(AccountManager.KEY_ACCOUNT_TYPE, account_type);
					setAccountAuthenticatorResult(result);
					Toast.makeText(context, "Add OK", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

}
