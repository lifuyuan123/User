package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.text.DecimalFormat;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

@Route(path = "/main/act/IndentMessageActivity")
public class IndentMessageActivity extends BaseAppcompatActivity {
    @Autowired(name = "id")
    int id = -1;
    @BindView(R.id.shops_linear_back)
    LinearLayout shopsLinearBack;
    @BindView(R.id.indent_message_iv_shopicon)
    CircleImageView indentMessageIvShopicon;
    @BindView(R.id.indent_message_tv_storeName)
    TextView indentMessageTvStoreName;
    @BindView(R.id.indent_message_money)
    TextView indentMessageMoney;
    @BindView(R.id.billing_tv_istransaction)
    TextView billingTvIstransaction;
    @BindView(R.id.indent_message_method)
    TextView indentMessageMethod;
    @BindView(R.id.indent_message_instructions)
    TextView indentMessageInstructions;
    @BindView(R.id.indent_message_account)
    TextView indentMessageAccount;
    @BindView(R.id.indent_message_fanli)
    TextView indentMessageFanli;
    @BindView(R.id.indent_message_fanli_lay)
    RelativeLayout indentMessageFanliLay;
    @BindView(R.id.indent_message_time)
    TextView indentMessageTime;
    @BindView(R.id.indent_message_order_number)
    TextView indentMessageOrderNumber;
    @BindView(R.id.rl_order_num)
    RelativeLayout rlOrderNum;
    @BindView(R.id.account_layout)
    RelativeLayout account_layout;
    private HttpServiceImpl service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        getIndentMsg();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_indent_message;
    }

    @Override
    public void reloadData() {

    }



    @OnClick(R.id.shops_linear_back)
    public void onViewClicked() {
        finish();
    }

    private void getIndentMsg(){
        HashMap<String,String> body=new HashMap<>();
        body.put("ctype","orderform");
        body.put("id",id+"");
        body.put("jf","storeTbl|storeLogo|manager|ifPayment|orderSettels");

        service.doCommonPost(null, MainUrl.baseSingleQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("getIndentMsg",result.toString());
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject obj=new JSONObject(result);
                        if(obj.getBoolean("result")){
                            JSONObject data=obj.getJSONObject("data");
                            indentMessageTime.setText(data.getString("insertTime"));

                            int types=data.getInt("type");
                            if(types!=1){
                                indentMessageIvShopicon.setImageResource(R.drawable.ic_share);
                                indentMessageTvStoreName.setText("平台");
                                account_layout.setVisibility(View.GONE);
                            }else {
                                     JSONObject store=data.getJSONObject("storeTbl");
                            indentMessageTvStoreName.setText(store.getString("name"));
                            PicassoUtil.getPicassoObject().load(store.getJSONObject("storeLogo").getString("url"))
                                    .error(R.mipmap.icon_default_portrait)
                                    .resize(DpUtils.dpToPx(IndentMessageActivity.this,80),DpUtils.dpToPx(IndentMessageActivity.this,80))
                                    .into(indentMessageIvShopicon);
                                JSONObject manager=store.getJSONObject("manager");
                                indentMessageAccount.setText(manager.getString("phone"));
                            }

                            indentMessageOrderNumber.setText(data.getString("name"));
                            indentMessageMoney.setText("￥"+new DecimalFormat("######0.00").format(data.getDouble("totalprice")));
                            String buyerRemark=data.getString("buyerRemark");
                            if(!StringUtil.isBlank(buyerRemark)){
                                indentMessageInstructions.setText(buyerRemark);
                            }else {
                                indentMessageInstructions.setText("暂无说明");
                            }


                            int rebateFlag=data.getInt("rebateFlag");
                            if(rebateFlag==1){
                                indentMessageFanliLay.setVisibility(View.VISIBLE);
                                JSONArray orderSettels=data.getJSONArray("orderSettels");
                                double rebateValue=0;
                                if(null!=orderSettels&&orderSettels.length()>0){
                                    JSONObject orderObj=orderSettels.getJSONObject(0);
                                    double payValue=orderObj.getDouble("payValue");
                                    double remainRebate=orderObj.getDouble("remainRebate");
                                    rebateValue=payValue-remainRebate;
                                }
                                indentMessageFanli.setText("￥"+new DecimalFormat("######0.00").format(rebateValue));

                            }else if(rebateFlag==2) {
                                indentMessageFanliLay.setVisibility(View.GONE);
                            }

                            JSONObject ifPayment=data.getJSONObject("ifPayment");
                            int type=ifPayment.getInt("type");
                            switch (type){
                                case 1:
                                    indentMessageMethod.setText("微信");
                                    break;
                                case 2:
                                    indentMessageMethod.setText("支付宝");
                                    break;
                                case 3:
                                    indentMessageMethod.setText("余额");
                                    break;

                                    default:
                                        indentMessageMethod.setText("其他");
                                    break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("getIndentMsg",e.toString());
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
