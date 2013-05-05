/**
 * 文件名：LrcAnalyst.java
 * 环境： GNU/Linux Ubuntu 7.04 + Eclipse 3.2 + JDK 1.6
 * 功能：解析Lrc歌词文件，这只是一个在CLI下的Demo
 * 版本：0.0.1.0
 * 作者：88250
 * 日期：2007.4.22
 * E-mail & MDN: DL88250@gmail.com
 * QQ：845765
 */

import java.io.*;
import java.util.*;

/**
 * 歌词解释类，用于对lrc歌词文件的解析
 */
public class LrcAnalystCLI {
	/**
	 * lrc文件缓冲区
	 */
	private static Vector lyrics;

	/**
	 * 格式化好的歌词时间与索引，格式参考TimeAndIndex类
	 */
	private static Vector lrcTimeValAndIndex;

	/**
	 * 歌词的实际内容
	 */
	private static Vector lyrcisContent;

	/**
	 * 时间标签的长度，用于区分时间标签的格式
	 * 
	 * @see private float computeTimeTag(String timeStr)方法
	 */
	private int timeTagLength;

	/**
	 * 真正的歌词内容开始到lrc文本开头的偏移量
	 */
	private int realLyrcisStartOffset;

	/**
	 * 返回真正的歌词内容开始到lrc文本开头的偏移量
	 * 
	 * @return 偏移量
	 */
	public int getRealLrcStartOffset() {
		return realLyrcisStartOffset;
	}

	/**
	 * 是否确定了歌词标签的格式，确定的话是<code>true</code>, 否则是<code>false</code>
	 * 
	 * @see private float computeTimeTag(String timeStr)方法
	 */
	private boolean isConfirmTimeTagLeng;

	/**
	 * 歌词解析器的默认构造器 <br>
	 * 初始化歌词文件缓冲等字段
	 */
	public LrcAnalystCLI() {
		lyrics = new Vector();
		lrcTimeValAndIndex = new Vector();
		lyrcisContent = new Vector();
		timeTagLength = 0;
		isConfirmTimeTagLeng = false;
		realLyrcisStartOffset = 0;
	}

