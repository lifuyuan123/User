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
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mayikang on 17/5/12.
 */

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.CommentHolder> {


    private Context context;
    private List<CommentBean> data;
    private LayoutInflater inflater;

    public CommentListAdapter(Context context,List<CommentBean> data){
        this.context=context;
        this.data=data;
        if(inflater==null){
            inflater=LayoutInflater.from(context);
        }
    }


    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.store_lay_7,null);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, final int position) {
        CommentBean cb= data.get(position);
       /* PicassoUtil.getPicassoObject().load(cb.getCommentUserLogo()).resize(DpUtils
                .dpToPx(context,80),DpUtils.dpToPx(context,80)).error(R.mipmap.icon_load_faild)
                .into(holder.civLogo);*/
        Glide.with(context).load(cb.getCommentUserLogo())
                .placeholder(R.drawable.ic_defult_load).crossFade()
                .error(R.drawable.ic_defult_error)
               .into(holder.civLogo);

        if(cb.getIsAnonymous()==1){
            holder.commentUserName.setText(cb.getCommentUserName());
        }else {
            StringBuffer buffer=new StringBuffer();
            String name=cb.getCommentUserName();
            if(!StringUtil.isBlank(name)&&name.length()>2){
                buffer.append(name.charAt(0));
                buffer.append("***");
                holder.commentUserName.setText(buffer.toString());
            }else {
                holder.commentUserName.setText("****");
            }
        }

        holder.commentTime.setText(cb.getCommentTime());
        holder.rvUserCommentScore.setRating((float)cb.getScore());
        holder.commentContent.setText(cb.getContent());
        //评论图片
        if(cb.getPhotoList()==null || cb.getPhotoList().size()<=0){
            holder.llCommentPhoto.setVisibility(View.GONE);
        }else {
            holder.llCommentPhoto.setVisibility(View.VISIBLE);
        }

        if(cb.getPhotoList()!=null){

            if(cb.getPhotoList().size()>=3){
               /* PicassoUtil.getPicassoObject().load(cb.getPhotoList().get(2))
                        .resize(DpUtils.dpToPx(context,80),DpUtils.dpToPx(context,80))
                        .error(R.mipmap.icon_load_faild).into(holder.commentIv3);*/
                Glide.with(context).load(cb.getPhotoList().get(2))
                        .placeholder(R.drawable.ic_defult_load).crossFade()
                        .error(R.drawable.ic_defult_error)
                        .into(holder.commentIv3);

                holder.rlTotalCommentPhoto.setVisibility(View.VISIBLE);
                holder.totalCommentPhoto.setText(cb.getPhotoList().size()+"张");
                holder.commentIv3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityOptionsCompat compat = ActivityOptionsCompat.
                                makeScaleUpAnimation(v, v.getWidth() / 2, v.getHeight() / 2, 0, 0);
                        Bundle b=new Bundle();

                        b.putCharSequenceArrayList("list",(ArrayList) data.get(position).getPhotoList());
                        ARouter.getInstance().build("/main/act/gallery_scan").
                                withInt("index",2).
                                withBundle("data",b).
                                withOptionsCompat(compat).navigation();
                    }
                });
            }else {
                holder.rlTotalCommentPhoto.setVisibility(View.GONE);
            }

            if(cb.getPhotoList().size()>=2){
                /*PicassoUtil.getPicassoObject().
                        load(cb.getPhotoList().get(1))
                        .resize(DpUtils.dpToPx(context,80),DpUtils.dpToPx(context,80))
                        .error(R.mipmap.icon_load_faild).into(holder.commentIv2);*/
                Glide.with(context).load(cb.getPhotoList().get(1))
                        .placeholder(R.drawable.ic_defult_load).crossFade()
                        .error(R.drawable.ic_defult_error)
                        .into(holder.commentIv2);

                holder.commentIv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityOptionsCompat compat = ActivityOptionsCompat.
                                makeScaleUpAnimation(v, v.getWidth() / 2, v.getHeight() / 2, 0, 0);
                        Bundle b=new Bundle();

                        b.putCharSequenceArrayList("list",(ArrayList) data.get(position).getPhotoList());
                        ARouter.getInstance().build("/main/act/gallery_scan").
                                withInt("index",1).
                                withBundle("data",b).
                                withOptionsCompat(compat).navigation();
                    }
                });
            }

            if(cb.getPhotoList().size()>=1){
                /*PicassoUtil.getPicassoObject().
                        load(cb.getPhotoList().get(0)).
                        resize(DpUtils.dpToPx(context,80),DpUtils.dpToPx(context,80)).
                        error(R.mipmap.icon_load_faild).into(holder.commentIv1);*/
                Glide.with(context).load(cb.getPhotoList().get(0))
                        .placeholder(R.drawable.ic_defult_load).crossFade()
                        .error(R.drawable.ic_defult_error)
                        .into(holder.commentIv1);

                holder.commentIv1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityOptionsCompat compat = ActivityOptionsCompat.
                                makeScaleUpAnimation(v, v.getWidth() / 2, v.getHeight() / 2, 0, 0);
                        Bundle b=new Bundle();

                        b.putCharSequenceArrayList("list",(ArrayList) data.get(position).getPhotoList());
                        ARouter.getInstance().build("/main/act/gallery_scan").
                                withInt("index",0).
                                withBundle("data",b).
                                withOptionsCompat(compat).navigation();
                    }
                });

            }


            if(StringUtil.isBlank(cb.getRevert())){
                holder.llRevert.setVisibility(View.GONE);
            }else {
                holder.llRevert.setVisibility(View.VISIBLE);
                holder.revertContent.setText(cb.getRevert());
            }


        }
    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    class CommentHolder extends RecyclerView.ViewHolder{
        CircleImageView civLogo;
        TextView commentUserName,commentTime,commentContent,revertContent,revertTime,totalCommentPhoto;
        RatingView rvUserCommentScore;
        LinearLayout llRevert,llCommentPhoto;
        ImageView commentIv1,commentIv2,commentIv3;
        RelativeLayout rlTotalCommentPhoto;
        public CommentHolder(View root) {
            super(root);
            civLogo= (CircleImageView) root.findViewById(R.id.store_lay_7_civ_comment_user_logo);
            commentUserName= (TextView) root.findViewById(R.id.store_lay_7_tv_comment_user_name);
            commentTime= (TextView) root.findViewById(R.id.store_lay_7_tv_comment_time);
            commentContent= (TextView) root.findViewById(R.id.store_lay_7_tv_comment);
            rvUserCommentScore= (RatingView) root.findViewById(R.id.store_lay_7_rv_comment_score);
            llRevert= (LinearLayout) root.findViewById(R.id.store_lay_7_ll_revert_parent);
            commentIv1= (ImageView) root.findViewById(R.id.store_lay_7_iv_comment_photo_1);
            commentIv2= (ImageView) root.findViewById(R.id.store_lay_7_iv_comment_photo_2);
            commentIv3= (ImageView) root.findViewById(R.id.store_lay_7_iv_comment_photo_3);
            rlTotalCommentPhoto= (RelativeLayout) root.findViewById(R.id.store_lay_7_rv_comment_photo_total);
            revertContent= (TextView) root.findViewById(R.id.store_lay_7_tv_revert_content);
            totalCommentPhoto= (TextView) root.findViewById(R.id.store_lay_7_iv_comment_photo_total);
            llCommentPhoto= (LinearLayout) root.findViewById(R.id.ll_comment_photo);
        }
    }

}
