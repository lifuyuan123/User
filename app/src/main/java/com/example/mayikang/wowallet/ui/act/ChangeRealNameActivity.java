package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 修改真实姓名
 */
@Route(path = "/main/act/ChangeRealNameActivity")
public class ChangeRealNameActivity extends BaseAppcompatActivity {


    @BindView(R.id.changeRealName_back_rl)
    RelativeLayout changeRealNameBackRl;
    @BindView(R.id.changeRealName_save_txt)
    TextView changeRealNameSaveTxt;
    @BindView(R.id.changeRealName_editText)
    EditText changeRealNameEditText;
    @BindView(R.id.activity_change_user_name)
    LinearLayout activityChangeUserName;

    private HttpServiceImpl server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        server= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();

    }

    @Override
    public int initLayout() {
        return R.layout.activity_change_real_name;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.changeRealName_back_rl, R.id.changeRealName_save_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.changeRealName_back_rl:
                finish();
                break;
            case R.id.changeRealName_save_txt:
                changeRealName();
                break;
        }
    }

    //修改真实姓名
    private void changeRealName() {
        if(!StringUtil.isBlank(changeRealNameEditText.getText().toString())){
            HashMap<String,String> body=new HashMap<>();
            body.put("userId",UserAuthUtil.getUserId()+"");
            body.put("realName",changeRealNameEditText.getText().toString());
            server.doCommonPost(null, MainUrl.ChangRealName, body, new XProgressCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.e("changeRealName",result.toString());
                    if(!StringUtil.isBlank(result)){
                        try {
                            JSONObject object=new JSONObject(result);
                            if(object.getBoolean("result")){
                                Toast.makeText(ChangeRealNameActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(ChangeRealNameActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onError(Throwable ex) {
                    Log.e("changeRealName",ex.toString());
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

        }else {
            Toast.makeText(this, "真实姓名不能为空！", Toast.LENGTH_SHORT).show();
        }

    }
}
