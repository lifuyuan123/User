package com.example.mayikang.wowallet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.modle.javabean.Data;
import com.example.mayikang.wowallet.modle.javabean.DetailBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lifuyuan on 2017/5/10.
 */

public class MyDetailAdapter extends BaseAdapter {
    private List<DetailBean> list;
    private Context context;

    public void setList(List<DetailBean> list) {
        this.list = list;
    }

    public MyDetailAdapter(List<DetailBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
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
            convertView= LayoutInflater.from(context).inflate(R.layout.detail_list_item,parent,false);
            holder.time= (TextView) convertView.findViewById(R.id.item_tv_time);
            holder.money= (TextView) convertView.findViewById(R.id.item_tv_money);
            holder.type= (TextView) convertView.findViewById(R.id.item_tv_type);
            convertView.setTag(holder);
        }else {
            holder= (MyViewHolder) convertView.getTag();
        }
        DetailBean bean= list.get(position);

        holder.type.setText(bean.getInfo());
        holder.time.setText(bean.getInsertTime()+"");
        int type= bean.getType();
        if(type==1){
            holder.money.setText("+"+bean.getAmount()+"");
        }else  if(type==2){
            holder.money.setText("-"+bean.getAmount()+"");
        }
        return convertView;
    }

    class MyViewHolder{
        TextView time,money,type;
    }
}
