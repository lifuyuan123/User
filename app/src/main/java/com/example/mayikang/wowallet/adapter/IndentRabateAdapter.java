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

public class IndentRabateAdapter extends RecyclerView.Adapter<IndentRabateAdapter.IndentRabateViewHolder> {

    private LayoutInflater inflater;
    private Context mContext;
    private List<IndentBean> data;
    private IndentRabateAdapterCallBack callBack;

    public IndentRabateAdapter(Context mContext, List<IndentBean> data) {
        this.mContext = mContext;
        this.data = data;
        inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public IndentRabateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = inflater.inflate(R.layout.indent_rebate_item, null);
        IndentRabateViewHolder holder = new IndentRabateViewHolder(mView);
        return holder;
    }

    @Override
    public void onBindViewHolder(IndentRabateViewHolder holder, final int position) {
        final IndentBean bean=data.get(position);
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
                    callBack.commentOnClick(position,bean.getId());
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

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class IndentRabateViewHolder extends RecyclerView.ViewHolder {
        //返利
        LinearLayout indent_rebate_item_ll;
        CircleImageView indent_rebate_item_Img;
        TextView indent_rabate_item_Title;
        TextView indent_rebate_item_date;
        TextView indent_rebate_item_money;
        TextView indent_rebate_item_rebate;
        TextView indent_wait_item_comment;

        public IndentRabateViewHolder(View itemView) {
            super(itemView);
            indent_rebate_item_ll= (LinearLayout) itemView.findViewById(R.id.indent_rebate_item_ll);
            indent_rebate_item_Img= (CircleImageView) itemView.findViewById(R.id.indent_rebate_item_Img);
            indent_rabate_item_Title= (TextView) itemView.findViewById(R.id.indent_rabate_item_Title);
            indent_rebate_item_date= (TextView) itemView.findViewById(R.id.indent_rebate_item_date);
            indent_rebate_item_money= (TextView) itemView.findViewById(R.id.indent_rebate_item_money);
            indent_rebate_item_rebate= (TextView) itemView.findViewById(R.id.indent_rebate_item_rebate);
            indent_wait_item_comment= (TextView) itemView.findViewById(R.id.indent_wait_item_comment);

        }
    }

    public  interface IndentRabateAdapterCallBack{
        public void itemOnClick(int position);
        public void commentOnClick(int position,int id);
    }

    public void setListener( IndentRabateAdapterCallBack callBack){
        this.callBack=callBack;
    }


}
