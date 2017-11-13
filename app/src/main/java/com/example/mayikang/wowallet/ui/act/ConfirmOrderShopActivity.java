package com.example.mayikang.wowallet.ui.act;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.ConfirmOrderShopAdapter;
import com.example.mayikang.wowallet.ui.xwidget.popupwindow.MyPopupWindow;
import com.example.mayikang.wowallet.util.PopuWindownUtil;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/*
 * 确认订单 （商城模式）
 */

//@Route(path = "/main/act/user/confirm_order_shop")
@Route(path = "/main/act/confirm_order_shop")

public class ConfirmOrderShopActivity extends BaseAppcompatActivity {

    @BindView(R.id.confirm_order_shop_back)
    RelativeLayout confirmOrderShopBack;
    @BindView(R.id.confirm_order_shop_rv)
    RecyclerView confirmOrderShopRv;
    @BindView(R.id.confirm_order_shop_price)
    TextView confirmOrderShopPrice;
    @BindView(R.id.confirm_order_shop_btn)
    Button confirmOrderShopBtn;
    @BindView(R.id.activity_confirm_order_shop)
    LinearLayout activityConfirmOrderShop;
    @BindView(R.id.confirm_order_button)
    LinearLayout confirm_order_button;

    private ConfirmOrderShopAdapter TYPE_a,TYPE_b,TYPE_c;
    private DelegateAdapter adapter;
    private ArrayList<DelegateAdapter.Adapter> data=new ArrayList<>();
    private HashMap<String,Object> aMap=new HashMap<>();
    private HashMap<String,Object> cMap=new HashMap<>();

    private ArrayList<HashMap<String,Object>> typeAdata=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> typeBdata=new ArrayList<>();
    private ArrayList<HashMap<String,Object>> typeCdata=new ArrayList<>();

    private  MyPopupWindow pupopWindown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        initView();

    }
    //初始化付款的对话框
    private void initPopupWindown() {


        View mView= LayoutInflater.from(this).inflate(R.layout.layout_my_popuwindow,null);
        PopupWindow mPopupWindow=new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);


        int windowPos[]= PopuWindownUtil.calculatePopWindowPos(confirm_order_button,mView);
        mPopupWindow.showAtLocation(confirm_order_button, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);


    }

    //初始化布局
    private void initView() {

        //1.设置 LayoutManager
        VirtualLayoutManager layoutManager = new VirtualLayoutManager(this);
        confirmOrderShopRv.setLayoutManager(layoutManager);

        //2.设置组件复用回收池
        RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
        confirmOrderShopRv.setRecycledViewPool(recycledViewPool);

        initTypeA();
        initTypeB();
        initTypeC();

        adapter=new DelegateAdapter(layoutManager,false);
        adapter.setAdapters(data);
        confirmOrderShopRv.setAdapter(adapter);


    }

    //初始化typeC
    private void initTypeC() {
        SingleLayoutHelper typeAhelper=new SingleLayoutHelper();
        typeAhelper.setItemCount(1);
        typeCdata.add(cMap);
        TYPE_c=new ConfirmOrderShopAdapter(this,typeAdata,3,typeAhelper);
        data.add(TYPE_c);

    }

    //初始化typeB
    private void initTypeB() {
        LinearLayoutHelper typeBhelper=new LinearLayoutHelper();
        HashMap<String,Object> td=new HashMap<>();
        typeBdata.add(td);
        TYPE_b=new ConfirmOrderShopAdapter(this,typeBdata,2,typeBhelper);
        data.add(TYPE_b);

    }

    //初始化typeA
    private void initTypeA() {
        SingleLayoutHelper typeAhelper=new SingleLayoutHelper();
        typeAhelper.setItemCount(1);
        typeAdata.add(aMap);
        TYPE_a=new ConfirmOrderShopAdapter(this,typeAdata,1,typeAhelper);
        data.add(TYPE_a);

    }

    @Override
    public int initLayout() {
        return R.layout.activity_confirm_order_shop;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.confirm_order_shop_back, R.id.confirm_order_shop_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.confirm_order_shop_back:
                finish();
                break;
            case R.id.confirm_order_shop_btn:
                //弹起支付框
                //pupopWindown.showAtLocation(confirm_order_button, Gravity.BOTTOM,0,0);
                //pupopWindown.showAsDropDown();
                initPopupWindown();
                break;
        }
    }
}
