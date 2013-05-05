/*
 * Copyright 2010 Renren, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.renren.api.connect.android;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.content.Context;
import android.os.Bundle;

import com.renren.api.connect.android.common.AbstractRequestListener;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.feed.FeedHelper;
import com.renren.api.connect.android.feed.FeedPublishRequestParam;
import com.renren.api.connect.android.feed.FeedPublishResponseBean;
import com.renren.api.connect.android.friends.FriendsGetFriendsRequestParam;
import com.renren.api.connect.android.friends.FriendsGetFriendsResponseBean;
import com.renren.api.connect.android.friends.FriendsGetRequestParam;
import com.renren.api.connect.android.friends.FriendsGetResponseBean;
import com.renren.api.connect.android.friends.FriendsHelper;
import com.renren.api.connect.android.photos.AlbumCreateRequestParam;
import com.renren.api.connect.android.photos.AlbumCreateResponseBean;
import com.renren.api.connect.android.photos.AlbumGetRequestParam;
import com.renren.api.connect.android.photos.AlbumGetResponseBean;
import com.renren.api.connect.android.photos.PhotoHelper;
import com.renren.api.connect.android.photos.PhotoUploadRequestParam;
import com.renren.api.connect.android.photos.PhotoUploadResponseBean;
import com.renren.api.connect.android.status.StatusHelper;
import com.renren.api.connect.android.status.StatusSetRequestParam;
import com.renren.api.connect.android.status.StatusSetResponseBean;
import com.renren.api.connect.android.users.UsersGetInfoHelper;
import com.renren.api.connect.android.users.UsersGetInfoRequestParam;
import com.renren.api.connect.android.users.UsersGetInfoResponseBean;
import com.renren.api.connect.android.view.RenrenAuthListener;

/**
 * 对人人的请求封装成异步。注意：使用该类调用人人接口时，不能在Listener中直接更新UI控件。
 * 
 * @see Renren
 * @see RequestListener
 * 
 * @author yong.li@opi-corp.com
 * 
 */
public class AsyncRenren {

    private Renren renren;

    private Executor pool;

    public AsyncRenren(Renren renren) {
        this.renren = renren;
        this.pool = Executors.newFixedThreadPool(2);
    }

