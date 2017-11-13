package com.example.mayikang.wowallet.ui.act;

import android.hardware.camera2.params.BlackLevelPattern;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

//修改登陆密码界面
@Route(path = "/main/act/user/change_login_pwd")
public class ChangeLoginPwdActivity extends BaseAppcompatActivity {


    @BindView(R.id.et_ori)
    EditText etOri;

    @BindView(R.id.et_new)
    EditText etNew;

    @BindView(R.id.et_confirm)
    EditText etConfirm;

    private HttpServiceImpl http;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        http = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();

    }

    @Override
    public int initLayout() {
        return R.layout.activity_change_login_pwd;
    }

    @Override
    public void reloadData() {

    }


    @OnClick({R.id.back,R.id.tv_save})
    public void clickView(View view){
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.tv_save:
                if(check()){
                    updateLoginPwd();
                }
                break;
        }
    }

    /**
     * 更新登录密码
     */
    private void updateLoginPwd(){
        HashMap<String,String> map=new HashMap<>();
        map.put("olderpasswoed",etOri.getText().toString().trim());
        map.put("newpassword",etNew.getText().toString().trim());
        map.put("user_id", UserAuthUtil.getUserId()+"");
        http.doCommonPost(null, MainUrl.updateLoginPasswordUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("updateLoginPwd",result.toString());
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject jsonObject=new JSONObject(result);
                        if(jsonObject.getBoolean("result")){
                            Toast.makeText(ChangeLoginPwdActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            Toast.makeText(ChangeLoginPwdActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("Throwable",ex.toString());
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
                showLoading(false,"修改中");
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });
    }

    private boolean check(){

        if(etOri.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "请输入登录密码", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(etNew.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "请输入新的登录密码", Toast.LENGTH_SHORT).show();
            return false;
        }


        if( etConfirm.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "请确认新的登录密码", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!etNew.getText().toString().trim().equals(etConfirm.getText().toString().trim())){
            Toast.makeText(this, "请保持两次密码一致", Toast.LENGTH_SHORT).show();
            return false;
        }



        return true;
    }

}
