package com.example.mayikang.wowallet.ui.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.event.UserLoginEvent;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.UserBean;
import com.example.mayikang.wowallet.ui.xwidget.dialog.QrDialog;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.text.DecimalFormat;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by mayikang on 17/5/8.
 */

public class OwnFragment extends Fragment {
    @BindView(R.id.circleImageView)
    CircleImageView civ;
    @BindView(R.id.own_userName)
    TextView tvUserName;
    @BindView(R.id.own_userPhone)
    TextView tvPhone;
    @BindView(R.id.mine_showQr)
    ImageView mineShowQr;
    @BindView(R.id.mine_userInfo)
    LinearLayout mineUserInfo;
    @BindView(R.id.mine_bill_ll)
    LinearLayout mineBillLl;
    @BindView(R.id.own_orderform_ll)
    LinearLayout ownOrderformLl;
    @BindView(R.id.mine_join_ll)
    LinearLayout mineJoinLl;
    @BindView(R.id.own_userFance_ll)
    LinearLayout ownUserFanceLl;
    @BindView(R.id.own_usercollect_ll)
    LinearLayout ownUsercollectLl;
    @BindView(R.id.own_useBlance)
    TextView ownUseBlance;
    @BindView(R.id.mine_balance_Layout)
    LinearLayout mineBalanceLayout;
    @BindView(R.id.agent_mange_layout)
    LinearLayout agent_mange_layout;
    @BindView(R.id.isShare_layout)
    LinearLayout isShare_layout;

