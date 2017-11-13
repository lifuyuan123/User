package com.example.mayikang.wowallet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.modle.javabean.IndentBean;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by liuha on 2017/5/16.
 */

public class IndentAllAdapter extends RecyclerView.Adapter<IndentAllAdapter.IndentAllViewHolder> {
    private LayoutInflater inflater;
    private Context mContext;
    private List<IndentBean> data;
    private IndentAllAdapterCallBack callBack;

    public IndentAllAdapter(Context mContext, List<IndentBean> data) {
        this.mContext = mContext;
        this.data = data;
        inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public IndentAllViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        switch (viewType){
            case 1:
                view= inflater.inflate(R.layout.indent_rebate_item, null);
                break;
            case 2:
                view= inflater.inflate(R.layout.indent_wait_item, null);
                break;
        }
        return new IndentAllViewHolder(view,viewType);
    }

    @Override
    public int getItemViewType(int position) {
        IndentBean bean = data.get(position);
        if(bean.getRebateFlag()==1){
            return 1;//返利
        }else{
            return 2;//不返利
        }
    }

    @Override
    public void onBindViewHolder(IndentAllViewHolder holder, final int position) {
        int type=getItemViewType(position);
        final IndentBean bean=data.get(position);
        switch (type){
            case 1:
             if(bean.getType()==1){
                /* PicassoUtil.getPicassoObject().load(bean.getStoreLogoUrl())
                         .resize(DpUtils.dpToPx(mContext,80),DpUtils.dpToPx(mContext,80))
                         .into(holder.indent_rebate_item_Img);*/
                 Glide.with(mContext).load(bean.getStoreLogoUrl())
                         .placeholder(R.drawable.ic_defult_load).crossFade()
                         .error(R.drawable.ic_defult_error)
                         .into(holder.indent_rebate_item_Img);

                 holder.indent_rabate_item_Title.setText(bean.getStoreName());
             }else {
                 holder.indent_rebate_item_Img.setImageResource(R.drawable.ic_share);
                 holder.indent_rabate_item_Title.setText(bean.getBuyerRemark());
             }
                holder.indent_rebate_item_date.setText(bean.getFinishTime());
                holder.indent_rebate_item_money.setText("￥"+new DecimalFormat("######0.00").format(bean.getPayValue()));
                holder.indent_rebate_item_rebate.setText("返利￥"+new DecimalFormat("######0.00").format(bean.getPostAmount()));
                if(bean.getIsEva()==1){
                    holder.indent_wait_item_comment.setVisibility(View.VISIBLE);
                }else if(bean.getIsEva()==2){
                    holder.indent_wait_item_comment.setVisibility(View.GONE);
                }
                holder.indent_wait_item_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(null!=callBack){
                           callBack.comment(position,bean.getId());
                        }
                    }
                });
                holder.indent_rebate_item_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(callBack!=null){
                            callBack.itemOnClick(position);
                        }
                    }
                });

                break;
            case 2:
                if(bean.getType()==1){
                    /*PicassoUtil.getPicassoObject().load(bean.getStoreLogoUrl())
                            .resize(DpUtils.dpToPx(mContext,80),DpUtils.dpToPx(mContext,80))
                            .into(holder.indent_wait_item_Img);*/
                    Glide.with(mContext).load(bean.getStoreLogoUrl())
                            .placeholder(R.drawable.ic_defult_load).crossFade()
                            .error(R.drawable.ic_defult_error)
                            .into(holder.indent_wait_item_Img);


                    holder.indent_wait_item_Title.setText(bean.getStoreName());
                }else {
                    holder.indent_wait_item_Img.setImageResource(R.drawable.ic_share);
                    holder.indent_wait_item_Title.setText(bean.getBuyerRemark());
                }
                holder.indent_wait_item_money.setText("￥"+new DecimalFormat("######0.00").format(bean.getPayValue()));
                holder.indent_wait_item_date.setText(bean.getFinishTime());
                if(bean.getIsEva()==1){
                    holder.indent_wait_item_comments.setVisibility(View.VISIBLE);
                }else if(bean.getIsEva()==2){
                    holder.indent_wait_item_comments.setVisibility(View.GONE);
                }

                holder.indent_wait_item_comments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(null!=callBack){
                            callBack.comment(position,bean.getId());
                        }
                    }
                });
                holder.indent_wait_item_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(callBack!=null){
                            callBack.itemOnClick(position);
                        }
                    }
                });

                break;

        }
    }

    @Override
    public int getItemCount() {
        return  data == null ? 0 : data.size();
    }


    public class IndentAllViewHolder extends RecyclerView.ViewHolder {
        //返利
        LinearLayout indent_rebate_item_ll;
        CircleImageView indent_rebate_item_Img;
        TextView indent_rabate_item_Title;
        TextView indent_rebate_item_date;
        TextView indent_rebate_item_money;
        TextView indent_rebate_item_rebate;
        TextView indent_wait_item_comment;

        //不返利
        LinearLayout indent_wait_item_ll;
        CircleImageView indent_wait_item_Img;
        TextView indent_wait_item_Title;
        TextView indent_wait_item_money;
        TextView indent_wait_item_date;
        TextView indent_wait_item_comments;


        public IndentAllViewHolder(View itemView,int ViewType) {
            super(itemView);
           if(null!=itemView){
               switch (ViewType){
                   case 1:
                       indent_rebate_item_ll= (LinearLayout) itemView.findViewById(R.id.indent_rebate_item_ll);
                       indent_rebate_item_Img= (CircleImageView) itemView.findViewById(R.id.indent_rebate_item_Img);
                       indent_rabate_item_Title= (TextView) itemView.findViewById(R.id.indent_rabate_item_Title);
                       indent_rebate_item_date= (TextView) itemView.findViewById(R.id.indent_rebate_item_date);
                       indent_rebate_item_money= (TextView) itemView.findViewById(R.id.indent_rebate_item_money);
                       indent_rebate_item_rebate= (TextView) itemView.findViewById(R.id.indent_rebate_item_rebate);
                       indent_wait_item_comment= (TextView) itemView.findViewById(R.id.indent_wait_item_comment);
                       break;
                   case 2:
                       indent_wait_item_ll= (LinearLayout) itemView.findViewById(R.id.indent_wait_item_ll);
                       indent_wait_item_Img= (CircleImageView) itemView.findViewById(R.id.indent_wait_item_Img);
                       indent_wait_item_Title= (TextView) itemView.findViewById(R.id.indent_wait_item_Title);
                       indent_wait_item_money= (TextView) itemView.findViewById(R.id.indent_wait_item_money);
                       indent_wait_item_date= (TextView) itemView.findViewById(R.id.indent_wait_item_date);
                       indent_wait_item_comments= (TextView) itemView.findViewById(R.id.indent_wait_item_comments);
                       break;
               }
           }

        }
    }




    public interface IndentAllAdapterCallBack {
        public void itemOnClick(int position);
        public void comment(int position,int id);
    }

    public void setListener(IndentAllAdapterCallBack callBack) {
        this.callBack = callBack;

    }

}
