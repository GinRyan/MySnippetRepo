package whu.iss.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.ZoomControls;

public class main extends Activity {
	/** Called when the activity is first created. */
	private ImageZoomView mZoomView;
	private ZoomState mZoomState;
	private Bitmap mBitmap;
	private SimpleZoomListener mZoomListener;
	private ProgressBar progressBar;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			progressBar.setVisibility(View.GONE);
			mZoomView.setImage(mBitmap);
			mZoomState = new ZoomState();
			mZoomView.setZoomState(mZoomState);
			mZoomListener = new SimpleZoomListener();
			mZoomListener.setZoomState(mZoomState);
			mZoomView.setOnTouchListener(mZoomListener);
			resetZoomState();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		mZoomView = (ImageZoomView) findViewById(R.id.zoomView);
		progressBar = (ProgressBar) findViewById(R.id.progress_large);
		progressBar.setVisibility(View.VISIBLE);
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				/*
				 * º”‘ÿÕ¯¬ÁÕº∆¨ load form url
				 */
				// mBitmap =
				// ImageDownloader.getInstance().getBitmap(url);
				mBitmap = BitmapFactory.decodeResource(
						main.this.getResources(), R.drawable.earth);
				handler.sendEmptyMessage(0);
			}
		});
		thread.start();

		ZoomControls zoomCtrl = (ZoomControls) findViewById(R.id.zoomCtrl);
		zoomCtrl.setOnZoomInClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				float z = mZoomState.getZoom() + 0.25f;
				mZoomState.setZoom(z);
				mZoomState.notifyObservers();
			}
		});
		zoomCtrl.setOnZoomOutClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				float z = mZoomState.getZoom() - 0.25f;
				mZoomState.setZoom(z);
				mZoomState.notifyObservers();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mBitmap != null)
			mBitmap.recycle();
		// mZoomView.setOnTouchListener(null);
		// mZoomState.deleteObservers();
	}

	private void resetZoomState() {
		mZoomState.setPanX(0.5f);
		mZoomState.setPanY(0.5f);
		mZoomState.setZoom(1f);
		mZoomState.notifyObservers();
	}
}