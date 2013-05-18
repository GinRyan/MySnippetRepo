/**
 * 文件名：LrcScrollFrame.java
 * 环境： GNU/Linux Ubuntu 7.04 + Eclipse 3.2 + JDK 1.6
 * 功能：滚动的Lrc歌词显示面板
 * 版本：0.0.3.0
 * 版本改动：
 *     1. 简化了LivaPlayer 0.0.2.0版本中关于歌词显示的代码
 *     2. 修复了开始不能显示歌词的Bug
 * 作者：88250
 * 日期：2007.4.28
 * E-mail & MDN: DL88250@gmail.com
 * QQ：845765
 *
 * Copyright (C) 2007 88250 <DL88250@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.Date;

public class LrcScrollFrame extends JFrame {
	/**
	 * 歌词滚动面板构造器
	 * 
	 * @param lrcFileName
	 *            歌词文件路径和文件名
	 */
	public LrcScrollFrame(String lrcFileName) {
		super(lrcFileName);
		ScrollPane textContentPane = new ScrollPane(lrcFileName);
		setBounds(600, 300, 370, 400);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel pane = new JPanel();
		pane.setLayout(new GridLayout(1, 1, 0, 0));
		pane.add(textContentPane);
		setContentPane(pane);
		setVisible(true);
	}

}

/**
 * 歌词显示面板
 */
class ScrollPane extends JPanel implements Runnable {

	/**
	 * 参考纵坐标，控制动画效果
	 */
	private static int y;

	private static long sleepTime = 0;
	private long startTime = 0;

	/**
	 * @see src.cn.edu.ynu.sei.livaPlayer.lrcAnalyst.LrcAnalystBase
	 */
	private LrcAnalystBase lrcAnalystBase = new LrcAnalystBase();

	/**
	 * 默认的构造器，创建一个新的线程并直接开启它
	 * 
	 * @param lrcFileName
	 *            歌词文件路径和文件名
	 * @see src.cn.edu.ynu.sei.livaPlayer.lrcAnalyst.ScrollPane.run()
	 * 
	 */
	public ScrollPane(String lrcFileName) {
		// 读歌词文件
		lrcAnalystBase.readFile(lrcFileName);
		// 解析歌词文件
		lrcAnalystBase.parseLyrics();

		Date startDate = new Date();
		startTime = startDate.getTime();

		new Thread(this).start();
	}

	/**
	 * 在线程的run里面，它每次都会使y改变，然后调用repaint()方法，此方法会调用paint再画一次
	 * 再画一次的时候，里面的y坐标是每次都会变的，这样就形成了一种动画效果
	 * 
	 * @see src.cn.edu.ynu.sei.livaPlayer.lrcAnalyst.LrcTimeAndIndex<br>
	 *      这个类表述了如何让歌词显示时间和歌词内容进行对应
	 * 
	 */
	public void run() {
		while (true) {
			y--;

			long currentTime = new Date().getTime();
			LrcTimeAndIndexNum lrcTimeAndIndex = new LrcTimeAndIndexNum();

			// 注意：在这里，歌词同步有问题，将在下一个版本中得到修正
			sleepTime = currentTime - startTime
					- (long) lrcTimeAndIndex.getLrcTime();

			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			repaint();
		}
	}

	public void paint(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		Font type = new Font("monospaced", Font.BOLD, 14);
		g2D.setFont(type);
		g2D.setColor(Color.black);
		g2D.fillRect(0, 0, getSize().width, getSize().height);
		g2D.setColor(Color.blue);

		for (int i = 0; i < lrcAnalystBase.getLrcContent().size(); i++) {
			g2D.drawString((String) lrcAnalystBase.getLrcContent().get(i), 5, y
					+ (20 * i));
		}
	}
}