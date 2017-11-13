package com.example.mayikang.wowallet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mayikang.wowallet.R;
import com.sctjsj.basemodule.base.db.entity.SearchRecordTbl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lifuyuan on 2017/5/11.
 */

public class SearchAdapter extends BaseAdapter {
    private List<SearchRecordTbl> list=new ArrayList<>();
    private Context context;

    public SearchAdapter(List<SearchRecordTbl> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder=null;
        if(convertView==null){
            holder=new MyViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.search_item,parent,false);
            holder.content= (TextView) convertView.findViewById(R.id.tv_content);
            holder.time= (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        }else {
            holder= (MyViewHolder) convertView.getTag();
        }
        holder.content.setText(list.get(position).getContent());
        long time=list.get(position).getInsertTime();
        SimpleDateFormat formatter =new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        Date date=new Date(time);
        String dates=formatter.format(date);
        holder.time.setText(dates);
        return convertView;
    }

    class MyViewHolder{
        TextView content,time;
    }
    public void movedata(SearchRecordTbl  s){
        list.remove(s);
        notifyDataSetChanged();
    }
    public void moveAlldata(){
        list.clear();
        notifyDataSetChanged();
    }
}
