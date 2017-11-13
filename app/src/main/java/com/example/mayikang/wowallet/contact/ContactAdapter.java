package com.example.mayikang.wowallet.contact;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mayikang.wowallet.R;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private Context mContext;
    private List<ContactBean> mDatas;
    private LayoutInflater mInflater;

    public ContactAdapter(Context mContext, List<ContactBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(final ContactAdapter.ViewHolder holder, final int position) {
        final ContactBean contactBean = mDatas.get(position);
        holder.tvName.setText(contactBean.getName());
        holder.tvPhone.setText(contactBean.getPhone());
        PicassoUtil.getPicassoObject().load(contactBean.getLogo()).
                resize(DpUtils.dpToPx(mContext,60),DpUtils.dpToPx(mContext,60)).error(R.mipmap.icon_default_portrait).into(holder.civ);
        holder.mLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/main/act/user/user_profile_detail").withInt("id",contactBean.getId()).navigation();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvPhone;
        CircleImageView civ;
        LinearLayout mLL;
        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvPhone= (TextView) itemView.findViewById(R.id.tvPhone);
            civ= (CircleImageView) itemView.findViewById(R.id.civ);
            mLL= (LinearLayout) itemView.findViewById(R.id.ll_parent);
        }
    }
}
