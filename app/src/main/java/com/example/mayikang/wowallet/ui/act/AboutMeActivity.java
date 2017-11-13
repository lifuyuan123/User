package com.example.mayikang.wowallet.ui.act;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.mayikang.wowallet.R;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;

import butterknife.BindView;
import butterknife.OnClick;

//关于我们
@Route(path = "/main/act/AboutMe")
public class AboutMeActivity extends BaseAppcompatActivity {

    @BindView(R.id.tv_aboutMe_verson)
    TextView tvAboutMeVerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取版本
        initVersion();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_about_me;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.aboutMe_back, R.id.aboutme_lin_clause})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.aboutMe_back:
                finish();
                break;
            case R.id.aboutme_lin_clause:
                Toast.makeText(this, "条款", Toast.LENGTH_SHORT).show();
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
                tvAboutMeVerson.setText("惠生活"+version+"版");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
