package com.riderim.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 文件下载类，适用于下载任何类型的小文件 适用于下载URL尾部为文件名的URL和百度音乐
 * 
 * @author 梁正
 * 
 */
public class FileDownloader {

	private String filePathPrefix = "/mnt/sdcard/";

	public FileDownloader() {
	}

	/**
	 * 更改文件保存路径
	 * 
	 * @param defaultSavePath
	 */
	public FileDownloader(String defaultSavePath) {
		filePathPrefix = defaultSavePath;
	}

	/**
	 * 获取当前下载器下载路径，默认值为：/mnt/sdcard/ 。
	 * 
	 * @return 返回路径
	 */
	public String getFilePathPrefix() {
		return filePathPrefix;
	}

	/**
	 * 设置自定义下载器路径
	 * 
	 * @param filePathPrefix
	 */
	public void setFilePathPrefix(String filePathPrefix) {
		this.filePathPrefix = filePathPrefix;
	}

	/**
	 * 文件下载
	 * 
	 * @param url
	 *            网络文件地址URL
	 * @param onFinishedStartDoing
	 *            文件下载结束后回调
	 * @return 存储的文件路径
	 * @throws IOException
	 */
	public String downloadFile(String url,
			OnFinishedDownloadingDoing onFinishedStartDoing) throws IOException {
		return write2SDFile(getFilenameFromUrl(url), getInputStreamFrom(url),
				onFinishedStartDoing);
	}

	/**
	 * 从URL获取文件名，仅限于URL路径结尾是文件名的
	 * 
	 * @param url
	 *            网络URL
	 * @return 返回原始文件名
	 */
	private String getFilenameFromUrl(String url) {
		int lastSaparator = -1;
		int endUrl = -1;
		lastSaparator = url.lastIndexOf("/") + 1;
		if (url.contains("?")) {
			endUrl = url.indexOf("?");
		} else {
			endUrl = url.length();
		}
		String file = url.substring(lastSaparator, endUrl);
		System.out.println(file);
		return file;

	}

	/**
	 * 从URL获取输入流
	 * 
	 * @param urlStr
	 *            下载文件URL
	 * @return 返回输入流对象
	 * @throws IOException
	 */
	private InputStream getInputStreamFrom(String urlStr) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		return conn.getInputStream();
	}

	/**
	 * 
	 * @param filename
	 *            文件名，从URL获取而来
	 * @param is
	 *            输入流
	 * @return 返回文件绝对路径
	 * @throws IOException
	 */
	private String write2SDFile(String filename, InputStream is,
			OnFinishedDownloadingDoing onFinishedDownloadingDoing)
			throws IOException {
		File file = null;
		BufferedOutputStream bufferedOutputs = null;
		BufferedInputStream bis = new BufferedInputStream(is);
		file = new File(filePathPrefix + filename);
		bufferedOutputs = new BufferedOutputStream(new FileOutputStream(file));
		byte buffer[] = new byte[4 * 1024];
		int length = 0;
		while ((length = bis.read(buffer)) != -1) {
			bufferedOutputs.write(buffer, 0, length);
		}
		bufferedOutputs.flush();
		bufferedOutputs.close();
		if (onFinishedDownloadingDoing != null) {
			onFinishedDownloadingDoing.Start(file.getAbsolutePath());
		}
		return file.getAbsolutePath();
	}

	public interface OnFinishedDownloadingDoing {
		public void Start(String filePath);
	}
}
