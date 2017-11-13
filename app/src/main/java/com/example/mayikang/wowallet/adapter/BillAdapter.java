package com.example.mayikang.wowallet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.modle.javabean.BillBean;
import com.example.mayikang.wowallet.modle.javabean.ExpendUserBean;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;

import java.text.DecimalFormat;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by liuha on 2017/5/12.
 */

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.XViewHolder> {
    private List<BillBean> data;
    private LayoutInflater inflater;
    private Context mContext;

    public BillAdapter(Context mContext, List<BillBean> data) {
        this.mContext = mContext;
        this.data = data;
        inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public XViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.bill_item,null);

        return new XViewHolder(v);
    }

    @Override
    public void onBindViewHolder(XViewHolder holder, final int position) {
        //跳转账单详情
        holder.llParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/main/act/user/bill_detail").withInt("id",data.get(position).getId()).navigation();
            }
        });


        //1-收入  2-支出,对比本地用户 id
        if(1==data.get(position).getfType()){
            ExpendUserBean eub=data.get(position).getExpend();
           if(null!=eub){
              /* PicassoUtil.getPicassoObject().load(data.get(position).getExpend().getLogo()).
                       resize(DpUtils.dpToPx(mContext,60),DpUtils.dpToPx(mContext,60)).
                       error(R.mipmap.icon_load_faild).into(holder.billItemIcon);*/
               Glide.with(mContext).load(data.get(position).getExpend().getLogo())
                       .placeholder(R.drawable.ic_defult_load).crossFade()
                       .error(R.drawable.ic_defult_error)
                       .into(holder.billItemIcon);

           }else {
              /* PicassoUtil.getPicassoObject().load(R.drawable.ic_share).
                       resize(DpUtils.dpToPx(mContext,60),DpUtils.dpToPx(mContext,60)).
                       error(R.mipmap.icon_load_faild).into(holder.billItemIcon);*/
               Glide.with(mContext).load(R.drawable.ic_share)
                       .placeholder(R.drawable.ic_defult_load).crossFade()
                       .error(R.drawable.ic_defult_error)
                       .into(holder.billItemIcon);

           }
            holder.billItemMoney.setText("+"+new DecimalFormat("######0.00").format(data.get(position).getAmount()));
        }else if(2==data.get(position).getfType()){
            /*PicassoUtil.getPicassoObject().load(data.get(position).getIncome().getLogo()).
                    resize(DpUtils.dpToPx(mContext,60),DpUtils.dpToPx(mContext,60)).
                    error(R.mipmap.icon_load_faild).into(holder.billItemIcon);*/
            Glide.with(mContext).load(data.get(position).getIncome().getLogo())
                    .placeholder(R.drawable.ic_defult_load).crossFade()
                    .error(R.drawable.ic_defult_error)
                    .into(holder.billItemIcon);

            holder.billItemMoney.setText("-"+new DecimalFormat("######0.00").format(data.get(position).getAmount()));
        }

        holder.billItemTitle.setText(data.get(position).getDesc());
        holder.billItemDay.setText(data.get(position).getInsertTime());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data==null?10:data.size();

    }




     class XViewHolder extends RecyclerView.ViewHolder{

        CircleImageView billItemIcon;
        TextView billItemTitle;
        TextView billItemMoney;
        TextView billItemDay;
        TextView billItemTime;
         LinearLayout llParent;
        public XViewHolder(View itemView) {
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
