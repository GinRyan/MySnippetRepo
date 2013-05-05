package com.krislq.cache.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.util.Log;

import com.krislq.cache.Constants;

/**
 * 
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @website www.krislq.com
 * @date Nov 20, 2012
 * @version 1.0.0
 *
 */
public class L {
	private static final String TAG				= "Cache";
	/**
	 * log file name
	 */
	public static final String  PERSIST_PATH	= Constants.LOGS_DIR + File.separator+ "log";
	
	public static void p(String log)
	{
		if(Constants.DEBUG)
		{

			System.out.println(log);
		}
		L.d(log);
	}
	
	public static void v(String text)
	{
		print(text, Log.VERBOSE);
	}
	
	public static void d(String text)
	{
		print(text, Log.DEBUG);
	}
	
	public static void i(String text)
	{
		print(text, Log.INFO);
	}
	
	public static void w(String text)
	{
		print(text, Log.WARN);
	}
	
	public static void e(String text)
	{
		print(text, Log.ERROR);
	}
	
	public static void e(String text,Throwable throwable)
	{
		print(text+"#message:"+throwable.getMessage(),Log.ERROR);
		StackTraceElement[] elements = throwable.getStackTrace();
		for(StackTraceElement e : elements)
		{
			print(e.toString(), Log.ERROR);
		}
	}
	
	private static synchronized void print(final String text, final int level)
	{
		if(Util.isEmpty(text))
		{
			return;
		}
		if(Constants.DEBUG)
		{

			switch (level) {
			case Log.VERBOSE:
					Log.v(TAG, text);
				break;
			case Log.DEBUG:
					Log.d(TAG, text);
				break;
			case Log.INFO:
					Log.i(TAG, text);
				break;
			case Log.WARN:
					Log.w(TAG, text);
				break;
			case Log.ERROR:
					Log.e(TAG, text);
				break;
			}
		}
		if(Constants.PERSISTLOG)
		{
			ThreadPoolUtil.execute(new Runnable() {
				
				@Override
				public void run() {
					writeLog(text, level);
				}
			});
			
		}
	}
	/**
	 * write the log into the file
	 * @param text
	 * @param level
	 */
	private static synchronized void writeLog(String text, int level )
	{
		StringBuilder sb = new StringBuilder();
		sb.append("["+DateUtil.toTime(System.currentTimeMillis(),DateUtil.DATE_FORMATE_HOUR_MINUTE_SECOND)+"]");
		switch (level) {
		case Log.VERBOSE:
			sb.append("[VERBOSE]\t");
			break;
		case Log.DEBUG:
			sb.append("[DEBUG]\t");
			break;
		case Log.INFO:
			sb.append("[INFO ]\t");
			break;
		case Log.WARN:
			sb.append("[WARN ]\t");
			break;
		case Log.ERROR:
			sb.append("[ERROR]\t");
			break;
		}
		
		RandomAccessFile raf = null;
		try {
			String fileName = PERSIST_PATH+"_"+DateUtil.toTime(System.currentTimeMillis(), DateUtil.DATE_DEFAULT_FORMATE);
			File logFile = new File(fileName);
			if(!logFile.exists())
			{
				Util.initExternalDir(false);
				logFile.createNewFile();
			}
			raf = new RandomAccessFile(fileName, "rw");
			raf.seek(raf.length());
			raf.writeBytes(sb.toString()+text+"\r\n");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			if(raf!=null)
			{
				try {
					raf.close();
				} catch (IOException e) {
				}
			}
		}
	}
	
}
