package com.example.mayikang.wowallet.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.modle.javabean.OrderTypeBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lifuy on 2017/9/28.
 */

public class AllOrderAdapter extends RecyclerView.Adapter<AllOrderAdapter.MyViewHolder> {

    private List<OrderTypeBean> list = new ArrayList<>();
    private Context context;
    private LinearLayoutManager manager;
    private GoodsAdapter adapter;



    public AllOrderAdapter(List<OrderTypeBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        switch (list.get(position).getType()){
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
        }
        return -1;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.allorder_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        int type=getItemViewType(position);

        manager=new LinearLayoutManager(context);
        adapter=new GoodsAdapter(list.get(position).getGoodsBeanList(),context);
        holder.orderItemRv.setLayoutManager(manager);
        holder.orderItemRv.setAdapter(adapter);

        switch (type){
            case 1:
                holder.itemTvPayStatus.setText("等待卖家付款");
                break;
            case 2:
                holder.itemTvPayStatus.setText("支付完成");
                holder.itemTvPay.setBackgroundResource(R.drawable.order_text_cancel_background);
                holder.itemTvPay.setTextColor(Color.parseColor("#919191"));
                holder.itemTvPay.setText("查看物流");
                holder.itemTvCancel.setText("删除订单");
                break;
            case 3:
                holder.itemTvPayStatus.setText("交易失败");
                holder.itemLinBottom.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_iv_store_icon)
        ImageView itemIvStoreIcon;
        @BindView(R.id.item_tv_store_name)
        TextView itemTvStoreName;
        @BindView(R.id.order_detail_lay_b_GotoStore)
        LinearLayout orderDetailLayBGotoStore;
        @BindView(R.id.item_tv_pay_status)
        TextView itemTvPayStatus;
        @BindView(R.id.order_item_rv)
        RecyclerView orderItemRv;
        @BindView(R.id.item_tv_all_goods_count)
        TextView itemTvAllGoodsCount;
        @BindView(R.id.item_tv_all_goods_price)
        TextView itemTvAllGoodsPrice;
        @BindView(R.id.item_tv_cancel)
        TextView itemTvCancel;
        @BindView(R.id.item_tv_pay)
        TextView itemTvPay;
        @BindView(R.id.item_lin_bottom)
        LinearLayout itemLinBottom;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
