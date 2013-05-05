package com.ginmiss.getipfromip138;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView ip_tv;
	String ip;
	String url = "http://iframe.ip138.com/city.asp";
	String tmp = "http://www.baidu.com";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ip_tv = (TextView) findViewById(R.id.ip);
		try {
			parseIp(url, ip_tv);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void parseIp(String getIpUrl, final TextView tv)
			throws UnsupportedEncodingException {
		tv.setText(getHTML(getIpUrl));
	}

	public static String UTF8toGBK(String str)
			throws UnsupportedEncodingException {
		str = new String(str.getBytes("utf-8"), "gb2312");
		return str;
	}

	public static String GBKtoUTF8(String str)
			throws UnsupportedEncodingException {
		str = new String(str.getBytes("gb2312"), "utf-8");
		return str;
	}

	public static String ISOtoGB(String str)
			throws UnsupportedEncodingException {
		str = new String(str.getBytes("ISO-8859-1"), "gb2312");
		return str;
	}

	public static String ISOtoUTF8(String str)
			throws UnsupportedEncodingException {
		str = new String(str.getBytes("ISO-8859-1"), "utf-8");
		return str;
	}

	private String getHTML(String getIpUrl) throws UnsupportedEncodingException {
		getIpUrl = "http://iframe.ip138.com/city.asp";
		StringBuffer document = new StringBuffer();

		try {
			URL url = new URL(getIpUrl);
			URLConnection conn = url.openConnection();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null)
				document.append(line + " ");
			reader.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String str = new String(document.toString().getBytes("utf-8"), "gb2312");
		int start = str.indexOf("[");
		int end = str.indexOf("]");
		str = str.substring(start+1, end-1);
		System.out.println(str);
		return str;
	}


}
