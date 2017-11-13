package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.StoreBean;
import com.example.mayikang.wowallet.util.UserAuthUtil;
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
//确认订单页面
@Route(path = "/user/main/act/confirm_order")
public class ConfirmOrderActivity extends BaseAppcompatActivity {

    //区分订单类型 1为普通订单  2为购买代理店铺 3为购买分享者
    @Autowired(name = "Flag")
    int Flag=-1;

    //普通是storeId   用于区分向平台订单得区分（-2 购买分享者  -3购买代理店铺）
    @Autowired(name = "id")
    public int id = -1;

    @Autowired(name = "userStoreId")
    int userStoreId=-1;
    @BindView(R.id.totPrice)
    EditText totPrice;
    @BindView(R.id.confirm_order_Img)
    CircleImageView confirmOrderImg;
    @BindView(R.id.confirm_storeName_txt)
    TextView confirmStoreNameTxt;

    private HttpServiceImpl service;

    private Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null != msg && msg.what == 0) {
                StoreBean bean= (StoreBean) msg.obj;
               /* PicassoUtil.getPicassoObject().load(bean.getLogo())
                        .resize(DpUtils.dpToPx(ConfirmOrderActivity.this,80),DpUtils.dpToPx(ConfirmOrderActivity.this,80))
                        .into(confirmOrderImg);*/
                Glide.with(ConfirmOrderActivity.this).load(bean.getLogo())
                        .placeholder(R.drawable.ic_defult_load).crossFade()
                        .error(R.mipmap.icon_load_faild)
                       .into(confirmOrderImg);

                confirmStoreNameTxt.setText("向\""+bean.getName()+"\"转账");
                }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        switch (Flag){
            case 1:
                getStoreMsg();
                break;
            case 2:
                getAgent();
                confirmOrderImg.setImageResource(R.drawable.ic_share);
                confirmStoreNameTxt.setText("向\"平台\"转账");
                break;
            case 3:
                getPayMoney();
                confirmOrderImg.setImageResource(R.drawable.ic_share);
                confirmStoreNameTxt.setText("向\"平台\"转账");
                break;
        }
    }

    @Override
    public int initLayout() {
        return R.layout.activity_confirm_order;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.writer_order_BtnSure, R.id.write_order_back})
    public void confirmOrderClick(View view) {
        switch (view.getId()) {
            case R.id.writer_order_BtnSure:

                switch (Flag){
                    case 1:
                        if(!StringUtil.isBlank(totPrice.getText().toString().trim())
                                &&StringUtil.isNumeric(totPrice.getText().toString().trim())){
                            getOrders();
                        }else {
                            Toast.makeText(this, "请输入正确的支付金额", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                        getStoreOrder();
                        break;
                    case 3:
                        saveOrders();
                        break;
                }

                break;
            case R.id.write_order_back:
                finish();
                break;
        }
    }


    //获取购买代理店铺的订单
    private void getStoreOrder() {
        HashMap<String, String> body = new HashMap<>();
        body.put("totalprice", totPrice.getText().toString().trim());
        body.put("storeId",userStoreId+"");
        body.put("userId",UserAuthUtil.getUserId()+"");
        body.put("type","2");
        service.doCommonPost(null, MainUrl.getOrder, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("getOrders",result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        if (object.getBoolean("result")) {
                            JSONObject bnOrderformTbl = object.getJSONObject("bnOrderformTbl");
                            int OrderId = bnOrderformTbl.getInt("id");
                            ARouter.getInstance().build("/main/act/confirm_pay").withInt("OrderId", OrderId).withInt("StoreId", id).withInt("Flag",2).navigation();
                            finish();
                        } else {
                            Toast.makeText(ConfirmOrderActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("getOrders",ex.toString());
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

    //购买分享者保存订单
    private void saveOrders() {
        HashMap<String, String> body = new HashMap<>();
        body.put("totalprice", totPrice.getText().toString().trim());
        body.put("userId", UserAuthUtil.getUserId() + "");
        body.put("type","3");
        service.doCommonPost(null, MainUrl.getOrder, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("getOrders",result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        if (object.getBoolean("result")) {
                            JSONObject bnOrderformTbl = object.getJSONObject("bnOrderformTbl");
                            int OrderId = bnOrderformTbl.getInt("id");
                            ARouter.getInstance().build("/main/act/confirm_pay").withInt("OrderId", OrderId).withInt("Flag",3).navigation();
                            finish();
                        } else {
                            Toast.makeText(ConfirmOrderActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("getOrders",ex.toString());
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


    //获取店铺信息
    private void getStoreMsg() {
        HashMap<String, String> body = new HashMap<>();
        body.put("ctype","store");
        body.put("cond","{id:"+id+"}");
        body.put("jf","storeLogo");

        service.doCommonPost(null, MainUrl.basePageQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        if (object.getBoolean("result")) {
                            JSONObject data = object.getJSONObject("data");
                            String storeName = data.getString("name");
                            String imgUrl = data.getJSONObject("storeLogo").getString("url");
                            StoreBean bean=new StoreBean();
                            bean.setName(storeName);
                            bean.setLogo(imgUrl);
                            Message message=new Message();
                            message.what=0;
                            message.obj=bean;
                            handle.sendMessage(message);
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


    //确认订单
    private void getOrders() {
        HashMap<String, String> body = new HashMap<>();
        body.put("totalprice", totPrice.getText().toString().trim());
        body.put("userId", UserAuthUtil.getUserId() + "");
        body.put("storeId", id + "");
        service.doCommonPost(null, MainUrl.getOrder, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("getOrders",result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        if (object.getBoolean("result")) {
                            JSONObject bnOrderformTbl = object.getJSONObject("bnOrderformTbl");
                            int OrderId = bnOrderformTbl.getInt("id");
                            ARouter.getInstance().build("/main/act/confirm_pay").withInt("OrderId", OrderId).withInt("Flag",1).navigation();
                            finish();
                        } else {
                            Toast.makeText(ConfirmOrderActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("getOrders",ex.toString());
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
                        double value=data.getDouble("value");
                        totPrice.setText(new DecimalFormat("######0.00").format(value)+"");
                        totPrice.setEnabled(false);
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


    private void getAgent() {
        HashMap<String, String> body = new HashMap<>();
        body.put("jf", "ap");
        service.doCommonPost(null, MainUrl.GetAgintlists, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray uapList=object.getJSONArray("uapList");
                        if(null!=uapList&&uapList.length()>0){
                                JSONObject data = uapList.getJSONObject(0);
                                JSONObject ap = data.getJSONObject("ap");
                                totPrice.setText(new DecimalFormat("######0.00").format(ap.getDouble("value")) + "");
                                totPrice.setEnabled(false);
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
