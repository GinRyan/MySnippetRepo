/**
 * http://bbs.sei.ynu.edu.cn/viewthread.php?tid=19689&page=1
    * 文件名：LrcAnalyst.java
    * 环境： GNU/Linux Ubuntu 7.04 + Eclipse 3.2 + JDK 1.6
    * 功能：解析Lrc歌词文件的主驱动程序
    * 版本：0.0.2.0
    * 作者：88250
    * 日期：2007.4.27
    * E-mail & MDN: DL88250@gmail.com
    * QQ：845765
    */

    import javax.swing.JFrame;

    /**
    * 测试歌词解析结果
    */
    public class LrcAnalyst2
    {
        public LrcAnalyst2()
        {
            JFrame frame = new JFrame("Lrc Srcoll");

            frame.setBounds(600, 300, 400, 200);  
            frame.setResizable(false);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            LrcScrollPane pane = new LrcScrollPane();
            frame.getContentPane().add(pane);
            frame.setVisible(true);
            pane.scroll();
        }
       
        /**
         * 主程序入口点
         *
         * @param args
         *                命令行参数，这里没用到，为<code>null</code>
         */
        public static void main(String[] args)
        {
            new LrcAnalyst2();
        }
    }