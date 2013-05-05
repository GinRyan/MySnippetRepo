/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.photos;

import java.io.File;

import android.os.Bundle;

import com.renren.api.connect.android.common.RequestParam;
import com.renren.api.connect.android.exception.RenrenException;

/**
 * 上传照片API的请求参数bean
 * 
 * @author sunting
 */
public class PhotoUploadRequestParam extends RequestParam {
	
	/**
	 * 照片描述的最大长度
	 */
	public static final int CAPTION_MAX_LENGTH = 140;
	
	/**
	 * 调用上传照片API传入的method参数，必须参数
	 */
	@SuppressWarnings("unused")
	private static final String METHOD = "photos.upload";
	/**
	 * 文件的数据，必须参数 目前支持的文件类型有：image/bmp, image/png, image/gif, image/jpeg,
	 * image/jpg
	 */
	private File file;
	/**
	 * 照片的描述信息，可选参数
	 */
	private String caption;
	/**
	 * 相册的ID，可选参数 如果指定此参数，将会传到指定相册，默认传到手机相册。此参数可以通过photos.getAlbums接口获取
	 */
	private long aid;
	/**
	 * 上传照片所在地点的ID，可选参数 place_id为一个地点的Id，可以通过places.create来创建地点，生成place_id
	 */
	private String place_id;
	
	public PhotoUploadRequestParam() {
		
	}

	public PhotoUploadRequestParam(File file) {
		this.file = file;
	}
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public long getAid() {
		return aid;
	}

	public void setAid(long aid) {
		this.aid = aid;
	}

	public String getPlace_id() {
		return place_id;
	}

	public void setPlace_id(String place_id) {
		this.place_id = place_id;
	}

	@Override
	public Bundle getParams() throws RenrenException {
		// 上传照片不调用requestXML或requestJSON数据接口，故此方法不实现
		return null;
	}

}
