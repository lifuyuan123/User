package com.example.mayikang.wowallet.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.Glide;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.modle.javabean.CommentBean;
import com.example.mayikang.wowallet.modle.javabean.Data;
import com.example.mayikang.wowallet.modle.javabean.OfficerBean;
import com.example.mayikang.wowallet.modle.javabean.ProjectBean;
import com.example.mayikang.wowallet.modle.javabean.StoreData;
import com.example.mayikang.wowallet.ui.act.StoreActivity;
import com.github.ornolfr.ratingview.RatingView;
import com.hymane.expandtextview.ExpandTextView;
import com.sctjsj.basemodule.base.util.CallUtil;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Carson_Ho on 17/4/26.
 */
public class StoreSubAdapter extends DelegateAdapter.Adapter<StoreSubAdapter.MainViewHolder> {
    /**
     * 首页一次从上往下对应的6种布局
     **/
    public static final int TYPE_1 = 1;//banner
    public static final int TYPE_2 = 2;//storeName
    public static final int TYPE_3 = 3;//location
    public static final int TYPE_4 = 4;//brand
    public static final int TYPE_5 = 5;//project
    public static final int TYPE_6 = 6;//officer
    public static final int TYPE_7 = 7;//comment

    public static final int TYPE_PROJECT_TITLE = 8;//TYPE_PROJECT_TITLE
    public static final int TYPE_OFFICER_TITLE = 9;//TYPE_OFFICER_TITLE
    public static final int TYPE_COMMENT_STORE_TITLE = 10;//TYPE_COMMENT_STORE_TITLE
    public static final int TYPE_TO_ALL_COMMENT_STORE_TITLE = 11;//TYPE_TO_ALL_COMMENT_STORE_TITLE

    /**
     * 系统变量
     **/
    private int id = -1;
    private String logo;
    private String callNum = null;
    private double longitude;
    private double latitude;


    // 使用DelegateAdapter首先就是要自定义一个它的内部类Adapter，让LayoutHelper和需要绑定的数据传进去
    // 此处的Adapter和普通RecyclerView定义的Adapter只相差了一个onCreateLayoutHelper()方法，其他的都是一样的做法.

    private ArrayList<HashMap<String, Object>> listItem;
    // 用于存放数据列表

    private Context context;
    private LayoutInflater inflater;
    private LayoutHelper layoutHelper;
    private RecyclerView.LayoutParams layoutParams;
    private int count = 0;
    private int type = 0;//本 item 的类型


