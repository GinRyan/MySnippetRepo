/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.renren.api.connect.android.exception.RenrenAuthError;
import com.renren.api.connect.android.view.RenrenAuthListener;
import com.renren.api.connect.android.view.RenrenWidgetListener;

/**
 * @author 李勇(yong.li@opi-corp.com) 2011-6-9
 */
public class SSO {

    private static final String LOG_TAG = "SSO";

    private Renren renren;

    private int mRequestCode;

    private RenrenAuthListener mAuthListener;

    private RenrenWidgetListener mWidgetDialogListener;

    private Activity mActivity;

    private String[] authPermissions;

    SSO(Renren renren) {
        this.renren = renren;
    }

    /**
     * 使用Single Sign-on功能 完成登录和授权(User-Agent Flow)。
     * 警告：使用此方法获取返回值请在调用此方法的activity中 重写onActivityResult
     * 并在onActivityResult中调用renren.authorizeCallback
     * 
     * @param activity
     * @param permissions 应用想拥有的权限列表。
     * @param requestCode 在callback时需要的requestCode.
     * @param listener
     */
    public void authorize(Activity activity, String[] permissions,
            final RenrenAuthListener listener, int requestCode) {
        mAuthListener = listener;
        Intent intent = new Intent();
        intent.putExtra("client_id", this.renren.getApiKey());
        if (permissions != null && permissions.length > 0) {
            intent.putExtra("scope", TextUtils.join(",", permissions));
        }
        mActivity = activity;
        authPermissions = permissions;
        mRequestCode = requestCode;

        boolean succeed = this.tryRRClientAuth(intent);
        if (!succeed) {
            this.renren.authorize(activity, permissions, mAuthListener);
        }
    }

    private boolean tryRRClientAuth(Intent intent) {
        boolean didSucceed = true;
        intent.setClassName("com.renren.mobile.android",
                "com.renren.mobile.android.auth.SSOAuthActivity");
        if (!validateAppSignatureForIntent(this.mActivity, intent, RR_APP_SIGNATURE)) {
            return false;
        }
        Log.d(LOG_TAG, "start renren client sso.");
        didSucceed = this.startActivity(intent);
        Log.d(LOG_TAG, "renren client sso succeed:" + didSucceed);
        return didSucceed;
    }

    /**
     * 注意：使用单点登录功能时请务必在调用activity中 重写onActivityResult(int requestCode, int
     * resultCode, Intent data)方法 并在该方法中调用authorizeCallback才可以获得返回值。
     */
    public void authorizeCallback(int requestCode, int resultCode, Intent data) {
        if (requestCode == mRequestCode) {

            // Successfully redirected.
            if (resultCode == Activity.RESULT_OK) {

                // Check OAuth 2.0/2.10 error code.
                String error = data.getStringExtra("error");
                String error_description = data.getStringExtra("error_description");

                if (error != null) {
                    if (error.equals("service_disabled")) {
                        Log.d(LOG_TAG, "Hosted auth currently "
                                + "disabled. Retrying dialog auth...");
                        this.renren.authorize(mActivity, authPermissions, mAuthListener);
                    } else if (error.equals("access_denied")) {
                        Log.d(LOG_TAG, "Auth canceled by user.");
                        mAuthListener.onCancelAuth(data.getExtras());
                    } else {
                        Log.d(LOG_TAG, "Login failed: " + error);
                        mAuthListener.onRenrenAuthError(new RenrenAuthError(error,
                                error_description, null));
                    }
                } else {// No errors.
                    Log.d(LOG_TAG, "access_token:" + data.getStringExtra("access_token"));
                    try {
                        this.renren.updateAccessToken(data.getStringExtra("access_token"));
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                        Log.w(LOG_TAG, e.getMessage());
                    }

                    if (this.renren.isSessionKeyValid()) {
                        mAuthListener.onComplete(data.getExtras());
                    } else {
                        mAuthListener.onRenrenAuthError(new RenrenAuthError("unknow",
                                "Failed to receive access token.", null));
                    }
                }
                // An error occurred before we could be redirected.
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // An Android error occured.
                if (data != null) {
                    Log.d(LOG_TAG, "Login failed: " + data.getStringExtra("error"));
                    mAuthListener.onRenrenAuthError(new RenrenAuthError(data
                            .getStringExtra("error"), data.getStringExtra("error_description"),
                            data.getStringExtra("failing_url")));
                } else {// User pressed the 'back' button.
                    Log.d(LOG_TAG, "Canceled by user.");
                    Bundle values = new Bundle();
                    values.putString("error", "press_back");
                    values.putString("error_description", "User press back key.");
                    mAuthListener.onCancelAuth(values);
                }
            }
        }
    }

