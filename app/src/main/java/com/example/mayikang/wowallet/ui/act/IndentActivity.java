package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.IndentAllAdapter;
import com.example.mayikang.wowallet.adapter.IndentRabateAdapter;
import com.example.mayikang.wowallet.adapter.IndentWaitComAdapter;
import com.example.mayikang.wowallet.event.PageSwitchEvent;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.IndentBean;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.router_service.IHttpService;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/***
 * 订单界面
 */
@Route(path = "/user/main/act/indent")
public class IndentActivity extends BaseAppcompatActivity {

    @BindView(R.id.indent_all_txt)
    TextView indentAllTxt;
    @BindView(R.id.indent_all_Img)
    ImageView indentAllImg;
    @BindView(R.id.indent_all_ll)
    LinearLayout indentAllLl;
    @BindView(R.id.indent_rebate_txt)
    TextView indentRebateTxt;
    @BindView(R.id.indent_rebate_Img)
    ImageView indentRebateImg;
    @BindView(R.id.indent_rebate_ll)
    LinearLayout indentRebateLl;
    @BindView(R.id.indent_wait_txt)
    TextView indentWaitTxt;
    @BindView(R.id.indent_wait_Img)
    ImageView indentWaitImg;
    @BindView(R.id.indent_wait_ll)
    LinearLayout indentWaitLl;
    @BindView(R.id.indent_RecycleView)
    RecyclerView indentRecycleView;
    @BindView(R.id.indent_layout)
    LinearLayout indentLayout;
    @BindView(R.id.indent_all_txtbum)
    TextView indentAllTxtbum;
    @BindView(R.id.indent_rebate_txtbum)
    TextView indentRebateTxtbum;
    @BindView(R.id.indent_wait_txtbum)
    TextView indentWaitTxtbum;
    @BindView(R.id.indent_refresh)
    MaterialRefreshLayout indentRefresh;
    @BindView(R.id.indent_gotoStore_btn)
    Button indentGotoStoreBtn;
    @BindView(R.id.indent_noData_layout)
    LinearLayout indentNoDataLayout;
    @BindView(R.id.indent_reLoading_btn)
    Button indentReLoadingBtn;
    @BindView(R.id.indent_error_layout)
    LinearLayout indentErrorLayout;
    @BindView(R.id.activity_indent)
    LinearLayout activityIndent;

