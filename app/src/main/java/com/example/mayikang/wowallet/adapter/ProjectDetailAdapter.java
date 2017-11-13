package com.example.mayikang.wowallet.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.modle.javabean.ImageBean;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;

import java.util.List;

/**
 * Created by mayikang on 17/5/18.
 */

public class ProjectDetailAdapter extends RecyclerView.Adapter<ProjectDetailAdapter.ImageHolder> {


    private Context context;
    private List<ImageBean> data;
    private LayoutInflater inflater;
    private Activity activity;

    public ProjectDetailAdapter(Context context,List<ImageBean> data){
        this.context=context;
        this.data=data;
        if(inflater==null){
            inflater=LayoutInflater.from(context);
        }
        if(context instanceof Activity){
            this.activity= (Activity) context;
        }
    }


    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_project_detail,null);

        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {

        ImageBean bean=data.get(position);

        DisplayMetrics metrics=new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screewidth=metrics.widthPixels;
        //int screeHeight=metrics.heightPixels;
        double width=bean.getWidth();
        double height=bean.getHeight();
        int realhe= (int) ((height/width)*screewidth-20);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(screewidth-20,realhe);
        holder.iv.setLayoutParams(layoutParams);

       /* PicassoUtil.getPicassoObject()
                .load(data.get(position).getUrl())
                .error(R.mipmap.icon_load_faild).into(holder.iv);*/
        //.resize(DpUtils.dpToPx(context,80),DpUtils.dpToPx(context,180))
        Glide.with(context).load(data.get(position).getUrl())
                .placeholder(R.drawable.ic_defult_load).crossFade()
                .error(R.drawable.ic_defult_error)
                .into(holder.iv);


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ImageHolder extends RecyclerView.ViewHolder{
        ImageView iv;
        public ImageHolder(View itemView) {
            super(itemView);
            iv= (ImageView) itemView.findViewById(R.id.iv_project_detail);
        }
    }
}
