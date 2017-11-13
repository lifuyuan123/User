package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.UserBean;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import butterknife.OnClick;

//账户安全
@Route(path = "/main/act/user/AccountSecurity")
public class AccountSecurityActivity extends BaseAppcompatActivity {

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
    }

    @Override
    public int initLayout() {
        return R.layout.activity_account_security;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.setting_back, R.id.setting_paypas_ll, R.id.setting_loginpas_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //返回
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
                            Toast.makeText(AccountSecurityActivity.this, "请先设置支付密码", Toast.LENGTH_LONG).show();
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
