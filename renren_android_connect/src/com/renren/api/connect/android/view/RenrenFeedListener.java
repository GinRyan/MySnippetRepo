/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.view;

import android.os.Bundle;

import com.renren.api.connect.android.exception.RenrenError;

/**
 * 监听发自定义新鲜事的状态；以后可能会被widget取代。
 * 
 * @author 李勇(yong.li@opi-corp.com) 2010-7-15
 */
public interface RenrenFeedListener {

    /**
     * 新鲜事发送完成时调用
     * 
     * @param retParams
     */
    public void onComplete(Bundle retParams);

    /**
     * 服务器请求返回错误内容时调用（如参数不正确）
     */
    public void onRenrenError(RenrenError renrenError);

    /**
     * 出现未知错误
     */
    public void onError(Exception exception);

    /**
     * 用户取消时调用。
     */
    public void onCancel();
}
