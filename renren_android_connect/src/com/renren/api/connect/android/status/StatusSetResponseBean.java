/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.status;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

import com.renren.api.connect.android.common.ResponseBean;

/**
 * 对StatusSetResponseBean的请求成功后的响应结果进行封装
 * 
 * @author Shaofeng Wang (shaofeng.wang@renren-inc.com)
 *
 */
public class StatusSetResponseBean extends ResponseBean implements Parcelable{
	
	private static final String RESULT = "result";
	
	public static final int DEFAULT_RESULT = 0;
	/**
	 * status.get接口返回的result
	 */
	private int result;
	
	public StatusSetResponseBean(String response) {
		super(response);
		try {
			JSONObject json = new JSONObject(response);
			result = json.getInt(RESULT);
		} catch(JSONException je) {
			result = DEFAULT_RESULT;
		}
	}
	public StatusSetResponseBean(Parcel in) {
		super("");
		result = in.readInt();
	}
	
	public StatusSetResponseBean(int result) {
		super("");
		this.result = result;
	}

	public int getResult() {
		return result;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(result);
	}

	public static final Parcelable.Creator<StatusSetResponseBean> CREATOR
             = new Parcelable.Creator<StatusSetResponseBean>() {
         public StatusSetResponseBean createFromParcel(Parcel in) {
             return new StatusSetResponseBean(in);
         }

         public StatusSetResponseBean[] newArray(int size) {
             return new StatusSetResponseBean[size];
         }
    };

	@Override
	public String toString() {
		return "result: " + result;
	}
}
