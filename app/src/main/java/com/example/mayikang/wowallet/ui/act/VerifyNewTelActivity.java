package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/main/act/erify_new_tel")
public class VerifyNewTelActivity extends BaseAppcompatActivity {

    @BindView(R.id.VerifyNewTel_back_rl)
    RelativeLayout VerifyNewTelBackRl;
    @BindView(R.id.VerifyNewTel_edt_newTel)
    EditText VerifyNewTelEdtNewTel;
    @BindView(R.id.VerifyNewTel_edt_coed)
    EditText VerifyNewTelEdtCoed;
    @BindView(R.id.verifyNewTel_commit)
    Button verifyNewTelCommit;
    @BindView(R.id.activity_verify_new_tel)
    LinearLayout activityVerifyNewTel;
    @BindView(R.id.verifynew_Img_code)
    EditText verifynewImgCode;
    @BindView(R.id.verifynew_Img)
    RelativeLayout verifynewImg;
    private HttpServiceImpl service;
    private static String rexStr = "^[1][3,4,5,7,8][0-9]{9}$";
//            "1[3|5|7|8]\\d{9}x";
    private Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                sendMsg();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        loadImg();
    }

    //加载图片
    private void loadImg(){
        verifynewImg.removeAllViews();
        ImageView mImageView=new ImageView(this);
        android.view.ViewGroup.LayoutParams pr= new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mImageView.setLayoutParams(pr);
        Picasso.with(this).load(MainUrl.PictureCodeUrl).memoryPolicy(MemoryPolicy.NO_CACHE).into(mImageView);
        verifynewImg.addView(mImageView);

    }

    @Override
    public int initLayout() {
        return R.layout.activity_verify_new_tel;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.VerifyNewTel_back_rl, R.id.verifyNewTel_commit,R.id.verifynew_Img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.VerifyNewTel_back_rl:
                finish();
                break;
            case R.id.verifyNewTel_commit:
                if(!StringUtil.isBlank(VerifyNewTelEdtNewTel.getText().toString())
                        &&!StringUtil.isBlank(VerifyNewTelEdtCoed.getText().toString())
                        &&!StringUtil.isBlank(verifynewImgCode.getText().toString())){
                    if(validata(rexStr,VerifyNewTelEdtNewTel.getText().toString())){
                        verifyNewPhone();
                    }else {
                        Toast.makeText(this, "输入的号码不合法！", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.verifynew_Img:
                loadImg();
                break;

        }
    }

    //验证手机号
    private void verifyNewPhone() {
        HashMap<String, String> body = new HashMap<>();
        body.put("newPhone", VerifyNewTelEdtNewTel.getText().toString());
        body.put("code", VerifyNewTelEdtCoed.getText().toString());
        body.put("userId", UserAuthUtil.getUserId() + "");
        service.doCommonPost(null, MainUrl.VerifyNewTelUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("verifyNewPhone",result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        if (object.getBoolean("result")) {
                            Message message = new Message();
                            message.what = 1;
                            handle.sendMessage(message);
                        } else {
                            Toast.makeText(VerifyNewTelActivity.this, "该手机号码已存在！", Toast.LENGTH_SHORT).show();
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


    //向手机发送短信
    private void sendMsg() {
        HashMap<String, String> body = new HashMap<>();
        body.put("mobile", VerifyNewTelEdtNewTel.getText().toString());
        body.put("code",verifynewImgCode.getText().toString());
        service.doCommonPost(null, MainUrl.SendSmsNewTelUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("sendMsg",result.toString());
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject obj=new JSONObject(result);
                        if(obj.getBoolean("result")){
                            ARouter.getInstance().build("/main/act/bind_new_tel").withString("tel",VerifyNewTelEdtNewTel.getText().toString()).navigation();
                        }else {
                            Toast.makeText(VerifyNewTelActivity.this, "发送验证码失败！", Toast.LENGTH_SHORT).show();
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

    private boolean validata(String reStr, String myStr) {
        Pattern patt = Pattern.compile(reStr);
        Matcher matcher = patt.matcher(myStr);
        return matcher.matches();
    }


}
