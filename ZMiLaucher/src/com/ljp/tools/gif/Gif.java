package com.ljp.tools.gif;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;

public class Gif implements GifAction{

	private GifDecoder gifDecoder = null;
	private Bitmap currentImage = null;
	
	public Gif(Resources resource,int resId) {
		// TODO Auto-generated constructor stub
    	InputStream is = resource.openRawResource(resId);
    	if(gifDecoder != null){
    		gifDecoder.free();
    		gifDecoder= null;
    	}
    	gifDecoder = new GifDecoder(is,this);
    	gifDecoder.run();
	}
		
	
	@Override
	public void parseOk(boolean parseStatus, int frameIndex) {
		// TODO Auto-generated method stub
		
		
	}
	
	public List<HashMap<String, Object>> Frame(){
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
		for(int i=0;i<=gifDecoder.getFrameCount();i++){
			 GifFrame frame = gifDecoder.next();
			 HashMap<String, Object> map = new HashMap<String, Object>();
	 		 currentImage = frame.image;
	 		 
	 		 map.put("image", currentImage);
	 		 map.put("delay", frame.delay);
	 		 list.add(map);
			}
		return list;
		
		
	}
	


}
