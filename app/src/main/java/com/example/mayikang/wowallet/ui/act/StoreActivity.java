package com.example.mayikang.wowallet.ui.act;


import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.ColumnLayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.StoreSubAdapter;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.CommentBean;
import com.example.mayikang.wowallet.modle.javabean.JobBean;
import com.example.mayikang.wowallet.modle.javabean.OfficerBean;
import com.example.mayikang.wowallet.modle.javabean.ProjectBean;
import com.example.mayikang.wowallet.modle.javabean.UserBean;
import com.example.mayikang.wowallet.ui.xwidget.dialog.SocialShareDialog;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.google.gson.Gson;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.constant.code;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.CallUtil;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.base.util.permission.EasyPermissionsEx;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;

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
import butterknife.OnClick;

@Route(path = "/main/act/store")
public class StoreActivity extends BaseAppcompatActivity implements EasyPermissionsEx.PermissionCallbacks {

    @BindView(R.id.refresh)
    MaterialRefreshLayout refreshLayout;
    @BindView(R.id.rv)
    RecyclerView mRV;
    @BindView(R.id.act_store_iv_collect)
    ImageView ivCollect;
    @BindView(R.id.act_store_iv_collected)
    ImageView ivCollected;
    HashMap<String,Object> data=new HashMap<>();


    /**整个页面的数据**/
    //private ArrayList<HashMap<String, Object>> data=new ArrayList<>();

    /****/
    private DelegateAdapter adapter;

    /**
     * 对应的子适配器
     **/
    private List<DelegateAdapter.Adapter> subAdapterList = new LinkedList<>();

    private StoreSubAdapter bannerAdapter, storeNameAdapter, storeLocationAdapter, storeBrandAdapter,
            storeProjectAdapter, projectTitleAdapter, commentStoreTitleAdapter,
            commentStoreAdapter, officerTitleAdapter, officerAdapter, allCommentTitleAdapter;

    private HttpServiceImpl http;

    private HashMap<String, Object> storeNameMap = new HashMap<>();
    private HashMap<String, Object> storeLocationMap = new HashMap<>();
    private HashMap<String, Object> storeBrandMap = new HashMap<>();
    private HashMap<String, Object> officerTitleMap = new HashMap<>();
    private HashMap<String, Object> bannerMap = new HashMap<>();
    private HashMap<String, Object> commentTitleMap = new HashMap<>();
    private HashMap<String, Object> projectTitleMap = new HashMap<>();
    private HashMap<String, Object> toAllCommentMap = new HashMap<>();

    private ArrayList<HashMap<String, Object>> projectData = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> officerData = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> commentData = new ArrayList<>();
    @Autowired(name = "id")
    public int id = -1;

