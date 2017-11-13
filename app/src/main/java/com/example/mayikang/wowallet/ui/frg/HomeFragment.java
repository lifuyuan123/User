package com.example.mayikang.wowallet.ui.frg;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.HomeMultLayoutAdapter;
import com.example.mayikang.wowallet.event.AreaEvent;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.ProjectBean;
import com.example.mayikang.wowallet.modle.javabean.StoreBean;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * Created by mayikang on 17/5/8.
 */

public class HomeFragment extends Fragment {
    @BindView(R.id.frg_home_xrv)
    RecyclerView mRV;
    @BindView(R.id.refresh)
    MaterialRefreshLayout refreshLayout;
    @BindView(R.id.home_location_txt)
    TextView homeLocationTxt;
    @BindView(R.id.home_location_layout)
    LinearLayout homeLocationLayout;
    @BindView(R.id.home_search_layout)
    LinearLayout homeSearchLayout;
    @BindView(R.id.home_lay1_iv_message)
    ImageView homeLay1IvMessage;
    @BindView(R.id.home_rl_message)
    RelativeLayout homeRlMessage;
    @BindView(R.id.frg_home_dataLayout)
    LinearLayout frg_home_dataLayout;

    private HomeMultLayoutAdapter balanceAdapter, moduleAdapter, brandAdapter, singleProductAdapter, storeTitleAdapter, storeAdapter;
    //代理适配器
    private DelegateAdapter adapter;

    //private boolean isLoadOver=true;//判断上次加载是否完成

    //private boolean isFirstLoad=true;//判断是否是第一次加载fragment

    /**
     * 对应的子适配器
     **/
    private List<DelegateAdapter.Adapter> adapterList = new LinkedList<>();

    //店铺列表
    private ArrayList<HashMap<String, Object>> storeData = new ArrayList<>();


    private HttpServiceImpl http;

    //数据
    private HashMap<String, Object> balanceMap = new HashMap<>();
    private HashMap<String, Object> singleProductMap = new LinkedHashMap<>();
    private HashMap<String, Object> brandStoreMap = new LinkedHashMap<>();


    private int pageIndex = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_home, null);
        ButterKnife.bind(this, view);
        http = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();

        initRVLayout();

        pullBrandStore();

        pullSingleProduct();


        pullBalance();


        //定位或者没有定位
        if (storeList()) {
            pullNearbyStoer();
        } else {
            pullStore();
        }



        pullBrandStore();

        pullSingleProduct();

        pullBalance();


       //定位或者没有定位
        if (storeList()) {
            pullNearbyStoer();
        } else {
            pullStore();
        }




        refreshLayout.setLoadMore(true);
        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {

                if (storeList()) {
                    pullNearbyStoer();
                } else {
                    pullStore();
                }
                pullBrandStore();

                pullSingleProduct();

                pullBalance();

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if (storeList()) {
                    pullNearbyStoerMore();
                } else {
                    pullMoreStore();
                }
            }
        });

        //滑动监听
        mRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                Picasso picasso = PicassoUtil.getPicassoObject();
                if (newState == SCROLL_STATE_IDLE) {
                    picasso.resumeTag("home_rv_img");
                } else {
                    picasso.pauseTag("home_rv_img");
                }
            }
        });
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("HomeFragment","onResume");
        if (!SPFUtil.contains(Tag.TAG_TOKEN) && !UserAuthUtil.isUserLogin()) {
            balanceMap.put("amount", 0d);
            balanceMap.put("cumulativeBonus", 0d);
            balanceMap.put("cumulativeSpend", 0d);
            balanceAdapter.notifyItemChanged(0);

        }else {
            pullBalance();
        }

       /* if (storeList()) {
            pullNearbyStoer();
        } else {
            pullStore();
        }*/


        LogUtil.e("location","TAG_CITY:"+(String)SPFUtil.get(Tag.TAG_CITY,"none")+"     TAG_LONGITUDE:"+(String)SPFUtil.get(Tag.TAG_LONGITUDE,"none")+"     TAG_LATITUDE"+(String)SPFUtil.get(Tag.TAG_LATITUDE,"none"));


        String city= (String) SPFUtil.get(Tag.TAG_CITY,"null");
        Log.e("HomeFragment",city);
        if(!StringUtil.isBlank(city)){
            homeLocationTxt.setText(city);
        }



