/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.exception;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 认证、授权错误。
 * 详细信息见http://wiki.dev.renren.com/wiki/%E9%94%99%E8%AF%AF%E5%93%8D
 * %E5%BA%94
 * 
 * @author 李勇(yong.li@opi-corp.com) 2011-2-25
 */
public class RenrenAuthError extends RuntimeException {
	
    private static final long serialVersionUID = 1L;

    private String error;

    private String errorDescription;

    private String errorUri;

    public RenrenAuthError(String error, String errorDescription, String errorUri) {
        super(errorDescription);
        this.error = error;
        this.errorDescription = errorDescription;
        this.errorUri = errorUri;
    }

    public JSONObject getJSONObjectError() {
        return genJSONObjectError(error, errorUri, errorDescription);
    }

    public static JSONObject genJSONObjectError(String error, String errorUri,
            String errorDescription) {
        if (error == null) error = "";
        if (errorUri == null) errorUri = "";
        if (errorDescription == null) errorDescription = "";
        JSONObject errorObj = new JSONObject();
        try {
            errorObj.put("error", error);
            errorObj.put("error_uri", errorUri);
            errorObj.put("error_description", errorDescription);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return errorObj;
    }

    public String getError() {
        return error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public String getErrorUri() {
        return errorUri;
    }
}
