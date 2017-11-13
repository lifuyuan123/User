package com.example.mayikang.wowallet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.modle.javabean.AdressBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lifuy on 2017/8/11.
 */

public class AdressAdapter extends RecyclerView.Adapter<AdressAdapter.MyViewHolder> {

    private List<AdressBean> list = new ArrayList<>();
    private Context context;

    public AdressAdapter(List<AdressBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adress_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final AdressBean bean = list.get(position);
        //编辑地址
        holder.adressLinEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/main/act/EditAdress").withObject("bean",bean).navigation();
            }
        });
        //删除地址
        holder.adressLinDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null) {
                    callBack.deleteItem(position);
                }
            }
        });


        holder.adressCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bean.setDefault(isChecked);
                if(isChecked){
                    //单选
                    if(callBack!=null){
                        callBack.onCheckedChanged(position,isChecked);
                    }
                    holder.adressTvCheck.setText("已为默认");
                }else {
                    holder.adressTvCheck.setText("设为默认");
                }
            }
        });

                if (bean.isDefault()) {
                    holder.adressCheckbox.setChecked(true);
                }else {
                    holder.adressCheckbox.setChecked(false);
                }






//        holder.adressTvName.setText(bean.getName());
//        holder.adressTvPhone.setText(bean.getPhone());
//        holder.adressTvAdress.setText(bean.getAdressRegion()+bean.getAdress());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.adress_tv_name)
        TextView adressTvName;
        @BindView(R.id.adress_tv_phone)
        TextView adressTvPhone;
        @BindView(R.id.adress_tv_adress)
        TextView adressTvAdress;
        @BindView(R.id.adress_checkbox)
        CheckBox adressCheckbox;
        @BindView(R.id.adress_tv_check)
        TextView adressTvCheck;
        @BindView(R.id.adress_lin_choice)
        LinearLayout adressLinChoice;
        @BindView(R.id.adress_lin_edit)
        LinearLayout adressLinEdit;
        @BindView(R.id.adress_lin_delete)
        LinearLayout adressLinDelete;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface CallBack {
        void deleteItem(int position);
        void onCheckedChanged(int position, boolean isChecked);
    }

    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
}
