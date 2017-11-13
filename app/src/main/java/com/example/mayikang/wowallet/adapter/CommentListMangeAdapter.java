package com.example.mayikang.wowallet.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.modle.javabean.CommentBean;
import com.github.ornolfr.ratingview.RatingView;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by haohaoliu on 2017/7/13.
 * explain:我的评价列表
 */

public class CommentListMangeAdapter extends RecyclerView.Adapter<CommentListMangeAdapter.CommentListMangeHolder> {

    private Context mContext;
    private LayoutInflater inflater;
    private List<CommentBean> data;

    public CommentListMangeAdapter(Context mContext, List<CommentBean> data) {
        this.mContext = mContext;
        this.data = data;
        this.inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public CommentListMangeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = inflater.inflate(R.layout.item_comment_list, null);
        return new CommentListMangeHolder(mView);
    }

    @Override
    public void onBindViewHolder(CommentListMangeHolder holder, final int position) {
        CommentBean bean=data.get(position);
        /*PicassoUtil.getPicassoObject().load(bean.getCommentUserLogo())
                .resize(DpUtils.dpToPx(mContext,80),DpUtils.dpToPx(mContext,80))
                .error(R.mipmap.icon_load_faild)
                .into(holder.itemCommentListUserIcon);*/
        Glide.with(mContext).load(bean.getCommentUserLogo())
                .placeholder(R.drawable.ic_defult_load).crossFade()
                .error(R.drawable.ic_defult_error)
                .into(holder.itemCommentListUserIcon);


        holder.itemCommentListUserName.setText(bean.getCommentUserName());
        holder.itemCommentListTime.setText(bean.getCommentTime());
        holder.itemCommentListScore.setRating((float) bean.getScore());
        holder.itemCommentListComment.setText(bean.getContent());
        /*PicassoUtil.getPicassoObject().load(bean.getStoreUrl())
                .resize(DpUtils.dpToPx(mContext,80),DpUtils.dpToPx(mContext,80))
                .error(R.mipmap.icon_load_faild)
                .into(holder.itemCommentListStoreIcon);*/
        Glide.with(mContext).load(bean.getStoreUrl())
                .placeholder(R.drawable.ic_defult_load).crossFade()
                .error(R.drawable.ic_defult_error)
               .into(holder.itemCommentListStoreIcon);
        holder.itemCommentListBody.setText(bean.getStoreDetail());
        //评论图片
        if(bean.getPhotoList()==null || bean.getPhotoList().size()<=0){
            holder.itemCommentListPhotoLay.setVisibility(View.GONE);
        }else {
            holder.itemCommentListPhotoLay.setVisibility(View.VISIBLE);
        }

        if(bean.getPhotoList()!=null) {

            if (bean.getPhotoList().size() >= 3) {
                /*PicassoUtil.getPicassoObject().
                        load(bean.getPhotoList().get(2))
                        .resize(DpUtils.dpToPx(mContext, 80), DpUtils.dpToPx(mContext, 80))
                        .error(R.mipmap.icon_load_faild).into(holder.itemCommentListPhoto3);*/
                Glide.with(mContext).load(bean.getPhotoList().get(2))
                        .placeholder(R.drawable.ic_defult_load).crossFade()
                        .error(R.drawable.ic_defult_error)
                        .into(holder.itemCommentListPhoto3);

                holder.storeLay7RvCommentPhotoTotal.setVisibility(View.VISIBLE);
                holder.itemCommentListPhotoTotal.setText(bean.getPhotoList().size() + "张");
                holder.itemCommentListPhoto3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityOptionsCompat compat = ActivityOptionsCompat.
                                makeScaleUpAnimation(v, v.getWidth() / 2, v.getHeight() / 2, 0, 0);
                        Bundle b = new Bundle();

                        b.putCharSequenceArrayList("list", (ArrayList) data.get(position).getPhotoList());
                        ARouter.getInstance().build("/main/act/gallery_scan").
                                withInt("index", 2).
                                withBundle("data", b).
                                withOptionsCompat(compat).navigation();
                    }
                });
            } else {
                holder.storeLay7RvCommentPhotoTotal.setVisibility(View.GONE);
            }

            if (bean.getPhotoList().size() >= 2) {

                /*PicassoUtil.getPicassoObject()
                        .load(bean.getPhotoList().get(1))
                        .resize(DpUtils.dpToPx(mContext, 80), DpUtils.dpToPx(mContext, 80))
                        .error(R.mipmap.icon_load_faild).into(holder.itemCommentListPhoto2);*/
                Glide.with(mContext).load(bean.getPhotoList().get(1))
                        .placeholder(R.drawable.ic_defult_load).crossFade()
                        .error(R.drawable.ic_defult_error)
                        .into(holder.itemCommentListPhoto2);



                holder.itemCommentListPhoto2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityOptionsCompat compat = ActivityOptionsCompat.
                                makeScaleUpAnimation(v, v.getWidth() / 2, v.getHeight() / 2, 0, 0);
                        Bundle b = new Bundle();

                        b.putCharSequenceArrayList("list", (ArrayList) data.get(position).getPhotoList());
                        ARouter.getInstance().build("/main/act/gallery_scan").
                                withInt("index", 1).
                                withBundle("data", b).
                                withOptionsCompat(compat).navigation();
                    }
                });
            }

            if (bean.getPhotoList().size() >= 1) {
               /* PicassoUtil.getPicassoObject().
                        load(bean.getPhotoList().get(0)).
                        resize(DpUtils.dpToPx(mContext, 80), DpUtils.dpToPx(mContext, 80)).
                        error(R.mipmap.icon_load_faild).into(holder.itemCommentListPhoto1);*/
                Glide.with(mContext).load(bean.getPhotoList().get(0))
                        .placeholder(R.drawable.ic_defult_load).crossFade()
                        .error(R.drawable.ic_defult_error)
                        .into(holder.itemCommentListPhoto1);

                holder.itemCommentListPhoto1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityOptionsCompat compat = ActivityOptionsCompat.
                                makeScaleUpAnimation(v, v.getWidth() / 2, v.getHeight() / 2, 0, 0);
                        Bundle b = new Bundle();

                        b.putCharSequenceArrayList("list", (ArrayList) data.get(position).getPhotoList());
                        ARouter.getInstance().build("/main/act/gallery_scan").
                                withInt("index", 0).
                                withBundle("data", b).
                                withOptionsCompat(compat).navigation();
                    }
                });

            }


        }

    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    static class CommentListMangeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_comment_list_userIcon)
        CircleImageView itemCommentListUserIcon;
        @BindView(R.id.item_comment_list_user_name)
        TextView itemCommentListUserName;
        @BindView(R.id.item_comment_list_time)
        TextView itemCommentListTime;
        @BindView(R.id.item_comment_list_score)
        RatingView itemCommentListScore;
        @BindView(R.id.item_comment_list_comment)
        TextView itemCommentListComment;
        @BindView(R.id.item_comment_list_storeIcon)
        ImageView itemCommentListStoreIcon;
        @BindView(R.id.item_comment_list_storeName)
        TextView itemCommentListStoreName;
        @BindView(R.id.item_comment_list_body)
        TextView itemCommentListBody;
        @BindView(R.id.item_comment_list_photo_1)
        ImageView itemCommentListPhoto1;
        @BindView(R.id.item_comment_list_photo_2)
        ImageView itemCommentListPhoto2;
        @BindView(R.id.item_comment_list_photo_3)
        ImageView itemCommentListPhoto3;
        @BindView(R.id.item_comment_list_photo_total)
        TextView itemCommentListPhotoTotal;
        @BindView(R.id.store_lay_7_rv_comment_photo_total)
        RelativeLayout storeLay7RvCommentPhotoTotal;
        @BindView(R.id.item_comment_list_photo_lay)
        LinearLayout itemCommentListPhotoLay;
        public CommentListMangeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
