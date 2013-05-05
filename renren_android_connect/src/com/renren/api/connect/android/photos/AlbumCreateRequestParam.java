/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.photos;

import android.os.Bundle;

import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.Util;
import com.renren.api.connect.android.common.RequestParam;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.exception.RenrenException;

/**
 * 创建相册的请求参数实体类
 * 
 * @author sunting ting.sun@renren-inc.com
 */
public class AlbumCreateRequestParam extends RequestParam {

	/**
	 * 调用创建相册API传入的method参数
	 */
	private static final String METHOD = "photos.createAlbum";
	/**
	 * 创建的相册的名称，必须参数
	 */
	private String name;
	/**
	 * 相册的拍照地点，可选参数
	 */
	private String location;
	/**
	 * 相册的描述，可选参数
	 */
	private String description;
	/**
	 * 相册的隐私设置，可选参数，默认为EVERYONE，不支持设置密码
	 */
	private AlbumPrivacyType visible = AlbumPrivacyType.EVERYONE;

	public AlbumCreateRequestParam() {

	}

	public AlbumCreateRequestParam(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AlbumPrivacyType getVisible() {
		return visible;
	}

	public void setVisible(AlbumPrivacyType visible) {
		this.visible = visible;
	}

	/**
	 * 将请求实体转换成Bundle参数
	 */
	@Override
	public Bundle getParams() throws RenrenException {
		// 判断相册名称，如果相册名称为null或者""的话，抛出参数为空异常
		if (this.name == null || "".equals(this.name.trim())) {
			Util.logger("创建相册异常:相册名称不能为空");
			Util.logger("exception in creating album: album name can't be null!");
			throw new RenrenException(RenrenError.ERROR_CODE_NULL_PARAMETER,
					"相册名称不能为空！", "相册名称不能为空！");
		}

		Bundle params = new Bundle();
		params.putString("method", METHOD);
		params.putString("name", this.name.trim());
		// 默认请求返回参数为json格式，不提供设置此值的接口给用户
		params.putString("format", Renren.RESPONSE_FORMAT_JSON);
		params.putString("visilbe", String.valueOf(this.visible.getOunces()));

		if (this.location != null) {
			params.putString("location", this.location);
		}
		if (this.description != null) {
			params.putString("description", this.description);
		}

		return params;
	}

}
