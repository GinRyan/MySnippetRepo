package com.ljp.laucher.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Environment;
import android.widget.ProgressBar;

public class FileOperation {

	public static boolean deleteFile(File file) {
		try {
			File[] fs = file.listFiles();
			if (fs != null) {
				for (File f : fs) {
					if (f.isDirectory()) {
						deleteFile(f);
						f.delete();
					} else {
						f.delete();
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	public static boolean loadFile(String url,ProgressBar pb) {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse response;
		try {
			response = client.execute(get);
			HttpEntity entity = response.getEntity();
			float length = entity.getContentLength();
			InputStream is = entity.getContent();
			FileOutputStream fileOutputStream = null;
			if (is != null) {
				File entryDir = new File(Environment.getExternalStorageDirectory()+ "/night_girls/vip/");
				if (!entryDir.exists()) {
					entryDir.mkdirs();
				}
				File file = new File(Environment.getExternalStorageDirectory()
						+ "/night_girls/vip/"+url.substring(url.lastIndexOf("/")+1,url.length()));
				fileOutputStream = new FileOutputStream(file);
				byte[] buf = new byte[1024];
				int ch = -1;
				float count = 0;
				while ((ch = is.read(buf)) != -1) {
					fileOutputStream.write(buf, 0, ch);
					count += ch;
					pb.setProgress((int) (count * 100 / length));
				}
			}
			fileOutputStream.flush();
			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	public static void Unzip(String zipFile, String toDir,String direName) {
		int BUFFER = 4096; // 这里缓冲区我们使用4KB，
		String strEntry; // 保存每个zip的条目名称

		try {
			BufferedOutputStream dest = null; // 缓冲输出流
			FileInputStream fis = new FileInputStream(zipFile);
			ZipInputStream zis = new ZipInputStream(
					new BufferedInputStream(fis));
			ZipEntry entry; // 每个zip条目的实例

			while ((entry = zis.getNextEntry()) != null) {

				try {
					int count;
					byte data[] = new byte[BUFFER];
					strEntry = entry.getName();

					File entryFile = new File(toDir + strEntry);
					File entryDir = new File(toDir+direName);
					if (!entryDir.exists()) {
						entryDir.mkdirs();
					}

					FileOutputStream fos = new FileOutputStream(entryFile);
					dest = new BufferedOutputStream(fos, BUFFER);
					while ((count = zis.read(data, 0, BUFFER)) != -1) {
						dest.write(data, 0, count);
					}
					dest.flush();
					dest.close();
				} catch (Exception ex) {
					// ex.printStackTrace();
				}
			}
			zis.close();
		} catch (Exception cwj) {
			// cwj.printStackTrace();
		}
		File f = new File(zipFile);
		f.delete();
	}

}
