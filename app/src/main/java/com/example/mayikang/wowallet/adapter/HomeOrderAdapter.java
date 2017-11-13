package com.example.mayikang.wowallet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.modle.javabean.IndentBean;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;

import java.text.DecimalFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by haohaoliu on 2017/8/4.
 * explain:
 */

public class HomeOrderAdapter extends RecyclerView.Adapter<HomeOrderAdapter.HomeOrderHolder> {
    private List<IndentBean> data;
    private Context mContext;
    private LayoutInflater inflater;

    public HomeOrderAdapter(Context mContext, List<IndentBean> data) {
        this.mContext = mContext;
        this.data = data;
        this.inflater= (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public HomeOrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.bill_item,null);
        return new HomeOrderHolder(v);
    }

    @Override
    public void onBindViewHolder(HomeOrderHolder holder, int position) {
        IndentBean bean=data.get(position);
       /* PicassoUtil.getPicassoObject().load(bean.getStoreLogoUrl())
                .resize(DpUtils.dpToPx(mContext,80),DpUtils.dpToPx(mContext,80))
                .into(holder.billItemIcon);*/
        Glide.with(mContext).load(bean.getStoreLogoUrl())
                .placeholder(R.drawable.ic_defult_load).crossFade()
                .error(R.drawable.ic_defult_error)
                .into(holder.billItemIcon);
        //holder.billItemIcon.setImageResource(R.drawable.ic_fans_tc);
        holder.billItemTitle.setText(bean.getStoreName());
        holder.billItemMoney.setText("￥"+new DecimalFormat("######0.00").format(bean.getPayValue()));
        holder.billItemDay.setText(bean.getFinishTime());

    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    static  class  HomeOrderHolder extends RecyclerView.ViewHolder{
        CircleImageView billItemIcon;
        TextView billItemTitle;
        TextView billItemMoney;
        TextView billItemDay;
        TextView billItemTime;
        LinearLayout llParent;
        public HomeOrderHolder(View itemView) {
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
