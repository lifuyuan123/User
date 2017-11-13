package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.UserBean;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

@Route(path = "/main/act/user/AddFriends")
public class AddFriendsMsgActivity extends BaseAppcompatActivity {

    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.messag_Add_icomImg)
    CircleImageView messagAddIcomImg;
    @BindView(R.id.messag_Add_user_name)
    TextView messagAddUserName;
    @BindView(R.id.messag_Add_phone)
    TextView messagAddPhone;
    @BindView(R.id.messag_Add_real_name)
    TextView messagAddRealName;
    @BindView(R.id.messag_Add_email)
    TextView messagAddEmail;
    @BindView(R.id.messag_Add_agree)
    Button messagAddAgree;
    @BindView(R.id.btn_disagree)
    Button btnDisagree;
    @Autowired(name = "key")
    int id;
    @Autowired(name = "msg")
    int msgId;

    private HttpServiceImpl service;
    private UserBean beans;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                UserBean bean= (UserBean) msg.obj;
                if(null!=bean){
                    beans=bean;
                    initView(bean);
                    setMsssageStatus();
                    Log.e("msg","-------");
                }
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        getUsreMsg();

    }

    //设置控件数据
    private void initView(UserBean bean){
        /*PicassoUtil.getPicassoObject().load(bean.getUrl()).into(messagAddIcomImg);*/

        Glide.with(this).load(bean.getUrl())
                .placeholder(R.drawable.ic_defult_load).crossFade()
                .error(R.mipmap.icon_load_faild)
                .into(messagAddIcomImg);

        messagAddUserName.setText(bean.getUsername());
        messagAddPhone.setText(bean.getPhone());
        if(!TextUtils.isEmpty(bean.getRealName())){
            messagAddRealName.setText(bean.getRealName());
        }else {
            messagAddRealName.setText("对方暂未设置");
        }
        messagAddEmail.setText(bean.getEmail());
    }

    @Override
    public int initLayout() {
        return R.layout.activity_add_friends_msg;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.back, R.id.messag_Add_agree, R.id.btn_disagree})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.messag_Add_agree:
                if(beans!=null){
                    addFriends();
                }
                break;
            case R.id.btn_disagree:
                if(beans!=null){
                    adddisFriends();
                }

                break;
        }
    }

    //获取发送添加好友人的信息
    private void getUsreMsg(){
        HashMap<String,String> body=new HashMap<>();
        body.put("ctype","user");
        body.put("cond","{id:"+id+"}");
        body.put("jf","photo");
        service.doCommonPost(null, MainUrl.basePageQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("getUsreMsg",result.toString());
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject object=new JSONObject(result);
                        JSONObject bean=object.getJSONObject("data");
                        UserBean userBean=new UserBean();
                        userBean.setPhone(bean.getString("phone"));
                        userBean.setEmail(bean.getString("email"));
                        userBean.setRealName(bean.getString("realName"));
                        userBean.setUsername(bean.getString("username"));
                        userBean.setId(bean.getInt("id"));
                        userBean.setUrl(bean.getJSONObject("photo").getString("url"));
                        Message mMessage=new Message();
                        mMessage.what=0;
                        mMessage.obj=userBean;
                        handler.sendMessage(mMessage);
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

    //添加好友
    private void addFriends(){
        HashMap<String,String> body=new HashMap<>();
        body.put("userId",UserAuthUtil.getUserId()+"");
        body.put("friendsId",beans.getId()+"");
        service.doCommonPost(null, MainUrl.AggreAddFriends, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("addFriends",result.toString());
                    if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject object=new JSONObject(result);
                        if(object.getBoolean("result")){
                            Toast.makeText(AddFriendsMsgActivity.this, "添加好友成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(AddFriendsMsgActivity.this, "添加好友失败", Toast.LENGTH_SHORT).show();
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


    //拒绝添加好友
    private void adddisFriends(){
        HashMap<String,String> body=new HashMap<>();
        body.put("userId",UserAuthUtil.getUserId()+"");
        body.put("friendsId",beans.getId()+"");
        body.put("type","2");
        service.doCommonPost(null, MainUrl.DisAggreAddFriends, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("adddisFriends",result.toString());
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject object=new JSONObject(result);
                        if(object.getBoolean("result")){
                            Toast.makeText(AddFriendsMsgActivity.this, "拒绝成功成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(AddFriendsMsgActivity.this, "添加好友失败", Toast.LENGTH_SHORT).show();
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


    //修改数据的status状态
    private void setMsssageStatus(){
        HashMap<String,String> body=new HashMap<>();
        body.put("ctype","message");
        body.put("data","{id:"+msgId+",status:2}");
        service.doCommonPost(null, MainUrl.SetMessageStatus, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("setMsssageStatusss",result.toString());
                if(!StringUtil.isBlank(result)){
                    //查看数据成功
                    Log.e("setMsssageStatus",result.toString());
                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("setMsssageonError",ex.toString());
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
