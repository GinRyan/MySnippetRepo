package com.wifiadb.exec;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	Context context = this;
	EditText et_port;
	Button bt_set;
	Button bt_reset;
	TextView tv_ip;
	String ip = "";

	private void init() {
		et_port = (EditText) this.findViewById(R.id.port);
		bt_set = (Button) this.findViewById(R.id.settcp);
		bt_reset = (Button) this.findViewById(R.id.reset);
		tv_ip = (TextView) this.findViewById(R.id.ip);
		ip = ADB_Service.getLocalIpAddress();
	}

	private void setListener() {
		bt_reset.setOnClickListener(new bt_impl());
		bt_set.setOnClickListener(new bt_impl());
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		setListener();
	}

	class bt_impl implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.settcp) {
				int port = Integer.parseInt(et_port.getText().toString());
				if (port > 1024 & port < 65535) {
					try {
						ADB_Service.set(port);

						String iptip = "adb connect " + ip + ":" + port;
						tv_ip.setText(iptip);
						tv_ip.setTextSize(16);
						Toast.makeText(context, "已经切换到WiFi调试", 1).show();
					} catch (IOException e) {
						Toast.makeText(context, "您可能没有root操作", 1).show();
						e.printStackTrace();
					} catch (InterruptedException e) {
						Toast.makeText(context, "您可能没有root操作", 1).show();
						e.printStackTrace();
					}
				} else {
					Toast.makeText(context, "这是不合法的端口编号", 1).show();
				}
			} else if (v.getId() == R.id.reset) {
				try {
					ADB_Service.reset();
					tv_ip.setText("现在是USB调试模式");
					Toast.makeText(context, "已经恢复到USB调试", 1).show();
				} catch (IOException e) {
					Toast.makeText(context, "您可能没有root操作", 1).show();
					e.printStackTrace();
				} catch (InterruptedException e) {
					Toast.makeText(context, "您可能没有root操作", 1).show();
					e.printStackTrace();
				}
			}
		}
	}

}
