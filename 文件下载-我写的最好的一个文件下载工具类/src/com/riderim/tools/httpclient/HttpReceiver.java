package com.riderim.tools.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * HTTP请求辅助类
 * 
 * @author Liang
 *
 */
public class HttpReceiver {
	public static String httpSendAndReceive(String getRequest) {
		String json = "";
		try {
			
			HttpClient client = new DefaultHttpClient();
			HttpParams params = client.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 5000);
			HttpConnectionParams.setSoTimeout(params, 5000);
			HttpGet get = new HttpGet(getRequest);
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			entity.getContentLength();
			InputStream is = entity.getContent();
			byte[] data = StreamTool.readInputStream(is);
			json = new String(data, "GBK");

		} catch (ClientProtocolException e) {
			System.out.println("客户端协议问题:" + e.getMessage());
			e.printStackTrace();
		} catch (IllegalStateException e) {
			System.out.println("状态错误:" + e.getMessage());
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			System.out.println("JSON不支持的编码类型:" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO或者域名解析问题:" + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return json;
	}

	/**
	 * 通过外网网络辅助获取本地IP
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getLocalIpAddress() throws UnsupportedEncodingException {  

			String getIpUrl = "http://iframe.ip138.com/city.asp";
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
