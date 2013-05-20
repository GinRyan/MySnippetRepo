package com.cnki.client.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class ImageLoaderTools {
	
	private HttpTools httptool;
	private Context mContext;
	//线程轮询的控制变量
	private boolean isLoop = true;
	//图片缓存集合
	private HashMap<String, SoftReference<Bitmap>> mHashMap_caches;
	//下载任务队列
	private ArrayList<ImageLoadTask> maArrayList_taskQueue;
	//用于回调callback中的方法，更新界面
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			ImageLoadTask _task = (ImageLoadTask) msg.obj;
			Log.i("info", ""+_task);
			//调用callback中的imageloaded方法，通知界面更新数据
			_task.callback.imageloaded(_task.path, _task.bitmap);
		};
	};
	//创建工作线程，用于轮询任务队列从而下载图片
	private Thread mThread = new Thread(){
		public void run() {
			while(isLoop){
				//当任务队列中有任务时开始执行下载
				while(maArrayList_taskQueue.size() >0){
					try {
						//从任务队列中移除任务时会返回任务对象即得到任务下载对象
						ImageLoadTask task = maArrayList_taskQueue.remove(0);
						
						//如果下载的是小图标，就将下载的图片按一定的比例进行相应的缩小			
						if(Constant.LOADPICTYPE == 1) {	
							//开始下载，获取图片文件的字节数组
							byte[] bytes = httptool.getByte(task.path, null, HttpTools.METHOD_GET);
							//获取固定大小的图片，即是经过缩放后的图片，纵横比不变,宽高为40，用来放置图书图片
							task.bitmap = BitMapTools.getBitmap(bytes, 40, 40);
						//获取图片的原始大小
						} else if(Constant.LOADPICTYPE == 2) {
							//开始下载，获取图片文件的字节数组
							InputStream in = httptool.getStream(task.path, null, HttpTools.METHOD_GET);							
							//获取固定大小的图片，即是经过缩放后的图片，纵横比不变,宽高为40，用来放置图书图片
							task.bitmap = BitMapTools.getBitmap(in, 1);
							//Log.i("info", "task.bitmap============"+task.bitmap);
							
						}
						
						
						//如果图片下载成功就向内存缓存和文件中放置缓存信息，以便之后从双缓存中读取图片信息
						if(task.bitmap != null){
							//向集合缓存中添加缓存
							mHashMap_caches.put(task.path,new SoftReference<Bitmap>(task.bitmap) );
							//向文件中添加缓存信息
							//获取文件存储路径
							File dir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
							//如果文件路径不存在 ,则创建该路径
							if(!dir.exists()){
								dir.mkdirs();
							}
							//创建图片存储路径
							File file = new File(dir, task.path);
							//向该文件路径中存储图片
							BitMapTools.saveBitmap(file.getAbsolutePath(), task.bitmap);
							//下载完成后发送消息回主线程
							Message msg = Message.obtain();
							msg.obj = task;
							mHandler.sendMessage(msg);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//当前线程处于等待状态
					synchronized (this) {
						try {
							wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		};
	};
	
	//构造方法，
	public ImageLoaderTools(Context context){
		this.mContext = context;
		httptool = new HttpTools(context);
		mHashMap_caches = new HashMap<String, SoftReference<Bitmap>>();
		maArrayList_taskQueue = new ArrayList<ImageLoaderTools.ImageLoadTask>();
		mThread.start();
	}
	/**
	 * 图片下载任务类
	 * @author 3gtarena
	 *
	 */
	private class ImageLoadTask{
		String path;
		Bitmap bitmap;
		Callback callback;
	}
	/**
	 * 回调接口。在调用loadimage方法时，需要传入回调接口的实现类对象
	 * @author 3gtarena
	 *
	 */
	public interface Callback{
		void imageloaded(String path,Bitmap bitmap);
	}
	//结束工作线程，终止线程轮询
	public void quit(){
		isLoop = false;
	}
	/**
	 * 利用图片路径下载图片 利用内存缓存和文件缓存双缓存机制优化下载
	 * @param path
	 * @param callback
	 * @return
	 */
	public Bitmap imageLoad(String path,Callback callback){
		Bitmap bitmap = null;
		//如果内存缓存中存在该路径，则从内存中直接获取该图片
		if(mHashMap_caches.containsKey(path)){
			bitmap = mHashMap_caches.get(path).get();
			//如果缓存中的图片已经被释放，则从该缓存中移除图片路径
			if(bitmap == null){
				mHashMap_caches.remove(path);
			}else {
				return bitmap;
			}
		}
		
		//如果缓存中未得到缓存需要的图片，则从文件中读取该图片
		//获取本文件的图片类的文件存储路径
		File dir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
		//Log.i("info", "dir=============="+dir);
		//创建要获取的图片路径
		File file = new File(dir, path);
		//从文件中读取指定路径的图片
		bitmap = BitMapTools.getBitMap(file.getAbsolutePath()); 
		//如果文件中存在该图片，则直接从文件中获取图片
		if(bitmap != null){
			return bitmap;
		}
		
		//如果两个缓存中都未获取图片，则直接从服务器端下载图片
		//创建下载人任务
		ImageLoadTask task = new ImageLoadTask();
		//设置下载路径
		task.path = path;
		//设置下载任务的callback对象
		task.callback = callback;
		//将下载任务添加到任务队列 进入任务轮询状态
		maArrayList_taskQueue.add(task);
		//唤醒线程，开始下载
		synchronized (mThread) {
			mThread.notify();
		}
		return null;
	}
	
}
