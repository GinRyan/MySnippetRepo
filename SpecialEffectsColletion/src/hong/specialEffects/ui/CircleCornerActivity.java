package hong.specialEffects.ui;

import hong.specialEffects.R;
import hong.specialEffects.graphic.Processor;
import hong.specialEffects.graphic.Processor.HalfType;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;
public class CircleCornerActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        setContentView(R.layout.circle_corner_main);   
        BitmapDrawable bitmapDrawable = (BitmapDrawable)getResources().getDrawable(R.drawable.btn_bg);
        Bitmap bitmap = bitmapDrawable.getBitmap();    
        bitmap =Processor.createRepeater(bitmap,150);

        bitmapDrawable=(BitmapDrawable)getResources().getDrawable(R.drawable.btn_bg_click);
        Bitmap bitmap_click = bitmapDrawable.getBitmap();
        bitmap_click =Processor.createRepeater(bitmap_click,150);
       
	    ImageView imageView_left = (ImageView)findViewById(R.id.img_show_left);
	    ImageView imageView_right = (ImageView)findViewById(R.id.img_show_right);
	    ImageView imageView_top = (ImageView)findViewById(R.id.img_show_top);
	    ImageView imageView_bottom = (ImageView)findViewById(R.id.img_show_bottom);
	    
	    imageView_left.setImageBitmap(Processor.getRoundCornerImage(bitmap,10,HalfType.LEFT));
	    imageView_right.setImageBitmap(Processor.getRoundCornerImage(bitmap_click,10,HalfType.RIGHT));
	    imageView_top.setImageBitmap(Processor.getRoundCornerImage(bitmap,10,HalfType.TOP));
	    imageView_bottom.setImageBitmap(Processor.getRoundCornerImage(bitmap,20,HalfType.BOTTOM));
	    
	    
        
    }
    
    
}