//        pullBrandStore();
//
//        pullSingleProduct();
//
//        pullBalance();
//
//
//        //定位或者没有定位
//        if (storeList()) {
//            pullNearbyStoer();
//        } else {
//            pullStore();
//        }




    }

    public void initRVLayout() {

        //虚拟适配器
        VirtualLayoutManager layoutManager = new VirtualLayoutManager(getActivity());
        mRV.setLayoutManager(layoutManager);

        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        mRV.setRecycledViewPool(viewPool);

        viewPool.setMaxRecycledViews(6, 10);


        initBalanceHelper();

        initModuleHelper();

        initBrandHelper();

        initSingleProductHelper();

        initStoreTitleHelper();

        initStoreHelper();

        //设置代理适配器
        adapter = new DelegateAdapter(layoutManager, false);
        adapter.setAdapters(adapterList);
        mRV.setAdapter(adapter);

    }

    private void initBalanceHelper() {
        SingleLayoutHelper singleLayoutHelper_balance = new SingleLayoutHelper();
        //只能有一个 item 项目
        singleLayoutHelper_balance.setItemCount(1);

        ArrayList<HashMap<String, Object>> data = new ArrayList<>();
        balanceMap = new HashMap<>();

        data.add(balanceMap);
        balanceAdapter = new HomeMultLayoutAdapter(getActivity(), singleLayoutHelper_balance, 1, data, 1);
        adapterList.add(balanceAdapter);
    }

    private void initModuleHelper() {
        SingleLayoutHelper singleLayoutHelper_module = new SingleLayoutHelper();
        //设置宽高比

        //只能有一个 item 项目
        singleLayoutHelper_module.setItemCount(1);

        ArrayList<HashMap<String, Object>> data = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();

        data.add(map);
        moduleAdapter = new HomeMultLayoutAdapter(getActivity(), singleLayoutHelper_module, 1, data, 2);
        moduleAdapter.setClickAgent(new HomeMultLayoutAdapter.HomeMultCallBack() {
            @Override
            public void joinAgentClick() {
                verifyAgent();
            }
        });
        adapterList.add(moduleAdapter);
    }

    private void initBrandHelper() {
        SingleLayoutHelper singleLayoutHelper_brand = new SingleLayoutHelper();

        //只能有一个 item 项目
        singleLayoutHelper_brand.setItemCount(1);
        ArrayList<HashMap<String, Object>> data = new ArrayList<>();
        data.add(brandStoreMap);
        brandAdapter = new HomeMultLayoutAdapter(getActivity(), singleLayoutHelper_brand, 1, data, 3);
        adapterList.add(brandAdapter);
    }

    private void initSingleProductHelper() {
        SingleLayoutHelper singleLayoutHelper_single = new SingleLayoutHelper();
        //设置宽高比
        //singleLayoutHelper_banner.setAspectRatio(2.36f);
        //只能有一个 item 项目
        singleLayoutHelper_single.setItemCount(1);

        ArrayList<HashMap<String, Object>> data = new ArrayList<>();
        singleProductMap = new HashMap<>();

        data.add(singleProductMap);
        singleProductAdapter = new HomeMultLayoutAdapter(getActivity(), singleLayoutHelper_single, 1, data, 4);
        adapterList.add(singleProductAdapter);
    }

    private void initStoreTitleHelper() {
        SingleLayoutHelper singleLayoutHelper_banner = new SingleLayoutHelper();
        //设置宽高比
        //singleLayoutHelper_banner.setAspectRatio(2.36f);
        //只能有一个 item 项目
        singleLayoutHelper_banner.setItemCount(1);

        ArrayList<HashMap<String, Object>> data = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();

        data.add(map);
        storeTitleAdapter = new HomeMultLayoutAdapter(getActivity(), singleLayoutHelper_banner, 1, data, 5);
        adapterList.add(storeTitleAdapter);
    }

    private void initStoreHelper() {
        LinearLayoutHelper linearLayoutHelper_store = new LinearLayoutHelper();

        storeAdapter = new HomeMultLayoutAdapter(getActivity(), linearLayoutHelper_store, 1, storeData, 6);
        adapterList.add(storeAdapter);
    }

    /**
     * 查询账余额详情
     */
    private void pullBalance() {

        HashMap<String, String> map = new HashMap<>();
        map.put("ctype", "user");
        map.put("jf", "userSpend");
        map.put("cond", "{id:" + UserAuthUtil.getUserId() + "}");

        http.doCommonPost(null, MainUrl.basePageQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {

                if (!StringUtil.isBlank(resultStr)) {

                    try {
                        JSONObject res = new JSONObject(resultStr);
                        boolean result = res.getBoolean("result");
                        if (result) {
                            JSONArray resultList = res.getJSONArray("resultList");
                            if (resultList != null && resultList.length() > 0) {
                                //账户余额
                                double amount = resultList.getJSONObject(0).getJSONArray("userSpend").getJSONObject(0).getDouble("amount");
                                //累计消费
                                double cumulativeBonus = resultList.getJSONObject(0).getJSONArray("userSpend").getJSONObject(0).getDouble("cumulativeBonus");
                                //累计分红
                                double cumulativeSpend = resultList.getJSONObject(0).getJSONArray("userSpend").getJSONObject(0).getDouble("cumulativeSpend");

                                balanceMap.put("amount", amount);
                                balanceMap.put("cumulativeBonus", cumulativeBonus);
                                balanceMap.put("cumulativeSpend", cumulativeSpend);
                                balanceAdapter.notifyItemChanged(0);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("pullBalance", e.toString());
                    }

                }
            }

            @Override
            public void onError(Throwable ex) {
                LogUtil.e("错误", ex.toString());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

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

    /**
     * 查询品牌合作专区
     */
    private void pullBrandStore() {
        //http://118.123.22.190:8010/wow/user/pageSearch$ajax.htm?ctype=store&cond={recommendLeve:3,isDelete:1,storeStatus:2}&jf=storeLogo
        HashMap<String, String> map = new HashMap<>();
        map.put("ctype", "store");
        map.put("cond", "{recommendLeve:3,isDelete:1,storeStatus:2}");
        map.put("jf", "storeLogo");

        http.doCommonPost(null, MainUrl.basePageQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {

                if (!StringUtil.isBlank(resultStr)) {
                    try {
                        JSONObject obj = new JSONObject(resultStr);
                        JSONArray storeStatus = obj.getJSONArray("resultList");

                        if (storeStatus != null && storeStatus.length() > 0) {
                            List<StoreBean> list = new ArrayList<>();
                            for (int i = 0; i < storeStatus.length(); i++) {

                                JSONObject x = storeStatus.getJSONObject(i);
                                //店铺 id
                                int id = x.getInt("id");
                                //店铺名
                                String name = x.getString("name");

                                //人气
                                int popularity = x.getInt("popularity");
                                //logo
                                String url = null;
                                String photo = x.getString("storeLogo");
                                if (!StringUtil.isBlank(photo)) {
                                    url = x.getJSONObject("storeLogo").getString("url");
                                }


                                StoreBean sb = new StoreBean();
                                sb.setId(id);
                                sb.setName(name);
                                sb.setLogo(url);
                                sb.setPopularity(popularity);
                                list.add(sb);
                            }

                            brandStoreMap.put("brand_store_list", list);
                            brandAdapter.notifyItemChanged(0);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("pullBrandStore", e.toString());
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


    //判断是否定位成功
    public boolean storeList() {
        String longitude = (String) SPFUtil.get(Tag.TAG_LONGITUDE, "null");
        String latude = (String) SPFUtil.get(Tag.TAG_LATITUDE, "null");
        Log.e("location",longitude+"----"+latude);
        if (!StringUtil.isBlank(longitude) &&
                !StringUtil.isBlank(latude)) {
            return true;
        }
        return false;
    }


    //查询附近店铺
    public void pullNearbyStoer() {
        pageIndex = 1;
        storeData.clear();
        String longitude = (String) SPFUtil.get(Tag.TAG_LONGITUDE, "null");
        String latude = (String) SPFUtil.get(Tag.TAG_LATITUDE, "null");
        HashMap<String, String> body = new HashMap<>();
        body.put("latitude", latude);
        body.put("longitude", longitude);
        body.put("raidus", "30");
        body.put("pageIndex", String.valueOf(pageIndex));
        body.put("jf", "storeLogo");
        http.doCommonPost(null, MainUrl.PullNearStoreUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("pullNearbyStoer", result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        if (object.getBoolean("result")) {
                            JSONArray array = object.getJSONArray("resultList");
                            if (null != array && array.length() > 0) {
                                pageIndex++;
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject arrObj = array.getJSONObject(i);
                                    int id = Integer.valueOf(arrObj.getString("id"));
                                    double score = 0;
                                    if (!StringUtil.isBlank(arrObj.getString("score"))) {
                                        score = Double.valueOf(arrObj.getString("score"));
                                    }
                                    String name = arrObj.getString("name");
                                    String lo = arrObj.getString("longitude");
                                    String la = arrObj.getString("latitude");
                                    double longitud = 0;
                                    double latitude = 0;
                                    if (!StringUtil.isBlank(lo) && !StringUtil.isBlank(la)) {
                                        longitud = Double.valueOf(arrObj.getString("longitude"));
                                        latitude = Double.valueOf(arrObj.getString("latitude"));
                                    }
                                    String detail = "";
                                    if(!StringUtil.isBlank(arrObj.getString("detail"))){
                                        detail=arrObj.getString("detail");
                                    }

                                    int salenum = 0;
                                    if (!StringUtil.isBlank(arrObj.getString("popularity"))) {
                                        salenum = Integer.valueOf(arrObj.getInt("popularity"));
                                    }
                                    String url = arrObj.getString("url");

                                    StoreBean sb = new StoreBean();
                                    sb.setId(id);
                                    sb.setName(name);
                                    sb.setLogo(url);
                                    sb.setScore(score);
                                    sb.setPopularity(salenum);
                                    sb.setLongitude(longitud);
                                    sb.setLatitude(latitude);
                                    sb.setDetail(detail);

                                    HashMap<String, Object> map = new HashMap<String, Object>();
                                    map.put("store", sb);
                                    storeData.add(map);

                                }
                                Log.e("pullNearbyStoer","adapter");
                                storeAdapter.notifyItemChanged(storeData == null ? 0 : storeData.size());


                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("pullNearbyStoer", e.toString());
                    }finally {
                        if(storeData.size()==0){
                            Snackbar snackbar = Snackbar.make(frg_home_dataLayout, "您附近没有店铺，请重新切换区域", Snackbar.LENGTH_LONG).setAction("切换", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //跳转到选择地区界面
                                    ARouter.getInstance().build("/main/act/ChoiceCity").navigation();
                                }
                            });
                            View view = snackbar.getView();
                            if(view!=null){
                                view.setBackgroundColor(getResources().getColor(R.color.color_primary_blue));
                            }
                            snackbar.show();

                        }
                    }

                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("pullNearbyStoer", ex.toString());

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                refreshLayout.finishRefresh();
                refreshLayout.finishRefreshLoadMore();
                //isLoadOver=true;
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                //isLoadOver=false;

            }

            @Override
            public void onLoading(long total, long current) {

            }
        });


    }


    //查询附近店铺
    public void pullNearbyStoerMore() {
        String longitude = (String) SPFUtil.get(Tag.TAG_LONGITUDE, "null");
        String latude = (String) SPFUtil.get(Tag.TAG_LATITUDE, "null");
        HashMap<String, String> body = new HashMap<>();
        body.put("latitude", latude);
        body.put("longitude", longitude);
        body.put("raidus", "10");
        body.put("pageIndex", String.valueOf(pageIndex));
        body.put("jf", "storeLogo");
        http.doCommonPost(null, MainUrl.PullNearStoreUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("pullNearbyStoer", result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        if (object.getBoolean("result")) {
                            JSONArray array = object.getJSONArray("resultList");
                            if (null != array && array.length() > 0) {
                                pageIndex++;
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject arrObj = array.getJSONObject(i);
                                    int id = Integer.valueOf(arrObj.getString("id"));
                                    double score = 0;
                                    if (!StringUtil.isBlank(arrObj.getString("score"))) {
                                        score = Double.valueOf(arrObj.getString("score"));
                                    }
                                    String name = arrObj.getString("name");
                                    String lo = arrObj.getString("longitude");
                                    String la = arrObj.getString("latitude");
                                    double longitud = 0;
                                    double latitude = 0;
                                    if (!StringUtil.isBlank(lo) && !StringUtil.isBlank(la)) {
                                        longitud = Double.valueOf(arrObj.getString("longitude"));
                                        latitude = Double.valueOf(arrObj.getString("latitude"));
                                    }
                                    String detail = "";
                                    if(!StringUtil.isBlank(arrObj.getString("detail"))){
                                        detail=arrObj.getString("detail");
                                    }
                                    int salenum = 0;
                                    if (!StringUtil.isBlank(arrObj.getString("popularity"))) {
                                        salenum = Integer.valueOf(arrObj.getInt("popularity"));
                                    }
                                    String url = arrObj.getString("url");

                                    StoreBean sb = new StoreBean();
                                    sb.setId(id);
                                    sb.setName(name);
                                    sb.setLogo(url);
                                    sb.setScore(score);
                                    sb.setPopularity(salenum);
                                    sb.setLongitude(longitud);
                                    sb.setLatitude(latitude);
                                    sb.setDetail(detail);

                                    HashMap<String, Object> map = new HashMap<String, Object>();
                                    map.put("store", sb);
                                    storeData.add(map);
                                    storeAdapter.notifyItemChanged(storeData == null ? 0 : storeData.size());
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("pullNearbyStoer", e.toString());
                    }

                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("pullNearbyStoer", ex.toString());

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                refreshLayout.finishRefresh();
                refreshLayout.finishRefreshLoadMore();
               // isLoadOver=true;
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                //isLoadOver=false;
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });


    }


    /**
     * 查询店铺
     */
    private void pullStore() {
        pageIndex = 1;
        storeData.clear();
        HashMap<String, String> map = new HashMap<>();
        map.put("ctype", "store");
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("cond", "{isDelete:1,storeStatus:2}");
        map.put("jf", "storeLogo");

        http.doCommonPost(null, MainUrl.basePageQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {

                if (!StringUtil.isBlank(resultStr)) {
                    try {
                        JSONObject obj = new JSONObject(resultStr);
                        JSONArray storeStatus = obj.getJSONArray("resultList");
                        Log.e("pullStore", resultStr.toString());
                        if (storeStatus != null && storeStatus.length() > 0) {
                            pageIndex++;
                            for (int i = 0; i < storeStatus.length(); i++) {

                                JSONObject x = storeStatus.getJSONObject(i);
                                //店铺 id
                                int id = x.getInt("id");
                                //店铺名
                                String name = x.getString("name");

                                String lo = x.getString("longitude");
                                String la = x.getString("latitude");
                                double longitude = 0;
                                double latitude = 0;
                                if (!StringUtil.isBlank(lo) && !StringUtil.isBlank(la)) {
                                    longitude = Double.valueOf(x.getString("longitude"));
                                    latitude = Double.valueOf(x.getString("latitude"));
                                }
                                //人气
                                int popularity = x.getInt("popularity");
                                double score = x.getDouble("score");
                                String detail = x.getString("detail");
                                //logo
                                String url = null;
                                String photo = x.getString("storeLogo");
                                if (!StringUtil.isBlank(photo)) {
                                    url = x.getJSONObject("storeLogo").getString("url");
                                }
                                StoreBean sb = new StoreBean();
                                sb.setId(id);
                                sb.setName(name);
                                sb.setLogo(url);
                                sb.setScore(score);
                                sb.setPopularity(popularity);
                                sb.setLongitude(longitude);
                                sb.setLatitude(latitude);
                                sb.setDetail(detail);
                                HashMap<String, Object> map = new HashMap<String, Object>();
                                map.put("store", sb);
                                storeData.add(map);
                                storeAdapter.notifyItemChanged(storeData == null ? 0 : storeData.size());
                            }


                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("pullStore", e.toString());
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
                refreshLayout.finishRefresh();
                refreshLayout.finishRefreshLoadMore();
                //isLoadOver=true;
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                //isLoadOver=false;
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });


    }

    private void pullMoreStore() {

        //http://118.123.22.190:8010/wow/user/pageSearch$ajax.htm?ctype=store&cond={isDelete:1,storeStatus:2}&jf=storeLogo
        HashMap<String, String> map = new HashMap<>();
        map.put("ctype", "store");
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("cond", "{isDelete:1,storeStatus:2}");
        map.put("jf", "storeLogo");

        http.doCommonPost(null, MainUrl.basePageQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                Log.e("pullStoreMore", resultStr.toString());

                if (!StringUtil.isBlank(resultStr)) {
                    try {
                        JSONObject obj = new JSONObject(resultStr);
                        JSONArray storeStatus = obj.getJSONArray("resultList");

                        if (storeStatus != null && storeStatus.length() > 0) {
                            pageIndex++;
                            for (int i = 0; i < storeStatus.length(); i++) {

                                JSONObject x = storeStatus.getJSONObject(i);
                                //店铺 id
                                int id = x.getInt("id");
                                //店铺名
                                String name = x.getString("name");

                                String lo = x.getString("longitude");
                                String la = x.getString("latitude");
                                double longitude = 0;
                                double latitude = 0;
                                if (!StringUtil.isBlank(lo) && !StringUtil.isBlank(la)) {
                                    longitude = Double.valueOf(x.getString("longitude"));
                                    latitude = Double.valueOf(x.getString("latitude"));
                                }
                                //人气
                                int popularity = x.getInt("popularity");
                                double score = x.getDouble("score");
                                String detail = x.getString("detail");
                                //logo
                                String url = null;
                                String photo = x.getString("storeLogo");
                                if (!StringUtil.isBlank(photo)) {
                                    url = x.getJSONObject("storeLogo").getString("url");
                                }

                                StoreBean sb = new StoreBean();
                                sb.setId(id);
                                sb.setName(name);
                                sb.setLogo(url);
                                sb.setScore(score);
                                sb.setPopularity(popularity);
                                sb.setLongitude(longitude);
                                sb.setLatitude(latitude);
                                sb.setDetail(detail);
                                HashMap<String, Object> map = new HashMap<String, Object>();
                                map.put("store", sb);
                                storeData.add(map);
                                storeAdapter.notifyItemChanged(storeData == null ? 0 : storeData.size());
                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("pullStoreMore", e.toString());
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
                refreshLayout.finishRefresh();
                refreshLayout.finishRefreshLoadMore();
                //isLoadOver=true;
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                //isLoadOver=false;
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });


    }

    /**
     * 查询热销单品
     */
    private void pullSingleProduct() {
        //http://192.168.1.138:8080/wow/user/pageSearch$ajax.htm?ctype=goods&orderby=salenum desc&jf=photo&cond={isDelete:1,isCertify:2}
        HashMap<String, String> map = new HashMap<>();
        map.put("ctype", "goods");
        map.put("orderby", "salenum desc");
        map.put("size", "3");
        map.put("pageIndex", "1");
        map.put("jf", "photo");
        map.put("cond", "{isDelete:1,isCertify:2}");
        http.doCommonPost(null, MainUrl.basePageQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                if (!StringUtil.isBlank(resultStr)) {
                    try {
                        Log.e("pullSingleProduct",resultStr.toString());
                        JSONObject obj = new JSONObject(resultStr);
                        JSONArray resultList = obj.getJSONArray("resultList");
                        if (resultList != null && resultList.length() > 0) {
                            List<ProjectBean> pbList = new ArrayList<ProjectBean>();
                            for (int i = 0; i < resultList.length(); i++) {

                                JSONObject x = resultList.getJSONObject(i);
                                //id
                                int id = x.getInt("id");
                                //单品名字
                                String goodsName = x.getString("goodsName");

                                //图片地址
                                String url = null;
                                String photo = x.getString("photo");
                                if (!StringUtil.isBlank(photo)) {
                                    url = x.getJSONObject("photo").getString("url");
                                }
                                //描述
                                String describe = x.getString("describe");

                                ProjectBean pb = new ProjectBean();
                                pb.setId(id);
                                pb.setName(goodsName);
                                pb.setLogoUrl(url);
                                pb.setDescribe(describe);

                                pbList.add(pb);

                            }

                            singleProductMap.put("sing_product_list", pbList);
                            singleProductAdapter.notifyItemChanged(0);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("pullSingleProduct", e.toString());
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("pullSingleProduct",ex.toString());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

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


    //查询套餐的状态
    private void verifyAgent() {
        HashMap<String, String> body = new HashMap<>();
        body.put("userId", UserAuthUtil.getUserId() + "");
        http.doCommonPost(null, MainUrl.VerifyAgent, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("verifyAgent", result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        if (object.getBoolean("result")) {
                            JSONObject applyTbl = object.getJSONObject("applyTbl");
                            ARouter.getInstance().build("/main/act/user/AgentApplyStateActivity").withInt("id", applyTbl.getInt("id")).navigation();
                        } else {
                            ARouter.getInstance().build("/main/act/ChooseAgentActivity").navigation();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("verifyAgent", e.toString());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @OnClick({R.id.home_location_layout, R.id.home_search_layout, R.id.home_rl_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //选择地区
            case R.id.home_location_layout:
                ARouter.getInstance().build("/main/act/ChoiceCity").navigation();
                break;
            case R.id.home_search_layout:
                ARouter.getInstance().build("/main/act/shopSearch").navigation();
                break;
            case R.id.home_rl_message:
                ARouter.getInstance().build("/main/act/message_list").navigation();
                break;
        }
    }




    public void pullData(){
        if (storeList()) {
            pullNearbyStoer();
        } else {
            pullStore();
        }
    }
}
