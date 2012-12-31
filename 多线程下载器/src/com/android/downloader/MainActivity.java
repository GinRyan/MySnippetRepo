package com.android.downloader;

import java.io.File;

import com.android.network.DownloadProgressListener;
import com.android.network.FileDownloader;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private EditText downloadpathText;
    private TextView resultView;
    private ProgressBar progressBar;
    
    /**
     * 当Handler被创建会关联到创建它的当前线程的消息队列，该类用于往消息队列发送消息
     * 消息队列中的消息由当前线程内部进行处理
     * 使用Handler更新UI界面信息。
     */
    private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {			
			switch (msg.what) {
			case 1:				
				progressBar.setProgress(msg.getData().getInt("size"));
				float num = (float)progressBar.getProgress()/(float)progressBar.getMax();
				int result = (int)(num*100);
				resultView.setText(result+ "%");
				
				//显示下载成功信息
				if(progressBar.getProgress()==progressBar.getMax()){
					Toast.makeText(MainActivity.this, R.string.success, 1).show();
				}
				break;
			case -1:
				//显示下载错误信息
				Toast.makeText(MainActivity.this, R.string.error, 1).show();
				break;
			}
		}
    };
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        downloadpathText = (EditText) this.findViewById(R.id.path);
        progressBar = (ProgressBar) this.findViewById(R.id.downloadbar);
        resultView = (TextView) this.findViewById(R.id.resultView);
        Button button = (Button) this.findViewById(R.id.button);
        
        button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String path = downloadpathText.getText().toString();
				System.out.println(Environment.getExternalStorageState()+"------"+Environment.MEDIA_MOUNTED);
				
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					//开始下载文件
					download(path, Environment.getExternalStorageDirectory());
				}else{
					//显示SDCard错误信息
					Toast.makeText(MainActivity.this, R.string.sdcarderror, 1).show();
				}
			}
		});
    }
    
  	/**
  	 * 主线程(UI线程)
  	 * 对于显示控件的界面更新只是由UI线程负责，如果是在非UI线程更新控件的属性值，更新后的显示界面不会反映到屏幕上
  	 * 如果想让更新后的显示界面反映到屏幕上，需要用Handler设置。
  	 * @param path
  	 * @param savedir
  	 */
    private void download(final String path, final File savedir) {
    	new Thread(new Runnable() {			
			@Override
			public void run() {
				//开启3个线程进行下载
				FileDownloader loader = new FileDownloader(MainActivity.this, path, savedir, 3);
		    	progressBar.setMax(loader.getFileSize());//设置进度条的最大刻度为文件的长度
		    	
				try {
					loader.download(new DownloadProgressListener() {
						@Override
						public void onDownloadSize(int size) {//实时获知文件已经下载的数据长度
							Message msg = new Message();
							msg.what = 1;
							msg.getData().putInt("size", size);
							handler.sendMessage(msg);//发送消息
						}
					});
				} catch (Exception e) {
					handler.obtainMessage(-1).sendToTarget();
				}
			}
		}).start();
	}
}