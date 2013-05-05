/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.exception;
/**
 * 
 * 接口调用异常，需要开发者进行处理
 * 
 * @author hecao (he.cao@renre-inc.com)
 *
 */
public class RenrenException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * 服务器返回的错误代码，详细信息见：
     * http://wiki.dev.renren.com/wiki/API%E9%94%99%E8%AF%AF%E4
     * %BB%A3%E7%A0%81%E6%9F%A5%E8%AF%A2
     */
    private int errorCode;

    private String orgResponse;

    public RenrenException(String errorMessage) {
        super(errorMessage);
    }

    public RenrenException(int errorCode, String errorMessage, String orgResponse) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.orgResponse = orgResponse;
    }

    public RenrenException(RenrenError error) {
        super (error.getMessage());
        this.errorCode = error.getErrorCode();
        this.orgResponse = error.getOrgResponse();
    }

    public String getOrgResponse() {
        return orgResponse;
    }

    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return "errorCode:" + this.errorCode + "\nerrorMessage:" + this.getMessage()
                + "\norgResponse:" + this.orgResponse;
    }

}
