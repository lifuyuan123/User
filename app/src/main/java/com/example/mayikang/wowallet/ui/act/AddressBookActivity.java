package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.contact.ContactAdapter;
import com.example.mayikang.wowallet.contact.ContactBean;
import com.example.mayikang.wowallet.contact.IndexBar.widget.IndexBar;
import com.example.mayikang.wowallet.contact.decoration.DividerItemDecoration;
import com.example.mayikang.wowallet.contact.decoration.TitleItemDecoration;
import com.example.mayikang.wowallet.intf.MainUrl;
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
 * 通讯录页面
 */
@Route(path = "/main/act/user/address_book")
public class AddressBookActivity extends BaseAppcompatActivity {


    @BindView(R.id.address_book_frameLayout)
    FrameLayout addressBookFrameLayout;
    @BindView(R.id.address_No_layout)
    LinearLayout addressNoLayout;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mManager;
    private List<ContactBean> data = new ArrayList<>();
    private HttpServiceImpl http;
    private TitleItemDecoration mDecoration;


    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.tvSideBarHint)
    TextView mTvSideBarHint;
    @BindView(R.id.indexBar)
    IndexBar mIndexBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        http = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        pullMyFriends();
    }


    private void initRV() {
        mAdapter = new ContactAdapter(this, data);
        mDecoration = new TitleItemDecoration(this, data);
        mManager = new LinearLayoutManager(this);

        mRv.setLayoutManager(mManager);
        mRv.setAdapter(mAdapter);
        mRv.addItemDecoration(mDecoration);
        //item divider
        mRv.addItemDecoration(new DividerItemDecoration(AddressBookActivity.this, DividerItemDecoration.VERTICAL_LIST));

        //使用indexBar


        mIndexBar.setmPressedShowTextView(mTvSideBarHint)//设置HintTextView
                .setNeedRealIndex(false)//设置需要真实的索引
                .setmLayoutManager(mManager)//设置RecyclerView的LayoutManager
                .setmSourceDatas(data);//设置数据源

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public int initLayout() {
        return R.layout.activity_address_book;
    }

    @Override
    public void reloadData() {

    }


    @OnClick({R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    /**
     * 查询当前用户的朋友
     */
    public void pullMyFriends() {
        //http://118.123.22.190:8010/wow/user/obtionDriends$ajax.htm?jf=user|friend|photo
        data.clear();
        HashMap<String, String> map = new HashMap<>();
        map.put("jf", "user|friend|photo");
        map.put("pageSize", "9999");

        http.doCommonPost(null, MainUrl.queryUserFriendUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {

                if (!StringUtil.isBlank(result)) {
                    Log.e("pullMyFriends",result.toString());
                    try {
                        JSONObject obj = new JSONObject(result);
                        JSONArray resultList = obj.getJSONArray("resultList");
                        if (resultList != null && resultList.length() > 0) {

                            for (int i = 0; i < resultList.length(); i++) {

                                JSONObject x = resultList.getJSONObject(i);

                                //先判断是否是自己好友
                                int friendId = x.getJSONObject("friend").getInt("id");
                                int userId = x.getJSONObject("user").getInt("id");

                                if (UserAuthUtil.getUserId() != friendId) {
                                    int isAgree = x.getInt("isAgree");
                                    if (2 != isAgree) {
                                        continue;
                                    }

                                    JSONObject friend = x.getJSONObject("friend");
                                    friendId = friend.getInt("id");
                                    String username = friend.getString("username");
                                    String photo = friend.getString("photo");
                                    String photoStr = null;
                                    if (!StringUtil.isBlank(photo)) {
                                        photoStr = friend.getJSONObject("photo").getString("url");
                                    }

                                    String phone = friend.getString("phone");

                                    ContactBean cb = new ContactBean();
                                    cb.setId(friendId);
                                    cb.setName(username);
                                    cb.setPhone(phone);
                                    cb.setLogo(photoStr);
                                    data.add(cb);

                                }

                                if (UserAuthUtil.getUserId() != userId) {
                                    int isAgree = x.getInt("isAgree");
                                    if (2 != isAgree) {
                                        continue;
                                    }
                                    JSONObject user = x.getJSONObject("user");
                                    friendId = user.getInt("id");
                                    String username = user.getString("username");
                                    String photo = user.getString("photo");
                                    String photoStr = null;
                                    if (!StringUtil.isBlank(photo)) {
                                        photoStr = user.getJSONObject("photo").getString("url");
                                    }
                                    String phone = user.getString("phone");
                                    ContactBean cb = new ContactBean();
                                    cb.setId(friendId);
                                    cb.setName(username);
                                    cb.setPhone(phone);
                                    cb.setLogo(photoStr);
                                    data.add(cb);
                                }
                            }
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (data.size() > 0) {
                            initRV();
                            addressBookFrameLayout.setVisibility(View.VISIBLE);
                            addressNoLayout.setVisibility(View.GONE);
                        } else {
                            //没有好友的情况
                            addressBookFrameLayout.setVisibility(View.GONE);
                            addressNoLayout.setVisibility(View.VISIBLE);
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
                dismissLoading();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                showLoading(true,"加载中...");
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });

    }


}
