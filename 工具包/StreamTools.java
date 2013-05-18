package com.cnki.client.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import android.os.Handler;
import android.os.Message;

public class StreamTools {
	/**
	 * 从网络输入流获取音乐信息，并保存
	 * @param in
	 * @param out
	 * @param handler
	 * @throws Exception
	 */
	public static void readData(InputStream in,OutputStream out,Handler handler) throws Exception{
		if(in != null && out != null && handler != null){
			//发送消息开始下载音乐
			handler.sendEmptyMessage(Constant.FILE_LOAD_START);
			//从服务器端读取音乐数据
			BufferedInputStream bis = new BufferedInputStream(in);
			//BufferedOutputStream bos = new BufferedOutputStream(out);
			//将读取到的音乐数据发送到指定路径存储
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int len = -1;
			//初始下载时，下载进度为0
			int loaded = 0;
			byte[] bytes = new byte[1024];
			while((len = bis.read(bytes)) != -1){
				out.write(bytes, 0, len);
				//每下载1kb，进度就加1
				loaded++;
				//如果下载到100kb，就发送信息到通知栏，每100kb更新一次下载进度
				if(loaded%100 == 0){
					//发送的消息类型为正在下载中
					Message msg = Message.obtain(handler, Constant.FILE_LOAD_UPDATE);
					//将下载进度值loaded携带在消息中传递给通知栏
					msg.obj = loaded;
					msg.sendToTarget();			
				}
			}
			//关闭所有的IO流
			out.close();
			bis.close();
			bos.close();
			in.close();
			//发送消息。标识下载完成
			handler.sendEmptyMessage(Constant.FILE_LOAD_END);
		}		
	}
	/**
	 * 获取输入流将音乐信息保存到指定的路径
	 * @param in
	 * @param savepath
	 * @param handler
	 * @throws Exception
	 */
	public static void saveTo(InputStream in,String savepath,Handler handler) throws Exception{
		if(in != null && savepath != null){
			//将音乐信息保存到指定路径
			OutputStream out = new FileOutputStream(savepath);
			readData(in, out, handler);
		}
	}
}
