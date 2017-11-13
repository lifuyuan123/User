package com.example.mayikang.wowallet.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.fastjson.parser.JSONLexer;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.bumptech.glide.Glide;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.modle.javabean.ProjectBean;
import com.example.mayikang.wowallet.modle.javabean.StoreBean;
import com.example.mayikang.wowallet.modle.javabean.UserBean;
import com.example.mayikang.wowallet.ui.act.ProjectListActivity;
import com.example.mayikang.wowallet.ui.xwidget.dialog.SocialShareDialog;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.github.ornolfr.ratingview.RatingView;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.base.util.permission.EasyPermissionsEx;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;


import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.mayikang.wowallet.constant.requestcode.PermissionRequestCode.HOME_REQUEST_CAMERA_PERMISSION;

/**
 * Created by Carson_Ho on 17/4/26.
 */
public class HomeMultLayoutAdapter extends DelegateAdapter.Adapter<HomeMultLayoutAdapter.MainViewHolder> {
    /**
     * 首页一次从上往下对应的6种布局
     **/
    public static final int TYPE_1 = 1;
    public static final int TYPE_2 = 2;
    public static final int TYPE_3 = 3;
    public static final int TYPE_4 = 4;
    public static final int TYPE_5 = 5;
    public static final int TYPE_6 = 6;

    // 使用DelegateAdapter首先就是要自定义一个它的内部类Adapter，让LayoutHelper和需要绑定的数据传进去
    // 此处的Adapter和普通RecyclerView定义的Adapter只相差了一个onCreateLayoutHelper()方法，其他的都是一样的做法.

    private ArrayList<HashMap<String, Object>> data;
    // 用于存放数据列表

    private Context context;
    private LayoutInflater inflater;
    private LayoutHelper layoutHelper;
    private RecyclerView.LayoutParams layoutParams;
    private int count = 0;
    private int type = 0;//本 item 的类型


