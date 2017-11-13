package com.example.mayikang.wowallet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.modle.javabean.MessageBean;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by liuha on 2017/5/15.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<MessageBean> data;
    private Context mContext;
    private LayoutInflater inflater;

    public MessageAdapter(List<MessageBean> data, Context mContext) {
        this.data = data;
        this.mContext = mContext;
        inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView=inflater.inflate(R.layout.message_item, null);

        return new MessageViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder holder, final int position) {
        final MessageBean bean=data.get(position);
        //设置flag（已读，未读）
        if(bean.getStatus()==1){
            holder.messagItemFlagImg.setVisibility(View.VISIBLE);
        }else if(bean.getStatus()==2){
            holder.messagItemFlagImg.setVisibility(View.INVISIBLE);
        }

        holder.messagItemTitleTxt.setText(bean.getTitle());
        holder.messagItemBodyTxt.setText(bean.getContent());
        if(bean.getUrl().equals("null")){
            //是平台消息
            holder.messagItemIconImg.setImageResource(R.mipmap.icon_app_start_logo);
        }else {
          /*  PicassoUtil.getPicassoObject().load(bean.getUrl()).error(R.mipmap.icon_default_portrait)
                    .resize(DpUtils.dpToPx(mContext,80),DpUtils.dpToPx(mContext,80))
                    .into(holder.messagItemIconImg);*/
            Glide.with(mContext).load(bean.getUrl())
                    .placeholder(R.drawable.ic_defult_load).crossFade()
                    .error(R.drawable.ic_defult_error)
                   .into(holder.messagItemIconImg);
        }

        holder.messagItemDelTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=callback){
                   callback.delMseeage(holder.getLayoutPosition());
               }
            }
        });
        holder.messagItemDateTxt.setText(bean.getInsert_time());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bean.getType()==4){
                    ARouter.getInstance().build("/main/act/user/AddFriends").withInt("key",bean.getBean().getId()).withInt("msg",bean.getId()).navigation();
                }else {
                    ARouter.getInstance().build("/main/act/MessageInFoActivity").withInt("key",bean.getId()).navigation();
                }


            }
        });
    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.messag_item_flagImg)
        ImageView messagItemFlagImg;
        @BindView(R.id.messag_item_iconImg)
        CircleImageView messagItemIconImg;
        @BindView(R.id.messag_item_titleTxt)
        TextView messagItemTitleTxt;
        @BindView(R.id.messag_item_dateTxt)
        TextView messagItemDateTxt;
        @BindView(R.id.messag_item_bodyTxt)
        TextView messagItemBodyTxt;
        @BindView(R.id.messag_item_delTxt)
        Button messagItemDelTxt;
        @BindView(R.id.message_item_layout)
        LinearLayout layout;


        public MessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface MessageListCallBack{
        public void delMseeage(int position);
    }
    private MessageListCallBack callback;

    public void setListener(MessageListCallBack callback){
        this.callback=callback;
    }

}
