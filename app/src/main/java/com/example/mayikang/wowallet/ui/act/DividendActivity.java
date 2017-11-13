package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.AllOrderFromAdapter;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.IndentBean;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 分红界面
 */

@Route(path = "/main/act/DividendActivity")
public class DividendActivity extends BaseAppcompatActivity {

    @BindView(R.id.divident_back)
    RelativeLayout dividentBack;
    @BindView(R.id.divident_rv)
    RecyclerView dividentRv;
    @BindView(R.id.divident_qrefresh)
    MaterialRefreshLayout dividentQrefresh;
    @BindView(R.id.divident_layout)
    LinearLayout dividentLayout;
    @BindView(R.id.divident_No_layout)
    LinearLayout dividentNoLayout;
    @BindView(R.id.activity_dividend)
    LinearLayout activityDividend;
    @BindView(R.id.order_txt)
    TextView orderTxt;
    @BindView(R.id.order_all_ll)
    LinearLayout orderAllLl;
    @BindView(R.id.fans_txt)
    TextView fansTxt;
    @BindView(R.id.fans_ll)
    LinearLayout fansLl;
    @BindView(R.id.order_txtbum)
    TextView orderTxtbum;
    @BindView(R.id.fans_txtbum)
    TextView fansTxtbum;

    private HttpServiceImpl service;
    private AllOrderFromAdapter adapter;
    private List<IndentBean> data = new ArrayList<>();
    private int pageIndex = 1;
    private boolean flag = true;
    private int flags=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        dividentRv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AllOrderFromAdapter(this, data, 2);
        dividentRv.setAdapter(adapter);
        setListener();
    }

    //设置监听
    private void setListener() {
        dividentQrefresh.setLoadMore(true);
        dividentQrefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                flag = false;
                if(flags==2){
                    pullDeviendData();
                }else {
                    pullDeviendfans();
                }


            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {

                if(flags==2){
                    pullDeviendDataMore();
                }else {
                  pullDeviendfanMore();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initOrder();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_dividend;
    }

    @Override
    public void reloadData() {

    }



    @OnClick({R.id.divident_back, R.id.order_all_ll, R.id.fans_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.divident_back:
                finish();
                break;
            case R.id.order_all_ll:
                initOrder();
                break;
            case R.id.fans_ll:
                initFans();
                break;
        }
    }
    //选择下级提成
    private void initFans() {
        orderTxt.setTextColor(this.getResources().getColor(R.color.text_color));
        fansTxt.setTextColor(this.getResources().getColor(R.color.color_primary_blue));
        fansTxtbum.setBackgroundColor(this.getResources().getColor(R.color.color_primary_blue));
        orderTxtbum.setBackgroundColor(this.getResources().getColor(R.color.color_fade_gray));
        flags=1;

        pullDeviendfans();
    }

    //选择返利分红
    private void initOrder() {
        orderTxt.setTextColor(this.getResources().getColor(R.color.color_primary_blue));
        fansTxt.setTextColor(this.getResources().getColor(R.color.text_color));
        orderTxtbum.setBackgroundColor(this.getResources().getColor(R.color.color_primary_blue));
        fansTxtbum.setBackgroundColor(this.getResources().getColor(R.color.color_fade_gray));
        flags=2;

        pullDeviendData();
    }


    //加载分红数据
    private void pullDeviendData() {
        data.clear();
        pageIndex = 1;
        HashMap<String, String> body = new HashMap<>();
        body.put("pageIndex", pageIndex + "");
        service.doCommonPost(null, MainUrl.getdevident, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (!StringUtil.isBlank(result)) {
                    try {
                        Log.e("pullDeviendData", result.toString());
                        JSONObject obj = new JSONObject(result);
                        JSONArray resultList = obj.getJSONArray("resultList");
                        pageIndex++;
                        if (null != resultList && resultList.length() > 0) {
                            for (int i = 0; i < resultList.length(); i++) {
                                IndentBean bean = new IndentBean();
                                JSONObject transObj = resultList.getJSONObject(i);
                                bean.setPostAmount(transObj.getDouble("amount"));
                                bean.setStoreName(transObj.getString("desc_"));
                                bean.setInsertTime(transObj.getString("insertTime"));
                                bean.setType(2);
                                data.add(bean);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (data.size() > 0) {
                            dividentLayout.setVisibility(View.VISIBLE);
                            dividentNoLayout.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        } else {
                            dividentLayout.setVisibility(View.GONE);
                            dividentNoLayout.setVisibility(View.VISIBLE);
                        }
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
                dividentQrefresh.finishRefresh();
                dividentQrefresh.finishRefreshLoadMore();
                if (flag) {
                    dismissLoading();
                }
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                if (flag) {
                    showLoading(true, "加载中...");
                }
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });

    }

    //加载分红数据更多
    private void pullDeviendDataMore() {
        HashMap<String, String> body = new HashMap<>();
        body.put("userId", UserAuthUtil.getUserId() + "");
        body.put("pageIndex", pageIndex + "");
        service.doCommonPost(null, MainUrl.getdevident, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (!StringUtil.isBlank(result)) {
                    try {
                        pageIndex++;
                        JSONObject obj = new JSONObject(result);
                        JSONArray trans = obj.getJSONArray("trans");
                        if (null != trans && trans.length() > 0) {
                            for (int i = 0; i < trans.length(); i++) {
                                JSONObject transObj = trans.getJSONObject(i);
                                IndentBean bean = new IndentBean();
                                bean.setPostAmount(transObj.getDouble("amount"));
                                bean.setStoreName(transObj.getString("desc_"));
                                bean.setInsertTime(transObj.getString("insertTime"));
                                bean.setType(2);
                                data.add(bean);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (data.size() > 0) {
                            dividentLayout.setVisibility(View.VISIBLE);
                            dividentNoLayout.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        } else {
                            dividentLayout.setVisibility(View.GONE);
                            dividentNoLayout.setVisibility(View.VISIBLE);
                        }
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
                dividentQrefresh.finishRefresh();
                dividentQrefresh.finishRefreshLoadMore();
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



    //下级提成
    private void pullDeviendfans() {
        data.clear();
        pageIndex = 1;
        HashMap<String, String> body = new HashMap<>();
        body.put("pageIndex", pageIndex + "");
        body.put("ctype","rewradRecTotal");
        body.put("orderby","id desc");
        body.put("jf","orderform|user");
        body.put("cond","{user:{id:"+UserAuthUtil.getUserId()+"},type:1}");
        service.doCommonPost(null, MainUrl.basePageQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                pageIndex++;
                if (!StringUtil.isBlank(result)) {
                    try {
                        Log.e("pullDeviendfans", result.toString());
                        JSONObject object = new JSONObject(result);
                        JSONArray arry=object.getJSONArray("resultList");
                        if(null!=arry&&arry.length()>0){
                            for (int i = 0; i <arry.length() ; i++) {
                                IndentBean bean=new IndentBean();
                                JSONObject fanObj=arry.getJSONObject(i);
                                bean.setFinishTime(fanObj.getString("grantTime"));
                                bean.setPayValue(fanObj.getDouble("value"));
                                bean.setName(fanObj.getJSONObject("orderform").getJSONObject("user").getString("username"));
                                bean.setPostAmount(fanObj.getJSONObject("orderform").getDouble("payValue"));
                                bean.setType(1);
                                data.add(bean);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (data.size() > 0) {
                            dividentLayout.setVisibility(View.VISIBLE);
                            dividentNoLayout.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        } else {
                            dividentLayout.setVisibility(View.GONE);
                            dividentNoLayout.setVisibility(View.VISIBLE);
                        }
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
                dividentQrefresh.finishRefresh();
                dividentQrefresh.finishRefreshLoadMore();
                if (flag) {
                    dismissLoading();
                }
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                if (flag) {
                    showLoading(true, "加载中...");
                }
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });

    }



    //下级提成更多
    private void pullDeviendfanMore() {
        HashMap<String, String> body = new HashMap<>();
        body.put("pageIndex", pageIndex + "");
        body.put("ctype","rewradRecTotal");
        body.put("orderby","id desc");
        body.put("jf","orderform|user");
        body.put("cond","{user:{id:"+UserAuthUtil.getUserId()+"},type:1}");
        service.doCommonPost(null, MainUrl.basePageQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (!StringUtil.isBlank(result)) {
                    pageIndex++;
                    try {
                        Log.e("pullDeviendfans", result.toString());
                        JSONObject object = new JSONObject(result);
                        JSONArray arry=object.getJSONArray("resultList");
                        if(null!=arry&&arry.length()>0){
                            for (int i = 0; i <arry.length() ; i++) {
                                IndentBean bean=new IndentBean();
                                JSONObject fanObj=arry.getJSONObject(i);
                                bean.setFinishTime(fanObj.getString("grantTime"));
                                bean.setPayValue(fanObj.getDouble("value"));
                                bean.setPostAmount(fanObj.getJSONObject("orderform").getDouble("payValue"));
                                bean.setName(fanObj.getJSONObject("orderform").getJSONObject("user").getString("username"));
                                bean.setType(1);
                                data.add(bean);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (data.size() > 0) {
                            dividentLayout.setVisibility(View.VISIBLE);
                            dividentNoLayout.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        } else {
                            dividentLayout.setVisibility(View.GONE);
                            dividentNoLayout.setVisibility(View.VISIBLE);
                        }
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
                dividentQrefresh.finishRefresh();
                dividentQrefresh.finishRefreshLoadMore();

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
