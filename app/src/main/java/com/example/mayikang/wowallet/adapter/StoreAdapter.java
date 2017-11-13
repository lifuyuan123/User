package com.example.mayikang.wowallet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.bumptech.glide.Glide;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.modle.javabean.StoreBean;
import com.github.ornolfr.ratingview.RatingView;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.squareup.picasso.Callback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by haohaoliu on 2017/7/30.
 * explain:店铺的适配器
 */

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {
    private Context mContext;
    private LayoutInflater inflater;
    private List<StoreBean> data;

    public StoreAdapter(Context mContext, List<StoreBean> data) {
        this.mContext = mContext;
        this.data = data;
        this.inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public StoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.home_lay_6, null);
        return new StoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StoreViewHolder holder, final int position) {
       /* PicassoUtil.getPicassoObject()
                .load(data.get(position).getLogo())
                .resize(DpUtils.dpToPx(mContext, 80), DpUtils.dpToPx(mContext, 80))
                .error(R.mipmap.icon_load_faild).into(holder.homeLay6IvLogo);*/
        if(!StringUtil.isBlank(data.get(position).getLogo().trim())){
            Glide.with(mContext).load(data.get(position).getLogo())
                    .placeholder(R.drawable.ic_defult_load).crossFade()
                    .error(R.drawable.ic_defult_error)
                    .into(holder.homeLay6IvLogo);
        }else {
            holder.homeLay6IvLogo.setImageResource(R.drawable.ic_defult_error);
        }




        holder.homeLay6TvName.setText(data.get(position).getName()+"");
        if (data.get(position).getLongitude() == 0d || data.get(position).getLatitude() == 0d) {
            holder.homeLay6TvDistance.setText("距离计算错误");
        } else {
            holder.homeLay6TvDistance.setText(calDistance(data.get(position).getLongitude(), data.get(position).getLatitude()) + "Km");
        }
        holder.homeLay6TvDescribe.setText(data.get(position).getDetail()+"");
        float rating= (float) data.get(position).getScore();
        holder.homeLay6RatingView.setRating(rating);
        holder.homeLay6Rq.setText("人气："+data.get(position).getPopularity()+"");
        holder.itemHomeLay6LlContainer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Log.e("adapter",data.get(position).getType()+"");
                switch (data.get(position).getType()){
                    case 1:
                        ARouter.getInstance().build("/main/act/store").withInt("id", data.get(position).getId()).navigation();
                        break;
                    case 2:
                        Log.e("adapter",data.get(position).getType()+"");
                        ARouter.getInstance().build("/main/act/store2").withInt("id",data.get(position).getId()).navigation();
                        break;

                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    static class StoreViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.home_lay_6_iv_logo)
        ImageView homeLay6IvLogo;
        @BindView(R.id.home_lay_6_tv_name)
        TextView homeLay6TvName;
        @BindView(R.id.home_lay_6_tv_distance)
        TextView homeLay6TvDistance;
        @BindView(R.id.home_lay_6_tv_describe)
        TextView homeLay6TvDescribe;
        @BindView(R.id.home_lay_6_ratingView)
        RatingView homeLay6RatingView;
        @BindView(R.id.home_lay_6_rq)
        TextView homeLay6Rq;
        @BindView(R.id.item_home_lay_6_ll_container)
        LinearLayout itemHomeLay6LlContainer;

        public StoreViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //计算距离
    private String calDistance(double longt, double lat) {

        LatLng latLng = new LatLng(Double.valueOf((String) SPFUtil.get(Tag.TAG_LATITUDE, "0")), Double.valueOf((String) SPFUtil.get(Tag.TAG_LONGITUDE, "0")));
        float distance = AMapUtils.calculateLineDistance(latLng, new LatLng(lat, longt)) / 1000;
        String str = String.valueOf(distance);
        str = str.substring(0, str.indexOf(".") + 2);
        return str;
    }
}
