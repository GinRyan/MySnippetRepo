package hong.specialEffects.ui;

import hong.specialEffects.R;

import java.util.LinkedList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class HorListviewActivity extends Activity {
    /** Called when the activity is first created. */
 

//    private Gallery gallery;
    private ListView listView;
    public static  int  width = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hor_listview_main);
        init();
    }
    private void init(){
        Display dis = getWindowManager().getDefaultDisplay();
        width=  dis.getWidth();
        listView = (ListView)findViewById(R.id.list1);
        LinkedList<MyLog> mylog = new LinkedList<MyLog>();
        for (int i = 0; i < 20; i++) {
            MyLog _mLog = new MyLog("name0_"+i,"name1_"+i,"name2_"+i,"name3_"+i,"name4_"+i);
            mylog.add(_mLog);
        }
        ButtonAdapter<MyLog> _log = new ButtonAdapter<MyLog>(getLayoutInflater(), mylog,
                new int[]{R.id.textView1,R.id.textView2,R.id.textView3,R.id.textView4,R.id.textView5}, 
                R.layout.items, null, MyLog.class);
        
        LinearLayout layout = (LinearLayout)getLayoutInflater().inflate(R.layout.items, null);
        int size = layout.getChildCount();
        for (int i = 0; i < size; i++) {
            TextView textView =(TextView)layout.getChildAt(i);
            textView.setText("¿ÓµùµÄ±êÌâ"+i);
        }
        listView.addHeaderView(layout);
      
        listView.setAdapter(_log);
    }
    
    
}