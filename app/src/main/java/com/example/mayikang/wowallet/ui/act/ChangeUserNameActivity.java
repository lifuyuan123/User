package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
@Route(path = "/main/act/ChangeUserNameActivity")
public class ChangeUserNameActivity extends BaseAppcompatActivity {

    @BindView(R.id.changeName_back_rl)
    RelativeLayout changeNameBackRl;
    @BindView(R.id.changeName_save_txt)
    TextView changeNameSaveTxt;
    @BindView(R.id.changeName_editText)
    EditText changeNameEditText;
    private HttpServiceImpl service= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_change_user_name;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.changeName_back_rl, R.id.changeName_save_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.changeName_back_rl:
                finish();
                break;
            case R.id.changeName_save_txt:
                if(!TextUtils.isEmpty(changeNameEditText.getText().toString())){
                    ChangeUserName();
                    finish();
                }else {
                    Toast.makeText(this,"输入的用户名不合法！",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    //修改用户名
    private void ChangeUserName(){
        Map<String,String> body=new HashMap<>();
        body.put("userId", String.valueOf(UserAuthUtil.getUserId()));
        body.put("username",changeNameEditText.getText().toString());
        service.doCommonPost(null, MainUrl.amendUserName, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject object=new JSONObject(result);
                        if(object.getBoolean("result")){
                            Toast.makeText(ChangeUserNameActivity.this,object.getString("resultMsg"),Toast.LENGTH_SHORT).show();
                            UserBean bean=UserAuthUtil.getCurrentUser();
                            bean.setUsername("changeNameEditText.getText().toString()");
                            SPFUtil.put(Tag.TAG_USER,new Gson().toJson(bean));
                        }else {
                            Toast.makeText(ChangeUserNameActivity.this,object.getString("resultMsg"),Toast.LENGTH_SHORT).show();
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
