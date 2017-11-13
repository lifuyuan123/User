package com.example.mayikang.wowallet.ui.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置支付密码
 */

@Route(path ="/main/act/user/setpaypassword")
public class SetPayPasswordActivity extends BaseAppcompatActivity {
    @BindView(R.id.paychangepass_edit_inputnewpass)EditText etPassWord;
    @BindView(R.id.paychangepass_edit_inputnewpass_agin)EditText etConfirmPassword;
//    @BindView(R.id.pay_change_forget)
//    RelativeLayout layout;
    private HttpServiceImpl http;
    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        http= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();


    }

    @Override
    public int initLayout() {
        return R.layout.activity_set_pay_password;
    }

    @Override
    public void reloadData() {

    }

    private void setPayPwd(String pwd){

        HashMap<String,String> map=new HashMap<>();
        map.put("payPassword",pwd);
        http.doCommonPost(null, MainUrl.setPayPasswordUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {

                if(!StringUtil.isBlank(resultStr)){
                    try {
                        Log.e("setPayPwd",resultStr.toString());
                        JSONObject obj=new JSONObject(resultStr);

                        boolean result=obj.getBoolean("result");
                        if(result){
                            Toast.makeText(SetPayPasswordActivity.this, "设置成功", Toast.LENGTH_LONG).show();

                            UserBean ub= UserAuthUtil.getCurrentUser();
                            ub.setHasPayPassword(true);
                            UserAuthUtil.saveUserBean(ub);

                            finish();
                        }else {
                            Toast.makeText(SetPayPasswordActivity.this, "设置失败", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {
                Toast.makeText(SetPayPasswordActivity.this, "设置错误", Toast.LENGTH_LONG).show();
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
                showLoading(true,"加载中");
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });
    }

    @OnClick({
            R.id.paychangepass_linear_back,
            R.id.set_paychangepass_tv_save
//            ,R.id.pay_change_forget
    })
    public void clickView(View view){
        switch (view.getId()){
            case R.id.paychangepass_linear_back:
                finish();
                break;

            case R.id.set_paychangepass_tv_save:
                if(check()){
                    setPayPwd(etPassWord.getText().toString());
                }
                break;

//            case R.id.pay_change_forget:
//                ARouter.getInstance().build("/main/act/ForgetPayPas").navigation();
//                break;
        }
    }


    public boolean check(){

        if(StringUtil.isBlank(etPassWord.getText().toString())){
            Toast.makeText(this, "请输入支付密码", Toast.LENGTH_SHORT).show();
            return false;
        }


        if(StringUtil.isBlank(etConfirmPassword.getText().toString())){
            Toast.makeText(this, "请确认支付密码", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!etPassWord.getText().toString().equals(etConfirmPassword.getText().toString())){
            Toast.makeText(this, "请保持两次输入的密码一直", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
