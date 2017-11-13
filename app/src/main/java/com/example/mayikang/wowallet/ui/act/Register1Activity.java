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

import com.alibaba.android.arouter.facade.annotation.Autowired;
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
 * 注册页面第一步，填写手机号获取短信验证码
 */
@Route(path = "/main/act/register_1")
public class Register1Activity extends BaseAppcompatActivity {
    @BindView(R.id.regist1_rela_piccode)
    RelativeLayout regist1RelaPiccode;
    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.edt_pic_code)
    EditText edtPicCode;
    @BindView(R.id.edt_phone_code)
    EditText edtPhoneCode;
    @BindView(R.id.act_register1_tv_to_login)
    TextView actRegister1TvToLogin;
    @BindView(R.id.regist_tv_getphone_code)
    TextView registTvGetphoneCode;
    private HttpServiceImpl service;

    private int orange= Color.parseColor("#ffffff");
    private int gray=Color.parseColor("#ffffff");

    //扫码入口的邀请码
    @Autowired(name = "inviteId")
    public String inviteId="";
    private OkHttpClient client;
    private String cookie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        showPicCode();

    }

    //加载验证码图片
    private void showPicCode() {

        regist1RelaPiccode.removeAllViews();
        ImageView tempIV=new ImageView(this);
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        tempIV.setLayoutParams(params);
        tempIV.setScaleType(ImageView.ScaleType.FIT_XY);
        regist1RelaPiccode.addView(tempIV);
        OkHttpClient client = new OkHttpClient();
        final CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        client.setCookieHandler(cookieManager);
        final OkHttpDownloader downloader = new OkHttpDownloader(client);

        Picasso picasso = new Picasso.Builder(Register1Activity.this).downloader(downloader).build();
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
        return R.layout.activity_register1;
    }

    @Override
    public void reloadData() {

    }


    @OnClick({R.id.act_register1_btn_go_next, R.id.act_register1_tv_to_login,
            R.id.regist1_rela_piccode, R.id.regist_tv_getphone_code, R.id.rela_back})
    public void register1Click(View view) {
        switch (view.getId()) {
            //下一步
            case R.id.act_register1_btn_go_next:
                //短信验证码验证
                picCodeIsture();
                break;
            //立即登录
            case R.id.act_register1_tv_to_login:
                ARouter.getInstance().build("/main/act/login").navigation();
                finish();
                break;
            //切换图片验证码
            case R.id.regist1_rela_piccode:
                showPicCode();
                break;
            //获取短信验证码
            case R.id.regist_tv_getphone_code:
                isOccupy();//判断电话号码是否被占用
                break;
            //返回
            case R.id.rela_back:
                finish();
                break;
        }
    }

    //判断电话号码是否被占用
    private void isOccupy() {
        Map<String, String> body = new HashMap<>();
        if (!StringUtil.isBlank(edtPhone.getText().toString())) {
            body.put("phoneNumber", edtPhone.getText().toString());
            service.doCommonPost(null, MainUrl.isOccupy, body, new XProgressCallback() {
                @Override
                public void onSuccess(String result) {
                    LogUtil.e("regist_onSuccess", result.toString()+edtPhone.getText().toString());
                    try {
                        JSONObject obj = new JSONObject(result);
                        boolean aBoolean = obj.getBoolean("result");
                        //判断注册的电话号码是否可用
                        if (aBoolean) {
                            //让edittext不可输入
                            edtPhone.setEnabled(false);
                            //图片验证码是否正确
                            MyCountTimer timer = new MyCountTimer(60000, 1000, registTvGetphoneCode, R.string.get_verify_code_txt, orange, gray);
                            getPhoenCode(timer);
                        } else {
                            Toast.makeText(Register1Activity.this, "用户已注册，请确认。", Toast.LENGTH_SHORT).show();
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

    //短信验证码是否正确
    private void picCodeIsture() {
        if (!StringUtil.isBlank(edtPhoneCode.getText().toString())) {
            Map<String, String> body = new HashMap<>();
            body.put("PhoneCode", edtPhoneCode.getText().toString());
            HashMap<String,String> heard=new HashMap<>();
            heard.put("Cookie",cookie);
            service.doCommonPost(heard, MainUrl.picCodeiSture, body, new XProgressCallback() {
                @Override
                public void onSuccess(String result) {
                    LogUtil.e("regist_picCodeIsture_onSuccess", result.toString());
                    try {
                        JSONObject object = new JSONObject(result);
                        boolean isture = object.getBoolean("result");
                        if (isture) {
                            //跳转到下一步
                            ARouter.getInstance().build("/main/act/register_2").
                                    withString("key", edtPhone.getText().toString()).
                                    withString("inviteId",inviteId)
                                    .navigation();
                            finish();
                        } else {
                            Toast.makeText(Register1Activity.this, "验证码错误。", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        LogUtil.e("regist_picCodeIsture_JSONException", e.toString());
                    }
                }

                @Override
                public void onError(Throwable ex) {
                    LogUtil.e("regist_picCodeIsture_onError", ex.toString());
                }

                @Override
                public void onCancelled(Callback.CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    LogUtil.e("regist_picCodeIsture_onFinished", "onFinished");
                }

                @Override
                public void onWaiting() {
                    dismissLoading();
                }

                @Override
                public void onStarted() {
                    showLoading(true,"验证中...");

                }

                @Override
                public void onLoading(long total, long current) {

                }
            });
        } else {
            Toast.makeText(this, "请填写短信验证码。", Toast.LENGTH_SHORT).show();
        }

    }

    //获取短信验证码
    private void getPhoenCode(final MyCountTimer timer) {
        Map<String, String> body = new HashMap<>();
        if (!StringUtil.isBlank(edtPicCode.getText().toString())) {
            body.put("code", edtPicCode.getText().toString());
            body.put("mobile", edtPhone.getText().toString());
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
                            timer.start();
                            Toast.makeText(Register1Activity.this, object.getString("resultMsg"), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Register1Activity.this, "图片验证码错误。", Toast.LENGTH_SHORT).show();
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
