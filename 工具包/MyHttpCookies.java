package com.cnki.client.utils;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.message.BasicHeader;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiManager;

/**
 * http请求的缓存和一些公用的参数
 * 
 *
 */
public class MyHttpCookies {
	/** 每页数据显示最大数 */
	private static int pageSize = 10;
	/** 当前会话后的cookie信息 */
	private static CookieStore uCookie = null;
	/** 公用的HTTP提示头信息 */
	private static Header[] httpHeader;
	/** HTTP连接的网络节点 */
	private static String httpProxyStr;
	/**http请求的公用url部分**/
	public static String baseurl = "http://192.168.50.56:5056/River";
	/**上下文对象**/
	Context context;
	
	public MyHttpCookies(Context context){
		this.context = context;
		/** y设置请求头 **/
		/** y设置请求头 **/
		Header[] header = {
				new BasicHeader("PagingRows", String.valueOf(pageSize)) };
		httpHeader = header;
	}
	
	/**
	 * 增加自动选择网络，自适应cmwap、CMNET、wifi或3G
	 */
	@SuppressWarnings("static-access")
	public void initHTTPProxy() {
		WifiManager wifiManager = (WifiManager) (context.getSystemService(context.WIFI_SERVICE));
		if (!wifiManager.isWifiEnabled()) {
			Uri uri = Uri.parse("content://telephony/carriers/preferapn"); // 获取当前正在使用的APN接入点
			Cursor mCursor =context. getContentResolver().query(uri, null, null, null,
					null);
			if (mCursor != null) {
				mCursor.moveToNext(); // 游标移至第一条记录，当然也只有一条
				httpProxyStr = mCursor.getString(mCursor
						.getColumnIndex("proxy"));
			}
		} else {
			httpProxyStr = null;
		}
	}
	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public CookieStore getuCookie() {
		return uCookie;
	}

	public void setuCookie(CookieStore uCookie) {
		this.uCookie = uCookie;
	}


	public Header[] getHttpHeader() {
		return httpHeader;
	}

	public String getHttpProxyStr() {
		return httpProxyStr;
	}
}
