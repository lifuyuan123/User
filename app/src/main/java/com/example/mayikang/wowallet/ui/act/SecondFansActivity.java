package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.FansTeamAdapter;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.UserBean;
import com.example.mayikang.wowallet.util.UserAuthUtil;
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
 * 二级代理
 */
@Route(path = "/main/act/user/SecondFans")
public class SecondFansActivity extends BaseAppcompatActivity {

    @BindView(R.id.myfans_iv_back)
    ImageView myfansIvBack;
    @BindView(R.id.second_fans_back)
    LinearLayout secondFansBack;
    @BindView(R.id.myfens_linear_centent)
    LinearLayout myfensLinearCentent;
    @BindView(R.id.second_fans_rv)
    RecyclerView secondFansRv;
    @BindView(R.id.qrefresh)
    MaterialRefreshLayout qrefresh;
    @BindView(R.id.second_fans_layout)
    LinearLayout secondFansLayout;
    @BindView(R.id.fansNo_layout)
    LinearLayout fansNoLayout;
    @BindView(R.id.activity_second_fans)
    LinearLayout activitySecondFans;

    @Autowired(name = "id")
    int id=-1;
    private HttpServiceImpl server;
    private FansTeamAdapter adapter;
    private List<UserBean> data=new ArrayList<>();
    private  int pageIndex=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        server= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        adapter=new FansTeamAdapter(this,data);
        secondFansRv.setLayoutManager(new LinearLayoutManager(this));
        secondFansRv.setAdapter(adapter);
        setListener();
    }

    private void setListener() {
        qrefresh.setLoadMore(true);
        qrefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                pullFansTeam();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                pullFansTeamMore();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        pullFansTeam();
    }



    @Override
    public int initLayout() {
        return R.layout.activity_second_fans;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.second_fans_back)
    public void onViewClicked() {
        finish();
    }
    //加载团队列表
    private void pullFansTeam() {
        data.clear();
        pageIndex=1;
        HashMap<String,String> body=new HashMap<>();
        body.put("ctype","user");
        body.put("jf","children|photo");
        body.put("cond","{parent:{id:"+id+"}}");
        body.put("pageIndex",pageIndex+"");

        server.doCommonPost(null, MainUrl.basePageQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("pullFansTeam",result.toString());
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject object=new JSONObject(result);
                        JSONArray array=object.getJSONArray("resultList");
                        if(null!=array&&array.length()>0){
                            pageIndex++;
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject beanObj=array.getJSONObject(i);
                                UserBean bean=new UserBean();
                                bean.setId(beanObj.getInt("id"));
                                bean.setUsername(beanObj.getString("username"));
                                bean.setPhone(beanObj.getString("phone"));
                                JSONObject phone=beanObj.getJSONObject("photo");
                                bean.setInsert_time(phone.getString("insert_time"));
                                bean.setUrl(phone.getString("url"));
                                data.add(bean);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }finally {
                        if(data.size()>0){
                            adapter.notifyDataSetChanged();
                            secondFansLayout.setVisibility(View.VISIBLE);
                            fansNoLayout.setVisibility(View.GONE);
                        }else {
                            fansNoLayout.setVisibility(View.VISIBLE);
                            secondFansLayout.setVisibility(View.GONE);
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
                qrefresh.finishRefresh();
                qrefresh.finishRefreshLoadMore();
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


    //上拉刷新
    private void pullFansTeamMore(){
        HashMap<String,String> body=new HashMap<>();
        body.put("ctype","user");
        body.put("jf","children|photo");
        body.put("orderby","insertTime desc");
        body.put("cond","{parent:{id:"+id+"}}");
        body.put("pageIndex",pageIndex+"");

        server.doCommonPost(null, MainUrl.basePageQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {

                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject object=new JSONObject(result);
                        JSONArray array=object.getJSONArray("resultList");
                        if(null!=array&&array.length()>0){
                            pageIndex++;
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject beanObj=array.getJSONObject(i);
                                UserBean bean=new UserBean();
                                bean.setId(beanObj.getInt("id"));
                                bean.setUsername(beanObj.getString("username"));
                                bean.setPhone(beanObj.getString("phone"));
                                JSONObject phone=beanObj.getJSONObject("photo");
                                bean.setInsert_time(phone.getString("insert_time"));
                                bean.setUrl(phone.getString("url"));
                                data.add(bean);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }finally {
                        if(data.size()>0){
                            adapter.notifyDataSetChanged();
                            secondFansLayout.setVisibility(View.VISIBLE);
                            fansNoLayout.setVisibility(View.GONE);
                        }else {
                            fansNoLayout.setVisibility(View.VISIBLE);
                            secondFansLayout.setVisibility(View.GONE);
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
                qrefresh.finishRefresh();
                qrefresh.finishRefreshLoadMore();
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
