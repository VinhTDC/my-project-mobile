package vn.edu.tdc.doan_d2;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MonAn_Adapter extends BaseAdapter {
//
    private Context context;
    private int layout;
    private List<MonAn> monAnList;

    //constructor

    public MonAn_Adapter(Context context, int layout, List<MonAn> monAnList) {
        this.context = context;
        this.layout = layout;
        this.monAnList = monAnList;
    }

    @Override
    public int getCount() {
        return monAnList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHoder {
        TextView txtTenMonAn;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoder holder;
        if(convertView == null) {
            holder = new ViewHoder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder.txtTenMonAn = (TextView) convertView.findViewById(R.id.textTenMonAn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHoder) convertView.getTag();
        }
        MonAn monAn = monAnList.get(position);
        holder.txtTenMonAn.setText(monAn.getTenMonAn());

        return convertView;
    }
}
