package com.kum.im.file;

import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/**
 * <p>
 * Title: Socket编程学习
 * </p>
 * 
 * <p>
 * Description: 文件接收端
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * 
 * <p>
 * Company: <a href="www.kum.net.cn">酷猫科技</a>
 * </p>
 * 
 * @author 贺翔<380595305@qq.com>
 * @version 1.0
 */
public class ReceiveFile {
	public static void main(String[] args) {
		try {
			System.out.println("***********文件接收端***********");
			SocketAddress saddress = new InetSocketAddress(9999);
			ServerSocketChannel ssc = ServerSocketChannel.open();
			
			ssc.socket().bind(saddress);
			ssc.configureBlocking(false);
			Selector selector = Selector.open();
			SelectionKey selkey = ssc
					.register(selector, SelectionKey.OP_ACCEPT);
			while (selkey.selector().isOpen()) {
				int key = selector.select();
				if (key == 0) {
					(new ReceiveThread(ssc.accept())).start();
					System.out.println("文件发送端已连接上...");
				}
			}
		} catch (Exception err) {
			System.out.println("等待文件发送端连接时异常，原因：" + err.getMessage());
		}
	}
}
