package com.example.mayikang.wowallet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.event.NormalOpEvent;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.CollectioniBean;
import com.example.mayikang.wowallet.modle.javabean.StoreBean;
import com.example.mayikang.wowallet.ui.act.StoreActivity;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lifuyuan on 2017/5/10.
 */

public class MyCollectionAdapter extends RecyclerView.Adapter<MyCollectionAdapter.MyViewHolder> {
    private List<StoreBean> list=new ArrayList<>();
    private Context context;
    private HttpServiceImpl http;
    private LayoutInflater inflater;
    public MyCollectionAdapter(List<StoreBean> list, Context context) {
        this.list = list;
        this.context = context;
        if(inflater==null){
            inflater=LayoutInflater.from(context);
        }
        http = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.collectionitem,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.content.setText(list.get(position).getDetail());
        holder.title.setText(list.get(position).getName());
       /* PicassoUtil.getPicassoObject()
                .load(list.get(position).getLogo())
                .resize(DpUtils.dpToPx(context,80),DpUtils.dpToPx(context,80))
                .error(R.mipmap.icon_load_faild).into(holder.imageView);*/
        Glide.with(context).load(list.get(position).getLogo())
                .placeholder(R.drawable.ic_defult_load).crossFade()
                .error(R.drawable.ic_defult_error)
               .into(holder.imageView);


        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteCollection(list.get(position).getId(),holder.getLayoutPosition());
            }
        });

        holder.rlParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/main/act/store").withInt("id",list.get(position).getId()).navigation();
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title,content;
        ImageView imageView;
        Button btnDelete;
        RelativeLayout rlParent;

        public MyViewHolder(View convertView) {
            super(convertView);
            title= (TextView) convertView.findViewById(R.id.collection_tv_title);
            content= (TextView) convertView.findViewById(R.id.collection_tv_content);
            imageView= (ImageView) convertView.findViewById(R.id.collection_iv);
            btnDelete= (Button) convertView.findViewById(R.id.btnDelete);
            rlParent= (RelativeLayout) convertView.findViewById(R.id.item_collection_rl_parent);
        }
    }

    /**
     * 删除收藏
     * @param id
     */
    private void deleteCollection(int id, final int index) {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId",String.valueOf(UserAuthUtil.getUserId()));
        map.put("storeId",String.valueOf(id));
        http.doCommonPost(null, MainUrl.addOrDeleteCollectedStoreUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {

                if(!StringUtil.isBlank(resultStr)){
                    try {
                        JSONObject obj=new JSONObject(resultStr);
                        boolean result=obj.getBoolean("result");
                        String msg=obj.getString("msg");
                        if(result){
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                            //取消收藏成功
                            if("取消收藏！".equals(msg)){
                                EventBus.getDefault().post(new NormalOpEvent(1,index));
                            }

                        }else {
                            Toast.makeText(context, "操作异常", Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current) {

            }
        });


    }



}
