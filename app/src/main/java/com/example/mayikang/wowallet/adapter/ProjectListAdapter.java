package com.example.mayikang.wowallet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.modle.javabean.CommentBean;
import com.example.mayikang.wowallet.modle.javabean.ProjectBean;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by mayikang on 17/5/12.
 */

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ProjectHolder> {


    private Context context;
    private List<ProjectBean> data;
    private LayoutInflater inflater;

    public ProjectListAdapter(Context context,List<ProjectBean> data){
        this.context=context;
        this.data=data;
        if(inflater==null){
            inflater=LayoutInflater.from(context);
        }
    }


    @Override
    public ProjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.store_lay_5,null);
        return new ProjectHolder(view);
    }

    @Override
    public void onBindViewHolder(ProjectHolder holder, final int position) {
       /* PicassoUtil.getPicassoObject()
                .load(data.get(position).getLogoUrl())
                .resize(DpUtils.dpToPx(context,80),DpUtils.dpToPx(context,80))
                .error(R.mipmap.icon_load_faild).into(holder.iv);*/
        Glide.with(context).load(data.get(position).getLogoUrl())
                .placeholder(R.drawable.ic_defult_load).crossFade()
                .error(R.drawable.ic_defult_error)
                .into(holder.iv);

        holder.tvName.setText(data.get(position).getName());
        holder.tvType.setText("["+(StringUtil.isBlank(data.get(position).getType())?"暂无分类":data.get(position).getType())+"]");
        holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/main/act/project_detail").withInt("id",data.get(position).getId()).navigation();
            }
        });
    }



    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    class ProjectHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        TextView tvName,tvType;
        LinearLayout llContainer;
        public ProjectHolder(View itemView) {
            super(itemView);
            iv= (ImageView) itemView.findViewById(R.id.store_lay_5_iv);
            tvName= (TextView) itemView.findViewById(R.id.store_lay_5_tv_name);
            tvType= (TextView) itemView.findViewById(R.id.store_lay_5_tv_type);
            llContainer= (LinearLayout) itemView.findViewById(R.id.store_lay_5_ll_container);
        }

    }

}
