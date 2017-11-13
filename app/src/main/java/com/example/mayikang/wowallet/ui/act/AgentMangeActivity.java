package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.AgentMangeAdapter;
import com.example.mayikang.wowallet.adapter.MangeAgentAdapter;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.AgentBean;
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

@Route(path = "/main/user/act/AgentMangeActivity")
public class AgentMangeActivity extends BaseAppcompatActivity {

    @BindView(R.id.agent_mange_back)
    RelativeLayout agentMangeBack;
    @BindView(R.id.activity_agent_mange)
    LinearLayout activityAgentMange;
    @BindView(R.id.agent_message_refresh)
    MaterialRefreshLayout refreshlayout;
    @BindView(R.id.agent_mange_addStore)
    RelativeLayout agent_mange_addStore;
    @BindView(R.id.agent_mange_rv)
    RecyclerView agentMangeRv;

    private HttpServiceImpl service;
    private List<AgentBean> data = new ArrayList();

    private int pageIndex = 1;
    private int surplusNum = -1;

    private MangeAgentAdapter TypeA, TypeB;
    private DelegateAdapter adapter;

    private ArrayList<DelegateAdapter.Adapter> adapterData = new ArrayList<>();

    private ArrayList<HashMap<String, Object>> TypeAdata = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> TypeBdata = new ArrayList<>();

