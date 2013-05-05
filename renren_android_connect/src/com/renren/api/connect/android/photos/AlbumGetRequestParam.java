/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.photos;

import android.os.Bundle;
import android.text.TextUtils;

import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.Util;
import com.renren.api.connect.android.common.RequestParam;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.exception.RenrenException;

/**
 * 获取相册API请求参数实体bean
 * 
 * @author sunting ting.sun@renren-inc.com
 */
public class AlbumGetRequestParam extends RequestParam {
	/**
	 * 调用获取相册API传入的method参数，必须参数
	 */
	private static final String METHOD = "photos.getAlbums";
	/**
	 * 获取的相册的所有者用户uid，必须参数
	 */
	private Long uid;
	/**
	 * 分页的页数，默认值为1
	 */
	private int page = 1;
	/**
	 * 分页后每页的个数，默认值为10
	 */
	private int count = 10;
	/**
	 * 多个相册的ID，以逗号分隔，最多支持10个数据
	 */
	private String aids;

	public AlbumGetRequestParam() {

	}

	public AlbumGetRequestParam(Long uid) {
		this.uid = uid;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
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

	public String getAids() {
		return aids;
	}

	public void setAids(String aids) {
		this.aids = aids;
	}

	@Override
	public Bundle getParams() throws RenrenException {
		Bundle params = new Bundle();
		params.putString("method", METHOD);
		// 默认请求返回参数为json格式，不提供设置此值的接口给用户
		params.putString("format", Renren.RESPONSE_FORMAT_JSON);
		params.putString("uid", String.valueOf(this.uid));

		// aids的长度如果大于10，系统会抛出错误信息“aids的长度必须小于10”
		if (this.aids != null && !"".equals(this.aids)) {
			// 判断是否合法的相册ids
			String[] aid = this.aids.split(",");
			int length = aid.length;
			
			if (length >= 10) {
				Util.logger("exception in getting albums: the length of aids should less than 10!");
				throw new RenrenException(
						RenrenError.ERROR_CODE_PARAMETER_EXTENDS_LIMIT,
						"同时获取的相册数不能大于10个", "同时获取的相册数不能大于10个");
			}
			
			for (int i = 0; i < length; i++) {
				if (!TextUtils.isDigitsOnly(aid[i].trim())) {
					continue;
				}
				
				Util.logger("exception in getting albums: invalid aids!");
				throw new RenrenException(
						RenrenError.ERROR_CODE_ILLEGAL_PARAMETER,
						"不合法的相册aid", "不合法的相册aid");
			}
			
			
			
			params.putString("aids", this.aids);
		}

		params.putString("page", String.valueOf(this.page));
		params.putString("count", String.valueOf(this.count));

		return params;
	}
	
}
