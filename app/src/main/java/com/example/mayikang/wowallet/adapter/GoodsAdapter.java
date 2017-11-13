package com.example.mayikang.wowallet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.modle.javabean.GoodsBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lifuy on 2017/9/29.
 */

public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.MyViewHolder> {

    private List<GoodsBean> list = new ArrayList<>();
    private Context context;

    public GoodsAdapter(List<GoodsBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.order_item_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_iv_goods_icon)
        ImageView itemIvGoodsIcon;
        @BindView(R.id.item_tv_goods_name)
        TextView itemTvGoodsName;
        @BindView(R.id.item_tv_goods_price)
        TextView itemTvGoodsPrice;
        @BindView(R.id.item_tv_goods_color)
        TextView itemTvGoodsColor;
        @BindView(R.id.item_tv_goods_count)
        TextView itemTvGoodsCount;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
