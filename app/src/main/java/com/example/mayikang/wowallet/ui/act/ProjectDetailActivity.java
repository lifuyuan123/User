package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.ProjectDetailAdapter;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.ImageBean;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.ui.widget.rv.WrapLinearLayoutManager;
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
 * 项目图文详情页面
 */
@Route(path = "/main/act/project_detail")
public class ProjectDetailActivity extends BaseAppcompatActivity {

    @BindView(R.id.rv)
    RecyclerView mRV;
    @BindView(R.id.web)
    WebView web;
    @BindView(R.id.project_detail_goto)
    RelativeLayout project_detail_goto;
    @BindView(R.id.back)
    RelativeLayout back;
    private ProjectDetailAdapter adapter;
    private List<ImageBean> data = new ArrayList<>();

    @Autowired(name = "id")
    public int id = -1;
    @Autowired(name = "flag")
    int flag = 0;

    private int storeId = -1;

    private HttpServiceImpl http;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        http = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        initView();
        initRV();
        initWeb();

        if (-1 != id) {
            pullProductImages(id);
            pullProductInfo(id);
        }

    }

    //初始化布局
    private void initView() {
        if (flag == -1) {
            project_detail_goto.setVisibility(View.VISIBLE);
        }
    }


    private void initWeb() {

        web.getSettings().setJavaScriptEnabled(true);

        web.setWebChromeClient(new WebChromeClient() {

        });

        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });

        String webS = "";

        web.loadData(webS, "text/html;charset=utf-8", null);
    }

    private void initRV() {

        WrapLinearLayoutManager manager = new WrapLinearLayoutManager(this);
        mRV.setLayoutManager(manager);
        adapter = new ProjectDetailAdapter(this, data);
        mRV.setAdapter(adapter);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_project_detail;
    }

    @Override
    public void reloadData() {

    }

    //查询项目相册
    public void pullProductImages(int id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("ctype", "goodsGallery");
        map.put("cond", "{isDelete:1,goods:{id:" + id + "}}");
        map.put("jf", "photo");

        http.doCommonPost(null, MainUrl.basePageQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                if (!StringUtil.isBlank(resultStr)) {
                    Log.e("pullProductImages", resultStr.toString());

                    try {
                        JSONObject obj = new JSONObject(resultStr);

                        JSONArray resultList = obj.getJSONArray("resultList");
                        if (resultList != null && resultList.length() > 0) {
                            for (int i = 0; i < resultList.length(); i++) {

                                JSONObject x = resultList.getJSONObject(i);
                                int id = x.getInt("id");
                                String url = x.getJSONObject("photo").getString("url");
                                JSONObject photoObj=x.getJSONObject("photo");
                                ImageBean ib = new ImageBean();
                                ib.setWidth(photoObj.getDouble("width"));
                                ib.setHeight(photoObj.getDouble("height"));
                                ib.setId(id);
                                ib.setUrl(url);
                                data.add(ib);

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("pullProductImages", e.toString());
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

    public void pullProductInfo(int id) {
        //http://118.123.22.190:8010/wow/singleSearch$ajax.htm?ctype=goods&id=1
        HashMap<String, String> map = new HashMap<>();
        map.put("ctype", "goods");
        map.put("id", String.valueOf(id));
        map.put("jf", "store");
        http.doCommonPost(null, MainUrl.baseSingleQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                if (!StringUtil.isBlank(resultStr)) {
                    Log.e("pullProductInfo", resultStr.toString());
                    try {
                        JSONObject obj = new JSONObject(resultStr);
                        JSONObject data = obj.getJSONObject("data");
                        String detail = data.getString("detail");
                        web.loadData(detail, "text/html;charset=utf-8", null);
                        JSONObject store = data.getJSONObject("store");
                        storeId = store.getInt("id");
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


    @OnClick({R.id.back, R.id.project_detail_goto})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.project_detail_goto:
                if(storeId!=-1){
                    ARouter.getInstance().build("/main/act/store").withInt("id",storeId).navigation();
                    finish();
                }
                break;
        }
    }
}
