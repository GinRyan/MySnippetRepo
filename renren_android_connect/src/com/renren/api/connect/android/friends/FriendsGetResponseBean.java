/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.friends;

import org.json.JSONArray;
import org.json.JSONException;

import com.renren.api.connect.android.common.ResponseBean;

/**
 * 
 * @author hecao (he.cao@renren-inc.com)
 * 
 * friends.get接口 返回结果封装
 *
 */
public class FriendsGetResponseBean extends ResponseBean {
	
	public FriendsGetResponseBean(String response) {
		super(response);
		if (response == null) {
			return;
		}
        
        try {
			JSONArray array = new JSONArray(response);
			if (array != null) {
				int size = array.length();
				uids = new long[size];
				for (int i = 0 ; i < size ; i ++ ) {
					uids[i] = array.optLong(i);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 好友的uids
	 */
	private long[] uids;
	
	public long[] getUids () {
		return this.uids;
	}
	
	@Override
	public String toString () {
        StringBuffer sb = new StringBuffer();
        if (uids != null) {
            for (long uid : uids) {
                sb.append(uid).append("\r\n");
            }
        }
        return sb.toString();
	}


}
