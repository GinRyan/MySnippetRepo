package hong.specialEffects.ui;

import hong.specialEffects.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.TextView;

public class DrawerActivity extends Activity {
	private ImageView arrawImageView;
	private SlidingDrawer slidingDrawer;
	private TextView textView;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);
        init();
    }
    
    private void init() {
		arrawImageView = (ImageView) findViewById(R.id.arrowImage);
		textView = (TextView) findViewById(R.id.textView);
		textView.setText("展开");
		slidingDrawer = (SlidingDrawer) findViewById(R.id.myslidingDrawer);
		slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() { // 打开抽屉
			
			@Override
			public void onDrawerOpened() {
				// TODO Auto-generated method stub
				arrawImageView.setImageResource(R.drawable.down);
				Animation animation = AnimationUtils.loadAnimation(DrawerActivity.this, R.drawable.arrowrote);
				arrawImageView.setAnimation(animation);
				textView.setText("收起");
			}
		});
		slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() { // 关闭抽屉
			
			@Override
			public void onDrawerClosed() {
				// TODO Auto-generated method stub
				arrawImageView.setImageResource(R.drawable.up);
				Animation animation = AnimationUtils.loadAnimation(DrawerActivity.this, R.drawable.arrowrote);
				arrawImageView.setAnimation(animation);
				textView.setText("展开");
			}
		});
	}
}