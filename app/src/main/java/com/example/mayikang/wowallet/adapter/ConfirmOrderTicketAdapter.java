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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lifuy on 2017/9/28.
 */

public class ConfirmOrderTicketAdapter extends DelegateAdapter.Adapter<ConfirmOrderTicketAdapter.MyViewHolder> {

    public static final int TYPE_1 = 1;
    public static final int TYPE_2 = 2;
    public static final int TYPE_3 = 3;

    private Context mContext;
    private ArrayList<HashMap<String, Object>> data = new ArrayList<>();
    private LayoutInflater inflater;
    private int type = 0;
    private LayoutHelper helper;
    private RecyclerView.LayoutParams params;

    public ConfirmOrderTicketAdapter(Context mContext, ArrayList<HashMap<String, Object>> data, int type, LayoutHelper helper, RecyclerView.LayoutParams params) {
        this.mContext = mContext;
        this.data = data;
        this.type = type;
        this.helper = helper;
        this.params = params;
        this.inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ConfirmOrderTicketAdapter(Context mContext, ArrayList<HashMap<String, Object>> data, int type, LayoutHelper helper) {
        this(mContext, data, type, helper, new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
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
            default:
                return -1;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View mView = null;
        switch (i) {

            case TYPE_1:
                mView = inflater.inflate(R.layout.confirm_order_ticket_lay_a, viewGroup, false);
                break;
            case TYPE_2:
                mView = inflater.inflate(R.layout.confirm_order_ticjet_lay_b, viewGroup, false);
                break;
            case TYPE_3:
                mView = inflater.inflate(R.layout.confirm_order_ticket_lay_c, viewGroup, false);
                break;
        }


        return new MyViewHolder(mView, i);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        int itemViewType = getItemViewType(i);
        switch (itemViewType) {
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
        return data.size();
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return helper;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        //type=1
        TextView conOrderTicketLayAStoreName;

        //type=2
        ImageView conOrderTicketLayBImg;
        TextView conOrderTicketLayBMsg;
        TextView conOrderTicketLayBPrice;
        TextView conOrderTicketLayBNumber;
        LinearLayout conOrderTicketLayBLayout;

        //type=3
        TextView conOrderTicketLayCPayWay;
        EditText conOrderTicketLayCEdt;
        TextView conOrderTicketLayCNumber;
        TextView conOrderTicketLayCPrice;



        public MyViewHolder(View itemView, int type) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            switch (type) {
                case TYPE_1:
                    conOrderTicketLayAStoreName= (TextView) itemView.findViewById(R.id.con_order_ticket_lay_a_StoreName);
                    break;
                case TYPE_2:
                    conOrderTicketLayBImg= (ImageView) itemView.findViewById(R.id.con_order_ticket_lay_b_img);
                    conOrderTicketLayBMsg= (TextView) itemView.findViewById(R.id.con_order_ticket_lay_b_msg);
                    conOrderTicketLayBPrice= (TextView) itemView.findViewById(R.id.con_order_ticket_lay_b_Price);
                    conOrderTicketLayBNumber= (TextView) itemView.findViewById(R.id.con_order_ticket_lay_b_number);
                    conOrderTicketLayBLayout= (LinearLayout) itemView.findViewById(R.id.con_order_ticket_lay_b_layout);
                    break;
                case TYPE_3:
                    conOrderTicketLayCPayWay= (TextView) itemView.findViewById(R.id.con_order_ticket_lay_c_payWay);
                    conOrderTicketLayCEdt= (EditText) itemView.findViewById(R.id.con_order_ticket_lay_c_edt);
                    conOrderTicketLayCNumber= (TextView) itemView.findViewById(R.id.con_order_ticket_lay_c_number);
                    conOrderTicketLayCPrice= (TextView) itemView.findViewById(R.id.con_order_ticket_lay_c_price);
                    break;
            }
        }
    }
}
