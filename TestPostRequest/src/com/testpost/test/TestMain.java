package com.testpost.test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.testpost.httpclient.FormFile;
import com.testpost.httpclient.SocketHttpRequester;

public class TestMain {

	/**
	 * @param args
	 */
	public static String urlrequest = "http://www.exiug.com:9000/exiugwap/createWeibo.do";

	public static void main(String[] args) {
		File file = new File("D:\\cy.mp3");
		file.getTotalSpace();
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", "yyyyyy");
		params.put("content", "是好用的");
		params.put("city", "你猜猜");
		try {
			FormFile formfile = new FormFile("de.png", file, "Image",
					FormFile.MULTIPART_FORMDATA);
			String isSuc = SocketHttpRequester.post(urlrequest, params,
					formfile);
			System.out.println("返回：" + isSuc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
