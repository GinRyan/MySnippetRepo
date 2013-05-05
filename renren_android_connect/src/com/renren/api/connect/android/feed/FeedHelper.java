/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.feed;

import java.util.concurrent.Executor;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;

import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.Util;
import com.renren.api.connect.android.common.AbstractRequestListener;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.exception.RenrenException;
import com.renren.api.connect.android.view.RenrenWidgetListener;

/**
 * 封装根据人人开放平台新鲜事的相关操作
 * 
 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com) 
 *
 */
//TODO: 修复某些手机上调用pageOnstart和pageOnBegin先后顺序不一致的问题
public class FeedHelper {

	/**
	 * 服务器的响应字段
	 */
	private static final String RESPONSE = "post_id";
	
	/**
	 * 用来发送请求的{@link Renren}对象
	 */
	private Renren renren;
	
	/**
	 * 用来在bundle中传递的是否进行验证的boolean值的key
	 */
	public static final String AUTH_OPTION = "auth_option";

	public FeedHelper(Renren renren) {
		this.renren = renren;
	}
	
	/**
	 * 同步调用feed.publishFeed发布新鲜事
	 * 
	 * @param feed 
	 * 			要发送的新鲜事
	 * @return 
	 * 			若发送成功，返回已发送的新鲜事，否则返回null
	 * @throws RenrenException 
	 * @throws Throwable
	 */
	public FeedPublishResponseBean publish(FeedPublishRequestParam feed) throws RenrenException, Throwable {

		if (!renren.isSessionKeyValid()) {
			String errorMsg = "Session key is not valid.";
			throw new RenrenException(RenrenError.ERROR_CODE_TOKEN_ERROR,
					errorMsg, errorMsg);
		}
		//参数不能为空
		if(feed == null) {
			String errorMsg = "The parameter is null.";
			throw new RenrenException(
					RenrenError.ERROR_CODE_NULL_PARAMETER, errorMsg, errorMsg);
		}
				
		// 发送发布状态请求
		String response;
		try {
			Bundle params = feed.getParams();
			response = renren.requestJSON(params);
		} catch (RenrenException rre) {
			Util.logger(rre.getMessage());
			throw rre;
		} catch (RuntimeException re) {
			Util.logger(re.getMessage());
			throw new Throwable(re);
		}

		RenrenError rrError = Util.parseRenrenError(response, Renren.RESPONSE_FORMAT_JSON);
		if (rrError != null) {
			Util.logger(rrError.getMessage());
			throw new RenrenException(rrError);
		} else {
			try {
				JSONObject json = new JSONObject(response);
				int postId = json.optInt(RESPONSE);
				if (postId > 0) {
					return new FeedPublishResponseBean(response);
				}
				String errorMsg = "Cannot parse the response.";
				throw new RenrenException(
						RenrenError.ERROR_CODE_UNABLE_PARSE_RESPONSE,
						errorMsg, errorMsg);
			} catch (JSONException je) {
				Util.logger(je.getMessage());
				throw new RenrenException(
						RenrenError.ERROR_CODE_UNABLE_PARSE_RESPONSE, je.getMessage(),
						je.getMessage());
			}
		}		
	}
	
	/**
	 * 异步调用feed.publishFeed发布新鲜事
	 * 
	 * @param pool
	 * 			执行发送新鲜事操作的线程池
	 * @param feed
	 *      	要发布的新鲜事
	 * @param listener
	 *      	用以监听发布新鲜事结果的监听器对象
	 * @param truncOption
	 *          若超出了长度，是否自动截短至限制的长度
	 */
	public void asyncPublish(Executor pool, 
			final FeedPublishRequestParam feed, 
			final AbstractRequestListener<FeedPublishResponseBean> listener, 
			final boolean truncOption) {
		pool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					FeedPublishResponseBean bean = publish(feed);
					if (listener != null) {
						listener.onComplete(bean);
					}
				} catch (RenrenException rre) { // 参数、服务器等错误或异常
					Util.logger(rre.getMessage());
					if (listener != null) {
						listener.onRenrenError(new RenrenError(rre
								.getErrorCode(), rre.getMessage(), rre
								.getOrgResponse()));
					}
				} catch (Throwable t) { // 一般为网络异常
					Util.logger(t.getMessage());
					if (listener != null) {
						listener.onFault(t);
					}
				}
			}
		});
	}
	
	/**
	 * 使用Widget窗口发新鲜事
	 * 
	 * @param appID 
	 * 			应用的app ID
	 * @param activity 
	 * 			显示此widget的activity
	 * @param listener 
	 * 			监听发送新鲜事结果的listener
	 * @throws RenrenException 
	 */
	public void startFeedPublishWidget(String appID, Activity activity, FeedPublishRequestParam feed, 
			final AbstractRequestListener<FeedPublishResponseBean> listener) throws RenrenException {
		/*
		 * 使用widgetDialog发布状态跟权限授予没有关系，此处不必要做权限限制。
		 * fixed:shaopeng.han
		 */
//		if (!renren.isSessionKeyValid()) {
//			String errorMsg = "Session key is not valid.";
//			throw new RenrenException(RenrenError.ERROR_CODE_TOKEN_ERROR,
//					errorMsg, errorMsg);
//		}
		
		Bundle params = feed.getWidgetParams();
		params.putString("app_id", appID);
		
		renren.widgetDialog(activity, params, new RenrenWidgetListener() {

			@Override
			public void onError(Bundle retParams) {
				if(listener != null) {
					listener.onRenrenError(
							new RenrenError(retParams.getString("error") 
									+ retParams.getString("error_description")));
				}
			}

			@Override
			public void onComplete(Bundle retParams) {
				if(listener != null) {
					if(retParams.containsKey(FeedPublishResponseBean.RESPONSE)) {
						listener.onComplete(new FeedPublishResponseBean(
								retParams.getString(FeedPublishResponseBean.RESPONSE)));
					} else {
						listener.onComplete(null);
					}
				}
			}

			@Override
			public void onCancel(Bundle retParams) {
				if(listener != null) {
					String errorMsg = "operation cancelled.";
					RenrenError error = 
							new RenrenError(RenrenError.ERROR_CODE_OPERATION_CANCELLED, 
									errorMsg, errorMsg);
					listener.onRenrenError(error);
				}
			}
		}, "http://widget.renren.com/dialog/feed");
	}
}
