/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.view;

import android.os.Bundle;

/**
 * widget相关操作监听。
 * 
 * @author 李勇(yong.li@opi-corp.com) 2011-4-7
 */
public interface RenrenWidgetListener {

    /**
     * 完成时调用
     * 
     * @param retParams
     */
    public void onComplete(Bundle retParams);

    /**
     * 出现未知错误
     */
    public void onError(Bundle retParams);

    /**
     * 用户取消时调用。
     */
    public void onCancel(Bundle retParams);
}
