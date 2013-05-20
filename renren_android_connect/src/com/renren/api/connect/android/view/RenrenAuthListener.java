/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.view;

import android.os.Bundle;

import com.renren.api.connect.android.exception.RenrenAuthError;

/**
 * 监听认证，和授权动作。
 * 
 * @author 李勇(yong.li@opi-corp.com) 2011-2-25
 */
public interface RenrenAuthListener {

    /**
     * 登录和授权完成后调用。
     * 
     * @param values key:授权服务器返回的参数名，value:是参数值。
     */
    public void onComplete(Bundle values);

    /**
     * 服务器返回错误
     * 
     * @param renrenAuthError
     */
    public void onRenrenAuthError(RenrenAuthError renrenAuthError);

    /**
     * 用户取消登录。
     */
    public void onCancelLogin();

    /**
     * 用户取消授权。
     * 
     * @param values key:授权服务器返回的参数名，value:是参数值。
     */
    public void onCancelAuth(Bundle values);

}
