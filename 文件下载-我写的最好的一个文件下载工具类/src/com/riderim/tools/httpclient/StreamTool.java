package com.riderim.tools.httpclient;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
/***
 * 读取输入流
 * @author 刘晓飞
 * <a href="http://blog.sina.com.cn/mynameisliuxiaofei">我的博客</a>
 */
public class StreamTool
{
	public static byte[] readInputStream(InputStream inStream) throws Exception{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while( (len=inStream.read(buffer)) != -1 ){
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}
}

