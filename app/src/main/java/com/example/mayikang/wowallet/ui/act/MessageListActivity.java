package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.MessageAdapter;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.MessageBean;
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

import static java.util.Collections.sort;

@Route(path = "/main/act/message_list")
public class MessageListActivity extends BaseAppcompatActivity implements MessageAdapter.MessageListCallBack {

    @BindView(R.id.message_back_lr)
    RelativeLayout messageBackLr;
    @BindView(R.id.message_title_txt)
    TextView messageTitleTxt;
    @BindView(R.id.message_listView)
    RecyclerView messageListView;
    @BindView(R.id.message_refresh)
    MaterialRefreshLayout messageRefresh;
    @BindView(R.id.messageList_layput)
    RelativeLayout messageListLayput;
    @BindView(R.id.messageNo_layout)
    LinearLayout messageNoLayout;
    private MessageAdapter adapter;
    private HttpServiceImpl service;
    private int pageIndex = 1;
    private List<MessageBean> data = new ArrayList<>();
    private boolean flag=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        setAdapter();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMssageData();
    }


    //设置监听
    private void setListener() {
        messageRefresh.setLoadMore(true);
        messageRefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                flag=false;
                getMssageData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                loadDataMore();
            }
        });
    }

    //设置适配器
    private void setAdapter() {
        adapter = new MessageAdapter(data, MessageListActivity.this);
        adapter.setListener(this);
        messageListView.setLayoutManager(new LinearLayoutManager(this));
        messageListView.setAdapter(adapter);
    }

    @Override
    public int initLayout() {
        return R.layout.activity_message_list;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.message_back_lr)
    public void onViewClicked() {
        finish();
    }


    //加载消息数据
    private void getMssageData() {
        data.clear();
        pageIndex = 1;
        HashMap<String, String> body = new HashMap<>();
        body.put("ctype", "message");
        body.put("cond", "{touser:{id:" + UserAuthUtil.getUserId() + "},deletestatusTo:2}");
        body.put("jf", "fromuserId|photo");
        body.put("orderby","insertTime desc");
        body.put("pageIndex", pageIndex + "");
        service.doCommonPost(body, MainUrl.basePageQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("getMssageData", result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONArray array = object.getJSONArray("resultList");
                        if (null != array && array.length() > 0) {
                            pageIndex++;
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject objBean = array.getJSONObject(i);
                                MessageBean bean = new MessageBean();
                                bean.setType(objBean.getInt("type"));
                                bean.setId(objBean.getInt("id"));
                                bean.setContent(objBean.getString("content"));
                                bean.setStatus(objBean.getInt("status"));
                                bean.setInsert_time(objBean.getString("insertTime"));
                                bean.setTitle(objBean.getString("title"));
                                bean.setDeletestatus_to(objBean.getInt("deletestatusTo"));
                                JSONObject userBean = objBean.getJSONObject("fromuserId");
                                UserBean userData=new UserBean();
                                userData.setId(userBean.getInt("id"));
                                bean.setBean(userData);

                                if(!userBean.isNull("photo")){
                                    JSONObject photo = userBean.getJSONObject("photo");
                                    bean.setUrl(photo.getString("url"));
                                }else {
                                    bean.setUrl("null");
                                }
                                data.add(bean);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("getMssageData",e.toString());
                    } finally {
                        if(data.size()>0){
                            adapter.notifyDataSetChanged();
                            messageListLayput.setVisibility(View.VISIBLE);
                            messageNoLayout.setVisibility(View.GONE);
                        }else {
                            messageListLayput.setVisibility(View.GONE);
                            messageNoLayout.setVisibility(View.VISIBLE);
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
                messageRefresh.finishRefreshLoadMore();
                messageRefresh.finishRefresh();
                if(flag){
                  dismissLoading();
                }
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                if(flag){
                    showLoading(true,"加载中...");
                }
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });

    }

    //上拉加载更多
    private void loadDataMore() {
        HashMap<String, String> body = new HashMap<>();
        body.put("ctype", "message");
        body.put("cond", "{touser:" + UserAuthUtil.getUserId() + ",deletestatusTo:2}");
        body.put("pageIndex", pageIndex + "");
        body.put("orderby","insertTime desc");
        service.doCommonPost(body, MainUrl.basePageQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (!StringUtil.isBlank(result)) {
                    try {

                        JSONObject object = new JSONObject(result);
                        JSONArray array = object.getJSONArray("resultList");
                        if (null != array && array.length() > 0) {
                            pageIndex++;
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject objBean = array.getJSONObject(i);
                                MessageBean bean = new MessageBean();
                                bean.setType(objBean.getInt("type"));
                                bean.setId(objBean.getInt("id"));
                                bean.setContent(objBean.getString("content"));
                                bean.setStatus(objBean.getInt("status"));
                                bean.setInsert_time(objBean.getString("insertTime"));
                                bean.setTitle(objBean.getString("title"));
                                bean.setDeletestatus_to(objBean.getInt("deletestatusTo"));
                                JSONObject userBean = objBean.getJSONObject("fromuserId");
                                UserBean userData=new UserBean();
                                userData.setId(userBean.getInt("id"));
                                bean.setBean(userData);

                                if(!userBean.isNull("photo")){
                                    JSONObject photo = userBean.getJSONObject("photo");
                                    bean.setUrl(photo.getString("url"));
                                }else {
                                    bean.setUrl("null");
                                }
                                data.add(bean);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if(data.size()>0){
                            adapter.notifyDataSetChanged();
                            messageListLayput.setVisibility(View.VISIBLE);
                            messageNoLayout.setVisibility(View.GONE);
                        }else {
                            messageListLayput.setVisibility(View.GONE);
                            messageNoLayout.setVisibility(View.VISIBLE);
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
                messageRefresh.finishRefreshLoadMore();
                messageRefresh.finishRefresh();
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


    //删除消息的回掉
    @Override
    public void delMseeage(int position) {
        delMseeageList(position);
    }

    private void delMseeageList(final int position) {
        MessageBean bean=data.get(position);
        HashMap<String,String> body=new HashMap<>();
        body.put("ctype","message");
        body.put("data","{id:"+bean.getId()+",deletestatusTo:1}");

        service.doCommonPost(null, MainUrl.SetMessageStatus, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("delMseeageList",result.toString());
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject object=new JSONObject(result);
                        if(object.getBoolean("result")){
                            Toast.makeText(MessageListActivity.this,"删除成功！", Toast.LENGTH_SHORT).show();
                            data.remove(position);
                            adapter.notifyItemRemoved(position);
                        }else {
                            Toast.makeText(MessageListActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
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


}
