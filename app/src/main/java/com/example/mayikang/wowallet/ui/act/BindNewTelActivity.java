package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
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

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/main/act/bind_new_tel")
public class BindNewTelActivity extends BaseAppcompatActivity {
    @BindView(R.id.BindNewTel_back_rl)
    RelativeLayout BindNewTelBackRl;
    @BindView(R.id.BindNewTel_txt)
    TextView BindNewTelTxt;
    @BindView(R.id.BindNewTel_edt_code)
    EditText BindNewTelEdtCode;
    @BindView(R.id.BindNewTel_commit)
    Button BindNewTelCommit;
    @Autowired(name = "tel")
    String tel;
    private HttpServiceImpl service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        initView();

    }
    //初始化控件
    private void initView() {
        BindNewTelTxt.setText(tel);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_bind_new_tel;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.BindNewTel_back_rl, R.id.BindNewTel_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.BindNewTel_back_rl:
                finish();
                break;
            case R.id.BindNewTel_commit:
                if(!StringUtil.isBlank(BindNewTelEdtCode.getText().toString())){
                    commit();
                }else {
                    Toast.makeText(this, "请输入手机验证码！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    //绑定号码
    private void  commit(){
        HashMap<String,String> body=new HashMap<>();
        body.put("userId", UserAuthUtil.getUserId()+"");
        body.put("phone",tel);
        body.put("code",BindNewTelEdtCode.getText().toString());
        service.doCommonPost(null, MainUrl.BindNewTelUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("commit",result.toString());
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject object=new JSONObject(result);
                        if(object.getBoolean("result")){
                            ARouter.getInstance().build("/main/act/user/user_info").navigation();
                            UserBean bean=UserAuthUtil.getCurrentUser();
                            bean.setPhone(tel);
                            SPFUtil.put(Tag.TAG_USER,new Gson().toJson(bean));
                            finish();
                        }else {
                            Toast.makeText(BindNewTelActivity.this, "绑定手机失败！", Toast.LENGTH_SHORT).show();
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
