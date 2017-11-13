package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.AdressAdapter;
import com.example.mayikang.wowallet.modle.javabean.AdressBean;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/main/act/AdressManager")
//收货地址管理
public class AdressManagerActivity extends BaseAppcompatActivity {

    @BindView(R.id.adress_rv)
    RecyclerView adressRv;
    @BindView(R.id.adress_mrl)
    MaterialRefreshLayout adressMrl;
    @BindView(R.id.adressNo_layout)
    LinearLayout adressNoLayout;

    private AdressAdapter adapter;
    private List<AdressBean> lists = new ArrayList<>();
    private LinearLayoutManager manager;
    private Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();

        adressMrl.setLoadMore(true);
        adressMrl.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
            }
        });

    }

    //加载数据
    private void initData() {
        for (int i = 0; i < 10; i++) {
            lists.add(new AdressBean());
        }
    }

    //初始化控件
    private void initView() {
        manager = new LinearLayoutManager(this);
        adapter = new AdressAdapter(lists, this);
        adressRv.setLayoutManager(manager);
        adressRv.setAdapter(adapter);
        //删除地址
        adapter.setCallBack(new AdressAdapter.CallBack() {
            @Override
            public void deleteItem(int position) {

            }

            @Override
            public void onCheckedChanged(int position, boolean isChecked) {
                for (int i = 0; i < lists.size(); i++) {
                    if ((i==position)) {
                        lists.get(i).setDefault(true);
                    }else {
                        lists.get(i).setDefault(false);
                    }
                }
                handlerPostAndNotifyAdapterNotifyDataSetChanged(handler,adressRv,adapter);
            }
        });

    }

    @Override
    public int initLayout() {
        return R.layout.activity_adress_manager;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.storemanage_linear_back, R.id.lin_add_adress})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //返回
            case R.id.storemanage_linear_back:
                finish();
                break;
            //添加地址
            case R.id.lin_add_adress:
                ARouter.getInstance().build("/main/act/EditAdress").navigation();
                break;
        }
    }

    //等待recycler绑定item的方法结束后再刷新页面
    protected void handlerPostAndNotifyAdapterNotifyDataSetChanged(final Handler handler, final RecyclerView recyclerView, final RecyclerView.Adapter adapter) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!recyclerView.isComputingLayout()) {
                    adapter.notifyDataSetChanged();
                } else {
                    handlerPostAndNotifyAdapterNotifyDataSetChanged(handler, recyclerView, adapter);
                }
            }
        });
    }
}
