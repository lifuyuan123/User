package com.example.mayikang.wowallet.ui.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mayikang.wowallet.R;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;

import butterknife.OnClick;

@Route(path = "/main/act/confirm_order_detail")
public class ConfirmOrderDetailActivity extends BaseAppcompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int initLayout() {
        return R.layout.activity_confirm_order_detail;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.confirm_order_Btnsure})
    public void confirmOrderDetailClick(View view){
        switch (view.getId()){
            case R.id.confirm_order_Btnsure:
                ARouter.getInstance().build("/main/act/confirm_pay").navigation();
                break;
        }
    }
}
