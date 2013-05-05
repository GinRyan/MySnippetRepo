/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.common;

import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.exception.RenrenException;

import android.os.Bundle;
import android.text.TextUtils;

/**
 * 
 * 开放平台各Api接口请求参数的抽象类
 * 
 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com)
 *
 */
public abstract class RequestParam {
	
	public abstract Bundle getParams() throws RenrenException;
	
	public void checkNullParams (String... params) throws RenrenException {
		
		for (String param : params) {
			if (TextUtils.isEmpty(param)) {
				String errorMsg = "required parameter MUST NOT be null";
				throw new RenrenException(
						RenrenError.ERROR_CODE_NULL_PARAMETER, errorMsg,
						errorMsg);
			}
		}
		
	}
}
