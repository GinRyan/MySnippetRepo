package com.alarmclocksnoozers.runnershigh;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


public class Util {
	private static Util mInstance =  null;
	public static int mScreenHeight = 0;
	public static int mScreenWidth = 0;
	public static int mWidthHeightRatio = 0;
	private static Context mContext = null;
	private static OpenGLRenderer mRenderer = null;
	public static long roundStartTime = 0;
	
	public static synchronized Util getInstance() {
		if(mInstance == null)
			mInstance = new Util();
		
		return mInstance;
	}
	
	public static float getPercentOfScreenWidth(float percent) {
		float percentWidth=mScreenWidth/100*percent;
		return percentWidth;
	}
	public static float getPercentOfScreenHeight(float percent) {
		float percentHeight=mScreenHeight/100*percent;
		return percentHeight;
	} 	
	
	public int toScreenY(int y) {
		y *= -1;
		y += mScreenHeight;
		
		return y;
	}
	
	public void setAppContext(Context context)
	{
		mContext = context;
	}
	
	public static Context getAppContext()
	{
		assert(mContext != null);
		return mContext;
	}
	public void setAppRenderer(OpenGLRenderer renderer)
	{
		mRenderer = renderer;
	}
	
	public static OpenGLRenderer getAppRenderer()
	{
		assert(mRenderer != null);
		return mRenderer;
	}
	public static long getTimeSinceRoundStartMillis()
	{
		assert(roundStartTime != 0);
		return System.currentTimeMillis()-roundStartTime;
	}
	public static Bitmap loadBitmapFromAssets(String filename) {
		
		try {
			return BitmapFactory.decodeStream(mContext.getAssets().open(filename));
		} catch (IOException e) {
			Log.e(Settings.LOG_TAG, "unable to load asset: " + filename);
			e.printStackTrace();
		}
		
		final int size = 16;
		int []colors = new int[size*size];
		
		for (int i = 0; i < size; i++) {
			colors[i*size+i] = 0xffff0000;
			colors[i*size+i + (size-i*2-1) ] = 0xffff0000;
		}
		
		return Bitmap.createBitmap(colors, size, size, Bitmap.Config.RGB_565);
	}
}
