/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.users;

import java.util.concurrent.Executor;

import android.os.Bundle;

import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.Util;
import com.renren.api.connect.android.common.AbstractRequestListener;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.exception.RenrenException;

/**
 * 
 * @author hecao (he.cao@renren-inc.com)
 * users.getInfo接口 助手类
 *
 */
public class UsersGetInfoHelper {
    
    
    /**
     * renren对象
     */
    private Renren renren;
    
    public UsersGetInfoHelper (Renren renren) {
        this.renren = renren;
    }
    
	/**
	 * 同步调用users.getInfo接口<br>
	 * 
	 * @param param
	 *            请求对象
	 * @return 返回{@link UsersGetInfoResponseBean}对象
	 * @throws RenrenException
	 */
    public UsersGetInfoResponseBean getUsersInfo (UsersGetInfoRequestParam param) throws RenrenException, Throwable {
        
        Bundle parameters = param.getParams();
        UsersGetInfoResponseBean usersBean = null;
        try {
            String response = renren.requestJSON(parameters);
			if (response != null) {
				Util.checkResponse(response, Renren.RESPONSE_FORMAT_JSON);

			} else {
				Util.logger("null response");
				throw new RenrenException(RenrenError.ERROR_CODE_UNKNOWN_ERROR, "null response", "null response");
			}
            usersBean = new UsersGetInfoResponseBean(response);
        } catch (RuntimeException re) {
			Util.logger("runtime exception " + re.getMessage());
			throw new Throwable(re);
		}
        return usersBean;
    }
    
	/**
	 * 异步方法调用users.getInfo接口<br>
	 * 
	 * @param pool
	 *            线程池
	 * @param param
	 *            请求对象
	 * @param listener
	 *            回调
	 */
    public void asyncGetUsersInfo (Executor pool, final UsersGetInfoRequestParam param, final AbstractRequestListener<UsersGetInfoResponseBean> listener) {
        
        pool.execute(new Runnable() {
            
            @Override
            public void run() {
           
                try {
                    UsersGetInfoResponseBean usersBean = getUsersInfo(param);
                    if (listener != null) {
                        listener.onComplete(usersBean);
                    }
                } catch (RenrenException e) {
                	Util.logger("renren exception " + e.getMessage());
                    if (listener != null) {
                        listener.onRenrenError(new RenrenError(e.getMessage()));
                        e.printStackTrace();
                    }
                } catch (Throwable e) {
                	Util.logger("on fault " + e.getMessage());
                    if (listener != null) {
                    	listener.onFault(e);
                    }
                }
                
            }
        });
        
    }

}
