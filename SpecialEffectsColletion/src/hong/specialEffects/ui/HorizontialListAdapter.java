package hong.specialEffects.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class HorizontialListAdapter extends BaseAdapter{
	private String[] dataObjects = null;
	private int resource;
	
	public HorizontialListAdapter(String[] dataObjects,int resource){
		this.dataObjects=dataObjects;
		this.resource=resource;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataObjects.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View retval = LayoutInflater.from(parent.getContext()).inflate(resource, null);
		/*TextView title = (TextView) retval.findViewById(R.id.title);
		title.setText(dataObjects[position]);*/
		return retval;
	}
	
}