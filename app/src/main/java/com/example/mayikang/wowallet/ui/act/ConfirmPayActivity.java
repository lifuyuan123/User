package com.example.mayikang.wowallet.ui.act;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.event.WxPayEvent;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.OrderBean;
import com.example.mayikang.wowallet.modle.javabean.StoreBean;
import com.example.mayikang.wowallet.modle.javabean.UserBean;
import com.example.mayikang.wowallet.pay.alipay.PayResult;
import com.example.mayikang.wowallet.ui.xwidget.dialog.BalancePayDialog;
import com.example.mayikang.wowallet.ui.xwidget.dialog.TransferDialog;
import com.example.mayikang.wowallet.util.RandCharsUtils;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.example.mayikang.wowallet.util.WXSignUtils;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.base.util.setup.SetupUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.basemodule.core.event.PushEvent;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/main/act/confirm_pay")
public class ConfirmPayActivity extends BaseAppcompatActivity {

    @BindView(R.id.pay_order_back)
    RelativeLayout payOrderBack;
    @BindView(R.id.pay_order_weixinImg)
    ImageView payOrderWeixinImg;

    @BindView(R.id.pay_order_alipayImg)
    ImageView payOrderAlipayImg;

    @BindView(R.id.iv_pay_order_balance)
    ImageView ivPayBalance;

    @BindView(R.id.pay_order_Btnsure)
    Button payOrderBtnsure;
    @BindView(R.id.activity_confirm_order)
    LinearLayout activityConfirmOrder;
    @Autowired(name = "OrderId")
    int OrderId = -1;

    @Autowired(name = "StoreId")
    int StoreId = -1;

    //区分订单类型 1为普通订单  2为购买代理店铺 3为购买分享者
    @Autowired(name = "Flag")
    int Flag=-1;


    @BindView(R.id.confirm_Store_Img)
    ImageView confirmStoreImg;
    @BindView(R.id.confirm_pay_money)
    TextView confirmPayMoney;
    @BindView(R.id.confirm_remark)
    TextView confirmRemark;
    @BindView(R.id.ll_pay_order_balance)
    LinearLayout ll_pay_order_balance;

    private HttpServiceImpl server;
    private int payWay = 0;
    //1支付宝 2微信 3余额
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private int StateFlag = -1;