    @Override
    public int initLayout() {
        return R.layout.activity_store;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        http = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();

        refreshLayout.setLoadMore(false);
        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefresh();
                    }
                }, 2000);
            }
        });

        initRVLayout();

        if (-1 != id) {
            pullStoreGallery(id);
            pullStoreInfo(id);
            pullStoreProject(id);
            pullStoreOfficer(id);
            pullCommentInfo(id);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        isCollected();
    }

    /**
     * 检查店铺是否已经被当前用户收藏了该店铺
     */
    private void isCollected() {

        HashMap<String, String> map = new HashMap();
        map.put("storeId", String.valueOf(id));
        http.doCommonPost(null, MainUrl.queryIsStoreCollectedUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {

                if (!StringUtil.isBlank(resultStr)) {
                    try {
                        JSONObject obj = new JSONObject(resultStr);
                        //是否收藏了店铺
                        boolean result = obj.getBoolean("result");
                        //已经收藏
                        if (result) {
                            ivCollect.setVisibility(View.GONE);
                            ivCollected.setVisibility(View.VISIBLE);
                        } else {
                            //未收藏
                            ivCollect.setVisibility(View.VISIBLE);
                            ivCollected.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
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

    /**
     * 添加收藏或删除收藏店铺
     */
    private void collectClick() {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId",String.valueOf(UserAuthUtil.getUserId()));
        map.put("storeId",String.valueOf(id));
        http.doCommonPost(null, MainUrl.addOrDeleteCollectedStoreUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {

                if(!StringUtil.isBlank(resultStr)){
                    try {
                        JSONObject obj=new JSONObject(resultStr);
                        boolean result=obj.getBoolean("result");
                        String msg=obj.getString("msg");
                        if(result){
                            Toast.makeText(StoreActivity.this, msg, Toast.LENGTH_SHORT).show();
                            //收藏成功
                            if("收藏成功！".equals(msg)){
                                ivCollect.setVisibility(View.GONE);
                                ivCollected.setVisibility(View.VISIBLE);
                            }
                            //取消收藏成功
                            if("取消收藏！".equals(msg)){
                                ivCollect.setVisibility(View.VISIBLE);
                                ivCollected.setVisibility(View.GONE);
                            }

                        }else {
                            Toast.makeText(StoreActivity.this, "操作异常", Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
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
                dismissLoading();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                showLoading(true,"正在加载中");
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });


    }

    /**
     * 完成整个页面的布局
     **/
    private void initRVLayout() {
        //1.设置 LayoutManager
        VirtualLayoutManager layoutManager = new VirtualLayoutManager(this);
        mRV.setLayoutManager(layoutManager);

        //2.设置组件复用回收池
        RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
        mRV.setRecycledViewPool(recycledViewPool);


        //3.创建对应的 LayoutHelper

        initBannerHelper();

        initStoreNameHelper();

        initStoreLocationHelper();

        initStoreBrandHelper();

        initProjectTitleHelper();

        initStoreProjectHelper();

        initOfficerTitleHelper();

        initOfficerHelper();

        initCommentStoreTitleHelper();

        initCommentStoreHelper();

        initAllCommentTitleHelper();

        adapter = new DelegateAdapter(layoutManager, false);
        adapter.setAdapters(subAdapterList);
        mRV.setAdapter(adapter);

    }

    private void initAllCommentTitleHelper() {
        SingleLayoutHelper singleLayoutHelper_allCommentTitle = new SingleLayoutHelper();
        //只能有一个 item 项目
        singleLayoutHelper_allCommentTitle.setItemCount(1);

        ArrayList<HashMap<String, Object>> data = new ArrayList<>();

        data.add(toAllCommentMap);

        allCommentTitleAdapter = new StoreSubAdapter(this, singleLayoutHelper_allCommentTitle, 1, data, 11);
        subAdapterList.add(allCommentTitleAdapter);
    }

    private void initCommentStoreHelper() {
        LinearLayoutHelper linearLayoutHelper_commentStore = new LinearLayoutHelper();

        //只能有一个 item 项目
        linearLayoutHelper_commentStore.setItemCount(3);

        commentStoreAdapter = new StoreSubAdapter(this, linearLayoutHelper_commentStore, 3, commentData, 7);
        subAdapterList.add(commentStoreAdapter);
    }

    private void initCommentStoreTitleHelper() {
        SingleLayoutHelper singleLayoutHelper_commentStoreTitle = new SingleLayoutHelper();
        //只能有一个 item 项目
        singleLayoutHelper_commentStoreTitle.setItemCount(1);

        ArrayList<HashMap<String, Object>> data = new ArrayList<>();
        data.add(commentTitleMap);

        commentStoreTitleAdapter = new StoreSubAdapter(this, singleLayoutHelper_commentStoreTitle, 1, data, 10);
        subAdapterList.add(commentStoreTitleAdapter);
    }

    private void initOfficerHelper() {
        GridLayoutHelper columnLayoutHelper_officer = new GridLayoutHelper(4);
        //只能有一个 item 项目
        columnLayoutHelper_officer.setItemCount(4);
        officerAdapter = new StoreSubAdapter(this, columnLayoutHelper_officer, 4, officerData, 6);
        subAdapterList.add(officerAdapter);
    }

    private void initOfficerTitleHelper() {
        SingleLayoutHelper singleLayoutHelper_officerTitle = new SingleLayoutHelper();
        //只能有一个 item 项目
        singleLayoutHelper_officerTitle.setItemCount(1);

        ArrayList<HashMap<String, Object>> data = new ArrayList<>();

        data.add(officerTitleMap);

        officerTitleAdapter = new StoreSubAdapter(this, singleLayoutHelper_officerTitle, 1, data, 9);
        subAdapterList.add(officerTitleAdapter);
    }

    private void initProjectTitleHelper() {
        SingleLayoutHelper singleLayoutHelper_projectTitle = new SingleLayoutHelper();
        //只能有一个 item 项目
        singleLayoutHelper_projectTitle.setItemCount(1);

        ArrayList<HashMap<String, Object>> data = new ArrayList<>();

        data.add(projectTitleMap);

        projectTitleAdapter = new StoreSubAdapter(this, singleLayoutHelper_projectTitle, 1, data, 8);
        subAdapterList.add(projectTitleAdapter);

    }

    private void initStoreProjectHelper() {
        LinearLayoutHelper linearLayoutHelper_storeProject = new LinearLayoutHelper();

        //只能有一个 item 项目
        linearLayoutHelper_storeProject.setItemCount(3);

        storeProjectAdapter = new StoreSubAdapter(this, linearLayoutHelper_storeProject, 3, projectData, 5);
        subAdapterList.add(storeProjectAdapter);
    }

    private void initStoreBrandHelper() {
        SingleLayoutHelper singleLayoutHelper_storeBrand = new SingleLayoutHelper();

        //只能有一个 item 项目
        singleLayoutHelper_storeBrand.setItemCount(1);

        ArrayList<HashMap<String, Object>> data = new ArrayList<>();

        data.add(storeBrandMap);

        storeBrandAdapter = new StoreSubAdapter(this, singleLayoutHelper_storeBrand, 1, data, 4);
        subAdapterList.add(storeBrandAdapter);
    }

    private void initStoreLocationHelper() {
        SingleLayoutHelper singleLayoutHelper_storeLocation = new SingleLayoutHelper();

        //只能有一个 item 项目
        singleLayoutHelper_storeLocation.setItemCount(1);

        ArrayList<HashMap<String, Object>> data = new ArrayList<>();
        data.add(storeLocationMap);

        storeLocationAdapter = new StoreSubAdapter(this, singleLayoutHelper_storeLocation, 1, data, 3);
        subAdapterList.add(storeLocationAdapter);
    }

    private void initStoreNameHelper() {
        StickyLayoutHelper stickyLayoutHelper_storeName = new StickyLayoutHelper();
        stickyLayoutHelper_storeName.setOffset(0);
        stickyLayoutHelper_storeName.setStickyStart(true);
        //只能有一个 item 项目
        stickyLayoutHelper_storeName.setItemCount(1);
        ArrayList<HashMap<String, Object>> data = new ArrayList<>();

        data.add(storeNameMap);

        storeNameAdapter = new StoreSubAdapter(this, stickyLayoutHelper_storeName, 1, data, 2);

        subAdapterList.add(storeNameAdapter);
    }

    private void initBannerHelper() {
        SingleLayoutHelper singleLayoutHelper_banner = new SingleLayoutHelper();
        //设置宽高比
        //singleLayoutHelper_banner.setAspectRatio(2.36f);
        //只能有一个 item 项目
        singleLayoutHelper_banner.setItemCount(1);

        ArrayList<HashMap<String, Object>> data = new ArrayList<>();

        data.add(bannerMap);
        bannerAdapter = new StoreSubAdapter(this, singleLayoutHelper_banner, 1, data, 1);
        subAdapterList.add(bannerAdapter);
    }

    public void pullStoreOfficer(final int id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("ctype", "staff");
        map.put("cond", "{store:{id:" + id + "},job:{isDelete:1},isDelete:1}");
        map.put("jf", "staffPhoto|job");
        map.put("size", "10");
        http.doCommonPost(null, MainUrl.basePageQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                Log.e("pullStoreOfficer",resultStr.toString());
                if (!StringUtil.isBlank(resultStr)) {
                    try {
                        JSONObject obj = new JSONObject(resultStr);
                        JSONArray resultList = obj.getJSONArray("resultList");
                        officerTitleMap.put("officer_size", resultList.length());
                        officerTitleMap.put("id", id);
                        officerTitleAdapter.notifyItemChanged(0);
                        if (resultList != null && resultList.length() > 0) {
                            int size = resultList.length();

                            for (int i = 0; i < resultList.length(); i++) {
                                //最多只显示三个就行
                                if (i > 2) {
                                    continue;
                                }
                                JSONObject x = resultList.getJSONObject(i);

                                int id = x.getInt("id");
                                String name = x.getString("name");
                                String jobName = x.getJSONObject("job").getString("name");

                                String url=null;
                                String photo=x.getString("staffPhoto");
                                if(!StringUtil.isBlank(photo)){
                                    url=x.getJSONObject("staffPhoto").getString("url");
                                }

                                int jobId = x.getJSONObject("job").getInt("id");
                                OfficerBean ob = new OfficerBean();
                                ob.setId(id);
                                ob.setName(name);
                                ob.setLogo(url);
                                JobBean jb = new JobBean();
                                jb.setId(jobId);
                                jb.setName(jobName);
                                ob.setJobBean(jb);

                                HashMap<String, Object> map = new HashMap<String, Object>();
                                map.put("officer", ob);
                                officerData.add(map);
                                officerAdapter.notifyItemChanged(officerData == null ? 0 : officerData.size());

                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("pullStoreOfficerJSON",e.toString());
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("pullStoreOfficer",ex.toString());

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

    //查询店铺项目
    public void pullStoreProject(final int id) {
        //http://118.123.22.190:8010/wow/user/pageSearch$ajax.htm?ctype=goods&cond={store:{id:1},state:2,isDelete:1,isCertify:2,goodsType:{isDelete:1}}&jf=goodsType|photo
        HashMap<String, String> map = new HashMap<>();
        map.put("ctype", "goods");
        map.put("cond", "{store:{id:" + id + "},state:2,isDelete:1,isCertify:2,goodsType:{isDelete:1}}");
        map.put("jf", "goodsType|photo");

        http.doCommonPost(null, MainUrl.basePageQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {

                if (!StringUtil.isBlank(resultStr)) {
                    try {
                        JSONObject obj = new JSONObject(resultStr);

                        JSONArray resultList = obj.getJSONArray("resultList");
                        if (resultList != null && resultList.length() > 0) {
                            int size = resultList.length();
                            projectTitleMap.put("project_size", size);
                            projectTitleMap.put("id", id);
                            projectTitleAdapter.notifyItemChanged(0);
                            for (int i = 0; i < resultList.length(); i++) {

                                if (i > 2) {
                                    continue;
                                }
                                JSONObject x = resultList.getJSONObject(i);

                                int id = x.getInt("id");
                                //项目名
                                String goodsName = x.getString("goodsName");
                                //logo
                                String url=null;
                                String photo=x.getString("photo");
                                if(!StringUtil.isBlank(photo)){
                                    url=x.getJSONObject("photo").getString("url");
                                }

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

                                HashMap<String, Object> map = new HashMap<String, Object>();
                                map.put("project", pb);
                                projectData.add(map);
                                storeProjectAdapter.notifyItemInserted(projectData == null ? 0 : projectData.size());

                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("pullStoreProjectJSON",e.toString());
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

    //查询店铺信息
    public void pullStoreInfo(int id) {

        //http://118.123.22.190:8010/wow/singleSearch$ajax.htm?ctype=store&jf=storeLogo&id=1
        HashMap<String, String> map = new HashMap<>();
        map.put("ctype", "store");
        map.put("jf", "storeLogo");
        map.put("id", String.valueOf(id));
        http.doCommonPost(null, MainUrl.baseSingleQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {

                if (!StringUtil.isBlank(resultStr)) {

                    try {
                        JSONObject obj = new JSONObject(resultStr);
                        JSONObject store = obj.getJSONObject("data");
                        int id = store.getInt("id");
                        data.put("StoreId",id);
                        //店铺名
                        String name = store.getString("name");
                        data.put("StoreName",name);
                        //人均消费
                        double average_spend = store.getDouble("averageSpend");
                        //店铺介绍
                        String detail = store.getString("detail");
                        data.put("StoreMsg",detail);
                        //经纬度
                        double latitude = 0;
                        double longitude = 0;
                        String la=store.getString("latitude");
                        String lo=store.getString("longitude");
                        if(!StringUtil.isBlank(la)&&!StringUtil.isBlank(lo)){
                            latitude=Double.valueOf(la);
                            longitude=Double.valueOf(lo);
                        }

                        //评分
                        double score = store.getDouble("score");
                        //地址
                        String storeAddress = store.getString("storeAddress");

                        String telephone = store.getString("telephone");

                        String logo=null;
                        String photo=store.getString("storeLogo");
                        if(!StringUtil.isBlank(photo)){
                            logo=store.getJSONObject("storeLogo").getString("url");
                        }


                        storeNameMap.put("id", id);
                        storeNameMap.put("name", name);
                        storeNameMap.put("average_spend", average_spend);
                        storeNameMap.put("score", score);
                        storeNameMap.put("logo", logo);
                        storeNameAdapter.notifyItemChanged(0);

                        storeLocationMap.put("storeAddress", storeAddress);
                        storeLocationMap.put("longitude", longitude);
                        storeLocationMap.put("latitude", latitude);
                        storeLocationMap.put("telephone", telephone);
                        storeLocationAdapter.notifyItemChanged(0);

                        storeBrandMap.put("detail", detail);
                        storeBrandAdapter.notifyItemChanged(0);

                        commentTitleMap.put("score", score);
                        commentStoreTitleAdapter.notifyItemChanged(0);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("pullStoreInfoJSON",e.toString());
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

    //查询店铺相册
    public void pullStoreGallery(int id) {

        //http://118.123.22.190:8010/wow/pageSearch$ajax.htm?ctype=storeGallery&cond={store:{id:1}}&jf=photo

        HashMap<String, String> map = new HashMap<>();
        map.put("ctype", "storeGallery");
        map.put("cond", "{store:{id:" + id + "},isDelete:1}");
        map.put("jf", "photo");

        http.doCommonPost(null, MainUrl.basePageQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                if (!StringUtil.isBlank(resultStr)) {
                    try {
                        JSONObject obj = new JSONObject(resultStr);
                        JSONArray resultList = obj.getJSONArray("resultList");
                        if (resultList != null && resultList.length() > 0) {
                            List<String> list = new ArrayList<String>();
                            for (int i = 0; i < resultList.length(); i++) {
                                JSONObject x = resultList.getJSONObject(i);
                                String url=null;
                                String photo=x.getString("photo");
                                if(!StringUtil.isBlank(photo)){
                                    url=x.getJSONObject("photo").getString("url");
                                }

                                list.add(url);
                            }
                            bannerMap.put("banner_list", list);
                            bannerAdapter.notifyItemChanged(0);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("pullStoreGalleryJSON",e.toString());
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

    public void pullCommentInfo(final int id) {
        //http://118.123.22.190:8010/wow/user/pageSearch$ajax.htm?
        // ctype=comment&cond={store:{id:1}}&jf=reviewer|photo|commentDetail|commentPhoto
        HashMap<String, String> map = new HashMap<>();
        map.put("ctype", "comment");
        map.put("cond", "{store:{id:" + id + "}}");
        map.put("jf", "reviewer|photo|commentDetail|commentPhoto");
        map.put("orderby","insertTime desc");
        map.put("size", "10");

        http.doCommonPost(null, MainUrl.basePageQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                if (!StringUtil.isBlank(resultStr)) {

                    try {
                        JSONObject obj = new JSONObject(resultStr);
                        toAllCommentMap.put("xid", id);
                        JSONArray resultList = obj.getJSONArray("resultList");
                        int length=0;
                        if (resultList != null && resultList.length() > 0) {
                            int commentSize = resultList.length();
                            length=commentSize;
                            commentTitleMap.put("comment_size", commentSize);
                            allCommentTitleAdapter.notifyItemChanged(0);
                            for (int i = 0; i < resultList.length(); i++) {
                                if (i > 1) {
                                    continue;
                                }
                                JSONObject x = resultList.getJSONObject(i);
                                //id
                                int id = x.getInt("id");
                                //是否匿名评价
                                int isAnonymous = x.getInt("isAnonymous");
                                //评论评分
                                double score = x.getDouble("score");
                                //评论内容
                                String content = x.getString("content");
                                //评论时间
                                String insertTime = x.getString("insertTime");
                                //评论者头像

                                String reviewerLogo=null;
                                String photo=x.getJSONObject("reviewer").getString("photo");
                                if(!StringUtil.isBlank(photo)){
                                    reviewerLogo=x.getJSONObject("reviewer").getJSONObject("photo").getString("url");
                                }

                                //评论者
                                String reviewerName = x.getJSONObject("reviewer").getString("username");

                                //评论的图片列表
                                String gallery = x.getString("commentPhoto");
                                List<String> photoList = new ArrayList<>();
                                if (!StringUtil.isBlank(gallery)) {
                                    JSONArray photos = new JSONArray(gallery);
                                    if (photos != null && photos.length() > 0) {
                                        for (int j = 0; j < photos.length(); j++) {
                                            String photoUrl=photos.getJSONObject(j).getString("commentPhoto");
                                            if(!StringUtil.isBlank(photoUrl)){
                                                photoUrl=photos.getJSONObject(j).getJSONObject("commentPhoto").getString("url");
                                            }

                                            photoList.add(photoUrl);
                                        }
                                    }
                                }
                                //商家回复
                                String revertStr = x.getString("commentDetail");
                                String revert = null;
                                if (!StringUtil.isBlank(revertStr)) {
                                    JSONArray revertArray = new JSONArray(revertStr);
                                    if (revertArray != null && revertArray.length() > 0) {
                                        //差一个回复时间

                                        //回复内容
                                        revert = revertArray.getJSONObject(0).getString("content");
                                    }
                                }

                                CommentBean cb = new CommentBean();
                                cb.setId(id);
                                cb.setIsAnonymous(isAnonymous);
                                cb.setScore(score);
                                cb.setContent(content);
                                cb.setCommentTime(insertTime);

                                cb.setCommentUserLogo(reviewerLogo);
                                cb.setCommentUserName(reviewerName);
                                cb.setRevert(revert);
                                cb.setPhotoList(photoList);

                                HashMap<String, Object> map = new LinkedHashMap<>();
                                map.put("comment", cb);
                                commentData.add(map);
                                commentStoreAdapter.notifyItemInserted(commentData == null ? 0 : commentData.size());



                            }
                        }
                        toAllCommentMap.put("length", length);
                        allCommentTitleAdapter.notifyItemChanged(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("pullCommentInfoJSON",e.toString());
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
    public void reloadData() {

    }


    @OnClick({R.id.act_store_rl_open_share, R.id.back, R.id.act_store_rl_collect})
    public void storeOnClick(View view) {
        switch (view.getId()) {
            //弹出分享框
            case R.id.act_store_rl_open_share:
                data.put("type",1);
                SocialShareDialog socialShareDialog = new SocialShareDialog(this,data);
                socialShareDialog.show();
                break;

            case R.id.back:
                finish();
                break;

            //收藏或取消收藏
            case R.id.act_store_rl_collect:
                //已经登录，可以执行操作
                if (UserAuthUtil.isUserLogin()) {
                    collectClick();
                } else {
                    Toast.makeText(StoreActivity.this, "请登录后操作", Toast.LENGTH_SHORT).show();
                    ARouter.getInstance().build("/main/act/login").navigation();
                }

                break;
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        switch (requestCode) {
            case code.REQUEST_CALL_PERMISSION:
                CallUtil.makeCall(StoreActivity.this, "1008611");
                break;
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        switch (requestCode) {
            case code.REQUEST_CALL_PERMISSION:
                Toast.makeText(this, "您已经拒绝了授予拨号权限,相关功能可能无法使用", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
