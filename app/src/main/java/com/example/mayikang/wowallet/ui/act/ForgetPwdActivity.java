package com.example.mayikang.wowallet.ui.act;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.MyCountTimer;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 忘记密码
 */
@Route(path = "/main/act/forget_pwd")
public class ForgetPwdActivity extends BaseAppcompatActivity {

    @BindView(R.id.edt_phoneNum)
    EditText edtPhoneNum;
    @BindView(R.id.edt_piccode)
    EditText edtPiccode;
    @BindView(R.id.rela_picCode)
    RelativeLayout relaPicCode;
    private HttpServiceImpl service;

    private OkHttpClient client;
    private String cookie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
//        获取图片验证码
          getPicCode();

    }

    private void getPicCode() {

        relaPicCode.removeAllViews();
        ImageView tempIV=new ImageView(this);
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        tempIV.setLayoutParams(params);
        tempIV.setScaleType(ImageView.ScaleType.FIT_XY);
        relaPicCode.addView(tempIV);
        OkHttpClient client = new OkHttpClient();
        final CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        client.setCookieHandler(cookieManager);
        final OkHttpDownloader downloader = new OkHttpDownloader(client);

        Picasso picasso = new Picasso.Builder(ForgetPwdActivity.this).downloader(downloader).build();
        picasso.load(MainUrl.getPicCode).skipMemoryCache().into(tempIV, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                List<HttpCookie> list = cookieManager.getCookieStore().getCookies();

                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).toString().contains("JSESSIONID=")) {
                            cookie = list.get(i).toString();
                            LogUtil.e("cookie-get", cookie);
                        }
                    }
                }
            }

            @Override
            public void onError() {
            }
        });




    }

    @Override
    public int initLayout() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    public void reloadData() {

    }


    @OnClick({R.id.rela_back, R.id.act_forget_pwd_btn_to_next,R.id.rela_picCode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //返回
            case R.id.rela_back:
                finish();
                break;
            //下一步
            case R.id.act_forget_pwd_btn_to_next:
                //验证手机号码  然后判断验证码
                isOccupy();
                break;
            //切换图片验证码
            case R.id.rela_picCode:
                getPicCode();
                break;
        }
    }

    //判断电话号码是否被占用
    private void isOccupy() {
        Map<String, String> body = new HashMap<>();
        if (!StringUtil.isBlank(edtPhoneNum.getText().toString())) {
            body.put("phoneNumber", edtPhoneNum.getText().toString());
            service.doCommonPost(null, MainUrl.isOccupy, body, new XProgressCallback() {
                @Override
                public void onSuccess(String result) {
                    LogUtil.e("regist_onSuccess", result.toString());
                    try {
                        JSONObject obj = new JSONObject(result);
                        boolean aBoolean = obj.getBoolean("result");
                        //判断注册的电话号码是否可用
                        if (aBoolean) {
                            //图片验证码是否正确
                            Toast.makeText(ForgetPwdActivity.this, "用户未注册，请确认。", Toast.LENGTH_SHORT).show();
                        } else {
                            getPhoenCode();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.e("regist_JSONException", e.toString());
                    }

                }

                @Override
                public void onError(Throwable ex) {
                    LogUtil.e("regist_onError", ex.toString());
                }

                @Override
                public void onCancelled(Callback.CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    LogUtil.e("regist_onFinished", "onFinished");
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
        } else {
            Toast.makeText(this, "请输入电话号码。", Toast.LENGTH_SHORT).show();
        }

    }


    //获取短信验证码
    private void getPhoenCode() {
        Map<String, String> body = new HashMap<>();
        if (!StringUtil.isBlank(edtPiccode.getText().toString())) {
            body.put("code", edtPiccode.getText().toString());
            body.put("mobile", edtPhoneNum.getText().toString());
            HashMap<String,String> heard=new HashMap<>();
            heard.put("Cookie",cookie);
            service.doCommonPost(heard, MainUrl.getPhoenCode, body, new XProgressCallback() {
                @Override
                public void onSuccess(String result) {
                    LogUtil.e("regist_getphcode_onSuccess", result.toString());
                    try {
                        JSONObject object = new JSONObject(result);
                        boolean result1 = object.getBoolean("result");
                        if (result1) {
                            ARouter.getInstance().build("/main/act/forgetpass")
                                    .withString("phone",edtPhoneNum.getText().toString()).navigation();
                            Toast.makeText(ForgetPwdActivity.this, object.getString("resultMsg"), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ForgetPwdActivity.this, "图片验证码错误。", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.e("regist_getphcode_JSONException", e.toString());
                    }

                }

                @Override
                public void onError(Throwable ex) {
                    LogUtil.e("regist_getphcode_onError", ex.toString());
                }

                @Override
                public void onCancelled(Callback.CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    LogUtil.e("regist_getphcode_onFinished", "onFinished");
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
        } else {
            Toast.makeText(this, "请填写图片验证码。", Toast.LENGTH_SHORT).show();
        }

    }


}