    private IWXAPI iwxapi;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Log.e("支付成功", "--------");
                        reServerPay();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。

                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iwxapi= WXAPIFactory.createWXAPI(ConfirmPayActivity.this,"wxebe6e5abd89af26b",false);
        server = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        if(Flag!=1){
            ll_pay_order_balance.setVisibility(View.GONE);
            confirmStoreImg.setImageResource(R.drawable.ic_share);
        }else {
            ll_pay_order_balance.setVisibility(View.VISIBLE);
        }
        getOrderState();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_confirm_pay;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.pay_order_back, R.id.pay_order_weixin, R.id.ll_pay_order_alipayImg,
            R.id.pay_order_Btnsure, R.id.ll_pay_order_balance})
    public void onVieClick(View view) {
        switch (view.getId()) {
            case R.id.pay_order_back:
                finish();
                break;
            case R.id.pay_order_weixin:
                checkWeiXin();
                break;
            case R.id.ll_pay_order_alipayImg:
                checkAlipay();
                break;
            case R.id.ll_pay_order_balance:
                checkBalance();
                break;
            case R.id.pay_order_Btnsure:

                //获取不同的签名方式
                if (getPayWayIsOrNot()) {

                    if (1 == payWay) {
                        getAliPaySign();
                    }

                    if (2 == payWay) {
                        getIWXSign();
                    }

                    if (3 == payWay) {
                     /*   UserBean bean=UserAuthUtil.getCurrentUser();
                       if(bean.isHasPayPassword()){

                       }else {

                       }*/
                       userIsHasPayPassWord();

                    }

                }

                break;
        }
    }
    //用户是否有支付密码
    private void userIsHasPayPassWord() {
        server.doCommonPost(null, MainUrl.isHasPayPasswordUrl, null, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if(!StringUtil.isBlank(result)){
                    try {
                        Log.e("userIsHasPayPassWord",result.toString());
                        JSONObject object=new JSONObject(result);
                        if(object.getBoolean("result")){
                            //有支付密码
                            pullBalance();
                        }else {
                            //没有支付密码
                            Snackbar.make(activityConfirmOrder,"您还没有设置支付密码",Snackbar.LENGTH_LONG).setAction("去设置", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ARouter.getInstance().build("/main/act/user/setting").navigation();
                                }
                            }).show();
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


    //选择支付宝
    private void checkAlipay() {
        payOrderWeixinImg.setImageDrawable(getResources().getDrawable(R.mipmap.ic_check_no));
        payOrderAlipayImg.setImageDrawable(getResources().getDrawable(R.mipmap.ic_check_yes));
        ivPayBalance.setImageDrawable(getResources().getDrawable(R.mipmap.ic_check_no));
        payWay = 1;
    }

    //选择微信付款
    private void checkWeiXin() {
        payOrderWeixinImg.setImageDrawable(getResources().getDrawable(R.mipmap.ic_check_yes));
        payOrderAlipayImg.setImageDrawable(getResources().getDrawable(R.mipmap.ic_check_no));
        ivPayBalance.setImageDrawable(getResources().getDrawable(R.mipmap.ic_check_no));
        payWay = 2;
    }

    //选择余额支付
    private void checkBalance() {
        payOrderWeixinImg.setImageDrawable(getResources().getDrawable(R.mipmap.ic_check_no));
        payOrderAlipayImg.setImageDrawable(getResources().getDrawable(R.mipmap.ic_check_no));
        ivPayBalance.setImageDrawable(getResources().getDrawable(R.mipmap.ic_check_yes));
        payWay = 3;
    }

    //判断支付是否可用
    private boolean getPayWayIsOrNot() {
        boolean flag = false;
        switch (payWay) {
            case 0:
                Toast.makeText(this, "请选择支付方式！", Toast.LENGTH_SHORT).show();
                flag = false;
                break;
            case 1:
                //判断手机是否安装支付宝
                if (SetupUtil.getInstance(ConfirmPayActivity.this).isXSetuped("com.eg.android.AlipayGphone")) {
                    flag = true;
                } else {
                    //未安装支付宝
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmPayActivity.this);
                    builder.setTitle("异常提示");
                    builder.setMessage("系统检测您的手机暂未安装支付宝，无法使用支付宝支付结账");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                    flag = false;
                }

                break;
            case 2:
                if (SetupUtil.getInstance(ConfirmPayActivity.this).isXSetuped("com.tencent.mm")) {
                    flag = true;
                } else {
                    //未安装微信
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmPayActivity.this);
                    builder.setTitle("异常提示");
                    builder.setMessage("系统检测您的手机暂未安装微信，无法使用微信支付结账");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which){

                        }
                    });
                    builder.show();
                    flag = false;
                }
                break;
            //余额支付不需要检视
            case 3:
                flag=true;
                break;
        }
        return flag;
    }

    /**
     * 订阅微信消息
     */
    //订阅消息
    @Subscribe
    public void OnEventMainThread(WxPayEvent event) {
        if(null!=event){
            int state=event.getState();
            switch (state){
                case 0://成功
                    Log.e("支付成功", "--------");
                    reServerPay();
                    break;
                case -1://错误
                    Snackbar.make(activityConfirmOrder,"支付参数错误",Snackbar.LENGTH_LONG).show();
                    break;
                case -2://取消
                    Snackbar.make(activityConfirmOrder,"支付取消",Snackbar.LENGTH_LONG).show();
                    break;
            }
        }



    }


    /**
     * 调用支付宝支付
     */
    private void alipay(final String sign) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(ConfirmPayActivity.this);
                Map<String, String> result = alipay.payV2(sign, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }



    //获取微信得签名
    private void getIWXSign() {
        HashMap<String,String> body=new HashMap<>();
        body.put("ofId",OrderId+"");
        body.put("type",Flag+"");
        server.doCommonPost(null, MainUrl.WeiXinPay, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("getIWXSign",result.toString());
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject object=new JSONObject(result);
                        String weixinPost=object.getString("weixinPost");
                        JSONObject weixin=new JSONObject(weixinPost);
                        String sign=weixin.getString("sign");
                        String nonce_str=weixin.getString("nonce_str");
                        String appid=weixin.getString("appid");
                        String mch_id=weixin.getString("mch_id");
                        String prepay_id=weixin.getString("prepay_id");
                        wxpay(appid,mch_id,prepay_id,nonce_str,sign);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("getIWXSign",e.toString());
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


    //唤起微信支付
    private void wxpay(String appId,String partnerId,String prepayId,String nonceStr,String sign) {
        //时间戳
        String time=String.valueOf(System.currentTimeMillis()/1000);
//        //随机字符串
        String ranStr= RandCharsUtils.getRandomString(16);
        //生成签名
        SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
        parameters.put("appid",appId);
        parameters.put("noncestr",ranStr);
        parameters.put("partnerid", partnerId);
        parameters.put("prepayid", prepayId);
        String str="Sign=WXPay";
        parameters.put("package",str);
        parameters.put("timestamp",time);
        String xSign= WXSignUtils.createSign("UTF-8",parameters);


        PayReq req = new PayReq();
        //应用 wxd34f7dc698b271af
        req.appId = appId;
        //随机字符串
        req.nonceStr = ranStr;
        //扩展字段
        req.packageValue =str;
        //商户号
        req.partnerId = partnerId;
        //预支付交易 id
        req.prepayId = prepayId;
        //时间戳
        req.timeStamp =time;//获取系统时间的10位的时间戳app data";
        req.sign=xSign;
        iwxapi.sendReq(req);

    }



    //获取订单信息
    private void getOrderState() {
        HashMap<String, String> body = new HashMap<>();
        body.put("ctype", "orderform");
        body.put("id", OrderId + "");
        body.put("jf", "storeTbl|storeLogo");
        server.doCommonPost(null, MainUrl.baseSingleQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (!StringUtil.isBlank(result)) {
                    try {
                        Log.e("getOrderState",result.toString());
                        JSONObject object = new JSONObject(result);
                        if (object.getBoolean("result")) {
                            JSONObject data = object.getJSONObject("data");
                            StateFlag = data.getInt("state");
                            OrderBean bean = new OrderBean();
                            bean.setId(data.getInt("id"));
                            bean.setState(StateFlag);
                            bean.setTotalprice(data.getDouble("totalprice"));
                            bean.setBuyerRemark(data.getString("buyerRemark"));
                            String storeTbl=data.getString("storeTbl");
                            if(Flag!=1){
                                confirmPayMoney.setText("¥" + new DecimalFormat("######0.00").format(bean.getTotalprice()));
                                confirmRemark.setText(bean.getBuyerRemark() + "");
                                payOrderBtnsure.setText("确认支付¥" + new DecimalFormat("######0.00").format(bean.getTotalprice()) );
                             }else {
                                StoreBean storeBean = new StoreBean();
                                JSONObject store = data.getJSONObject("storeTbl");
                                storeBean.setLogo(store.getJSONObject("storeLogo").getString("url"));
                                bean.setBean(storeBean);
                                RefreshView(bean);
                            }
                        } else {
                            StateFlag = -1;
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
            public void onStarted()     {

            }

            @Override
            public void onLoading(long total, long current) {

            }
        });

    }

    //验证服务器支付状态
    private void reServerPay() {
        HashMap<String, String> body = new HashMap<>();
        body.put("ctype", "orderform");
        body.put("id", OrderId + "");

        server.doCommonPost(null, MainUrl.baseSingleQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("reServerPay", result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        if (object.getBoolean("result")) {
                            JSONObject data = object.getJSONObject("data");
                            int state = data.getInt("state");
                            if (state == 3) {
                                ARouter.getInstance().build("/main/act/PayDetailActivity").withInt("OrderId", OrderId).withInt("payWay", payWay).withInt("Flag",Flag).navigation();
                                finish();
                            } else if (state == 2) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmPayActivity.this);
                                builder.setTitle("支付提醒");
                                builder.setMessage("支付失败，请重新支付！");
                                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.show();
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


    //刷新界面
    private void RefreshView(OrderBean bean) {
        if (null != bean) {
           /* PicassoUtil.getPicassoObject()
                    .load(bean.getBean().getLogo()).resize(DpUtils.dpToPx(ConfirmPayActivity.this, 80)
                    , DpUtils.dpToPx(ConfirmPayActivity.this, 80)).into(confirmStoreImg);*/
            Glide.with(this).load(bean.getBean().getLogo())
                    .placeholder(R.drawable.ic_defult_load).crossFade()
                    .error(R.mipmap.icon_load_faild)
                   .into(confirmStoreImg);

            confirmPayMoney.setText("¥" +new DecimalFormat("######0.00").format(bean.getTotalprice()) );
            confirmRemark.setText(bean.getBuyerRemark() + "");
            payOrderBtnsure.setText("确认支付¥" +new DecimalFormat("######0.00").format(bean.getTotalprice()) );
        }
    }


    //获取支付宝订单签名
    private void getAliPaySign() {
        HashMap<String, String> body = new HashMap<>();
        body.put("ofId", OrderId + "");
        body.put("type",Flag+"");

        server.doCommonPost(null, MainUrl.getPaySign, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("getAliPaySign",result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        if (object.getBoolean("result")) {
                            String sign = object.getString("sign");
                            alipay(sign);
                        } else {
                            Snackbar.make(activityConfirmOrder, "支付参数错误", Snackbar.LENGTH_LONG).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("error", ex.toString());
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

    //查询账户余额
    private void pullBalance() {
        server.doCommonPost(null, MainUrl.queryUserBalanceUrl, null, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("余额",result);
                if (!StringUtil.isBlank(result)) {
                    double amount = 0d;
                    try {
                        JSONObject obj = new JSONObject(result);
                        String msg = obj.getString("msg");
                        boolean res = obj.getBoolean("result");

                        if (res) {
                            amount = obj.getDouble("amount");
                            //弹出付款框
                            BalancePayDialog dialog = new BalancePayDialog(ConfirmPayActivity.this);
                            String str1=confirmPayMoney.getText().toString();
                            final String str2=str1.replaceAll("¥".trim(),"");
                            dialog.setAmount(str2);
                            dialog.setBalance(amount);
                            //密码输入完成之后去调用余额支付
                            dialog.setOnPayListener(new BalancePayDialog.PayListener() {
                                @Override
                                public void onPWDInput(String pwd) {
                                    payByBalance(pwd,str2);
                                }
                            });
                            dialog.show();

                        } else {
                            //用户无余额
                            if("用户无余额".equals(msg)){
                                Snackbar.make(activityConfirmOrder,"当前账户无余额，请更换支付方式", Snackbar.LENGTH_SHORT).show();
                            }
                            if ("登录用户信息异常".equals(msg)) {
                                Toast.makeText(ConfirmPayActivity.this, "登录用户信息异常", Toast.LENGTH_SHORT).show();
                                //清除本地 token
                                if (SPFUtil.contains(Tag.TAG_TOKEN)) {
                                    SPFUtil.removeOne(Tag.TAG_TOKEN);
                                }
                                //清除本地用户信息
                                if (SPFUtil.contains(Tag.TAG_USER)) {
                                    SPFUtil.removeOne(Tag.TAG_USER);
                                }
                                ARouter.getInstance().build("/main/act/login").navigation();
                            } else {
                                amount = 0d;
                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("余额",ex.toString());
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

    //通过余额支付
    private void payByBalance(String pwd,String amount){
        HashMap<String,String> map=new HashMap<>();
        map.put("ofId",String.valueOf(OrderId));
        map.put("userId",String.valueOf(UserAuthUtil.getUserId()));
        map.put("payPassword",pwd);
        map.put("amount",amount);
        server.doCommonPost(null, MainUrl.payByBalanceUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                Log.e("payByBalance",resultStr.toString());
                if(!StringUtil.isBlank(resultStr)){
                    try {
                        JSONObject object=new JSONObject(resultStr);
                        boolean result=object.getBoolean("result");
                        if(result){
                            Toast.makeText(ConfirmPayActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                            reServerPay();
                        } else {
                            Toast.makeText(ConfirmPayActivity.this,  object.getString("msg"), Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("payByBalance",ex.toString());
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
                showLoading(true,"付款中");

            }

            @Override
            public void onLoading(long total, long current) {

            }
        });
    }






}
