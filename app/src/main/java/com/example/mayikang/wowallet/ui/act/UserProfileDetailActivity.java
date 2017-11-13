package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.ui.widget.dialog.CommonDialog;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

@Route(path = "/main/act/user/user_profile_detail")
public class UserProfileDetailActivity extends BaseAppcompatActivity {

    @BindView(R.id.civ_logo)
    CircleImageView civLogo;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_real_name)
    TextView tvRealName;
    @BindView(R.id.tv_email)
    TextView tvEmail;

    @Autowired (name = "id")
    public int id=-1;

    private HttpServiceImpl http;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        http = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        if(-1!=id){
            pullUserDetail(String.valueOf(id));
        }
    }

    @Override
    public int initLayout() {
        return R.layout.activity_user_profile_detail;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.btn_transfer, R.id.btn_delete_Friend,R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_transfer:
                ARouter.getInstance().build("/main/act/user/transfer").withInt("id",id).navigation();
                break;
            case R.id.btn_delete_Friend:
                final CommonDialog dialog=new CommonDialog(this);
                dialog.setTitle("删除好友");
                dialog.setContent("确定解除与"+tvUserName.getText().toString()+"的好友关系？");
                dialog.setCancelClickListener("取消", new CommonDialog.CancelClickListener() {
                    @Override
                    public void clickCancel() {
                        dialog.dismiss();
                    }
                });
                        dialog.setConfirmClickListener("确认解除", new CommonDialog.ConfirmClickListener() {
                    @Override
                    public void clickConfirm() {
                        relieveFriendShip(id);
                       dialog.dismiss();
                    }
                });
                dialog.show();

                break;
            case R.id.back:
                finish();
                break;
        }
    }

    /**
     * 查询用户信息
     * @param id
     */
    public void pullUserDetail(String id){
        HashMap<String, String> body = new HashMap<>();
        body.put("ctype","user");
        body.put("cond","{id:"+id+",isDelete:1,isAdmin:1,isLocked:2,type:2,}");
        body.put("jf","photo");
        http.doCommonPost(null, MainUrl.basePageQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("xiang",result.toString());
                if(!StringUtil.isBlank(result)){

                    try {
                        JSONObject obj=new JSONObject(result);
                        JSONArray resultList=obj.getJSONArray("resultList");
                        if(resultList!=null && resultList.length()>0){
                            JSONObject x=resultList.getJSONObject(0);
                            String username=x.getString("username");
                            tvUserName.setText(username);

                            String logo=x.getString("photo");
                            String logoStr=null;
                            if(!StringUtil.isBlank(logo)){
                                logoStr=x.getJSONObject("photo").getString("url");
                               /* PicassoUtil.getPicassoObject().load(logoStr).
                                        resize(DpUtils.dpToPx(UserProfileDetailActivity.this,60),DpUtils.dpToPx(UserProfileDetailActivity.this,60))
                                        .into(civLogo);*/
                                Glide.with(UserProfileDetailActivity.this).load(logoStr)
                                        .placeholder(R.drawable.ic_defult_load).crossFade()
                                        .error(R.mipmap.icon_load_faild)
                                        .into(civLogo);

                            }

                            String realName=x.getString("realName");
                            if(StringUtil.isBlank(realName)){
                                tvRealName.setText("对方暂未设置");
                            } else {
                                tvRealName.setText(realName);
                            }

                            String phone=x.getString("phone");
                            if(StringUtil.isBlank(phone)){
                                tvPhone.setText("手机号:对方暂未设置");
                            }else {
                                tvPhone.setText("手机号:"+phone);
                            }

                            String email=x.getString("email");
                            if(StringUtil.isBlank(email)){
                                tvEmail.setText("对方暂未设置");
                            }else {
                                tvEmail.setText(email);
                            }


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

    /**
     * 解除好友关系
     * @param id 朋友 id
     */
    public void relieveFriendShip( int id){
        HashMap<String,String> map=new HashMap<>();
        map.put("friendId",String.valueOf(id));

        http.doCommonPost(null,MainUrl.relieveFriendShipUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("relieveFriendShip",result.toString());
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject object=new JSONObject(result);
                        if(object.getBoolean("result")){
                            Toast.makeText(UserProfileDetailActivity.this, "解除成功！", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //LogUtil.e(result);
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("relieveFriendShip",ex.toString());
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
