package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.CommentListAdapter;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.CommentBean;
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

@Route(path = "/main/act/comment_list")
public class CommentListActivity extends BaseAppcompatActivity {

    @BindView(R.id.act_comment_list_rv)
    RecyclerView mRV;
    @BindView(R.id.refresh)
    MaterialRefreshLayout refresh;


    @Autowired(name = "id")
    public int id = -1;
    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.commentList_layout)
    LinearLayout commentListLayout;
    @BindView(R.id.fansNo_layout)
    LinearLayout listNoLayout;


    private CommentListAdapter adapter;
    private List<CommentBean> data = new ArrayList<>();
    private HttpServiceImpl http;

    private int pageIndex = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        http = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        initRV();
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                pullCommentInfo(id);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                pullMoreCommentInfo(id);
            }
        });
        pullCommentInfo(id);

    }

    @Override
    public int initLayout() {
        return R.layout.activity_comment_list;
    }

    @Override
    public void reloadData() {

    }

    public void initRV() {
        refresh.setLoadMore(true);
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {

            }
        });

        adapter = new CommentListAdapter(this, data);
        mRV.setAdapter(adapter);
        mRV.setLayoutManager(new LinearLayoutManager(this));
    }

    public void pullCommentInfo(final int id) {
        pageIndex = 1;
        data.clear();
        //http://118.123.22.190:8010/wow/user/pageSearch$ajax.htm?
        // ctype=comment&cond={store:{id:1}}&jf=reviewer|photo|commentDetail|commentPhoto
        HashMap<String, String> map = new HashMap<>();
        map.put("ctype", "comment");
        map.put("cond", "{store:{id:" + id + "}}");
        map.put("jf", "reviewer|photo|commentDetail|commentPhoto");
        map.put("orderby", "insertTime desc");
        map.put("pageIndex", String.valueOf(pageIndex));

        http.doCommonPost(null, MainUrl.basePageQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {

                if (!StringUtil.isBlank(resultStr)) {

                    try {
                        JSONObject obj = new JSONObject(resultStr);
                        JSONArray resultList = obj.getJSONArray("resultList");
                        if (resultList != null && resultList.length() > 0) {
                            pageIndex++;
                            for (int i = 0; i < resultList.length(); i++) {

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
                                String reviewerLogo = x.getJSONObject("reviewer").getJSONObject("photo").getString("url");
                                //评论者
                                String reviewerName = x.getJSONObject("reviewer").getString("username");

                                //评论的图片列表
                                String gallery = x.getString("commentPhoto");
                                List<String> photoList = new ArrayList<>();
                                if (!StringUtil.isBlank(gallery)) {
                                    JSONArray photos = new JSONArray(gallery);
                                    if (photos != null && photos.length() > 0) {
                                        for (int j = 0; j < photos.length(); j++) {
                                            String photoUrl = photos.getJSONObject(j).getJSONObject("commentPhoto").getString("url");
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
                                data.add(cb);

                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (data.size() > 0) {
                            commentListLayout.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();
                        }else {
                            commentListLayout.setVisibility(View.GONE);
                            listNoLayout.setVisibility(View.VISIBLE);
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
                refresh.finishRefresh();
                refresh.finishRefreshLoadMore();
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


    public void pullMoreCommentInfo(final int id) {

        //http://118.123.22.190:8010/wow/user/pageSearch$ajax.htm?
        // ctype=comment&cond={store:{id:1}}&jf=reviewer|photo|commentDetail|commentPhoto
        HashMap<String, String> map = new HashMap<>();
        map.put("ctype", "comment");
        map.put("cond", "{store:{id:" + id + "}}");
        map.put("jf", "reviewer|photo|commentDetail|commentPhoto");
        map.put("orderby", "insertTime desc");
        map.put("pageIndex", String.valueOf(pageIndex));

        http.doCommonPost(null, MainUrl.basePageQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {

                if (!StringUtil.isBlank(resultStr)) {

                    try {
                        JSONObject obj = new JSONObject(resultStr);
                        JSONArray resultList = obj.getJSONArray("resultList");
                        if (resultList != null && resultList.length() > 0) {
                            pageIndex++;
                            for (int i = 0; i < resultList.length(); i++) {

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
                                String reviewerLogo = x.getJSONObject("reviewer").getJSONObject("photo").getString("url");
                                //评论者
                                String reviewerName = x.getJSONObject("reviewer").getString("username");

                                //评论的图片列表
                                String gallery = x.getString("commentPhoto");
                                List<String> photoList = new ArrayList<>();
                                if (!StringUtil.isBlank(gallery)) {
                                    JSONArray photos = new JSONArray(gallery);
                                    if (photos != null && photos.length() > 0) {
                                        for (int j = 0; j < photos.length(); j++) {
                                            String photoUrl = photos.getJSONObject(j).getJSONObject("commentPhoto").getString("url");
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
                                data.add(cb);

                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (data.size() > 0) {
                            commentListLayout.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();
                        }else {
                            commentListLayout.setVisibility(View.GONE);
                            listNoLayout.setVisibility(View.VISIBLE);
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
                refresh.finishRefresh();
                refresh.finishRefreshLoadMore();
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


    @OnClick(R.id.back)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
}
