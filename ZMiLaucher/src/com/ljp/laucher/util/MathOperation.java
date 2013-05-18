package com.ljp.laucher.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;

public class MathOperation {

	/** * 根据手机的分辨率从 dp 的单位 转成为 px(像素) */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/** * 根据手机的分辨率从 px(像素) 的单位 转成为 dp */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	public static boolean[] addToArray(boolean[] a,int addlength){
		if(a.length<=0) return new boolean[addlength];
		if(addlength<=0)return a;
		boolean[] ret= new boolean[a.length+addlength];
		for(int i=0;i<a.length;i++){
			ret[i] = a[i];
		}
		return ret;
	}
	public static Bitmap[] addToArray(Bitmap[] a,int addlength){
		if(a.length<=0) return new Bitmap[addlength];
		if(addlength<=0)return a;
		Bitmap[] ret= new Bitmap[a.length+addlength];
		for(int i=0;i<a.length;i++){
			ret[i] = a[i];
		}
		return ret;
	}
	public static File[] addToArray(File[] a,File[] b){
		if(a==null || a.length<=0) return b;
		if(b==null || b.length<=0)return a;
		
		File[] ret= new File[a.length+b.length];
		for(int i=0;i<a.length;i++){
			ret[i] = a[i];
		}
		for(int i=0;i<b.length;i++){
			ret[i+a.length] = b[i];
		}
		return ret;
	}
	public static URL Pic50to180(String str){
		str = str.replaceFirst("/50/", "/180/");
		try {
			return new URL(str);
		} catch (MalformedURLException ex) {
			return null;
		}
	}
	public static int count(String str) {
		int singelC = 0, doubleC = 0;
		String s = "[^\\x00-\\xff]";
		Pattern pattern = Pattern.compile(s);
		Matcher ma = pattern.matcher(str);

		while (ma.find()) {
			doubleC++;
		}
		singelC = str.length() - doubleC;
		if (singelC % 2 != 0) {
			doubleC += (singelC + 1) / 2;
		} else {
			doubleC += singelC / 2;
		}
		return doubleC;
	}

	/*
	public static ArrayList<ContentItem> JsonToArrayList(JSONObject jos) {
		ArrayList<ContentItem> items = new ArrayList<ContentItem>();
		if (jos == null)
			return null;
		if (!jos.has("Items"))
			return null;
		else {
			JSONArray jo = null;
			try {
				jo = jos.getJSONArray("Items");
				for (int i = 0; i < jo.length(); i++) {
						JSONObject json_items= jo.getJSONObject(i);
						int id = json_items.getInt("id");
						String from =json_items.getString("name");
						String from_icon = json_items.getString("icon");
						JSONArray json_item= json_items.getJSONArray("item");
						for(int j=0;j<json_item.length();j++){
							ContentItem item = new ContentItem();
							item.setId(id);
							item.setFrom_icon(from_icon);
							item.setIcon(json_item.getJSONObject(j).getString("icon"));
							item.setFrom(from);
							item.setText(json_item.getJSONObject(j).getString("text"));
							items.add(item);
						}
					}
				}
			catch (JSONException e) {
				System.out.println("异常了");
				e.printStackTrace();
			}
		}

		return items;
	}
	*/
	public static String getDateDifferFromNow(Date in) {
		Date now = new Date();
		int in_minutes = in.getMinutes();
		int in_hour = in.getHours();
		int now_day = now.getDate();
		int in_day = in.getDate();
		int now_month = now.getMonth();
		int in_month = in.getMonth();
		int now_year = now.getYear();
		int in_year = in.getYear();
		if (in_year < now_year)
			return "很久以前";
		if (in_month < now_month)
			return "很久以前";
		if (in_day < now_day) {
			if (in_day == now_day - 1)
				return "昨天 " + getMinutes(in_hour) + ":" + getMinutes(in_minutes);
			else
				return now_day + " 号" + getMinutes(in_hour) + ":" + getMinutes(in_minutes);
		} else {
			return "今天 " + getMinutes(in_hour) + ":" + getMinutes(in_minutes);
		}

	}
	public static String getMinutes(int m){
		if(m<10)
			return "0"+m;
		return m+"";
	}
	
	
}



