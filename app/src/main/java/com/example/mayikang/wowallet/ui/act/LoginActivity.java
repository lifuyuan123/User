package com.example.mayikang.wowallet.ui.act;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.event.UserLoginEvent;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.UserBean;
import com.example.mayikang.wowallet.util.UserAuthUtil;

import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

import static com.sctjsj.basemodule.core.config.AsyncNotifyCode.CODE_LOGIN_ERROR;
import static com.sctjsj.basemodule.core.config.AsyncNotifyCode.CODE_LOGIN_FAILED;
import static com.sctjsj.basemodule.core.config.AsyncNotifyCode.CODE_LOGIN_SUCCESS;
import static com.sctjsj.basemodule.core.config.Tag.TAG_LOGIN_RESULT;

@Route(path = "/main/act/login")
public class LoginActivity extends BaseAppcompatActivity {
    private HttpServiceImpl http;
    @BindView(R.id.et_account)EditText etAccount;
    @BindView(R.id.et_password)EditText etPassword;

    //扫码入口的店铺 id
    @Autowired(name = "storeId")
    public String storeId="";
    //扫码入口的邀请码
    @Autowired(name = "inviteId")
    public String inviteId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        http= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void reloadData() {

    }


    @OnClick({R.id.act_login_tv_to_forget_pwd,R.id.act_login_tv_to_register1,R.id.btn_to_login,R.id.back})
    public void loginClick(View v){
        switch (v.getId()){
            //忘记密码
            case R.id.act_login_tv_to_forget_pwd:
                ARouter.getInstance().build("/main/act/forget_pwd").navigation();
                finish();
                break;

            //注册账户
            case R.id.act_login_tv_to_register1:
                ARouter.getInstance().build("/main/act/register_1").withString("inviteId",inviteId).navigation();
                finish();
                break;
            //去登录
            case R.id.btn_to_login:
                if(verify()){
                 login();
                }
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private boolean verify(){

        if(StringUtil.isBlank(etAccount.getText().toString())){
            Toast.makeText(this, "请输入账户名", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(StringUtil.isBlank(etPassword.getText().toString())){
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void login(){

        HashMap<String,String> map=new HashMap<>();
        map.put("username",etAccount.getText().toString());
        map.put("password",etPassword.getText().toString());
        map.put("cid", PushManager.getInstance().getClientid(this));
        http.doCommonPost(null, MainUrl.loginUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                Log.e("login",resultStr.toString());
                if(!StringUtil.isBlank(resultStr)){
                    try {
                        JSONObject res=new JSONObject(resultStr);
                        boolean result=res.getBoolean("result");
                        //登录成功
                        if(result){

                            String info=res.getString("info");
                            JSONObject user=new JSONObject(info);

                            int id=user.getInt("id");
                            String phone=user.getString("phone");
                            String url=null;
                            String photo=user.getString("photo");
                            int isShareMaker=user.getInt("isShareMaker");
                            int agent=user.getInt("agent");
                            if(!StringUtil.isBlank(photo)){
                                url=user.getJSONObject("photo").getString("url");
                            }
                            String username=user.getString("username");
                            UserBean ub=new UserBean();
                            ub.setEmail(user.getString("email"));
                            ub.setSex(user.getInt("sex"));
                            ub.setRealName(user.getString("realName"));
                            ub.setInvitationCode(user.getString("invitationCode"));
                            ub.setId(id);
                            ub.setPhone(phone);
                            ub.setUsername(username);
                            ub.setUrl(url);
                            ub.setIsShareMaker(isShareMaker);
                            ub.setAgent(agent);
                            SPFUtil.put(Tag.TAG_USER,new Gson().toJson(ub));

                            //发送用户登录成功
                            EventBus.getDefault().post(new UserLoginEvent(1));

                            //发送本地广播通知拦截器
                            Intent intent=new Intent(Tag.TAG_LOGIN_FILTER);
                            intent.putExtra(TAG_LOGIN_RESULT,CODE_LOGIN_SUCCESS);
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                            finish();
                        }else {
                            if (SPFUtil.contains(Tag.TAG_TOKEN)) {
                                SPFUtil.removeOne(Tag.TAG_TOKEN);
                            }
                            //清除本地用户信息
                            if (SPFUtil.contains(Tag.TAG_USER)) {
                                SPFUtil.removeOne(Tag.TAG_USER);
                            }
                            Intent intent=new Intent(Tag.TAG_LOGIN_FILTER);
                            intent.putExtra(TAG_LOGIN_RESULT,CODE_LOGIN_FAILED);
                             LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                            Toast.makeText(LoginActivity.this,res.getString("resultMsg") , Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (SPFUtil.contains(Tag.TAG_TOKEN)) {
                            SPFUtil.removeOne(Tag.TAG_TOKEN);
                        }
                        //清除本地用户信息
                        if (SPFUtil.contains(Tag.TAG_USER)) {
                            SPFUtil.removeOne(Tag.TAG_USER);
                        }
                    }
                }
            }


            @Override
            public void onError(Throwable ex) {
                if (SPFUtil.contains(Tag.TAG_TOKEN)) {
                    SPFUtil.removeOne(Tag.TAG_TOKEN);
                }
                //清除本地用户信息
                if (SPFUtil.contains(Tag.TAG_USER)) {
                    SPFUtil.removeOne(Tag.TAG_USER);
                }
                LogUtil.e(ex.toString());
                Intent intent=new Intent(Tag.TAG_LOGIN_FILTER);
                intent.putExtra(TAG_LOGIN_RESULT,CODE_LOGIN_ERROR);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                               Log.e("login",ex.toString());
                finish();
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
                showLoading(true,"登陆中..");

            }

            @Override
            public void onLoading(long total, long current) {

            }
        });
    }



}
