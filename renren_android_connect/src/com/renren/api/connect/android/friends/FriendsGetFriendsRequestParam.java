/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.friends;

import android.os.Bundle;
import android.text.TextUtils;

import com.renren.api.connect.android.common.RequestParam;
import com.renren.api.connect.android.exception.RenrenException;

/**
 * 
 * @author hecao (he.cao@renren-inc.com)
 * 
 * Friends.getFriends接口请求参数封装
 *
 */
public class FriendsGetFriendsRequestParam extends RequestParam {
	
	private static final String METHOD = "friends.getFriends";
	
	/**
	 * 分页
	 */
	private int page = 1;
	
	/**
	 * 每页个数
	 */
	private int count = 500;
	
	/**
	 * 需要返回的字段
	 */
	private String[] fields = null;
	
	/**
	 * 带有人人logo的头像
	 */
	public static final String FIELDS_HEADURL_WITH_LOGO = "headurl_with_logo";
	
	/**
	 * 带有人人logo的小头像
	 */
	public static final String FIELDS_TINYURL_WITH_LOGO = "tinyurl_with_logo";
	
	/**
	 * 默认的请求参数
	 */
	public FriendsGetFriendsRequestParam () {
		
	}
	
	/**
	 * 自定义的请求参数
	 * @param page
	 * 			分页
	 * @param count
	 * 			每页个数
	 * @param fields
	 * 			需要返回的字段
	 */
	public FriendsGetFriendsRequestParam (int page, int count, String[] fields) {
		this.page = page;
		this.count = count;
		this.fields = fields;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String[] getFields() {
		return fields;
	}

	public void setFields(String[] fields) {
		this.fields = fields;
	}

	@Override
	public Bundle getParams() throws RenrenException {
		Bundle parameters = new Bundle();
        parameters.putString("method", METHOD);
		parameters.putString("page", String.valueOf(page));
		parameters.putString("count", String.valueOf(count));
        if (fields != null && fields.length > 0) {
            String fieldString = TextUtils.join(", ", fields);
            parameters.putString("fields", fieldString);
        }
		return parameters;
	}

}
