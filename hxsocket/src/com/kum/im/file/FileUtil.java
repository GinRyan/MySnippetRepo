package com.kum.im.file;

import java.nio.ByteBuffer;

/**
 * <p>
 * Title: Socket编程学习
 * </p>
 * 
 * <p>
 * Description: 文件传输协议类
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

public class FileUtil {

	private byte FileInfo = 0x1; // /文件信息上传命令
	private byte FileDB = 0x2; // /文件数据传输命令
	private int BlockSize = 512; // /规定文件块大小为512

	public byte[] getFileInfoPack(String FileName, int FileSize) {
		ByteBuffer buf = ByteBuffer.allocate(260);
		byte[] infopack = new byte[260];
		byte[] filename = new byte[255];

		System.arraycopy(FileName.getBytes(), 0, filename, 0, FileName.length());
		buf.clear();
		buf.put(FileInfo);
		buf.putInt(FileSize);
		buf.put(filename);
		buf.flip();
		buf.get(infopack);
		buf.compact();
		return infopack;
	}

	public byte[] getFileDB(int index, int blocksize, byte[] data) {
		byte[] filedb = new byte[9 + blocksize];
		ByteBuffer buf = ByteBuffer.allocate(9 + blocksize);
		buf.clear();
		buf.put(this.FileDB);
		buf.putInt(index);
		buf.putInt(blocksize);
		buf.put(data, 0, blocksize);
		buf.flip();
		buf.get(filedb);
		buf.compact();
		return filedb;
	}

	public int getBlockSize() {
		return this.BlockSize;
	}

}
