package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/main/act/ProtocolActivity")
//用户协议界面
public class ProtocolActivity extends BaseAppcompatActivity {

    @BindView(R.id.protocol_back)
    RelativeLayout protocolBack;
    @BindView(R.id.protocol_webView)
    WebView protocolWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWebView();
    }


    //初始化WebView
    private void initWebView() {
        WebSettings setting=protocolWebView.getSettings();
        setting.setJavaScriptEnabled(true);//设置支持js
        setting.setPluginsEnabled(true);//设置支持插件
        setting.setLoadWithOverviewMode(true);//缩放至屏幕大小
        protocolWebView.loadUrl(MainUrl.Protocol);
        protocolWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);
                return true;
            }
        });
    }

    @Override
    public int initLayout() {
        return R.layout.activity_protocol;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.protocol_back)
    public void onViewClicked() {
        finish();
    }
}
