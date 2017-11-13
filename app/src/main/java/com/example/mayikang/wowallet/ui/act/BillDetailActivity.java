package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.text.DecimalFormat;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

// 账单明细
@Route(path = "/main/act/user/bill_detail")
public class BillDetailActivity extends BaseAppcompatActivity {
    //账单 id
    @Autowired(name = "id")
    public int id = -1;

    @BindView(R.id.billing_iv_shopicon)CircleImageView civStoreLogo;
    @BindView(R.id.act_bill_detail_tv_storeName)TextView tvStoreName;
    @BindView(R.id.money)TextView tvPayValue;
    @BindView(R.id.billing_tv_istransaction)TextView tvStatus;
    @BindView(R.id.billing_tv_payment_method)TextView tvPayAccount;
    @BindView(R.id.billing_tv_account)TextView tvGatheringAccount;
    @BindView(R.id.billing_tv_time)TextView tvInsertTime;
    @BindView(R.id.billing_tv_transfer_instructions)TextView tvDescribe;
    private HttpServiceImpl http;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        http = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        if (-1 != id) {
            queryBillDetail(id);
        }
    }

    @Override
    public int initLayout() {
        return R.layout.activity_bill_detail;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.shops_linear_back)
    public void onViewClicked() {
        finish();
    }

    /**
     * 查询账单详情
     *
     * @param id
     */
    public void queryBillDetail(int id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("ctype", "transFlow");
        map.put("id", String.valueOf(id));
        map.put("jf", "flowOrder|storeTbl|storeLogo|manager|ifPayment|incomeUser|expenditureUser|photo");
        http.doCommonPost(null, MainUrl.baseSingleQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                Log.e("queryBillDetail",resultStr.toString());
                if(!StringUtil.isBlank(resultStr)){

                    try {
                        JSONObject obj=new JSONObject(resultStr);
                        JSONObject data=obj.getJSONObject("data");
                        if(data!=null){
                            //操作金额
                            double amount=data.getDouble("amount");
                            //创建时间
                            String insertTime=data.getString("insertTime");
                            tvInsertTime.setText(insertTime);
//***************************根据流水类型获取交易对方账户信息***********************************************************************************



                            //交易类型
                            int fType=data.getInt("fType");
                            //对比当前用户id
                            int incomeId=data.getJSONObject("incomeUser").getInt("id");

                            if(UserAuthUtil.getUserId()==incomeId){
                                fType=1;
                            }else {
                                fType=2;
                            }

                            //收入金流
                            if(1==fType){
                                tvPayValue.setText("+"+new DecimalFormat("######0.00").format(amount));
                                String expenditureUsers=data.getString("expenditureUser");
                                if(!expenditureUsers.equals("null")){
                                    JSONObject expend=data.getJSONObject("expenditureUser");
                                    String expendLogo=null;
                                    String expendStr=expend.getString("photo");
                                    if(!StringUtil.isBlank(expendStr)){
                                        expendLogo=expend.getJSONObject("photo").getString("url");
                                       /* PicassoUtil.getPicassoObject().load(expendLogo).
                                                resize(DpUtils.dpToPx(BillDetailActivity.this,80),DpUtils.dpToPx(BillDetailActivity.this,80)).
                                                error(R.mipmap.icon_load_faild).into(civStoreLogo);*/
                                        Glide.with(BillDetailActivity.this).load(expendLogo)
                                                .placeholder(R.drawable.ic_defult_load).crossFade()
                                                .error(R.mipmap.icon_load_faild)
                                                .into(civStoreLogo);

                                    }

                                    String username=expend.getString("username");
                                    tvStoreName.setText(username);
                                }else {
                                    tvStoreName.setText("平台");
                                    civStoreLogo.setImageResource(R.drawable.ic_share);


                                }
                            }


                            //支出金流
                            if(2==fType){
                                tvPayValue.setText("-"+new DecimalFormat("######0.00").format(amount));

                                JSONObject income=data.getJSONObject("incomeUser");
                                String incomeLogo=null;
                                String incomeStr=income.getString("photo");
                                if(!StringUtil.isBlank(incomeStr)){
                                    incomeLogo=income.getJSONObject("photo").getString("url");
                                  /*  PicassoUtil.getPicassoObject().load(incomeLogo).
                                            resize(DpUtils.dpToPx(BillDetailActivity.this,80),DpUtils.dpToPx(BillDetailActivity.this,80)).
                                            error(R.mipmap.icon_load_faild).into(civStoreLogo);*/
                                    Glide.with(BillDetailActivity.this).load(incomeLogo)
                                            .placeholder(R.drawable.ic_defult_load).crossFade()
                                            .error(R.mipmap.icon_load_faild)
                                            .into(civStoreLogo);

                                }

                                String username=income.getString("username");
                                tvStoreName.setText(username);

                            }

//******************************根据 tTypeT 判断交易类型* 1-内部交易，平台帐户金额不变；2-接口交易，平台帐户金额会变化*******************************************************************************
                            int tType=data.getInt("tType");

                            //无订单
                            if(1==tType){
                                //付款方式
                                tvPayAccount.setText("余额");
                                //转账说明
                                tvDescribe.setText("余额转账");

                                //   对方帐号

                                //收入
                                if(1==fType){
                                    String n1=data.getJSONObject("expenditureUser").getString("username");
                                    tvGatheringAccount.setText(n1);
                                }
                                //支出
                                if(2==fType){
                                    String n2=data.getJSONObject("incomeUser").getString("username");
                                    tvGatheringAccount.setText(n2);
                                }

                            }



                            //提现订单
                            if(23==tType){
                                if(1==fType){//用户提现
                                    tvGatheringAccount.setText("平台账户");
                                    tvPayAccount.setText("支付宝");
                                    //JSONObject order=data.getJSONObject("flowOrder");
                                    //订单号
                                    tvDescribe.setText(data.getString("remark"));

                                }
                            }


                            //有订单
                            if(2==tType) {
                                //付款方式
                                tvPayAccount.setText("");
                                //转账说明
                                tvDescribe.setText("现金支付");
                                //收入
                                if (1 == fType) {

                                }
                                //支出
                                if (2 == fType) {
                                    JSONObject order = data.getJSONObject("flowOrder");
                                    //订单号
                                    String orderNum = order.getString("name");
                                    JSONObject store = order.getJSONObject("store");
                                    JSONObject manager = store.getJSONObject("manager");
                                    //支付宝帐号
                                    String alipayNumber = manager.getString("alipayNumber");
                                    //微信帐号
                                    String weixinpayNumber = manager.getString("weixinpayNumber");
                                    JSONObject ifPayment = order.getJSONObject("ifPayment");
                                    //现金支付方式 1:微信 2：支付宝
                                    int payType = ifPayment.getInt("type");

                                    if (1 == payType) {
                                        tvGatheringAccount.setText("微信支付（" + weixinpayNumber + "）");
                                    }

                                    if (2 == payType) {
                                        tvGatheringAccount.setText("支付宝支付（" + alipayNumber + "）");
                                    }
                                }

                            }

                                //好友转账
                                if(21==tType){
                                    //收入
                                    String desc=data.getString("desc_");
                                    tvDescribe.setText(desc);
                                    if(1==fType){
                                        String user=data.getJSONObject("expenditureUser").getString("username");
                                        tvGatheringAccount.setText(user);
                                        Log.e("user",user);
                                    }
                                    //支出
                                    if(2==fType){
                                        String user=data.getJSONObject("incomeUser").getString("username");
                                        tvGatheringAccount.setText(user);
                                        Log.e("user",user);
                                    }

                                }


                            if(tType==14){
                                //分销提成的账单
                                tvPayAccount.setText("支付宝转账");
                                String desc=data.getString("desc_");
                                tvDescribe.setText(desc);
                                tvGatheringAccount.setText("平台支付宝");
                            }


                            if(tType==15){
                                //订单返利
                                tvPayAccount.setText("支付宝转账");
                                String desc=data.getString("desc_");
                                tvDescribe.setText(desc);
                                tvGatheringAccount.setText("平台支付宝");
                            }


                            //用户消费支出
                            if(tType==22){
                                JSONObject order=data.getJSONObject("flowOrder");
                                JSONObject pay=order.getJSONObject("ifPayment");
                                //现金支付方式 1:微信 2：支付宝
                                int payType = pay.getInt("type");

                                if (1 == payType) {
                                    tvPayAccount.setText("微信支付");
                                }

                                if (2 == payType) {
                                    tvPayAccount.setText("支付宝支付");
                                }

                                if(3==payType){
                                    tvPayAccount.setText("余额支付");
                                }

                                tvDescribe.setText(data.getString("desc_"));

                                JSONObject income=data.getJSONObject("incomeUser");
                                String username=income.getString("username");
                                tvGatheringAccount.setText(username);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("queryBillDetail",e.toString());
                    }

                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("queryBillDetail",ex.toString());

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
                showLoading(true, "正在加载中");
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });


    }


}
