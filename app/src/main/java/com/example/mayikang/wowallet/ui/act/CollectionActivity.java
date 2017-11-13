package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.MyCollectionAdapter;
import com.example.mayikang.wowallet.event.NormalOpEvent;
import com.example.mayikang.wowallet.event.PageSwitchEvent;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.CollectioniBean;
import com.example.mayikang.wowallet.modle.javabean.StoreBean;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.ListViewUtil;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.view.annotation.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import q.rorbin.qrefreshlayout.QRefreshLayout;
import q.rorbin.qrefreshlayout.RefreshHandler;

@Route(path = "/main/act/user/Collection")
public class CollectionActivity extends BaseAppcompatActivity {
    @BindView(R.id.act_collection_rv)
    RecyclerView mRV;
    @BindView(R.id.collection_qrefresh)
    MaterialRefreshLayout collectionQrefresh;

    @BindView(R.id.collection_linea_on)LinearLayout llRemind;
    private MyCollectionAdapter adapter;
    private List<StoreBean> list = new ArrayList<>();
    private HttpServiceImpl http;
    private int pageIndex=1;
    private boolean flag=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        http = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();

        initRV();
        pullCollectionStore();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_collection;
    }

    @Override
    public void reloadData() {

    }

    public void initRV(){
        adapter = new MyCollectionAdapter(list, this);
        mRV.setAdapter(adapter);
        mRV.setLayoutManager(new LinearLayoutManager(CollectionActivity.this));
        //collectionQrefresh.autoRefreshLoadMore();
        collectionQrefresh.setLoadMore(true);
        collectionQrefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                flag=false;
                pullCollectionStore();
            }
            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                pullMoreCollectionStore();
            }
        });
    }

    //查询收藏的店铺
    public void pullCollectionStore(){
        list.clear();
        pageIndex=1;
        HashMap<String,String> map=new HashMap<>();
        map.put("ctype","favorite");
        map.put("jf","store|storeLogo");
        map.put("cond","{user:{id:"+ UserAuthUtil.getUserId()+"}}");
        map.put("pageIndex",String.valueOf(pageIndex));
        http.doCommonPost(null, MainUrl.basePageQueryUrl, map, new XProgressCallback() {

            @Override
            public void onSuccess(String resultStr) {
                 LogUtil.e("collection",resultStr.toString());
                if(resultStr!=null){
                    try {
                        JSONObject obj=new JSONObject(resultStr);
                        JSONArray resultList=obj.getJSONArray("resultList");
                        if(resultList!=null && resultList.length()>0){
                            llRemind.setVisibility(View.GONE);
                            pageIndex++;
                            for (int i=0;i<resultList.length();i++){
                                JSONObject store=resultList.getJSONObject(i).getJSONObject("store");

                                int id=store.getInt("id");
                                String name=store.getString("name");
                                String detail=store.getString("detail");
                                String logo=store.getJSONObject("storeLogo").getString("url");

                                StoreBean sb=new StoreBean();
                                sb.setId(id);
                                sb.setName(name);
                                sb.setDetail(detail);
                                sb.setLogo(logo);

                                list.add(sb);
                            }
                        }else {
                            llRemind.setVisibility(View.VISIBLE);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    finally {
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
                collectionQrefresh.finishRefresh();
                collectionQrefresh.finishRefreshLoadMore();
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

    public void pullMoreCollectionStore(){

        HashMap<String,String> map=new HashMap<>();
        map.put("ctype","favorite");
        map.put("jf","store");
        map.put("cond","{user:{id:"+ UserAuthUtil.getUserId()+"}}");
        map.put("pageIndex",String.valueOf(pageIndex));
        http.doCommonPost(null, MainUrl.basePageQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                if(resultStr!=null){
                    try {
                        JSONObject obj=new JSONObject(resultStr);
                        JSONArray resultList=obj.getJSONArray("resultList");
                        if(resultList!=null && resultList.length()>0){

                            pageIndex++;
                            for (int i=0;i<resultList.length();i++){
                                JSONObject store=resultList.getJSONObject(i);

                                int id=store.getInt("id");
                                String name=store.getString("name");
                                String detail=store.getString("detail");
                                String logo=store.getJSONObject("storeLogo").getString("url");

                                StoreBean sb=new StoreBean();
                                sb.setId(id);
                                sb.setName(name);
                                sb.setDetail(detail);
                                sb.setLogo(logo);

                                list.add(sb);
                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    finally {
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

                collectionQrefresh.finishRefresh();
                collectionQrefresh.finishRefreshLoadMore();
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

    @Subscribe
    public void onMainEvent(NormalOpEvent event){
        if(event==null){
            return;
        }

        if(1==event.getOpStatus()&& event.getIndex()>=0){
            if(list!=null && list.size()>event.getIndex()){
                list.remove(event.getIndex());
                adapter.notifyItemRemoved(event.getIndex());

                if(list.size()<=0){
                    llRemind.setVisibility(View.VISIBLE);
                }else {
                    llRemind.setVisibility(View.GONE);
                }

            }
        }



    }

    @OnClick({R.id.mafens_linear_back,R.id.act_collection_btn_scan})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.mafens_linear_back:
                finish();
                break;
            case R.id.act_collection_btn_scan:
                EventBus.getDefault().post(new PageSwitchEvent("STORE"));
                finish();
                break;
        }
    }

}
