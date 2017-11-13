package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.event.PageSwitchEvent;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.text.DecimalFormat;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 加入沃克
 */
@Route(path = "/user/main/act/join")
public class JoinWoActivity extends BaseAppcompatActivity {

    @BindView(R.id.join_consume_money)
    TextView joinConsumeMoney;
    @BindView(R.id.act_join_wo_to_purchase)
    Button actJoinWoToPurchase;
    @BindView(R.id.mine_join_confirmOrderBtn)
    Button mineJoinConfirmOrderBtn;
    private HttpServiceImpl service;
    private double cumulativeSpend = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        getwoMoney();
        isOrNotShareMaker();
    }

    //判断是否是分享者
    private void isOrNotShareMaker() {
        HashMap<String, String> body = new HashMap<>();
        body.put("ctype", "user");
        body.put("id", UserAuthUtil.getUserId() + "");
        body.put("jf", "photo");
        service.doCommonPost(null, MainUrl.GetUserMessage, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject obj = new JSONObject(result);
                        if (obj.getBoolean("result")) {
                            JSONObject data = obj.getJSONObject("data");
                            if (data.getInt("isShareMaker") == 1) {
                                //是分享者
                                actJoinWoToPurchase.setVisibility(View.VISIBLE);
                                mineJoinConfirmOrderBtn.setVisibility(View.VISIBLE);
                            } else {
                                actJoinWoToPurchase.setVisibility(View.GONE);
                                mineJoinConfirmOrderBtn.setVisibility(View.GONE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("getUserInfo", ex.toString());

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


    @Override
    public int initLayout() {
        return R.layout.activity_join_wo;
    }

    @Override
    public void reloadData() {

    }


    @OnClick({R.id.mine_join_confirmOrderBtn, R.id.mine_join_back, R.id.act_join_wo_to_purchase})
    public void joinWoClick(View view) {
        switch (view.getId()) {
            case R.id.mine_join_confirmOrderBtn:
                ARouter.getInstance().build("/main/act/join_wo_pay_delegate").navigation();
                break;
            case R.id.mine_join_back:
                finish();
                break;
            case R.id.act_join_wo_to_purchase:
                EventBus.getDefault().post(new PageSwitchEvent("STORE"));
                finish();
                break;
        }
    }

    //获取当前消费得金额
    private void getwoMoney() {
        HashMap<String, String> body = new HashMap<>();
        body.put("ctype", "user");
        body.put("jf", "userSpend");
        body.put("id", UserAuthUtil.getUserId() + "");
        service.doCommonPost(null, MainUrl.baseSingleQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        if (object.getBoolean("result")) {
                            JSONObject data = object.getJSONObject("data");
                            JSONArray userSpend = data.getJSONArray("userSpend");

                            if (null != userSpend && userSpend.length() > 0) {
                                for (int i = 0; i < userSpend.length(); i++) {
                                    JSONObject userMoney = userSpend.getJSONObject(i);
                                    cumulativeSpend = userMoney.getDouble("cumulativeSpend");
                                }
                                joinConsumeMoney.setText(new DecimalFormat("######0.00").format(cumulativeSpend) + "");
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
