package com.example.mayikang.wowallet.ui.act;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.ListDropDownAdapter;
import com.example.mayikang.wowallet.adapter.Store2Adapter;
import com.example.mayikang.wowallet.adapter.StoreAdapter;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.Store2Bean;
import com.example.mayikang.wowallet.ui.xwidget.dialog.SocialShareDialog;
import com.example.mayikang.wowallet.util.Eyes;
import com.example.mayikang.wowallet.util.FastBlurUtil;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.jauker.widget.BadgeView;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.yyydjk.library.DropDownMenu;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

@Route(path = "/main/act/store2")
public class Store2Activity extends BaseAppcompatActivity implements View.OnClickListener {
    RecyclerView rv;
    MaterialRefreshLayout refresh;
    @BindView(R.id.act_store2_iv_collect)
    ImageView ivCollect;
    @BindView(R.id.act_store2_iv_collected)
    ImageView ivCollected;
    @Autowired(name = "id")
    public int id = -1;
    LinearLayout linShoppingcart;
    TextView store2TvAllmoney;
    @BindView(R.id.store2_iv_icon)
    CircleImageView store2IvIcon;
    @BindView(R.id.store2_tv_introduce)
    TextView store2TvIntroduce;
    @BindView(R.id.store2_tv_score)
    TextView store2TvScore;
    @BindView(R.id.store2_tv_distance)
    TextView store2TvDistance;
    @BindView(R.id.store2_tv_adress)
    TextView store2TvAdress;
    @BindView(R.id.store2_tv_payment)
    TextView store2TvPayment;
//    @BindView(R.id.store2_normol)
//    RadioButton store2Normol;
//    @BindView(R.id.store2_count)
//    RadioButton store2Count;
//    @BindView(R.id.store2_new)
//    RadioButton store2New;
//    @BindView(R.id.store2_price)
//    RadioButton store2Pi;
//    @BindView(R.id.store_radGroup)
//    RadioGroup storeRadGroup;
//    @BindView(R.id.store2_all)
//    RadioButton store2All;
    LinearLayout store2NoLayout;
    TextView store2TvComit;
    @BindView(R.id.store2_tv_title)
    TextView store2TvTitle;
    @BindView(R.id.store2_title_back)
    ImageView store2TitleBack;
    @BindView(R.id.store_dropDownMenu)
    DropDownMenu storeDropDownMenu;

    private String headers[] = {"排序","类型"};
    private String sort[]={"综合","销量","新品"};
    private String type[]={"到店扫码","线下","线上"};

    private ListDropDownAdapter sortAdapter;
    private ListDropDownAdapter typeAdapter;
    private int constellationPosition = 0;//用于判断是否选择
    private List<View> popupViews = new ArrayList<>();



    private HttpServiceImpl http;
    private HashMap<String, Object> data = new HashMap<>();
    private BadgeView badgeview;//显示消息数量控件

    private Store2Adapter adapter;
    private LinearLayoutManager manager;
    private List<Store2Bean> list = new ArrayList<>();

    private boolean flag = false;//价格筛选图标显示判断
    private int count = 1;

    private String logo = "";
    private String name = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Eyes.translucentStatusBar(this);//取消状态栏
        http = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        initView();
        initData();
    }

    private void initData() {
        //查询店铺信息
        pullStoreInfo(id);
        for (int i = 0; i < 5; i++) {
            list.add(new Store2Bean());
        }
    }

    //初始化控件
    private void initView() {
        //获取收藏状态
        isCollected();

        //购物车商品数量操作相关
        badgeview = new BadgeView(this);
        badgeview.setTargetView(linShoppingcart);
        badgeview.setBadgeGravity(Gravity.RIGHT | Gravity.TOP);
        badgeview.setBadgeCount(3);
        badgeview.setBackground(9, Color.parseColor("#FB4644"));


        //毛玻璃效果
        Resources res = getResources();
        Bitmap scaledBitmap = BitmapFactory.decodeResource(res, R.drawable.ic_defult_pphz_tree);
        //        scaledBitmap为目标图像，10是缩放的倍数（越大模糊效果越高）
        final Bitmap blurBitmap = FastBlurUtil.toBlur(scaledBitmap, 8);
        store2TitleBack.setScaleType(ImageView.ScaleType.CENTER_CROP);
        store2TitleBack.setImageBitmap(blurBitmap);

        //添加商品列表布局
        View contentView = LayoutInflater.from(this).inflate(R.layout.store_contentview, null);
        store2NoLayout= (LinearLayout) contentView.findViewById(R.id.store2No_layout);
        refresh= (MaterialRefreshLayout) contentView.findViewById(R.id.refresh);
        rv= (RecyclerView) contentView.findViewById(R.id.rv);
        linShoppingcart= (LinearLayout) contentView.findViewById(R.id.lin_shoppingcart);
        store2TvAllmoney= (TextView) contentView.findViewById(R.id.store2_tv_money);
        store2TvComit= (TextView) contentView.findViewById(R.id.store2_tv_comit);
        linShoppingcart.setOnClickListener(this);
        store2TvComit.setOnClickListener(this);


        //排序下拉列表
        final ListView sortView = new ListView(this);
        sortAdapter = new ListDropDownAdapter(this, Arrays.asList(sort));
        sortView.setDividerHeight(0);
        sortView.setAdapter(sortAdapter);

        //类型下拉列表
        final ListView typeView = new ListView(this);
        typeView.setDividerHeight(0);
        typeAdapter = new ListDropDownAdapter(this, Arrays.asList(type));
        typeView.setAdapter(typeAdapter);


        //init popupViews
        popupViews.add(sortView);
        popupViews.add(typeView);

        //add item click event
        sortView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sortAdapter.setCheckItem(position);
                constellationPosition = position;
                storeDropDownMenu.setTabText(position == 0 ? headers[0] : sort[position]);
                storeDropDownMenu.closeMenu();

                switch (sort[position]){
                    case "综合":
                        //TODO条件搜索
                        break;
                    case "销量":
                        //TODO条件搜索
                        break;
                    case "新品":
                        //TODO条件搜索
                        break;
                }
            }
        });

        typeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                typeAdapter.setCheckItem(position);
                constellationPosition = position;
                storeDropDownMenu.setTabText(position == 0 ? headers[1] : type[position]);
                storeDropDownMenu.closeMenu();

                switch (type[position]){
                    case "到店扫码":
                        //TODO条件搜索
                        break;
                    case "线下":
                        //TODO条件搜索
                        break;
                    case "线上":
                        //TODO条件搜索
                        break;
                }
            }
        });
        manager = new LinearLayoutManager(this);
        adapter = new Store2Adapter(list, this);
        rv.setLayoutManager(manager);
        rv.setAdapter(adapter);
        refresh.setLoadMore(true);
        refresh.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {

            }
            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);

            }
        });


        storeDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);
