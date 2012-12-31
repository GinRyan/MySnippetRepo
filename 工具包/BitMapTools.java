package com.cnki.client.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
/**
 * 获取bitmap的工具类 以及保存bitmap到指定路径
 * @author WANGXIAOHONG
 *
 */
public class BitMapTools {
	/**
	 * 根据输入流获取响应的位图对象
	 * @param in
	 * @return
	 */
	public static Bitmap getBitmap(InputStream in){
		return BitmapFactory.decodeStream(in);
	}
	/**
	 * 根据输入流， 缩小比获取位图对象
	 * @param in
	 * @param scale
	 * @return
	 */
	public static Bitmap getBitmap(InputStream in,int scale){
		Bitmap _bitmap = null;
		Options _ops = new Options();
		_ops.inSampleSize = scale;
		_bitmap = BitmapFactory.decodeStream(in, null, _ops);
		return _bitmap;
	}
	/**
	 * 根据指定输入的宽高，保持纵横比，缩小获取位图对象
	 * @param in
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap getBitmap(byte[] bytes,int width,int height){
		Bitmap _bitmap = null;
		Options _ops = new Options();
		_ops.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(bytes, 0, bytes.length,_ops);
		
		_ops.inJustDecodeBounds = false;
		
		int scaleX = _ops.outWidth/width;
		int scaleY = _ops.outHeight/height;
		int scale = scaleX>scaleY?scaleX:scaleY;
		_ops.inSampleSize = scale;
		_bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,_ops);
		return _bitmap;
	}
	/**
	 * 根据指定的文件路径获取位图对象
	 * @param path
	 * @return
	 */
	public static Bitmap getBitMap(String path){
		Bitmap bitmap = null;
		bitmap = BitmapFactory.decodeFile(path);
		return bitmap;
	}
	/**
	 * 将位图保存到指定的路径 
	 * @param path
	 * @param bitmap
	 * @throws IOException 
	 */
	public static void saveBitmap(String path,Bitmap bitmap) throws IOException{
		if(path != null && bitmap != null){
			File _file = new File(path);
			//如果文件夹不存在则创建一个新的文件 
			if(!_file.exists()){
				_file.getParentFile().mkdirs();
				_file.createNewFile();
			}
			//创建输出流
			OutputStream write = new FileOutputStream(_file);
			//获取文件名
			String fileName = _file.getName();
			//取出文件的格式名
			String endName = fileName.substring(fileName.lastIndexOf(".")+1);
			if("png".equalsIgnoreCase(endName)){
				//bitmap的压缩格式
				bitmap.compress(CompressFormat.PNG, 100, write);
			}else {
				bitmap.compress(CompressFormat.JPEG, 100, write);
			}
		}
	}
}
