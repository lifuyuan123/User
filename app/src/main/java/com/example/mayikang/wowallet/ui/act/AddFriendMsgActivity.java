package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.os.Message;
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
import com.sctjsj.basemodule.base.util.LogUtil;
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

@Route(path = "/main/act/user/AddFriendMsg")
public class AddFriendMsgActivity extends BaseAppcompatActivity {
    @Autowired(name = "bean")
    UserBean bean=null;
    @Autowired(name = "id")
    String id="";
    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.addfriends_msg_civ_logo)
    CircleImageView addfriendsMsgCivLogo;
    @BindView(R.id.addfriends_msg_user_name)
    TextView addfriendsMsgUserName;
    @BindView(R.id.addfriends_msg_phone)
    TextView addfriendsMsgPhone;
    @BindView(R.id.addfriends_msg_real_name)
    TextView addfriendsMsgRealName;
    @BindView(R.id.addfriends_msg_email)
    TextView addfriendsMsgEmail;
    @BindView(R.id.btn_transfer)
    Button btnTransfer;
    private HttpServiceImpl server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        server= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
       if(null!=bean){
           iniView();
       }else {
           getUserMsg();
       }
    }
    //获取添加用户的信息
    private void getUserMsg() {
        HashMap<String, String> body = new HashMap<>();
        body.put("ctype", "user");
        body.put("id", id);
        body.put("jf", "photo");
        Log.e("getUserData", id+"");
        server.doCommonPost(null, MainUrl.GetUserMessage, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("getUserData", result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONObject data = object.getJSONObject("data");
                        bean=new UserBean();
                        bean.setId(data.getInt("id"));
                        bean.setUsername(data.getString("username"));
                        bean.setSex(data.getInt("sex"));
                        bean.setEmail(data.getString("email"));
                        bean.setPhone(data.getString("phone"));
                        bean.setRealName(data.getString("realName"));
                        bean.setAlipayNumber(data.getString("alipayNumber"));
                        bean.setUrl(data.getJSONObject("photo").getString("url"));
                        iniView();
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

    //初始化控件数据
    private void iniView() {
        /*PicassoUtil.getPicassoObject().load(bean.getUrl()).into(addfriendsMsgCivLogo);*/
        Glide.with(this).load(bean.getUrl())
                .placeholder(R.drawable.ic_defult_load).crossFade()
                .error(R.mipmap.icon_load_faild)
                .into(addfriendsMsgCivLogo);

        addfriendsMsgUserName.setText(bean.getUsername());
        addfriendsMsgPhone.setText(bean.getPhone());
        if(!StringUtil.isBlank(bean.getRealName())){
            addfriendsMsgRealName.setText(bean.getRealName());
        }else {
            addfriendsMsgRealName.setText("用户暂未设置");
        }
        addfriendsMsgEmail.setText(bean.getEmail());
    }

    @Override
    public int initLayout() {
        return R.layout.activity_add_friend_msg;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.back, R.id.btn_transfer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_transfer:
                addFriends();
                break;
        }
    }

    //添加好友
    private void addFriends(){
        HashMap<String,String> body=new HashMap<>();
        body.put("id",bean.getId()+"");
        server.doCommonPost(null, MainUrl.AddFriendsUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("addFriends",result.toString());

                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject object=new JSONObject(result);
                        String msg=object.getString("msg");
                        Toast.makeText(AddFriendMsgActivity.this, msg+"", Toast.LENGTH_SHORT).show();
                        if(object.getBoolean("result")){
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
