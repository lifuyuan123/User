package com.example.mayikang.wowallet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.modle.javabean.AgentBean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lifuy on 2017/8/2.
 */

public class ChooseAgAdapter extends RecyclerView.Adapter<ChooseAgAdapter.MyViewHolder>{
    private List<AgentBean> data=new ArrayList<>();
    private Context context;

    public ChooseAgAdapter(List<AgentBean> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.chooseagent_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final AgentBean bean=data.get(position);
        if(bean.isCheck()){
            holder.iv_choose.setVisibility(View.VISIBLE);
        }else {
            holder.iv_choose.setVisibility(View.INVISIBLE);
        }

        holder.lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清除选择
                if(bean.isCheck()){
                    holder.iv_choose.setVisibility(View.INVISIBLE);
                    bean.setCheck(false);
                }else {
                    clearChooseAgent();
                    bean.setCheck(true);
                    holder.iv_choose.setVisibility(View.VISIBLE);
                }
            }
        });
        holder.tv_total_price.setText("￥"+new DecimalFormat("######0.00").format(bean.getAllValue()));
        holder.tv_conut.setText("可购买"+bean.getTotNum()+"个店铺");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class  MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv_choose;
        private TextView tv_total_price;
        private TextView tv_conut;
        private LinearLayout lin;

        public MyViewHolder(View itemView) {
            super(itemView);
            lin= (LinearLayout) itemView.findViewById(R.id.lin_onclick);
            iv_choose= (ImageView) itemView.findViewById(R.id.iv_choose);
            tv_conut= (TextView) itemView.findViewById(R.id.tv_count);
            tv_total_price= (TextView) itemView.findViewById(R.id.tv_total_price);
        }
    }

    private void clearChooseAgent(){
        for (int i = 0; i <data.size(); i++) {
            if(data.get(i).isCheck()){
                data.get(i).setCheck(false);
            }
        }
        notifyDataSetChanged();
    }

    public int getChooseBeanId(){
        int id=-1;
        for (int i = 0; i <data.size(); i++) {
            if(data.get(i).isCheck()){
                id=data.get(i).getId();
            }
        }
        return id;
    }

    public List<AgentBean> getAdapterData(){
        List<AgentBean> list=new ArrayList<>();
        for (int i = 0; i <data.size() ; i++) {
            AgentBean bean=data.get(i);
            if(bean.isCheck()){
                list.add(bean);
            }
        }
        return list;
    }
}
