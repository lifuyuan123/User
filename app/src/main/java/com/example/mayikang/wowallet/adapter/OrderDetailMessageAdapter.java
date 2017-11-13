package com.example.mayikang.wowallet.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.example.mayikang.wowallet.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by XiaoHaoWit on 2017/9/21.
 * 订单详情的适配器（复杂的订单）
 *
 */

public class OrderDetailMessageAdapter extends DelegateAdapter.Adapter<OrderDetailMessageAdapter.OrderDetailMessageHolder> {


    //对应的订单界面的四种布局
    public static final int TYPE_a = 1;
    public static final int TYPE_b = 2;
    public static final int TYPE_c = 3;
    public static final int TYPE_d = 4;


    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String,Object>> data;
    private int type=0;
    private LayoutHelper helper;
    private RecyclerView.LayoutParams params;

    public OrderDetailMessageAdapter(Context mContext, ArrayList<HashMap<String, Object>> data, int type, LayoutHelper helper, RecyclerView.LayoutParams params) {
        this.mContext = mContext;
        this.data = data;
        this.type = type;
        this.helper = helper;
        this.params = params;
        this.inflater= (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public OrderDetailMessageAdapter(Context mContext,ArrayList<HashMap<String, Object>> data, int type,LayoutHelper helper){
        this(mContext,data,type, helper,new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
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
                return TYPE_a;
            case 2:
                return TYPE_b;
            case 3:
                return TYPE_c;
            case 4:
                return TYPE_d;
            default:
                return -1;
        }
    }

    @Override
    public OrderDetailMessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=null;
        switch (viewType){
            case TYPE_a:
                view=inflater.inflate(R.layout.order_detail_lay_a,parent,false);
                break;
            case TYPE_b:
                view=inflater.inflate(R.layout.order_detail_lay_b,parent,false);
                break;
            case TYPE_c:
                view=inflater.inflate(R.layout.order_detail_lay_c,parent,false);
                break;
            case TYPE_d:
                view=inflater.inflate(R.layout.order_detail_lay_d,parent,false);
                break;
        }

        return new OrderDetailMessageHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(OrderDetailMessageHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType){
            case TYPE_a:

                break;
            case TYPE_b:

                break;
            case TYPE_c:

                break;
            case TYPE_d:

                break;
        }


    }

    @Override
    public int getItemCount() {
        return null==data?0:data.size();
    }



    static class OrderDetailMessageHolder extends RecyclerView.ViewHolder{
        //TYPE_a的控件
        TextView order_detail_lay_a_orderState;
        TextView order_detail_lay_a_remark;
        TextView order_detail_lay_a_expressMsg;
        TextView order_detail_lay_a_expressDate;
        RelativeLayout order_detail_lay_a_expressNext;
        TextView order_detail_lay_a_Name;
        TextView order_detail_lay_a_Phone;
        TextView order_detail_lay_a_expressLocation;
        TextView order_detail_lay_a_UserRemark;
        //TYPE_b的控件
        TextView order_detail_lay_b_StoreNome;
        LinearLayout order_detail_lay_b_GotoStore;
        //TYPE_c的控件
        LinearLayout order_detail_lay_c_layout;
        ImageView order_detail_lay_c_StorePic;
        TextView order_detail_lay_c_goodsMsg;
        TextView order_detail_lay_c_Price;
        TextView order_detail_lay_c_Number;
        TextView order_detail_lay_c_sort;
        Button order_detail_lay_c_goToComment;
        Button order_detail_lay_c_refundMoney;
        //TYPE_d的控件
        TextView order_detail_lay_d_totalPrice;
        TextView order_detail_lay_d_freMoney;
        TextView order_detail_lay_d_orderPrice;
        LinearLayout order_detail_lay_d_CallPhone;
        Button order_detail_lay_d_Copybtn;
        TextView order_detail_lay_d_OrderNumber;
        TextView order_detail_lay_d_InsterTime;
        TextView order_detail_lay_d_payTime;
        TextView order_detail_lay_d_shipmentTime;
        TextView order_detail_lay_d_makeBargainTime;


        public OrderDetailMessageHolder(View root,int viewType) {
            super(root);
            switch (viewType){

                case TYPE_a:
                    order_detail_lay_a_orderState= (TextView) root.findViewById(R.id.order_detail_lay_a_orderState);
                    order_detail_lay_a_remark= (TextView) root.findViewById(R.id.order_detail_lay_a_remark);
                    order_detail_lay_a_expressMsg= (TextView) root.findViewById(R.id.order_detail_lay_a_expressMsg);
                    order_detail_lay_a_expressDate= (TextView) root.findViewById(R.id.order_detail_lay_a_expressDate);
                    order_detail_lay_a_expressNext= (RelativeLayout) root.findViewById(R.id.order_detail_lay_a_expressNext);
                    order_detail_lay_a_Name= (TextView) root.findViewById(R.id.order_detail_lay_a_Name);
                    order_detail_lay_a_Phone= (TextView) root.findViewById(R.id.order_detail_lay_a_Phone);
                    order_detail_lay_a_expressLocation= (TextView) root.findViewById(R.id.order_detail_lay_a_expressLocation);
                    order_detail_lay_a_UserRemark= (TextView) root.findViewById(R.id.order_detail_lay_a_UserRemark);
                    break;
                case TYPE_b:
                    order_detail_lay_b_StoreNome= (TextView) root.findViewById(R.id.order_detail_lay_b_StoreNome);
                    order_detail_lay_b_GotoStore= (LinearLayout) root.findViewById(R.id.order_detail_lay_b_GotoStore);
                    break;
                case TYPE_c:
                    order_detail_lay_c_layout= (LinearLayout) root.findViewById(R.id.order_detail_lay_c_layout);
                    order_detail_lay_c_StorePic= (ImageView) root.findViewById(R.id.order_detail_lay_c_StorePic);
                    order_detail_lay_c_goodsMsg= (TextView) root.findViewById(R.id.order_detail_lay_c_goodsMsg);
                    order_detail_lay_c_Price= (TextView) root.findViewById(R.id.order_detail_lay_c_Price);
                    order_detail_lay_c_Number= (TextView) root.findViewById(R.id.order_detail_lay_c_Number);
                    order_detail_lay_c_sort= (TextView) root.findViewById(R.id.order_detail_lay_c_sort);
                    order_detail_lay_c_goToComment= (Button) root.findViewById(R.id.order_detail_lay_c_goToComment);
                    order_detail_lay_c_refundMoney= (Button) root.findViewById(R.id.order_detail_lay_c_refundMoney);
                    break;
                case TYPE_d:
                    order_detail_lay_d_totalPrice= (TextView) root.findViewById(R.id.order_detail_lay_d_totalPrice);
                    order_detail_lay_d_freMoney= (TextView) root.findViewById(R.id.order_detail_lay_d_freMoney);
                    order_detail_lay_d_orderPrice= (TextView) root.findViewById(R.id.order_detail_lay_d_orderPrice);
                    order_detail_lay_d_CallPhone= (LinearLayout) root.findViewById(R.id.order_detail_lay_d_CallPhone);
                    order_detail_lay_d_Copybtn= (Button) root.findViewById(R.id.order_detail_lay_d_Copybtn);
                    order_detail_lay_d_OrderNumber= (TextView) root.findViewById(R.id.order_detail_lay_d_OrderNumber);
                    order_detail_lay_d_InsterTime= (TextView) root.findViewById(R.id.order_detail_lay_d_InsterTime);
                    order_detail_lay_d_payTime= (TextView) root.findViewById(R.id.order_detail_lay_d_payTime);
                    order_detail_lay_d_shipmentTime= (TextView) root.findViewById(R.id.order_detail_lay_d_shipmentTime);
                    order_detail_lay_d_makeBargainTime= (TextView) root.findViewById(R.id.order_detail_lay_d_makeBargainTime);
                    break;
            }

        }
    }
}