    public void widgetDialog(Activity activity, Bundle params, final RenrenWidgetListener listener,
            String url, int requestCode) {
        Intent intent = new Intent();
        intent.putExtra("client_id", this.renren.getApiKey());
        //widget_dialog_url SSOLib使用
        intent.putExtra("widget_dialog_url", url);
        this.mWidgetDialogListener = listener;
        mActivity = activity;
        mRequestCode = requestCode;
        intent.putExtras(params);
        boolean succee = this.tryRRClientDialog(intent);
        if (!succee) {
            this.renren.widgetDialog(activity, params, listener, url);
        }
    }

    private boolean tryRRClientDialog(Intent intent) {
        intent
                .setClassName("com.renren.mobile.android",
                        "com.renren.mobile.android.auth.SSODialog");
        if (!validateAppSignatureForIntent(this.mActivity, intent, RR_APP_SIGNATURE)) {
            return false;
        }
        Log.d(LOG_TAG, "start renren client sso dialog...");
        boolean succeed = this.startActivity(intent);
        Log.d(LOG_TAG, "renren client sso dialog succeed:" + succeed);
        return succeed;
    }

    public void widgetDialogCallback(int requestCode, int resultCode, Intent data) {
        if (requestCode == mRequestCode) {
            if (data != null) {
                Bundle values = data.getExtras();
                String error = values.getString("error");
                if (error == null) {
                    this.mWidgetDialogListener.onComplete(values);
                } else if ("access_denied".equals(error)) {
                    this.mWidgetDialogListener.onCancel(values);
                } else {
                    this.mWidgetDialogListener.onError(values);
                }
            } else {
                Bundle values = new Bundle();
                values.putString("error", "press_back");
                this.mWidgetDialogListener.onCancel(values);
            }
        }
    }

    private boolean validateAppSignatureForIntent(Activity activity, Intent intent,
            String targetSignatures) {

        ResolveInfo resolveInfo = activity.getPackageManager().resolveActivity(intent, 0);
        if (resolveInfo == null) {
            Log.i(LOG_TAG, "className:" + intent.getComponent().getClassName());
            Log.i(LOG_TAG, "resolveInfo is null!");
            return false;
        }

        String packageName = resolveInfo.activityInfo.packageName;
        PackageInfo packageInfo;
        try {
            packageInfo = activity.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);
        } catch (NameNotFoundException e) {
            Log.w(LOG_TAG, "obtain package info exception:" + e.getMessage());
            return false;
        }

        for (Signature signature : packageInfo.signatures) {
            if (signature.toCharsString().equals(targetSignatures)) {
                return true;
            }
        }
        return false;
    }

    private boolean startActivity(Intent intent) {
        try {
            this.mActivity.startActivityForResult(intent, this.mRequestCode);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.w(LOG_TAG, "start activity " + intent.getComponent().getClassName() + " error "
                    + e.getMessage());
        }
        return false;
    }

    private static final String RR_APP_SIGNATURE = "3082023e308201a702044b85da7d300d06092"
            + "a864886f70d01010405003065310b300906035504061302434e3110300e0603550408130"
            + "76265696a696e673110300e060355040713076265696a696e67310c300a060355040a130"
            + "36f706931133011060355040b130a72656e72656e2e636f6d310f300d060355040313067"
            + "2656e72656e3020170d3130303232353032303334315a180f323036343131323830323033"
            + "34315a3065310b300906035504061302434e3110300e060355040813076265696a696e67311"
            + "0300e060355040713076265696a696e67310c300a060355040a13036f70693113301106035"
            + "5040b130a72656e72656e2e636f6d310f300d0603550403130672656e72656e30819f300d0"
            + "6092a864886f70d010101050003818d0030818902818100e21721992472cccca98841c6087b"
            + "732b03045240e1ba2f0102888d8346305c2f032cf874feca6a39461026ceaf6cf637b94e76f9d0"
            + "2812236c1d645073ec87cf17e55068c8155acd2c920ba1d192a5731883fcfa382fea62a45c8d77"
            + "90291b085fc7d4e6358a5799b0e752aa10d7560651a705e6909ae49cb4a078c064fcf89502030"
            + "10001300d06092a864886f70d010104050003818100726d02419a0a9b413d0c8af0dec12b0e15"
            + "41b57e8536840e223081f971750d41a493e8968c1f3f32da9e0e85f10e65a0ed11b0e0c61afc51aa"
            + "49c89a5ad549136d960862108aade918726465da15f24e0adf71c84d370d289b349e4ab35262a8"
            + "cbd50e05acd6fbed092c2200e79fa0dc2cfa539ace46525f7772766f3524d0c9";
}
