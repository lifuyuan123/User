package com.example.mayikang.wowallet.ui.act;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.MyShopsAdapter;
import com.example.mayikang.wowallet.adapter.SearchAdapter;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.StoreBean;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.db.entity.SearchRecordTbl;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.base.util.TimeUtil;
import com.sctjsj.basemodule.core.router_service.IHttpService;
import com.sctjsj.basemodule.core.router_service.ISearchRecordService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/main/act/shopSearch")
public class ShopSearchActivity extends BaseAppcompatActivity {
    @BindView(R.id.shopsearch_edit_serch)
    EditText shopsearchEditSerch;
    @BindView(R.id.search_search)
    ListView searchSearch;
    @BindView(R.id.shops_iv_back)
    ImageView shopsIvBack;
    @BindView(R.id.shopsearch_tv_search)
    TextView shopsearchTvSearch;
    @BindView(R.id.shopsearch_iv_delete)
    ImageView shopsearchIvDelete;
    @BindView(R.id.shopsearch_linear_back)
    LinearLayout shopsearchLinearBack;
    @BindView(R.id.shopsearch_nosearch_layout)
    LinearLayout shopsearchNosearchLayout;
    @BindView(R.id.shopsearch_shop_list)
    ListView shopsearchShopList;
    @BindView(R.id.shopsearch_shop_layout_list)
    LinearLayout shopsearchShopLayoutList;
    @BindView(R.id.shopsearch_History_layout)
    LinearLayout shopsearchHistoryLayout;
    @BindView(R.id.mater_refresh)
    MaterialRefreshLayout materRefresh;
    private List<SearchRecordTbl> strings = new ArrayList<>();
    private SearchAdapter adapter;
    private ISearchRecordService searchRecordService;
    private IHttpService service;
    private List<StoreBean> data = new ArrayList<>();
    private MyShopsAdapter adapterShop;
    private int pageIndex = 1;
    private String search = "";
    private boolean flag=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchRecordService = (ISearchRecordService) ARouter.getInstance().build("/basemodule/service/search_record").navigation();
        service = (IHttpService) ARouter.getInstance().build("/basemodule/service/http").navigation();
        materRefresh.setLoadMore(true);
        init();
        setListener();
    }





    private void pullSearchShop(String search){
        data.clear();
        pageIndex = 1;
        HashMap<String,String> body=new HashMap<>();
        body.put("str",search.trim());
        body.put("jf", "storeLogo");
        body.put("pageIndex", String.valueOf(pageIndex));
        service.doCommonPost(null, MainUrl.GoosNameOrShopName, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("pullSeachShop",result.toString());
                    try {
                        JSONObject obj = new JSONObject(result);
                            JSONArray arr = obj.getJSONArray("resultList");
                            if (null != arr && arr.length() > 0) {
                                pageIndex++;
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject shopObj = arr.getJSONObject(i);
                                    StoreBean bean = new StoreBean();
                                    bean.setId(shopObj.getInt("id"));
                                    bean.setName(shopObj.getString("name"));
                                    bean.setDetail(shopObj.getString("detail"));
                                    bean.setPopularity(shopObj.getInt("popularity"));
                                    bean.setLogo(shopObj.getJSONObject("storeLogo").getString("url"));
                                    String lo = shopObj.getString("longitude");
                                    String la = shopObj.getString("latitude");
                                    double longitude = 0;
                                    double latitude = 0;
                                    if (!StringUtil.isBlank(lo) && !StringUtil.isBlank(la)) {
                                        longitude = Double.valueOf(lo);
                                        latitude = Double.valueOf(la);
                                    }
                                    bean.setLongitude(longitude);
                                    bean.setLatitude(latitude);
                                    bean.setScore(shopObj.getDouble("score"));
                                    data.add(bean);
                                }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("pullSeachShop",e.toString());

                    } finally {
                        if (data.size() > 0) {
                            shopsearchShopLayoutList.setVisibility(View.VISIBLE);
                            shopsearchHistoryLayout.setVisibility(View.GONE);
                            shopsearchNosearchLayout.setVisibility(View.GONE);
                            adapterShop.notifyDataSetChanged();
                        } else {
                            shopsearchNosearchLayout.setVisibility(View.VISIBLE);
                            shopsearchHistoryLayout.setVisibility(View.GONE);
                            shopsearchShopLayoutList.setVisibility(View.GONE);
                        }
                    }

            }

            @Override
            public void onError(Throwable ex) {
                Log.e("pullSeachShop",ex.toString());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                materRefresh.finishRefreshLoadMore();
                materRefresh.finishRefresh();
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

    private void pullSeachShopMore(String search){
        HashMap<String,String> body=new HashMap<>();
        body.put("str",search.trim());
        body.put("jf", "storeLogo");
        body.put("pageIndex", String.valueOf(pageIndex));
        service.doCommonPost(null, MainUrl.GoosNameOrShopName, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("pullSeachShop",result.toString());
                try {
                    JSONObject obj = new JSONObject(result);
                    JSONArray arr = obj.getJSONArray("resultList");
                    if (null != arr && arr.length() > 0) {
                        pageIndex++;
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject shopObj = arr.getJSONObject(i);
                            StoreBean bean = new StoreBean();
                            bean.setId(shopObj.getInt("id"));
                            bean.setName(shopObj.getString("name"));
                            bean.setDetail(shopObj.getString("detail"));
                            bean.setPopularity(shopObj.getInt("popularity"));
                            bean.setLogo(shopObj.getJSONObject("storeLogo").getString("url"));
                            String lo = shopObj.getString("longitude");
                            String la = shopObj.getString("latitude");
                            double longitude = 0;
                            double latitude = 0;
                            if (!StringUtil.isBlank(lo) && !StringUtil.isBlank(la)) {
                                longitude = Double.valueOf(lo);
                                latitude = Double.valueOf(la);
                            }
                            bean.setLongitude(longitude);
                            bean.setLatitude(latitude);
                            bean.setScore(shopObj.getDouble("score"));
                            data.add(bean);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("pullSeachShop",e.toString());

                } finally {
                    if (data.size() > 0) {
                        shopsearchShopLayoutList.setVisibility(View.VISIBLE);
                        shopsearchHistoryLayout.setVisibility(View.GONE);
                        shopsearchNosearchLayout.setVisibility(View.GONE);
                        adapterShop.notifyDataSetChanged();
                    } else {
                        shopsearchNosearchLayout.setVisibility(View.VISIBLE);
                        shopsearchHistoryLayout.setVisibility(View.GONE);
                        shopsearchShopLayoutList.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onError(Throwable ex) {
                Log.e("pullSeachShop",ex.toString());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                materRefresh.finishRefreshLoadMore();
                materRefresh.finishRefresh();
                if(flag){
                    dismissLoading();
                }
                flag=false;
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                if(flag){
                    showLoading(true,"搜索中....");
                }
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });
    }



    //设置监听
    private void setListener() {
        searchSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchRecordTbl table = (SearchRecordTbl) adapter.getItem(position);
                table.setInsertTime(TimeUtil.getTimestamp());
                searchRecordService.insertRecord(table);
                findHistory();
                search = table.getContent();
                flag=true;
                pullSearchShop(search);
            }
        });
        searchSearch.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShopSearchActivity.this);
                builder.show();
                return true;
            }
        });

        //输入内容监听
        shopsearchEditSerch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                shopsearchShopLayoutList.setVisibility(View.GONE);
                shopsearchHistoryLayout.setVisibility(View.VISIBLE);
                shopsearchNosearchLayout.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && !TextUtils.isEmpty(s)) {
                    shopsearchIvDelete.setVisibility(View.VISIBLE);
                } else {
                    shopsearchIvDelete.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        shopsearchEditSerch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    shopsearchShopLayoutList.setVisibility(View.GONE);
                    shopsearchHistoryLayout.setVisibility(View.VISIBLE);
                    shopsearchNosearchLayout.setVisibility(View.GONE);
                }

            }
        });


        materRefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                pullSearchShop(search);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                pullSeachShopMore(search);
            }
        });


        shopsearchShopList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StoreBean bean= (StoreBean) adapterShop.getItem(position);
                ARouter.getInstance().build("/main/act/store").withInt("id",bean.getId()).navigation();
            }
        });

    }

    @Override
    public int initLayout() {
        return R.layout.activity_shop_search;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.shopsearch_linear_back, R.id.shopsearch_tv_search, R.id.shop_search_removeall, R.id.shopsearch_iv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.shopsearch_linear_back://点击返回建
                finish();
                break;
            case R.id.shopsearch_tv_search://点击收索按钮
                if (!TextUtils.isEmpty(shopsearchEditSerch.getText().toString())) {
                    SearchRecordTbl s = new SearchRecordTbl();
                    s.setContent(shopsearchEditSerch.getText().toString());
                    s.setInsertTime(TimeUtil.getTimestamp());
                    searchRecordService.insertRecord(s);
                    findHistory();
                    String str=shopsearchEditSerch.getText().toString();
                    if(!TextUtils.isEmpty(str)){
                        search=str;
                        flag=true;
                        pullSearchShop(search);
                    }else {
                        Toast.makeText(this,"请输入店铺关键字",Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            case R.id.shop_search_removeall://清空搜索记录
                searchRecordService.clearAllRecord();
                adapter.moveAlldata();
                break;
            case R.id.shopsearch_iv_delete://点击删除搜索框中的内容
                shopsearchEditSerch.setText("");
                shopsearchIvDelete.setVisibility(View.GONE);
                break;
        }
    }


    //初始化
    private void init() {
        //历史记录设置adapter
        adapter = new SearchAdapter(strings, this);
        searchSearch.setAdapter(adapter);
        findHistory();

        //店铺列表设置adapter
        adapterShop = new MyShopsAdapter(data, this);
        shopsearchShopList.setAdapter(adapterShop);

    }


    //查找本地历史记录
    private void findHistory() {
        List<SearchRecordTbl> data = searchRecordService.findAllRecords();
        if (null != data && data.size() > 0) {
            strings.clear();
            strings.addAll(data);
            adapter.notifyDataSetChanged();
        }
    }


}
