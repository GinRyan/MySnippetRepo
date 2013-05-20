package hong.specialEffects.ui;

import hong.specialEffects.wight.FlingGallery;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class FlingGalleryActivity extends Activity
{
	private final int color_red = Color.argb(100, 200, 0, 0);
	private final int color_green = Color.argb(100, 0, 200, 0);
	private final int color_blue = Color.argb(100, 0, 0, 200);
	private final int color_yellow = Color.argb(100, 200, 200, 0);
	private final int color_purple = Color.argb(100, 200, 0, 200);

    private final String[] mLabelArray = {"View1", "View2", "View3", "View4", "View5"};
    private final int[] mColorArray = {color_red, color_green, color_blue, color_yellow, color_purple};

	private FlingGallery mGallery;
	private CheckBox mCheckBox;

    // Note: The following handler is critical to correct function of
    // the FlingGallery class. This enables the FlingGallery class to
    // detect when the motion event has ended by finger being lifted

    @Override
    public boolean onTouchEvent(MotionEvent event)
	{
        return mGallery.onGalleryTouchEvent(event);
    }

    @Override
	public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mGallery = new FlingGallery(this);
        mGallery.setPaddingWidth(5);
        mGallery.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, mLabelArray)
        {
        	@Override
        	public View getView(int position, View convertView, ViewGroup parent)
        	{
        		Log.d("111", "count="+position);
//            	if (convertView != null && convertView instanceof GalleryViewItem)
//                {
//            		GalleryViewItem galleryView = (GalleryViewItem) convertView;
//
//            		galleryView.mEdit1.setText("");
//            		galleryView.mText1.setText(mLabelArray[position]);
//            		galleryView.mText1.setBackgroundColor(mColorArray[position]);
//            		galleryView.mText2.setText(mLabelArray[position]);
//            		galleryView.mText2.setBackgroundColor(mColorArray[position]);
//            		
//            		Log.d("111", "count="+position);
//            		
//            		return galleryView;
//            		
//                }
            	
            	return new GalleryViewItem(getApplicationContext(), position);
        	}
        });

        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

		layoutParams.setMargins(10, 10, 10, 10);
		layoutParams.weight = 1.0f;
  
        layout.addView(mGallery, layoutParams);
	    
        mCheckBox = new CheckBox(getApplicationContext());
        mCheckBox.setText("Gallery is Circular");
        mCheckBox.setText("Gallery is Circular");
        mCheckBox.setPadding(50, 10, 0, 10);
        mCheckBox.setTextSize(30);
        mCheckBox.setChecked(true);
        mCheckBox.setOnClickListener(new OnClickListener()
        {
			@Override
			public void onClick(View view)
			{
				mGallery.setIsGalleryCircular(mCheckBox.isChecked());
			}
        });

        layout.addView(mCheckBox, new LinearLayout.LayoutParams(
	    		LayoutParams.MATCH_PARENT,
	    		LayoutParams.WRAP_CONTENT));
	    
        setContentView(layout);
    }	

	private class GalleryViewItem extends TableLayout
	{
		private EditText mEdit1;
		private TextView mText1;
		private TextView mText2;
		private Button mButton1;
		private Button mButton2;

		public GalleryViewItem(Context context, int position)
		{
			super(context);

			this.setOrientation(LinearLayout.VERTICAL);

    		this.setLayoutParams(new LinearLayout.LayoutParams(
    				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
    				android.view.ViewGroup.LayoutParams.MATCH_PARENT));
    		
    		mEdit1 = new EditText(context);

    		this.addView(mEdit1, new LinearLayout.LayoutParams(
            		android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            		android.view.ViewGroup.LayoutParams.WRAP_CONTENT)); 

            mText1 = new TextView(context);
            mText1.setText(mLabelArray[position]);
            mText1.setTextSize(30);
            mText1.setGravity(Gravity.LEFT);
            mText1.setBackgroundColor(mColorArray[position]);

    		this.addView(mText1, new LinearLayout.LayoutParams(
            		android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            		android.view.ViewGroup.LayoutParams.WRAP_CONTENT)); 

            mButton1 = new Button(context);
            mButton1.setText("<<");
            mButton1.setGravity(Gravity.LEFT);
            mButton1.setOnClickListener(new OnClickListener()
            {
				@Override
				public void onClick(View view)
				{
					mGallery.movePrevious();
				}
            });
            
            this.addView(mButton1, new LinearLayout.LayoutParams(
            		android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            		android.view.ViewGroup.LayoutParams.WRAP_CONTENT)); 

            mButton2 = new Button(context);
            mButton2.setText(">>");
            mButton2.setGravity(Gravity.RIGHT);
            mButton2.setOnClickListener(new OnClickListener()
            {
				@Override
				public void onClick(View view)
				{
					mGallery.moveNext();
				}
            });
            
            this.addView(mButton2, new LinearLayout.LayoutParams(
            		android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            		android.view.ViewGroup.LayoutParams.WRAP_CONTENT)); 

            mText2 = new TextView(context);
            mText2.setText(mLabelArray[position]);
    		mText2.setTextSize(30);
    		mText2.setGravity(Gravity.RIGHT);
    		mText2.setBackgroundColor(mColorArray[position]);

    		this.addView(mText2, new LinearLayout.LayoutParams(
            		android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            		android.view.ViewGroup.LayoutParams.MATCH_PARENT, 1)); 
		}
	}
}
