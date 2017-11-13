package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.maps.AMap;
import com.bumptech.glide.Glide;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.event.AfterInputPayPWDEvent;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.UserBean;
import com.example.mayikang.wowallet.ui.xwidget.dialog.TransferDialog;
import com.example.mayikang.wowallet.ui.xwidget.view.SecurityCodeView;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.basemodule.core.event.PushEvent;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 余额转账给好友
 */

@Route(path = "/main/act/user/transfer")
public class TransferActivity extends BaseAppcompatActivity {
    @Autowired(name = "id")
    public int id = -1;
    @BindView(R.id.civ)
    CircleImageView civ;
    @BindView(R.id.username)
    TextView tvUsername;
    @BindView(R.id.et)
    EditText et;

    private HttpServiceImpl http;
    //收款方实体
    private UserBean ub=new UserBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        http = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        if (-1 != id) {
            pullUserDetail(String.valueOf(id));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_transfer;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.back, R.id.btn_transfer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_transfer:
                //查询余额
                if(!StringUtil.isBlank(et.getText().toString())){
                    pullBalance();
                }else {
                    Toast.makeText(this, "请先输入转账金额", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    /**
     * 查询用户信息
     *
     * @param id
     */
    public void pullUserDetail(final String id) {
        HashMap<String, String> body = new HashMap<>();
        body.put("ctype", "user");
        body.put("cond", "{id:" + id + ",isDelete:1,isAdmin:1,isLocked:2,type:2,}");
        body.put("jf", "photo");
        http.doCommonPost(null, MainUrl.basePageQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {

                if (!StringUtil.isBlank(result)) {

                    try {
                        JSONObject obj = new JSONObject(result);
                        JSONArray resultList = obj.getJSONArray("resultList");
                        if (resultList != null && resultList.length() > 0) {
                            JSONObject x = resultList.getJSONObject(0);


                            String logo = x.getString("photo");
                            String logoStr = null;
                            if (!StringUtil.isBlank(logo)) {
                                logoStr = x.getJSONObject("photo").getString("url");
                               /* PicassoUtil.getPicassoObject().load(logoStr).
                                        resize(DpUtils.dpToPx(TransferActivity.this, 60), DpUtils.dpToPx(TransferActivity.this, 60))
                                        .into(civ);*/

                                Glide.with(TransferActivity.this).load(logoStr)
                                        .placeholder(R.drawable.ic_defult_load).crossFade()
                                        .error(R.mipmap.icon_load_faild)
                                       .into(civ);

                            }

                            String realName = x.getString("realName");
                            String username = x.getString("username");
                            if (StringUtil.isBlank(realName)) {
                                tvUsername.setText(username + "(对方暂未设置真实姓名)");
                            } else {
                                tvUsername.setText(username + "(" + realName + ")");
                            }

                            ub.setId(Integer.valueOf(id));
                            ub.setUrl(logo);
                            ub.setUsername(username);
                            ub.setRealName(realName);

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


    //查询账户余额
    private void pullBalance() {
        http.doCommonPost(null, MainUrl.queryUserBalanceUrl, null, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e(result.toString());
                if (!StringUtil.isBlank(result)) {
                    double amount = 0d;
                    try {
                        JSONObject obj = new JSONObject(result);
                        String msg = obj.getString("msg");
                        boolean res = obj.getBoolean("result");

                        if (res) {
                            amount = obj.getDouble("amount");
                            //弹出付款框
                            TransferDialog dialog = new TransferDialog(TransferActivity.this);
                            dialog.setAmount(et.getText().toString());
                            dialog.setBalance(amount);
                            dialog.setUb(ub);
                            dialog.show();

                        } else {
                            if ("登录用户信息异常".equals(msg)) {
                                Toast.makeText(TransferActivity.this, "登录用户信息异常", Toast.LENGTH_SHORT).show();
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


    /**
     * 执行转账
     */
    public void transfer(double amount,String payPassword){
        HashMap<String,String> map=new HashMap<>();
        map.put("friendId",String.valueOf(id));
        map.put("amount",String.valueOf(amount));
        map.put("payPassword",payPassword);
        http.doCommonPost(null, MainUrl.balanceTransferUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                LogUtil.e("转账结果",resultStr);
                if(!StringUtil.isBlank(resultStr)){
                    try {
                        JSONObject obj=new JSONObject(resultStr);
                        boolean result=obj.getBoolean("result");
                        String msg=obj.getString("msg");
                        Toast.makeText(TransferActivity.this, msg, Toast.LENGTH_SHORT).show();
                        if(result){
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onError(Throwable ex) {
                LogUtil.e("转账报错"+ex.toString());
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
                    showLoading(false,"正在转账中");
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });
    }






    @Subscribe
    public void onMainEvent(AfterInputPayPWDEvent event) {
        if(event!=null){

            if(2==event.getOp()){
                transfer(event.getPayAmount(),event.getPayPwd());
            }


        }
    }
}
