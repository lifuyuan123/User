package com.example.mayikang.wowallet.ui.act;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.config.Tag;

import com.sctjsj.basemodule.core.cookie.CookieUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;


import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;


import java.io.IOException;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/main/act/ForgetPayPas")
public class ForgetPayPasActivity extends BaseAppcompatActivity {

    @BindView(R.id.froget_pay_back)
    LinearLayout frogetPayBack;
    @BindView(R.id.froget_pay_phoneEdt)
    EditText frogetPayPhoneEdt;
    @BindView(R.id.froget_pay_imgNumber)
    EditText frogetPayImgNumber;
    @BindView(R.id.froget_pay_img)
    RelativeLayout frogetPayImg;
    @BindView(R.id.froget_pay_sendmsg)
    Button frogetPaySendmsg;
    @BindView(R.id.activity_forget_pay_pas)
    LinearLayout activityForgetPayPas;

    private OkHttpClient client;
    private HttpServiceImpl service;
    private String cookie;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                //发送短信验证码
                sendMsg();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadImg();
        service= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();

    }

    @Override
    public int initLayout() {
        return R.layout.activity_forget_pay_pas;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.froget_pay_back, R.id.froget_pay_img, R.id.froget_pay_sendmsg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.froget_pay_back:
                finish();
                break;
            case R.id.froget_pay_img:
                loadImg();
                break;
            case R.id.froget_pay_sendmsg:
                if(!StringUtil.isBlank(frogetPayPhoneEdt.getText().toString())
                        &&!StringUtil.isBlank(frogetPayImgNumber.getText().toString())){
                    rePhone();
                }else {
                    Toast.makeText(this, "请检查输入的号码和验证码！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //加载图片验证码
    private void loadImg(){

        frogetPayImg.removeAllViews();
        ImageView tempIV=new ImageView(this);
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        tempIV.setLayoutParams(params);
        tempIV.setScaleType(ImageView.ScaleType.FIT_XY);
        frogetPayImg.addView(tempIV);
        OkHttpClient client = new OkHttpClient();
        final CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        client.setCookieHandler(cookieManager);
        final OkHttpDownloader downloader = new OkHttpDownloader(client);

        Picasso picasso = new Picasso.Builder(ForgetPayPasActivity.this).downloader(downloader).build();
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
                Snackbar.make(activityForgetPayPas,"图片验证码加载失败",Snackbar.LENGTH_SHORT).show();
            }
        });



    }


    //验证手机号码
    private void rePhone(){
        HashMap<String,String> body=new HashMap<>();
        body.put("phone",frogetPayPhoneEdt.getText().toString());
        body.put("code",frogetPayImgNumber.getText().toString());
        body.put("userId", UserAuthUtil.getUserId()+"");
        HashMap<String,String> heard=new HashMap<>();
        heard.put("Cookie",cookie);
        service.doCommonPost(heard, MainUrl.VerifyOldTelUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("rePhone",result.toString());
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject object=new JSONObject(result);
                        if(object.getBoolean("result")){
                            Message message=new Message();
                            message.what=0;
                            handler.sendMessage(message);
                        }else {
                            Toast.makeText(ForgetPayPasActivity.this,"输入的手机号或验证码不匹配！",Toast.LENGTH_SHORT).show();
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

    //发送短信验证码
    private void sendMsg(){
        HashMap<String,String> body=new HashMap<>();
        body.put("mobile",frogetPayPhoneEdt.getText().toString());
        body.put("code",frogetPayImgNumber.getText().toString());
        HashMap<String,String> heard=new HashMap<>();
        heard.put("Cookie",cookie);
        service.doCommonPost(heard, MainUrl.SendOldPhoneUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject object=new JSONObject(result);
                        if(object.getBoolean("result")){
                            //跳转到下一个界面
                            ARouter.getInstance().build("/main/act/FrogetPaySure").withString("key",frogetPayPhoneEdt.getText().toString()).navigation();
                            finish();
                        }else {
                            Toast.makeText(ForgetPayPasActivity.this, "发送短信验证码失败！", Toast.LENGTH_SHORT).show();
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
