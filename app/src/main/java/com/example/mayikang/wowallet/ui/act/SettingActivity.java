package com.example.mayikang.wowallet.ui.act;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.UserBean;
import com.example.mayikang.wowallet.util.UpdateUtil;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.ui.widget.dialog.CommonDialog;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.config.Tag;

import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;


import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/main/act/user/setting")
public class SettingActivity extends BaseAppcompatActivity {

    @BindView(R.id.setting_back)
    RelativeLayout settingBack;

    @BindView(R.id.setting_paypas_ll)
    LinearLayout settingPaypasLl;
    @BindView(R.id.setting_loginpas_ll)
    LinearLayout settingLoginpasLl;

    @BindView(R.id.setting_private_ll)
    LinearLayout settingPrivateLl;
    @BindView(R.id.setting_ver_ll)
    LinearLayout settingVerLl;
    @BindView(R.id.setting_exit_ll)
    LinearLayout settingExitLl;
    @BindView(R.id.activity_mine_setting)
    LinearLayout activityMineSetting;

    @BindView(R.id.act_setting_tv_version)
    TextView tvVersion;

    private HttpServiceImpl http;

    private Handler mH = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //直接去修改支付密码
                    ARouter.getInstance().build("/main/act/user/paychangepass").navigation();
                    break;
                case 2:
                    ARouter.getInstance().build("/main/act/user/setpaypassword").navigation();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        http = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        initVersion();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.setting_back, R.id.setting_paypas_ll, R.id.setting_loginpas_ll, R.id.setting_private_ll, R.id.setting_ver_ll, R.id.setting_exit_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.setting_back:
                finish();
                break;
            //修改支付密码
            case R.id.setting_paypas_ll:
                //已经有支付密码，直接去修改

                //没有支付密码，去设置
                checkIfHasPayPassword();

                break;
            //修改登录密码
            case R.id.setting_loginpas_ll:
                ARouter.getInstance().build("/main/act/user/change_login_pwd").navigation();
                break;


            case R.id.setting_private_ll:
                break;

            case R.id.setting_ver_ll:
                /*UpdateUtil.getInstance(this).checkVersionManual();*/
                Beta.checkUpgrade();
                break;
            case R.id.setting_exit_ll:
                new UserAuthUtil().doLogout(this, new UserAuthUtil.LogInOutListener() {
                    @Override
                    public void onSuccess() {
                        finish();
                        Toast.makeText(SettingActivity.this, "退出登陆成功！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed() {

                    }

                    @Override
                    public void onError() {

                    }
                });

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
                tvVersion.setText("v_" + version);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 判断用户是否已经设置了支付密码
     */
    public void checkIfHasPayPassword() {
        http.doCommonPost(null, MainUrl.isHasPayPasswordUrl, null, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                LogUtil.e("r", resultStr.toString());
                if (!StringUtil.isBlank(resultStr)) {
                    try {
                        JSONObject obj = new JSONObject(resultStr);
                        boolean result = obj.getBoolean("result");
                        //
                        if (result) {
                            UserBean ub = UserAuthUtil.getCurrentUser();
                            ub.setHasPayPassword(true);
                            UserAuthUtil.saveUserBean(ub);

                            mH.sendMessage(mH.obtainMessage(1));
                            //直接去修改支付密码
                            //ARouter.getInstance().build("/main/act/paychangepass").navigation();

                        } else {
                            Toast.makeText(SettingActivity.this, "请先设置支付密码", Toast.LENGTH_LONG).show();
                            mH.sendMessage(mH.obtainMessage(2));
                            //ARouter.getInstance().build("/main/act/user/setpaypassword").navigation();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dismissLoading();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                showLoading(false, "");
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });

    }


}
