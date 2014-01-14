package com.myc;

import imageUtil.ImageUtil;
import imageUtil.ToneLayer;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ImageToneActivity extends Activity implements OnSeekBarChangeListener {  
    private ToneLayer mToneLayer;  
    private ImageView mImageView;  
    private Bitmap mBitmap;  
      
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.handle_image);  
          
        init();  
    }  
      
    private void init()  
    {  
        mToneLayer = new ToneLayer(this);  
          
        mBitmap = ImageUtil.readBitMap(this, R.drawable.image); 
        mImageView = (ImageView) findViewById(R.id.img_view);  
        mImageView.setImageBitmap(mBitmap);  
        ((LinearLayout) findViewById(R.id.tone_view)).addView(mToneLayer.getParentView());  
        ArrayList<SeekBar> seekBars = mToneLayer.getSeekBars();  
        for (int i = 0, size = seekBars.size(); i < size; i++)  
        {  
            seekBars.get(i).setOnSeekBarChangeListener(this);  
        }  
    }  
  
    public void onProgressChanged(SeekBar seekBar, int progress,  
            boolean fromUser) {  
        int flag = (Integer) seekBar.getTag();  
        switch (flag)  
        {  
        case ToneLayer.FLAG_SATURATION:  
            mToneLayer.setSaturation(progress);  
            break;  
        case ToneLayer.FLAG_LUM:  
            mToneLayer.setLum(progress);  
            break;  
        case ToneLayer.FLAG_HUE:  
            mToneLayer.setHue(progress);  
            break;  
        }  
          
        mImageView.setImageBitmap(mToneLayer.handleImage(mBitmap, flag));  
    }  
  
    public void onStartTrackingTouch(SeekBar seekBar) {  
          
    }  
    
    public void onStopTrackingTouch(SeekBar seekBar) {  
          
    }  
}  
