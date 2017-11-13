package com.example.mayikang.wowallet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.example.mayikang.wowallet.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by XiaoHaoWit on 2017/9/25.
 */

public class ConfirmOrderShopAdapter extends DelegateAdapter.Adapter<ConfirmOrderShopAdapter.ConfirmOrderShopHolder> {

    public static final int TYPE_1 = 1;
    public static final int TYPE_2 = 2;
    public static final int TYPE_3 = 3;

    private Context mContext;
    private ArrayList<HashMap<String,Object>> data;
    private LayoutInflater inflater;
    private int type=0;
    private LayoutHelper helper;
    private RecyclerView.LayoutParams params;

    public ConfirmOrderShopAdapter(Context mContext, ArrayList<HashMap<String, Object>> data, int type, LayoutHelper helper, RecyclerView.LayoutParams params) {
        this.mContext = mContext;
        this.data = data;
        this.type = type;
        this.helper = helper;
        this.params = params;
        this.inflater= (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ConfirmOrderShopAdapter(Context mContext, ArrayList<HashMap<String, Object>> data, int type, LayoutHelper helper){
       this(mContext,data,type,helper,new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
               ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return helper;
    }



    @Override
    public int getItemViewType(int position) {
        switch (type){
            case 1:
                return TYPE_1;
            case 2:
                return TYPE_2;
            case 3:
                return TYPE_3;
            default:
                return -1;
        }
    }

    @Override
    public ConfirmOrderShopHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mView=null;
        switch (viewType){

            case TYPE_1:
                mView=inflater.inflate(R.layout.confirm_order_shop_lay_a,parent,false);
                break;
            case TYPE_2:
                mView=inflater.inflate(R.layout.confirm_order_shop_lay_b,parent,false);
                break;
            case TYPE_3:
                mView=inflater.inflate(R.layout.confirm_order_shop_lay_c,parent,false);
                break;
        }


        return new ConfirmOrderShopHolder(mView,viewType);
    }

    @Override
    public void onBindViewHolder(ConfirmOrderShopHolder holder, int position) {

        int itemViewType = getItemViewType(position);
        switch (itemViewType){
            case TYPE_1:
                break;
            case TYPE_2:
                break;
            case TYPE_3:
                break;
        }

    }

    @Override
    public int getItemCount() {
        return null==data?0:data.size();
    }

    static class ConfirmOrderShopHolder extends RecyclerView.ViewHolder{
        //type1 的控件
        TextView con_order_shop_lay_a_UserName;
        TextView con_order_shop_lay_a_location;
        TextView con_order_shop_lay_a_StoreName;
        LinearLayout con_order_shop_lay_a_layout;
        //type2 的控件
        LinearLayout con_order_shop_lay_b_layout;
        TextView con_order_shop_lay_b_msg;
        ImageView con_order_shop_lay_b_img;
        TextView    con_order_shop_lay_b_sort;
        TextView con_order_shop_lay_b_Price;
        TextView con_order_shop_lay_b_number;
        //type3 的布局
        TextView con_order_shop_lay_c_mallMode;
        TextView con_order_shop_lay_c_payWay;
        EditText con_order_shop_lay_c_edt;
        TextView con_order_shop_lay_c_number;
        TextView con_order_shop_lay_c_price;

        public ConfirmOrderShopHolder(View root,int type) {
            super(root);

            switch (type){
                case TYPE_1:
                    con_order_shop_lay_a_UserName= (TextView) root.findViewById(R.id.con_order_shop_lay_a_UserName);
                    con_order_shop_lay_a_location= (TextView) root.findViewById(R.id.con_order_shop_lay_a_location);
                    con_order_shop_lay_a_StoreName= (TextView) root.findViewById(R.id.con_order_shop_lay_a_StoreName);
                    con_order_shop_lay_a_layout= (LinearLayout) root.findViewById(R.id.con_order_shop_lay_a_layout);
                    break;
                case TYPE_2:
                    con_order_shop_lay_b_layout= (LinearLayout) root.findViewById(R.id.con_order_shop_lay_b_layout);
                    con_order_shop_lay_b_msg= (TextView) root.findViewById(R.id.con_order_shop_lay_b_msg);
                    con_order_shop_lay_b_Price= (TextView) root.findViewById(R.id.con_order_shop_lay_b_Price);
                    con_order_shop_lay_b_number= (TextView) root.findViewById(R.id.con_order_shop_lay_b_number);
                    con_order_shop_lay_b_sort= (TextView) root.findViewById(R.id.con_order_shop_lay_b_sort);
                    con_order_shop_lay_b_img= (ImageView) root.findViewById(R.id.con_order_shop_lay_b_img);
                    break;
                case TYPE_3:
                    con_order_shop_lay_c_mallMode= (TextView) root.findViewById(R.id.con_order_shop_lay_c_mallMode);
                    con_order_shop_lay_c_payWay= (TextView) root.findViewById(R.id.con_order_shop_lay_c_payWay);
                    con_order_shop_lay_c_number= (TextView) root.findViewById(R.id.con_order_shop_lay_c_number);
                    con_order_shop_lay_c_price= (TextView) root.findViewById(R.id.con_order_shop_lay_c_price);
                    con_order_shop_lay_c_edt= (EditText) root.findViewById(R.id.con_order_shop_lay_c_edt);
                    break;


            }

        }
    }
}
