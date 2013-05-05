/**
 * $id$
 * Copyright 2011-2012 Renren Inc. All rights reserved.
 */
package com.renren.api.connect.android.photos;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.Util;
import com.renren.api.connect.android.common.AbstractRequestListener;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.exception.RenrenException;

/**
 * 封装对相册，照片相关API的实现
 * 
 * @author sunting ting.sun@renren-inc.com
 * 
 */
public class PhotoHelper {

	/**
	 * 调用API所需的权限
	 */
	public final static String CREATE_ALBUM_PERMISSION = "create_album";
	public final static String UPLOAD_PHPTO_PERMISSION = "photo_upload";
	public final static String GET_ALBUMS_PERMISSION = "read_user_album";

	/**
	 * 请求数据的renren对象
	 */
	private Renren renren;
	/**
	 * 异步调用线程池
	 */
	private Executor pool;

	public PhotoHelper(Renren renren) {
		this.renren = renren;
		this.pool = Executors.newFixedThreadPool(2);
	}

	/**
	 * 跳转到一键创建相册的界面<br>
	 * 
	 * <p>
	 * 调用此方法需在AndroidManifest.
	 * xml文件中配置CreateAlbumActivity和AuthorizationHelper$BlockActivity
	 * </p>
	 * 
	 * @param activity
	 *            调用此方法的Activity
	 */
	public void startCreateAlbumActivity(final Activity activity) {
		Intent intent = new Intent(activity, CreateAlbumActivity.class);
		// 传递Renren对象
		intent.putExtra(Renren.RENREN_LABEL, renren);
		activity.startActivity(intent);
	}

	/**
	 * 跳转到一键上传照片的界面<br>
	 * 
	 * <p>
	 * 调用此方法需在AndroidManifest.
	 * xml文件中配置UploadPhotoActivity和AuthorizationHelper$BlockActivity
	 * </p>
	 * 
	 * @param activity
	 *            调用此方法的Activity
	 * @param file
	 *            上传的文件，必须有
	 * @param caption
	 *            照片描述，非必须参数，可为空
	 */
	public void startUploadPhotoActivity(Activity activity, File file,
			String caption) {
		Intent intent = new Intent(activity, UploadPhotoActivity.class);
		// 传递Renren对象
		intent.putExtra(Renren.RENREN_LABEL, renren);
		// 传递文件对象
		intent.putExtra("file", file);
		if (caption != null) {
			intent.putExtra("caption", caption);
		}
		activity.startActivity(intent);
	}

	/**
	 * 创建相册<br>
	 * 需要create_album权限<br>
	 * 注：不支持设置密码<br>
	 * 
	 * @param albumRequest
	 *            参数，必需设置name字段，location和description为可选字段，visible默认为99
	 * @return 成功返回创建相册的结果，失败返回null
	 * @throws RenrenException
	 *             , Throwable
	 */
	public AlbumCreateResponseBean createAlbum(
			AlbumCreateRequestParam albumRequest) throws RenrenException,
			Throwable {
		// 同步数据接口只判断用户是否登录，不提供自动跳转到验证界面验证的功能
		if (!renren.isSessionKeyValid()) {
			// 若用户没有登录，则直接抛出异常
			Util.logger("exception in creating album: no login!");
			throw new RenrenException(RenrenError.ERROR_CODE_TOKEN_ERROR,
					"没有登录", "没有登录");
		}

		if (albumRequest == null) {
			albumRequest = new AlbumCreateRequestParam();
		}

		// 请求数据，这里可能会产生连接网络的一些异常，需捕获，详见Util.open()方法
		String result = null;
		try {
			result = renren.requestJSON(albumRequest.getParams());
		} catch (RuntimeException e) {
			Util.logger("exception in creating album:error in internet requesting\t"
					+ e.getMessage());
			throw new Throwable(e);
		}

		if (result == null) {
			return null;
		}

		// 检查请求结果是否为错误信息
		Util.checkResponse(result, Renren.RESPONSE_FORMAT_JSON);

		// 用请求结果串构造返回对象
		AlbumCreateResponseBean albumResponse = new AlbumCreateResponseBean(
				result);

		// 相册已经创建成功
		Util.logger("success creating an album! \n\t" + albumResponse);

		return albumResponse;
	}

