package com.example.mayikang.wowallet.ui.frg;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.event.UserLoginEvent;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.StoreBean;
import com.example.mayikang.wowallet.modle.javabean.UserBean;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.google.gson.Gson;
import com.jauker.widget.BadgeView;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.widget.dialog.CommonDialog;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
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
 * Created by lifuy on 2017/9/28.
 */

public class OwnsFragment extends Fragment {
    @BindView(R.id.own_lin_background)
    LinearLayout ownLinBackground;
    @BindView(R.id.own_iv_icon)
    CircleImageView ownIvIcon;
    @BindView(R.id.own_tv_name)
    TextView ownTvName;
    @BindView(R.id.own_tv_phone)
    TextView ownTvPhone;
    @BindView(R.id.own_tv_balance)
    TextView ownTvBalance;
    @BindView(R.id.own_tv_collection)
    TextView ownTvCollection;
    @BindView(R.id.own_tv_fans_count)
    TextView ownTvFansCount;
    @BindView(R.id.own_lin_user_info)
    LinearLayout ownLinUserInfo;
    @BindView(R.id.own_lin_user_info_out)
    LinearLayout ownLinUserInfoOut;
    @BindView(R.id.own_liniv_payment)
    LinearLayout ownIvPayment;
    @BindView(R.id.own_liniv_delivery)
    LinearLayout ownIvOwnDelivery;
    @BindView(R.id.own_liniv_take_delivery)
    LinearLayout ownIvDelivery;
    @BindView(R.id.own_liniv_goods_evaluate)
    LinearLayout ownIvGoodsEvaluate;
    @BindView(R.id.own_scrollview)
    ScrollView ownScrollview;
    private HttpServiceImpl http;
    private BadgeView badgeview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_owns, null);
        http = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }


    private void initView() {
        //购物车商品数量操作相关
        badgeview = new BadgeView(getActivity());
        badgeview.setTargetView(ownIvPayment);
        badgeview.setBadgeGravity(Gravity.RIGHT | Gravity.TOP);
        badgeview.setBadgeCount(3);
        badgeview.setBackground(12, Color.parseColor("#FB4644"));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UserAuthUtil.isUserLogin()) {
            pullBalance();
            getUserInfo();
            ownLinUserInfo.setVisibility(View.VISIBLE);
            ownLinUserInfoOut.setVisibility(View.GONE);
            pullUserFans();//获取粉丝数量
            pullCollectionStore();//获取收藏店铺数

        } else {
            ownLinUserInfo.setVisibility(View.GONE);
            ownLinUserInfoOut.setVisibility(View.VISIBLE);
            ownTvBalance.setText("￥0.00");
        }


    }

    @OnClick({R.id.own_lin_edit, R.id.own_tv_name_out, R.id.own_lin_allorder, R.id.own_lin_payment, R.id.own_lin_delivery, R.id.own_lin_take_delivery,
            R.id.own_lin_goods_evaluate, R.id.lin_essential_tool, R.id.own_lin_safe, R.id.own_lin_history_evaluate, R.id.own_lin_aboutme,
            R.id.own_lin_verson, R.id.own_bt_logout, R.id.own_lin_balance, R.id.own_lin_collection, R.id.own_lin_fans,
            R.id.own_lin_custom_service, R.id.own_lin_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //个人资料编辑
            case R.id.own_lin_edit:
                ARouter.getInstance().build("/main/act/user/user_info").navigation();
                break;
            //点击登录
            case R.id.own_tv_name_out:
                ARouter.getInstance().build("/main/act/login").navigation();
                break;
            //查看全部订单
            case R.id.own_lin_allorder:
                ARouter.getInstance().build("/main/act/user/AllOrders").navigation();
                break;
            //待支付
            case R.id.own_lin_payment:
                ARouter.getInstance().build("/main/act/user/AllOrders").withInt("type",1).navigation();
                break;
            //代发货
            case R.id.own_lin_delivery:
                ARouter.getInstance().build("/main/act/user/AllOrders").withInt("type",2).navigation();
                break;
            //待收货
            case R.id.own_lin_take_delivery:
                ARouter.getInstance().build("/main/act/user/AllOrders").withInt("type",3).navigation();
                break;
            //待评价
            case R.id.own_lin_goods_evaluate:
                ARouter.getInstance().build("/main/act/user/AllOrders").withInt("type",4).navigation();
                break;
            //查看全部工具
            case R.id.lin_essential_tool:
                break;
            //账户安全
            case R.id.own_lin_safe:
                ARouter.getInstance().build("/main/act/user/AccountSecurity").navigation();
                break;
            //历史评价
            case R.id.own_lin_history_evaluate:
                ARouter.getInstance().build("/main/act/user/CommentListMangeActivity").navigation();
                break;
            //关于我们
            case R.id.own_lin_aboutme:
               ARouter.getInstance().build("/main/act/AboutMe").navigation();
                break;
            //当前版本
            case R.id.own_lin_verson:
                ARouter.getInstance().build("/main/act/Verson").navigation();
                break;
            //退出登录
            case R.id.own_bt_logout:
                logout();
                break;
            //余额
            case R.id.own_lin_balance:
                ARouter.getInstance().build("/main/act/user/balance").navigation();
                break;
            //收藏
            case R.id.own_lin_collection:
                ARouter.getInstance().build("/main/act/user/Collection").navigation();
                break;
            //粉丝
            case R.id.own_lin_fans:
                ARouter.getInstance().build("/main/act/user/fan").navigation();
                break;
            //客服
            case R.id.own_lin_custom_service:
                ARouter.getInstance().build("/main/act/store2").navigation();
                break;
            //消息
            case R.id.own_lin_message:
                ARouter.getInstance().build("/main/act/message_list").navigation();
                break;
        }
    }

    //退出登录
    private void logout() {
        final CommonDialog dia = new CommonDialog(getActivity());
        dia.setTitle("确认退出");
        dia.setContent("确认退出当前账号？");
        dia.setCancelClickListener("取消", new CommonDialog.CancelClickListener() {
            @Override
            public void clickCancel() {
                dia.dismiss();
            }
        });
        dia.setConfirmClickListener("退出", new CommonDialog.ConfirmClickListener() {
            @Override
            public void clickConfirm() {
                new UserAuthUtil().doLogout(getActivity(), new UserAuthUtil.LogInOutListener() {
                    @Override
                    public void onSuccess() {
                        ownLinUserInfo.setVisibility(View.GONE);
                        ownLinUserInfoOut.setVisibility(View.VISIBLE);
                        ownTvBalance.setText("￥0.00");
                        Toast.makeText(getActivity(), "退出登陆成功！", Toast.LENGTH_SHORT).show();
                        dia.dismiss();
                    }
                    @Override
                    public void onFailed() {}
                    @Override
                    public void onError() {}
                });
            }
        });
        dia.show();

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
                ownTvName.setText(ub.getUsername());
                ownTvPhone.setText(ub.getPhone());
                PicassoUtil.getPicassoObject().load(ub.getUrl()).resize(DpUtils.dpToPx(getActivity(), 80), DpUtils.dpToPx(getActivity(), 80)).error(R.mipmap.icon_load_faild).into(ownIvIcon);


            }
        }
    }


    //获取余额
    private void pullBalance() {
        http.doCommonPost(null, MainUrl.queryUserBalanceUrl, null, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {

                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject obj = new JSONObject(result);
                        String msg = obj.getString("msg");
                        boolean res = obj.getBoolean("result");
                        if (res) {
                            double amount = obj.getDouble("amount");
                            ownTvBalance.setText("￥" + new DecimalFormat("######0.00").format(amount));
                        } else {
                            if ("登录用户信息异常".equals(msg)) {
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
                            } else {
                                ownTvBalance.setText("￥0.00");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        ownTvBalance.setText("0");
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {}
            @Override
            public void onCancelled(Callback.CancelledException cex) {}
            @Override
            public void onFinished() {}
            @Override
            public void onWaiting() {}
            @Override
            public void onStarted() {}
            @Override
            public void onLoading(long total, long current) {}
        });

    }

    //获取当前用户信息
    private void getUserInfo() {
        HashMap<String, String> body = new HashMap<>();
        body.put("ctype", "user");
        body.put("id", UserAuthUtil.getUserId() + "");
        body.put("jf", "photo");
        http.doCommonPost(null, MainUrl.GetUserMessage, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject obj = new JSONObject(result);
                        if (obj.getBoolean("result")) {
                            JSONObject data = obj.getJSONObject("data");
                            ownTvName.setText(data.getString("username"));
                            ownTvPhone.setText(data.getString("phone"));

                          /* PicassoUtil.getPicassoObject().load(data.getJSONObject("photo").getString("url")).
                                   resize(DpUtils.dpToPx(getActivity(), 80), DpUtils.dpToPx(getActivity(), 80)).
                                   error(R.mipmap.icon_load_faild).into(civ);*/
                            Glide.with(getActivity()).load(data.getJSONObject("photo").getString("url"))
                                    .error(R.mipmap.icon_load_faild)
                                    .into(ownIvIcon);


                            int agent = data.getInt("agent");
//                            if(agent==1){
//                                agent_mange_layout.setVisibility(View.VISIBLE);
//                            }else {
//                                agent_mange_layout.setVisibility(View.GONE);
//                            }

                            int isShareMaker = data.getInt("isShareMaker");
//                            if(isShareMaker==1){
//                                isShare_layout.setVisibility(View.VISIBLE);
//                            }else {
//                                isShare_layout.setVisibility(View.GONE);
//                            }
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
            public void onCancelled(Callback.CancelledException cex) {}
            @Override
            public void onFinished() {}
            @Override
            public void onWaiting() {}
            @Override
            public void onStarted() {}
            @Override
            public void onLoading(long total, long current) {}
        });

    }


    //请求粉丝数据
    public void pullUserFans() {
        HashMap<String, String> body = new HashMap<>();
        body.put("parentId", UserAuthUtil.getUserId() + "");
        body.put("jf", "children|photo");
        http.doCommonPost(null, MainUrl.getFansList, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("pullUserFans", result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        int fansCount=object.getInt("childrens");
                        ownTvFansCount.setText(fansCount+"");
                    } catch (JSONException e) {
                        Log.e("pullUserFans", e.toString());
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onError(Throwable ex) {
                Log.e("pullUserFanse", ex.toString());
            }
            @Override
            public void onCancelled(Callback.CancelledException cex) {}
            @Override
            public void onFinished() {}
            @Override
            public void onWaiting() {}
            @Override
            public void onStarted() {}
            @Override
            public void onLoading(long total, long current) {}
        });
    }

    //查询收藏的店铺
    public void pullCollectionStore(){
        HashMap<String,String> map=new HashMap<>();
        map.put("ctype","favorite");
        map.put("jf","store|storeLogo");
        map.put("cond","{user:{id:"+ UserAuthUtil.getUserId()+"}}");
        http.doCommonPost(null, MainUrl.basePageQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                LogUtil.e("collection",resultStr.toString());
                if(resultStr!=null){
                    try {
                        JSONObject obj=new JSONObject(resultStr);
                        int count=obj.getInt("rowCount");
                        ownTvCollection.setText(count+"");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onError(Throwable ex) {}
            @Override
            public void onCancelled(Callback.CancelledException cex) {}
            @Override
            public void onFinished() {}
            @Override
            public void onWaiting() {}
            @Override
            public void onStarted() {}
            @Override
            public void onLoading(long total, long current) {}
        });
    }

}
