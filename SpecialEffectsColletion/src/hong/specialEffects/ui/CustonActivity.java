package hong.specialEffects.ui;

import hong.specialEffects.wight.NinePointLineView;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class CustonActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        View v = new NinePointView(MyCustomViewActivity.this);
        View v = new NinePointLineView(CustonActivity.this);
        setContentView(v);
    }
}