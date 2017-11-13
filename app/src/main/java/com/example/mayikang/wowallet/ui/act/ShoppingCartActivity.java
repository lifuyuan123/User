package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.cjj.MaterialRefreshLayout;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.ShoppingCarAdapter;
import com.example.mayikang.wowallet.modle.javabean.Store2Bean;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/main/act/ShoppingCart")
//购物车
public class ShoppingCartActivity extends BaseAppcompatActivity {

    @BindView(R.id.shopping_tv_edit)
    TextView shoppingTvEdit;
    @BindView(R.id.shopping_checkbox_all)
    CheckBox shoppingCheckboxAll;
    @BindView(R.id.shopping_rv)
    RecyclerView shoppingRv;
    @BindView(R.id.shopping_refresh)
    MaterialRefreshLayout shoppingRefresh;
    @BindView(R.id.store2No_layout)
    LinearLayout store2NoLayout;
    @BindView(R.id.shopping_lin_delete)
    LinearLayout shoppingLinDelete;
    @BindView(R.id.shopping_tv_allmoney)
    TextView shoppingTvAllmoney;
    @BindView(R.id.shopping_lin_settlement)
    LinearLayout shoppingLinSettlement;

    @Autowired(name = "url")
    String url;
    @Autowired(name = "name")
    String name;

    @BindView(R.id.shopping_iv_icon)
    ImageView shoppingIvIcon;
    @BindView(R.id.shopping_tv_name)
    TextView shoppingTvName;


    private ShoppingCarAdapter adapter;
    private LinearLayoutManager manager;
    private List<Store2Bean> list = new ArrayList<>();

    private boolean isShow = false;//判断是否编辑状态
    private boolean isChoiceALL = false;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            list.add(new Store2Bean());
        }

    }

    private void initView() {
        manager = new LinearLayoutManager(this);
        adapter = new ShoppingCarAdapter(list, this);
        shoppingRv.setLayoutManager(manager);
        shoppingRv.setAdapter(adapter);

        //加载商家头像
        Glide.with(this).load(url)
                .error(R.mipmap.icon_load_faild)
                .into(shoppingIvIcon);

        //加载商家名字
        shoppingTvName.setText(name);

        //全选监听
        shoppingCheckboxAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    adapter.choiceAll();
                    isChoiceALL = true;
                } else {
                    if (isChoiceALL) {
                        isChoiceALL = false;
                        adapter.cancleAll();
                    }
                }
                //刷新界面
                handlerPostAndNotifyAdapterNotifyDataSetChanged(handler, shoppingRv, adapter);
            }
        });

        //当全选的状态下点击了item后不再显示全选状态
        adapter.setCallBack(new ShoppingCarAdapter.CallBack() {
            @Override
            public void onItemCkeckBox(int position) {
                if (isChoiceALL) {
                    if (shoppingCheckboxAll.isChecked()) {
                        isChoiceALL = false;
                        shoppingCheckboxAll.setChecked(false);
                    }
                }
            }
        });
    }

    @Override
    public int initLayout() {
        return R.layout.activity_shopping_cart;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.agent_history_back, R.id.shopping_lin_edit, R.id.shopping_tv_delete,
            R.id.shopping_tv_settlement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.agent_history_back:
                finish();
                break;
            //编辑/取消
            case R.id.shopping_lin_edit:
                //编辑 /取消
                editOrCancel();
                break;
            //删除
            case R.id.shopping_tv_delete:
                break;
            //结算
            case R.id.shopping_tv_settlement:
                break;
        }
    }

    //编辑/取消
    private void editOrCancel() {
        if (isShow) {
            isShow = false;
            shoppingTvEdit.setText("编辑");
            shoppingLinDelete.setVisibility(View.GONE);
            shoppingLinSettlement.setVisibility(View.VISIBLE);
            adapter.setIsdelete(false);
        } else {
            isShow = true;
            shoppingTvEdit.setText("取消");
            shoppingLinDelete.setVisibility(View.VISIBLE);
            shoppingLinSettlement.setVisibility(View.GONE);
            adapter.setIsdelete(true);
        }
        adapter.notifyDataSetChanged();
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
