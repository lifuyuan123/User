package com.example.mayikang.wowallet.ui.act;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.sctjsj.basemodule.base.util.LogUtil;

public class SchemeFilterActivity extends Activity {

    private String TAG="SchemeFilterActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //直接通过ARouter处理外部Uri
        Uri uri = getIntent().getData();

        ARouter.getInstance().build(uri).navigation(this, new NavigationCallback() {
            @Override
            public void onFound(Postcard postcard) {
                finish();
            }

            @Override
            public void onLost(Postcard postcard) {
                finish();
            }

            @Override
            public void onArrival(Postcard postcard) {
                Uri u=postcard.getUri();
                LogUtil.e(TAG,"到达"+u.toString());
            }
        });
    }
}
