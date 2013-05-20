package hong.specialEffects.ui;

import hong.specialEffects.R;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class CustomSpinnerActivity extends Activity {
    public Spinner spinner;

    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spiner_main);

        Resources res = getResources();
        CharSequence[] platforms = res.getTextArray(R.array.anim_type);

        spinner = (Spinner) findViewById(R.id.custom_spiner);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, platforms) {
        	
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
            	
                View view = getLayoutInflater().inflate(R.layout.my_spiner, parent, false);
                
                TextView label = (TextView) view.findViewById(R.id.label);
                label.setText(getItem(position));

                if (spinner.getSelectedItemPosition() == position) {
                    label.setTextColor(getResources().getColor(R.color.blue));
                    view.setBackgroundColor(getResources().getColor(R.color.green));
                    view.findViewById(R.id.icon).setVisibility(View.VISIBLE);
                }

                return view;
            }
        };
        spinner.setAdapter(adapter);
    }

}