    private IndentAllAdapter addAdapter;
    private IndentRabateAdapter raAdapter;
    private IndentWaitComAdapter waAdapter;
    private List<IndentBean> data = new ArrayList<>();
    private int pageIndex = 1;
    private int flag = 1;
    private IHttpService service;
    private int type = 1;
    private boolean loadflag=false;//用来标示是否为刷新的加载  true为是


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addAdapter = new IndentAllAdapter(this, data);
        raAdapter = new IndentRabateAdapter(this, data);
        waAdapter = new IndentWaitComAdapter(this, data);
        service = (IHttpService) ARouter.getInstance().build("/basemodule/service/http").navigation();
        setListener();
    }

    private void setListener() {
        indentRefresh.setLoadMore(true);
        indentRefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                PullIndentData(type);
                loadflag=true;
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                PullIndentDataMore(type);
                loadflag=true;
            }
        });
        //全部订单的item的点击事件
        addAdapter.setListener(new IndentAllAdapter.IndentAllAdapterCallBack() {

            @Override
            public void itemOnClick(int position) {

                    ARouter.getInstance().build("/main/act/IndentMessageActivity").withInt("id",data.get(position).getId()).navigation();
            }

            @Override
            public void comment(int position,int id) {
                ARouter.getInstance().build("/user/main/act/EvaluateActivity").withInt("key",id).withString("storeId",data.get(position).getStoreName()).navigation();
            }
        });
        //返利订单的item的点击事件
        raAdapter.setListener(new IndentRabateAdapter.IndentRabateAdapterCallBack() {

            @Override
            public void itemOnClick(int position) {
                ARouter.getInstance().build("/main/act/IndentMessageActivity").withInt("id",data.get(position).getId()).navigation();
            }

            @Override
            public void commentOnClick(int position, int id) {
                ARouter.getInstance().build("/main/act/EvaluateActivity").withInt("key",id).withString("storeId",data.get(position).getStoreName()).navigation();
            }

        });

        waAdapter.setListener(new IndentWaitComAdapter.IndentWaitAdapterCallBack() {
            @Override
            public void itemOnClick(int position) {
                ARouter.getInstance().build("/main/act/IndentMessageActivity").withInt("id",data.get(position).getId()).navigation();
            }

            @Override
            public void comment(int position, int id) {
                ARouter.getInstance().build("/main/act/EvaluateActivity").withInt("key",id).withString("storeId",data.get(position).getStoreName()).navigation();
            }
        });

    }

    @Override
    public int initLayout() {
        return R.layout.activity_indent;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.indent_all_ll, R.id.indent_rebate_ll, R.id.indent_wait_ll, R.id.indent_gotoStore_btn,R.id.rela_back, R.id.indent_reLoading_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.indent_all_ll:
                initAll();
                loadflag=false;
                break;
            case R.id.indent_rebate_ll:
                initRebate();
                loadflag=false;
                break;
            case R.id.indent_wait_ll:
                initWait();
                loadflag=false;
                break;
            case R.id.indent_gotoStore_btn:
                EventBus.getDefault().post(new PageSwitchEvent("STORE"));
                finish();
                break;
            case R.id.indent_reLoading_btn:
                indentErrorLayout.setVisibility(View.GONE);
                activityIndent.setVisibility(View.VISIBLE);
                switch (flag) {
                    case 1:
                        initAll();
                        loadflag=false;
                        break;
                    case 2:
                        initRebate();
                        loadflag=false;
                        break;
                    case 3:
                        initWait();
                        loadflag=false;
                        break;
                }
                break;
            case R.id.rela_back:
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        switch (flag) {
            case 1:
                initAll();
                loadflag=false;
                break;
            case 2:
                initRebate();
                loadflag=false;
                break;
            case 3:
                initWait();
                loadflag=false;
                break;
        }
    }

    //点击带评论
    private void initWait() {
        indentWaitTxt.setTextColor(this.getResources().getColor(R.color.color_primary_blue));
        indentWaitImg.setVisibility(View.VISIBLE);
        indentWaitTxtbum.setBackgroundColor(this.getResources().getColor(R.color.color_primary_blue));

        indentRebateTxt.setTextColor(this.getResources().getColor(R.color.color_primary_black));
        indentRebateImg.setVisibility(View.INVISIBLE);
        indentRebateTxtbum.setBackgroundColor(this.getResources().getColor(R.color.color_fade_gray));

        indentAllTxt.setTextColor(this.getResources().getColor(R.color.color_primary_black));
        indentAllImg.setVisibility(View.INVISIBLE);
        indentAllTxtbum.setBackgroundColor(this.getResources().getColor(R.color.color_fade_gray));

        this.type = 3;
        this.flag = 3;
        PullIndentData(type);
        indentRecycleView.setLayoutManager(new LinearLayoutManager(IndentActivity.this, LinearLayoutManager.VERTICAL, false));
        indentRecycleView.setAdapter(waAdapter);

    }

    //点击返利
    private void initRebate() {
        indentAllTxt.setTextColor(this.getResources().getColor(R.color.color_primary_black));
        indentAllImg.setVisibility(View.INVISIBLE);
        indentAllTxtbum.setBackgroundColor(this.getResources().getColor(R.color.color_fade_gray));

        indentRebateTxt.setTextColor(this.getResources().getColor(R.color.color_primary_blue));
        indentRebateImg.setVisibility(View.VISIBLE);
        indentRebateTxtbum.setBackgroundColor(this.getResources().getColor(R.color.color_primary_blue));

        indentWaitTxt.setTextColor(this.getResources().getColor(R.color.color_primary_black));
        indentWaitImg.setVisibility(View.INVISIBLE);
        indentWaitTxtbum.setBackgroundColor(this.getResources().getColor(R.color.color_fade_gray));
        indentRecycleView.setLayoutManager(new LinearLayoutManager(IndentActivity.this, LinearLayoutManager.VERTICAL, false));
        this.type = 2;
        this.flag = 2;
        PullIndentData(type);
        indentRecycleView.setAdapter(raAdapter);

    }

    //点击全部
    private void initAll() {
        indentAllTxt.setTextColor(this.getResources().getColor(R.color.color_primary_blue));
        indentAllImg.setVisibility(View.VISIBLE);
        indentAllTxtbum.setBackgroundColor(this.getResources().getColor(R.color.color_primary_blue));

        indentRebateTxt.setTextColor(this.getResources().getColor(R.color.color_primary_black));
        indentRebateImg.setVisibility(View.INVISIBLE);
        indentRebateTxtbum.setBackgroundColor(this.getResources().getColor(R.color.color_fade_gray));

        indentWaitTxt.setTextColor(this.getResources().getColor(R.color.color_primary_black));
        indentWaitImg.setVisibility(View.INVISIBLE);
        indentWaitTxtbum.setBackgroundColor(this.getResources().getColor(R.color.color_fade_gray));
        this.type = 1;
        this.flag = 1;
        PullIndentData(type);
        indentRecycleView.setLayoutManager(new LinearLayoutManager(IndentActivity.this, LinearLayoutManager.VERTICAL, false));
        indentRecycleView.setAdapter(addAdapter);


    }

    //加载数据
    private void PullIndentData(final int type) {
        data.clear();
        pageIndex = 1;
        HashMap<String, String> body = new HashMap<>();
        body.put("jf", "storeTbl|storeLogo|orderSettels");
        body.put("type", String.valueOf(type));
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
                            bean.setIsEva(indentObj.getInt("isEva"));
                            bean.setState(indentObj.getInt("state"));
                            JSONArray orderSettels=indentObj.getJSONArray("orderSettels");
                            double amout=0;
                            if(null!=orderSettels&&orderSettels.length()>0){
                                JSONObject orderObj=orderSettels.getJSONObject(0);
                                double payValue=orderObj.getDouble("payValue");
                                double remainRebate=orderObj.getDouble("remainRebate");
                                amout=payValue-remainRebate;
                            }
                            bean.setPostAmount(amout);
                            bean.setBuyerRemark(indentObj.getString("buyerRemark"));
                            int type=indentObj.getInt("type");
                            bean.setType(type);
                                if(type==1){
                                    JSONObject storeObj = indentObj.getJSONObject("storeTbl");
                                    bean.setStoreName(storeObj.getString("name"));
                                    bean.setStoreId(storeObj.getInt("id"));
                                    JSONObject storeLogoObj = storeObj.getJSONObject("storeLogo");
                                    bean.setStoreLogoUrl(storeLogoObj.getString("url"));
                                }
                            int rebateFlag=indentObj.getInt("rebateFlag");
                            bean.setRebateFlag(rebateFlag);
                            /*if(rebateFlag==1||rebateFlag==2){
                                data.add(bean);
                            }*/
                            data.add(bean);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("jsonException",e.toString());
                    } finally {
                        if(data.size()>0){
                            indentErrorLayout.setVisibility(View.GONE);
                            indentNoDataLayout.setVisibility(View.GONE);
                            indentLayout.setVisibility(View.VISIBLE);
                        }else {
                            indentErrorLayout.setVisibility(View.GONE);
                            indentNoDataLayout.setVisibility(View.VISIBLE);
                            indentLayout.setVisibility(View.GONE);
                        }

                        switch (flag) {
                            case 1:
                                addAdapter.notifyDataSetChanged();
                                break;
                            case 2:
                                raAdapter.notifyDataSetChanged();
                                break;
                            case 3:
                                waAdapter.notifyDataSetChanged();
                                break;
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {
                if(activityIndent!=null){
                    activityIndent.setVisibility(View.GONE);
                }
                indentNoDataLayout.setVisibility(View.GONE);
                indentErrorLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                indentRefresh.finishRefresh();
                indentRefresh.finishRefreshLoadMore();
               if(!loadflag){
                   dismissLoading();
               }
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
              if(!loadflag){
                  showLoading(false,"加载中...");
              }
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });

    }


    //加载更多的数据
    private void PullIndentDataMore(final int type) {
        HashMap<String, String> body = new HashMap<>();
        body.put("jf", "storeTbl|storeLogo|orderSettels");
        body.put("orderby","id desc");
        body.put("type", String.valueOf(type));
        body.put("pageIndex", String.valueOf(pageIndex));
        service.doCommonPost(null, MainUrl.indentUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject obj = new JSONObject(result);
                        pageIndex++;
                        JSONArray array = obj.getJSONArray("resultList");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject indentObj = array.getJSONObject(i);
                            IndentBean bean = new IndentBean();
                            bean.setId(indentObj.getInt("id"));
                            bean.setFinishTime(indentObj.getString("insertTime"));
                            bean.setName(indentObj.getString("name"));
                            bean.setNum(indentObj.getInt("num"));
                            bean.setPaytime(indentObj.getString("paytime"));
                            JSONArray orderSettels=indentObj.getJSONArray("orderSettels");
                            double amout=0;
                            if(null!=orderSettels&&orderSettels.length()>0){
                                JSONObject orderObj=orderSettels.getJSONObject(0);
                                double payValue=orderObj.getDouble("payValue");
                                double remainRebate=orderObj.getDouble("remainRebate");
                                amout=payValue-remainRebate;
                            }
                            bean.setPostAmount(amout);
                            bean.setPayValue(indentObj.getDouble("payValue"));
                            bean.setIsEva(indentObj.getInt("isEva"));
                            bean.setState(indentObj.getInt("state"));
                            bean.setBuyerRemark(indentObj.getString("buyerRemark"));
                            int type=indentObj.getInt("type");
                            bean.setType(type);
                            if(type==1){
                                JSONObject storeObj = indentObj.getJSONObject("storeTbl");
                                bean.setStoreName(storeObj.getString("name"));
                                bean.setStoreId(storeObj.getInt("id"));
                                JSONObject storeLogoObj = storeObj.getJSONObject("storeLogo");
                                bean.setStoreLogoUrl(storeLogoObj.getString("url"));
                            }
                            int rebateFlag=indentObj.getInt("rebateFlag");
                            bean.setRebateFlag(rebateFlag);
                            /*if(rebateFlag==1||rebateFlag==2){
                                data.add(bean);
                            }*/
                            data.add(bean);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if(data.size()>0){
                            indentErrorLayout.setVisibility(View.GONE);
                            indentNoDataLayout.setVisibility(View.GONE);
                            indentLayout.setVisibility(View.VISIBLE);
                        }else {
                            indentErrorLayout.setVisibility(View.GONE);
                            indentNoDataLayout.setVisibility(View.VISIBLE);
                            indentLayout.setVisibility(View.GONE);
                        }

                        switch (flag) {
                            case 1:
                                addAdapter.notifyDataSetChanged();
                                break;
                            case 2:
                                raAdapter.notifyDataSetChanged();
                                break;
                            case 3:
                                waAdapter.notifyDataSetChanged();
                                break;
                        }
                    }
                }
            }


            @Override
            public void onError(Throwable ex) {
                activityIndent.setVisibility(View.GONE);
                indentNoDataLayout.setVisibility(View.GONE);
                indentErrorLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                indentRefresh.finishRefresh();
                indentRefresh.finishRefreshLoadMore();
                if(!loadflag){
                    dismissLoading();
                }
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                if(!loadflag){
                    showLoading(false,"加载中...");
                }
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });


    }
}
