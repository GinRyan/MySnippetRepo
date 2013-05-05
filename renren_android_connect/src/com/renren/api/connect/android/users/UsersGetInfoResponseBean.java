/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.users;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.renren.api.connect.android.common.ResponseBean;
import com.renren.api.connect.android.exception.RenrenException;

/**
 * users.getInfo接口的封装<br>
 * 
 * @author hecao (he.cao@renren-inc.com)
 * 
 */
public class UsersGetInfoResponseBean extends ResponseBean {
    
	/**
	 * 构造UsersGetInfoResponseBean对象
	 * @param response 返回的数据 json格式
	 * @throws RenrenException
	 */
    public UsersGetInfoResponseBean (String response) {
    	super(response);
    	if (response == null) {
            return;
        }
    	
        try {
            JSONArray array = new JSONArray(response);
            if (array != null) {
                users = new ArrayList<UserInfo>();
                int size = array.length();
                for (int i = 0; i < size; i++) {
                    UserInfo info = new UserInfo();
                    info.parse(array.optJSONObject(i));
                    if (info != null) {
                        users.add(info);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (RenrenException e) { 
			e.printStackTrace();
		}
	}

	/**
     * user数组
     */
    private ArrayList<UserInfo> users;

    public ArrayList<UserInfo> getUsersInfo () {
        return this.users;
    }
    
    @Override
    public String toString () {
        StringBuffer sb = new StringBuffer();
        if (users != null) {
            for (UserInfo info : users) {
                sb.append(info.toString()).append("\r\n");
            }
        }
        return sb.toString();
    }
    
    
}
