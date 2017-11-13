package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.ProjectListAdapter;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.ProjectBean;
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
 * 店内项目列表页
 */
@Route(path = "/main/act/project_list")
public class ProjectListActivity extends BaseAppcompatActivity {

    @BindView(R.id.act_project_list_rv)
    RecyclerView mRV;
    @BindView(R.id.refresh)
    MaterialRefreshLayout refresh;

    private List<ProjectBean> data = new ArrayList<>();
    @Autowired(name = "id")
    public int id = -1;
    private HttpServiceImpl http;
    private int pageIndex = 1;
    private ProjectListAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initRV();
        http = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();

        pullStoreProject(id);
        refresh.setLoadMore(true);
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                pullStoreProject(id);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                pullMoreStoreProject(id);
            }
        });


    }

    //初始化view
    private void initView() {

    }

    private void initRV() {

        adapter = new ProjectListAdapter(this, data);
        mRV.setAdapter(adapter);
        mRV.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public int initLayout() {
        return R.layout.activity_project_list;
    }

    @Override
    public void reloadData() {

    }


    //查询店铺项目
    public void pullStoreProject(final int id) {
        pageIndex = 1;
        data.clear();
        HashMap<String, String> map = new HashMap<>();
        map.put("ctype", "goods");
        map.put("cond", "{store:{id:" + id + "},state:2,isDelete:1,isCertify:2,goodsType:{isDelete:1}}");
        map.put("jf", "goodsType|photo");
        map.put("pageIndex",String.valueOf(pageIndex));


        http.doCommonPost(null, MainUrl.basePageQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                if (!StringUtil.isBlank(resultStr)) {
                    Log.e("pullStoreProject",resultStr.toString());
                    try {
                        JSONObject obj = new JSONObject(resultStr);

                        JSONArray resultList = obj.getJSONArray("resultList");
                        if (resultList != null && resultList.length() > 0) {
                            pageIndex++;
                            for (int i = 0; i < resultList.length(); i++) {

                                JSONObject x = resultList.getJSONObject(i);

                                int id = x.getInt("id");
                                //项目名
                                String goodsName = x.getString("goodsName");
                                //logo
                                String url = x.getJSONObject("photo").getString("url");

                                //type可能会被删除
                                JSONObject goodsType = x.getJSONObject("goodsType");
                                String type = null;
                                if (goodsType != null) {
                                    type = goodsType.getString("name");
                                }
                                ProjectBean pb = new ProjectBean();
                                pb.setId(id);
                                pb.setName(goodsName);
                                pb.setLogoUrl(url);
                                pb.setType(type);

                                data.add(pb);

                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        adapter.notifyDataSetChanged();
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
                refresh.finishRefresh();
                refresh.finishRefreshLoadMore();
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

    public void pullMoreStoreProject(final int id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("ctype", "goods");
        map.put("cond", "{store:{id:" + id + "},state:2,isDelete:1,isCertify:2,goodsType:{isDelete:1}}");
        map.put("jf", "goodsType|photo");
        map.put("pageIndex",String.valueOf(pageIndex));

        http.doCommonPost(null, MainUrl.basePageQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                if (!StringUtil.isBlank(resultStr)) {
                    try {
                        JSONObject obj = new JSONObject(resultStr);

                        JSONArray resultList = obj.getJSONArray("resultList");
                        if (resultList != null && resultList.length() > 0) {
                            pageIndex++;
                            for (int i = 0; i < resultList.length(); i++) {

                                JSONObject x = resultList.getJSONObject(i);

                                int id = x.getInt("id");
                                //项目名
                                String goodsName = x.getString("goodsName");
                                //logo
                                String url = x.getJSONObject("photo").getString("url");

                                //type可能会被删除
                                JSONObject goodsType = x.getJSONObject("goodsType");
                                String type = null;
                                if (goodsType != null) {
                                    type = goodsType.getString("name");
                                }
                                ProjectBean pb = new ProjectBean();
                                pb.setId(id);
                                pb.setName(goodsName);
                                pb.setLogoUrl(url);
                                pb.setType(type);

                                data.add(pb);

                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        adapter.notifyDataSetChanged();
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
                refresh.finishRefresh();
                refresh.finishRefreshLoadMore();
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


    @OnClick(R.id.back)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
}
