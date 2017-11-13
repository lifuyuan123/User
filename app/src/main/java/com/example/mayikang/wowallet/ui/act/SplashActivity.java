package com.example.mayikang.wowallet.ui.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.mayikang.wowallet.R;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;

/**
 * 启动欢迎页
 */
@Route(path = "/main/act/splash")
public class SplashActivity extends BaseAppcompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO 可以在这里对外部进入的连接做页面分发


        //TODO webview展示欢迎页

    }

    @Override
    public int initLayout() {
        return R.layout.activity_splash;
    }

    @Override
    public void reloadData() {

    }
}
