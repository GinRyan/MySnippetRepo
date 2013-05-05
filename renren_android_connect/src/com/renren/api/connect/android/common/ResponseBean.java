/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.common;

/**
 * 对人人网开放平台api请求结果进行封装的抽象类
 * 
 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com)
 *
 */
public abstract class ResponseBean {
	
	/**
	 * 利用响应字符串构造响应对象
	 * @param response
	 * 			服务器的响应字符串
	 */
	public ResponseBean(String response) {
		
	}
}
