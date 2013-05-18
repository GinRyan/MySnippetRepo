/**
    * 文件名：LrcScrollPane.java
    * 环境： GNU/Linux Ubuntu 7.04 + Eclipse 3.2 + JDK 1.6
    * 功能：滚动的Lrc歌词显示面板
    * 版本：0.0.2.0
    * 作者：88250
    * 日期：2007.4.28
    * E-mail & MDN: DL88250@gmail.com
    * QQ：845765
    */

    import java.awt.Color;
    import java.awt.Font;
    import java.awt.Graphics;
    import java.awt.Point;
    import javax.swing.JPanel;
    import java.util.Date;

    public class LrcScrollPane extends JPanel
    {
        private final int heightOfChar = 20;

        private final Font fontOfChar = new Font("monospaced", Font.BOLD, 14);

        private Point scrollPoint; // 参考点，会慢慢向上移。

        private LrcAnalystBase lrcAnalyst;
       
        private static int lrcIndex = 0;
       
        public LrcScrollPane()
        {
            lrcAnalyst = new LrcAnalystBase();
            lrcAnalyst.readFile("六月的雨.lrc");
            lrcAnalyst.parseLyrics();
            scrollPoint = new Point(5, getSize().height);
        }

        /**
         * 实现歌词的滚动显示
         */
        public void scroll()
        {
            Date startTime = new Date();
            for (int i = 0; i < lrcAnalyst.getLrcTimeValAndIndex().size(); i++)
            {
                while (true)
                {
                    try
                    {
                        Thread.currentThread().sleep(50);
                        
                        Date currentTime = new Date();
                        LrcTimeAndIndexNum fl = (LrcTimeAndIndexNum) (lrcAnalyst
                                .getLrcTimeValAndIndex().get(i));
                        float diffTime = currentTime.getTime()
                                - startTime.getTime();
                        lrcIndex = fl.getLrcIndex()- lrcAnalyst.getRealLrcStartOffset();
                        if (fl.getLrcTime() - (float) diffTime / 1000 < 0.0)
                        {
                            scrollPoint.y--;
                            //System.out.println((String) lrcAnalyst.getLrcContent().get(
                            //        fl.getLrcIndex()- lrcAnalyst.getRealLrcStartOffset()));
                            repaint();
                           
                            break;
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
       
        public void paintComponent(Graphics brush)
        {
            // 用背景色覆盖，原来的文字就看不到了。再写上新的文字，由于视觉停留，
            // 看起来文字就会滚动
            brush.setColor(getBackground());
            brush.fillRect(0, 0, getSize().width, getSize().height);

            brush.setColor(Color.blue);
            brush.setFont(fontOfChar);

            // i为下标，用来历遍歌词缓存容器,
            // 当scrollPoint.y+(heightOfChar*i)<0 时，会把文字写到面板上方，看不见。
            // 没有必要显示。
            // scrollPoint.y+(heightOfChar*i)=0 => i=(-scrollPoint.y/heightOfChar)
            // 故要使(-scrollPoint.y/heightOfChar)和 0 作比较，要是小于0，i取0
            /*int i = 0;
            if ((-scrollPoint.y / heightOfChar) > 0)
            {
                i = (-scrollPoint.y / heightOfChar);
            }*/
           
            /*String lyrics = null;
            for (; i < lrcAnalyst.getLrcContent().size(); i++)
            {
                // 当条件成立，文字会写到面板下方，也没有必要显示，跳出循环
                if (scrollPoint.y + (heightOfChar * i) > getSize().height)
                {
                    break;
                }
                lyrics = (String) (lrcAnalyst.getLrcContent().get(i));
                // 见scroll(),靠scrollPoint.y的上移，使文字上移
                brush.drawString(lyrics, scrollPoint.x, scrollPoint.y
                         + (heightOfChar * i));
            }        */
           
            String lyrics = (String) lrcAnalyst.getLrcContent().get(lrcIndex);
                // 见scroll(),靠scrollPoint.y的上移，使文字上移
                brush.drawString(lyrics, scrollPoint.x, -scrollPoint.y + 20);
        }
    }