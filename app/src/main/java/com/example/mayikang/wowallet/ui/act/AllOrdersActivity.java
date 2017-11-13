package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.AllOrderAdapter;
import com.example.mayikang.wowallet.modle.javabean.GoodsBean;
import com.example.mayikang.wowallet.modle.javabean.OrderTypeBean;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

//全部订单
@Route(path = "/main/act/user/AllOrders")
public class AllOrdersActivity extends BaseAppcompatActivity {

    @BindView(R.id.allorder_rb_all)
    RadioButton allorderRbAll;
    @BindView(R.id.allorder_rb_obligation)
    RadioButton allorderRbObligation;
    @BindView(R.id.allorder_rb_delivery)
    RadioButton allorderRbDelivery;
    @BindView(R.id.allorder_rb_goods_receipt)
    RadioButton allorderRbGoodsReceipt;
    @BindView(R.id.allorder_rb_evaluation)
    RadioButton allorderRbEvaluation;
    @BindView(R.id.allorder_rg)
    RadioGroup allorderRg;
    @BindView(R.id.allorder_rv)
    RecyclerView allorderRv;
    @BindView(R.id.allorder_mrl)
    MaterialRefreshLayout allorderMrl;
    private AllOrderAdapter adapter;
    private List<OrderTypeBean> list = new ArrayList<>();
    private LinearLayoutManager manager;
    @Autowired(name = "type")
    int type;//1-待支付,2-代发货,3-待收货,4-待评价

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
    }

    private void initData() {
        for (int i = 0; i < 8; i++) {
            OrderTypeBean bean= new OrderTypeBean();
            if(i<3){
                bean.setType(1);
                ArrayList<GoodsBean> goodsBeen = new ArrayList<>();
                    goodsBeen.add(new GoodsBean());
                   bean.setGoodsBeanList(goodsBeen);
            }else if(i<6){
                bean.setType(2);
                ArrayList<GoodsBean> goodsBeen = new ArrayList<>();
                for (int j = 0; j < 2; j++) {
                    goodsBeen.add(new GoodsBean());
                }
                bean.setGoodsBeanList(goodsBeen);
            }else {
                bean.setType(3);
                ArrayList<GoodsBean> goodsBeen = new ArrayList<>();
                for (int j = 0; j < 3; j++) {
                    goodsBeen.add(new GoodsBean());
                }
                bean.setGoodsBeanList(goodsBeen);
            }
            list.add(bean);
        }
    }

    private void initView() {
        //1-待支付,2-代发货,3-待收货,4-待评价
        switch (type){
            case 1:
                allorderRbObligation.setChecked(true);
                break;
            case 2:
                allorderRbDelivery.setChecked(true);
                break;
            case 3:
                allorderRbGoodsReceipt.setChecked(true);
                break;
            case 4:
                allorderRbEvaluation.setChecked(true);
                break;
        }


        manager=new LinearLayoutManager(this);
        adapter=new AllOrderAdapter(list,this);
        allorderRv.setLayoutManager(manager);
        allorderRv.setAdapter(adapter);

        allorderMrl.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                allorderMrl.finishRefresh();
            }
        });


    }

    @Override
    public int initLayout() {
        return R.layout.activity_all_orders;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.rel_all_rders_back)
    public void onViewClicked() {
        finish();
    }
}
