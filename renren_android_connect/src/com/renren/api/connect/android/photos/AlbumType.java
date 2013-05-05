/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.photos;

import com.renren.api.connect.android.photos.AlbumType;

/**
 * 相册的类型枚举	0.普通相册，1.头像相册，3.彩信相册，5.上传相册，7.大头贴相册，12.应用相册
 * 
 * @author sunting ting.sun@renren-inc.com
 *
 */
public enum AlbumType {
	
	COMMON(1, "普通相册"),		//普通相册
	MSS(3, "彩信相册"),			//彩信相册
	UPLOAD(5, "上传相册"),		//上传相册
	BIGHEAD(7, "大头贴相册"),		//大头贴相册
	APPLICATION(12, "应用相册");	//应用相册
	
	AlbumType(int ounces, String names) {
		this.ounces = ounces;
		this.names = names;
	}
	
	private final int ounces;
	private String names;

	public int getOunces() {
		return ounces;
	}
	
	public String getNames() {
		return names;
	}
	
	/**
	 * 将指定的值转换为AlbumType类型，默认为上传相册
	 * @param value
	 * @return
	 */
	public static AlbumType parse(int value) {
		switch (value) {
		case 1:
			return COMMON;
		case 3:
			return MSS;
		case 5:
			return UPLOAD;
		case 7:
			return BIGHEAD;
		case 12:
			return APPLICATION;
		default:
			return UPLOAD;
		}
	}

	@Override
	public String toString() {
		return names;
	}
}