    //构造函数(传入每个的数据列表 & 展示的Item数量)
    public StoreSubAdapter(Context context, LayoutHelper layoutHelper, int count, ArrayList<HashMap<String, Object>> listItem, int type) {
        //宽度占满，高度随意
        this(context, layoutHelper, count, new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), listItem, type);
    }

    public ArrayList<HashMap<String, Object>> getListItem() {
        return listItem;
    }

    public RecyclerView.LayoutParams getLayoutParams() {
        return layoutParams;
    }

    public int getCount() {
        return count;
    }

    public int getType() {
        return type;
    }

    public StoreSubAdapter(Context context, LayoutHelper layoutHelper, int count, @NonNull RecyclerView.LayoutParams layoutParams, ArrayList<HashMap<String, Object>> listItem, int type) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.count = count;
        this.layoutParams = layoutParams;
        this.listItem = listItem;
        this.type = type;

        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }
    }

    // 把ViewHolder绑定Item的布局
    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case TYPE_1:
                view = inflater.inflate(R.layout.store_lay_1, parent, false);
                break;
            case TYPE_2:
                view = inflater.inflate(R.layout.store_lay_2, parent, false);
                break;
            case TYPE_3:
                view = inflater.inflate(R.layout.store_lay_3, parent, false);
                break;
            case TYPE_4:
                view = inflater.inflate(R.layout.store_lay_4, parent, false);
                break;
            case TYPE_5:
                view = inflater.inflate(R.layout.store_lay_5, parent, false);
                break;
            case TYPE_6:
                view = inflater.inflate(R.layout.store_lay_6, parent, false);
                break;
            case TYPE_7:
                view = inflater.inflate(R.layout.store_lay_7, parent, false);
                break;
            case TYPE_PROJECT_TITLE:
                view = inflater.inflate(R.layout.store_lay_project_title, parent, false);
                break;
            case TYPE_OFFICER_TITLE:
                view = inflater.inflate(R.layout.store_lay_officer_title, parent, false);
                break;
            case TYPE_COMMENT_STORE_TITLE:
                view = inflater.inflate(R.layout.store_lay_comment_store_title, parent, false);
                break;
            case TYPE_TO_ALL_COMMENT_STORE_TITLE:
                view = inflater.inflate(R.layout.store_lay_to_all_comment_store_title, parent, false);
                break;
        }

        return new MainViewHolder(view, viewType);
    }

    // 此处的Adapter和普通RecyclerView定义的Adapter只相差了一个onCreateLayoutHelper()方法
    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    // 绑定Item的数据
    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        int type = getItemViewType(position);

        switch (type) {
            case TYPE_1:

                HashMap<String,Object> map1=listItem.get(0);
                if(map1.containsKey("banner_list")){
                    List<String> list = (List<String>) map1.get("banner_list");
                    initBanner(holder.banner,list);
                }

                break;
            case TYPE_2:

                HashMap<String, Object> map2 = listItem.get(0);
                if (map2.containsKey("id")) {
                    id = (int) map2.get("id");

                }
                holder.mRLToPay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance().build("/user/main/act/confirm_order").withInt("id",id).withInt("Flag",1).navigation();
                    }
                });



                if(map2.containsKey("logo")){
                    logo= (String) map2.get("logo");
                }
                if (map2.containsKey("name")) {
                    String name = (String) map2.get("name");
                    holder.tvStoreName.setText(name);
                }
                if (map2.containsKey("average_spend")) {
                    double average_spend = (double) map2.get("average_spend");
                    holder.tvStoreAverage.setText("人均：￥" + average_spend);
                }
                if (map2.containsKey("score")) {
                    double score = (double) map2.get("score");
                    holder.rvStoreScore.setRating((float) score);
                    holder.tvStoreScore.setText(score + "分");
                }
                break;
            case TYPE_3:
                HashMap<String, Object> map3 = listItem.get(0);

                if (map3.containsKey("telephone")) {
                    callNum = (String) map3.get("telephone");
                }

                holder.mRLToCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CallUtil.makeCall(context, callNum);
                    }
                });

                if (map3.containsKey("storeAddress")) {
                    String storeAddress = (String) map3.get("storeAddress");
                    holder.tvStoreAddress.setText(storeAddress);
                }

                if (map3.containsKey("longitude")) {
                    longitude = (double) map3.get("longitude");
                }

                if (map3.containsKey("latitude")) {
                    latitude = (double) map3.get("latitude");
                }

                break;
            case TYPE_4:
                HashMap<String,Object> map4=listItem.get(0);
                if(map4.containsKey("detail")){
                    String detail= (String) map4.get("detail");
                    holder.etvDetail.setContent(detail);
                    //如果字数大于50就显示折叠的布局
                     if(detail.length()>50){
                         holder.etvDetail.showOrheadLayout(true);
                     }else {
                         holder.etvDetail.showOrheadLayout(false);
                     }
                }

                break;
            case TYPE_5:
                final ProjectBean pb= (ProjectBean) listItem.get(position).get("project");
               /* PicassoUtil.getPicassoObject()
                        .load(pb.getLogoUrl()).tag("store_img")
                        .resize(DpUtils.dpToPx(context,60),DpUtils.dpToPx(context,60))
                        .error(R.mipmap.icon_load_faild).into(holder.ivProjectLogo);*/
                Glide.with(context).load(pb.getLogoUrl())
                        .placeholder(R.drawable.ic_defult_load).crossFade()
                        .error(R.drawable.ic_defult_error)
                        .override(80,80).into(holder.ivProjectLogo);


                holder.tvProjectType.setText("["+(StringUtil.isBlank(pb.getType())?"暂无分类":pb.getType())+"]");
                holder.tvProjectName.setText(pb.getName());

                //点击查看项目详情
                holder.llProjectContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance().build("/main/act/project_detail").withInt("id",pb.getId()).navigation();
                    }
                });

                break;
            case TYPE_6:
                OfficerBean ob= (OfficerBean) listItem.get(position).get("officer");
                /*PicassoUtil.getPicassoObject()
                        .load(ob.getLogo()).tag("store_img")
                        .resize(DpUtils.dpToPx(context,60),DpUtils.dpToPx(context,60))
                        .error(R.mipmap.icon_load_faild).into(holder.ivOfficerLogo);*/
                Glide.with(context).load(ob.getLogo())
                        .error(R.drawable.ic_defult_error)
                        .placeholder(R.drawable.ic_defult_load).crossFade()
                        .override(80,80).into(holder.ivOfficerLogo);

                holder.tvOfficerName.setText(ob.getName());
                holder.tvOfficerJob.setText(ob.getJobBean().getName());
                break;

            case TYPE_7:

                final CommentBean cb= (CommentBean) listItem.get(position).get("comment");
                /*PicassoUtil.getPicassoObject()
                        .load(cb.getCommentUserLogo())
                        .resize(DpUtils.dpToPx(context,80),DpUtils.dpToPx(context,80))
                        .error(R.mipmap.icon_load_faild).into(holder.civLogo);*/
                Glide.with(context).load(cb.getCommentUserLogo())
                        .placeholder(R.drawable.ic_defult_load).crossFade()
                        .error(R.drawable.ic_defult_error)
                        .override(80,80).into(holder.civLogo);

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
                       /* PicassoUtil.getPicassoObject().load(cb.getPhotoList().get(2)).
                                resize(DpUtils.dpToPx(context,80),DpUtils.dpToPx(context,80)).
                                error(R.mipmap.icon_load_faild).into(holder.commentIv3);*/

                        Glide.with(context).load(cb.getPhotoList().get(2))
                                .placeholder(R.drawable.ic_defult_load).crossFade()
                                .error(R.drawable.ic_defult_error)
                                .override(80,80).into(holder.commentIv3);

                        holder.rlTotalCommentPhoto.setVisibility(View.VISIBLE);
                        holder.totalCommentPhoto.setText(cb.getPhotoList().size()+"张");
                        holder.commentIv3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityOptionsCompat compat = ActivityOptionsCompat.
                                        makeScaleUpAnimation(v, v.getWidth() / 2, v.getHeight() / 2, 0, 0);
                                Bundle b=new Bundle();

                                b.putCharSequenceArrayList("list",(ArrayList) cb.getPhotoList());
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
                       /* PicassoUtil.getPicassoObject()
                                .load(cb.getPhotoList().get(1))
                                .resize(DpUtils.dpToPx(context,80),DpUtils.dpToPx(context,80)).
                                error(R.mipmap.icon_load_faild).into(holder.commentIv2);*/
                        Glide.with(context).load(cb.getPhotoList().get(1))
                                .placeholder(R.drawable.ic_defult_load).crossFade()
                                .error(R.drawable.ic_defult_error)
                                .override(80,80).into(holder.commentIv2);


                        holder.commentIv2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityOptionsCompat compat = ActivityOptionsCompat.
                                        makeScaleUpAnimation(v, v.getWidth() / 2, v.getHeight() / 2, 0, 0);
                                Bundle b=new Bundle();

                                b.putCharSequenceArrayList("list",(ArrayList) cb.getPhotoList());
                                ARouter.getInstance().build("/main/act/gallery_scan").
                                        withInt("index",1).
                                        withBundle("data",b).
                                        withOptionsCompat(compat).navigation();
                            }
                        });
                    }

                    if(cb.getPhotoList().size()>=1){
                        /*PicassoUtil.getPicassoObject()
                                .load(cb.getPhotoList().get(0))
                                .resize(DpUtils.dpToPx(context,80),DpUtils.dpToPx(context,80))
                                .error(R.mipmap.icon_load_faild).into(holder.commentIv1);*/

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

                                b.putCharSequenceArrayList("list",(ArrayList) cb.getPhotoList());
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

                break;

            case TYPE_PROJECT_TITLE:

                HashMap<String,Object> map8=listItem.get(0);
                if(map8.containsKey("id")){
                    id= (int) map8.get("id");
                }
                if(map8.containsKey("project_size")){
                    int size= (int) map8.get("project_size");
                    holder.stoer_lay_project_title_Img.setVisibility(View.VISIBLE);
                    if(size>0){
                        holder.tvProjectTitleNum.setText("全部"+size+"个项目");
                        holder.mRLToProjectList.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ARouter.getInstance().build("/main/act/project_list").withInt("id",id).navigation();
                            }
                        });
                    }

                }


                break;

            case TYPE_OFFICER_TITLE:

                HashMap<String,Object> map9=listItem.get(0);

                if(map9.containsKey("id")){
                    id= (int) map9.get("id");
                }

                if(map9.containsKey("officer_size")){
                    int size= (int) map9.get("officer_size");
                    //大于三位员工的时候可以查看列表
                    if(size>0){
                        holder.store_lay_office_title_Img.setVisibility(View.VISIBLE);
                        holder.tvOfficerTitleNum.setText("全部"+size+"位店员");
                        holder.mRlToAllOfficerList.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //跳转到officer列表
                                ARouter.getInstance().build("/main/act/officer_list").withInt("id",id).navigation();
                            }
                        });
                    }else {
                        holder.store_lay_office_title_Img.setVisibility(View.INVISIBLE);
                        holder.tvOfficerTitleNum.setText("暂无店员");
                    }

                }

                break;

            case TYPE_COMMENT_STORE_TITLE:

                HashMap<String,Object> map10=listItem.get(0);
                if(map10.containsKey("id")){
                    id= (int) map10.get("id");
                }
                if(map10.containsKey("score")){
                    double score= (double) map10.get("score");
                    holder.rvCommentScore.setRating((float)score);
                    holder.tvCommentScore.setText(score+"分");
                }

                if(map10.containsKey("comment_size")){
                    int size= (int) map10.get("comment_size");

//                    if(size>0){
//                        ARouter.getInstance().build("/main/act/comment_list").withInt("id",id).navigation();
//                    }

                    if(size>9){
                        holder.tvCommentNum.setText("全部"+size+"+条评价");
                    }else {
                        holder.tvCommentNum.setText("全部"+size+"条评价");
                    }

                }

                break;

            case TYPE_TO_ALL_COMMENT_STORE_TITLE:
                HashMap<String,Object> map11=listItem.get(0);


                if(map11.containsKey("length")){
                    int length= (int) map11.get("length");
                    Log.e("adapter",length+"");
                    if(length>0){
                        holder.store_lay_to_all_comment_Title.setText("查看全部评价");
                        holder.store_lay_to_all_comment_ImgLayout.setVisibility(View.VISIBLE);
                        if(map11.containsKey("xid")){
                            id= (int) map11.get("xid");
                            Log.e("接收 map11",id+"");
                            holder.mRLToAllComment.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ARouter.getInstance().build("/main/act/comment_list").withInt("id",id).navigation();
                                }
                            });
                        }
                    }else {
                        holder.store_lay_to_all_comment_Title.setText("暂无评价");
                        holder.store_lay_to_all_comment_ImgLayout.setVisibility(View.GONE);
                    }

                }


                break;
        }

    }

    @Override
    public int getItemViewType(int position) {

        switch (type) {
            case 1:
                return TYPE_1;

            case 2:
                return TYPE_2;

            case 3:
                return TYPE_3;

            case 4:
                return TYPE_4;

            case 5:
                return TYPE_5;

            case 6:
                return TYPE_6;


            case 7:
                return TYPE_7;

            case 8:

                return TYPE_PROJECT_TITLE;

            case 9:

                return TYPE_OFFICER_TITLE;


            case 10:

                return TYPE_COMMENT_STORE_TITLE;

            case 11:

                return TYPE_TO_ALL_COMMENT_STORE_TITLE;

            default:
                return -1;
        }


    }

    // 返回Item数目
    @Override
    public int getItemCount() {
        return listItem==null?0:listItem.size();
    }


    //定义Viewholder
    class MainViewHolder extends RecyclerView.ViewHolder {
        //type=1
        Banner banner;
        //type=2
        RelativeLayout mRLToPay;
        TextView tvStoreName, tvStoreScore, tvStoreAverage;
        RatingView rvStoreScore;

        //type=3
        RelativeLayout mRLToCall;
        LinearLayout llToAddressMap;
        TextView tvStoreAddress;

        //type=4
        ExpandTextView etvDetail;

        //type=5
        ImageView ivProjectLogo;
        TextView tvProjectType,tvProjectName;
        LinearLayout llProjectContainer;

        //type=6
        ImageView ivOfficerLogo;
        TextView tvOfficerName,tvOfficerJob;

        //type=7
        CircleImageView civLogo;
        TextView commentUserName,commentTime,commentContent,revertContent,revertTime,totalCommentPhoto;
        RatingView rvUserCommentScore;
        LinearLayout llRevert,llCommentPhoto;
        ImageView commentIv1,commentIv2,commentIv3;
        RelativeLayout rlTotalCommentPhoto;

        //type=comment title
        RatingView rvCommentScore;
        TextView tvCommentScore,tvCommentNum;

        //type=TYPE_PROJECT_TITLE
        RelativeLayout mRLToProjectList;
        TextView tvProjectTitleNum;
        ImageView stoer_lay_project_title_Img;

        //type=TYPE_TO_ALL_COMMENT_STORE_TITLE
        RelativeLayout mRLToAllComment;
        TextView store_lay_to_all_comment_Title;
        LinearLayout store_lay_to_all_comment_ImgLayout;

        //type=TYPE_OFFICER_TITLE
        RelativeLayout mRlToAllOfficerList;
        TextView tvOfficerTitleNum;
        ImageView store_lay_office_title_Img;

        public MainViewHolder(View root, int viewtype) {
            super(root);
            switch (viewtype) {

                case 1:
                    banner= (Banner) root.findViewById(R.id.banner);
                    break;

                case 2:
                    mRLToPay = (RelativeLayout) root.findViewById(R.id.store_lay_2_rl_to_Pay);
                    tvStoreName = (TextView) root.findViewById(R.id.store_lay_2_tv_store_name);
                    tvStoreAverage = (TextView) root.findViewById(R.id.store_lay_2_tv_store_average);
                    tvStoreScore = (TextView) root.findViewById(R.id.store_lay_2_tv_store_score);
                    rvStoreScore = (RatingView) root.findViewById(R.id.store_lay_2_rv_store_score);

                    break;

                case 3:
                    mRLToCall = (RelativeLayout) root.findViewById(R.id.store_lay_3_rl_to_call);
                    llToAddressMap = (LinearLayout) root.findViewById(R.id.store_lay_3_ll_to_store_address_map);
                    tvStoreAddress = (TextView) root.findViewById(R.id.store_lay_3_tv_store_address);
                    break;

                case 4:

                    etvDetail= (ExpandTextView) root.findViewById(R.id.store_lay_4_expand_tv);

                    break;
                case 5:
                    ivProjectLogo= (ImageView) root.findViewById(R.id.store_lay_5_iv);
                    tvProjectName= (TextView) root.findViewById(R.id.store_lay_5_tv_name);
                    tvProjectType= (TextView) root.findViewById(R.id.store_lay_5_tv_type);
                    llProjectContainer= (LinearLayout) root.findViewById(R.id.store_lay_5_ll_container);
                    break;

                case 6:
                    ivOfficerLogo= (ImageView) root.findViewById(R.id.store_lay_6_iv);
                    tvOfficerName= (TextView) root.findViewById(R.id.store_lay_6_tv_name);
                    tvOfficerJob= (TextView) root.findViewById(R.id.store_lay_6_tv_job);
                    break;


                case 7:
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

                    break;

                case 8:
                    mRLToProjectList = (RelativeLayout) root.findViewById(R.id.store_lay_project_title_rl_to_all_project);
                    tvProjectTitleNum= (TextView) root.findViewById(R.id.store_lay_project_title_tv_num);
                    stoer_lay_project_title_Img= (ImageView) root.findViewById(R.id.stoer_lay_project_title_Img);
                    break;

                //officer title
                case 9:
                    mRlToAllOfficerList = (RelativeLayout) root.findViewById(R.id.store_lay_to_all_comment_rl_container);
                    tvOfficerTitleNum= (TextView) root.findViewById(R.id.store_lay_officer_title_tv_num);
                    store_lay_office_title_Img= (ImageView) root.findViewById(R.id.store_lay_office_title_Img);
                    break;

                //comment title
                case 10:
                    rvCommentScore= (RatingView) root.findViewById(R.id.store_lay_comment_store_title_rv);
                    tvCommentScore= (TextView) root.findViewById(R.id.store_lay_comment_store_title_tv_score);
                    tvCommentNum= (TextView) root.findViewById(R.id.store_lay_comment_store_title_tv_num);
                    break;


                case TYPE_TO_ALL_COMMENT_STORE_TITLE:
                    mRLToAllComment = (RelativeLayout) root.findViewById(R.id.store_lay_to_all_comment_rl_container);
                    store_lay_to_all_comment_Title= (TextView) root.findViewById(R.id.store_lay_to_all_comment_Title);
                    store_lay_to_all_comment_ImgLayout= (LinearLayout) root.findViewById(R.id.store_lay_to_all_comment_ImgLayout);
                    break;

            }

        }


    }


    /**
     * 初始化 banner
     */
    private void initBanner(Banner mBanner, List<?> list){
        //显示圆形指示器
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //指示器居中
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        mBanner.setImages(list);
        mBanner.setImageLoader(new MyImageLoader());
        mBanner.start();
    }

    class MyImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //Picasso 加载图片
            //加载本地图片
            if(path instanceof Integer){
                Picasso.with(context).load((int)path).into(imageView);
            }

            //加载网络图片要压缩一下质量
            if(path instanceof String){
                OkHttpClient client=new OkHttpClient();
                OkHttpDownloader okHttpDownloader=new OkHttpDownloader(client);
                Picasso picasso=new Picasso.Builder(context).downloader(okHttpDownloader).build();
                picasso.load((String) path).error(R.mipmap.icon_load_faild).
                        into(imageView);
            }


        }

    }

}