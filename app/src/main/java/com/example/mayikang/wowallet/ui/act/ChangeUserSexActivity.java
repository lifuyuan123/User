package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.UserBean;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.google.gson.Gson;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/main/act/ChangeUserSexActivity")
public class ChangeUserSexActivity extends BaseAppcompatActivity {

    @BindView(R.id.change_sex_back_rl)
    RelativeLayout changeSexBackRl;
    @BindView(R.id.change_sex_save)
    TextView changeSexSave;
    @BindView(R.id.change_sex_manImg)
    ImageView changeSexManImg;
    @BindView(R.id.change_sex_man_ll)
    LinearLayout changeSexManLl;
    @BindView(R.id.change_sex_womanImg)
    ImageView changeSexWomanImg;
    @BindView(R.id.change_sex_woman_ll)
    LinearLayout changeSexWomanLl;
    private HttpServiceImpl service= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
    private int flag=0;
    UserBean bean=UserAuthUtil.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }
    //初始化布局
    private void initView() {
       UserBean bean= UserAuthUtil.getCurrentUser();
        if(bean.getSex()==1){
            chooseMan();
        }else if(bean.getSex()==2){
            chooseWoman();
        }
    }

    @Override
    public int initLayout() {
        return R.layout.activity_change_user_sex;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.change_sex_back_rl, R.id.change_sex_save, R.id.change_sex_man_ll, R.id.change_sex_woman_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.change_sex_back_rl:
                finish();
                break;
            case R.id.change_sex_save:
                if(bean.getSex()!=flag&&flag!=0){
                    ChangeSex();
                }else {
                   finish();
                }
                break;
            case R.id.change_sex_man_ll:
                chooseMan();
                break;
            case R.id.change_sex_woman_ll:
                chooseWoman();
                break;
        }
    }



    private void chooseWoman() {
        changeSexWomanImg.setImageResource(R.mipmap.ic_sure);
        changeSexWomanImg.setVisibility(View.VISIBLE);
        changeSexManImg.setVisibility(View.GONE);
        flag=2;
    }

    private void chooseMan() {
        changeSexManImg.setImageResource(R.mipmap.ic_sure);
        changeSexManImg.setVisibility(View.VISIBLE);
        changeSexWomanImg.setVisibility(View.GONE);
        flag=1;
    }


    //修改性别
    private void ChangeSex(){
        final Map<String,String> body=new HashMap<>();
        body.put("id",String.valueOf(UserAuthUtil.getUserId()));
        body.put("sex",String.valueOf(flag));
        service.doCommonPost(null, MainUrl.amendUserUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject object=new JSONObject(result);
                        if(object.getBoolean("result")){
                            Toast.makeText(ChangeUserSexActivity.this,object.getString("msg"),Toast.LENGTH_SHORT).show();
                            UserBean bean=UserAuthUtil.getCurrentUser();
                            bean.setSex(flag);
                            SPFUtil.put(Tag.TAG_USER,new Gson().toJson(bean));
                            finish();
                        }else {
                            Toast.makeText(ChangeUserSexActivity.this,object.getString("msg"),Toast.LENGTH_SHORT).show();
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
                dismissLoading();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                showLoading(true,"修改中...");
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });
    }

}
