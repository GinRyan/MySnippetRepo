package com.ljp.tools.gif;

import android.graphics.Bitmap;

public class GifFrame {
	/**Õº∆¨*/
	public Bitmap image;
	/**—” ±*/
	public int delay;
	/**œ¬“ª÷°*/
	public GifFrame nextFrame = null;
	
	public GifFrame(Bitmap im, int del) {
		image = im;
		delay = del;
	}
	
}
