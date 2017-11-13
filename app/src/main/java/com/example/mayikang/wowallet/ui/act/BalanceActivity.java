package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.db.entity.HttpCacheTbl;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.text.DecimalFormat;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 提现页面
 */


@Route(path = "/main/act/user/balance")
public class BalanceActivity extends BaseAppcompatActivity {
    private HttpServiceImpl http;

    @BindView(R.id.tv_balance)TextView tvBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        http= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();

    }

    @Override
    public int initLayout() {
        return R.layout.activity_balance;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.mine_balance_linear,R.id.act_tv_to_balance_detail,R.id.mine_balance_back})
    public void depositOnClick(View view){
        switch (view.getId()){
            case R.id.mine_balance_linear://提现界面
                ARouter.getInstance().build("/main/act/confirm_deposit").navigation();
                break;
            case R.id.act_tv_to_balance_detail://账单明细界面
                ARouter.getInstance().build("/main/act/balance_detail").navigation();
                break;
            case R.id.mine_balance_back://返回键
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        pullBalance();
    }

    //查询账户余额
    private void pullBalance(){
        http.doCommonPost(null, MainUrl.queryUserBalanceUrl, null, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject obj=new JSONObject(result);
                        String msg=obj.getString("msg");
                        boolean res=obj.getBoolean("result");
                        if(res){
                            double amount=obj.getDouble("amount");
                            tvBalance.setText("￥"+new DecimalFormat("######0.00").format(amount));
                        }else {
                            if("登录用户信息异常".equals(msg)){
                                Toast.makeText(BalanceActivity.this, "登录用户信息异常", Toast.LENGTH_SHORT).show();
                                //清除本地 token
                                if (SPFUtil.contains(Tag.TAG_TOKEN)) {
                                    SPFUtil.removeOne(Tag.TAG_TOKEN);
                                }
                                //清除本地用户信息
                                if (SPFUtil.contains(Tag.TAG_USER)) {
                                    SPFUtil.removeOne(Tag.TAG_USER);
                                }
                                ARouter.getInstance().build("/main/act/login").navigation();
                            }else {
                                tvBalance.setText("￥0.00");
                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        tvBalance.setText("￥0.00");
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