	/**
	 * 按行对去读取文本文件内容
	 * 
	 * @param fileName
	 *            文件路径与文件名
	 * @throws IOException
	 */
	public void readFile(String fileName) {
		try {
			InputStream r = new FileInputStream(fileName);
			ByteArrayOutputStream byteout = new ByteArrayOutputStream();
			byte tmp[] = new byte[1024];
			byte context[];

			r.read(tmp);
			byteout.write(tmp);
			context = byteout.toByteArray();
			String str = new String(context, "GBK");
			String lyrcisText[] = str.split("\n");

			for (int i = 0; i < lyrcisText.length; i++) {
				lyrics.add(lyrcisText);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 显示读取lrc里的所有内容到控制台
	 */
	public void displayLrcContent() {
		for (int i = 0; i < lyrics.size(); i++) {
			System.out.println(lyrics.get(i));
		}
	}

	/**
	 * 从文件缓冲lyrics Vector里进行歌词文件内容的解析，并将解析结果保存 <br>
	 * 解析方法： <br>
	 * Step0. 用']'分割一行内容到String[] Step1. 查看每一行在第3个char是否是':' <br>
	 * Step2. 查看每一行在第6个char是否是'.' <br>
	 * Step3. 确定为歌词行，确定该lrc用的格式 <br>
	 * <b>注意：目前，时间标签的格式有两种，分别是长度为8和5的</b> <br>
	 * Step4. 循环直到歌词缓冲结束 <br>
	 * <b>注意：这个算法可能存在严重缺陷<b>
	 * 
	 * @see 关于时间标签的格式，参考TimeTag类
	 */
	public void parseLyrics() {
		for (int i = 0; i < lyrics.size(); i++) {
			String aLineLyrics = (String) lyrics.get(i);
			String[] partsLrc = aLineLyrics.split("]");

			char flag1 = 0;
			char flag2 = 0;
			if (!aLineLyrics.isEmpty()) {// 很多制作lrc文件的人不负责，乱搞格式!!!!
				flag1 = aLineLyrics.charAt(1);
				flag2 = aLineLyrics.charAt(3);
			}

			if ((aLineLyrics.length() > 3) && (flag1 >= 48 && flag1 <= 57)
					&& (':' == flag2)) {// 确定为歌词内容行
				if (!isConfirmTimeTagLeng && aLineLyrics.length() > 9) {
					if (aLineLyrics.charAt(9) == ']') {// 长度为8的时间标签格式
						timeTagLength = 8;
					} else if (aLineLyrics.charAt(6) == ']') {// 长度为5的时间标签格式
						timeTagLength = 5;
					}
					realLyrcisStartOffset = i;
					isConfirmTimeTagLeng = true;
				}

				for (int j = 0; j < partsLrc.length; j++) {
					if (partsLrc[j].charAt(0) == '[') {// 确定为时间标签部分，而不是歌词内容部分
						String timeTagStr = partsLrc[j].substring(1,
								timeTagLength + 1);
						float timeValue = computeTimeTag(timeTagStr);
						// 添加歌词时间于对应的歌词索引
						lrcTimeValAndIndex
								.add(new LrcTimeAndIndexNum(timeValue, i));
					} else // 如果歌词开头的内容也是'['将出现FATAL ERROR
					{// 确定为歌词内容部分
						lyrcisContent.add(partsLrc[j]);
					}
				}
			}
		}
		sortByTimeTag();
	}

	/**
	 * @param timeStr
	 *            表示歌词显示时间的String <br>
	 *            例如：<br>
	 *            "02:03.30"表示2*60s+3.3s=123.3s=1233ms的时刻 <br>
	 *            <b>注意：考虑到歌曲长度不会很长，所以算法中直接取前两位作为分钟， <br>
	 *            而从'：'后的单位是秒。但是应为现在lrc文件格式不是很统一，例如： <br>
	 *            "02:03"表示2*60s+3s=123s=1230ms，这是短格式表示，也要考虑到。 <br>
	 *            目前，将时间标签分为的两种格式，长度分别为8，5
	 * @return 计算好的时间值
	 */
	private float computeTimeTag(String timeStr) {
		int minutes = Integer.parseInt(timeStr.substring(0, 2)); // 取得分钟
		float seconds = 0;

		if (timeStr.length() == 8) {
			seconds = Float.parseFloat(timeStr.substring(3, 8)); // 取得秒钟
		} else if (timeStr.length() == 5) {
			seconds = Float.parseFloat(timeStr.substring(3, 5));
		}

		return (minutes * 60 + seconds); // 返回时间值
	}

	/**
	 * 更具歌词显示的先后时间顺序进行排序的标准
	 */
	private void sortByTimeTag() {
		Collections.sort(lrcTimeValAndIndex, new Comparator() {
			// Parameters:
			// o1 - the first object to be compared.
			// o2 - the second object to be compared.
			// Returns:
			// a negative integer, zero, or a positive integer as the first
			// argument is less than, equal to, or greater than the second.
			// Throws:
			// ClassCastException - if the arguments' types prevent them from
			// being compared by this Comparator.
			public int compare(Object o1, Object o2) {
				LrcTimeAndIndexNum fl1 = (LrcTimeAndIndexNum) o1;
				LrcTimeAndIndexNum fl2 = (LrcTimeAndIndexNum) o2;

				if (fl1.getLrcTime() - fl2.getLrcTime() > 0) {
					return 1;
				} else if (fl1.getLrcTime() - fl2.getLrcTime() > 0) {
					return -1;
				} else {
					return 0;
				}
			}

		});
	}

	public void dispalyLrc() {

	}

	/**
	 * 主程序入口点
	 * 
	 * @param args
	 *            命令行参数，这里没用到，为<code>null</code>
	 */
	public static void main(String[] args) {
		LrcAnalystCLI lrcAnalyst = new LrcAnalystCLI();
		lrcAnalyst.readFile("灰色的心.lrc");

		lrcAnalyst.parseLyrics();

		Date startTime = new Date();

		for (int i = 0; i < lrcTimeValAndIndex.size(); i++) {
			while (true) {
				try {
					Thread.currentThread().sleep(500);
					Date currentTime = new Date();
					LrcTimeAndIndexNum fl = (LrcTimeAndIndexNum) (lrcTimeValAndIndex
							.get(i));
					float diffTime = currentTime.getTime()
							- startTime.getTime();

					if (fl.getLrcTime() - (float) diffTime / 1000 < 0.0) {
						System.out.println((String) lyrcisContent.get(fl
								.getLrcIndex()
								- lrcAnalyst.getRealLrcStartOffset()));
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}

/**
 * @author daniel
 */
class LrcTimeAndIndex {
	/**
	 * 该行歌词的显示时间
	 */
	private float lrcTime;

	/**
	 * 该行歌词行索引
	 */
	private int lrcIndex;

	/**
	 * 默认的构造器
	 */
	public LrcTimeAndIndex() {

	}

	/**
	 * 带参数的构造器，设置时间标签和对应的歌词行索引
	 * 
	 * @param lrcTime
	 *            时间标签
	 * @param lrcIndex
	 *            歌词行索引
	 */
	public LrcTimeAndIndex(float lrcTime, int lrcIndex) {
		this.lrcTime = lrcTime;
		this.lrcIndex = lrcIndex;
	}

	/**
	 * 返回歌词应该出现的时间
	 * 
	 * @return lrcTime 出现时间
	 * @uml.property name="lrcTime"
	 */
	public float getLrcTime() {
		return lrcTime;
	}

	/**
	 * 返回歌词内容行索引
	 * 
	 * @return 歌词内容行索引
	 * @uml.property name="lrcIndex"
	 */
	public int getLrcIndex() {
		return lrcIndex;
	}
}


