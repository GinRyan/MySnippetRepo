/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.friends;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.renren.api.connect.android.common.ResponseBean;

/**
 * 
 * @author hecao (he.cao@renren-inc.com)
 * 
 * Friends.getFriends接口返回封装
 *
 */
public class FriendsGetFriendsResponseBean extends ResponseBean {
	
	/**
	 * 好友列表
	 */
	private ArrayList<Friend> friendList;

	public FriendsGetFriendsResponseBean(String response) {
		super(response);
		if (response == null) {
			return;
		}
        
        try {
			JSONArray array = new JSONArray(response);
			if (array != null) {
				friendList = new ArrayList<FriendsGetFriendsResponseBean.Friend>();
				int size = array.length();
				JSONObject jsonObject = null;
				for (int i = 0 ; i < size ; i ++ ) {
					jsonObject = array.optJSONObject(i);
					if (jsonObject != null) {
						friendList.add(new Friend(jsonObject));
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public ArrayList<Friend> getFriendList() {
		return friendList;
	}


	public void setFriendList(ArrayList<Friend> friendList) {
		this.friendList = friendList;
	}
	
	@Override
	public String toString () {
		StringBuffer sb = new StringBuffer();
		if (friendList != null) {
			for (Friend f : friendList) {
				sb.append(f.toString()).append("\r\n");
			}
		}
		return sb.toString();
	}


	/**
	 * 
	 * @author hecao (he.cao@renren-inc.com)
	 *
	 * 单个好友的封装
	 *
	 */
	public class Friend extends ResponseBean {
		
		private static final String KEY_UID = "id";
		private static final String KEY_NAME = "name";
		private static final String KEY_HEADURL = "headurl";
		private static final String KEY_HEADURL_WITH_LOGO = "headurl_with_logo";
		private static final String KEY_TINYURL_WITH_LOGO = "tinyurl_with_logo";

		public Friend(String response) {
			super(response);
		}
		
		public Friend(JSONObject object) {
			super(null);
			if (object != null) {
				this.uid = object.optLong(KEY_UID);
				this.name = object.optString(KEY_NAME);
				this.headurl = object.optString(KEY_HEADURL);
				this.headurl_with_logo = object.optString(KEY_HEADURL_WITH_LOGO);
				this.tinyurl_with_logo = object.optString(KEY_TINYURL_WITH_LOGO);
			}
		}

		/**
		 * uid
		 */
		private long uid;
		
		/**
		 * 姓名
		 */
		private String name;
		
		/**
		 * 头像
		 */
		private String headurl;
		
		/**
		 * 带有人人logo的头像
		 */
		private String headurl_with_logo;
		
		/**
		 * 带有人人logo的小头像
		 */
		private String tinyurl_with_logo;
		
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

		public String getHeadurl() {
			return headurl;
		}

		public void setHeadurl(String headurl) {
			this.headurl = headurl;
		}

		public String getHeadurl_with_logo() {
			return headurl_with_logo;
		}

		public void setHeadurl_with_logo(String headurl_with_logo) {
			this.headurl_with_logo = headurl_with_logo;
		}

		public String getTinyurl_with_logo() {
			return tinyurl_with_logo;
		}

		public void setTinyurl_with_logo(String tinyurl_with_logo) {
			this.tinyurl_with_logo = tinyurl_with_logo;
		}
		
		@Override
		public String toString () {
			StringBuffer sb = new StringBuffer();
			sb.append(KEY_UID).append(" = ").append(uid).append("\r\n");
			sb.append(KEY_NAME).append(" = ").append(name).append("\r\n");
			sb.append(KEY_HEADURL).append(" = ").append(headurl).append("\r\n");
			sb.append(KEY_HEADURL_WITH_LOGO).append(" = ").append(headurl_with_logo).append("\r\n");
			sb.append(KEY_TINYURL_WITH_LOGO).append(" = ").append(tinyurl_with_logo).append("\r\n");
			return sb.toString();
		}
		
	}

}
