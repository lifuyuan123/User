package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.ChooseAgAdapter;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.AgentBean;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.ui.widget.rv.FullyGridLayoutManager;
import com.sctjsj.basemodule.base.util.ListViewUtil;
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

/*
 * 代理商选择
 */

@Route(path = "/main/act/ChooseAgentActivity")
public class ChooseAgentActivity extends BaseAppcompatActivity {

    @BindView(R.id.choose_agent_back)
    RelativeLayout chooseAgentBack;
    @BindView(R.id.activity_choose_agent)
    LinearLayout activityChooseAgent;
    @BindView(R.id.Choose_refresh)
    MaterialRefreshLayout RefreshLayout;
    @BindView(R.id.choose_layout)
    LinearLayout choose_layout;
    @BindView(R.id.Choose_noData_layout)
    LinearLayout Choose_noData_layout;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    private HttpServiceImpl service;
    private List<AgentBean> group = new ArrayList<>();
    private ChooseAgAdapter agAdapter;
    private int pageIndex = 1;
    private FullyGridLayoutManager manager;
    private List<AgentBean> chooseItem=null;
    private boolean flag=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();

        agAdapter=new ChooseAgAdapter(group,this);
        manager=new FullyGridLayoutManager(this,2, GridLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(manager);
        recycler.setAdapter(agAdapter);
        getListData();
        setListener();
    }


    private void setListener() {
        RefreshLayout.setLoadMore(true);
        RefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                flag=false;
                getListData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                getListDataMore();
            }
        });
    }

    @Override
    public int initLayout() {
        return R.layout.activity_choose_agent;
    }

    @Override
    public void reloadData() {

    }


    //获取套餐
    private void getListData() {
        chooseItem= agAdapter.getAdapterData();
        group.clear();
        pageIndex = 1;
        HashMap<String, String> body = new HashMap<>();
        body.put("ctype", "agentPackage");
        body.put("pageIndex", pageIndex + "");
        body.put("cond", "{isDelete:1}");
        service.doCommonPost(null, MainUrl.basePageQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("getListData", result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray resultList = object.getJSONArray("resultList");
                        pageIndex++;
                        if (null != resultList && resultList.length() > 0) {
                            for (int i = 0; i < resultList.length(); i++) {
                                JSONObject beanObj = resultList.getJSONObject(i);
                                AgentBean bean = new AgentBean();
                                bean.setCheck(false);
                                bean.setName(beanObj.getString("name"));
                                int id=beanObj.getInt("id");
                                bean.setId(beanObj.getInt("id"));
                                bean.setInsertTime(beanObj.getString("insertTime"));
                                bean.setTotNum(beanObj.getInt("totNum"));
                                bean.setValue(beanObj.getDouble("value"));
                                bean.setAllValue(beanObj.getDouble("totAmount"));
                                if(chooseItem!=null&&chooseItem.size()>0){
                                    AgentBean choose=chooseItem.get(0);
                                    if(choose.getId()==id){
                                        bean.setCheck(true);
                                    }
                                }
                                group.add(bean);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (group.size() > 0) {
                            choose_layout.setVisibility(View.VISIBLE);
                            Choose_noData_layout.setVisibility(View.GONE);
                            agAdapter.notifyDataSetChanged();
                        } else {
                            choose_layout.setVisibility(View.GONE);
                            Choose_noData_layout.setVisibility(View.VISIBLE);
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
                if (flag) {
                    dismissLoading();
                }
                RefreshLayout.finishRefreshLoadMore();
                RefreshLayout.finishRefresh();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                if(flag){
                    showLoading(true, "加载中...");
                }
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });

    }


    private void getListDataMore() {
        HashMap<String, String> body = new HashMap<>();
        body.put("ctype", "agentPackage");
        body.put("pageIndex", pageIndex + "");
        service.doCommonPost(null, MainUrl.basePageQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("getListData", result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray resultList = object.getJSONArray("resultList");
                        pageIndex++;
                        if (null != resultList && resultList.length() > 0) {
                            for (int i = 0; i < resultList.length(); i++) {
                                JSONObject beanObj = resultList.getJSONObject(i);
                                AgentBean bean = new AgentBean();
                                bean.setCheck(false);
                                bean.setName(beanObj.getString("name"));
                                bean.setId(beanObj.getInt("id"));
                                bean.setInsertTime(beanObj.getString("insertTime"));
                                bean.setTotNum(beanObj.getInt("totNum"));
                                bean.setValue(beanObj.getDouble("value"));
                                group.add(bean);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (group.size() > 0) {
                            choose_layout.setVisibility(View.VISIBLE);
                            Choose_noData_layout.setVisibility(View.GONE);
                            agAdapter.notifyDataSetChanged();
                        } else {
                            choose_layout.setVisibility(View.GONE);
                            Choose_noData_layout.setVisibility(View.VISIBLE);
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

                RefreshLayout.finishRefreshLoadMore();
                RefreshLayout.finishRefresh();
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

    //提交
    private void commentData() {
        int flag = isOrNotComment();
        if (flag != -1) {
            HashMap<String, String> body = new HashMap<>();
            body.put("agId", flag + "");
            service.doCommonPost(null, MainUrl.CommentAgentResult, body, new XProgressCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.e("commentData", result.toString());
                    if (!StringUtil.isBlank(result)) {
                        try {
                            JSONObject obj = new JSONObject(result);
                            if (obj.getBoolean("result")) {
                                Toast.makeText(ChooseAgentActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ChooseAgentActivity.this, obj.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onError(Throwable ex) {
                    Log.e("commentData_onError", ex.toString());
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
                    showLoading(true, "提交中....");
                }

                @Override
                public void onLoading(long total, long current) {

                }
            });
        }


    }

    @OnClick({R.id.choose_agent_back, R.id.choose_agent_comment, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.choose_agent_back:
                finish();
                break;
            case R.id.choose_agent_comment:
                commentData();
                break;
            //申请记录
            case R.id.tv_right:
                ARouter.getInstance().build("/main/act/user/AgentApplyHistoryActivity").navigation();
                break;
        }
    }

    //判断是否可以提交
    private int isOrNotComment() {
        if (UserAuthUtil.isUserLogin()) {
            return agAdapter.getChooseBeanId();
        } else {
            //跳转到登陆页
            ARouter.getInstance().build("/main/act/login").navigation();
        }
        return -1;
    }

}
