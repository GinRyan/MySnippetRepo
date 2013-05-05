/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.users;

import android.os.Bundle;

import com.renren.api.connect.android.common.RequestParam;
import com.renren.api.connect.android.exception.RenrenException;

/**
 * 
 * @author hecao (he.cao@renren-inc.com)
 * users.getInfo接口的请求参数
 * 
 *
 */
public class UsersGetInfoRequestParam extends RequestParam {
	
	private static final String METHOD = "users.getInfo";
	

    /**
     * 所有字段
     */
    public static final String FIELDS_ALL = UserInfo.KEY_UID + "," +
                                                UserInfo.KEY_NAME + "," +
                                                UserInfo.KEY_SEX + "," +
                                                UserInfo.KEY_STAR + "," +
                                                UserInfo.KEY_ZIDOU + "," +
                                                UserInfo.KEY_VIP + "," +
                                                UserInfo.KEY_BIRTHDAY + "," +
                                                UserInfo.KEY_EMAIL_HASH + "," +
                                                UserInfo.KEY_TINYURL + "," +
                                                UserInfo.KEY_HEADURL + "," +
                                                UserInfo.KEY_MAINURL + "," +
                                                UserInfo.KEY_HOMETOWN_LOCATION + "," +
                                                UserInfo.KEY_WORK_INFO + "," +
                                                UserInfo.KEY_UNIVERSITY_INFO + "," +
                                                UserInfo.KEY_HS_INFO;
    
    /**
     * 默认字段<br>
     * 不添加fields参数也按此字段返回
     */
    public static final String FIELD_DEFAULT = UserInfo.KEY_UID + "," +
                                                UserInfo.KEY_NAME + "," +
                                                UserInfo.KEY_TINYURL + "," +
                                                UserInfo.KEY_HEADURL + "," +
                                                UserInfo.KEY_ZIDOU + "," +
                                                UserInfo.KEY_STAR;
	
	/**
	 * 需要获取的用户uid的数组
	 */
	private String[] uids;
	
	/**
	 * 需要获取的字段
	 */
	private String fields = FIELD_DEFAULT;
		
	/**
	 * 构造一个users.getInfo接口请求参数
	 * @param uids 需要获取的用户uid的数组
	 */
	public UsersGetInfoRequestParam(String[] uids) {
		this.uids = uids;
	}
	
	/**
	 * 构造一个users.getInfo接口请求参数
	 * @param uids 需要获取的用户uid的数组
	 * @param fields 需要获取的字段
	 */
	public UsersGetInfoRequestParam(String[] uids, String fields) {
		this.uids = uids;
		this.setFields(fields);
	}

	/**
	 * 获取uids
	 * @return
	 */
	public String[] getUids() {
		return uids;
	}

	/**
	 * 设置uids
	 * @param uids
	 */
	public void setUids(String[] uids) {
		this.uids = uids;
	}

	/**
	 * 获取fields
	 * @return
	 */
	public String getFields() {
		return fields;
	}

	/**
	 * 设置fields
	 * @param fields
	 */
	public void setFields(String fields) {
		this.fields = fields;
	}

	@Override
	public Bundle getParams() throws RenrenException {
		
        Bundle parameters = new Bundle ();
        parameters.putString("method", METHOD);
        if (fields != null) {
            parameters.putString("fields", fields);
        }
		if (uids != null) {
	        StringBuffer sb = new StringBuffer();
	        for (String uid : uids) {
	            sb.append(uid).append(",");
	        }
	        sb.deleteCharAt(sb.length() - 1);
	        parameters.putString("uids", sb.toString());
        }
        return parameters;
	}


}
