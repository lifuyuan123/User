package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.AllOrderFromAdapter;
import com.example.mayikang.wowallet.adapter.HomeOrderAdapter;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.Data;
import com.example.mayikang.wowallet.modle.javabean.IndentBean;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.router_service.IHttpService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
@Route(path = "/main/act/AllOrderForm")
public class AllOrderForm extends BaseAppcompatActivity {

    @BindView(R.id.all_order_from_back)
    RelativeLayout allOrderFromBack;
    @BindView(R.id.all_order_from_rv)
    RecyclerView allOrderFromRv;
    @BindView(R.id.all_order_from_qrefresh)
    MaterialRefreshLayout allOrderFromQrefresh;
    @BindView(R.id.fans_data_layout)
    LinearLayout fansDataLayout;
    @BindView(R.id.all_order_from_No_layout)
    LinearLayout allOrderFromNoLayout;
    @BindView(R.id.activity_all_order_form)
    LinearLayout activityAllOrderForm;

    private List<IndentBean> data = new ArrayList<>();
    private IHttpService service;
    private int pageIndex=1;
    private HomeOrderAdapter adapter;
    private boolean flag=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = (IHttpService) ARouter.getInstance().build("/basemodule/service/http").navigation();
        adapter=new HomeOrderAdapter(this,data);
        allOrderFromRv.setLayoutManager(new LinearLayoutManager(this));
        allOrderFromRv.setAdapter(adapter);
        setListener();
    }

    private void setListener() {
        allOrderFromQrefresh.setLoadMore(true);
        allOrderFromQrefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                flag=false;
                PullAllOrderFromtData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                PullAllOrderFromtDataMore();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        PullAllOrderFromtData();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_all_order_form;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.all_order_from_back)
    public void onViewClicked() {
        finish();
    }

    //加载数据
    private void PullAllOrderFromtData() {
        data.clear();
        pageIndex = 1;
        HashMap<String, String> body = new HashMap<>();
        body.put("jf", "storeTbl|storeLogo");
        body.put("type", "1");
        body.put("pageIndex", String.valueOf(pageIndex));
        service.doCommonPost(null, MainUrl.indentUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("-------", result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        pageIndex++;
                        JSONObject obj = new JSONObject(result);
                        JSONArray array = obj.getJSONArray("resultList");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject indentObj = array.getJSONObject(i);
                            IndentBean bean = new IndentBean();
                            bean.setId(indentObj.getInt("id"));
                            bean.setFinishTime(indentObj.getString("insertTime"));
                            bean.setName(indentObj.getString("name"));
                            bean.setNum(indentObj.getInt("num"));
                            bean.setPaytime(indentObj.getString("paytime"));
                            bean.setPayValue(indentObj.getDouble("payValue"));
                            JSONObject storeObj = indentObj.getJSONObject("storeTbl");
                            bean.setStoreName(storeObj.getString("name"));
                            bean.setStoreId(storeObj.getInt("id"));
                            JSONObject storeLogoObj = storeObj.getJSONObject("storeLogo");
                            bean.setStoreLogoUrl(storeLogoObj.getString("url"));
                            data.add(bean);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("jsonException",e.toString());
                    } finally {
                        if(data.size()>0){
                            fansDataLayout.setVisibility(View.VISIBLE);
                            allOrderFromNoLayout.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        }else {
                            fansDataLayout.setVisibility(View.GONE);
                            allOrderFromNoLayout.setVisibility(View.VISIBLE);
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
                allOrderFromQrefresh.finishRefreshLoadMore();
                allOrderFromQrefresh.finishRefresh();
                if(flag){
                   dismissLoading();
                }
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                if(flag){
                    showLoading(true,"加载中...");
                }

            }

            @Override
            public void onLoading(long total, long current) {

            }
        });

    }



    //加载数据
    private void PullAllOrderFromtDataMore() {
        data.clear();
        HashMap<String, String> body = new HashMap<>();
        body.put("jf", "storeTbl|storeLogo");
        body.put("type","1");
        body.put("pageIndex", String.valueOf(pageIndex));
        service.doCommonPost(null, MainUrl.indentUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("-------", result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        pageIndex++;
                        JSONObject obj = new JSONObject(result);
                        JSONArray array = obj.getJSONArray("resultList");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject indentObj = array.getJSONObject(i);
                            IndentBean bean = new IndentBean();
                            bean.setId(indentObj.getInt("id"));
                            bean.setFinishTime(indentObj.getString("insertTime"));
                            bean.setName(indentObj.getString("name"));
                            bean.setNum(indentObj.getInt("num"));
                            bean.setPaytime(indentObj.getString("paytime"));
                            bean.setPayValue(indentObj.getDouble("payValue"));
                            JSONObject storeObj = indentObj.getJSONObject("storeTbl");
                            bean.setStoreName(storeObj.getString("name"));
                            bean.setStoreId(storeObj.getInt("id"));
                            JSONObject storeLogoObj = storeObj.getJSONObject("storeLogo");
                            bean.setStoreLogoUrl(storeLogoObj.getString("url"));
                            data.add(bean);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("jsonException",e.toString());
                    } finally {
                        if(data.size()>0){
                            fansDataLayout.setVisibility(View.VISIBLE);
                            allOrderFromNoLayout.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        }else {
                            fansDataLayout.setVisibility(View.GONE);
                            allOrderFromNoLayout.setVisibility(View.VISIBLE);
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
                allOrderFromQrefresh.finishRefreshLoadMore();
                allOrderFromQrefresh.finishRefresh();
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
