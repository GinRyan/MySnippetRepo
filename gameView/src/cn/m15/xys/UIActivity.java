package cn.m15.xys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class UIActivity extends Activity {
  
    Context mContext = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.main);
	
	/**绘制字符串**/
        Button botton0 = (Button)findViewById(R.id.button0);
        botton0.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View arg0) {
		 Intent intent = new Intent(mContext,Font.class); 
		 startActivity(intent);
	    }
	}); 
        
	/**绘制无规则等集合图形**/
        Button botton1 = (Button)findViewById(R.id.button1);
        botton1.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View arg0) {
		 Intent intent = new Intent(mContext,Geometry.class); 
		 startActivity(intent);
	    }
	}); 
        
	/**图片旋转缩放的绘制**/
        Button botton2 = (Button)findViewById(R.id.button2);
        botton2.setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View arg0) {
		 Intent intent = new Intent(mContext,Image.class); 
		 startActivity(intent);
	    }
	}); 
        
	/**播放frame动画**/
        Button botton3 = (Button)findViewById(R.id.button3);
        botton3.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View arg0) {
		 Intent intent = new Intent(mContext,FramAnimation.class); 
		 startActivity(intent);
	    }
	}); 
    }
}