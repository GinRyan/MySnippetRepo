/**=========================================================
 * 版权：联信永益 版权所有 (c) 2002 - 2003
 * 文件： com.surekam.anroidmobile.exp.apps.common.adapter.ImageAdapter
 * 所含类: ImageAdapter
 * 修改记录：
 * 日期                  作者           内容
 * =========================================================
 * 2011-4-20     xiaow
 * =========================================================*/

package hong.specialEffects.ui;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ButtonAdapter<T> extends BaseAdapter {

    LayoutInflater mInflater;
    List<T> list;
    int[] id_arr;
    int layout_id;
    int view_position = -1;
    View add_view;
    private String[] fieldnames;
    private String[] _fields;
    private Class<? extends T> t;
    ViewHolderBean holder = null;

    public ButtonAdapter(LayoutInflater mInflater, List<T> list, int[] id_arr,
            int layout_id, String[] fieldNames, Class<? extends T> t) {
        this.mInflater = mInflater;
        this.list = list;
        this.id_arr = id_arr;
        this.layout_id = layout_id;
        this.fieldnames = fieldNames;
        this.t = t;
        changes();
    }

    public void changes() {
        List<String> _list = new  LinkedList<String>();
        if(fieldnames!=null){
            _list= Arrays.asList(fieldnames);
        }
        Field[] _fFields = t.getDeclaredFields();
        List<String> _list_0 = new LinkedList<String>();
        for (Field field : _fFields) {
            if (!_list.contains(field.getName())) {
                _list_0.add(field.getName());
            }
        }
        _fields = _list_0.toArray(new String[_list_0.size()]);
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(layout_id, null);
            TextView text0 = (TextView) convertView.findViewById(id_arr[0]);
            TextView text1 = (TextView) convertView.findViewById(id_arr[1]);
            TextView text2 = (TextView) convertView.findViewById(id_arr[2]);
            TextView text3 = (TextView) convertView.findViewById(id_arr[3]);
            TextView text4 = (TextView) convertView.findViewById(id_arr[4]);
            text0.setWidth(HorListviewActivity.width / 3);
            text1.setWidth(HorListviewActivity.width / 3);
            text2.setWidth(HorListviewActivity.width / 3);
            text3.setWidth(HorListviewActivity.width / 3);
            text4.setWidth(HorListviewActivity.width / 3);
            holder = new ViewHolderBean();
            holder.setTextView01(text0);
            holder.setTextView02(text1);
            holder.setTextView03(text2);
            holder.setTextView04(text3);
            holder.setTextView05(text4);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderBean) convertView.getTag();
        }

        T _holder = list.get(position);
        setValues(_holder, holder.getTextView01(), holder.getTextView02(),
                holder.getTextView03(), holder.getTextView04(),
                holder.getTextView05());

        convertView.setTag(holder);
        return convertView;
    }

    private void setValues(T t, TextView... args) {
        if (args == null || args.length < 1)
            return;
        int _bu = 0;
        try {
            for (int i = 0; i < args.length; i++) {
                Field field = null;
                if (fieldnames != null && i < fieldnames.length) {
                    field = t.getClass().getDeclaredField(fieldnames[i]);
                } else {
                    field = t.getClass().getDeclaredField(_fields[_bu++]);
                }
                field.setAccessible(true);
                Object object = field.get(t);
                if (object == null)
                    object = "";
                args[i].setText(object.toString());
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void addView(int position, View addview) {
        this.view_position = position;
        this.add_view = addview;
    }

    public void deleteAllItem() {
        for (int i = 0; i < list.size(); i++) {
            list.remove(i);
            this.notifyDataSetChanged();
        }
    }

    public void removeItem(int position) {
        list.remove(position);
        this.notifyDataSetInvalidated();
    }

    public void changItems(List<T> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public void changeFields(String[] fieldNames) {
        this.fieldnames = fieldNames;
        this.notifyDataSetChanged();
        changes();
    }

}
