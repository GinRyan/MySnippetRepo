package com.example.animation;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FrameAnimationActivity extends Activity {
	Button m_btnStart;
	ImageView m_imgAnimation;
	TextView m_lblText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(com.example.R.layout.frameanimaitonlayout);
		
		m_btnStart=(Button)findViewById(com.example.R.id.frameanimation_btnStart);
		m_imgAnimation=(ImageView)findViewById(com.example.R.id.frameanimaiton_imageView1);
		m_imgAnimation.setBackgroundResource(com.example.R.drawable .frame_animation );
		m_btnStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AnimationDrawable frameAnimation=(AnimationDrawable)m_imgAnimation.getBackground();

				if(frameAnimation.isRunning()){
					frameAnimation.stop();
				}else{
					frameAnimation.stop();
					
					frameAnimation.start();
				}
			}
		});
		
		m_lblText=(TextView)findViewById(com.example.R.id.frameanimation_lblText);
		m_lblText.setTextColor(getResources().getColor( com.example.R.color.frameanimation_lblTextColor));
		
	}
}
