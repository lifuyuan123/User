package com.example.mayikang.wowallet.ui.act;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.intf.MainUrl;
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

//修改支付密码
@Route(path = "/main/act/user/paychangepass")
public class PayChangePassActivity extends BaseAppcompatActivity {

    public HttpServiceImpl http;
    @BindView(R.id.paychangepass_edit_inputpass)EditText etOriPwd;
    @BindView(R.id.paychangepass_edit_inputnewpass)EditText etNewPwd;
    @BindView(R.id.paychangepass_edit_inputnewpass_agin)EditText etConfirmPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        http = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_pay_change_pass;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.paychangepass_linear_back,R.id.paychangepass_tv_save,R.id.pay_change_forget})
    public void clickView(View view){
        switch (view.getId()){
            case R.id.paychangepass_linear_back:
                finish();
                break;

            case R.id.paychangepass_tv_save:
                if(check()){
                    updatePayPassword();
                }
                break;
            case R.id.pay_change_forget:
                ARouter.getInstance().build("/main/act/ForgetPayPas").navigation();
                break;
        }
    }


    public boolean check(){

        if(StringUtil.isBlank(etOriPwd.getText().toString())){
            Toast.makeText(this, "请输入原支付密码", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(StringUtil.isBlank(etNewPwd.getText().toString())){
            Toast.makeText(this, "请输入新支付密码", Toast.LENGTH_SHORT).show();
            return false;
        }


        if(StringUtil.isBlank(etConfirmPwd.getText().toString())){
            Toast.makeText(this, "请确认支付密码", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!etNewPwd.getText().toString().equals(etConfirmPwd.getText().toString())){
            Toast.makeText(this, "请保持两次支付密码一致", Toast.LENGTH_SHORT).show();

            return false;
        }


        return true;

    }


    private void updatePayPassword(){

        HashMap<String,String> map=new HashMap<>();
        map.put("oldPayPassword",etOriPwd.getText().toString());
        map.put("newPassWord",etNewPwd.getText().toString());
        http.doCommonPost(null, MainUrl.updatePayPasswordUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {

                if(!StringUtil.isBlank(resultStr)){
                    try {
                        JSONObject obj=new JSONObject(resultStr);
                        boolean result=obj.getBoolean("result");
                        String msg=obj.getString("msg");
                        Toast.makeText(PayChangePassActivity.this, msg, Toast.LENGTH_SHORT).show();
                        if(result){
                            finish();
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

            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current) {

            }
        });
    }

}
