/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android;

import org.json.JSONException;
import org.json.JSONObject;

import com.renren.api.connect.android.common.ResponseBean;
import com.renren.api.connect.android.exception.RenrenException;

/**
 * 使用用户名和密码登陆返回数据的数据结构
 * 
 * @author hecao (he.cao@renren-inc.com)
 * 
 */
public class PasswordFlowResponseBean extends ResponseBean {
    
	/**
	 * 构造PasswordFlowResponseBean对象
	 * 
	 * @param response
	 *            返回的数据 json格式
	 * @throws RenrenException
	 */
    public PasswordFlowResponseBean(String response) {
		super(response);
        if (response == null) {
            return;
        }
        try {
            JSONObject object = new JSONObject(response);
            if (object != null) {
                this.accessToken = object.optString(KEY_ACCESS_TOKEN);
                this.expire = object.optLong(KEY_EXPIRES_IN);
                this.refreshToken = object.optString(KEY_REFRESH_TOKEN);
                this.scope = object.optString(KEY_SCOPE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
	}

	public static final String KEY_ACCESS_TOKEN = "access_token";
    public static final String KEY_EXPIRES_IN = "expires_in";
    public static final String KEY_REFRESH_TOKEN = "refresh_token";
    public static final String KEY_SCOPE = "scope";

    /**
     * access token
     */
    private String accessToken;

    /**
     * 过期时间，使用long型表示的时间
     */
    private long expire;

    /**
     * refresh token
     */
    private String refreshToken;

    /**
     * 已授权的权限列表
     */
    private String scope;

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public long getExpire() {
        return expire;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return scope;
    }
    
    @Override
    public String toString () {
        
        StringBuffer sb = new StringBuffer();
        sb.append(KEY_ACCESS_TOKEN).append(" = ").append(accessToken).append("\r\n");
        sb.append(KEY_EXPIRES_IN).append(" = ").append(expire).append("\r\n");
        sb.append(KEY_REFRESH_TOKEN).append("refreshToken = ").append(refreshToken).append("\r\n");
        sb.append(KEY_SCOPE).append("scope = ").append(scope).append("\r\n");
        return sb.toString();
        
    }

}
