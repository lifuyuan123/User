package com.example.mayikang.wowallet.ui.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.mayikang.wowallet.R;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;

/**
 * 重置密码
 */
@Route(path = "/main/act/reset_pwd")
public class ResetPwdActivity extends BaseAppcompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_reset_pwd;
    }

    @Override
    public void reloadData() {

    }
}
