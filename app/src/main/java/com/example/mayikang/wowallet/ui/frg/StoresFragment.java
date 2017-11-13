package com.example.mayikang.wowallet.ui.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.ListDropDownAdapter;
import com.example.mayikang.wowallet.adapter.StoreAdapter;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.StoreBean;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.widget.LoadingDialog;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.yyydjk.library.DropDownMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by lifuy on 2017/9/29.
 */

public class StoresFragment extends Fragment {
    @BindView(R.id.dropDownMenu)
    DropDownMenu dropDownMenu;
    private Unbinder bind;
    private HttpServiceImpl http;
    private MaterialRefreshLayout mrl;
    private LinearLayoutManager manager;
    private RecyclerView rv;
    private LinearLayout linNoData;
    private String headers[] = {"排序","类型"};
    private String sort[]={"价格","人气","距离"};
    private String type[]={"到店扫码","线下","线上"};

    private ListDropDownAdapter sortAdapter;
    private ListDropDownAdapter typeAdapter;
    private int constellationPosition = 0;//用于判断是否选择
    private List<View> popupViews = new ArrayList<>();

    private LoadingDialog dialog;


    private StoreAdapter adapter;
    private List<StoreBean> list = new LinkedList<>();
    private boolean Flag=true;
    private List<StoreBean> copyList=new LinkedList<>();
    private int pageIndex=1;
    private int types=1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_stores, null);
        bind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        http = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        initView();
        initData();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (dropDownMenu.isShowing()&&dropDownMenu!=null) {
            dropDownMenu.closeMenu();
        }
    }

    private void initView() {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.rental_content, null);
        mrl= (MaterialRefreshLayout) contentView.findViewById(R.id.frg_store_mrl);
        rv= (RecyclerView) contentView.findViewById(R.id.frg_store_rv);
        linNoData= (LinearLayout) contentView.findViewById(R.id.lin_frg_store_nodata);
        manager=new LinearLayoutManager(getActivity());

        if(null==dialog){
            dialog=new LoadingDialog(getActivity());
        }

        //排序下拉列表
        final ListView sortView = new ListView(getActivity());
        sortAdapter = new ListDropDownAdapter(getActivity(), Arrays.asList(sort));
        sortView.setDividerHeight(0);
        sortView.setAdapter(sortAdapter);

        //类型下拉列表
        final ListView typeView = new ListView(getActivity());
        typeView.setDividerHeight(0);
        typeAdapter = new ListDropDownAdapter(getActivity(), Arrays.asList(type));
        typeView.setAdapter(typeAdapter);


        //init popupViews
        popupViews.add(sortView);
        popupViews.add(typeView);

        //add item click event
        sortView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sortAdapter.setCheckItem(position);
                constellationPosition = position;
                dropDownMenu.setTabText(position == 0 ? headers[0] : sort[position]);
                dropDownMenu.closeMenu();

                switch (sort[position]){
                    case "价格":
                        //TODO条件搜索
                        break;
                    case "人气":
                        //TODO条件搜索
                        break;
                    case "距离":
                        //TODO条件搜索
                        break;
                }
            }
        });

        typeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                typeAdapter.setCheckItem(position);
                constellationPosition = position;
                dropDownMenu.setTabText(position == 0 ? headers[1] : type[position]);
                dropDownMenu.closeMenu();

                switch (type[position]){
                    case "到店扫码":
                        //TODO条件搜索
                        break;
                    case "线下":
                        //TODO条件搜索
                        break;
                    case "线上":
                        //TODO条件搜索
                        break;
                }
            }
        });

        adapter=new StoreAdapter(getActivity(),list);
        rv.setLayoutManager(manager);
        rv.setAdapter(adapter);
        mrl.setLoadMore(true);
        mrl.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                pullStore();
            }
            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                pullMoreStore();
            }
        });


        dropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);
    }

    private void initData() {
        pullStore();
    }


    @Override
    public void onDestroyView() {
            super.onDestroyView();
            bind.unbind();
        //退出activity前关闭菜单
        if (dropDownMenu.isShowing()&&dropDownMenu!=null) {
            dropDownMenu.closeMenu();
        }

    }


    @OnClick(R.id.shops_tv_serch)
    public void onViewClicked() {
        //搜索
        ARouter.getInstance().build("/main/act/shopSearch").navigation();
    }




    /**
     * 查询店铺
     */
    private void pullStore(){
        list.clear();
        copyList.clear();
        pageIndex=1;
        //http://118.123.22.190:8010/wow/user/pageSearch$ajax.htm?ctype=store&cond={isDelete:1,storeStatus:2}&jf=storeLogo
        HashMap<String,String> map=new HashMap<>();
        map.put("ctype","store");
        map.put("pageIndex",String.valueOf(pageIndex));
        map.put("cond","{isDelete:1,storeStatus:2}");
        map.put("jf","storeLogo");

        http.doCommonPost(null, MainUrl.basePageQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {

                if(!StringUtil.isBlank(resultStr)){
                    try {
                        JSONObject obj=new JSONObject(resultStr);
                        JSONArray storeStatus=obj.getJSONArray("resultList");
                        Log.e("pullStore",resultStr.toString());
                        if(storeStatus!=null && storeStatus.length()>0){
                            pageIndex++;
                            for(int i=0;i<storeStatus.length();i++){

                                JSONObject x=storeStatus.getJSONObject(i);
                                //店铺 id
                                int id=x.getInt("id");
                                //店铺名
                                String name=x.getString("name");

                                String detail=x.getString("detail");
                                //人气
                                int popularity=x.getInt("popularity");
                                String url="";
                                String storeLogo=x.getString("storeLogo");
                                if(!StringUtil.isBlank(storeLogo)){
                                    url=x.getJSONObject("storeLogo").getString("url");
                                }else {
                                    url="null";
                                }

                                //logo
                                String lo=x.getString("longitude");
                                String la=x.getString("latitude");
                                double longitude=0;
                                double latitude=0;
                                if(!StringUtil.isBlank(lo)&&!StringUtil.isBlank(la)){
                                    longitude =Double.valueOf(x.getString("longitude"));
                                    latitude=Double.valueOf(x.getString("latitude"));
                                }
                                double score=x.getDouble("score");
                                int type=x.getInt("type");

                                StoreBean sb=new StoreBean();
                                sb.setId(id);
                                sb.setDetail(detail);
                                sb.setName(name);
                                sb.setLogo(url);
                                sb.setLongitude(longitude);
                                sb.setLatitude(latitude);
                                sb.setPopularity(popularity);
                                sb.setScore(score);
                                sb.setType(type);
                                list.add(sb);
                            }
                            if(types==2){
                                Collections.sort(list, new Comparator<StoreBean>() {
                                    @Override
                                    public int compare(StoreBean o1, StoreBean o2) {
                                        if(o1.getPopularity()>o2.getPopularity()){
                                            return -1;
                                        }else if(o1.getPopularity()==o2.getPopularity()) {
                                            return 0;
                                        }
                                        else {
                                            return 1;
                                        }
                                    }
                                });
                            }
                            adapter.notifyDataSetChanged();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("pullStore",e.toString());
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {}
            @Override
            public void onCancelled(Callback.CancelledException cex) {}
            @Override
            public void onFinished() {
                mrl.finishRefresh();
                mrl.finishRefreshLoadMore();
                if(Flag){
                    dialog.dismiss();
                }
            }
            @Override
            public void onWaiting() {}
            @Override
            public void onStarted() {
                if(Flag){
                    dialog.setLoadingStr("加载中...");
                    dialog.setCancelable(true);
                    dialog.show();
                }
            }
            @Override
            public void onLoading(long total, long current) {}
        });


    }

    private void pullMoreStore(){

        //http://118.123.22.190:8010/wow/user/pageSearch$ajax.htm?ctype=store&cond={isDelete:1,storeStatus:2}&jf=storeLogo
        HashMap<String,String> map=new HashMap<>();
        map.put("ctype","store");
        map.put("pageIndex",String.valueOf(pageIndex));
        map.put("cond","{isDelete:1,storeStatus:2}");
        map.put("jf","storeLogo");

        http.doCommonPost(null, MainUrl.basePageQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                Log.e("pullStoreMore",resultStr.toString());
                if(!StringUtil.isBlank(resultStr)){
                    try {
                        JSONObject obj=new JSONObject(resultStr);
                        JSONArray storeStatus=obj.getJSONArray("resultList");
                        if(storeStatus!=null && storeStatus.length()>0){
                            pageIndex++;
                            for(int i=0;i<storeStatus.length();i++){

                                JSONObject x=storeStatus.getJSONObject(i);
                                //店铺 id
                                int id=x.getInt("id");
                                //店铺名
                                String name=x.getString("name");

                                String detail=x.getString("detail");
                                //人气
                                int popularity=x.getInt("popularity");
                                //logo
                                String url="";
                                String storeLogo=x.getString("storeLogo");
                                if(!StringUtil.isBlank(storeLogo)){
                                    url=x.getJSONObject("storeLogo").getString("url");
                                }else {
                                    url="null";
                                }

                                String lo=x.getString("longitude");
                                String la=x.getString("latitude");
                                double longitude=0;
                                double latitude=0;
                                if(!StringUtil.isBlank(lo)&&!StringUtil.isBlank(la)){
                                    longitude =Double.valueOf(x.getString("longitude"));
                                    latitude=Double.valueOf(x.getString("latitude"));
                                }
                                double score=x.getDouble("score");

                                StoreBean sb=new StoreBean();
                                sb.setId(id);
                                sb.setDetail(detail);
                                sb.setName(name);
                                sb.setLogo(url);
                                sb.setLongitude(longitude);
                                sb.setLatitude(latitude);
                                sb.setScore(score);
                                sb.setPopularity(popularity);
                                list.add(sb);
                            }

                            if(types==2){
                                Collections.sort(list, new Comparator<StoreBean>() {
                                    @Override
                                    public int compare(StoreBean o1, StoreBean o2) {
                                        if(o1.getPopularity()>o2.getPopularity()){
                                            return -1;
                                        }else if(o1.getPopularity()==o2.getPopularity()) {
                                            return 0;
                                        }
                                        else {
                                            return 1;
                                        }
                                    }
                                });
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("pullStoreMorejs",e.toString());
                    }finally {
                        adapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onError(Throwable ex) {}
            @Override
            public void onCancelled(Callback.CancelledException cex) {}
            @Override
            public void onFinished() {
                mrl.finishRefresh();
                mrl.finishRefreshLoadMore();
            }
            @Override
            public void onWaiting() {}
            @Override
            public void onStarted() {}
            @Override
            public void onLoading(long total, long current) {}
        });


    }

}
