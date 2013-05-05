package com.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class UITutorial_V2Activity extends Activity {
	private Button _btnLayout1 = null;
	private Button _btnLayout2 = null;
	private Button _btntableLayout = null;
	private Button m_btnRadioLayout;
	private Button m_btnChk;
	Button m_btnListView;
	Button m_btnSpinner;
	Button m_btnProgressbar;
	Button m_btnDial;
	Button m_btnSms;
	Button m_btnNotification;
	Button m_btnEditTextChange;
	Button m_btnActivityLifecycly;
	Button m_btnHandler;
	Button m_btnFrameAnimation;
	Button m_btnScaleAnimation;
	Button m_btnRotateAnimation;
	Button m_btnTranslateAnimation;
	Button m_btnAlphaAnimation;
	Button m_btnAutoComplete;
	private static final int OK = 1;
	private static final int CANCEL = 2;
	private static final int ABOUT = 3;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		getViews();
		setClickListener();
	}

	private void getViews() {
		// TODO Auto-generated method stub
		_btnLayout1 = (Button) findViewById(com.example.R.id.home_btnLayout1);
		_btnLayout2 = (Button) findViewById(com.example.R.id.home_btnLayout2);
		_btntableLayout = (Button) findViewById(com.example.R.id.home_btntableLayout);
		m_btnRadioLayout = (Button) findViewById(com.example.R.id.home_btnRadioLayout);
		m_btnChk = (Button) findViewById(com.example.R.id.home_btnChk);
		m_btnListView = (Button) findViewById(com.example.R.id.home_btnListViewLayout);
		m_btnSpinner=(Button)findViewById(com.example.R.id.home_btnSpinnerLayout);
		m_btnProgressbar=(Button)findViewById(com.example.R.id.home_btnProgressbarLayout);
		m_btnDial=(Button)findViewById(com.example.R.id.home_btnDial);
		m_btnSms=(Button)findViewById(com.example.R.id.home_btnSMS);
		m_btnNotification=(Button)findViewById(com.example.R.id.home_btnNotification);
		m_btnEditTextChange=(Button)findViewById(com.example.R.id.home_btnEditTextChange);
		m_btnActivityLifecycly=(Button)findViewById(com.example.R.id.home_btnActivityLifeCycle);
		m_btnHandler=(Button)findViewById(com.example.R.id.home_btnHandler);
		m_btnFrameAnimation=(Button)findViewById(com.example.R.id.home_btnFrameAnimation);
		m_btnScaleAnimation=(Button)findViewById(com.example.R.id.home_btnScaleAnimation);
		m_btnRotateAnimation=(Button)findViewById(com.example.R.id.home_btnRotateAnimation);
		
		m_btnTranslateAnimation=(Button)findViewById(com.example.R.id.home_btnTranslateAnimation);
		m_btnAlphaAnimation=(Button)findViewById(com.example.R.id.home_btnAlphaAnimation);
		m_btnAutoComplete=(Button)findViewById(com.example.R.id.home_btnAutoComplete);
	}

	private void setClickListener() {
		// TODO Auto-generated method stub
		_btnLayout1.setOnClickListener(layout1Click());
		_btnLayout2.setOnClickListener(layout2Click());
		_btntableLayout.setOnClickListener(tablelayoutClick());
		m_btnRadioLayout.setOnClickListener(radioClick());
		m_btnChk.setOnClickListener(checkboxClick());
		m_btnListView.setOnClickListener(listViewClick());
		m_btnSpinner.setOnClickListener(spinnerClick());
		m_btnProgressbar.setOnClickListener(progressbarClick());
		m_btnDial.setOnClickListener(dialClick());
		m_btnSms.setOnClickListener(smsClick());
		m_btnNotification.setOnClickListener(notificationClick());
		m_btnEditTextChange.setOnClickListener(editTextChangeClick());
		m_btnActivityLifecycly.setOnClickListener(activityLifecycleClick());
		m_btnHandler.setOnClickListener(handlerClick());
		m_btnFrameAnimation.setOnClickListener(frameanimaitonClick());
		m_btnScaleAnimation.setOnClickListener(scaleanimationClick());
		m_btnRotateAnimation.setOnClickListener(rotateanimationClick());
		m_btnTranslateAnimation.setOnClickListener(translateanimationClick());
		m_btnAlphaAnimation.setOnClickListener(alphaanimationClick());
		m_btnAutoComplete.setOnClickListener(autocompleteClick());
	}
	View.OnClickListener autocompleteClick(){
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(
						com.example.UITutorial_V2Activity.this,
						com.example.layout.AutoCompleteTextViewActivity.class);
				startActivity(intent);
			}
		};
	}
	View.OnClickListener alphaanimationClick(){
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(
						com.example.UITutorial_V2Activity.this,
						com.example.animation.AlphaAnimationActivity.class);
				startActivity(intent);
			}
		};
	}
	View.OnClickListener translateanimationClick(){
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(
						com.example.UITutorial_V2Activity.this,
						com.example.animation.TranslateAnimationActivity.class);
				startActivity(intent);
			}
		};
	}
	View.OnClickListener rotateanimationClick(){
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(
						com.example.UITutorial_V2Activity.this,
						com.example.animation.RotateAnimationActivity.class);
				startActivity(intent);
			}
		};
	}
	View.OnClickListener scaleanimationClick(){
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(
						com.example.UITutorial_V2Activity.this,
						com.example.animation.ScaleAnimationActivity.class);
				startActivity(intent);
			}
		};
	}
	
	View.OnClickListener frameanimaitonClick(){
		return new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(
						com.example.UITutorial_V2Activity.this,
						com.example.animation.FrameAnimationActivity.class);
				startActivity(intent);
			}
		};
	}
	View.OnClickListener handlerClick() {
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						com.example.UITutorial_V2Activity.this,
						com.example.layout.HandlerActivity.class);
				startActivity(intent);
				
			}
		};
	}
	
	View.OnClickListener activityLifecycleClick() {
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						com.example.UITutorial_V2Activity.this,
						com.example.layout.ActivityLifecycle.class);
				startActivity(intent);
				
			}
		};
	}
	
	View.OnClickListener editTextChangeClick() {
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						com.example.UITutorial_V2Activity.this,
						com.example.layout.EditTextChangeActivity.class);
				startActivity(intent);
				
			}
		};
	}
	View.OnClickListener notificationClick() {
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						com.example.UITutorial_V2Activity.this,
						com.example.layout.NotificationActivity.class);
				startActivity(intent);
				
			}
		};
	}
	View.OnClickListener smsClick() {
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						com.example.UITutorial_V2Activity.this,
						com.example.layout.SmsActivity.class);
				startActivity(intent);
			}
		};
	}
	
	View.OnClickListener dialClick() {
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						com.example.UITutorial_V2Activity.this,
						com.example.layout.DialActivity.class);
				startActivity(intent);
			}
		};
	}
	View.OnClickListener progressbarClick() {
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						com.example.UITutorial_V2Activity.this,
						com.example.layout.progressbarLayoutActivity.class);
				startActivity(intent);
			}
		};
	}
	View.OnClickListener spinnerClick() {
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						com.example.UITutorial_V2Activity.this,
						com.example.layout.SpinnerLayoutActivity.class);
				startActivity(intent);
			}
		};
	}
	View.OnClickListener listViewClick() {
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						com.example.UITutorial_V2Activity.this,
						com.example.layout.ListViewLayoutActivity.class);
				startActivity(intent);
			}
		};
	}

	private View.OnClickListener checkboxClick() {
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						com.example.UITutorial_V2Activity.this,
						com.example.layout.CheckBoxLayoutActivity.class);
				startActivity(intent);
			}
		};
	}

	private View.OnClickListener radioClick() {
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						com.example.UITutorial_V2Activity.this,
						com.example.layout.RadioLayoutActivity.class);
				startActivity(intent);
			}
		};
	}

	private View.OnClickListener layout1Click() {

		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						com.example.UITutorial_V2Activity.this,
						com.example.layout.layout1Activity.class);
				startActivity(intent);
				com.example.UITutorial_V2Activity.this.finish();
			}
		};
	}

	private View.OnClickListener layout2Click() {

		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						com.example.UITutorial_V2Activity.this,
						com.example.layout.layout2Activity.class);
				Bundle bundle = new Bundle();
				bundle.putString("key1", "value1");
				intent.putExtra("key1", bundle);
				startActivity(intent);
				com.example.UITutorial_V2Activity.this.finish();
			}
		};
	}

	private View.OnClickListener tablelayoutClick() {
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						com.example.UITutorial_V2Activity.this,
						com.example.layout.tablelayoutActivity.class);
				startActivity(intent);
				com.example.UITutorial_V2Activity.this.finish();
			}
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, OK, 0, "开始");
		menu.add(0, CANCEL, 0, "取消");
		Menu file = menu.addSubMenu(0, ABOUT, 0, "关于");
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(com.example.R.menu.menu, file);
		return true;

	}

	private void displayMsg(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		displayMsg(item.getTitle().toString());

		return super.onOptionsItemSelected(item);
	}
}