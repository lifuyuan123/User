package com.example.mayikang.wowallet.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.modle.javabean.PhotoBean;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;

import java.io.File;
import java.util.List;


/**
 * Created by haohaoliu on 2017/5/16.
 */

public class EvaluateAdapter extends RecyclerView.Adapter<EvaluateAdapter.EvaluateViewHolder> {
    private Context mContext;
    private List<PhotoBean> data;
    private LayoutInflater inflater;
    private int type=-1;
    private  EvaluateAdapterCallBack callBack;

    public EvaluateAdapter(List<PhotoBean> data, Context mContext) {
        this.data = data;
        this.mContext = mContext;
        inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemViewType(int position) {
        if(data.get(position).getType().equals("local")){
            type=1;
        }else {
            type=2;
        }
        return type;
    }

    @Override
    public EvaluateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         View mView = null;

        switch (viewType){
            case 1:
                mView=inflater.inflate(R.layout.store_photo_head,null);
                break;
            case 2:
                mView=inflater.inflate(R.layout.item_store_gallery,null);
                break;
        }
        return new EvaluateViewHolder(mView,viewType);
    }

    @Override
    public void onBindViewHolder(final EvaluateViewHolder holder, int position) {
        switch (getItemViewType(position)){
            case 1:
              holder.lay_2_add_img.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      if(callBack!=null){
                          callBack.addImg();
                      }
                  }
              });
                break;
            case 2:
                PhotoBean bean=data.get(position);
                PicassoUtil.getPicassoObject().load(new File(bean.getUrl())).into(holder.store_photo_gallery);
                holder.icon_delete_red.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(callBack!=null){
                            callBack.delImg(holder.getLayoutPosition());
                        }
                    }
                });
                break;
        }
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class EvaluateViewHolder extends RecyclerView.ViewHolder {
        CardView lay_2_add_img;
        ImageView store_photo_gallery;
        ImageView icon_delete_red;


        public EvaluateViewHolder(View itemView,int type) {
            super(itemView);
            switch (type){
                case 1:
                    lay_2_add_img= (CardView) itemView.findViewById(R.id.lay_2_add_img);
                    break;
                case 2:
                    store_photo_gallery= (ImageView) itemView.findViewById(R.id.store_photo_gallery);
                    icon_delete_red= (ImageView) itemView.findViewById(R.id.store_photo_del);
                    break;
            }
        }
    }

    public interface EvaluateAdapterCallBack{
        public void addImg();
        public void delImg(int position);
    }

    public void setListener(EvaluateAdapterCallBack callBack){
        this.callBack=callBack;
    }

}
