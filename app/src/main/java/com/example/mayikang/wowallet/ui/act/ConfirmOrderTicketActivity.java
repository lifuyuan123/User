package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.ConfirmOrderTicketAdapter;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/main/act/ConfirmOrderTicket")
//确认订单（券）
public class ConfirmOrderTicketActivity extends BaseAppcompatActivity {

    @BindView(R.id.confirm_order_ticket_rv)
    RecyclerView confirmOrderTicketRv;
    @BindView(R.id.confirm_order_ticket_price)
    TextView confirmOrderTicketPrice;


    private ConfirmOrderTicketAdapter TYPE_a,TYPE_b,TYPE_c;
    private DelegateAdapter adapter;
    private ArrayList<DelegateAdapter.Adapter> data=new ArrayList<>();
    private HashMap<String,Object> aMap=new HashMap<>();
    private HashMap<String,Object> cMap=new HashMap<>();

    private ArrayList<HashMap<String,Object>> typeAdata=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> typeBdata=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> typeCdata=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        //1.设置 LayoutManager
        VirtualLayoutManager layoutManager = new VirtualLayoutManager(this);
        confirmOrderTicketRv.setLayoutManager(layoutManager);

        //2.设置组件复用回收池
        RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
        confirmOrderTicketRv.setRecycledViewPool(recycledViewPool);

        initTypeA();
        initTypeB();
        initTypeC();

        adapter=new DelegateAdapter(layoutManager,false);
        adapter.setAdapters(data);
        confirmOrderTicketRv.setAdapter(adapter);
    }

    private void initTypeC() {
        SingleLayoutHelper typeAhelper=new SingleLayoutHelper();
        typeAhelper.setItemCount(1);
        typeCdata.add(cMap);
        TYPE_c=new ConfirmOrderTicketAdapter(this,typeAdata,3,typeAhelper);
        data.add(TYPE_c);
    }

    private void initTypeB() {
        LinearLayoutHelper typeBhelper=new LinearLayoutHelper();
        for (int i = 0; i < 3; i++) {
            HashMap<String,Object> td=new HashMap<>();
            typeBdata.add(td);
        }
        TYPE_b=new ConfirmOrderTicketAdapter(this,typeBdata,2,typeBhelper);
        data.add(TYPE_b);
    }

    private void initTypeA() {
        SingleLayoutHelper typeAhelper=new SingleLayoutHelper();
        typeAhelper.setItemCount(1);
        typeAdata.add(aMap);
        TYPE_a=new ConfirmOrderTicketAdapter(this,typeAdata,1,typeAhelper);
        data.add(TYPE_a);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_confirm_order_ticket;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.confirm_order_shop_back, R.id.confirm_order_ticket_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.confirm_order_shop_back:
                finish();
                break;
            case R.id.confirm_order_ticket_btn:
                break;
        }
    }
}
