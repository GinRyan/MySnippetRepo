package com.kum.im.file;

import java.nio.channels.SocketChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.ByteBuffer;

import java.io.RandomAccessFile;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.channels.ClosedChannelException;

/**
 * <p>
 * Title: Socket编程学习
 * </p>
 * 
 * <p>
 * Description: 文件传输接收端
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
public class ReceiveThread extends Thread {
	private FileUtil util = new FileUtil();
	private SocketChannel r_channel = null;
	private ByteBuffer buf = ByteBuffer.allocate(util.getBlockSize() + 9);

	public ReceiveThread(SocketChannel channel) throws Exception {
		this.r_channel = channel;
		this.r_channel.configureBlocking(false);
	}

	public void run() {
		try {
			Selector selector = Selector.open();
			SelectionKey key = r_channel.register(selector,
					SelectionKey.OP_READ);
			buf.clear();
			File file = null;
			FileOutputStream fout = null;// 文件输出流对象
			RandomAccessFile raf = null;// 创建临时文件对象

			int FileSize = 0; // 文件大小
			String FileName = ""; // 文件名称
			int BlockIndex = 0; // 文件块索引号
			int BlockSize = 0; // 文件块大小
			int readlen = 0; // 读到的数据长度
			int buflen = 0; // buf的长度
			byte cmd = 0x0; // 命令码
			int BlockNum = 0; // 文件块数据计数器
			int CompSize = 0; // 已经完成数

			while (key.selector().isOpen()) {
				int selkey = selector.select();
				if (selkey == 0) {
					readlen = this.r_channel.read(buf); // 开始读数据
					buflen += readlen; // 记录缓冲区buf的长度
					if (readlen >= 1) { // 读到了数据
						buf.rewind(); // /数据指针指到0
						cmd = buf.get(); // 读取一个字节,获得文件头信息,判断发送信息类型(head or
											// body)
						switch (cmd) {
						case 0x1: { // /文件信息
							if (buflen >= 260) {
								FileSize = buf.getInt(); // 获取文件大小
								byte[] filename = new byte[255];
								buf.get(filename); // 从缓冲区中读取255个字节，获取文件名
								FileName = (new String(filename)).trim();
								buf.compact(); // 删除读取过的数据
								buflen -= 260; // 缓冲区大小减去已读的260个字节大小
								buf.position(buflen); // 数据指针指向buf的最后位置
								System.out.println("文件名-->：" + FileName
										+ ",文件大小-->：" + FileSize);
								fout = new FileOutputStream(FileName);
								file = new File("~" + FileName + ".tmp"); // 创建临时文件
								raf = new RandomAccessFile(file, "rw"); // 向临时文件中写入数据
								System.out.println("文件创建成功,开始写入数据...");
							} else {
								buf.position(buflen); // /数据指针指向buf的最后位置
							}
						}
							break;
						case 0x2: { // 文件主体数据(body)
							if (buflen >= 9) {
								BlockIndex = buf.getInt(); // 获得文件块索引号
								BlockSize = buf.getInt(); // 获得文件块大小
								if (buflen >= (9 + BlockSize)) { // 缓冲区大小减去已读数据
									byte[] blockdb = new byte[BlockSize];
									buf.get(blockdb); // 读取文件块数据
									buf.compact(); // 删除已读数据
									buflen -= (9 + BlockSize);
									buf.position(buflen); // 数据指针指向buf缓冲区最后位置
									if (BlockSize < util.getBlockSize()) {
										// 如果收到的数据块大小小于定义的最大文件块大小
										byte[] tmpdb = new byte[util
												.getBlockSize()]; // /定义临时最大数据块
										System.arraycopy(blockdb, 0, tmpdb, 0,
												BlockSize);//
										raf.seek((util.getBlockSize() + 4)
												* BlockIndex); // (+4)是用于存放文件大小信息的
										// 文件块写入临时文件中将相应数据块放到相应的位置中去
										raf.writeInt(BlockSize); // 向临时数据块中写入文件大小信息
										raf.write(tmpdb); // 向临时数据块中写入文件块信息
										BlockNum++; // 文件块计数器累加
									} else {
										// 收到的数据等于文件块定义的最大值
										raf.seek((util.getBlockSize() + 4)
												* BlockIndex);
										// 文件块写入临时文件中将相应数据块放到相应的位置中去
										raf.writeInt(BlockSize);
										raf.write(blockdb);
										BlockNum++;// 文件块计数器累加
									}
									System.out.println("写入临时文件完成： ---->"
											+ (CompSize += BlockSize) + "****");
									FileSize -= BlockSize; // /每收到一块，就从总文件大小里减去
									if (FileSize == 0) { // /如果减到0了，说明文件块已经收全了，可以关闭文件了。
										for (int i = 0; i < BlockNum; i++) {
											// 根据文件块个数循环将临时文件写入正式文件
											raf.seek((util.getBlockSize() + 4)
													* i);
											BlockSize = raf.readInt();// 从临时文件中读取文件块大小
											byte[] tmpdb = new byte[BlockSize];
											raf.read(tmpdb);// 从临时文件中读取文件块数据
											fout.write(tmpdb);// 向正式文件中写入文件块
										}
										raf.close();// 释放资源
										file.delete(); // /删除临时文件
										fout.close();
										System.out
												.println("*******文件已经接收完成了！*******");
									}
								} else {
									buf.position(buflen); // /数据指针指向buf的最后位置
								}
							} else {
								buf.position(buflen); // /数据指针指向buf的最后位置
							}
						}
							break;
						}
					}
				}
			}
		} catch (ClosedChannelException ex) {
			System.out.println("发生ClosedChannelException异常,原因 ----> ："
					+ ex.getMessage());
		} catch (IOException ex) {
			System.out.println("打开选择器时异常，原因 ----> ：" + ex.getMessage());
		}

	}

}
