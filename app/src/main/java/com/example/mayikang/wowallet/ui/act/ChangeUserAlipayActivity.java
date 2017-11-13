package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.os.Message;
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
import com.example.mayikang.wowallet.modle.javabean.UserBean;
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
@Route(path = "/main/act/ChangeUserAlipayActivity")
public class ChangeUserAlipayActivity extends BaseAppcompatActivity {

    @BindView(R.id.changeAlipay_back_rl)
    RelativeLayout changeAlipayBackRl;
    @BindView(R.id.changeAlipay_save_txt)
    TextView changeAlipaySaveTxt;
    @BindView(R.id.changeRealAlipay_editText)
    EditText changeRealAlipayEditText;
    @BindView(R.id.activity_change_user_name)
    LinearLayout activityChangeUserName;
    private HttpServiceImpl service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        initView();
    }
    //初始化
    private void initView() {
        UserBean bean= UserAuthUtil.getCurrentUser();
        if(!StringUtil.isBlank(bean.getAlipayNumber())){
            changeRealAlipayEditText.setText(bean.getAlipayNumber());
        }
    }

    @Override
    public int initLayout() {
        return R.layout.activity_change_user_alipay;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.changeAlipay_back_rl, R.id.changeAlipay_save_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.changeAlipay_back_rl:
                finish();
                break;
            case R.id.changeAlipay_save_txt:
                changeAlipay();
                break;
        }
    }
    //修改支付宝
    private void changeAlipay() {
        if(!StringUtil.isBlank(changeRealAlipayEditText.getText().toString())){
            HashMap<String,String> body=new HashMap<>();
            body.put("userId",UserAuthUtil.getUserId()+"");
            body.put("alipayNumber",changeRealAlipayEditText.getText().toString());
            service.doCommonPost(null, MainUrl.ChangAlipayNumber, body, new XProgressCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.e("changeAlipay",result.toString());
                    if(!StringUtil.isBlank(result)){
                        try {
                            JSONObject object=new JSONObject(result);
                            if(object.getBoolean("result")){
                                Toast.makeText(ChangeUserAlipayActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(ChangeUserAlipayActivity.this,object.getString("msg"), Toast.LENGTH_SHORT).show();
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
        }else {
            Toast.makeText(this, "支付宝账号不能为空！", Toast.LENGTH_SHORT).show();
        }

    }





}
