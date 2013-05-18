package com.ljp.tools.gif;

import java.io.InputStream;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;


public class GifDrawable extends AnimationDrawable implements GifAction{

	/**gif������*/
	private GifDecoder gifDecoder = null;
	/**��ǰҪ����֡��ͼ*/
	private Bitmap currentImage = null;
	
	public GifDrawable(Resources resource,int resId) {
		// TODO Auto-generated constructor stub
    	InputStream is = resource.openRawResource(resId);
    	if(gifDecoder != null){
    		gifDecoder.free();
    		gifDecoder= null;
    	}
    	gifDecoder = new GifDecoder(is,this);
    	gifDecoder.run();
	}
		
	public GifDrawable(InputStream is) {
		// TODO Auto-generated constructor stub
		try {
			if(gifDecoder != null){
	    		gifDecoder.free();
	    		gifDecoder= null;
	    	}
	    	gifDecoder = new GifDecoder(is,this);
	    	gifDecoder.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}	
	


	@Override
	public void parseOk(boolean parseStatus, int frameIndex) {
		// TODO Auto-generated method stub
		if(frameIndex==-1){
		for(int i=0;i<=gifDecoder.getFrameCount();i++){
		 GifFrame frame = gifDecoder.next();
 		 currentImage = frame.image;
 		 int delay = frame.delay;
 		 addFrame(new BitmapDrawable(currentImage), delay);
		}
		setOneShot(false);
		setVisible(true, true);
		}
		
	}



	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		
		super.draw(canvas);
		start();
	}
}