//        //给radiobutton添加一个图片
//        changeRadioIcon(R.drawable.adress_icon);
//
//        //radiogroup监听事件
//        storeRadGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//                switch (checkedId) {
//                    //综合
//                    case R.id.store2_normol:
//                        count = 1;
//                        break;
//                    //销量
//                    case R.id.store2_count:
//                        count = 1;
//                        break;
//                    //新品
//                    case R.id.store2_new:
//                        count = 1;
//                        break;
//                    //价格
//                    case R.id.store2_price:
//
//                        break;
//                    //全部
//                    case R.id.store2_all:
//                        count = 1;
//                        break;
//                }
//            }
//        });
//
//        store2Pi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (store2Pi.isChecked()) {
//                    if (count == 1) {
//                        count = 2;
//                        return;
//                    } else if (count == 2) {
//                        flag = !flag;
//                        if (flag) {
//                            changeRadioIcon(R.drawable.brand_icon);
//                        } else {
//                            changeRadioIcon(R.drawable.adress_icon);
//                        }
//                    }
//                }
//            }
//        });

    }

//    //切换图片
//    private void changeRadioIcon(int id) {
//        Drawable mDrawable = getResources().getDrawable(id);
//        mDrawable.setBounds(-20, 0, 30, 40);
//        store2Pi.setCompoundDrawables(null, null, mDrawable, null);
//    }

    @Override
    public int initLayout() {
        return R.layout.activity_store2;
    }

    @Override
    public void reloadData() {

    }


    @OnClick({R.id.back, R.id.act_store2_rl_collect, R.id.act_store2_rl_open_share, R.id.store2_tv_payment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            //收藏
            case R.id.act_store2_rl_collect:
                //已经登录，可以执行操作
                if (UserAuthUtil.isUserLogin()) {
                    collectClick();
                } else {
                    Toast.makeText(Store2Activity.this, "请登录后操作", Toast.LENGTH_SHORT).show();
                    ARouter.getInstance().build("/main/act/login").navigation();
                }
                break;
            //分享
            case R.id.act_store2_rl_open_share:
                data.put("type", 1);
                SocialShareDialog socialShareDialog = new SocialShareDialog(this, data);
                socialShareDialog.show();
                break;
            //店内付款
            case R.id.store2_tv_payment:
                ARouter.getInstance().build("/user/main/act/confirm_order").withInt("id", id).withInt("Flag", 1).navigation();
                break;
        }
    }


    /**
     * 检查店铺是否已经被当前用户收藏了该店铺
     */
    private void isCollected() {
        HashMap<String, String> map = new HashMap();
        map.put("storeId", String.valueOf(id));
        http.doCommonPost(null, MainUrl.queryIsStoreCollectedUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {

                if (!StringUtil.isBlank(resultStr)) {
                    try {
                        JSONObject obj = new JSONObject(resultStr);
                        //是否收藏了店铺
                        boolean result = obj.getBoolean("result");
                        //已经收藏
                        if (result) {
                            ivCollect.setVisibility(View.GONE);
                            ivCollected.setVisibility(View.VISIBLE);
                        } else {
                            //未收藏
                            ivCollect.setVisibility(View.VISIBLE);
                            ivCollected.setVisibility(View.GONE);
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


    /**
     * 添加收藏或删除收藏店铺
     */
    private void collectClick() {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", String.valueOf(UserAuthUtil.getUserId()));
        map.put("storeId", String.valueOf(id));
        http.doCommonPost(null, MainUrl.addOrDeleteCollectedStoreUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {

                if (!StringUtil.isBlank(resultStr)) {
                    try {
                        JSONObject obj = new JSONObject(resultStr);
                        boolean result = obj.getBoolean("result");
                        String msg = obj.getString("msg");
                        if (result) {
                            Toast.makeText(Store2Activity.this, msg, Toast.LENGTH_SHORT).show();
                            //收藏成功
                            if ("收藏成功！".equals(msg)) {
                                ivCollect.setVisibility(View.GONE);
                                ivCollected.setVisibility(View.VISIBLE);
                            }
                            //取消收藏成功
                            if ("取消收藏！".equals(msg)) {
                                ivCollect.setVisibility(View.VISIBLE);
                                ivCollected.setVisibility(View.GONE);
                            }
                        } else {
                            Toast.makeText(Store2Activity.this, "操作异常", Toast.LENGTH_SHORT).show();
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
                dismissLoading();
            }

            @Override
            public void onWaiting() {
            }

            @Override
            public void onStarted() {
                showLoading(true, "正在加载中");
            }

            @Override
            public void onLoading(long total, long current) {
            }
        });
    }


    //查询店铺信息
    public void pullStoreInfo(int id) {

        HashMap<String, String> map = new HashMap<>();
        map.put("ctype", "store");
        map.put("jf", "storeLogo");
        map.put("id", String.valueOf(id));
        http.doCommonPost(null, MainUrl.baseSingleQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {

                if (!StringUtil.isBlank(resultStr)) {

                    try {
                        JSONObject obj = new JSONObject(resultStr);
                        JSONObject store = obj.getJSONObject("data");
                        int id = store.getInt("id");
                        data.put("StoreId", id);
                        //店铺名
                        name = store.getString("name");
                        data.put("StoreName", name);
                        //人均消费
                        double average_spend = store.getDouble("averageSpend");
                        //店铺介绍
                        String detail = store.getString("detail");
                        data.put("StoreMsg", detail);
                        //经纬度
                        double latitude = 0;
                        double longitude = 0;
                        String la = store.getString("latitude");
                        String lo = store.getString("longitude");
                        if (!StringUtil.isBlank(la) && !StringUtil.isBlank(lo)) {
                            latitude = Double.valueOf(la);
                            longitude = Double.valueOf(lo);
                        }

                        //评分
                        double score = store.getDouble("score");
                        //地址
                        String storeAddress = store.getString("storeAddress");

                        String telephone = store.getString("telephone");

                        String photo = store.getString("storeLogo");
                        if (!StringUtil.isBlank(photo)) {
                            logo = store.getJSONObject("storeLogo").getString("url");
                        }

                        //标题
                        store2TvTitle.setText(name);

                        //店铺头像
                        Glide.with(Store2Activity.this)
                                .load(logo)
                                .error(R.mipmap.icon_load_faild)
                                .into(store2IvIcon);

                        //距离
                        if (latitude == 0d || longitude == 0d) {
                            store2TvDistance.setText("距离计算错误");
                        } else {
                            store2TvDistance.setText(calDistance(longitude, latitude) + "km");
                        }

                        //评分
                        store2TvScore.setText(score + "分");

                        //地址
                        store2TvAdress.setText(storeAddress);

                        //毛玻璃效果
                        Glide.with(Store2Activity.this).load(logo).asBitmap().into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                Bitmap blurBitmap = FastBlurUtil.toBlur(resource, 10);
                                store2TitleBack.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                store2TitleBack.setImageBitmap(blurBitmap);
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("pullStoreInfoJSON", e.toString());
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

    //计算距离
    private String calDistance(double longt, double lat) {
        LatLng latLng = new LatLng(Double.valueOf((String) SPFUtil.get(Tag.TAG_LATITUDE, "0")), Double.valueOf((String) SPFUtil.get(Tag.TAG_LONGITUDE, "0")));
        float distance = AMapUtils.calculateLineDistance(latLng, new LatLng(lat, longt)) / 1000;
        String str = String.valueOf(distance);
        str = str.substring(0, str.indexOf(".") + 2);
        return str;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //结算
            case R.id.store2_tv_comit:
                //卡券结算
                ARouter.getInstance().build("/main/act/ConfirmOrderTicket").navigation();
                break;
            //购物车
            case R.id.lin_shoppingcart:
                ARouter.getInstance().build("/main/act/ShoppingCart").withString("url", logo).withString("name", name).navigation();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        //退出activity前关闭菜单
        if (storeDropDownMenu.isShowing()) {
            storeDropDownMenu.closeMenu();
        } else {
            super.onBackPressed();
        }
    }
}