    //构造函数(传入每个的数据列表 & 展示的Item数量)
    public HomeMultLayoutAdapter(Context context, LayoutHelper layoutHelper, int count,
                                 ArrayList<HashMap<String, Object>> data, int type) {
        //宽度占满，高度随意
        this(context, layoutHelper, count,
                new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT), data, type);
    }


    public HomeMultLayoutAdapter(Context context, LayoutHelper layoutHelper, int count, @NonNull RecyclerView.LayoutParams layoutParams,
                                 ArrayList<HashMap<String, Object>> data, int type) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.count = count;
        this.layoutParams = layoutParams;
        this.data = data;
        this.type = type;

        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }
    }


    public ArrayList<HashMap<String, Object>> getData() {
        return data;
    }

    public RecyclerView.LayoutParams getLayoutParams() {
        return layoutParams;
    }

    public int getCount() {
        return data == null ? 0 : data.size();
    }

    public int getType() {
        return type;
    }

    // 把ViewHolder绑定Item的布局
    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;

        switch (viewType) {
            case TYPE_1:
                view = inflater.inflate(R.layout.home_lay_1, parent, false);
                break;
            case TYPE_2:
                view = inflater.inflate(R.layout.home_lay_2, parent, false);
                break;
            case TYPE_3:
                view = inflater.inflate(R.layout.home_lay_3, parent, false);
                break;
            case TYPE_4:
                view = inflater.inflate(R.layout.home_lay_4, parent, false);
                break;
            case TYPE_5:
                view = inflater.inflate(R.layout.home_lay_5, parent, false);
                break;
            case TYPE_6:
                view = inflater.inflate(R.layout.home_lay_6, parent, false);
                break;

        }


        return new MainViewHolder(view, viewType);
    }

    // 此处的Adapter和普通RecyclerView定义的Adapter只相差了一个onCreateLayoutHelper()方法
    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    // 绑定Item的数据
    @Override
    public void onBindViewHolder(final MainViewHolder holder, final int position) {
        int type = getItemViewType(position);

        switch (type) {
            case TYPE_1:
                holder.llExpend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!UserAuthUtil.isUserLogin()){
                            Toast.makeText(context, "请登录后操作", Toast.LENGTH_SHORT).show();
                            ARouter.getInstance().build("/main/act/login").navigation();
                        }else {
                            ARouter.getInstance().build("/main/act/AllOrderForm").navigation();
                        }
                    }
                });

                holder.llProfit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!UserAuthUtil.isUserLogin()){
                            Toast.makeText(context, "请登录后操作", Toast.LENGTH_SHORT).show();
                            ARouter.getInstance().build("/main/act/login").navigation();
                        }else {
                            ARouter.getInstance().build("/main/act/DividendActivity").navigation();
                        }
                    }
                });

                HashMap<String, Object> map = new HashMap<>();
                map = data.get(0);
                if (map.containsKey("amount")) {
                    double amount = (double) map.get("amount");
                    holder.tvBalance.setText("账户余额：￥" + new DecimalFormat("######0.00").format(amount));
                }

                if (map.containsKey("cumulativeBonus")) {
                    double cumulativeBonus = (double) map.get("cumulativeBonus");
                    holder.tvBonus.setText("￥" + new DecimalFormat("######0.00").format(cumulativeBonus));
                }

                if (map.containsKey("cumulativeSpend")) {
                    double cumulativeSpend = (double) map.get("cumulativeSpend");
                    holder.tvConsume.setText("￥" +new DecimalFormat("######0.00").format(cumulativeSpend) );
                }

                break;
            case TYPE_2:

                //付钱
                holder.llToPay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(EasyPermissionsEx.hasPermissions(context, Manifest.permission.CAMERA)){
                            ARouter.getInstance().build("/main/act/qr_scan").navigation();
                        }else {
                            EasyPermissionsEx.requestPermissions(context,"扫描二维码需要授予相机权限",HOME_REQUEST_CAMERA_PERMISSION,Manifest.permission.CAMERA);
                        }

                    }
                });

                //提现
                holder.llToDeposit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance().build("/main/act/user/balance").navigation();
                    }
                });

                //账单
                holder.llToBill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //ARouter.getInstance().build("/main/act/user/bill").navigation();
                        ARouter.getInstance().build("/main/act/confirm_order_shop").navigation();
                    }
                });

                //加入沃克
                holder.llToJoin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!UserAuthUtil.isUserLogin()){
                            Toast.makeText(context, "请登录后操作", Toast.LENGTH_SHORT).show();
                            ARouter.getInstance().build("/main/act/login").navigation();
                        }else {
                            UserBean bean=UserAuthUtil.getCurrentUser();
                            if(bean.getIsShareMaker()==2){
                                Toast.makeText(context, "您已经是分享者了！", Toast.LENGTH_SHORT).show();
                            }else {
                                ARouter.getInstance().build("/user/main/act/join").navigation();
                            }
                        }
                    }
                });

                //沃粉
                holder.llToFan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance().build("/main/act/user/fan").navigation();
                    }
                });

                //分享
                holder.llToShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       if(UserAuthUtil.isUserLogin()){
                           HashMap<String,Object> data=new HashMap<>();
                           data.put("type",2);
                           SocialShareDialog dialog=new SocialShareDialog(context,data);
                           dialog.show();
                       }else {
                           Toast.makeText(context, "请登录后操作", Toast.LENGTH_SHORT).show();
                           ARouter.getInstance().build("/main/act/login").navigation();
                       }
                    }
                });

                //订单
                holder.llToOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ARouter.getInstance().build("/user/main/act/indent").navigation();
                    }
                });

                //加入代理商
                holder.llAgent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!UserAuthUtil.isUserLogin()){
                            Toast.makeText(context, "请登录后操作", Toast.LENGTH_SHORT).show();
                            ARouter.getInstance().build("/main/act/login").navigation();
                        }else {
                            if(null!=callBack){
                                callBack.joinAgentClick();
                            }
                        }


                    }
                });

                break;
            case TYPE_3:

                HashMap<String, Object> map3 = data.get(0);
                if (map3.containsKey("brand_store_list")) {
                    final List<StoreBean> list = (List<StoreBean>) map3.get("brand_store_list");

                    if (list != null && list.size() > 4) {
                       /* PicassoUtil.getPicassoObject().
                                load(list.get(4).getLogo()).tag("home_rv_img").
                                resize(DpUtils.dpToPx(context,80),DpUtils.dpToPx(context,80)).
                                error(R.mipmap.icon_load_faild).into(holder.ivStore5);*/
                        Glide.with(context).load(list.get(4).getLogo()).placeholder(R.drawable.ic_defult_load).crossFade()
                                .error(R.drawable.ic_defult_error)
                               .into(holder.ivStore5);

                        holder.tvStoreName5.setText(list.get(4).getName());
                        if(list.get(4).getPopularity()>99){
                            holder.tvStorePopularity5.setText(99 + "+");
                        }else {
                            holder.tvStorePopularity5.setText(list.get(4).getPopularity() + "");
                        }

                        holder.rlBrand5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ARouter.getInstance().build("/main/act/store").withInt("id", list.get(4).getId()).navigation();
                            }
                        });


                    }
                    if (list != null && list.size() > 3) {
                       /* PicassoUtil.getPicassoObject().load(list.get(3).getLogo())
                                .tag("home_rv_img").resize(DpUtils.dpToPx(context,80),DpUtils.dpToPx(context,80)).
                                error(R.mipmap.icon_load_faild).
                                into(holder.ivStore4);*/
                        Glide.with(context).load(list.get(3).getLogo())
                                .error(R.drawable.ic_defult_error)
                                .placeholder(R.drawable.ic_defult_load).crossFade()
                                .into(holder.ivStore4);
                        holder.tvStoreName4.setText(list.get(3).getName());
                        if(list.get(3).getPopularity()>99){
                            holder.tvStorePopularity4.setText(99 + "+");
                        }else {
                            holder.tvStorePopularity4.setText(list.get(3).getPopularity() + "");
                        }
                        holder.rlBrand4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ARouter.getInstance().build("/main/act/store").withInt("id", list.get(3).getId()).navigation();
                            }
                        });
                    }

                    if (list != null && list.size() > 2) {
                        /*PicassoUtil.getPicassoObject()
                                .load(list.get(2).getLogo()).tag("home_rv_img")
                                .resize(DpUtils.dpToPx(context,80),DpUtils.dpToPx(context,80))
                                .error(R.mipmap.icon_load_faild).into(holder.ivStore3);*/
                        Glide.with(context).load(list.get(2).getLogo())
                                .error(R.drawable.ic_defult_error)
                                .placeholder(R.drawable.ic_defult_load).crossFade()
                                .into(holder.ivStore3);
                        holder.tvStoreName3.setText(list.get(2).getName());
                        if(list.get(2).getPopularity()>99){
                            holder.tvStorePopularity3.setText(99 + "+");
                        }else {
                            holder.tvStorePopularity3.setText(list.get(2).getPopularity() + "");
                        }

                        holder.rlBrand3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ARouter.getInstance().build("/main/act/store").withInt("id", list.get(2).getId()).navigation();
                            }
                        });
                    }
                    if (list != null && list.size() > 1) {
                       /* PicassoUtil.getPicassoObject()
                                .load(list.get(1).getLogo()).tag("home_rv_img")
                                .resize(DpUtils.dpToPx(context,80),DpUtils.dpToPx(context,80))
                                .error(R.mipmap.icon_load_faild).into(holder.ivStore2);*/
                        Glide.with(context).load(list.get(1).getLogo())
                                .error(R.drawable.ic_defult_error)
                                .placeholder(R.drawable.ic_defult_load).crossFade()
                                .into(holder.ivStore2);

                        holder.tvStoreName2.setText(list.get(1).getName());
                        if(list.get(1).getPopularity()>99){
                            holder.tvStorePopularity2.setText(99 + "+");
                        }else {
                            holder.tvStorePopularity2.setText(list.get(1).getPopularity() + "");
                        }

                        holder.rlBrand2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ARouter.getInstance().build("/main/act/store").withInt("id", list.get(1).getId()).navigation();
                            }
                        });
                    }

                    if (list != null && list.size() > 0) {
                       /* PicassoUtil.getPicassoObject()
                                .load(list.get(0).getLogo())
                                .tag("home_rv_img").resize(DpUtils.dpToPx(context,80),DpUtils.dpToPx(context,80)).error(R.mipmap.icon_load_faild)
                                .into(holder.ivStore1);*/
                        Glide.with(context).load(list.get(0).getLogo())
                                .error(R.drawable.ic_defult_error)
                                .placeholder(R.drawable.ic_defult_load).crossFade()
                                .into(holder.ivStore1);

                        holder.tvStoreName1.setText(list.get(0).getName());
                        if(list.get(0).getPopularity()>99){
                            holder.tvStorePopularity1.setText(99 + "+");
                        }else {
                            holder.tvStorePopularity1.setText(list.get(0).getPopularity() + "");
                        }
                        holder.llBrand1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ARouter.getInstance().build("/main/act/store").withInt("id", list.get(0).getId()).navigation();
                            }
                        });
                    }

                }

                break;
            case TYPE_4:
                HashMap<String, Object> map4 = data.get(0);
                if (map4.containsKey("sing_product_list")) {
                    final List<ProjectBean> list = (List<ProjectBean>) map4.get("sing_product_list");

                    if (list != null && list.size() > 2) {
                        /*PicassoUtil.getPicassoObject()
                                .load(list.get(2).getLogoUrl()).tag("home_rv_img")
                                .resize(DpUtils.dpToPx(context,80),DpUtils.dpToPx(context,80))
                                .error(R.mipmap.icon_load_faild).into(holder.ivProduct3);*/
                        Glide.with(context).load(list.get(2).getLogoUrl())
                                .placeholder(R.drawable.ic_defult_load).crossFade()
                                .error(R.drawable.ic_defult_error)
                                .into(holder.ivProduct3);


                        holder.tvProductName3.setText(list.get(2).getName());
                        holder.tvProductDescribe3.setText(list.get(2).getDescribe());
                        holder.llProduct3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ARouter.getInstance().build("/main/act/project_detail").withInt("id", list.get(2).getId()).navigation();
                            }
                        });
                    }

                    if (list != null && list.size() > 1) {
                        /*PicassoUtil.getPicassoObject()
                                .load(list.get(1).getLogoUrl()).tag("home_rv_img")
                                .resize(DpUtils.dpToPx(context,80),DpUtils.dpToPx(context,80))
                                .error(R.mipmap.icon_load_faild).into(holder.ivProduct2);*/
                        Glide.with(context).load(list.get(1).getLogoUrl())
                                .placeholder(R.drawable.ic_defult_load).crossFade()
                                .error(R.drawable.ic_defult_error)
                                .into(holder.ivProduct2);
                        holder.tvProductName2.setText(list.get(1).getName());
                        holder.tvProductDescribe2.setText(list.get(1).getDescribe());
                        holder.llProduct2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ARouter.getInstance().build("/main/act/project_detail").withInt("id", list.get(1).getId()).navigation();
                            }
                        });
                    }

                    if (list != null && list.size() > 0) {
                       /* PicassoUtil.getPicassoObject().load(list.get(0).getLogoUrl()).tag("home_rv_img")
                                .resize(DpUtils.dpToPx(context,80),DpUtils.dpToPx(context,80))
                                .error(R.mipmap.icon_load_faild).into(holder.ivProduct1);*/
                        Glide.with(context).load(list.get(0).getLogoUrl())
                                .placeholder(R.drawable.ic_defult_load).crossFade()
                                .error(R.drawable.ic_defult_error)
                                .into(holder.ivProduct1);

                        holder.tvProductName1.setText(list.get(0).getName());
                        holder.tvProductDescribe1.setText(list.get(0).getDescribe());
                        holder.llProduct1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ARouter.getInstance().build("/main/act/project_detail").withInt("id", list.get(0).getId()).navigation();
                            }
                        });
                    }
                    //查看平台所有项目
                    holder.rlToMoreProduct.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ARouter.getInstance().build("/main/act/project_list").navigation();
                        }
                    });

                }

                break;
            case TYPE_5:

                break;

            case TYPE_6:
                final StoreBean sb = (StoreBean) data.get(position).get("store");
                /*PicassoUtil.getPicassoObject()
                        .load(sb.getLogo()).tag("home_rv_img")
                        .resize(DpUtils.dpToPx(context,80),DpUtils.dpToPx(context,80))
                        .error(R.mipmap.icon_load_faild).into(holder.ivStoreLogo);*/

                Glide.with(context).load(sb.getLogo())
                        .placeholder(R.drawable.ic_defult_load).crossFade()
                        .error(R.drawable.ic_defult_error)
                        .into(holder.ivStoreLogo);

                holder.tvStoreName.setText(sb.getName());
                holder.item_home_lay_6_ll_container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                                 /main/act/AdressManager
                        ARouter.getInstance().build("/main/act/store2").withInt("id", sb.getId()).navigation();
                    }
                });
                if(sb.getLongitude()==0d|| sb.getLatitude()==0d){
                    holder.tvStoreDistance.setText("距离计算错误");
                } else {
                    holder.tvStoreDistance.setText(calDistance(sb.getLongitude(),sb.getLatitude())+"Km");
                }
                float score= (float) sb.getScore();
                holder.ratingView.setRating(score);
                holder.ivTextRq.setText("人气："+sb.getPopularity());
                holder.home_lay_6_tv_describe.setText(sb.getDetail());
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {

        switch (type) {
            case 1:
                return TYPE_1;
            case 2:
                return TYPE_2;
            case 3:
                return TYPE_3;
            case 4:
                return TYPE_4;
            case 5:
                return TYPE_5;
            case 6:
                return TYPE_6;

            default:
                Log.e("type_Adapter",type+"");
                return -1;
        }
    }

    // 返回Item数目
    @Override
    public int getItemCount() {
          LogUtil.e("test  datasize:   ",data.size()+"");
        return data == null ? 0 : data.size();
    }


    //定义Viewholder
    class MainViewHolder extends RecyclerView.ViewHolder {
        //type=1
        TextView tvBalance, tvBonus, tvConsume;
        LinearLayout llExpend,llProfit;
        //type=2
        LinearLayout llToPay, llToDeposit, llToBill, llToJoin, llToFan, llToShare, llToOrder,llAgent;
        //type=3
        ImageView ivStore1, ivStore2, ivStore3, ivStore4, ivStore5;
        TextView tvStoreName1, tvStoreName2, tvStoreName3, tvStoreName4, tvStoreName5;
        TextView tvStorePopularity1, tvStorePopularity2, tvStorePopularity3, tvStorePopularity4, tvStorePopularity5;
        LinearLayout llBrand1;
        RelativeLayout rlBrand2, rlBrand3, rlBrand4, rlBrand5;


        //type=4
        ImageView ivProduct1, ivProduct2, ivProduct3;
        TextView tvProductName1, tvProductName2, tvProductName3, tvProductDescribe1, tvProductDescribe2, tvProductDescribe3;
        RelativeLayout rlToMoreProduct;
        LinearLayout llProduct1, llProduct2, llProduct3;
        //type=5

      //type=6
        ImageView ivStoreLogo;
        TextView tvStoreName, tvStoreDistance;
        RatingView ratingView;
        TextView ivTextRq;
        LinearLayout item_home_lay_6_ll_container;
        TextView home_lay_6_tv_describe;


        public MainViewHolder(View root, int viewtype) {
            super(root);
            switch (viewtype) {

                case 1:
                    tvBalance = (TextView) root.findViewById(R.id.home_lay_1_balance);
                    tvBonus = (TextView) root.findViewById(R.id.home_lay_1_bonus);
                    tvConsume = (TextView) root.findViewById(R.id.home_lay_1_consume);
                    llExpend= (LinearLayout) root.findViewById(R.id.home_lay_1_ll_expend);
                    llProfit= (LinearLayout) root.findViewById(R.id.home_lay_1_ll_profit);
                    break;

                case 2:
                    llToPay = (LinearLayout) root.findViewById(R.id.home_ll_pay);
                    llToDeposit = (LinearLayout) root.findViewById(R.id.home_ll_deposit);
                    llToBill = (LinearLayout) root.findViewById(R.id.home_ll_bill);
                    llToJoin = (LinearLayout) root.findViewById(R.id.home_ll_join);
                    llToFan = (LinearLayout) root.findViewById(R.id.home_ll_fan);
                    llToShare = (LinearLayout) root.findViewById(R.id.home_ll_share);
                    llToOrder = (LinearLayout) root.findViewById(R.id.home_ll_order);
                    llAgent= (LinearLayout) root.findViewById(R.id.home_ll_agent);
                    break;

                case 3:
                    ivStore1 = (ImageView) root.findViewById(R.id.home_lay_3_iv_1);
                    ivStore2 = (ImageView) root.findViewById(R.id.home_lay_3_iv_2);
                    ivStore3 = (ImageView) root.findViewById(R.id.home_lay_3_iv_3);
                    ivStore4 = (ImageView) root.findViewById(R.id.home_lay_3_iv_4);
                    ivStore5 = (ImageView) root.findViewById(R.id.home_lay_3_iv_5);

                    tvStoreName1 = (TextView) root.findViewById(R.id.home_lay_3_tv_name_1);
                    tvStoreName2 = (TextView) root.findViewById(R.id.home_lay_3_tv_name_2);
                    tvStoreName3 = (TextView) root.findViewById(R.id.home_lay_3_tv_name_3);
                    tvStoreName4 = (TextView) root.findViewById(R.id.home_lay_3_tv_name_4);
                    tvStoreName5 = (TextView) root.findViewById(R.id.home_lay_3_tv_name_5);

                    tvStorePopularity1 = (TextView) root.findViewById(R.id.home_lay_3_tv_popularity_1);
                    tvStorePopularity2 = (TextView) root.findViewById(R.id.home_lay_3_tv_popularity_2);
                    tvStorePopularity3 = (TextView) root.findViewById(R.id.home_lay_3_tv_popularity_3);
                    tvStorePopularity4 = (TextView) root.findViewById(R.id.home_lay_3_tv_popularity_4);
                    tvStorePopularity5 = (TextView) root.findViewById(R.id.home_lay_3_tv_popularity_5);

                    llBrand1 = (LinearLayout) root.findViewById(R.id.home_lay_1_ll_brand_1);
                    rlBrand2 = (RelativeLayout) root.findViewById(R.id.home_lay_1_rl_brand_2);
                    rlBrand3 = (RelativeLayout) root.findViewById(R.id.home_lay_1_rl_brand_3);
                    rlBrand4 = (RelativeLayout) root.findViewById(R.id.home_lay_1_rl_brand_4);
                    rlBrand5 = (RelativeLayout) root.findViewById(R.id.home_lay_1_rl_brand_5);
                    break;
                case 4:
                    ivProduct1 = (ImageView) root.findViewById(R.id.home_lay_4_iv_1);
                    ivProduct2 = (ImageView) root.findViewById(R.id.home_lay_4_iv_2);
                    ivProduct3 = (ImageView) root.findViewById(R.id.home_lay_4_iv_3);

                    tvProductName1 = (TextView) root.findViewById(R.id.home_lay_4_tv_name_1);
                    tvProductName2 = (TextView) root.findViewById(R.id.home_lay_4_tv_name_2);
                    tvProductName3 = (TextView) root.findViewById(R.id.home_lay_4_tv_name_3);

                    tvProductDescribe1 = (TextView) root.findViewById(R.id.home_lay_4_tv_describe_1);
                    tvProductDescribe2 = (TextView) root.findViewById(R.id.home_lay_4_tv_describe_2);
                    tvProductDescribe3 = (TextView) root.findViewById(R.id.home_lay_4_tv_describe_3);

                    rlToMoreProduct = (RelativeLayout) root.findViewById(R.id.home_lay_4_rl_to_more_product);

                    llProduct1 = (LinearLayout) root.findViewById(R.id.home_lay_4_ll_product_1);
                    llProduct2 = (LinearLayout) root.findViewById(R.id.home_lay_4_ll_product_2);
                    llProduct3 = (LinearLayout) root.findViewById(R.id.home_lay_4_ll_product_3);

                    break;
                case 5:

                    break;

                case 6:
                    ivStoreLogo = (ImageView) root.findViewById(R.id.home_lay_6_iv_logo);
                    tvStoreName = (TextView) root.findViewById(R.id.home_lay_6_tv_name);
                    tvStoreDistance = (TextView) root.findViewById(R.id.home_lay_6_tv_distance);
                    ratingView= (RatingView) root.findViewById(R.id.home_lay_6_ratingView);
                    ivTextRq= (TextView) root.findViewById(R.id.home_lay_6_rq);
                    item_home_lay_6_ll_container= (LinearLayout) root.findViewById(R.id.item_home_lay_6_ll_container);
                    home_lay_6_tv_describe= (TextView) root.findViewById(R.id.home_lay_6_tv_describe);
                    break;


            }
        }
    }

    //计算距离
    private String calDistance(double longt,double lat){
        LatLng latLng=new LatLng(Double.valueOf((String)SPFUtil.get(Tag.TAG_LATITUDE,"0")),Double.valueOf((String)SPFUtil.get(Tag.TAG_LONGITUDE,"0")));
        float distance = AMapUtils.calculateLineDistance(latLng ,new LatLng(lat,longt))/1000;
        String str=String.valueOf(distance);
        str=str.substring(0,str.indexOf(".")+2);
        return str;
    }

    public interface HomeMultCallBack{
        public void joinAgentClick();
    }
    private HomeMultCallBack callBack;

    public void setClickAgent(HomeMultCallBack callBack){
        this.callBack=callBack;

    }

}