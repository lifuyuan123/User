package com.example.mayikang.wowallet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.arouter.utils.TextUtils;
import com.bumptech.glide.Glide;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.modle.javabean.FensBean;
import com.example.mayikang.wowallet.modle.javabean.UserBean;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lifuyuan on 2017/5/10.
 */

public class MyFansAdapter extends RecyclerView.Adapter<MyFansAdapter.MyViewHolder> {
    private List<UserBean> list;
    private Context context;
    private LayoutInflater inflater;
    private int flag;
    public MyFansAdapter(List<UserBean> list, Context context,int flag) {
        this.list = list;
        this.context = context;
        this.flag=flag;
        if(inflater==null){
            inflater=LayoutInflater.from(context);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.myfansitem,null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,  int position) {
        final UserBean bean=list.get(position);
       /* PicassoUtil.getPicassoObject().
                load(bean.getUrl()).resize(DpUtils.dpToPx(context,80),DpUtils.dpToPx(context,80))
                .error(R.mipmap.icon_default_portrait)
                .into(holder.fansItem_icon);*/
        Glide.with(context).load(bean.getUrl())
                .placeholder(R.drawable.ic_defult_load).crossFade()
                .error(R.drawable.ic_defult_error)
               .into(holder.fansItem_icon);


        holder.fansItem_fansName.setText(bean.getUsername());
        holder.fansItem_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//跳转到下级页面
                ARouter.getInstance().build("/main/act/user/FansMessageActivity").withInt("id",bean.getId()).navigation();
            }
        });
        holder.fansItem_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==1){
                    if(bean.getUserFans()>0){
                        ARouter.getInstance().build("/main/act/user/FirstFans").withInt("id",bean.getId()).navigation();
                    }else {
                        Toast.makeText(context, "此用户没有粉丝", Toast.LENGTH_SHORT).show();
                    }
                }else if(flag==2) {
                    if(bean.getUserFans()>0){
                        ARouter.getInstance().build("/main/act/user/SecondFans").withInt("id",bean.getId()).navigation();
                    }else {
                        Toast.makeText(context, "此用户没有粉丝", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        holder.fansItem_button.setText("下级("+bean.getUserFans()+")人");
    }




    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        LinearLayout fansItem_mainLayout;
        CircleImageView fansItem_icon;
        LinearLayout fansItem_layout;
        TextView fansItem_fansName;
        Button fansItem_button;


        public MyViewHolder(View itemView) {
            super(itemView);
            fansItem_mainLayout= (LinearLayout) itemView.findViewById(R.id.fansItem_mainLayout);
            fansItem_icon= (CircleImageView) itemView.findViewById(R.id.fansItem_icon);
            fansItem_layout= (LinearLayout) itemView.findViewById(R.id.fansItem_layout);
            fansItem_fansName= (TextView) itemView.findViewById(R.id.fansItem_fansName);
            fansItem_button= (Button) itemView.findViewById(R.id.fansItem_button);
        }
    }
}
