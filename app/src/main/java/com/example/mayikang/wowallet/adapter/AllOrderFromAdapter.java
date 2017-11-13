package com.example.mayikang.wowallet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.modle.javabean.IndentBean;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;

import java.text.DecimalFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by haohaoliu on 2017/7/10.
 * explain:首页累计消费得adapter
 */

public class AllOrderFromAdapter extends RecyclerView.Adapter<AllOrderFromAdapter.AllOrderFromHolder> {
    private List<IndentBean> data;
    private Context mContext;
    private LayoutInflater inflater;
    private int type;

    public AllOrderFromAdapter(Context mContext, List<IndentBean> data,int type) {
        this.mContext = mContext;
        this.data = data;
        this.type=type;
        this.inflater= (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public AllOrderFromHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.bill_item,null);
        return new AllOrderFromHolder(v);
    }

    @Override
    public void onBindViewHolder(AllOrderFromHolder holder, int position) {
        IndentBean bean=data.get(position);
        switch (bean.getType()){
            case 1:
                holder.billItemIcon.setImageResource(R.drawable.ic_fans_tc);
                holder.billItemTitle.setText("\""+bean.getName()+"\""+"下单"+new DecimalFormat("######0.00").format(bean.getPostAmount())+"提成");
                holder.billItemMoney.setText("￥"+new DecimalFormat("######0.00").format(bean.getPayValue()));
                holder.billItemDay.setText(bean.getFinishTime());
                break;
            case 2:
                holder.billItemIcon.setImageResource(R.drawable.ic_fanli_icon);
                holder.billItemTitle.setText(bean.getStoreName());
                holder.billItemMoney.setText("￥"+new DecimalFormat("######0.00").format(bean.getPostAmount()));
                holder.billItemDay.setText(bean.getInsertTime());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class AllOrderFromHolder extends RecyclerView.ViewHolder{
        CircleImageView billItemIcon;
        TextView billItemTitle;
        TextView billItemMoney;
        TextView billItemDay;
        TextView billItemTime;
        LinearLayout llParent;
        public AllOrderFromHolder(View itemView) {
            super(itemView);
            billItemIcon= (CircleImageView) itemView.findViewById(R.id.bill_item_icon);
            billItemTitle= (TextView) itemView.findViewById(R.id.bill_item_title);
            billItemMoney= (TextView) itemView.findViewById(R.id.bill_item_Money);
            billItemDay= (TextView) itemView.findViewById(R.id.bill_item_day);
            billItemTime= (TextView) itemView.findViewById(R.id.bill_item_time);
            llParent= (LinearLayout) itemView.findViewById(R.id.item_bill_rl_parent);
        }
    }
}
