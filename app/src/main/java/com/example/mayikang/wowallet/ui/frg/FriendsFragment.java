package com.example.mayikang.wowallet.ui.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.example.mayikang.wowallet.adapter.MyAdapter;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.TransferBean;
import com.example.mayikang.wowallet.modle.javabean.UserBean;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.util.ListViewUtil;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by mayikang on 17/5/8.
 */

public class FriendsFragment extends Fragment {
    @BindView(R.id.myfriend_listview)
    ListView listView;
    @BindView(R.id.friend_qrefresh)
    MaterialRefreshLayout friendQrefresh;
    @BindView(R.id.friends_layout)
    LinearLayout friendsLayout;
    @BindView(R.id.friendsNo_layout)
    LinearLayout friendsNoLayout;
    private List<TransferBean> data = new ArrayList<>();
    private MyAdapter adapter;
    private Unbinder bind;
    private HttpServiceImpl service;
    private int pageIndex = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_friends, null);
        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        bind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        pullFriends();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter=new MyAdapter(data,getActivity());
        listView.setAdapter(adapter);
        friendQrefresh.setLoadMore(true);
        friendQrefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                pullFriends();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                pullFriendsMore();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TransferBean bean= (TransferBean) adapter.getItem(position);
                ARouter.getInstance().build("/main/act/user/bill_detail").withInt("id",bean.getId()).navigation();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (bind != null) {
            bind.unbind();
        }
    }

    @OnClick({R.id.right_relative_address_list, R.id.right_relative_address_add})
    public void onViewClick(View view) {
        switch (view.getId()) {
            //打开通讯录
            case R.id.right_relative_address_list:
                ARouter.getInstance().build("/main/act/user/address_book").navigation();
                break;
            //打开添加好友
            case R.id.right_relative_address_add:
                ARouter.getInstance().build("/main/act/user/add_friends").navigation();
                break;
        }
    }


    //加载好友转账记录
    private void pullFriends() {
        data.clear();
        pageIndex = 1;
        HashMap<String, String> body = new HashMap<>();
        body.put("jf", "incomeUser|expenditureUser|photo");
        body.put("pageIndex", pageIndex + "");
        service.doCommonPost(null, MainUrl.FriendsMoneyUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("账单列表",result.toString());
                if (!StringUtil.isBlank(result)) {

                    try {
                        JSONObject object = new JSONObject(result);

                       /* //判断登录异常的情况
                        if(object.has("result") && object.has("msg")){
                            boolean re=object.getBoolean("result");

                            if(!re){
                                    //非正常情况下导致 token 改变，强制登录

                                    //清除本地 token
                                    if (SPFUtil.contains(Tag.TAG_TOKEN)) {
                                        SPFUtil.removeOne(Tag.TAG_TOKEN);
                                    }
                                    //清除本地用户信息
                                    if (SPFUtil.contains(Tag.TAG_USER)) {
                                        SPFUtil.removeOne(Tag.TAG_USER);
                                    }
                                    ARouter.getInstance().build("/main/act/login").navigation();
                            }
                        }*/


                        JSONArray array = object.getJSONArray("resultList");
                        if (null != array && array.length() > 0) {
                            pageIndex++;
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject datas = array.getJSONObject(i);
                                TransferBean bean = new TransferBean();
                                bean.setId(datas.getInt("id"));
                                bean.setAmount(datas.getInt("amount"));
                                bean.setDesc(datas.getString("desc_"));
                                bean.setInsertTime(datas.getString("insertTime"));
                                bean.setfType(datas.getInt("fType"));
                                bean.setRemark(datas.getString("remark"));
                                JSONObject expenditureUser = datas.getJSONObject("expenditureUser");
                                UserBean exBean = new UserBean();
                                exBean.setId(expenditureUser.getInt("id"));
                                exBean.setUsername(expenditureUser.getString("username"));
                                exBean.setUrl(expenditureUser.getJSONObject("photo").getString("url"));
                                bean.setExpenditureUser(exBean);
                                JSONObject incomeUser=datas.getJSONObject("incomeUser");
                                UserBean inBean=new UserBean();
                                inBean.setId(incomeUser.getInt("id"));
                                inBean.setUsername(incomeUser.getString("username"));
                                inBean.setUrl(incomeUser.getJSONObject("photo").getString("url"));
                                bean.setIncomeUser(inBean);
                                data.add(bean);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("pullJSONException",e.toString());
                    } finally {
                        if (data.size() > 0) {
                            adapter.notifyDataSetChanged();
                            friendsLayout.setVisibility(View.VISIBLE);
                            friendsNoLayout.setVisibility(View.GONE);
                        } else {
                            friendsLayout.setVisibility(View.GONE);
                            friendsNoLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("onError",ex.toString());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                friendQrefresh.finishRefreshLoadMore();
                friendQrefresh.finishRefresh();
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


    //加载好友转账记录
    private void pullFriendsMore() {
        HashMap<String, String> body = new HashMap<>();
        body.put("jf", "incomeUser|expenditureUser|photo");
        body.put("pageIndex", pageIndex + "");
        service.doCommonPost(null, MainUrl.FriendsMoneyUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("pullFriends", result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {

                        JSONObject object = new JSONObject(result);
                        JSONArray array = object.getJSONArray("resultList");
                        if (null != array && array.length() > 0) {
                            pageIndex++;
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject datas = array.getJSONObject(i);
                                TransferBean bean = new TransferBean();
                                bean.setId(datas.getInt("id"));
                                bean.setAmount(datas.getInt("amount"));
                                bean.setDesc(datas.getString("desc_"));
                                bean.setInsertTime(datas.getString("insertTime"));
                                bean.setfType(datas.getInt("fType"));
                                bean.setRemark(datas.getString("remark"));
                                JSONObject expenditureUser = datas.getJSONObject("expenditureUser");
                                UserBean exBean = new UserBean();
                                exBean.setId(expenditureUser.getInt("id"));
                                exBean.setUrl(expenditureUser.getJSONObject("photo").getString("url"));
                                bean.setExpenditureUser(exBean);
                                JSONObject incomeUser=datas.getJSONObject("incomeUser");
                                UserBean inBean=new UserBean();
                                inBean.setId(incomeUser.getInt("id"));
                                inBean.setUrl(incomeUser.getJSONObject("photo").getString("url"));
                                bean.setIncomeUser(inBean);
                                data.add(bean);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (data.size() > 0) {
                            adapter.notifyDataSetChanged();
                            friendsLayout.setVisibility(View.VISIBLE);
                            friendsNoLayout.setVisibility(View.GONE);
                        } else {
                            friendsLayout.setVisibility(View.GONE);
                            friendsNoLayout.setVisibility(View.VISIBLE);
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
                friendQrefresh.finishRefreshLoadMore();
                friendQrefresh.finishRefresh();
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
