/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.friends;

import android.os.Bundle;

import com.renren.api.connect.android.common.RequestParam;
import com.renren.api.connect.android.exception.RenrenException;

/**
 * 
 * @author hecao (he.cao@renren-inc.com)
 * 
 * friends.get 接口 请求参数封装
 *
 */
public class FriendsGetRequestParam extends RequestParam {
	
	private static final String METHOD = "friends.get";

	/**
	 * 分页，默认为1
	 */
	private int page = 1;
	
	/**
	 * 返回每页个数，默认为500
	 */
	private int count = 500;
	
	/**
	 * 构造一个friends.get接口的默认请求
	 */
	public FriendsGetRequestParam () {
		
	}
	
	/**
	 * 构造一个friends.get接口的请求，传入page和count参数
	 */
	public FriendsGetRequestParam (int page, int count) {
		this.page = page;
		this.count = count;
	}

	/**
	 * 获取分页
	 * @return
	 */
	public int getPage() {
		return page;
	}

	/**
	 * 设置分页
	 * @param page
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * 获取每页个数
	 * @return
	 */
	public int getCount() {
		return count;
	}

	/**
	 * 设置每页个数
	 * @param count
	 */
	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public Bundle getParams() throws RenrenException {
		Bundle parameters = new Bundle ();
        parameters.putString("method", METHOD);
		parameters.putString("page", String.valueOf(page));
		parameters.putString("count", String.valueOf(count));
		return parameters;
	}

}
