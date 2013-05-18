package hong.specialEffects.ui;

import hong.specialEffects.R;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class Animation_BeginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animation_main);

		final Spinner sprAnim = (Spinner) findViewById(R.id.sprAnim);
		String[] ls = getResources().getStringArray(R.array.anim_type);
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < ls.length; i++) {
			list.add(ls[i]);
		}
		ArrayAdapter<String> animType = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		animType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sprAnim.setAdapter(animType);
		sprAnim.setSelection(0);

		Button btn = (Button) findViewById(R.id.btn_next);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it = new Intent(Animation_BeginActivity.this, Animation_TargetActivity.class);
				startActivityForResult(it, 0);

				switch (sprAnim.getSelectedItemPosition()) {
					case 0:
						overridePendingTransition(R.anim.fade, R.anim.hold);
						break;
					case 1:
						overridePendingTransition(R.anim.my_scale_action,
								R.anim.my_alpha_action);
						break;
					case 2:
						overridePendingTransition(R.anim.scale_rotate,
								R.anim.my_alpha_action);
						break;
					case 3:
						overridePendingTransition(R.anim.scale_translate_rotate,
								R.anim.my_alpha_action);
						break;
					case 4:
						overridePendingTransition(R.anim.scale_translate,
								R.anim.my_alpha_action);
						break;
					case 5:
						overridePendingTransition(R.anim.hyperspace_in,
								R.anim.hyperspace_out);
						break;
					case 6:
						overridePendingTransition(R.anim.push_left_in,
								R.anim.push_left_out);
						break;
					case 7:
						overridePendingTransition(R.anim.push_up_in,
								R.anim.push_up_out);
						break;
					case 8:
						overridePendingTransition(R.anim.slide_left,
								R.anim.slide_right);
						break;
					case 9:
						overridePendingTransition(R.anim.wave_scale,
								R.anim.my_alpha_action);
						break;
					case 10:
						overridePendingTransition(R.anim.zoom_enter,
								R.anim.zoom_exit);
						break;
					case 11:
						overridePendingTransition(R.anim.slide_up_in,
								R.anim.slide_down_out);
						break;
				}
			}
		});
	}
}
