/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.photos;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.Util;
import com.renren.api.connect.android.common.ResponseBean;

/**
 * photos.createAlbum API请求的返回结果包装类
 * 
 * @author sunting ting.sun@renren-inc.com
 * 
 */
public class AlbumCreateResponseBean extends ResponseBean implements Parcelable {

	private static final String KEY_AID = "aid";
	/**
	 * 返回的相册aid
	 */
	private long aid;

	/**
	 * 构造函数，将请求返回的json串格式数据解析成对象
	 * 
	 * @param response
	 */
	public AlbumCreateResponseBean(String response) {
		this(response, Renren.RESPONSE_FORMAT_JSON);
	}
	
	/**
	 * 构造函数，将请求返回的json串格式数据解析成对象
	 * 
	 * @param response
	 * 			服务器返回的请求结果串
	 * @param format
	 * 			服务器返回结果的格式
	 */
	private AlbumCreateResponseBean(String response, String format) {
		super(response);
		
		if (response == null) {
			return;
		}
		
		// 暂时只提供json格式的数据解析
		if (format.toLowerCase().endsWith("json")) {
			try {
				JSONObject object = new JSONObject(response);
				if (object != null) {
					this.aid = object.optLong(KEY_AID);
				}
			} catch (JSONException e) {
				Util.logger("exception in parsing json data:" + e.getMessage());
			}
		}
	}

	public void setAid(long aid) {
		this.aid = aid;
	}

	public long getAid() {
		return this.aid;
	}

	@Override
	public String toString() {
		return "\"" + KEY_AID + "\": " + aid;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flag) {
		Bundle bundle = new Bundle();
		if (aid != 0) {
			bundle.putLong(KEY_AID, aid);
		}

		dest.writeBundle(bundle);
	}

	public static final Parcelable.Creator<AlbumCreateResponseBean> CREATOR = new Parcelable.Creator<AlbumCreateResponseBean>() {
		public AlbumCreateResponseBean createFromParcel(Parcel in) {
			return new AlbumCreateResponseBean(in);
		}

		public AlbumCreateResponseBean[] newArray(int size) {
			return new AlbumCreateResponseBean[size];
		}
	};

	/**
	 * 构造函数，根据序列化对象构造实例
	 * 
	 * @param in
	 */
	public AlbumCreateResponseBean(Parcel in) {
		super(null);

		Bundle bundle = in.readBundle();
		if (bundle.containsKey(KEY_AID)) {
			aid = bundle.getLong(KEY_AID);
		}
	}

}
