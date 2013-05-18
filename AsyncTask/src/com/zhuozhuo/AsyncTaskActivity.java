package com.zhuozhuo;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class AsyncTaskActivity extends Activity {
    
	private ImageView mImageView;
	private Button mButton;
	private ProgressBar mProgressBar;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mImageView= (ImageView) findViewById(R.id.imageView);
        mButton = (Button) findViewById(R.id.button);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				GetCSDNLogoTask task = new GetCSDNLogoTask();
				task.execute("http://www.linuxidc.com/pic/logo.gif");
			}
		});
    }
    
    class GetCSDNLogoTask extends AsyncTask<String,Integer,Bitmap> {//继承AsyncTask

		@Override
		protected Bitmap doInBackground(String... params) {//处理后台执行的任务，在后台线程执行
			publishProgress(0);//将会调用onProgressUpdate(Integer... progress)方法
			HttpClient hc = new DefaultHttpClient();
			publishProgress(30);
			HttpGet hg = new HttpGet(params[0]);//获取csdn的logo
			final Bitmap bm;
			try {
				HttpResponse hr = hc.execute(hg);
				bm = BitmapFactory.decodeStream(hr.getEntity().getContent());
			} catch (Exception e) {
				
				return null;
			}
			publishProgress(100);
			//mImageView.setImageBitmap(result); 不能在后台线程操作ui
			return bm;
		}
		
		protected void onProgressUpdate(Integer... progress) {//在调用publishProgress之后被调用，在ui线程执行
			mProgressBar.setProgress(progress[0]);//更新进度条的进度
	     }

	     protected void onPostExecute(Bitmap result) {//后台任务执行完之后被调用，在ui线程执行
	    	 if(result != null) {
	    		 Toast.makeText(AsyncTaskActivity.this, "成功获取图片", Toast.LENGTH_LONG).show();
	    		 mImageView.setImageBitmap(result);
	    	 }else {
	    		 Toast.makeText(AsyncTaskActivity.this, "获取图片失败", Toast.LENGTH_LONG).show();
	    	 }
	     }
	     
	     protected void onPreExecute () {//在 doInBackground(Params...)之前被调用，在ui线程执行
	    	 mImageView.setImageBitmap(null);
	    	 mProgressBar.setProgress(0);//进度条复位
	     }
	     
	     protected void onCancelled () {//在ui线程执行
	    	 mProgressBar.setProgress(0);//进度条复位
	     }
    	
    }
    

}