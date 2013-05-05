package com.xiawenquan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class TextBitmapAvtivity extends Activity {
 	Drawable bmImg;    
    ImageView imView;   
    ImageView imView2;   
    TextView text;  
    String theTime; 
    long start, stop;   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
//        text=(TextView)findViewById(R.id.text);  
//        
//        imView=(ImageView) findViewById(R.id.imageShow);  
//        imView2=(ImageView) findViewById(R.id.image2);  
          
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),     
                R.drawable.qqy);  
          
//        start=System.currentTimeMillis();  
//        Log.e("Main", start + "");
////        imView.setImageDrawable(resizeImage(bitmap, 300, 100));    
//          
//        imView2.setImageDrawable(resizeImage2("/sdcard/qq.jpg", 200, 100));   
//          
//        stop=System.currentTimeMillis();  
//        Log.e("Main", stop + "");
//        String theTime= String.format("\n1 iterative: (%d msec)",    
//                stop - start);    
//          Log.e("Main", theTime);
          
          
        start=System.currentTimeMillis();  
        Bitmap bitmap2 = ThumbnailUtils.extractThumbnail(bitmap,50,50);
        saveThePicture(bitmap2);
        imView.setImageBitmap(bitmap2);//2.2才加进来的新类，简单易用   
//        imView.setImageBitmap(ThumbnailUtils.extractThumbnail(bitmap, 100, 100));//2.2才加进来的新类，简单易用   
//        imView.setImageBitmap(ThumbnailUtils.extractThumbnail(bitmap, 150, 150, 1000));//2.2才加进来的新类，简单易用   
//        imView.setImageDrawable(resizeImage(bitmap, 30, 30));    
        stop=System.currentTimeMillis();  
          
         theTime+= String.format("\n2 iterative: (%d msec)",    
                stop - start);   
          
        text.setText(theTime);  
    }
    
  //使用Bitmap加Matrix来缩放   
    public static Drawable resizeImage(Bitmap bitmap, int w, int h)   
    {    
//        Bitmap BitmapOrg = bitmap;    
//        int width = BitmapOrg.getWidth();    
//        int height = BitmapOrg.getHeight();    
        int width = bitmap.getWidth();    
        int height = bitmap.getHeight();    
        int newWidth = w;    
        int newHeight = h;    
  
        float scaleWidth = ((float) newWidth) / width;    
        float scaleHeight = ((float) newHeight) / height;    
  
        Matrix matrix = new Matrix();    
        matrix.postScale(scaleWidth, scaleHeight);    
        // if you want to rotate the Bitmap      
         matrix.postRotate(45);      
//        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,    
//                        height, matrix, true);    
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width,    
        		height, matrix, true);    
        return new BitmapDrawable(resizedBitmap);    
    }  
      
    /**
     * 使用BitmapFactory.Options的inSampleSize参数来缩放   
     * @param path
     * @param width
     * @param height
     * @return
     */
    public static Drawable resizeImage2(String path,  
            int width,int height)   
    {  
        BitmapFactory.Options options = new BitmapFactory.Options();  
        options.inJustDecodeBounds = true;//不加载bitmap到内存中   
        BitmapFactory.decodeFile(path,options);   
        int outWidth = options.outWidth;  
        int outHeight = options.outHeight;  
        options.inDither = false;  
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;  
        options.inSampleSize = 1;  
          
        if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0)   
        {  
            int sampleSize=(outWidth/width+outHeight/height)/2;  
            Log.d("Main", "sampleSize = " + sampleSize);  
            options.inSampleSize = sampleSize;  
        }  
      
        options.inJustDecodeBounds = false;  
        return new BitmapDrawable(BitmapFactory.decodeFile(path, options));       
    }  
  
    //图片保存   
    private void saveThePicture(Bitmap bitmap)  
    {  
    	File fileSC = Environment.getExternalStorageDirectory();
    	Log.v("Main", fileSC + "");
        File file=new File(fileSC + "/qqy.png");  
        try  
        {  
            FileOutputStream fos=new FileOutputStream(file);  
            if(bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos))  
            {  
                fos.flush();  
                fos.close();  
            }  
        }  
        catch(FileNotFoundException e1)  
        {  
            e1.printStackTrace();  
        }  
        catch(IOException e2)  
        {  
            e2.printStackTrace();  
        }  
    }  
}