    private QrDialog mQrDialog;
    private HttpServiceImpl http;
    @BindView(R.id.ll_after_login)
    LinearLayout llAfterLogin;
    @BindView(R.id.tv_to_login)
    TextView tvToLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_own, null);
        http = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        //判断是否登录显示头像等信息
        return view;
    }
    //初始化view
    private void initView() {

    }


    @Override
    public void onResume() {
        super.onResume();
        initView();

        if (UserAuthUtil.isUserLogin()) {
            pullBalance();
            getUserInfo();
            llAfterLogin.setVisibility(View.VISIBLE);
            tvToLogin.setVisibility(View.GONE);

        } else {
            llAfterLogin.setVisibility(View.GONE);
            tvToLogin.setVisibility(View.VISIBLE);
            agent_mange_layout.setVisibility(View.GONE);
            PicassoUtil.getPicassoObject().load(R.mipmap.icon_default_portrait).into(civ);


            ownUseBlance.setText("￥0.00");
        }

    }
    //获取当前用户信息
    private void getUserInfo() {
        HashMap<String,String> body=new HashMap<>();
        body.put("ctype","user");
        body.put("id",UserAuthUtil.getUserId()+"");
        body.put("jf","photo");
        http.doCommonPost(null,MainUrl.GetUserMessage, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject obj=new JSONObject(result);
                       if(obj.getBoolean("result")){
                           JSONObject data=obj.getJSONObject("data");
                           tvUserName.setText(data.getString("username"));
                           tvPhone.setText(data.getString("phone"));

                          /* PicassoUtil.getPicassoObject().load(data.getJSONObject("photo").getString("url")).
                                   resize(DpUtils.dpToPx(getActivity(), 80), DpUtils.dpToPx(getActivity(), 80)).
                                   error(R.mipmap.icon_load_faild).into(civ);*/
                          Glide.with(getActivity()).load(data.getJSONObject("photo").getString("url"))
                                  .placeholder(R.drawable.ic_defult_load).crossFade()
                                  .error(R.mipmap.icon_load_faild)
                                  .into(civ);


                           int agent=data.getInt("agent");
                           if(agent==1){
                               agent_mange_layout.setVisibility(View.VISIBLE);
                           }else {
                               agent_mange_layout.setVisibility(View.GONE);
                           }

                           int isShareMaker=data.getInt("isShareMaker");
                           if(isShareMaker==1){
                               isShare_layout.setVisibility(View.VISIBLE);
                           }else {
                               isShare_layout.setVisibility(View.GONE);
                           }
                       }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("getUserInfo",ex.toString());

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
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.mine_setting_layout, R.id.mine_userInfo, R.id.mine_showQr, R.id.mine_bill_ll,
            R.id.own_orderform_ll, R.id.mine_join_ll, R.id.own_userFance_ll, R.id.own_usercollect_ll,
            R.id.mine_balance_Layout, R.id.tv_to_login,R.id.own_agent_mange,R.id.own_commebtList})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mine_setting_layout:
                 ARouter.getInstance().build("/main/act/user/setting").navigation();
               //ARouter.getInstance().build("/main/act/ProtocolActivity").navigation();
                break;
            case R.id.mine_userInfo:
                if(UserAuthUtil.isUserLogin()){
                    ARouter.getInstance().build("/main/act/user/user_info").navigation();
                }else {
                    Toast.makeText(getActivity(), "请登录后操作", Toast.LENGTH_SHORT).show();
                    ARouter.getInstance().build("/main/act/login").navigation();
                }
                break;
            case R.id.mine_showQr:
                if(UserAuthUtil.isUserLogin()){
                    showQR();
                }else {
                    Toast.makeText(getActivity(), "请登录后操作", Toast.LENGTH_SHORT).show();
                    ARouter.getInstance().build("/main/act/login").navigation();
                }
                break;
            case R.id.mine_bill_ll:
                ARouter.getInstance().build("/main/act/user/bill").navigation();
                break;
            case R.id.own_orderform_ll://订单查询
                ARouter.getInstance().build("/user/main/act/indent").navigation();
                break;
            case R.id.mine_join_ll://加入分享者
                ARouter.getInstance().build("/user/main/act/join").navigation();
                break;
            case R.id.own_userFance_ll:
                ARouter.getInstance().build("/main/act/user/fan").navigation();
                break;
            //收藏店铺
            case R.id.own_usercollect_ll:
                ARouter.getInstance().build("/main/act/user/Collection").navigation();
                break;
            case R.id.mine_balance_Layout:
                //账户余额界面
                ARouter.getInstance().build("/main/act/user/balance").navigation();
                break;
            //去登录
            case R.id.tv_to_login:
                ARouter.getInstance().build("/main/act/login").navigation();
                break;
            //代理商管理
            case R.id.own_agent_mange:
                ARouter.getInstance().build("/main/user/act/AgentMangeActivity").navigation();
                break;
            //评价列表
            case R.id.own_commebtList:
                ARouter.getInstance().build("/main/act/user/CommentListMangeActivity").navigation();
                break;
        }
    }

    private void showQR() {
        if(mQrDialog==null){
            mQrDialog = new QrDialog(getActivity(), R.style.Qr_dialog);
        }
        mQrDialog.setUser(UserAuthUtil.getCurrentUser());
        if(!mQrDialog.isShowing()){
            mQrDialog.show();
        }
    }


    @Subscribe
    public void onMainEvent(UserLoginEvent event) {
        if (event != null) {
            if (1 == event.getStatus()) {
                String data = SPFUtil.get(Tag.TAG_USER, "null").toString();
                UserBean ub = new Gson().fromJson(data, UserBean.class);

                if (ub == null) {
                    return;
                }

                tvUserName.setText(ub.getUsername());
                tvPhone.setText(ub.getPhone());
                PicassoUtil.getPicassoObject().load(ub.getUrl()).resize(DpUtils.dpToPx(getActivity(), 80), DpUtils.dpToPx(getActivity(), 80)).error(R.mipmap.icon_load_faild).into(civ);


            }
        }
    }


    private void pullBalance() {
        http.doCommonPost(null, MainUrl.queryUserBalanceUrl, null, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {

                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject obj = new JSONObject(result);
                        String msg=obj.getString("msg");
                        boolean res=obj.getBoolean("result");
                        if(res){
                            double amount = obj.getDouble("amount");
                            ownUseBlance.setText("￥" + new DecimalFormat("######0.00").format(amount));
                        }else {
                            if("登录用户信息异常".equals(msg)){
                                Toast.makeText(getActivity(), "登录用户信息异常", Toast.LENGTH_SHORT).show();
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
                                ownUseBlance.setText("￥0.00");
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        ownUseBlance.setText("0");
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
