/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.friends;

import java.util.concurrent.Executor;

import android.os.Bundle;

import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.Util;
import com.renren.api.connect.android.common.AbstractRequestListener;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.exception.RenrenException;

/**
 * 
 * @author hecao (he.cao@renren-inc.com)
 * friends相关接口
 * 
 */
public class FriendsHelper {

	private Renren renren;

	public FriendsHelper(Renren renren) {
		this.renren = renren;
	}

	/**
	 * 同步调用 friends.get接口
	 * 
	 * @param param
	 *            请求参数
	 * @return 返回{@link FriendsGetResponseBean}对象
	 * @throws RenrenException
	 * @throws Throwable
	 */
	public FriendsGetResponseBean getFriends(FriendsGetRequestParam param) throws RenrenException, Throwable {
		Bundle parameters = param.getParams();
		FriendsGetResponseBean responseBean = null;
		try {
			String response = renren.requestJSON(parameters);
			if (response != null) {
				Util.checkResponse(response, Renren.RESPONSE_FORMAT_JSON);
				responseBean = new FriendsGetResponseBean(response);
				return responseBean;
			} else {
				Util.logger("null response");
				throw new RenrenException(RenrenError.ERROR_CODE_UNKNOWN_ERROR, "null response", "null response");
			}
		} catch (RuntimeException re) {
			Util.logger("runtime exception" + re.getMessage());
			throw new Throwable(re);
		}
	}
	
	/**
	 * 异步调用friends.get接口
	 * 
	 * @param pool
	 *            线程池
	 * @param param
	 *            请求参数
	 * @param listener
	 *            请求回调
	 */
	public void asyncGetFriends(Executor pool, final FriendsGetRequestParam param, final AbstractRequestListener<FriendsGetResponseBean> listener) {
		
		pool.execute(new Runnable() {
			
			@Override
			public void run() {
			
				try {
					FriendsGetResponseBean bean = getFriends(param);
					if (listener != null) {
						listener.onComplete(bean);
					}
				} catch (RenrenException e) {
					Util.logger("renren exception " + e.getMessage());
					if (listener != null) {
						listener.onRenrenError(new RenrenError(e.getErrorCode(), e.getMessage(), e.getMessage()));
					}
				} catch (Throwable e) {
					Util.logger("on fault " + e.getMessage());
					if (listener != null) {
						listener.onFault(e);
					}
				}
				
			}
		});
		
	}
	
	/**
	 * 同步调用friends.getFriends接口
	 * 
	 * @param param
	 *            请求参数
	 * @return 返回{@link FriendsGetFriendsResponseBean}对象
	 * @throws RenrenException
	 * @throws Throwable
	 */
	public FriendsGetFriendsResponseBean getFriends (FriendsGetFriendsRequestParam param) throws RenrenException, Throwable {
		Bundle parameters = param.getParams();
		FriendsGetFriendsResponseBean responseBean = null;
		try {
			String response = renren.requestJSON(parameters);
			if (response != null) {
				Util.checkResponse(response, Renren.RESPONSE_FORMAT_JSON);
				responseBean = new FriendsGetFriendsResponseBean(response);
				return responseBean;
			} else {
				Util.logger("null response");
				throw new RenrenException(RenrenError.ERROR_CODE_UNKNOWN_ERROR, "null response", "null response");
			}
		} catch (RuntimeException re) {
			Util.logger("runtime exception" + re.getMessage());
			throw new Throwable(re);
		}	
	}
	
	/**
	 * 异步调用friends.getFriends接口
	 * 
	 * @param pool
	 *            线程池
	 * @param param
	 *            请求参数
	 * @param listener
	 *            请求回调
	 */
	public void asyncGetFriends(Executor pool, final FriendsGetFriendsRequestParam param, final AbstractRequestListener<FriendsGetFriendsResponseBean> listener) {
		
		pool.execute(new Runnable() {
			
			@Override
			public void run() {
			
				try {
					FriendsGetFriendsResponseBean bean = getFriends(param);
					if (listener != null) {
						listener.onComplete(bean);
					}
				} catch (RenrenException e) {
					Util.logger("renren exception " + e.getMessage());
					if (listener != null) {
						listener.onRenrenError(new RenrenError(e.getErrorCode(), e.getMessage(), e.getMessage()));
					}
				} catch (Throwable e) {
					Util.logger("on fault " + e.getMessage());
					if (listener != null) {
						listener.onFault(e);
					}
				}
				
			}
		});
		
	}
	

}
