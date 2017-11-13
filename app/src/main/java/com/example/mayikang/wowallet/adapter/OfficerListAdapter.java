package com.example.mayikang.wowallet.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.modle.javabean.OfficerBean;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;

import java.util.List;

/**
 * Created by liuha on 2017/5/15.
 */

public class OfficerListAdapter extends RecyclerView.Adapter<OfficerListAdapter.OfficerViewHolder> {
    private List<OfficerBean> data;
    private Context mContext;
    private LayoutInflater inflater;

    public OfficerListAdapter(List<OfficerBean> data, Context mContext) {
        this.data = data;
        this.mContext = mContext;
        inflater= (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public OfficerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView=inflater.inflate(R.layout.officer_list_item,null);
        OfficerViewHolder holder=new OfficerViewHolder(mView);
        return holder;
    }

    @Override
    public void onBindViewHolder(OfficerViewHolder holder, int position) {
        /*PicassoUtil.getPicassoObject()
                .load(data.get(position).getLogo())
                .resize(DpUtils.dpToPx(mContext,80),DpUtils.dpToPx(mContext,80))
                .error(R.mipmap.icon_load_faild).into(holder.iv);*/

        Glide.with(mContext).load(data.get(position).getLogo())
                .placeholder(R.drawable.ic_defult_load).crossFade()
                .error(R.drawable.ic_defult_error)
                .into(holder.iv);


        holder.tvName.setText(data.get(position).getName());
        holder.tvJob.setText(data.get(position).getJobBean().getName());
    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    public class OfficerViewHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView tvName,tvJob;
        public OfficerViewHolder(View itemView) {
            super(itemView);
            iv= (ImageView) itemView.findViewById(R.id.iv_officer_logo);
            tvName= (TextView) itemView.findViewById(R.id.tv_officer_name);
            tvJob= (TextView) itemView.findViewById(R.id.tv_officer_job);
        }
    }
}
