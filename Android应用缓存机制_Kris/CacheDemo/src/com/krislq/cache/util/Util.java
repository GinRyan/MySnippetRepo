package com.krislq.cache.util;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.krislq.cache.Constants;

/**
 * 
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @website www.krislq.com
 * @date Nov 20, 2012
 * @version 1.0.0
 *
 */
public class Util {
	/**
	 * get the external storage file
	 * 
	 * @return the file
	 */
	public static File getExternalStorageDir() {
		return Environment.getExternalStorageDirectory();
	}

	/**
	 * get the external storage file path
	 * 
	 * @return the file path
	 */
	public static String getExternalStoragePath() {
		return getExternalStorageDir().getAbsolutePath();
	}

	/**
	 * get the external storage state
	 * 
	 * @return
	 */
	public static String getExternalStorageState() {
		return Environment.getExternalStorageState();
	}

	/**
	 * check the usability of the external storage.
	 * 
	 * @return enable -> true, disable->false
	 */
	public static boolean isExternalStorageEnable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;

	}

	

	// public static final void hideSoftInputFromWindow(EditText editText,
	// Context context){
	// InputMethodManager imm = (InputMethodManager)
	// context.getSystemService(Context.INPUT_METHOD_SERVICE);
	// imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	// }

	/**
	 * judge the list is null or isempty
	 * 
	 * @param list
	 * @return
	 */
	public static boolean isEmpty(final List<? extends Object> list) {
		if (list == null || list.isEmpty()) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(final Set<? extends Object> sets) {
		if (sets == null || sets.isEmpty()) {
			return true;
		}
		return false;
	}
	public static boolean isEmpty(final Map<? extends Object,? extends Object> map) {
		if (map == null || map.isEmpty()) {
			return true;
		}
		return false;
	}

	/**
	 * Returns true if the string is null or 0-length.
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isEmpty(final String text) {
		return TextUtils.isEmpty(text);
	}

	/**
	 * return true ,if the string is numeric
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(final String str) {
		if (isEmpty(str)) {
			return false;
		}
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * get the width of the device screen
	 * 
	 * @param context
	 * @return
	 */
	public static int getSceenWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 * get the height of the device screen
	 * 
	 * @param context
	 * @return
	 */
	public static int getSceenHeight(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static float getSceenDensity(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}

	/**
	 * convert the dip value to px value
	 * 
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * convert the string to another string which is build by the char value
	 * 
	 * @param str
	 * @return
	 */
	public static String toCharString(String str) {
		if (TextUtils.isEmpty(str)) {
			str = "null";
		}
		String strBuf = "";
		for (int i = 0; i < str.length(); i++) {
			int a = str.charAt(i);
			strBuf += Integer.toHexString(a).toUpperCase();
		}
//		strBuf=String.valueOf(str.hashCode());
		return strBuf;
	}

	/**
	 * hide the input method
	 * 
	 * @param view
	 */
	public static void hideSoftInput(View view) {
		if (view == null)
			return;
		InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
		}
	}

	/**
	 * show the input method
	 * 
	 * @param view
	 */
	public static void showSoftInput(View view) {
		if (view == null)
			return;
		InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, 0);
	}
	public static void initExternalDir(boolean cleanFile)
	{
		if(Util.isExternalStorageEnable())
		{
			File external = new File(Constants.EXTERNAL_DIR);
			if(!external.exists())
			{
				external.mkdirs();
			}
			//check the cache whether exist
			File cache = new File(Constants.CACHE_DIR);
			if(!cache.exists())
			{
				cache.mkdirs();
			}
			else
			{
				if(cleanFile)
				{
					//if exist,so clear the old file
					cleanFile(cache, DateUtil.WEEK_MILLIS);
				}
			}
			//check the log dir
			File logs = new File(Constants.LOGS_DIR);
			if(!logs.exists())
			{
				logs.mkdirs();
			}
			else
			{
				if(cleanFile)
				{
					cleanFile(logs, DateUtil.HALF_MONTH_MILLIS);
				}
			}
			
			File files = new File(Constants.FILES_DIR);
			if(!files.exists())
			{
				files.mkdirs();
			}
			else
			{
				if(cleanFile)
				{
					cleanFile(logs, 0);
				}
			}
			
			File data = new File(Constants.DATA_DIR);
			if(!data.exists())
			{
				data.mkdirs();
			}
		}
	}
	public static int cleanFile(File dir, long maxInterval)
	{
		File[] files = dir.listFiles();
		if(files == null) return 0;
		int beforeNum = 0;
		long current = System.currentTimeMillis();
		for(File file:files)
		{
			long lastModifiedTime = file.lastModified();
			if((current-lastModifiedTime) > maxInterval)
			{
				//if the file is exist more than a week , so need to delete.
				file.delete();
				beforeNum ++;
			}
		}
		return beforeNum;
	}
}
