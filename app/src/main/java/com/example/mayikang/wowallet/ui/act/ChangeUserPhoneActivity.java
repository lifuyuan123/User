package com.example.mayikang.wowallet.ui.act;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.UserBean;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.google.gson.Gson;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/main/act/ChangeUserPhoneActivity")
public class ChangeUserPhoneActivity extends AppCompatActivity {


    private static String rexStr = "1[3|5|7|8]\\d{9}x";
    @BindView(R.id.changePhone_back_rl)
    RelativeLayout changePhoneBackRl;
    @BindView(R.id.change_phone_edt)
    EditText changePhoneEdt;
    @BindView(R.id.change_Img_code)
    EditText changeImgCode;
    @BindView(R.id.change_Img)
    RelativeLayout changeImg;
    @BindView(R.id.change_sendMsg)
    Button changeSendMsg;
    private HttpServiceImpl service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                sendMsg();
            }else if (msg.what==1){
                Bitmap bitmap= (Bitmap) msg.obj;
                if(null!=bitmap){
                    changeImg.removeAllViews();
                    ImageView mImageView=new ImageView(ChangeUserPhoneActivity.this);
                    mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    android.view.ViewGroup.LayoutParams pr= new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    mImageView.setLayoutParams(pr);
                    mImageView.setImageBitmap(bitmap);
                    changeImg.addView(mImageView);
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_phone);
        ButterKnife.bind(this);
        init();

    }
    //初始化
    private void init() {
        changeImg.removeAllViews();
        ImageView mImageView=new ImageView(this);
        android.view.ViewGroup.LayoutParams pr= new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mImageView.setLayoutParams(pr);
        PicassoUtil.getPicassoObject().load(MainUrl.PictureCodeUrl).memoryPolicy(MemoryPolicy.NO_CACHE).into(mImageView);
        changeImg.addView(mImageView);
    }

    @OnClick({R.id.changePhone_back_rl, R.id.change_sendMsg,R.id.change_Img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.changePhone_back_rl:
                finish();
                break;
            case R.id.change_sendMsg:
               if(!TextUtils.isEmpty(changePhoneEdt.getText().toString())&&
                       !TextUtils.isEmpty(changeImgCode.getText().toString())){
                   vreifyPhone();

               }else {
                   Toast.makeText(this,"请检查输入的号码和验证码！",Toast.LENGTH_SHORT).show();
               }

                break;
            case R.id.change_Img:
                init();
                break;

        }
    }


    //发送手机验证码
    private void sendMsg(){
        HashMap<String,String> body=new HashMap<>();
        body.put("mobile",changePhoneEdt.getText().toString());
        body.put("code",changeImgCode.getText().toString());
        service.doCommonPost(null, MainUrl.SendOldPhoneUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("sendMsg",result.toString());
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject object=new JSONObject(result);
                        if(object.getBoolean("result")){
                            Toast.makeText(ChangeUserPhoneActivity.this, "短信发送成功!", Toast.LENGTH_SHORT).show();
                            ARouter.getInstance().build("/main/act/erify_new_tel").navigation();
                        }else {
                            Toast.makeText(ChangeUserPhoneActivity.this, "发送手机验证码失败！", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("sendMsg",ex.toString());
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

    private void loadImg(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bitmap= Picasso.with(ChangeUserPhoneActivity.this).load(MainUrl.PictureCodeUrl).get();
                    if(null!=bitmap){
                        Message message=new Message();
                        message.obj=bitmap;
                        message.what=1;
                        handler.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }



    //验证旧手机号
    private void vreifyPhone(){
        HashMap<String,String> body=new HashMap<>();
        body.put("phone",changePhoneEdt.getText().toString());
        body.put("code",changeImgCode.getText().toString());
        body.put("userId",UserAuthUtil.getUserId()+"");
        service.doCommonPost(null, MainUrl.VerifyOldTelUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if(!StringUtil.isBlank(result)){
                    Log.e("changeUser",result.toString());
                    try {
                        JSONObject obj=new JSONObject(result);
                        if(obj.getBoolean("result")){
                            Message message=new Message();
                            message.what=0;
                            handler.sendMessage(message);
                        }else {
                            //失败
                            Toast.makeText(ChangeUserPhoneActivity.this,"输入的手机号或验证码不匹配！",Toast.LENGTH_SHORT).show();
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
