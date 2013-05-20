package com.kum.im.file;

/**
 * <p>Title: Socket编程学习</p>
 *
 * <p>Description: 文件传输发送端</p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: <a href="www.kum.net.cn">酷猫科技</a></p>
 *
 * @author 贺翔<380595305@qq.com>
 * @version 1.0
 */

import java.nio.channels.SocketChannel;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.nio.ByteBuffer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SendThread extends Thread {
	private FileUtil util = new FileUtil();

	private SocketChannel s_channel = null;
	private int down_count = 1; // 记录文件被接收的次数
	private InputStream is = null;
	private InputStreamReader isr = null;
	private BufferedReader br = null;

	private ByteBuffer buf = ByteBuffer.allocate(1024);

	public SendThread(SocketChannel channel) {
		this.s_channel = channel;
	}

	public void run() {
		String ch = "";
		do {
			try {
				System.out.print("****请选择需要发送的文件："); // 选择进行传输的文件
				is = System.in; // 获得输入流对象
				isr = new InputStreamReader(is);
				br = new BufferedReader(isr);
				String filePath = br.readLine(); // 控制台读取目标文件路径
				if (filePath.equals("exit")) {
					break;
				}
				int index = 0; // 文件块索引
				int size = 0; // 文件块大小
				File file = new File(filePath); // 创建文件对象
				FileInputStream filestream = new FileInputStream(file); // 包装一个文件输出流
				byte[] filedb = new byte[util.getBlockSize()]; // 定义一个文件块(字节数组)
				System.out.println("文件名是: ---->" + filePath);
				byte[] fileinfo = util.getFileInfoPack(file.getName(),
						(int) file.length());
				buf.clear(); // 有待研究***********************************
				buf.put(fileinfo); // 将文件块添加到ByteBuffer中
				buf.flip(); // 将极限设置为当前位置，位置设置为0
				size = buf.limit(); // 获得极限值
				while ((size -= this.s_channel.write(buf)) > 0) {
				}
				buf.compact();
				System.out.println("文件发送完毕 O(∩_∩)O哈哈~");
				while ((size = filestream.read(filedb)) != -1) {
					byte[] filedatabase = util.getFileDB(index, size, filedb);
					buf.clear(); // 有待研究*************************************
					buf.put(filedatabase);
					buf.flip(); // 有待研究***************************************
					while ((size -= this.s_channel.write(buf)) > 0) {
					}
					buf.compact(); // 有待研究**************************************
					index++;
				}
				System.out.println("文件传输完成，共有" + (down_count++)
						+ "个客户端(文件接收端)接收了服务端发送的文件!");
				filestream.close();
				System.out.println("****发送文件发送成功!****");
			} catch (Exception err) {
				System.out.println("发送文件时异常，原因：  ---->" + err.getMessage());
			}
			// ////////////////////////提示是否继续操作////////////////////////////
			System.out.println("是否继续传输：(y or n)");
			is = System.in;
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);
			try {
				ch = br.readLine();
				if (!("y".equalsIgnoreCase(ch))) {
					System.out.println("您选择了退出程序!");
					System.exit(1);// 程序退出
				}
			} catch (IOException ex1) {
				System.out.println("读取字符参数时异常，原因：" + ex1.getMessage());
			}
		} while ("y".equalsIgnoreCase(ch));
	}
}
