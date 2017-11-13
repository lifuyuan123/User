package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.OrderDetailMessageAdapter;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;


//订单详情页面
@Route(path = "/main/act/user/Order_Detail")
public class OrderDetailMessageActivity extends BaseAppcompatActivity {

    @BindView(R.id.order_detail_msg_back)
    LinearLayout orderDetailMsgBack;
    @BindView(R.id.order_detial_msg_rv)
    RecyclerView orderDetialMsgRv;
    @BindView(R.id.activity_order_detail_message)
    LinearLayout activityOrderDetailMessage;
    private DelegateAdapter adapter;
    private OrderDetailMessageAdapter TYPE_a,TYPE_b,TYPE_c,TYPE_d;
    private ArrayList<HashMap<String,Object>> typeAdata=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> typeBdata=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> typeCdata=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> typeDdata=new ArrayList<>();

    //装适配器的集合
    private ArrayList<DelegateAdapter.Adapter> data=new ArrayList<>();

    private HashMap<String,Object> typeAmap=new HashMap<>();
    private HashMap<String,Object> typeBmap=new HashMap<>();
    private HashMap<String,Object> typecmap=new HashMap<>();
    private HashMap<String,Object> typeDmap=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();


    }
    //初始化View
    private void initView() {
        //1.设置 LayoutManager
        VirtualLayoutManager layoutManager = new VirtualLayoutManager(this);
        orderDetialMsgRv.setLayoutManager(layoutManager);

        //2.设置组件复用回收池
        RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
        orderDetialMsgRv.setRecycledViewPool(recycledViewPool);

        initTypeA();
        initTypeB();
        initTypeC();
        initTypeD();
        adapter=new DelegateAdapter(layoutManager);
        adapter.setAdapters(data);
        orderDetialMsgRv.setAdapter(adapter);


    }

    //初始化适配器D
    private void initTypeD() {
        SingleLayoutHelper typeDHelper=new SingleLayoutHelper();
        typeDHelper.setItemCount(1);
        typeDdata.add(typeDmap);
        TYPE_d=new OrderDetailMessageAdapter(this,typeDdata,4,typeDHelper);
        data.add(TYPE_d);
    }

    //初始化适配器C
    private void initTypeC() {
        LinearLayoutHelper typeCHelper=new LinearLayoutHelper();
        typeCdata.add(typecmap);
        typeCdata.add(typecmap);
        typeCdata.add(typecmap);
        TYPE_c=new OrderDetailMessageAdapter(this,typeCdata,3,typeCHelper);
        data.add(TYPE_c);
    }

    //初始化适配器B
    private void initTypeB() {
         SingleLayoutHelper typeBHelper=new SingleLayoutHelper();
         typeBHelper.setItemCount(1);
         typeBdata.add(typeBmap);
         TYPE_b=new OrderDetailMessageAdapter(this,typeBdata,2,typeBHelper);
         data.add(TYPE_b);
    }

    //初始化TypeA适配
    private void initTypeA() {
        SingleLayoutHelper typeAHelper=new SingleLayoutHelper();
        typeAHelper.setItemCount(1);
        typeAdata.add(typeAmap);
        TYPE_a=new OrderDetailMessageAdapter(this,typeAdata,1,typeAHelper);
        data.add(TYPE_a);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_order_detail_message;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.order_detail_msg_back)
    public void onViewClicked() {
        finish();
    }
}
