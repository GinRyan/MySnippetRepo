package org.cn.gv;

import org.cn.bean.MyService;
import org.cn.bean.SelectDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
/***
 * 
 * @author 
 *
 */
public class SelectPicActivity extends Activity {
    /** Called when the activity is first created. */
	
	private Button btn_select;
	private LinearLayout ll_this;
	private SelectDialog selectdialog;
	private Button btn_a;
	private Button btn_b;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_cam);
        
        Intent itnt = new Intent(SelectPicActivity.this,MyService.class);
        startService(itnt);
        btn_select = (Button)findViewById(R.id.button);
        ll_this = (LinearLayout)findViewById(R.id.ll_this);
        ImageView iv = (ImageView)findViewById(R.id.image);
        
        if (MyService.bm!=null) {
        	iv.setImageBitmap(MyService.bm);
		}
        
        selectdialog = new SelectDialog(SelectPicActivity.this, R.style.dialog);
        Window win = selectdialog.getWindow();
        LayoutParams params = new LayoutParams();
        //params.x = -80;
        params.y = 300;
        params.width = 300;
        win.setAttributes(params);
        btn_select.setOnClickListener(new OnClickListener() {
        	
			public void onClick(View v) {
				
				selectdialog.show();
				
		        btn_a = (Button)selectdialog.findViewById(R.id.btn_a);
			    btn_b = (Button)selectdialog.findViewById(R.id.btn_b);
			    
			    final Intent intent = new Intent();

			    btn_a.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						intent.setClass(SelectPicActivity.this, SelectPicturesFromSD.class);
						selectdialog.dismiss();
						
						startActivity(intent);
						
					}
				});
			    btn_b.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						intent.setClass(SelectPicActivity.this, TakePictureActivity.class);
						selectdialog.dismiss();
						startActivity(intent);
					}
				});
			    
			}
		});
    }
  
    @Override
    protected void onResume() {
    	
    	super.onResume();
    }
}