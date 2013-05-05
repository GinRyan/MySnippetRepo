/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.photos;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 对相片实体Bean的封装<br>
 * 
 * 注：不继承ResponseBean
 * 
 * @author sunting ting.sun@renren-inc.com
 * 
 */
public class PhotoBean implements Parcelable {
	/**
	 * 字段常量
	 */
	private static final String KEY_PID = "pid";
	private static final String KEY_FILE_NAME = "fileName";
	private static final String KEY_AID = "aid";
	private static final String KEY_UID = "uid";
	private static final String KEY_CAPTION = "caption";
	private static final String KEY_CREATE_TIME = "createTime";
	private static final String KEY_VIEW_COUNT = "viewCount";
	private static final String KEY_COMMENT_COUNT = "commentCount";
	private static final String KEY_URL_TINY = "urlTiny";
	private static final String KEY_URL_HEAD = "urlHead";
	private static final String KEY_URL_LARGE = "urlLarge";
	private static final String KEY_URL_MAIN = "urlMain";
	private static final String KEY_CONTENT = "content";

	/**
	 * 照片id
	 */
	private long pid;
	/**
	 * 照片文件名 注：此名代表上传到服务器上的建议文件名，系统实际上可忽略，这个参数的主要作用是在上传的时候识别文件类型，所以名字可以随便取，
	 * 但类型一定要设置正确
	 */
	private String fileName;
	/**
	 * 照片所在相册id
	 */
	private long aid;
	/**
	 * 一张照片的所有者用户id
	 */
	private long uid;
	/**
	 * 照片的描述
	 */
	private String caption;
	/**
	 * 照片的创建时间
	 */
	private Date createTime;
	/**
	 * 照片的浏览数
	 */
	private int viewCount;
	/**
	 * 照片的评论数
	 */
	private int commentCount;
	/**
	 * 这几个都是同意图片不同的尺寸的图片的url地址，具体含义有点混乱，还在考证中
	 * 
	 * 100*150相册列表中的大小，url源地址
	 */
	private String urlTiny;
	/**
	 * 200*300 封面大小，url源地址
	 */
	private String urlHead;
	/**
	 * 600*900正常相片，url源地址
	 */
	private String urlLarge;
	/**
	 * 正常大小，url源地址
	 */
	private String urlMain;
	/**
	 * 图片的二进制数据
	 */
	private byte[] content;

	public PhotoBean() {
		
	}

