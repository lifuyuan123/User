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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by haohaoliu on 2017/8/2.
 * explain: 选择代理商套餐的适配器
 */

public class ChooseRvAdapter extends RecyclerView.Adapter<ChooseRvAdapter.ChooseRvAdapterHolder> {
    private List<AgentBean> data;
    private Context mContext;
    private LayoutInflater inflater;

    public ChooseRvAdapter(List<AgentBean> data, Context mContext) {
        this.data = data;
        this.mContext = mContext;
        this.inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public ChooseRvAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.chooseagent_item, null);
        return new ChooseRvAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(ChooseRvAdapterHolder holder, int position) {
        AgentBean bean=data.get(position);
        holder.tvCount.setText(bean.getTotNum()+"");
        holder.tvTotalPrice.setText(bean.getTotAmount()+"");
    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    static class ChooseRvAdapterHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_choose)
        ImageView ivChoose;
        @BindView(R.id.tv_total_price)
        TextView tvTotalPrice;
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.lin_onclick)
        LinearLayout linOnclick;
        public ChooseRvAdapterHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
