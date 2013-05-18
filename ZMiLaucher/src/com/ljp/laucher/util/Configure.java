package com.ljp.laucher.util;

import weibo4android.Weibo;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;

import com.ljp.laucher.R;

public class Configure{
	public static boolean isDatabaseOprating=false;
	
	//==============old==================
	public static boolean isMove=false;
	public static boolean isChangingPage=false;
	public static boolean isDelDark = false;
	public static int screenHeight=0;
	public static int screenWidth=0;
	public static float screenDensity=0;
	
	public static int curentPage=0;public static int countPages=0;public static int removeItem=0;

	public static Integer[] images = { R.drawable.default_homebg, R.drawable.bg_cb,
			R.drawable.bg_fm, R.drawable.bg_gt, R.drawable.bg_tr };

	public int[] ret(int[] intArray) {
		int size = intArray.length;
		for (int i =size - 1; i >= 0; i--)
			for (int j = 0; j < i; j++)
				if (intArray[j] > intArray[j + 1]) {
					int t = intArray[j];
					intArray[j] = intArray[j + 1];
					intArray[j + 1] = t;
				}
		return intArray;
	}
	
	
		public static String httpurl=null;
		public static Bitmap NO_IMAGE=null;
		public static Bitmap[] DetailWeiboImages=null;

		public static Weibo userWeibo=null,extendWeibo = null,feedbackWeibo=null;
		public static Weibo hotWeibo=null, entertainmentWeibo=null, creativeWeibo=null, funnyWeibo=null, hotbpicWeibo=null, constellation=null, gameWeibo=null;
		
		public static String FB_ID="2629706930",FB_USER = "夜之娱乐",FB_USER_KEY = "9ef6744ac2c1e5c8b02409bd4042690b",FB_USER_SECRET = "31054b8231155c3fb66cc30f1af43b40";
		public static String S_ID="2526216395",S_USER = "夜族",S_USER_KEY = "f52f0dd985653193b8a62e2a90b1686d",S_USER_SECRET = "b6f0ec8f03b0bcd97e1f88d3b88cf67d";

		public static String N_USER_NAME=null;
		public static long N_USER_ID=0;
		public static String N_USER_KEY = null;
		public static String N_USER_SECRET=null;

		public static void inits(Activity context) {
			System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
			System.setProperty("weibo4j.oauth.consumerSecret",
					Weibo.CONSUMER_SECRET);
			if(Configure.NO_IMAGE==null)
				Configure.NO_IMAGE = ImageOperation.readNoImageBitmap(context,R.drawable.icon);

			if(screenDensity==0||screenWidth==0||screenHeight==0){
				DisplayMetrics dm = new DisplayMetrics();
				context.getWindowManager().getDefaultDisplay().getMetrics(dm);
				Configure.screenDensity = dm.density;
				Configure.screenHeight = dm.heightPixels;
				Configure.screenWidth = dm.widthPixels;
			}
			if(N_USER_NAME==null||N_USER_KEY == null||N_USER_SECRET==null||N_USER_ID==0){
				SharedPreferences refreshtime;
				refreshtime = context.getSharedPreferences("sp_users", 0);
				Configure.N_USER_ID = refreshtime.getLong("UserId", -1);
				Configure.N_USER_NAME = refreshtime.getString("UserName", null);
				Configure.N_USER_KEY = refreshtime.getString("Token", null);
				Configure.N_USER_SECRET = refreshtime.getString("TokenSecret", null);
			}	
		//	System.out.println(Configure.N_USER_ID+"--"+Configure.N_USER_KEY+"--"+Configure.N_USER_SECRET+"--"+Configure.N_USER_NAME);
			
			curentPage=0;countPages=0;
		}
		
		public static int getScreenHeight(Activity context){
			if(screenWidth==0||screenHeight==0){
				DisplayMetrics dm = new DisplayMetrics();
				context.getWindowManager().getDefaultDisplay().getMetrics(dm);
				Configure.screenDensity = dm.density;
				Configure.screenHeight = dm.heightPixels;
				Configure.screenWidth = dm.widthPixels;
			}
			return screenHeight;
		}
		public static int getScreenWidth(Activity context){
			if(screenWidth==0||screenHeight==0){
				DisplayMetrics dm = new DisplayMetrics();
				context.getWindowManager().getDefaultDisplay().getMetrics(dm);
				Configure.screenDensity = dm.density;
				Configure.screenHeight = dm.heightPixels;
				Configure.screenWidth = dm.widthPixels;
			}
			return screenWidth;
		}
		
		public static Weibo getUserWeibo(Context context) {
			if (Configure.userWeibo == null) {
				if(N_USER_NAME==null||N_USER_KEY == null||N_USER_SECRET==null||N_USER_ID==0){
					SharedPreferences refreshtime;
					refreshtime = context.getSharedPreferences("sp_users", 0);
					Configure.N_USER_ID = refreshtime.getLong("UserId", -1);
					Configure.N_USER_NAME = refreshtime.getString("UserName", null);
					Configure.N_USER_KEY = refreshtime.getString("Token", null);
					Configure.N_USER_SECRET = refreshtime.getString("TokenSecret", null);
					if(Configure.N_USER_ID==-1)
						return null;
				}
				Configure.userWeibo = OAuthConstantBean.getUserWeibo();
				Configure.userWeibo.setToken(Configure.N_USER_KEY,
						Configure.N_USER_SECRET);
			}
			return Configure.userWeibo;
		}
		public static Weibo getSysWeibo(int position) {

			if (feedbackWeibo == null) {
				feedbackWeibo = OAuthConstantBean.getUserWeibo();
				feedbackWeibo.setToken(FB_USER_KEY,
						FB_USER_SECRET);
			}
			return feedbackWeibo;
		}

		public static Weibo getExtendWeibos(){
			if (extendWeibo == null) {
				extendWeibo = OAuthConstantBean.getUserWeibo();
				extendWeibo.setToken(S_USER_KEY,
						S_USER_SECRET);
			}
			return extendWeibo;
		}
		
		public static void setUserWeibo(long id, String name, String key,
				String secret) {
			N_USER_ID = id;
			N_USER_NAME = name;
			N_USER_KEY = key;
			N_USER_SECRET = secret;
		}
		public static void userRegister(){
			userWeibo=null;
			N_USER_NAME=null;
			N_USER_ID=0;
			N_USER_KEY = null;
			N_USER_SECRET=null;
		}

		public static int _position;
}