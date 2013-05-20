/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.view;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;

import com.renren.api.connect.android.Util;
import com.renren.api.connect.android.exception.RenrenAuthError;

/**
 * @author 李勇(yong.li@opi-corp.com) 2010-7-19
 */
public class ConnectButtonListenerHelper implements ConnectButtonListener {

    private List<ConnectButtonListener> listeners = new ArrayList<ConnectButtonListener>();

    public void addConnectButtonListener(ConnectButtonListener connectButtonListener) {
        this.listeners.add(connectButtonListener);
    }

    public boolean removeConnectButtonListener(ConnectButtonListener connectButtonListener) {
        return this.listeners.remove(connectButtonListener);
    }

    @Override
    public void onException(Exception error) {
        for (ConnectButtonListener listener : listeners) {
            listener.onException(error);
        }
    }

    @Override
    public void onLogined(Bundle values) {
        for (ConnectButtonListener listener : listeners) {
            listener.onLogined(values);
        }
    }

    @Override
    public void onLogouted() {
        for (ConnectButtonListener listener : listeners) {
            listener.onLogouted();
        }
    }

    @Override
    public void onRenrenAuthError(RenrenAuthError renrenAuthError) {
        for (ConnectButtonListener listener : listeners) {
            listener.onRenrenAuthError(renrenAuthError);
        }
    }

    @Override
    public void onCancelAuth(Bundle values) {
        for (ConnectButtonListener listener : listeners) {
            listener.onCancelAuth(values);
        }
    }

    @Override
    public void onCancelLogin() {
        for (ConnectButtonListener listener : listeners) {
            listener.onCancelLogin();
        }
    }

    /**
     * 该类可以当作ConnectButtonListener接口的适配器来用；
     * 继承该类可以只实现ConnectButtonListener接口中你感兴趣的方法。
     * 
     * @author yong.li@opi-corp.com
     * 
     */
    public static class DefaultConnectButtonListener implements ConnectButtonListener {

        @Override
        public void onLogined(Bundle values) {
            Log.i(Util.LOG_TAG, "onLogined called.");
        }

        @Override
        public void onLogouted() {
            Log.i(Util.LOG_TAG, "onLogouted called.");
        }

        @Override
        public void onException(Exception error) {
            Log.e(Util.LOG_TAG, error.getMessage(), error);
        }

        @Override
        public void onCancelAuth(Bundle values) {
            Log.w(Util.LOG_TAG, "cancel auth.");
        }

        @Override
        public void onCancelLogin() {
            Log.w(Util.LOG_TAG, "cancel login.");
        }

        @Override
        public void onRenrenAuthError(RenrenAuthError renrenAuthError) {
            Log.e(Util.LOG_TAG, renrenAuthError.getMessage(), renrenAuthError);
        }
    }
}
