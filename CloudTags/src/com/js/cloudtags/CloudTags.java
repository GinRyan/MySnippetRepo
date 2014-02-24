package com.js.cloudtags;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CloudTags extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	
	public String[] keywords = {
			"QQ", "Sodino", "APK", "GFW", "铅笔", 
            "短信", "桌面精灵", "MacBook Pro", "平板电脑", "雅诗兰黛",
            "卡西欧 TR-100", "笔记本", "SPY Mouse", "Thinkpad E40", "捕鱼达人", 
            "内存清理", "地图", "导航", "闹钟", "主题",   
            "通讯录", "播放器", "CSDN leak", "安全", "3D",   
            "美女", "天气", "4743G", "戴尔", "联想",   
            "欧朋", "浏览器", "愤怒的小鸟", "mmShow", "网易公开课",   
            "iciba", "油水关系", "网游App", "互联网", "365日历",   
            "脸部识别", "Chrome", "Safari", "中国版Siri", "A5处理器",   
            "iPhone4S", "摩托 ME525", "魅族 M9", "尼康 S2500" 
	};
	private KeywordsFlow keywordsFlow;
	private Button btnIn, btnOut;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        btnIn = (Button) findViewById(R.id.in);  
        btnOut = (Button) findViewById(R.id.out);  
        btnIn.setOnClickListener(this);  
        btnOut.setOnClickListener(this);  
        keywordsFlow = (KeywordsFlow) findViewById(R.id.keywordsflow);
        keywordsFlow.setDuration(800l);  
        keywordsFlow.setOnItemClickListener(this);  
        // 添加   
        feedKeywordsFlow(keywordsFlow, keywords);  
        keywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);
    }
    
    private static void feedKeywordsFlow(KeywordsFlow keywordsFlow, String[] arr) {  
        Random random = new Random();  
        for (int i = 0; i < KeywordsFlow.MAX; i++) {  
            int ran = random.nextInt(arr.length);  
            String tmp = arr[ran];  
            keywordsFlow.feedKeyword(tmp);  
        }  
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnIn) {  
            keywordsFlow.rubKeywords();  
            // keywordsFlow.rubAllViews();   
            feedKeywordsFlow(keywordsFlow, keywords);  
            keywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);  
        } else if (v == btnOut) {  
            keywordsFlow.rubKeywords();  
            // keywordsFlow.rubAllViews();   
            feedKeywordsFlow(keywordsFlow, keywords);  
            keywordsFlow.go2Show(KeywordsFlow.ANIMATION_OUT);  
        } else if (v instanceof TextView) {  
            String keyword = ((TextView) v).getText().toString();  
            Intent intent = new Intent();  
            intent.setAction(Intent.ACTION_VIEW);  
            intent.addCategory(Intent.CATEGORY_DEFAULT);  
            intent.setData(Uri.parse("http://www.google.com.hk/#q=" + keyword));  
            startActivity(intent);  
        }  
	}  
   
    
}