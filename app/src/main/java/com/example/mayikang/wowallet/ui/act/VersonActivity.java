package com.example.mayikang.wowallet.ui.act;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.mayikang.wowallet.R;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.tencent.bugly.beta.Beta;

import butterknife.BindView;
import butterknife.OnClick;

//版本信息
@Route(path = "/main/act/Verson")
public class VersonActivity extends BaseAppcompatActivity {

    @BindView(R.id.verson_tv)
    TextView versonTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取版本号
        initVersion();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_verson;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.setting_back, R.id.verson_lin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //返回
            case R.id.setting_back:
                finish();
                break;
            //获取新版本
            case R.id.verson_lin:
                Beta.checkUpgrade();
                break;
        }
    }

    /**
     * 获取版本号
     */
    private void initVersion() {
        try {
            String version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            if (version != null) {
                versonTv.setText("v_" + version);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
