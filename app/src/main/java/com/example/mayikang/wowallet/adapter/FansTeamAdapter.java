package com.example.mayikang.wowallet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.Glide;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.modle.javabean.UserBean;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by haohaoliu on 2017/7/7.
 * explain: 二级粉丝的适配器
 */

public class FansTeamAdapter extends RecyclerView.Adapter<FansTeamAdapter.FansTeamHolder> {
    private List<UserBean> data;
    private Context mContext;
    private LayoutInflater inflater;

    public FansTeamAdapter(Context mContext, List<UserBean> data) {
        this.mContext = mContext;
        this.data = data;
        this.inflater= (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public FansTeamHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_fansteam_layout,null);
        return new FansTeamHolder(view);
    }

    @Override
    public void onBindViewHolder(FansTeamHolder holder, int position) {
        final UserBean bean=data.get(position);
       /*PicassoUtil.getPicassoObject().load(bean.getUrl())
               .resize(DpUtils.dpToPx(mContext,80),DpUtils.dpToPx(mContext,80))
               .into(holder.fansteamItem_icon);*/
        Glide.with(mContext).load(bean.getUrl())
                .placeholder(R.drawable.ic_defult_load).crossFade()
                .error(R.drawable.ic_defult_error)
                .into(holder.fansteamItem_icon);
        holder.fansteamItem_fansName.setText(bean.getUsername());
        holder.fansteamItem_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/main/act/user/FansMessageActivity").withInt("id",bean.getId()).navigation();
            }
        });

    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }


    class FansTeamHolder extends RecyclerView.ViewHolder{
        CircleImageView fansteamItem_icon;
        LinearLayout fansteamItem_layout;
        TextView fansteamItem_fansName;

        public FansTeamHolder(View itemView) {
            super(itemView);
            fansteamItem_icon= (CircleImageView) itemView.findViewById(R.id.fansteamItem_icon);
            fansteamItem_layout= (LinearLayout) itemView.findViewById(R.id.fansteamItem_layout);
            fansteamItem_fansName= (TextView) itemView.findViewById(R.id.fansteamItem_fansName);
        }
    }
}
