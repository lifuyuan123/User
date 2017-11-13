package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.OfficerListAdapter;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.JobBean;
import com.example.mayikang.wowallet.modle.javabean.OfficerBean;
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

@Route(path = "/main/act/officer_list")
public class OfficerListActivity extends BaseAppcompatActivity {

    @BindView(R.id.officer_list_back_rl)
    RelativeLayout officerListBackRl;
    @BindView(R.id.officer_list_RecycleView)
    RecyclerView officerListRecycleView;
    @BindView(R.id.refresh)MaterialRefreshLayout refresh;



    private HttpServiceImpl http;
    private OfficerListAdapter adapter;


    //店铺 id
    @Autowired(name = "id")
    public int storeId=-1;

    private int pageIndex=1;
    private List<OfficerBean> list=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        http = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        setAdapter();

        refresh.setLoadMore(true);
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                pullOfficerList(storeId);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                pullMoreOfficerList(storeId);
            }
        });

        pullOfficerList(storeId);

    }
    //设置适配器
    private void setAdapter() {
        adapter=new OfficerListAdapter(list,OfficerListActivity.this);
        officerListRecycleView.setLayoutManager(new GridLayoutManager(OfficerListActivity.this,2,GridLayoutManager.VERTICAL,false));
        officerListRecycleView.setAdapter(adapter);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_officer_list;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.officer_list_back_rl)
    public void onViewClicked() {
        finish();
    }

    public void pullOfficerList(int id){
        pageIndex=1;
        list.clear();
        HashMap<String,String> map=new HashMap<>();
        map.put("ctype","staff");
        map.put("cond","{store:{id:"+id+"},job:{isDelete:1},isDelete:1}");
        map.put("jf","staffPhoto|job");
        map.put("pageIndex",String.valueOf(pageIndex));
        http.doCommonPost(null, MainUrl.basePageQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                Log.e("gg",resultStr.toString());
                if(!StringUtil.isBlank(resultStr)){
                    try {
                        JSONObject obj=new JSONObject(resultStr);
                        JSONArray resultList=obj.getJSONArray("resultList");
                        if(resultList!=null && resultList.length()>0){
                            pageIndex++;
                            for (int i=0;i<resultList.length();i++){
                                JSONObject x=resultList.getJSONObject(i);

                                int id=x.getInt("id");
                                String name=x.getString("name");
                                String jobName=x.getJSONObject("job").getString("name");
                                String url=x.getJSONObject("staffPhoto").getString("url");
                                int jobId=x.getJSONObject("job").getInt("id");
                                OfficerBean ob=new OfficerBean();
                                ob.setId(id);
                                ob.setName(name);
                                ob.setLogo(url);
                                JobBean jb=new JobBean();
                                jb.setId(jobId);
                                jb.setName(jobName);
                                ob.setJobBean(jb);

                                list.add(ob);

                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }finally {
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
                refresh.finishRefreshLoadMore();
                refresh.finishRefresh();
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


    public void pullMoreOfficerList(int id){

        HashMap<String,String> map=new HashMap<>();
        map.put("ctype","staff");
        map.put("cond","{store:{id:"+id+"},job:{isDelete:1},isDelete:1}");
        map.put("jf","staffPhoto|job");
        map.put("pageIndex",String.valueOf(pageIndex));
        http.doCommonPost(null, MainUrl.basePageQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                if(!StringUtil.isBlank(resultStr)){
                    try {
                        JSONObject obj=new JSONObject(resultStr);
                        JSONArray resultList=obj.getJSONArray("resultList");
                        if(resultList!=null && resultList.length()>0){
                            pageIndex++;
                            for (int i=0;i<resultList.length();i++){
                                JSONObject x=resultList.getJSONObject(i);

                                int id=x.getInt("id");
                                String name=x.getString("name");
                                String jobName=x.getJSONObject("job").getString("name");
                                String url=x.getJSONObject("staffPhoto").getString("url");
                                int jobId=x.getJSONObject("job").getInt("id");
                                OfficerBean ob=new OfficerBean();
                                ob.setId(id);
                                ob.setName(name);
                                ob.setLogo(url);
                                JobBean jb=new JobBean();
                                jb.setId(jobId);
                                jb.setName(jobName);
                                ob.setJobBean(jb);

                                list.add(ob);

                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }finally {
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
                refresh.finishRefreshLoadMore();
                refresh.finishRefresh();
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
