/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.photos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.renren.api.connect.android.photos.AlbumBean;
import com.renren.api.connect.android.photos.AlbumPrivacyType;
import com.renren.api.connect.android.photos.AlbumType;
import com.renren.api.connect.android.photos.PhotoBean;

/**
 * 相册实体bean的封装
 * 
 * 注：此类不继承ResponseBean
 * 
 * @author sunting ting.sun@renren-inc.com
 * 
 */
public class AlbumBean implements Parcelable {
	/**
	 * 字段常量
	 */
	private static final String KEY_AID = "aid";
	private static final String KEY_URL = "url";
	private static final String KEY_UID = "uid";
	private static final String KEY_NAME = "name";
	private static final String KEY_CREATE_TIME = "createTime";
	private static final String KEY_UPDATE_TIME = "updateTime";
	private static final String KEY_DESCRIPTION = "description";
	private static final String KEY_LOCATION = "location";
	private static final String KEY_SIZE = "size";
	private static final String KEY_VISIBLE = "visible";
	private static final String KEY_COMMENT_COUNT = "commentCount";
	private static final String KEY_TYPE = "type";
	@SuppressWarnings("unused")
	private static final String KEY_PHOTOS = "photos";

	/**
	 * 相册id
	 */
	private long aid;
	/**
	 * 相册封面的图片地址
	 */
	private String url;
	/**
	 * 相册所有者的id
	 */
	private long uid;
	/**
	 * 相册的名字
	 */
	private String name;
	/**
	 * 相册的创建时间
	 */
	private Date createTime;
	/**
	 * 相册的更新时间
	 */
	private Date updateTime;
	/**
	 * 相册的描述
	 */
	private String description;
	/**
	 * 相册的拍摄地点
	 */
	private String location;
	/**
	 * 相册中照片的数量
	 */
	private int size;
	/**
	 * 相册的隐私设置 注：创建相册API不支持设置密码功能，即visible为4无效，默认为EVERYONE(99)
	 */
	private AlbumPrivacyType visible = AlbumPrivacyType.EVERYONE;
	/**
	 * 相册的评论数量
	 */
	private int commentCount;
	/**
	 * 相册的类型
	 */
	private AlbumType type;
	/**
	 * 相册中的所有照片
	 */
	private List<PhotoBean> photos = new ArrayList<PhotoBean>();

	public AlbumBean() {
		
	}

	public AlbumBean(long aid) {
		this.aid = aid;
	}

	public long getAid() {
		return aid;
	}

	public void setAid(long aid) {
		this.aid = aid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public AlbumPrivacyType getVisible() {
		return visible;
	}

	public void setVisible(AlbumPrivacyType visible) {
		this.visible = visible;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public AlbumType getType() {
		return type;
	}

	public void setType(AlbumType type) {
		this.type = type;
	}

	public List<PhotoBean> getPhotos() {
		return photos;
	}

	public void setPhotos(List<PhotoBean> photos) {
		this.photos = photos;
	}

	public void addPhoto(PhotoBean photo) {
		photos.add(photo);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		sb.append(KEY_AID).append(" = ").append(aid).append("\r\n");
		sb.append(KEY_URL).append(" = ").append(url).append("\r\n");
		sb.append(KEY_UID).append(" = ").append(uid).append("\r\n");
		sb.append(KEY_NAME).append(" = ").append(name).append("\r\n");
		sb.append(KEY_CREATE_TIME).append(" = ").append(sdf.format(createTime))
				.append("\r\n");
		sb.append(KEY_UPDATE_TIME).append(" = ").append(sdf.format(updateTime))
				.append("\r\n");
		sb.append(KEY_DESCRIPTION).append(" = ").append(description)
				.append("\r\n");
		sb.append(KEY_LOCATION).append(" = ").append(location).append("\r\n");
		sb.append(KEY_SIZE).append(" = ").append(size).append("\r\n");
		sb.append(KEY_VISIBLE).append(" = ").append(visible.getNames())
				.append("\r\n");
		sb.append(KEY_COMMENT_COUNT).append(" = ").append(commentCount)
				.append("\r\n");
		sb.append(KEY_TYPE).append(" = ").append(type.getNames())
				.append("\r\n");

		return sb.toString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Bundle bundle = new Bundle();
		if (aid != 0) {
			bundle.putLong(KEY_AID, aid);
		}
		if (url != null) {
			bundle.putString(KEY_URL, url);
		}
		if (uid != 0) {
			bundle.putLong(KEY_UID, uid);
		}
		if (name != null) {
			bundle.putString(KEY_NAME, name);
		}
		if (createTime != null) {
			bundle.putSerializable(KEY_CREATE_TIME, createTime);
		}
		if (updateTime != null) {
			bundle.putSerializable(KEY_UPDATE_TIME, updateTime);
		}
		if (description != null) {
			bundle.putString(KEY_DESCRIPTION, description);
		}
		if (location != null) {
			bundle.putString(KEY_LOCATION, location);
		}

		bundle.putInt(KEY_SIZE, size);
		bundle.putInt(KEY_COMMENT_COUNT, commentCount);

		bundle.putInt(KEY_VISIBLE, visible.getOunces());
		if (type != null) {
			bundle.putInt(KEY_TYPE, type.getOunces());
		}

		dest.writeBundle(bundle);

		dest.writeTypedList(photos);

	}

	public static final Parcelable.Creator<AlbumBean> CREATOR = new Parcelable.Creator<AlbumBean>() {
		public AlbumBean createFromParcel(Parcel in) {
			return new AlbumBean(in);
		}

		public AlbumBean[] newArray(int size) {
			return new AlbumBean[size];
		}
	};

	/**
	 * 序列化构造函数
	 * 
	 * @param in
	 */
	public AlbumBean(Parcel in) {
		Bundle bundle = in.readBundle();
		in.readTypedList(photos, PhotoBean.CREATOR);

		if (bundle.containsKey(KEY_AID)) {
			aid = bundle.getLong(KEY_AID);
		}
		if (bundle.containsKey(KEY_URL)) {
			url = bundle.getString(KEY_URL);
		}
		if (bundle.containsKey(KEY_UID)) {
			uid = bundle.getLong(KEY_UID);
		}
		if (bundle.containsKey(KEY_NAME)) {
			name = bundle.getString(KEY_NAME);
		}
		if (bundle.containsKey(KEY_CREATE_TIME)) {
			createTime = (Date) bundle.getSerializable(KEY_CREATE_TIME);
		}
		if (bundle.containsKey(KEY_UPDATE_TIME)) {
			updateTime = (Date) bundle.getSerializable(KEY_UPDATE_TIME);
		}
		if (bundle.containsKey(KEY_DESCRIPTION)) {
			description = bundle.getString(KEY_DESCRIPTION);
		}
		if (bundle.containsKey(KEY_LOCATION)) {
			location = bundle.getString(KEY_LOCATION);
		}

		size = bundle.getInt(KEY_SIZE);
		commentCount = bundle.getInt(KEY_COMMENT_COUNT);
		visible = AlbumPrivacyType.parse(bundle.getInt(KEY_VISIBLE));

		if (bundle.containsKey(KEY_TYPE)) {
			type = AlbumType.parse(bundle.getInt(KEY_TYPE));
		}
	}

}
