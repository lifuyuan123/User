package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
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
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/main/act/forgetpass")
public class ForgetPassActivity extends BaseAppcompatActivity {
    @Autowired(name = "phone")
    String phone;
    @BindView(R.id.edt_psw)
    EditText edtPsw;
    @BindView(R.id.edt_psw_agin)
    EditText edtPswAgin;
    @BindView(R.id.edt_phone_code)
    EditText edtPhoneCode;
    private HttpServiceImpl service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_forget_pass;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.forgetpass_linear_back, R.id.bt_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forgetpass_linear_back:
                finish();
                break;
            //保存密码
            case R.id.bt_save:
                //保存
                save();
                break;
        }
    }

    //保存
    private void save() {
        if(!StringUtil.isBlank(edtPsw.getText().toString())&&
                !StringUtil.isBlank(edtPswAgin.getText().toString())
                &&!StringUtil.isBlank(edtPhoneCode.getText().toString())){
            //判断两次输入密码是否一致
            if(edtPsw.getText().toString().equals(edtPswAgin.getText().toString())){
                //忘记密码的接口
                forgetPsw(edtPsw.getText().toString(),edtPhoneCode.getText().toString());
            }else {
                Toast.makeText(this, "两次输入的密码不一致，请确认。", Toast.LENGTH_SHORT).show();
            }

        }else {
           if(StringUtil.isBlank(edtPsw.getText().toString())){
               Toast.makeText(this, "请输入新密码。", Toast.LENGTH_SHORT).show();
           }else if(StringUtil.isBlank(edtPswAgin.getText().toString())){
               Toast.makeText(this, "请再次输入新密码。", Toast.LENGTH_SHORT).show();
           } else if(StringUtil.isBlank(edtPhoneCode.getText().toString())){
               Toast.makeText(this, "请输入短信验证码。", Toast.LENGTH_SHORT).show();
           }
        }
    }

    private void forgetPsw(String psw,String code) {
        Map<String,String> body=new HashMap<>();
        body.put("phone",phone);
        body.put("code",code);
        body.put("password",psw);
        service.doCommonPost(null, MainUrl.forgetpsw, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("forget_onSuccess",result.toString());
                try {
                    JSONObject object=new JSONObject(result);

                    boolean aFalse = object.getBoolean("result");
                    String msg = object.getString("msg");
                    if(aFalse){
                        Toast.makeText(ForgetPassActivity.this, msg, Toast.LENGTH_SHORT).show();
                        ARouter.getInstance().build("/main/act/login").navigation();
                        finish();
                    }else {
                        Toast.makeText(ForgetPassActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtil.e("forget_onSuccess",result.toString());
                }
            }

            @Override
            public void onError(Throwable ex) {
                LogUtil.e("forget_onError",ex.toString());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                LogUtil.e("forget_onFinished","onFinished");
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
