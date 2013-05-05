package com.alarmclocksnoozers.runnershigh;

import javax.microedition.khronos.opengles.GL10;
import android.graphics.Bitmap;

public class ParalaxBackground extends Group {
	
	private RHDrawable backgroundLayer1;
	private RHDrawable backgroundLayer2;
	private RHDrawable backgroundLayer3;
	
	private Bitmap BGImg1 = null;
	private Bitmap BGImg2 = null;
	private Bitmap BGImg3 = null;
	
	private int mWidth;
	private int mHeight;
	
	public ParalaxBackground(int width, int heigth) {
		mWidth = width;
		mHeight = heigth;
	}
	
	public void cleanup() {
		if (BGImg1 != null) BGImg1.recycle();
		if (BGImg2 != null) BGImg2.recycle();
		if (BGImg3 != null) BGImg3.recycle();
	}
	
	
	public void loadLayerNear(Bitmap image){
		
		BGImg1 = image;
		backgroundLayer1 = new RHDrawable(0, 0, -1, mWidth*4, mHeight);
		
		backgroundLayer1.loadBitmap(BGImg1, GL10.GL_REPEAT, GL10.GL_CLAMP_TO_EDGE); 
		backgroundLayer1.z = -0.1f;
		
		add(backgroundLayer1);
		
		float textureCoordinates[] = { 0.0f, 1.0f, //
				2.0f, 1.0f, //
				0.0f, 0.0f, //
				2.0f, 0.0f, //
		};
		
		backgroundLayer1.setTextureCoordinates(textureCoordinates);
	}
	
	public void loadLayerMiddle(Bitmap image) {
		BGImg2 = image;
		backgroundLayer2 = new RHDrawable(0, 0, -1, mWidth*4, mHeight);
		backgroundLayer2.loadBitmap(BGImg2, GL10.GL_REPEAT, GL10.GL_CLAMP_TO_EDGE);
		backgroundLayer2.z = -0.2f;

		add(backgroundLayer2);
		
		float textureCoordinates[] = { 0.0f, 1.0f, //
				2.0f, 1.0f, //
				0.0f, 0.0f, //
				2.0f, 0.0f, //
		};
		
		
		backgroundLayer2.setTextureCoordinates(textureCoordinates);
	}
	
	public void loadLayerFar(Bitmap image) {

		BGImg3 = image;
		backgroundLayer3 = new RHDrawable(0, 0, -1, mWidth*4, mHeight);
		backgroundLayer3.loadBitmap(BGImg3, GL10.GL_REPEAT, GL10.GL_CLAMP_TO_EDGE);
		backgroundLayer3.z = -0.3f;

		add(backgroundLayer3);
		float textureCoordinates[] = { 0.0f, 1.0f, //
				2.0f, 1.0f, //
				0.0f, 0.0f, //
				2.0f, 0.0f, //
		};
		
		backgroundLayer3.setTextureCoordinates(textureCoordinates);
	}
	
	
	public void update(){
		backgroundLayer1.x -= 1;
		if (backgroundLayer1.x < -mWidth*2)
			backgroundLayer1.x = 0;
		
		backgroundLayer2.x -= 0.5f;
		if (backgroundLayer2.x < -mWidth*2)
			backgroundLayer2.x = 0;
		
		backgroundLayer3.x -= 0.2f;
		if (backgroundLayer3.x < -mWidth*2)
			backgroundLayer3.x = 0;
		
	}
	
	
};
	
