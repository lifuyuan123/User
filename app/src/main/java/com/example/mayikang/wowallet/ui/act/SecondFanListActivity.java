package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.MyFansAdapter;
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
@Route(path = "/main/act/user/SecondFanListActivity")
public class SecondFanListActivity extends BaseAppcompatActivity {

    @BindView(R.id.fans_list_back)
    LinearLayout fansListBack;
    @BindView(R.id.myfens_linear_centent)
    LinearLayout myfensLinearCentent;
    @BindView(R.id.fans_list_rv)
    RecyclerView fansListRv;
    @BindView(R.id.fans_list_qrefresh)
    MaterialRefreshLayout fansListQrefresh;
    @BindView(R.id.fans_data_layout)
    LinearLayout fansDataLayout;
    @BindView(R.id.fans_list_No_layout)
    LinearLayout fansListNoLayout;
    @BindView(R.id.activity_second_fan_list)
    LinearLayout activitySecondFanList;
    @Autowired(name = "id")
    int id=-1;
    private MyFansAdapter adapter;
    private List<UserBean> data = new ArrayList<>();
    public HttpServiceImpl http;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        http = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        adapter = new MyFansAdapter(data, this,2);
        fansListRv.setLayoutManager(new LinearLayoutManager(this));
        fansListRv.setAdapter(adapter);
        setListener();
    }

    //设置监听
    private void setListener() {
        fansListQrefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                pullUserFans();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                pullUserFansMore();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        pullUserFans();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_second_fan_list;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.fans_list_back)
    public void onViewClicked() {
        finish();
    }

    //请求粉丝数据
    public void pullUserFans() {
        data.clear();
        HashMap<String, String> body = new HashMap<>();
        body.put("parentId", UserAuthUtil.getUserId() + "");
        body.put("jf", "children|photo");

        http.doCommonPost(null, MainUrl.getFansList, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("pullUserFans", result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray userList = object.getJSONArray("userList");
                        if (null != userList && userList.length() > 0) {
                            for (int i = 0; i < userList.length(); i++) {
                                JSONObject userBean = userList.getJSONObject(i);
                                UserBean bean = new UserBean();
                                bean.setId(userBean.getInt("id"));
                                bean.setUsername(userBean.getString("username"));
                                bean.setUserFans(userBean.getJSONArray("children").length());
                                bean.setUrl(userBean.getJSONObject("photo").getString("url"));
                                data.add(bean);
                            }
                        }
                    } catch (JSONException e) {
                        Log.e("pullUserFans", e.toString());
                        e.printStackTrace();
                    } finally {
                        if (data.size() > 0) {
                            adapter.notifyDataSetChanged();
                            fansDataLayout.setVisibility(View.VISIBLE);
                            fansListNoLayout.setVisibility(View.GONE);
                        } else {
                            fansListNoLayout.setVisibility(View.VISIBLE);
                            fansDataLayout.setVisibility(View.GONE);
                        }

                    }
                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("pullUserFanse", ex.toString());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                fansListQrefresh.finishRefresh();
                fansListQrefresh.finishRefreshLoadMore();
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

    //加载更多
    public void pullUserFansMore() {
        HashMap<String, String> body = new HashMap<>();
        body.put("parentId", UserAuthUtil.getUserId() + "");
        body.put("jf", "children");
        // body.put("pageIndex",pageIndex+"");

        http.doCommonPost(null, MainUrl.getFansList, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray array = object.getJSONArray("userList");
                        if (null != array && array.length() > 0) {
                            //pageIndex++;
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject beanObj = array.getJSONObject(i);
                                UserBean bean = new UserBean();
                                bean.setUserFans(beanObj.getJSONArray("children").length());
                                bean.setId(beanObj.getInt("id"));
                                bean.setUsername(beanObj.getString("username"));
                                JSONObject photo = beanObj.getJSONObject("photo");
                                bean.setUrl(photo.getString("url"));
                                data.add(bean);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (data.size() > 0) {
                            adapter.notifyDataSetChanged();
                            fansDataLayout.setVisibility(View.VISIBLE);
                            fansListNoLayout.setVisibility(View.GONE);
                        } else {
                            fansListNoLayout.setVisibility(View.VISIBLE);
                            fansDataLayout.setVisibility(View.GONE);
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
                fansListQrefresh.finishRefresh();
                fansListQrefresh.finishRefreshLoadMore();
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
