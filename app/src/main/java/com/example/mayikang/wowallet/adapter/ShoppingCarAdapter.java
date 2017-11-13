package com.example.mayikang.wowallet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class ShoppingCarAdapter extends RecyclerView.Adapter<ShoppingCarAdapter.MyViewHolder> {

    private List<Store2Bean> list = new ArrayList<>();
    private Context context;

    public boolean isdelete() {
        return isdelete;
    }

    public void setIsdelete(boolean isdelete) {
        this.isdelete = isdelete;
    }

    private boolean isdelete=false;

    public ShoppingCarAdapter(List<Store2Bean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.shoppingcar_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int i) {
        final Store2Bean bean = list.get(i);


        holder.shoppingItemCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bean.setIscheck(isChecked);
            }
        });

        holder.shoppingItemCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callBack!=null){
                    callBack.onItemCkeckBox(i);
                }
            }
        });

        if(bean.ischeck()){
            holder.shoppingItemCheck.setChecked(true);
        }else {
            holder.shoppingItemCheck.setChecked(false);
        }

//        Glide.with(context).
//                load(bean.getIconUrl())
//                .placeholder(R.drawable.ic_defult_load).crossFade()
//                .error(R.mipmap.icon_load_faild)
//                .into(myViewHolder.store2IvIcon);

        if(isdelete){
            holder.shoppingLinAddReduce.setVisibility(View.VISIBLE);
            holder.shoppingTvCountAfter.setVisibility(View.GONE);
        }else {
            holder.shoppingTvCountAfter.setVisibility(View.VISIBLE);
            holder.shoppingLinAddReduce.setVisibility(View.GONE);
        }



        holder.shoppingIvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=bean.getCount()+1;
                bean.setCount(count);
                holder.shoppingTvCount.setText(bean.getCount()+"");
                if(bean.getCount()>=1){
                    holder.shoppingIvReduce.setVisibility(View.VISIBLE);
                }

            }
        });

        holder.shoppingIvReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bean.getCount()>1){
                    int count=bean.getCount()-1;
                    bean.setCount(count);
                    holder.shoppingTvCount.setText(bean.getCount()+"");
                }else {
                    holder.shoppingIvReduce.setVisibility(View.GONE);
                }
            }
        });






    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.shopping_item_check)
        CheckBox shoppingItemCheck;
        @BindView(R.id.shopping_iv_icon)
        ImageView shoppingIvIcon;
        @BindView(R.id.shopping_tv_title)
        TextView shoppingTvTitle;
        @BindView(R.id.shopping_tv_price)
        TextView shoppingTvPrice;
        @BindView(R.id.shopping_tv_count_after)
        TextView shoppingTvCountAfter;
        @BindView(R.id.shopping_iv_reduce)
        ImageView shoppingIvReduce;
        @BindView(R.id.shopping_tv_count)
        TextView shoppingTvCount;
        @BindView(R.id.shopping_iv_add)
        ImageView shoppingIvAdd;
        @BindView(R.id.shopping_lin_add_reduce)
        LinearLayout shoppingLinAddReduce;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


    public void choiceAll(){
        for (int i = 0; i < list.size(); i++) {
            if(!list.get(i).ischeck()){
                list.get(i).setIscheck(true);
            }
        }
    }

    public void cancleAll(){
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).ischeck()){
                list.get(i).setIscheck(false);
            }
        }
    }

    public interface CallBack{
        void onItemCkeckBox(int position);
    }

    private CallBack callBack;

    public CallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
}
