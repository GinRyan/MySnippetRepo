package com.ljp.laucher.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class HttpClients{
	public static JSONObject getJsonArray(String Url) {
		HttpClient client = new DefaultHttpClient();
		StringBuilder sb = new StringBuilder();
		String js = null;JSONObject son=null;
		HttpGet myget = new HttpGet(Url); 
		try {
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, 8000);
			HttpResponse response = client.execute(myget);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				sb.append(s);
			}
			js = sb.toString();
			son = new JSONObject(js);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("异常-》下载转化JSON");
			return null;
		}
		return son;
	}
	public static int getNewVerCode(String url) {
		HttpClient client = new DefaultHttpClient();
		StringBuilder sb = new StringBuilder();
		int vercode = 1;
		HttpGet myget = new HttpGet(url); 
		try {
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, 8000);
			HttpResponse response = client.execute(myget);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				sb.append(s);
			}
			vercode  = Integer.parseInt(sb.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("异常-》网络");
			return vercode;
		}
		return vercode;
	}
	public static boolean isConnect(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isWiFiActive(Context inContext) {
		Context context = inContext.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getTypeName().equals("WIFI")
							&& info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}

}