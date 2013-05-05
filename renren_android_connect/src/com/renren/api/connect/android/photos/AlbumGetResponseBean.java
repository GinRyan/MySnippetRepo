/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.photos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.renren.api.connect.android.Util;
import com.renren.api.connect.android.common.ResponseBean;

/**
 * 封装List<AlbumBean>，让其继承AbstractBean，便于AbstractRequestListener处理
 * 
 * @author sunting ting.sun@renren-inc.com
 * 
 */
public class AlbumGetResponseBean extends ResponseBean implements Parcelable {

	private List<AlbumBean> albums = new ArrayList<AlbumBean>();

	public AlbumGetResponseBean() {
		super(null);
	}

	public AlbumGetResponseBean(String response) {
		this(response, "json");
	}

	/**
	 * 构造函数，通过请求返回的字符串构造返回对象
	 * 
	 * @param response
	 * @param format
	 */
	public AlbumGetResponseBean(String response, String format) {
		super(response);

		if (response == null) {
			return;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");

		if (format.toLowerCase().endsWith("json")) {
			try {
				JSONArray array = new JSONArray(response);
				int size = array.length();
				JSONObject object = null;
				for (int i = 0; i < size; i++) {
					object = array.optJSONObject(i);
					if (object != null) {
						AlbumBean album = new AlbumBean();
						album.setCommentCount(object.optInt("comment_count"));
						album.setUid(object.optLong("uid"));
						album.setVisible(AlbumPrivacyType.parse(object
								.optInt("visible")));
						album.setUpdateTime(sdf.parse(object
								.optString("update_time")));
						album.setCreateTime(sdf.parse(object
								.optString("create_time")));
						album.setLocation(object.optString("location"));
						album.setDescription(object.optString("description"));
						album.setName(object.optString("name"));

						album.setAid(object.optLong("aid"));
						album.setType(AlbumType.parse(object.optInt("type")));
						album.setUrl(object.optString("url"));
						album.setSize(object.optInt("size"));
						
						albums.add(album);
					}
				}
			} catch (JSONException e) {
				Util.logger("exception in parsing json data: " + e.getMessage());
			} catch (ParseException e) {
				Util.logger("exception in parsing json data: " + e.getMessage());
			}
		}

	}

	public List<AlbumBean> getAlbums() {
		return albums;
	}

	public void setAlbums(List<AlbumBean> albums) {
		if (albums != null) {
			this.albums = albums;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		for (AlbumBean album : albums) {
			sb.append("\n").append(album.toString()).append("\r\n");
		}

		return sb.toString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(albums);

	}

	public static final Parcelable.Creator<AlbumGetResponseBean> CREATOR = new Parcelable.Creator<AlbumGetResponseBean>() {
		public AlbumGetResponseBean createFromParcel(Parcel in) {
			return new AlbumGetResponseBean(in);
		}

		public AlbumGetResponseBean[] newArray(int size) {
			return new AlbumGetResponseBean[size];
		}
	};

	/**
	 * 序列化构造函数
	 * 
	 * @param in
	 */
	public AlbumGetResponseBean(Parcel in) {
		super(null);

		in.readTypedList(albums, AlbumBean.CREATOR);
	}

}
