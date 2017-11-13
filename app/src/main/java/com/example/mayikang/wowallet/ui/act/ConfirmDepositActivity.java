package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.mayikang.wowallet.ui.xwidget.dialog.WithdrawMsgDialog;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
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
 * 确认提现页面
 */

@Route(path = "/main/act/confirm_deposit")
public class ConfirmDepositActivity extends BaseAppcompatActivity {

    @BindView(R.id.mine_withdraw_back)
    RelativeLayout mineWithdrawBack;
    @BindView(R.id.mine_withdraw_WenXinImg)
    ImageView mineWithdrawWenXinImg;
    @BindView(R.id.mine_withdraw_WenXin)
    RelativeLayout mineWithdrawWenXin;
    @BindView(R.id.mine_withdraw_alipayImg)
    ImageView mineWithdrawAlipayImg;
    @BindView(R.id.mine_withdraw_alipay)
    RelativeLayout mineWithdrawAlipay;
    @BindView(R.id.mine_withdraw_edt)
    EditText mineWithdrawEdt;
    @BindView(R.id.mine_withdraw_Btnsure)
    Button mineWithdrawBtnsure;
    @BindView(R.id.activity_mine_withdraw)
    LinearLayout activityMineWithdraw;
    @BindView(R.id.confirmDepYuE)
    TextView confirmDepYuE;
    private HttpServiceImpl http;
    private double amount=0;
    private  WithdrawMsgDialog dialog;
    private int flag=-1;
    private  UserBean bean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        http= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();

    }


    @Override
    public int initLayout() {
        return R.layout.activity_confirm_deposit;
    }

    @Override
    public void reloadData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        pullBalance();
        getUserData();
    }

    @OnClick({R.id.mine_withdraw_back, R.id.mine_withdraw_WenXin, R.id.mine_withdraw_alipay, R.id.mine_withdraw_Btnsure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mine_withdraw_back:
                finish();
                break;
            case R.id.mine_withdraw_WenXin:
                checkWeiXin();
                break;
            case R.id.mine_withdraw_alipay:
                checkAlipay();
                break;
            case R.id.mine_withdraw_Btnsure:
                confirmIsOrNot();
                break;
        }
    }


    private void checkAlipay() {
        mineWithdrawWenXinImg.setImageDrawable(getResources().getDrawable(R.mipmap.ic_check_no));
        mineWithdrawAlipayImg.setImageDrawable(getResources().getDrawable(R.mipmap.ic_check_yes));
        flag=2;
    }


    //选择微信
    private void checkWeiXin() {
        mineWithdrawWenXinImg.setImageDrawable(getResources().getDrawable(R.mipmap.ic_check_yes));
        mineWithdrawAlipayImg.setImageDrawable(getResources().getDrawable(R.mipmap.ic_check_no));
        flag=1;
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
                            amount=obj.getDouble("amount");
                            confirmDepYuE.setText("余额￥"+new DecimalFormat("######0.00").format(amount));
                        }else {
                            if("登录用户信息异常".equals(msg)){
                                Toast.makeText(ConfirmDepositActivity.this, "登录用户信息异常", Toast.LENGTH_SHORT).show();
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
                                confirmDepYuE.setText("余额￥0.00");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        confirmDepYuE.setText("余额￥0.00");
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


    //判断是否可以提现
    private void confirmIsOrNot() {
        String inputValue=mineWithdrawEdt.getText().toString();
        String realName=bean.getRealName();
        String alipayNumber=bean.getAlipayNumber();
        if(flag!=-1){
            if(StringUtil.isNumeric(inputValue)){
                double inputMoney=Double.valueOf(inputValue);
                if(inputMoney>amount){
                    Toast.makeText(this, "余额不足，请重新输入！", Toast.LENGTH_SHORT).show();
                }else {//开始提现
                    if(!StringUtil.isBlank(realName)&&!StringUtil.isBlank(alipayNumber)){//判断是否设置真实姓名
                        confirmBalance(realName,inputMoney);
                    }else {
                        Toast.makeText(this, "您还没有设置真实姓名或支付宝账号！", Toast.LENGTH_SHORT).show();
                        ARouter.getInstance().build("/main/act/user/user_info").navigation();
                    }
                }
            }else {
                Toast.makeText(this, "请输入提现得金额！", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "请选择提现方式！", Toast.LENGTH_SHORT).show();
        }
    }


    //提现
    private void confirmBalance(String realName,double inputMoney){
        HashMap<String,String> body=new HashMap<>();
            body.put("realName",realName);
            body.put("amount",inputMoney+"");
        http.doCommonPost(null, MainUrl.ConfirmAli, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("confirmBalance",result.toString());
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject object=new JSONObject(result);
                        if(object.getBoolean("result")){
                            dialog=new WithdrawMsgDialog(ConfirmDepositActivity.this,R.style.Qr_dialog);
                            dialog.setCountent(object.getString("msg"));
                            dialog.onWithdrawMsgOnclick(new WithdrawMsgDialog.WithdrawMsgOnclick() {
                                @Override
                                public void onClick() {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }else {
                            dialog=new WithdrawMsgDialog(ConfirmDepositActivity.this,R.style.Qr_dialog);
                            dialog.setImageResId(R.drawable.ic_defult_dialog);
                            dialog.setTitle("提现失败");
                            dialog.setCountent(object.getString("msg"));
                            dialog.onWithdrawMsgOnclick(new WithdrawMsgDialog.WithdrawMsgOnclick() {
                                @Override
                                public void onClick() {
                                    dialog.dismiss();
                                }
                            });
                            dialog.setCancelable(true);
                            dialog.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("confirmBalance",e.toString());
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




    //获取用户信息
    private void getUserData() {
        HashMap<String, String> body = new HashMap<>();
        body.put("ctype", "user");
        body.put("id", UserAuthUtil.getUserId() + "");
        body.put("jf", "photo");
        http.doCommonPost(null, MainUrl.GetUserMessage, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("getUserData", result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONObject data = object.getJSONObject("data");
                        bean = new UserBean();
                        bean.setRealName(data.getString("realName"));
                        bean.setAlipayNumber(data.getString("alipayNumber"));

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
