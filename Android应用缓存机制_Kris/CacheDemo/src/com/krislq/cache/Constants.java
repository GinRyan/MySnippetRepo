package com.krislq.cache;

import java.io.File;

import com.krislq.cache.util.Util;

/**
 * 
 * @author <a href="mailto:kris1987@qq.com">Kris.lee</a>
 * @website www.krislq.com
 * @date Nov 20, 2012
 * @version 1.0.0
 *
 */
public class Constants {
	public static final String  APP_NAME			= "Cache";
	
	public static final boolean DEBUG 				= true;
	public static final boolean PERSISTLOG			=true;
	

	public static  		String		EXTERNAL_DIR 		= Util.getExternalStoragePath()+File.separator+APP_NAME;
	public static  		String		CACHE_DIR 			= EXTERNAL_DIR+File.separator+"cache";
	public static  		String		LOGS_DIR 			= EXTERNAL_DIR+File.separator+"logs";
	public static  		String		FILES_DIR 			= EXTERNAL_DIR+File.separator+"files";
	public static  		String		DATA_DIR 			= EXTERNAL_DIR+File.separator+"data";
	
}
