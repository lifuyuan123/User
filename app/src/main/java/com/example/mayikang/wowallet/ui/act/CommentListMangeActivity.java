package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.CommentListAdapter;
import com.example.mayikang.wowallet.adapter.CommentListMangeAdapter;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.CommentBean;
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

/***
 * 我的页面的评论列表
 */
@Route(path = "/main/act/user/CommentListMangeActivity")
public class CommentListMangeActivity extends BaseAppcompatActivity {

    @BindView(R.id.comment_list_mange_back)
    RelativeLayout commentListMangeBack;
    @BindView(R.id.messageList_number)
    TextView messageListNumber;
    @BindView(R.id.comment_list_mange_rv)
    RecyclerView commentListMangeRv;
    @BindView(R.id.comment_list_mange_refresh)
    MaterialRefreshLayout commentListMangeRefresh;
    @BindView(R.id.activity_comment_list_mange)
    LinearLayout activityCommentListMange;
    @BindView(R.id.comment_data_layout)
    LinearLayout commentDataLayout;
    @BindView(R.id.comment_list_No_layout)
    LinearLayout commentListNoLayout;
    private CommentListMangeAdapter adapter;
    private List<CommentBean> data = new ArrayList<>();
    private HttpServiceImpl server;
    private int pageIndex=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        server = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        adapter = new CommentListMangeAdapter(this, data);
        commentListMangeRv.setLayoutManager(new LinearLayoutManager(this));
        commentListMangeRv.setAdapter(adapter);
        setListener();

    }

    //设置监听
    private void setListener() {
        commentListMangeRefresh.setLoadMore(true);
        commentListMangeRefresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                pullCommentList();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                pullCommentListMore();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        pullCommentList();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_comment_list_mange;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.comment_list_mange_back)
    public void onViewClicked() {
        finish();
    }

    //获取评论数据
    private void pullCommentList() {
        data.clear();
        pageIndex=1;
        HashMap<String, String> body = new HashMap<>();
        body.put("userId", UserAuthUtil.getUserId()+"");
        body.put("jf","reviewer|store|photo|storeLogo|commentPhoto");
        body.put("pageIndex",pageIndex+"");
        server.doCommonPost(null, MainUrl.GetMineCommentList, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (!StringUtil.isBlank(result)) {
                    Log.e("pullCommentList",result.toString());
                    try {
                        pageIndex++;
                        JSONObject obj = new JSONObject(result);
                        int coms=obj.getInt("coms");
                        messageListNumber.setText("共有"+coms+"条评论");
                        JSONArray resultList=obj.getJSONArray("resultList");
                        if(null!=resultList&&resultList.length()>0){
                            for (int i = 0; i <resultList.length() ; i++) {
                                JSONObject cmmentObj=resultList.getJSONObject(i);
                                CommentBean bean=new CommentBean();
                                //获取评论照片
                                JSONArray photoList=cmmentObj.getJSONArray("commentPhoto");
                                List<String> photo=new ArrayList<>();
                                if(null!=photoList&&photoList.length()>0){
                                    for (int j = 0; j < photoList.length(); j++) {
                                        JSONObject photos=photoList.getJSONObject(j);
                                        photo.add(photos.getJSONObject("commentPhoto").getString("url"));
                                    }
                                }
                                bean.setPhotoList(photo);

                                //获取评论内容
                                bean.setContent(cmmentObj.getString("content"));
                                bean.setCommentTime(cmmentObj.getString("insertTime"));
                                bean.setIsAnonymous(cmmentObj.getInt("isAnonymous"));
                                bean.setScore(cmmentObj.getDouble("score"));
                                JSONObject user=cmmentObj.getJSONObject("reviewer");
                                bean.setCommentUserName(user.getString("username"));
                                bean.setCommentUserLogo(user.getJSONObject("photo").getString("url"));
                                //获取商家信息

                                JSONObject store=cmmentObj.getJSONObject("store");
                                bean.setStoreName(store.getString("name"));
                                bean.setStoreDetail(store.getString("detail"));
                                bean.setStoreUrl(store.getJSONObject("storeLogo").getString("url"));
                                data.add(bean);
                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("pullCommentList",e.toString());
                    } finally {
                        if (data.size() > 0) {
                            commentDataLayout.setVisibility(View.VISIBLE);
                            commentListNoLayout.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        }else {
                            commentDataLayout.setVisibility(View.GONE);
                            commentListNoLayout.setVisibility(View.VISIBLE);
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
                commentListMangeRefresh.finishRefreshLoadMore();
                commentListMangeRefresh.finishRefresh();
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
    private void pullCommentListMore() {
        HashMap<String, String> body = new HashMap<>();
        body.put("userId", UserAuthUtil.getUserId()+"");
        body.put("jf","reviewer|store|photo|storeLogo|commentPhoto");
        body.put("pageIndex",pageIndex+"");
        server.doCommonPost(null, MainUrl.GetMineCommentList, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if (!StringUtil.isBlank(result)) {
                    try {
                        pageIndex++;
                        JSONObject obj = new JSONObject(result);
                        int coms=obj.getInt("coms");
                        messageListNumber.setText(coms+"");
                        JSONArray resultList=obj.getJSONArray("resultList");
                        if(null!=resultList&&resultList.length()>0){
                            for (int i = 0; i <resultList.length() ; i++) {
                                JSONObject cmmentObj=resultList.getJSONObject(i);
                                CommentBean bean=new CommentBean();
                                //获取评论照片
                                JSONArray photoList=cmmentObj.getJSONArray("photoList");
                                List<String> photo=new ArrayList<>();
                                if(null!=photoList&&photoList.length()>0){
                                    for (int j = 0; j < photoList.length(); j++) {
                                        JSONObject photos=photoList.getJSONObject(j);
                                        photo.add(photos.getJSONObject("commentPhoto").getString("url"));
                                    }
                                }
                                bean.setPhotoList(photo);

                                //获取评论内容
                                bean.setContent(cmmentObj.getString("content"));
                                bean.setCommentTime(cmmentObj.getString("insertTime"));
                                bean.setIsAnonymous(cmmentObj.getInt("isAnonymous"));
                                bean.setScore(cmmentObj.getDouble("score"));
                                JSONObject user=cmmentObj.getJSONObject("reviewer");
                                bean.setCommentUserName(user.getString("username"));
                                bean.setCommentUserLogo(user.getJSONObject("photo").getString("url"));
                                //获取商家信息

                                JSONObject store=cmmentObj.getJSONObject("store");
                                bean.setStoreName(store.getString("name"));
                                bean.setStoreDetail(store.getString("detail"));
                                bean.setStoreUrl(store.getJSONObject("storeLogo").getString("url"));
                                data.add(bean);
                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (data.size() > 0) {
                            commentDataLayout.setVisibility(View.VISIBLE);
                            commentListNoLayout.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        }else {
                            commentDataLayout.setVisibility(View.GONE);
                            commentListNoLayout.setVisibility(View.VISIBLE);
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
                commentListMangeRefresh.finishRefreshLoadMore();
                commentListMangeRefresh.finishRefresh();
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