	/**
	 * 异步创建相册<br>
	 * 需要create_album权限<br>
	 * 注：不支持设置密码<br>
	 * 
	 * @param albumRequest
	 *            请求需传入的参数
	 * @param listener
	 *            使用此listener处理请求返回结果
	 */
	public void asyncCreateAlbum(final AlbumCreateRequestParam albumRequest,
			final AbstractRequestListener<AlbumCreateResponseBean> listener) {
		// 异步方法也不做登录验证
		pool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					AlbumCreateResponseBean albumResponse = createAlbum(albumRequest);

					if (albumResponse != null) {
						Util.logger("success creating an album! \n\t"
								+ albumResponse);

						if (listener != null) {
							listener.onComplete(albumResponse);
						}
					}
				} catch (RenrenException e) {
					Util.logger("exception in creating album: "
							+ e.getMessage());

					if (listener != null) {
						listener.onRenrenError(new RenrenError(
								e.getErrorCode(), e.getMessage(), e
										.getOrgResponse()));
					}
				} catch (Throwable e) {
					Util.logger("fault in creating album: " + e.getMessage());

					if (listener != null) {
						listener.onFault(e);
					}
				}
			}
		});
	}

	/**
	 * 获取相册信息<br>
	 * 需要read_user_album权限<br>
	 * 
	 * @param albumsRequest
	 *            请求参数
	 * @param listener
	 *            使用此listener处理返回结果
	 * @return 成功返回结果实体，失败返回null
	 * @throws RenrenException
	 *             ,Throwable
	 */
	public AlbumGetResponseBean getAlbums(AlbumGetRequestParam albumsRequest)
			throws RenrenException, Throwable {
		// 同步数据接口只判断用户是否登录，不提供自动跳转到验证界面验证的功能
		if (!renren.isSessionKeyValid()) {
			// 若用户没有登录，则直接抛出异常
			Util.logger("exception in getting albums: no login!");
			throw new RenrenException(RenrenError.ERROR_CODE_TOKEN_ERROR,
					"没有登录", "没有登录");
		}

		if (albumsRequest == null) {
			albumsRequest = new AlbumGetRequestParam();
		}

		// 如果uid为null，那么设置uid为当前登录的用户的uid
		if (albumsRequest.getUid() == null) {
			albumsRequest.setUid(renren.getCurrentUid());
		}

		// 请求数据，这里可能会产生连接网络的一些异常，需捕获，详见Util.open()方法
		String result = null;
		try {
			result = renren.requestJSON(albumsRequest.getParams());
		} catch (RuntimeException e) {
			Util.logger("exception in getting album:error in internet requesting\t"
					+ e.getMessage());
			throw new Throwable(e);
		}

		// 检查请求返回字符串是否错误信息
		Util.checkResponse(result, Renren.RESPONSE_FORMAT_JSON);

		AlbumGetResponseBean albumsResponse = new AlbumGetResponseBean(result);

		// 成功获取相册，记录日志
		Util.logger("success getting albums! \n\t" + albumsResponse);

		return albumsResponse;
	}

	/**
	 * 异步获取相册信息<br>
	 * 需要read_user_album权限<br>
	 * 
	 * @param albumsRequest
	 *            请求参数
	 * @param listener
	 *            使用此listener获取请求结果
	 */
	public void asyncGetAlbums(final AlbumGetRequestParam albumsRequest,
			final AbstractRequestListener<AlbumGetResponseBean> listener) {
		// 异步方法也不做登录验证
		pool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					AlbumGetResponseBean albumsResponse = getAlbums(albumsRequest);

					if (albumsResponse != null) {
						Util.logger("success getting albums! \n\t"
								+ albumsResponse);

						if (listener != null) {
							listener.onComplete(albumsResponse);
						}
					}
				} catch (RenrenException e) {
					Util.logger("exception in getting albums: "
							+ e.getMessage());

					if (listener != null) {
						listener.onRenrenError(new RenrenError(
								e.getErrorCode(), e.getMessage(), e
										.getOrgResponse()));
					}
				} catch (Throwable e) {
					Util.logger("fault in getting albums: " + e.getMessage());
					if (listener != null) {
						listener.onFault(e);
					}
				}
			}
		});
	}

	/**
	 * 上传照片<br>
	 * 需要photo_upload权限<br>
	 * 
	 * @param file
	 *            上传的文件
	 * @return 成功返回请求结果，失败返回null
	 * @throws RenrenException
	 *             , Throwable
	 */
	public PhotoUploadResponseBean uploadPhoto(File file)
			throws RenrenException, Throwable {
		PhotoUploadRequestParam photoRequest = new PhotoUploadRequestParam();
		photoRequest.setFile(file);
		return uploadPhoto(photoRequest);
	}

	/**
	 * 上传照片<br>
	 * 需要photo_upload权限<br>
	 * 
	 * @param photoRequest
	 *            请求参数
	 * @return 成功返回请求结果，失败返回null
	 * @throws RenrenException
	 *             , Throwable
	 */
	public PhotoUploadResponseBean uploadPhoto(
			PhotoUploadRequestParam photoRequest) throws RenrenException,
			Throwable {
		// 同步数据接口只判断用户是否登录，不提供自动跳转到验证界面验证的功能
		if (!renren.isSessionKeyValid()) {
			// 若用户没有登录，则直接抛出异常
			Util.logger("exception in uploading photo: no login!");
			throw new RenrenException(RenrenError.ERROR_CODE_TOKEN_ERROR,
					"没有登录", "没有登录");
		}

		if (photoRequest == null) {
			photoRequest = new PhotoUploadRequestParam();
		}

		if (photoRequest.getFile() == null) {
			Util.logger("exception in uploading photo: no upload photo file!");
			throw new RenrenException(RenrenError.ERROR_CODE_NULL_PARAMETER,
					"上传失败，没有文件！", "上传失败，没有文件！");
		}

		// 检查文件类型是否合法，目前支持jpg/jpeg, png, bmp, gif，建议为jpg或者png格式
		String fileName = photoRequest.getFile().getName();

		if (!fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg")
				&& !fileName.endsWith(".png") && !fileName.endsWith(".bmp")
				&& !fileName.endsWith("gif")) {
			Util.logger("exception in uploading photo: file format is invalid! only jpg/jpeg,png,bmp,gif is supported!");
			throw new RenrenException(RenrenError.ERROR_CODE_ILLEGAL_PARAMETER,
					"暂不支持此格式照片，请重新选择", "暂不支持此格式照片，请重新选择");
		}

		// 获取文件内容字节数组
		byte[] content = Util.fileToByteArray(photoRequest.getFile());
		if (content == null) {
			Util.logger("exception in uploading photo: file can't be empty");
			throw new RenrenException(RenrenError.ERROR_CODE_NULL_PARAMETER,
					"上传失败，文件内容为空！", "上传失败，文件内容为空！");
		}

		// 如果photo.Aid为空或者没有指定的相册，都会默认上传到手机相册
		// caption如果为null的话，会将caption的值设为"null"，所以要处理下为空的情况
		if (photoRequest.getCaption() == null) {
			photoRequest.setCaption("");
		}

		// 如果照片描述的字数超过140个字，则抛出RenrenException异常
		if (photoRequest.getCaption().trim().length() > 140) {
			Util.logger("exception in uploading photo: the length of photo caption should no more than 140 words!");
			throw new RenrenException(
					RenrenError.ERROR_CODE_PARAMETER_EXTENDS_LIMIT,
					"照片描述不能超过140个字", "照片描述不能超过140个字");
		}

		String result = null;
		try {
			result = renren.publishPhoto(photoRequest.getAid(), content,
					photoRequest.getFile().getName(),
					photoRequest.getCaption(), Renren.RESPONSE_FORMAT_JSON);
		} catch (RuntimeException e) {
			Util.logger("exception in uploading photo:error in internet requesting\t"
					+ e.getMessage());
			throw new Throwable(e);
		}

		if (result == null) {
			return null;
		}

		// 检查请求返回值是否错误信息
		Util.checkResponse(result, Renren.RESPONSE_FORMAT_JSON);

		// 用请求结果构造返回实体
		PhotoUploadResponseBean photoResponse = new PhotoUploadResponseBean(
				result);

		// 照片已经上传成功
		Log.i(Util.LOG_TAG, "success uploading photo! \n" + photoResponse);

		return photoResponse;
	}

	/**
	 * 异步上传照片
	 * 
	 * @param photoRequest
	 *            调用API传入的请求参数
	 * @param listener
	 *            使用此listener处理返回结果
	 * @return
	 */
	public void asyncUploadPhoto(final PhotoUploadRequestParam photoRequest,
			final AbstractRequestListener<PhotoUploadResponseBean> listener) {
		// 异步方法也不做登录验证
		pool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					PhotoUploadResponseBean photoResponse = uploadPhoto(photoRequest);

					if (photoResponse != null) {
						Util.logger("success uploading photo! \n"
								+ photoResponse);
						if (listener != null) {
							listener.onComplete(photoResponse);
						}
					}
				} catch (RenrenException e) {
					Util.logger("exception in uploading photo: "
							+ e.getMessage());

					if (listener != null) {
						listener.onRenrenError(new RenrenError(
								e.getErrorCode(), e.getMessage(), e
										.getOrgResponse()));
					}
				} catch (Throwable e) {
					Util.logger("fault in uploading photo: " + e.getMessage());

					if (listener != null) {
						listener.onFault(e);
					}
				}
			}

		});
	}

}