
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

/**
 * 
 * 拾取系统照片和相机捕获
 * 
 * @author Liang
 * 
 */
public class PickImageWithCropHelper {
	protected AlertDialog.Builder dialog;// 对话框
	protected Activity context;// 上下文对象
	public static File mCurrentFilePath = new File(Environment.getExternalStorageDirectory() + "/tempCapture.jpg");// 当前写入的文件路径
	public static File mCropedFilePath = new File(Environment.getExternalStorageDirectory() + "/tempCroped.jpg");// 当前已剪裁的文件路径

	public static final int CODE_PICK_IMAGE = 101;// 从相册里选取
	public static final int CODE_CAPTURE = 100;// 拍摄照片
	public static final int CODE_PHOTO_CROP = 111;// 照片剪裁
	public static Bitmap mBitmapResult;// 获取到的位图对象

	public boolean allowCrop = false;// 允许裁剪

	/**
	 * 选择相册图片还是拍摄照片。
	 * 
	 * @param context
	 *            传入上下文
	 * @param allowCrop
	 *            是否允许裁剪
	 */
	public PickImageWithCropHelper(Activity context, boolean allowCrop) {
		if (context != null) {
			this.context = context;
			this.allowCrop = allowCrop;
			dialog = new AlertDialog.Builder(context);
		} else {
			throw new NullPointerException("必须传入有效Activity");
		}
	}