    /**
     * 退出登录
     * 
     * @param context
     * @param listener 注意监听器中不在主线程中执行，所以不能在监听器中直接更新UI代码。
     */
    public void logout(final Context context, final RequestListener listener) {
        pool.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    String resp = renren.logout(context);
                    RenrenError rrError = Util.parseRenrenError(resp, Renren.RESPONSE_FORMAT_JSON);
                    if (rrError != null) listener.onRenrenError(rrError);
                    else listener.onComplete(resp);
                } catch (Throwable e) {
                    listener.onFault(e);
                }
            }
        });
    }

    /**
     * 调用 人人 APIs，服务器的响应是JSON串。
     * 
     * 人人 APIs 详细信息见 http://wiki.dev.renren.com/wiki/API
     * 
     * @param parameters 注意监听器中不在主线程中执行，所以不能在监听器中直接更新UI代码。
     * @param listener
     */
    public void requestJSON(Bundle parameters, RequestListener listener) {
        request(parameters, listener, Renren.RESPONSE_FORMAT_JSON);
    }

    /**
     * 调用 人人 APIs 服务器的响应是XML串。
     * 
     * 人人 APIs 详细信息见 http://wiki.dev.renren.com/wiki/API
     * 
     * @param parameters 注意监听器中不在主线程中执行，所以不能在监听器中直接更新代码。
     * @param listener
     */
    public void requestXML(Bundle parameters, RequestListener listener) {
        request(parameters, listener, Renren.RESPONSE_FORMAT_XML);
    }

    /**
     * 调用 人人 APIs。
     * 
     * 人人 APIs 详细信息见 http://wiki.dev.renren.com/wiki/API
     * 
     * @param parameters
     * @param listener 注意监听器中不在主线程中执行，所以不能在监听器中直接更新UI代码。
     * @param format return data format (json or xml)
     */
    private void request(final Bundle parameters, final RequestListener listener,
            final String format) {
        pool.execute(new Runnable() {

            @Override
            public void run() {

                try {
                    String resp = "";
                    if ("xml".equalsIgnoreCase(format)) {
                        resp = renren.requestXML(parameters);
                    } else {
                        resp = renren.requestJSON(parameters);
                    }
                    RenrenError rrError = Util.parseRenrenError(resp, format);
                    if (rrError != null) {
                        listener.onRenrenError(rrError);
                    } else {
                        listener.onComplete(resp);
                    }
                } catch (Throwable e) {
                    listener.onFault(e);
                }
            }
        });
    }
    
	/**
	 * 异步方法<br>
	 * 使用用户名和密码完成登陆和授权 <br>
	 * 
	 * @see Renren.authorize
	 * @param param
	 *            请求对象
	 * @param listener
	 *            登陆状态的监听器
	 */
	public void authorize(PasswordFlowRequestParam param, RenrenAuthListener listener) {
		PasswordFlowHelper passwordFlowHelper = new PasswordFlowHelper();
		passwordFlowHelper.login(pool, param, listener, renren);
	}
    
	/**
	 * 
	 * @see Renren.uploadPhoto
	 */
    public void publishPhoto(final long albumId, final byte[] photo, final String fileName,
            final String description, final String format, final RequestListener listener) {
        pool.execute(new Runnable() {

            @Override
            public void run() {

                try {
                    String resp = renren.publishPhoto(albumId, photo, fileName, description, format);
                    RenrenError rrError = Util.parseRenrenError(resp, format);
                    if (rrError != null) {
                        listener.onRenrenError(rrError);
                    } else {
                        listener.onComplete(resp);
                    }
                } catch (Throwable e) {
                    listener.onFault(e);
                }
            }
        });
    }
    
	/**
	 * users.getInfo接口<br>
	 * http://wiki.dev.renren.com/wiki/Users.getInfo
	 * @see Renren.getUsersInfo
	 * 
	 * @param param
	 *            请求参数
	 * @see Renren.getUsersInfo
	 */
    public void getUsersInfo (UsersGetInfoRequestParam param, AbstractRequestListener<UsersGetInfoResponseBean> listener) {
        UsersGetInfoHelper helper = new UsersGetInfoHelper(renren);
        helper.asyncGetUsersInfo(pool, param, listener);
    }
    
	/**
	 * 发状态，异步调用status.set
	 * 参见http://wiki.dev.renren.com/wiki/Status.set
	 * 
	 * @param status
	 *          要发布的状态
	 * @param listener
	 *          用以监听发布状态结果的监听器对象
	 * @param truncOption
	 *          若超出了长度，是否自动截短至140个字
	 */
    public void publishStatus(StatusSetRequestParam status,
    		AbstractRequestListener<StatusSetResponseBean> listener, 
    		boolean truncOption) {
    	StatusHelper helper = new StatusHelper(renren);
    	helper.asyncPublish(pool, status, listener, truncOption);
    }
    
	/**
	 * 发送新鲜事<br>
	 * 异步调用feed.publishFeed接口
	 * 参见http://wiki.dev.renren.com/wiki/Feed.publishFeed
	 * 
	 * @param feed
	 *          要发布的新鲜事
	 * @param listener
	 *          用以监听发布新鲜事结果的监听器对象
	 * @param truncOption
	 *          若超出了长度，是否自动截短至限制的长度
	 */
	public void publishFeed(FeedPublishRequestParam feed,
			AbstractRequestListener<FeedPublishResponseBean> listener,
			boolean truncOption) {
		FeedHelper helper = new FeedHelper(renren);
		helper.asyncPublish(pool, feed, listener, truncOption);
	}
	
	/**
	 * 创建相册的异步接口
	 * 
	 * @param album
	 * 			调用此接口需准备的参数
	 * @param listener
	 *            开发者实现，对返回结果进行操作
	 *            
	 * 			  <p>详情请参考 http://wiki.dev.renren.com/wiki/Photos.createAlbum
	 */
	public void createAlbum(AlbumCreateRequestParam album,
			AbstractRequestListener<AlbumCreateResponseBean> listener) {
		new PhotoHelper(renren).asyncCreateAlbum(album, listener);
	}
	
	/**
	 * 获取相册的异步接口
	 * 
	 * @param album	
	 * 			调用此接口需准备的参数
	 * @param listener
	 *            开发者实现，对返回结果进行操作
	 *            
	 *            <p>详情请参考 http://wiki.dev.renren.com/wiki/Photos.getAlbums
	 * 
	 */
	public void getAlbums(AlbumGetRequestParam album,
			AbstractRequestListener<AlbumGetResponseBean> listener) {
		new PhotoHelper(renren).asyncGetAlbums(album, listener);
	}
	
	/**
	 * 上传照片的异步接口
	 * 
	 * @param photo
	 * 			调用此接口需准备的参数
	 * @param listener
	 *            开发者实现，对返回结果进行操作
	 * 
	 * 			  <p>详情请参考 http://wiki.dev.renren.com/wiki/Photos.upload
	 */
	public void publishPhoto(PhotoUploadRequestParam photo,
			AbstractRequestListener<PhotoUploadResponseBean> listener) {
		new PhotoHelper(renren).asyncUploadPhoto(photo, listener);
	}

	/**
	 * friends.get接口 得到当前登录用户的好友列表，得到的只是含有好友id的列表<br>
	 * http://wiki.dev.renren.com/wiki/Friends.get
	 * 
	 * @see Renren.getFriends
	 * @param param
	 * 			调用接口需要的参数
	 * @param listener
	 * 			接口调用的监听器
	 */
	public void getFriends (FriendsGetRequestParam param, AbstractRequestListener<FriendsGetResponseBean> listener) {
		new FriendsHelper(renren).asyncGetFriends(pool, param, listener);
	}
	
	/**
	 * friends.getFriends接口  得到当前登录用户的好友列表<br>
	 * http://wiki.dev.renren.com/wiki/Friends.getFriends
	 * 
	 * @see Renren.getFriends
	 * @param param
	 * 			调用接口需要的参数
	 * @param listener
	 * 			接口调用的监听器
	 */
	public void getFriends (FriendsGetFriendsRequestParam param, AbstractRequestListener<FriendsGetFriendsResponseBean> listener) {
		new FriendsHelper(renren).asyncGetFriends(pool, param, listener);
	}
}