	public PhotoBean(long pid) {
		this.pid = pid;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getAid() {
		return aid;
	}

	public void setAid(long aid) {
		this.aid = aid;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public String getUrlTiny() {
		return urlTiny;
	}

	public void setUrlTiny(String urlTiny) {
		this.urlTiny = urlTiny;
	}

	public String getUrlHead() {
		return urlHead;
	}

	public void setUrlHead(String urlHead) {
		this.urlHead = urlHead;
	}

	public String getUrlLarge() {
		return urlLarge;
	}

	public void setUrlLarge(String urlLarge) {
		this.urlLarge = urlLarge;
	}

	public String getUrlMain() {
		return urlMain;
	}

	public void setUrlMain(String urlMain) {
		this.urlMain = urlMain;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		sb.append(KEY_PID).append(" = ").append(pid).append("\r\n");
		sb.append(KEY_AID).append(" = ").append(aid).append("\r\n");
		sb.append(KEY_UID).append(" = ").append(uid).append("\r\n");
		sb.append(KEY_CAPTION).append(" = ").append(caption).append("\r\n");
		sb.append(KEY_CREATE_TIME).append(" = ").append(sdf.format(createTime))
				.append("\r\n");
		sb.append(KEY_VIEW_COUNT).append(" = ").append(viewCount)
				.append("\r\n");
		sb.append(KEY_COMMENT_COUNT).append(" = ").append(commentCount)
				.append("\r\n");
		sb.append(KEY_URL_TINY).append(" = ").append(urlTiny).append("\r\n");
		sb.append(KEY_URL_HEAD).append(" = ").append(urlHead).append("\r\n");
		sb.append(KEY_URL_LARGE).append(" = ").append(urlLarge).append("\r\n");
		sb.append(KEY_URL_MAIN).append(" = ").append(urlMain).append("\r\n");

		return sb.toString();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flag) {
		Bundle bundle = new Bundle();
		if (pid != 0) {
			bundle.putLong(KEY_PID, pid);
		}
		if (fileName != null) {
			bundle.putString(KEY_FILE_NAME, fileName);
		}
		if (aid != 0) {
			bundle.putLong(KEY_AID, aid);
		}
		if (uid != 0) {
			bundle.putLong(KEY_UID, uid);
		}
		if (caption != null) {
			bundle.putString(KEY_CAPTION, caption);
		}
		if (createTime != null) {
			bundle.putSerializable(KEY_CREATE_TIME, createTime);
		}
		bundle.putInt(KEY_VIEW_COUNT, viewCount);
		bundle.putInt(KEY_COMMENT_COUNT, commentCount);
		if (urlTiny != null) {
			bundle.putString(KEY_URL_TINY, urlTiny);
		}
		if (urlHead != null) {
			bundle.putString(KEY_URL_HEAD, urlHead);
		}
		if (urlLarge != null) {
			bundle.putString(KEY_URL_LARGE, urlLarge);
		}
		if (urlMain != null) {
			bundle.putString(KEY_URL_MAIN, urlMain);
		}

		// 一般不传递content，因为内容可能太大
		if (content != null) {
			bundle.putByteArray(KEY_CONTENT, content);
		}

		dest.writeBundle(bundle);
	}

	public static final Parcelable.Creator<PhotoBean> CREATOR = new Parcelable.Creator<PhotoBean>() {
		public PhotoBean createFromParcel(Parcel in) {
			return new PhotoBean(in);
		}

		public PhotoBean[] newArray(int size) {
			return new PhotoBean[size];
		}
	};

	/**
	 * 构造函数，根据序列化对象构造实例
	 * 
	 * @param in
	 */
	public PhotoBean(Parcel in) {
		Bundle bundle = in.readBundle();
		if (bundle.containsKey(KEY_PID)) {
			pid = bundle.getLong(KEY_PID);
		}
		if (bundle.containsKey(KEY_FILE_NAME)) {
			fileName = bundle.getString(KEY_FILE_NAME);
		}
		if (bundle.containsKey(KEY_AID)) {
			aid = bundle.getLong(KEY_AID);
		}
		if (bundle.containsKey(KEY_UID)) {
			uid = bundle.getLong(KEY_UID);
		}
		if (bundle.containsKey(KEY_CAPTION)) {
			caption = bundle.getString(KEY_CAPTION);
		}
		if (bundle.containsKey(KEY_CREATE_TIME)) {
			createTime = (Date) bundle.getSerializable(KEY_CREATE_TIME);
		}
		viewCount = bundle.getInt(KEY_VIEW_COUNT);
		commentCount = bundle.getInt(KEY_COMMENT_COUNT);

		if (bundle.containsKey(KEY_URL_TINY)) {
			urlTiny = bundle.getString(KEY_URL_TINY);
		}
		if (bundle.containsKey(KEY_URL_HEAD)) {
			urlHead = bundle.getString(KEY_URL_HEAD);
		}
		if (bundle.containsKey(KEY_URL_LARGE)) {
			urlLarge = bundle.getString(KEY_URL_LARGE);
		}
		if (bundle.containsKey(KEY_URL_MAIN)) {
			urlMain = bundle.getString(KEY_URL_MAIN);
		}
		if (bundle.containsKey(KEY_CONTENT)) {
			content = bundle.getByteArray(KEY_CONTENT);
		}
	}
}