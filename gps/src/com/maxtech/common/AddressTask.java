package com.maxtech.common;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONObject;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;

import com.maxtech.common.gps.IAddressTask;

public class AddressTask extends IAddressTask {

	public AddressTask(Activity context, int postType) {
		super(context, postType);
	}

	@Override
	public HttpResponse execute(JSONObject params) throws Exception {
		HttpClient httpClient = new DefaultHttpClient();

		HttpConnectionParams.setConnectionTimeout(httpClient.getParams(),
				20 * 1000);
		HttpConnectionParams.setSoTimeout(httpClient.getParams(), 20 * 1000);

		HttpPost post = new HttpPost("http://74.125.71.147/loc/json");
		// 设置代理
		if (postType == DO_APN) {
			Uri uri = Uri.parse("content://telephony/carriers/preferapn"); // 获取当前正在使用的APN接入点
			Cursor mCursor = context.getContentResolver().query(uri, null,
					null, null, null);
			if (mCursor != null) {
//				mCursor.moveToNext(); // 游标移至第一条记录，当然也只有一条
				if(mCursor.moveToFirst()) {
					String proxyStr = mCursor.getString(mCursor
							.getColumnIndex("proxy"));
					if (proxyStr != null && proxyStr.trim().length() > 0) {
						HttpHost proxy = new HttpHost(proxyStr, 80);
						httpClient.getParams().setParameter(
								ConnRouteParams.DEFAULT_PROXY, proxy);
					}
				}
			}
		}
		
		StringEntity se = new StringEntity(params.toString());
		post.setEntity(se);
		HttpResponse response = httpClient.execute(post);
		return response;
	}

}
