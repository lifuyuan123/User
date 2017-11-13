package com.example.mayikang.wowallet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.modle.javabean.RegionInfo;

import java.util.List;

/**
 * Created by lifuy on 2017/9/13.
 */

public class MyGridViewAdapter extends MyBaseAdapter<RegionInfo, GridView> {
    private LayoutInflater inflater;
    public MyGridViewAdapter(Context ct, List<RegionInfo> list) {
        super(ct, list);
        inflater = LayoutInflater.from(ct);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_remen_city, parent,false);
            holder.id_tv_cityname = (TextView) convertView.findViewById(R.id.id_tv_cityname);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        RegionInfo info = list.get(position);
        holder.id_tv_cityname.setText(info.getName());
        return convertView;
    }
    class ViewHolder{
        TextView id_tv_cityname;
    }
}
