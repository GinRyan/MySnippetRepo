package hong.specialEffects.ui;


import hong.specialEffects.R;
import hong.specialEffects.wight.CoverFlow;
import hong.specialEffects.wight.CoverFlowImageAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class CoverFlowActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CoverFlow cf = new CoverFlow(this);
		// cf.setBackgroundResource(R.drawable.shape);
		cf.setBackgroundColor(Color.BLACK);
		final Integer[] mImageIds = { R.drawable.animation,R.drawable.bookpage, R.drawable.custon,R.drawable.drawer,
				R.drawable.header_bottom_list,R.drawable.plasma,R.drawable.splashscreen,R.drawable.wheel,R.drawable.circle_list_view, 
				R.drawable.hor_list_view,R.drawable.hor_list_scorll,R.drawable.cus_radio_btn,R.drawable.circle_corner,
				R.drawable.muilt_grade_list,R.drawable.custom_spinner,R.drawable.muilt_table,R.drawable.left_right_slide,
				R.drawable.fling_gallery};
		final Class<?>[] target={Animation_BeginActivity.class,BookpageActivity.class,CustonActivity.class,DrawerActivity.class,
				HeaderBottomListActivity.class,PlasmaActivity.class,SplashScreen_BeginActivity.class,WheelActivity.class,RoundCornerActivity.class,
				HorizontalListViewActivity.class,HorListviewActivity.class,CustomRadioButtonActivity.class,CircleCornerActivity.class,
				MuiltGradeListActivity.class,CustomSpinnerActivity.class,MuiltTableActivity.class,LeftRightSlideActivity.class,
				FlingGalleryActivity.class};
		CoverFlowImageAdapter imageAdapter = new CoverFlowImageAdapter(this,mImageIds,target);
		cf.setAdapter(imageAdapter);
		// cf.setAlphaMode(false);
		// cf.setCircleMode(false);
		cf.setSelection(2, true);
		cf.setAnimationDuration(1000);
		cf.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				final Intent intent=new Intent();
				System.out.println("onItemClick:"+arg2);
				intent.setClass(CoverFlowActivity.this,target[arg2]);
				startActivity(intent);
			}
		});
		setContentView(cf);
	}

}