/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.photos;

import com.renren.api.connect.android.photos.AlbumPrivacyType;

/**
 * 相册隐私设置的类型，有5个int值: 99(所有人),4(密码保护) 3(同网络人),1(好友),-1(仅自己可见)
 * 
 * @author sunting ting.sun@renren-inc.com
 * 
 */
public enum AlbumPrivacyType {
	
	EVERYONE(99, "所有人"),
	OWNER(-1, "只有我自己"), 
	FRIENDS(1, "我的好友"), 
	NETWORKS(3, "好友及同城，同公司，同学校的人"), 
	PASSWORD(4, "使用密码访问");
	
	AlbumPrivacyType(int ounces, String names) {
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
	 * 将指定的值转换为AlbumPrivacyType类型，默认为EVERYONE
	 * @param value
	 * @return
	 */
	public static AlbumPrivacyType parse(int value) {
		switch (value) {
		case -1:
			return OWNER;
		case 1:
			return FRIENDS;
		case 3:
			return NETWORKS;
		case 4:
			return PASSWORD;
		case 99:
			return EVERYONE;
		default:
			return EVERYONE;
		}
	}
	
	@Override
	public String toString() {
		return this.names;
	}
	
}