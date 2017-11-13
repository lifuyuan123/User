package com.example.mayikang.wowallet.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mayikang.wowallet.db.DBManager;
import com.example.mayikang.wowallet.push.DemoIntentService;
import com.example.mayikang.wowallet.push.DemoPushService;
import com.igexin.sdk.PushManager;
import com.sctjsj.basemodule.core.app.BaseApplication;
import com.sctjsj.basemodule.core.router_service.impl.SearchRecordServiceImpl;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;


/**
 * Created by Chris-Jason on 2016/10/31.
 */
public class MyApp extends BaseApplication {
    private SearchRecordServiceImpl service;
    private DBManager dbHelper;
    @Override
    public void onCreate() {
        super.onCreate();
        PushManager.getInstance().initialize(this.getApplicationContext(), DemoPushService.class);
        // com.getui.demo.DemoIntentService 为第三方自定义的推送服务事件接收类
        UMShareAPI.get(this);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), DemoIntentService.class);
        PlatformConfig.setWeixin("wxebe6e5abd89af26b", "a4668103ceadbb1bf69a4e976cae7daa");
        PlatformConfig.setQQZone("1106220676", "JwQQvEqdugDHy1hM");
        PlatformConfig.setSinaWeibo("1218494865", "e5f9ce1a03b74654158bd6db2b57b5d3", "http://sns.whalecloud.com");
        Config.isJumptoAppStore = true;
        Bugly.init(getApplicationContext(), "d68f718759", false);
        Beta.autoCheckUpgrade = true;

        //导入数据库
        dbHelper = new DBManager(this);
        dbHelper.openDatabase();




    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
