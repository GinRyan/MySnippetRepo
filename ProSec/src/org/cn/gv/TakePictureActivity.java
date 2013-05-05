package org.cn.gv;

import org.cn.bean.MyPictureCallback;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

public class TakePictureActivity extends Activity {
	
	private SurfaceView sv;
	private SurfaceHolder holder;
	private Camera camera;
	private Button btn0;
	private boolean isOn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera);
		isOn = false;
		sv = (SurfaceView)findViewById(R.id.surfaceview4cam);
		holder = sv.getHolder();
		holder.setFixedSize(240, 320);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		holder.addCallback(new MyAddCallback());
		btn0 = (Button)findViewById(R.id.btn_cature);
		btn0.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				camera.autoFocus(null);
				camera.takePicture(null, null,new MyPictureCallback());
				
			}
		});
	}

	private class MyAddCallback implements SurfaceHolder.Callback{

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			try {
			camera = Camera.open();
			Parameters parameters =  camera.getParameters();
			parameters.setPictureSize(768, 1024);
			parameters.setPictureFormat(PixelFormat.JPEG);
			//LayoutParams lp = getWindow().getAttributes();
			//parameters.setPreviewSize(lp.width, lp.height);
			parameters.set("jpeg-quality",85);
			//parameters.set("orientation", "portrait"); 
			//camera.setParameters(parameters);
			camera.setPreviewDisplay(holder);
			camera.startPreview();
			isOn = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			if (camera!=null) {
				if (isOn) {
					camera.stopPreview();
					Intent itnt = new Intent(TakePictureActivity.this,SelectPicActivity.class);
					startActivity(itnt);
				}
				camera.release();
			}
		}
	}
}
