package hong.specialEffects.ui;

import hong.specialEffects.wight.PlasmaView;
import android.app.Activity;
import android.os.Bundle;


public class PlasmaActivity extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(new PlasmaView(this));
	}
}
