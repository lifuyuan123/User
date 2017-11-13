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
import com.example.mayikang.wowallet.modle.javabean.Store2Bean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lifuy on 2017/9/25.
 */

public class Store2Adapter extends RecyclerView.Adapter<Store2Adapter.MyViewHolder> {

    private List<Store2Bean> lists = new ArrayList<>();
    private Context context;

    public Store2Adapter(List<Store2Bean> lists, Context context) {
        this.lists = lists;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.store2_goods_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, int i) {
        final Store2Bean bean = lists.get(i);
//        Glide.with(context).
//                load(bean.getIconUrl())
//                .placeholder(R.drawable.ic_defult_load).crossFade()
//                .error(R.mipmap.icon_load_faild)
//                .into(myViewHolder.store2IvIcon);


        myViewHolder.store2IvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = bean.getCount() + 1;
                bean.setCount(count);
                myViewHolder.store2TvCount.setText(count + "");
                if (bean.getCount() >= 1) {
                    myViewHolder.store2IvReduce.setVisibility(View.VISIBLE);
                }
            }
        });

        myViewHolder.store2IvReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bean.getCount()>1){
                    int count = bean.getCount() - 1;
                    bean.setCount(count);
                    myViewHolder.store2TvCount.setText(count + "");
                }else {
                    myViewHolder.store2IvReduce.setVisibility(View.GONE);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.store2_iv_icon)
        ImageView store2IvIcon;
        @BindView(R.id.store2_tv_title)
        TextView store2TvTitle;
        @BindView(R.id.store2_tv_freeshipping)
        TextView store2TvFreeshipping;
        @BindView(R.id.store2_tv_money)
        TextView store2TvMoney;
        @BindView(R.id.store2_iv_reduce)
        ImageView store2IvReduce;
        @BindView(R.id.store2_tv_count)
        TextView store2TvCount;
        @BindView(R.id.store2_iv_add)
        ImageView store2IvAdd;
        @BindView(R.id.lin_store2_item)
        LinearLayout linStore2Item;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
