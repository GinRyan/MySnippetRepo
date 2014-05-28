package com.example.deannota;

import java.lang.reflect.Field;

import com.example.deannota.anot.ViewClick;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class AnnotationActivity extends Activity {
	protected Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
	}

	protected void inject() {
		Field[] fields = this.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			ViewClick viewc = fields[i].getAnnotation(ViewClick.class);
			int id = viewc.targetId();
			fields[i].setAccessible(true);
			try {
				View view = (View) fields[i].get(this);
				view.setOnClickListener(new Click(id));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
	}

	class Click implements OnClickListener {
		private int id;

		public Click(int id) {
			this.id = id;
		}

		@Override
		public void onClick(View v) {
			Toast.makeText(context, "Click!!!! " + id, Toast.LENGTH_SHORT).show();
		}

	}
}
