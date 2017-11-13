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
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.MyShopsAdapter;
import com.example.mayikang.wowallet.adapter.StoreAdapter;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.Data;
import com.example.mayikang.wowallet.modle.javabean.StoreBean;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.widget.LoadingDialog;
import com.sctjsj.basemodule.base.util.ListViewUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by mayikang on 17/5/8.
 */

public class StoreFragment extends Fragment {


    @BindView(R.id.shops_tv_comprehensive)
    TextView shopsTvComprehensive;
    @BindView(R.id.shops_iv_comprehensive)
    ImageView shopsIvComprehensive;
    @BindView(R.id.shops_tv_comprehensive_bottom)
    TextView shopsTvComprehensiveBottom;
    @BindView(R.id.shops_tv_popularity)
    TextView shopsTvPopularity;
    @BindView(R.id.shops_iv_popularity)
    ImageView shopsIvPopularity;
    @BindView(R.id.shops_tv_popularity_bottom)
    TextView shopsTvPopularityBottom;
    @BindView(R.id.shops_rel_popularity)
    RelativeLayout shopsRelPopularity;
    /*@BindView(R.id.fragment_data_listview)
    ListView mLV;*/
    @BindView(R.id.store_rv)
    RecyclerView mLV;

    @BindView(R.id.friend_qrefresh)
    MaterialRefreshLayout friendQrefresh;


  /*  private MyShopsAdapter adapter;*/


    private Unbinder bind;
    private int pageIndex=1;
    private HttpServiceImpl http;
    private List<StoreBean> list = new LinkedList<>();
    private List<StoreBean> copyList=new LinkedList<>();
    private int type=1;
    private StoreAdapter adapter;
    private boolean Flag=true;
    private LoadingDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_store, null);
        bind = ButterKnife.bind(this, view);
        http= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
       /* adapter=new MyShopsAdapter(list,getActivity());*/
        adapter=new StoreAdapter(getActivity(),list);
        mLV.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLV.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        choiceCompre();
    }

    private void init() {
        initLV();
        //mLV.setAdapter(adapter);
        friendQrefresh.setLoadMore(true);
        friendQrefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                Flag=false;
                pullStore();
            }
            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                Flag=false;
               pullMoreStore();
            }
        });

        if(null==dialog){
            dialog=new LoadingDialog(getActivity());
        }

     /*   mLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               StoreBean bean= (StoreBean) adapter.getItem(position);

            }
        });*/
    }

    //切换fragment
    @OnClick({R.id.shops_rel_comprehensive, R.id.shops_rel_popularity, R.id.shops_tv_serch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //选择综合
            case R.id.shops_rel_comprehensive:
                choiceCompre();
                break;
            //选择人气
            case R.id.shops_rel_popularity:
                choicePopu();
                break;
            //搜索店铺
            case R.id.shops_tv_serch:
                ARouter.getInstance().build("/main/act/shopSearch").navigation();
        }
    }

   //选择人气
    private void choicePopu() {
        type=2;
        shopsTvComprehensive.setTextColor(getResources().getColor(R.color.color_text));
        shopsTvComprehensiveBottom.setVisibility(View.INVISIBLE);
        shopsIvComprehensive.setVisibility(View.INVISIBLE);
        shopsTvPopularity.setTextColor(getResources().getColor(R.color.color_deep_blue));
        shopsTvPopularityBottom.setVisibility(View.VISIBLE);
        shopsIvPopularity.setVisibility(View.VISIBLE);
        Flag=true;
        pullStore();


    }

    //选择综合
    private void choiceCompre() {
        type=1;
        shopsTvComprehensive.setTextColor(getResources().getColor(R.color.color_deep_blue));
        shopsTvComprehensiveBottom.setVisibility(View.VISIBLE);
        shopsIvComprehensive.setVisibility(View.VISIBLE);
        shopsTvPopularity.setTextColor(getResources().getColor(R.color.color_text));
        shopsTvPopularityBottom.setVisibility(View.INVISIBLE);
        shopsIvPopularity.setVisibility(View.INVISIBLE);
        Flag=true;
        pullStore();
    }

    //加载数据
    private void initLV() {


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (bind != null) {
            bind.unbind();
        }

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
                            if(type==2){
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
            public void onError(Throwable ex) {

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                friendQrefresh.finishRefresh();
                friendQrefresh.finishRefreshLoadMore();
                if(Flag){
                    dialog.dismiss();
                }
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                if(Flag){
                    dialog.setLoadingStr("加载中...");
                    dialog.setCancelable(true);
                    dialog.show();
                }
            }

            @Override
            public void onLoading(long total, long current) {

            }
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

                            if(type==2){
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
            public void onError(Throwable ex) {

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                friendQrefresh.finishRefresh();
                friendQrefresh.finishRefreshLoadMore();
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
