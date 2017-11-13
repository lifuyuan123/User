package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.text.DecimalFormat;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/main/act/join_wo_pay_delegate")
public class JoinWoPayDelegateActivity extends BaseAppcompatActivity {

    @BindView(R.id.join_wo_payMoney)
    TextView joinWoPayMoney;
    private int PayMoney=-1;
    private HttpServiceImpl service;
    private double value;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        getPayMoney();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_join_wo_pay_delegate;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.join_cost_BtnSure, R.id.join_cost_back})
    public void joinWoPayDelegateClick(View view) {
        switch (view.getId()) {
            case R.id.join_cost_BtnSure:
                ARouter.getInstance().build("/user/main/act/confirm_order").withInt("Flag",3 ).navigation();
                break;
            case R.id.join_cost_back:
                finish();
                break;
        }
    }


    //获取要交费得钱
    private void getPayMoney(){
        HashMap<String,String> body=new HashMap<>();
        body.put("ctype","paraConfig");
        body.put("cond","{key:\"payment_share_value\"}");

        service.doCommonPost(null, MainUrl.basePageQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("getPayMoney",result.toString());
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject object=new JSONObject(result);
                        JSONObject data=object.getJSONObject("data");
                        value=data.getDouble("value");
                        joinWoPayMoney.setText(new DecimalFormat("######0.00").format(value)+"");
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
