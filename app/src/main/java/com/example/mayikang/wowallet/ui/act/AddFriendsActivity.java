package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.SearchFriendsAdapter;
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
 * 添加好友页面
 */

@Route(path = "/main/act/user/add_friends")
public class AddFriendsActivity extends BaseAppcompatActivity {

    @BindView(R.id.add_friends_back)
    LinearLayout addFriendsBack;
    @BindView(R.id.add_friends_edit_serch)
    EditText addFriendsEditSerch;
    @BindView(R.id.shopsearch_iv_delete)
    ImageView shopsearchIvDelete;
    @BindView(R.id.add_friends_rcv)
    RecyclerView addFriendsRcv;
    @BindView(R.id.add_friends_rl)
    RelativeLayout addFriendsRl;
    @BindView(R.id.add_friends_nosearch_layout)
    LinearLayout addFriendsNosearchLayout;
    @BindView(R.id.seach_friends_refresh)
    MaterialRefreshLayout seachFriendsRefresh;
    @BindView(R.id.add_friends_search)
    TextView addFriendsSearch;

    private List<UserBean> data = new ArrayList<>();
    private SearchFriendsAdapter adapter;
    private HttpServiceImpl service;
    private String edt="";
    private int pageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        adapter = new SearchFriendsAdapter(data, this);
        addFriendsRcv.setLayoutManager(new LinearLayoutManager(this));
        addFriendsRcv.setAdapter(adapter);
        setListener();
    }


    //设置监听
    private void setListener() {

        //刷新的监听
        seachFriendsRefresh.setLoadMore(true);
        seachFriendsRefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
               if(!TextUtils.isEmpty(edt)){
                   pullSearchData();
               }else {
                   seachFriendsRefresh.finishRefresh();
               }
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if(!TextUtils.isEmpty(edt)){
                    pullSearchDataMore();
                }else {
                    seachFriendsRefresh.finishRefreshLoadMore();
                }

            }
        });

        //adapter的监听
        adapter.setListener(new SearchFriendsAdapter.OnclickCallBack() {
            @Override
            public void itemClick(UserBean bean) {
                //点击item
                ARouter.getInstance().build("/main/act/user/AddFriendMsg").withObject("bean",bean).navigation();

            }


        });

        addFriendsEditSerch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               if(s.toString().length()>0){
                   shopsearchIvDelete.setVisibility(View.VISIBLE);
               }else {
                   shopsearchIvDelete.setVisibility(View.GONE);
                   data.clear();
                   adapter.notifyDataSetChanged();
               }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    @Override
    public int initLayout() {
        return R.layout.activity_add_friends;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.add_friends_back, R.id.shopsearch_iv_delete,R.id.add_friends_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_friends_back:
                finish();
                break;
            case R.id.shopsearch_iv_delete:
                addFriendsEditSerch.setText("");
                edt="";
                data.clear();
                adapter.notifyDataSetChanged();
                break;
            case R.id.add_friends_search:
                if(!TextUtils.isEmpty(addFriendsEditSerch.getText().toString())){
                    edt =addFriendsEditSerch.getText().toString();
                    pullSearchData();
                }
                break;
        }
    }


    //加载搜索的数据
    private void pullSearchData() {
        data.clear();
        pageIndex = 1;
        HashMap<String, String> body = new HashMap<>();
        body.put("ctype","user");
        body.put("cond","{username:"+"\""+edt+"\""+",isDelete:1,isAdmin:1,isLocked:2,type:2,}");
        body.put("qtype","2");
        body.put("jf","photo");
        body.put("pageIndex",pageIndex+"");

        service.doCommonPost(null, MainUrl.basePageQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (!StringUtil.isBlank(result)) {
                    Log.e("onSuccess",result.toString());
                    try {
                        JSONObject obj = new JSONObject(result);
                        JSONArray arry=obj.getJSONArray("resultList");
                        if(null!=arry&&arry.length()>0){
                            for (int i = 0; i <arry.length() ; i++) {
                                JSONObject bean=arry.getJSONObject(i);
                                if(bean.getInt("id")== UserAuthUtil.getUserId()){
                                    continue;
                                }
                                UserBean user=new UserBean();
                                user.setEmail(bean.getString("email"));
                                user.setId(bean.getInt("id"));
                                user.setSex(bean.getInt("sex"));
                                user.setRealName(bean.getString("realName"));
                                user.setUsername(bean.getString("username"));
                                user.setPhone(bean.getString("phone"));
                                JSONObject photo=bean.getJSONObject("photo");
                                user.setUrl(photo.getString("url"));
                                data.add(user);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("JSONException",e.toString());
                    } finally {
                        if (data.size() > 0) {
                            addFriendsRl.setVisibility(View.VISIBLE);
                            addFriendsNosearchLayout.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        } else {
                            addFriendsRl.setVisibility(View.GONE);
                            addFriendsNosearchLayout.setVisibility(View.VISIBLE);
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
                seachFriendsRefresh.finishRefreshLoadMore();
                seachFriendsRefresh.finishRefresh();
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


    //下拉加载更多
    private void pullSearchDataMore() {
        HashMap<String, String> body = new HashMap<>();
        body.put("ctype","user");
        body.put("cond","{username:"+"\'"+addFriendsEditSerch.getText().toString()+"\'"+",isDelete:1,isAdmin:1,isLocked:2,type:2,}");
        body.put("qtype","2");
        body.put("jf","photo");
        body.put("pageIndex",pageIndex+"");

        service.doCommonPost(null, MainUrl.basePageQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (StringUtil.isBlank(result)) {
                    try {
                        JSONObject obj = new JSONObject(result);
                        pageIndex++;
                        JSONArray arry=obj.getJSONArray("resultList");
                        if(null!=arry&&arry.length()>0){
                            for (int i = 0; i <arry.length() ; i++) {
                                JSONObject bean=arry.getJSONObject(i);
                                if(bean.getInt("id")== UserAuthUtil.getUserId()){
                                    continue;
                                }
                                UserBean user=new UserBean();
                                user.setEmail(bean.getString("email"));
                                user.setId(bean.getInt("id"));
                                user.setSex(bean.getInt("sex"));
                                user.setRealName(bean.getString("realName"));
                                user.setUsername(bean.getString("username"));
                                user.setPhone(bean.getString("phone"));
                                JSONObject photo=bean.getJSONObject("photo");
                                user.setUrl(photo.getString("url"));
                                data.add(user);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (data.size() > 0) {
                            addFriendsRl.setVisibility(View.VISIBLE);
                            addFriendsNosearchLayout.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        } else {
                            addFriendsRl.setVisibility(View.GONE);
                            addFriendsNosearchLayout.setVisibility(View.VISIBLE);
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
                seachFriendsRefresh.finishRefreshLoadMore();
                seachFriendsRefresh.finishRefresh();
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
