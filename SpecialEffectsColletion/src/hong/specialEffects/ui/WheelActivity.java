package hong.specialEffects.ui;

import hong.specialEffects.R;
import hong.specialEffects.wheel.OnWheelChangedListener;
import hong.specialEffects.wheel.OnWheelScrollListener;
import hong.specialEffects.wheel.WheelView;
import hong.specialEffects.wheel.adapter.AbstractWheelTextAdapter;
import hong.specialEffects.wheel.adapter.ArrayWheelAdapter;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class WheelActivity extends Activity {
    // Scrolling flag
    private boolean scrolling = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.wheel);
                
        final WheelView country = (WheelView) findViewById(R.id.country);
        country.setVisibleItems(3);
        country.setViewAdapter(new CountryAdapter(this));
        //country.setCyclic(true);
        
        final String cities[][] = new String[][] {
                        new String[] {"长生", "不老", "不灭", "无法", "无天"},
                        new String[] {"昆明", "乌鲁木齐", "宁夏", "桂林", "合肥"},
                        new String[] {"厦门", "香港", "澳门", "福州"},
                        new String[] {"成都", "重庆", "广州", "北京","杭州","上海","东京","纽约"},
                        new String[] {"华盛顿", "莫斯科", "伦敦", "圣彼得堡", "渥太华"},
                        new String[] {"天津", "呼和浩特", "台北", "香港", "唐山"},
                        new String[] {"成都", "重庆", "广州", "北京","杭州","上海","东京","纽约"},
                        };
        
        final WheelView city= (WheelView) findViewById(R.id.city);
        city.setVisibleItems(5);
        country.addChangingListener(new OnWheelChangedListener() {
                        @Override
						public void onChanged(WheelView wheel, int oldValue, int newValue) {
                            if (!scrolling) {
                                updateCities(city, cities, newValue);
                                Log.i("Change",newValue+"");
                            }
                        }
                });
        
        country.addScrollingListener( new OnWheelScrollListener() {
            @Override
			public void onScrollingStarted(WheelView wheel) {
                scrolling = true;
            }
            @Override
			public void onScrollingFinished(WheelView wheel) {
                scrolling = false;
                Log.i("onScrollingFinished",wheel.getCurrentItem()+"");
                updateCities(city, cities, country.getCurrentItem());
            }
        });

        country.setCurrentItem(1);
        
    }
    
    /**
     * Updates the city wheel
     */
    private void updateCities(WheelView city, String cities[][], int index) {
        ArrayWheelAdapter<String> adapter =
            new ArrayWheelAdapter<String>(this, cities[index]);
        adapter.setTextSize(20);
        city.setViewAdapter(adapter);
        city.setCurrentItem(cities[index].length / 2);        
    }
    
    /**
     * Adapter for countries
     */
    private class CountryAdapter extends AbstractWheelTextAdapter {
        // Countries names
        private String countries[] =
            new String[] {"中国", "中国", "中国", "中国","中国","中国","中国"};
        // Countries flags
        private int flags[] =
            new int[] {R.drawable.usa,
        		R.drawable.usa,
        		R.drawable.usa,
        		R.drawable.usa, 
        		R.drawable.canada, 
        		R.drawable.ukraine,
        		R.drawable.france};
        
        /**
         * Constructor
         */
        protected CountryAdapter(Context context) {
            super(context, R.layout.country_layout, NO_RESOURCE);
            
           // setItemTextResource(R.id.country_name);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
           
            ImageView img = (ImageView) view.findViewById(R.id.flag);
            img.setImageResource(flags[index]);
            return view;
        }
        
        @Override
        public int getItemsCount() {
            return countries.length;
        }
        
        @Override
        protected CharSequence getItemText(int index) {
            return countries[index];
        }
    }
}