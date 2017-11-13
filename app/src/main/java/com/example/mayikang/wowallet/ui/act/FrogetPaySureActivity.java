package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.config.TargetPage;
import com.sctjsj.basemodule.core.cookie.CookieUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.cookie.DbCookieStore;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/main/act/FrogetPaySure")
public class FrogetPaySureActivity extends BaseAppcompatActivity {
    @Autowired(name = "key")
    String phone;
    @BindView(R.id.froget_pay_back)
    LinearLayout frogetPayBack;
    @BindView(R.id.froget_pay_NewEdt)
    EditText frogetPayNewEdt;
    @BindView(R.id.pay_sure_Edt)
    EditText paySureEdt;
    @BindView(R.id.paysure_code_edt)
    EditText paysureCodeEdt;
    @BindView(R.id.paysure_froget_btns)
    Button frogetPaySendmsg;
    @BindView(R.id.activity_forget_pay_pas)
    LinearLayout activityForgetPayPas;
    private HttpServiceImpl service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();


    }

    @Override
    public int initLayout() {
        return R.layout.activity_froget_pay_sure;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.froget_pay_back, R.id.paysure_froget_btns})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.froget_pay_back:
                finish();
                break;
            case R.id.paysure_froget_btns:
                Log.e("FrogetPaySureActivity","sendMs2");
                if(!TextUtils.isEmpty(frogetPayNewEdt.getText().toString())
                        &&!TextUtils.isEmpty(paySureEdt.getText().toString())
                        &&!TextUtils.isEmpty(paysureCodeEdt.getText().toString())){
                    Log.e("FrogetPaySureActivity","sendMsg1");
                    if(paySureEdt.getText().toString().trim().equals(frogetPayNewEdt.getText().toString().trim())){
                        frogetPayPas();
                        Log.e("FrogetPaySureActivity","sendMsg");
                    }else {
                        Toast.makeText(this, "输入的密码不一致！", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "请检查输入的是否正确！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    //修改支付密码
    private void frogetPayPas(){
        HashMap<String,String> body=new HashMap<>();
        body.put("phone",phone);
        body.put("code",paysureCodeEdt.getText().toString());
        body.put("payPassword",paySureEdt.getText().toString());
        Log.e("FrogetPaySureActivity","frogetPayPas");
        service.doCommonPost(null, MainUrl.FrogetPayUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("FrogetPaySureActivity","frogetPayPas"+result.toString());
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject object=new JSONObject(result);
                        if(object.getBoolean("result")){
                            ARouter.getInstance().build("/main/act/user/setting").navigation();

                            Toast.makeText(FrogetPaySureActivity.this, "支付密码修改成功！", Toast.LENGTH_SHORT).show();

                            finish();

                        }else {
                            Toast.makeText(FrogetPaySureActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("FrogetPaySureActivity",ex.toString());
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
