package hong.specialEffects.wight;

import java.util.List;

import android.content.Context;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class CustomRadioGroup extends RadioGroup{

	public CustomRadioGroup(Context context) {
		super(context);
	}
	
	
	public CustomRadioGroup(Context context,List<String> list) {
		super(context);
		RadioButton radio=null;
		for(String item:list){
			radio=new RadioButton(context);
			radio.setText(item);
			this.addView(radio);
		}
	}
	
}
