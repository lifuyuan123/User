package com.example.mayikang.wowallet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.modle.javabean.UserBean;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.soundcloud.android.crop.CropImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by liuha on 2017/6/9.
 * 搜索好友的适配器
 */

public class SearchFriendsAdapter extends RecyclerView.Adapter<SearchFriendsAdapter.SearchFriendsHolder> {
    private List<UserBean> data;
    private LayoutInflater inflater;
    private Context mContext;
    private OnclickCallBack callBack;

    public SearchFriendsAdapter(List<UserBean> data, Context mContext) {
        this.data = data;
        this.mContext = mContext;
        this.inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public SearchFriendsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.add_friends_item, null);
        SearchFriendsHolder holder=new SearchFriendsHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SearchFriendsHolder holder, int position) {
        final UserBean bean=data.get(position);
        holder.addFriendsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=callBack){
                    callBack.itemClick(bean);
                }
            }
        });

      /*  PicassoUtil.getPicassoObject()
                .load(bean.getUrl()).into(holder.addFriendsItemIcon);*/
        Glide.with(mContext).load(bean.getUrl())
                .placeholder(R.drawable.ic_defult_load).crossFade()
                .error(R.drawable.ic_defult_error)
                .into(holder.addFriendsItemIcon);


        holder.addFriendsItemName.setText(bean.getUsername());
        holder.phone.setText(bean.getPhone());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class SearchFriendsHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.add_friends_item_icon)
        CircleImageView addFriendsItemIcon;
        @BindView(R.id.add_friends_item_name)
        TextView addFriendsItemName;
        @BindView(R.id.add_friends_item_Phone)
        TextView phone;
        @BindView(R.id.add_friends_item)
        LinearLayout addFriendsItem;


        public SearchFriendsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnclickCallBack{
        public void itemClick(UserBean bean);
    }

    public void setListener(OnclickCallBack callBack){
        this.callBack=callBack;
    }


}
