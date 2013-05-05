/**
 * $id$
 */
package com.renren.api.connect.android.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Read the logcat log
 * 
 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com)
 */
public final class LogHelper {

	private static final String LOG_TAG_REQUEST = "Renren-SDK-Request";
	
	private static final String LOG_TAG_RESPONSE = "Renren-SDK-Response";
	/**
	 * Get the log using the specified filter 
	 * 
	 * @param tag 
	 * @return
	 */
	public synchronized String getLog() { 
		StringBuffer logger = new StringBuffer();
		try {
			Process process = Runtime.getRuntime().exec("logcat -v time + "
					+ LOG_TAG_REQUEST + ":I " 
					+ LOG_TAG_RESPONSE + ":I *:S");
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()), 1024);
			Thread thread = new Thread(new LogFinalizer(process));
			thread.start();
			String line = bufferedReader.readLine();
			while (line != null && line.length() != 0) {
				logger.append(parseLog(line));
				line = bufferedReader.readLine();
			}
			if(bufferedReader != null) {
				bufferedReader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return logger.toString();
	}
	
	/**
	 * clear the log
	 */
	public synchronized void clear() {
		try {
			Runtime.getRuntime().exec("logcat -c + "
					+ LOG_TAG_REQUEST + ":I " 
					+ LOG_TAG_RESPONSE + ":I *:S");
		} catch (IOException e) {
		}
	}
	
	/**
	 * 解析log为显示格式
	 * @param log
	 * @return
	 */
	private static String parseLog(String logStr) {
		StringBuffer sb = new StringBuffer();
		String requestTag = "I/" + LOG_TAG_REQUEST;
		String responseTag = "I/" + LOG_TAG_RESPONSE;
		if(logStr.contains(requestTag)) { 
			//请求log 格式形如：08-23 03:05:43.112 I/Renren-SDK-Request(  775): 
			//method=users.getInfo&Bundle[{v=1.0, uids=3××××××, …… 
			StringBuffer log = new StringBuffer(logStr);
			int tagIndex = log.indexOf(requestTag);
			//添加时间
			sb.append(log.substring(0, tagIndex)).append("\r\n");
			int methodIndex = log.indexOf("method");
			int methodEnd = log.indexOf("&");
			//添加method项
			sb.append(log.substring(methodIndex, methodEnd)).append("\r\n").append("request:\r\n");
			String bundleStr = "Bundle[{";
			int paramIndex = log.indexOf(bundleStr, 0) + bundleStr.length();
			//获取参数序列（不包括"[{"以及"}]"）
			String paramStr = log.substring(paramIndex, log.length() - 2);
			//添加参数 key=value 对
			String[] params = paramStr.split(",");
			sb.append("{\r\n");
			if(params != null) {
				for(String str : params) {
					sb.append("\t").append(str.trim()).append(";\r\n");
				}
			}
			sb.append("}\r\n\r\n");
		} else if(logStr.contains(responseTag)){ 
			//响应log 格式形如：03:05:51.452 I/Renren-SDK-Response(  775): 
			// method=status.set&{"result":1}…… 
			StringBuffer log = new StringBuffer(logStr);
			int tagIndex = log.indexOf(responseTag);
			//添加时间
			sb.append(log.substring(0, tagIndex)).append("\r\n");
			int methodIndex = log.indexOf("method");
			int methodEnd = log.indexOf("&");
			//添加method项
			sb.append(log.substring(methodIndex, methodEnd)).append("\r\n").append("response:\r\n");
			int paramIndex = methodEnd + 1;
			//获取参数序列
			String paramStr = log.substring(paramIndex, log.length());
			//添加参数 key=value 对
			paramStr.replace(",", ",\r\n");
			sb.append(paramStr).append("\r\n\r\n");
		} else {
			return logStr;
		}
		return sb.toString();
	}
	
	/**
	 * Used to stop the log process after a specified time 
	 * 
	 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com)
	 */
	private class LogFinalizer implements Runnable{
		
		private Process process;
		
		public LogFinalizer(Process process) {
			this.process = process;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			process.destroy();
		}
		
	}
}
