package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
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

@Route(path = "/main/act/user/FansMessageActivity")
public class FansMessageActivity extends BaseAppcompatActivity {

    @BindView(R.id.back)
    RelativeLayout back;
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
    private HttpServiceImpl http;

    @Autowired(name = "id")
    int id=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        http = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        pullUserDetail(id+"");
    }

    @Override
    public int initLayout() {
        return R.layout.activity_fans_message;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }

    /**
     * 查询用户信息
     * @param id
     */
    public void pullUserDetail(String id){
        HashMap<String, String> body = new HashMap<>();
        body.put("ctype","user");
        body.put("cond","{id:"+id+",isDelete:1,isAdmin:1,isLocked:2}");
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
                                        resize(DpUtils.dpToPx(FansMessageActivity.this,60),DpUtils.dpToPx(FansMessageActivity.this,60))
                                        .error(R.mipmap.icon_default_portrait)
                                        .into(civLogo);*/
                                Glide.with(FansMessageActivity.this).load(logoStr)
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
}
