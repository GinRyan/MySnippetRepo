package com.kum.im.file;

import java.nio.channels.SocketChannel;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.io.IOException;

/**
 * <p>
 * Title: Socket编程学习
 * </p>
 * 
 * <p>
 * Description: 文件传输发送端
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
public class SendFile {
	public static void main(String[] args) throws IOException {
		System.out.println("***********文件发送端***********");
		SocketAddress address = new InetSocketAddress("192.168.2.166", 9999); // 打开套接字通道，并将其连接到远程
		SocketChannel sc = SocketChannel	.open(address);
		sc.configureBlocking(false);
		Selector selector = Selector.open(); // 打开选择器
		SelectionKey key = sc.register(selector, SelectionKey.OP_READ); // 注册key,为read方式
		new SendThread(sc).start();
		while (key.selector().isOpen()) {
			int celkey = selector.select();
			if (celkey == 0) {
				System.out.println("文件接收端已连接上...");
			}
		}
	}
}
