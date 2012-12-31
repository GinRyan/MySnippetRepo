package com.android.demo;




import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewAnimator;

public class TestCustomTitle extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.i_main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.i_title);
        

        final TextView title = (TextView) findViewById(R.id.title);
        final Button leftBtn = (Button) findViewById(R.id.title_left_btn);
        final Button rightBtn = (Button) findViewById(R.id.title_right_btn);
        title.setText("step #1");
        leftBtn.setText("quit");
        rightBtn.setText("next");
        final EditText firstName = (EditText) findViewById(R.id.first_name);
        final EditText lastName = (EditText) findViewById(R.id.last_name);
        final ViewAnimator animator = (ViewAnimator) findViewById(R.id.animator);
        final Animation slideInLeft = AnimationUtils.loadAnimation(this, R.anim.i_slide_in_left);
        final Animation slideInRight = AnimationUtils.loadAnimation(this, R.anim.i_slide_in_right);
        final Animation slideOutLeft = AnimationUtils.loadAnimation(this, R.anim.i_slide_out_left);
        final Animation slideOutRight = AnimationUtils.loadAnimation(this, R.anim.i_slide_out_right);
        
        leftBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (animator.getDisplayedChild() == 0) {
					finish();
				} else {
			        title.setText("step #1");
			        leftBtn.setText("quit");
			        rightBtn.setText("next");
					animator.setInAnimation(slideInRight);
					animator.setOutAnimation(slideOutRight);
					animator.showPrevious();
				}
			}
        });
        rightBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (animator.getDisplayedChild() == 0) {
			        title.setText("step #2");
			        leftBtn.setText("back");
					rightBtn.setText("ok");
					animator.setInAnimation(slideInLeft);
					animator.setOutAnimation(slideOutLeft);
					animator.showNext();
				} else {
					String msg = "don't be suprized, but your first name is [" +
						firstName.getText() + "] and your last name is [" +
						lastName.getText() + "]";
					new AlertDialog.Builder(TestCustomTitle.this).setMessage(msg)
						.setTitle("hmmm...")
						.setIcon(android.R.drawable.ic_dialog_info)
						.setPositiveButton("ok", null)
						.show();
				}
			}
        });
    }
}
