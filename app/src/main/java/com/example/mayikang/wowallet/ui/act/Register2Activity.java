package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

/**
 * 注册页面第二步
 */

@Route(path = "/main/act/register_2")
public class Register2Activity extends BaseAppcompatActivity {

    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.edt_password_agin)
    EditText edtPasswordAgin;
    @BindView(R.id.edt_invitation_code)
    EditText edtInvitationCode;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.registre2_protocol)
    LinearLayout registre2_protocol;
    private HttpServiceImpl server;
    @Autowired (name="key")
    String phone;

    //扫码入口的邀请码
    @Autowired(name = "inviteId")
    public String inviteId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(StringUtil.isNumeric(inviteId)){
            edtInvitationCode.setText(inviteId);
            edtInvitationCode.setEnabled(false);
        }else {
            edtInvitationCode.setEnabled(true);
        }

    }

    @Override
    public int initLayout() {
        return R.layout.activity_register2;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.rela_back, R.id.bt_regist, R.id.tv_land,R.id.registre2_protocol})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rela_back:
                finish();
                break;
            //注册
            case R.id.bt_regist:
                //判断是否同意协议
                if(checkbox.isChecked()){
                    //注册
                    register();
                }else {
                    Toast.makeText(this, "您还没有同意注册协议。", Toast.LENGTH_SHORT).show();
                }
                break;
           // 已有账号  登陆
            case R.id.tv_land:
                ARouter.getInstance().build("/main/act/login").navigation();
                finish();
                break;
            //点击注册协议
            case R.id.registre2_protocol:
                ARouter.getInstance().build("/main/act/ProtocolActivity").navigation();
                break;
        }
    }

    //注册
    private void register() {
        server= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        Map<String,String> body=new HashMap<>();
        if(!StringUtil.isBlank(edtPassword.getText().toString())&&!StringUtil.isBlank(edtPasswordAgin.getText().toString())){
            body.put("phoneNumber",phone);
            body.put("password",edtPassword.getText().toString());
            body.put("isPassword",edtPasswordAgin.getText().toString());
            body.put("invitation_code",edtInvitationCode.getText().toString());
            server.doCommonPost(null, MainUrl.regist, body, new XProgressCallback() {
                @Override
                public void onSuccess(String result) {
                    LogUtil.e("regist_onSuccess",result.toString());
                    try {
                        JSONObject o=new JSONObject(result);
                        if(edtPassword.getText().toString().equals(edtPasswordAgin.getText().toString())){
                            boolean result1 = o.getBoolean("result");
                            if(result1){
                                Toast.makeText(Register2Activity.this, o.getString("msg"), Toast.LENGTH_SHORT).show();
                                ARouter.getInstance().build("/main/act/login").navigation();
                                finish();
                            }else {
                                Toast.makeText(Register2Activity.this, o.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(Register2Activity.this, "两次输入的密码不一致。", Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.e("regist_JSONException",e.toString());
                    }
                }

                @Override
                public void onError(Throwable ex) {
                    LogUtil.e("regist_onError",ex.toString());
                }

                @Override
                public void onCancelled(Callback.CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    LogUtil.e("regist_onFinished","onFinished");
                    dismissLoading();
                }

                @Override
                public void onWaiting() {

                }

                @Override
                public void onStarted() {
                    showLoading(true,"注册中...");
                }

                @Override
                public void onLoading(long total, long current) {

                }
            });

        }else {
            if(!StringUtil.isBlank(edtPassword.getText().toString())){
                Toast.makeText(this, "密码不能为空。", Toast.LENGTH_SHORT).show();
            }
            if(!StringUtil.isBlank(edtPasswordAgin.getText().toString())){
                Toast.makeText(this, "确认密码不能为空。", Toast.LENGTH_SHORT).show();
            }

        }

    }
}