    private HashMap<String, Object> dataAmap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        initView();
        setListener();
    }

    //初始化布局
    private void initView() {
        //1.设置 LayoutManager
        VirtualLayoutManager layoutManager = new VirtualLayoutManager(this);
        agentMangeRv.setLayoutManager(layoutManager);

        //2.设置组件复用回收池
        RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
        agentMangeRv.setRecycledViewPool(recycledViewPool);

        initTypeA();
        initTYpeB();

        adapter=new DelegateAdapter(layoutManager,false);
        adapter.setAdapters(adapterData);
        agentMangeRv.setAdapter(adapter);




    }

    //初始化B的adapter
    private void initTYpeB() {
        LinearLayoutHelper bhelper = new LinearLayoutHelper();
        TypeB = new MangeAgentAdapter(this, TypeBdata, bhelper, 2);
        adapterData.add(TypeB);

    }

    //初始化A的adapter
    private void initTypeA() {
        SingleLayoutHelper ahelper = new SingleLayoutHelper();
        ahelper.setItemCount(1);
        TypeAdata.add(dataAmap);
        TypeA = new MangeAgentAdapter(this, TypeAdata, ahelper, 1);
        adapterData.add(TypeA);

    }

    private void setListener() {
        refreshlayout.setLoadMore(true);
        refreshlayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                getAgent();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                getAgentMore();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        getAgent();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_agent_mange;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.agent_mange_back, R.id.agent_mange_addStore})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.agent_mange_back:
                finish();
                break;
            case R.id.agent_mange_addStore:
                if (surplusNum > 0) {
                    ARouter.getInstance().build("/main/act/AddAgentStoreActivity").navigation();
                } else {
                    Toast.makeText(this, "套餐剩余量不足，请购买！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private void getAgent() {
        TypeBdata.clear();
        pageIndex = 1;
        HashMap<String, String> body = new HashMap<>();
        body.put("jf", "ap|store");
        body.put("pageIndex", pageIndex + "");
        service.doCommonPost(null, MainUrl.GetAgintlists, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (!StringUtil.isBlank(result)) {
                    Log.e("getAgent", result.toString());
                    if (!StringUtil.isBlank(result)) {
                        try {
                            pageIndex++;
                            JSONObject object = new JSONObject(result);
                            if (object.has("uapList")) {
                                JSONArray uapList = object.getJSONArray("uapList");
                                AgentBean agentBean = null;
                                int isOrNotData = object.getJSONArray("resultList").length();
                                if (null != uapList && uapList.length() > 0) {
                                    for (int i = 0; i < uapList.length(); i++) {
                                        JSONObject uapObj = uapList.getJSONObject(i);
                                        JSONObject ap = uapObj.getJSONObject("ap");
                                        agentBean = new AgentBean();
                                        int totNum = ap.getInt("totNum");
                                        surplusNum = uapObj.getInt("surplusNum");
                                        agentBean.setName(ap.getString("name"));
                                        agentBean.setAgetState(surplusNum);
                                        agentBean.setSurplusNum((totNum - surplusNum));
                                        agentBean.setTotAmount(ap.getDouble("totAmount"));
                                        agentBean.setId(isOrNotData);
                                        dataAmap.put("data", agentBean);
                                    }
                                } else {
                                    agentBean = new AgentBean();
                                    agentBean.setName("暂无");
                                    agentBean.setTotAmount(0);
                                    agentBean.setSurplusNum(0);
                                    agentBean.setTotAmount(0);
                                    agentBean.setId(isOrNotData);
                                    dataAmap.put("data", agentBean);
                                }
                                TypeA.notifyDataSetChanged();

                                JSONArray resultList = object.getJSONArray("resultList");
                                if (null != resultList && resultList.length() > 0) {
                                    for (int i = 0; i < resultList.length(); i++) {
                                        JSONObject objbean = resultList.getJSONObject(i);
                                        AgentBean bean = new AgentBean();
                                        bean.setName(objbean.getString("username"));
                                        String store = objbean.getString("store");
                                        if (!store.equals("null")) {
                                            JSONObject ojStore = objbean.getJSONObject("store");
                                            bean.setInsertTime(ojStore.getString("insertTime"));
                                            bean.setId(ojStore.getInt("id"));
                                            int state = ojStore.getInt("storeStatus");
                                            if (state == 1) {
                                                bean.setStoreState("待审核");
                                            } else if (state == 2) {
                                                bean.setStoreState("已审核");
                                            } else if (state == 3) {
                                                bean.setStoreState("已关闭");
                                            }
                                        }
                                        data.add(bean);
                                        HashMap<String, Object> TypeBmap = new HashMap<>();
                                        TypeBmap.put("data", bean);
                                        TypeBdata.add(TypeBmap);
                                    }
                                    TypeB.notifyDataSetChanged();
                                }

                            } else {
                                //没有套餐，给个默认得布局
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("getAgent",e.toString());
                        } finally {


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
                refreshlayout.finishRefresh();
                refreshlayout.finishRefreshLoadMore();
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


    private void getAgentMore() {
        HashMap<String, String> body = new HashMap<>();
        body.put("jf", "ap|store");
        body.put("pageIndex", pageIndex + "");
        service.doCommonPost(null, MainUrl.GetAgintlists, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (!StringUtil.isBlank(result)) {
                    Log.e("getAgent", result.toString());
                    if (!StringUtil.isBlank(result)) {
                        try {
                            pageIndex++;
                            JSONObject object = new JSONObject(result);
                                JSONArray resultList = object.getJSONArray("resultList");
                                if (null != resultList && resultList.length() > 0) {
                                    for (int i = 0; i < resultList.length(); i++) {
                                        JSONObject objbean = resultList.getJSONObject(i);
                                        AgentBean bean = new AgentBean();
                                        bean.setName(objbean.getString("username"));
                                        String store = objbean.getString("store");
                                        if (!store.equals("null")) {
                                            JSONObject ojStore = objbean.getJSONObject("store");
                                            bean.setInsertTime(ojStore.getString("insertTime"));
                                            bean.setId(ojStore.getInt("id"));
                                            int state = ojStore.getInt("storeStatus");
                                            if (state == 1) {
                                                bean.setStoreState("待审核");
                                            } else if (state == 2) {
                                                bean.setStoreState("已审核");
                                            } else if (state == 3) {
                                                bean.setStoreState("已关闭");
                                            }
                                        }
                                        data.add(bean);
                                        HashMap<String, Object> TypeBmap = new HashMap<>();
                                        TypeBmap.put("data", bean);
                                        TypeBdata.add(TypeBmap);
                                    }
                                    TypeB.notifyDataSetChanged();
                                }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("getAgent",e.toString());
                        } finally {


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
                refreshlayout.finishRefresh();
                refreshlayout.finishRefreshLoadMore();
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