	/**
	 * 弹出对话框
	 */
	public void createDialog() {
		String[] items = { "拍照", "相册选取" };
		dialog.setTitle("选择照片").setItems(items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:// 照相获取
					Intent takephoto = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					takephoto.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentFilePath));
					context.startActivityForResult(takephoto, CODE_CAPTURE);
					break;
				case 1:// 从相册获取
					Intent album = new Intent(Intent.ACTION_GET_CONTENT);
					album.setType("image/*");
					context.startActivityForResult(album, CODE_PICK_IMAGE);
					context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
					break;
				}
			}
		});
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * 取回位图数据并处理，这里需要放在onActivityResult()回调方法中
	 * 
	 * @param requestCode
	 *            这里直接传入回调方法的参数requestCode
	 * @param resultCode
	 *            这里直接传入回调方法的参数resultCode
	 * @param data
	 *            这里直接传入回调方法的参数resultCode
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public Bitmap bitmapHandler(int requestCode, int resultCode, Intent data) throws FileNotFoundException, IOException {
		ContentResolver resolver = context.getContentResolver();
		if (resultCode != Activity.RESULT_OK) {// 返回结果不为OK
			return null;
		} else if (resultCode == Activity.RESULT_OK && requestCode == CODE_PICK_IMAGE) {// 获取相册照片返回结果为OK时
			Uri uri = data.getData();
			if (uri != null) {
				mBitmapResult = MediaStore.Images.Media.getBitmap(resolver, uri);
				// 处理缩小图片，需要写图片压缩器
				BitmapHandler.writeToFile(mCurrentFilePath, BitmapHandler.bitmapToBytes(mBitmapResult));
				mBitmapResult.recycle();
				mBitmapResult = BitmapHandler.startDecodeBitmapByPath(mCurrentFilePath.getAbsolutePath(), 800, 600);
				BitmapHandler.writeToFile(mCurrentFilePath, BitmapHandler.bitmapToBytes(mBitmapResult));
				if (allowCrop) {
					startPhotoZoom(uri);
				}
				return mBitmapResult;
			}
		} else if (resultCode == Activity.RESULT_OK && requestCode == CODE_CAPTURE) {// 照相获取[系统相机已经写入到文件中]
			//
			mBitmapResult = BitmapHandler.startDecodeBitmapByPath(mCurrentFilePath.getAbsolutePath(), 800, 600);
			BitmapHandler.writeToFile(mCurrentFilePath, BitmapHandler.bitmapToBytes(mBitmapResult));
			if (allowCrop) {
				startPhotoZoom(Uri.fromFile(mCurrentFilePath));
			}
			return mBitmapResult;
		} else if (resultCode == Activity.RESULT_OK && requestCode == CODE_PHOTO_CROP) {
			getCropBitmap(data);
		}
		return mBitmapResult;
	}

	/**
	 * 获取已经裁剪的位图
	 * 
	 * @param data 获取数据
	 * @throws IOException
	 */
	public static void getCropBitmap(Intent data) throws IOException {
		// 裁剪
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			// (0 - 100)压缩文件
			// imageView.setImageBitmap(photo);
			mBitmapResult = photo;
			byte[] bmArrbyte;
			bmArrbyte = BitmapHandler.bitmapToBytes(mBitmapResult);
			FileOutputStream outputStream = new FileOutputStream(mCropedFilePath);
			outputStream.write(bmArrbyte);
			outputStream.flush();
			outputStream.close();
		}
	}

	/**
	 * 缩放图片处理入口
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true"); // aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1); // outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 220);
		intent.putExtra("outputY", 220);
		intent.putExtra("return-data", true);
		context.startActivityForResult(intent, CODE_PHOTO_CROP);
	}

	/**
	 * 图片处理器
	 * 
	 * @author Liang
	 * 
	 */
	public static class BitmapHandler {
		Bitmap bitmap;// 图片对象
		static BitmapFactory.Options opts = new Options();// 图片配置

		int originalWidth;// 原始宽度
		int originalHeight;// 原始高度
		int inSampleSize;// 缩放分母

		public static Bitmap startDecodeBitmapByPath(String pathName, int finalWidth, int finalHeight) {
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(pathName, opts);
			// 计算 inSampleSize
			opts.inSampleSize = calculateInSampleSize(opts, finalWidth, finalHeight);
			// 用 inSampleSize 设置解码位图对象
			opts.inJustDecodeBounds = false;
			return BitmapFactory.decodeFile(pathName, opts);
		}

		public static Bitmap startDecodeBitmapByInputStream(InputStream is, int finalWidth, int finalHeight) {
			opts.inJustDecodeBounds = true;
			Rect rect = new Rect(-1, -1, -1, -1);
			BitmapFactory.decodeStream(is, rect, opts);
			// 计算 inSampleSize
			opts.inSampleSize = calculateInSampleSize(opts, finalWidth, finalHeight);
			// 用 inSampleSize 设置解码位图对象
			opts.inJustDecodeBounds = false;
			return BitmapFactory.decodeStream(is, rect, opts);
		}

		public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
			// 原始图像的宽和高
			final int height = options.outHeight;
			final int width = options.outWidth;
			int inSampleSize = 1;

			if (height > reqHeight || width > reqWidth) {
				// 计算原始高度和请求高度的比例，计算原始宽度和请求宽度的比例
				final int heightRatio = Math.round((float) height / (float) reqHeight);
				final int widthRatio = Math.round((float) width / (float) reqWidth);
				// 选择一个最小的比例值作为 inSampleSize 值, 这样能够保证
				// 保证最终的图像的尺寸大于或等于所要求的高度和宽度.
				inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
			}
			return inSampleSize;
		}

		/**
		 * 用于上传图片之前，将图片转换为字节流
		 * 
		 * @param bitmap
		 *            图片
		 * @return 转换的字节流对象
		 */
		public static byte[] bitmapToBytes(Bitmap bitmap) {
			if (bitmap == null) {
				return null;
			}
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
			// 将Bitmap压缩成JPG编码，质量为95%存储
			bitmap.compress(Bitmap.CompressFormat.JPEG, 95, os);// 除了PNG还有很多常见格式，如jpeg等。
			return os.toByteArray();
		}

		/**
		 * 把bytes一次性写入file并关闭
		 * 
		 * @param file
		 * @param bytes
		 * @throws IOException
		 */
		public static void writeToFile(File file, byte[] bytes) throws IOException {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(bytes);
			fos.flush();
			fos.close();
		}
	}
}
