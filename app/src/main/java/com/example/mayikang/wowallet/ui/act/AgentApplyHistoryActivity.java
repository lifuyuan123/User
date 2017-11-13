package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.AgentApplyHistoryAdapter;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.AgentBean;
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
@Route(path = "/main/act/user/AgentApplyHistoryActivity")
public class AgentApplyHistoryActivity extends BaseAppcompatActivity {

    @BindView(R.id.agent_history_back)
    LinearLayout agentHistoryBack;
    @BindView(R.id.agent_history_rv)
    RecyclerView agentHistoryRv;
    @BindView(R.id.agent_history_refresh)
    MaterialRefreshLayout agentHistoryRefresh;
    @BindView(R.id.agent_history_layout)
    LinearLayout agentHistoryLayout;
    @BindView(R.id.agent_history_No_layout)
    LinearLayout agentHistoryNoLayout;

    private HttpServiceImpl service;
    private int pageIndex=1;
    private AgentApplyHistoryAdapter adapter;
    private List<AgentBean> data=new ArrayList<>();
    private boolean flag=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        initView();
        setListener();
    }
    //初始化view
    private void initView() {
        adapter=new AgentApplyHistoryAdapter(data,this);
        agentHistoryRv.setLayoutManager(new LinearLayoutManager(this));
        agentHistoryRv.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAgentHistoryData();
    }


    //设置监听
    private void setListener() {
        agentHistoryRefresh.setLoadMore(true);
        agentHistoryRefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                flag=false;
                getAgentHistoryData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                getAgentHistoryDataMore();
            }
        });
    }

    @Override
    public int initLayout() {
        return R.layout.activity_agent_apply_history;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.agent_history_back)
    public void onViewClicked() {
        finish();
    }

    //获取历史记录数据
    private void getAgentHistoryData() {
        pageIndex=1;
        data.clear();
        HashMap<String,String> body=new HashMap<>();
        body.put("ctype","agentApp");
        body.put("cond","{appltUser:{id:"+ UserAuthUtil.getUserId()+"}}");
        body.put("pageIndex",pageIndex+"");
        body.put("jf","applyAp");
        service.doCommonPost(null, MainUrl.basePageQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("getAgentHistoryData",result.toString());
                if(!StringUtil.isBlank(result)){
                    try {
                        pageIndex++;
                        JSONObject object=new JSONObject(result);
                        if(object.getBoolean("result")){
                            JSONArray arry=object.getJSONArray("resultList");
                            if(null!=arry&&arry.length()>0){
                                for (int i = 0; i <arry.length() ; i++) {
                                    JSONObject agent=arry.getJSONObject(i);
                                    AgentBean bean=new AgentBean();
                                    bean.setId(agent.getInt("id"));
                                    JSONObject beantc=agent.getJSONObject("applyAp");
                                    bean.setName("代理"+beantc.getString("name"));
                                    int status=agent.getInt("status");
                                    //1 通过审核，2审核中 3,审核不通过4,套餐用完
                                    bean.setAgetState(status);
                                    bean.setInsertTime(agent.getString("insertTime"));
                                    data.add(bean);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }finally {
                        if(data.size()>0){
                            agentHistoryLayout.setVisibility(View.VISIBLE);
                            agentHistoryNoLayout.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        }else {
                            agentHistoryLayout.setVisibility(View.GONE);
                            agentHistoryNoLayout.setVisibility(View.VISIBLE);
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
                agentHistoryRefresh.finishRefreshLoadMore();
                agentHistoryRefresh.finishRefresh();
                if (flag){
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
    //获取更多的数据
    private void getAgentHistoryDataMore() {
        HashMap<String,String> body=new HashMap<>();
        body.put("ctype","agentApp");
        body.put("cond","{appltUser={id:"+ UserAuthUtil.getUserId()+"}}");
        body.put("pageIndex",pageIndex+"");
        body.put("jf","applyAp");
        service.doCommonPost(null, MainUrl.basePageQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("getAgentHistoryData",result.toString());
                if(!StringUtil.isBlank(result)){
                    try {
                        pageIndex++;
                        JSONObject object=new JSONObject(result);
                        if(object.getBoolean("result")){
                            JSONArray arry=object.getJSONArray("resultList");
                            if(null!=arry&&arry.length()>0){
                                for (int i = 0; i <arry.length() ; i++) {
                                    JSONObject agent=arry.getJSONObject(i);
                                    AgentBean bean=new AgentBean();
                                    bean.setId(agent.getInt("id"));
                                    JSONObject beantc=agent.getJSONObject("applyAp");
                                    bean.setName("代理"+beantc.getString("name"));
                                    int status=agent.getInt("status");
                                    //1 通过审核，2审核中 3,审核不通过4,套餐用完
                                    bean.setAgetState(status);
                                    bean.setInsertTime(agent.getString("insertTime"));
                                    data.add(bean);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }finally {
                        if(data.size()>0){
                            agentHistoryLayout.setVisibility(View.VISIBLE);
                            agentHistoryNoLayout.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        }else {
                            agentHistoryLayout.setVisibility(View.GONE);
                            agentHistoryNoLayout.setVisibility(View.VISIBLE);
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
                agentHistoryRefresh.finishRefreshLoadMore();
                agentHistoryRefresh.finishRefresh();

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
