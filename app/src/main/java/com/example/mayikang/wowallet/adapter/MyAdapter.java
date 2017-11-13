package com.example.mayikang.wowallet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.modle.javabean.FriendBean;
import com.example.mayikang.wowallet.modle.javabean.TransferBean;
import com.example.mayikang.wowallet.modle.javabean.UserBean;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lifuyuan on 2017/5/9.
 */

public class MyAdapter extends BaseAdapter {
    private List<TransferBean> list = new ArrayList<>();
    private Context context;

    public MyAdapter(List<TransferBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.friend_list_item, parent, false);
            holder = new MyViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (MyViewHolder) convertView.getTag();
        }
        TransferBean bean=list.get(position);

        UserBean userBean=bean.getIncomeUser();
        if(userBean.getId()== UserAuthUtil.getUserId()){
            holder.friendItemName.setText(bean.getExpenditureUser().getUsername());
            holder.friendItemTransferAccounts.setText("[转账]向我转账"+bean.getAmount()+"元");
           /* PicassoUtil.getPicassoObject()
                    .load(bean.getExpenditureUser().getUrl())
                    .resize(DpUtils.dpToPx(context,80),DpUtils.dpToPx(context,80))
                    .into(holder.friendItemIcon);*/
            Glide.with(context).load(bean.getExpenditureUser().getUrl())
                    .placeholder(R.drawable.ic_defult_load).crossFade()
                    .error(R.drawable.ic_defult_error)
                    .into(holder.friendItemIcon);


        }else {
            holder.friendItemTransferAccounts.setText("[转账]向"+userBean.getUsername()+"转账"+bean.getAmount()+"元");
          /*  PicassoUtil.getPicassoObject()
                    .load(bean.getExpenditureUser().getUrl())
                    .resize(DpUtils.dpToPx(context,80),DpUtils.dpToPx(context,80))
                    .into(holder.friendItemIcon);*/
            Glide.with(context).load(bean.getExpenditureUser().getUrl())
                    .placeholder(R.drawable.ic_defult_load).crossFade()
                    .error(R.drawable.ic_defult_error)
                    .override(80,80).into(holder.friendItemIcon);

            holder.friendItemName.setText(bean.getExpenditureUser().getUsername());
        }
        holder.friendItemTime.setText(bean.getInsertTime());
        return convertView;
    }



    static class MyViewHolder {
        @BindView(R.id.friend_item_icon)
        CircleImageView friendItemIcon;
        @BindView(R.id.friend_item_name)
        TextView friendItemName;
        @BindView(R.id.friend_item_time)
        TextView friendItemTime;
        @BindView(R.id.friend_item_transfer_accounts)
        TextView friendItemTransferAccounts;

        MyViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
