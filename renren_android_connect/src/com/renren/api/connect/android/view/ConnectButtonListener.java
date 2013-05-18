/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.view;

import android.os.Bundle;

import com.renren.api.connect.android.exception.RenrenAuthError;

/**
 * 监听ConnectButton，来获取登入、登出情况。
 * 
 * @see RenrenAuthListener
 * @author 李勇(yong.li@opi-corp.com) 2010-7-19
 */
public interface ConnectButtonListener {

    /**
     * 登录成功后调用
     * 
     * @param values
     */
    public void onLogined(Bundle values);

    /**
     * 用户取消登录。
     */
    public void onCancelLogin();

    /**
     * 登出成功后调用
     * 
     */
    public void onLogouted();

    /**
     * 用户取消授权。
     */
    public void onCancelAuth(Bundle values);

    /**
     * 服务器返回错误内容时调用
     * 
     * @param renrenAuthError
     */
    public void onRenrenAuthError(RenrenAuthError renrenAuthError);

    /**
     * 出现严重错误时调用
     * 
     * @param error
     */
    public void onException(Exception error);
}
