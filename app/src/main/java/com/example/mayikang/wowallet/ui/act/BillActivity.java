package com.example.mayikang.wowallet.ui.act;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bigkoo.pickerview.TimePickerView;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.BillAdapter;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.BillBean;
import com.example.mayikang.wowallet.modle.javabean.ExpendUserBean;
import com.example.mayikang.wowallet.modle.javabean.IncomeUserBean;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 账单页面
 */
@Route(path = "/main/act/user/bill")
public class BillActivity extends BaseAppcompatActivity {

    @BindView(R.id.mine_bill_back)
    RelativeLayout mineBillBack;

    @BindView(R.id.bill_Txtday)
    TextView billTxtday;

    @BindView(R.id.bill_Txtexpend)
    TextView billTxtexpend;

    @BindView(R.id.bill_Txtincome)
    TextView billTxtIncome;

    @BindView(R.id.bill_Imgdate)
    LinearLayout billImgdate;

    @BindView(R.id.act_bill_rv)
    RecyclerView mRV;

    @BindView(R.id.bill_img)
    ImageView billImg;
    @BindView(R.id.act_bill_refresh)
    MaterialRefreshLayout refreshLayout;
    @BindView(R.id.bill_layout)
    RelativeLayout billLayout;
    @BindView(R.id.bill_No_layout)
    LinearLayout billNoLayout;


    private HttpServiceImpl http;
    private BillAdapter adapter;
    private List<BillBean> data = new ArrayList<>();
    private int index = 1;
    private String currentMonth;
    private boolean flag=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        http = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();

        initRV();

        /**
         * 默认查询当前月份的账单
         */
        DateFormat format = new SimpleDateFormat("yyyy-MM");
        currentMonth = format.format(new Date());

        queryBillByMonth(currentMonth);

    }

    private void initRV() {
        adapter = new BillAdapter(BillActivity.this, data);
        mRV.setLayoutManager(new LinearLayoutManager(BillActivity.this));
        mRV.setAdapter(adapter);
        refreshLayout.setLoadMore(true);
        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                flag=false;
                queryBillByMonth(currentMonth);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                queryMoreBillByMonth(currentMonth);
            }
        });
    }

    /**
     * 按月份查询账单
     *
     * @param month
     */
    public void queryBillByMonth(String month) {
        index = 1;
        data.clear();
        HashMap<String, String> map = new HashMap<>();
        map.put("pageIndex", String.valueOf(index));
        map.put("time", month);
        map.put("orderby", "id desc");
        map.put("cond", "{isDelete:1}");
        map.put("jf", "incomeUser|expenditureUser|photo");
        http.doCommonPost(null, MainUrl.queryBillUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                Log.e("账单", resultStr.toString());
                if (!StringUtil.isBlank(resultStr)) {
                    try {
                        JSONObject obj = new JSONObject(resultStr);
                        //查询月份
                        String month = obj.getString("time");
                        billTxtday.setText(month);
                        //总支出
                        Double totAmount = obj.getDouble("totExpenditureAmount");
                        billTxtexpend.setText("总支出￥" + new DecimalFormat("######0.00").format(totAmount));

                        //总收入
                        Double totIncomeAmount = obj.getDouble("totIncomeAmount");
                        billTxtIncome.setText("总收入￥" + new DecimalFormat("######0.00").format(totIncomeAmount));

                        JSONArray resultList = obj.getJSONArray("resultList");

                        if (resultList != null && resultList.length() > 0) {
                            index++;

                            for (int i = 0; i < resultList.length(); i++) {
                                JSONObject bill = resultList.getJSONObject(i);
                                //账单 id
                                int id = bill.getInt("id");

                                //流水类型 {1：收入  2：支出}
                                int fType = bill.getInt("fType");

                                /**
                                 * 支出方帐号
                                 */
                                int expendId = 0;
                                ExpendUserBean eub = null;
                                String expenditureUsers = bill.getString("expenditureUser");
                                if (!expenditureUsers.equals("null")) {
                                    JSONObject expenditureUser = bill.getJSONObject("expenditureUser");
                                    expendId = expenditureUser.getInt("id");
                                    String expendLogoStr = expenditureUser.getString("photo");
                                    String expendLogo = null;
                                    if (!StringUtil.isBlank(expendLogoStr)) {
                                        expendLogo = expenditureUser.getJSONObject("photo").getString("url");
                                    }
                                    eub = new ExpendUserBean();
                                    eub.setId(expendId);
                                    eub.setLogo(expendLogo);
                                }
                                /**
                                 * 收入方帐号
                                 */

                                JSONObject incomeUser = bill.getJSONObject("incomeUser");
                                int incomeId = incomeUser.getInt("id");
                                String incomeLogoStr = incomeUser.getString("photo");
                                String incomeLogo = null;
                                if (!StringUtil.isBlank(incomeLogoStr)) {
                                    incomeLogo = incomeUser.getJSONObject("photo").getString("url");
                                }

                                IncomeUserBean iub = new IncomeUserBean();
                                iub.setId(incomeId);
                                iub.setLogo(incomeLogo);


                                //操作金额
                                double amount = bill.getDouble("amount");

                                //交易描述
                                String desc = bill.getString("desc_");

                                //交易时间
                                String insertTime = bill.getString("insertTime");

                                BillBean bb = new BillBean();
                                bb.setId(id);

                                //判断 fType 对应用户 id
                                if (UserAuthUtil.getUserId() == expendId) {
                                    fType = 2;
                                }
                                if (UserAuthUtil.getUserId() == incomeId) {
                                    fType = 1;
                                }
                                if (expendId == 0) {
                                    fType = 1;
                                }

                                bb.setfType(fType);
                                bb.setDesc(desc);
                                bb.setAmount(amount);
                                bb.setInsertTime(insertTime);
                                bb.setIncome(iub);
                                bb.setExpend(eub);
                                data.add(bb);

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("bill", e.toString());
                    } finally {
                        if(data.size()>0){
                            billLayout.setVisibility(View.VISIBLE);
                            billNoLayout.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        }else {
                            billLayout.setVisibility(View.GONE);
                            billNoLayout.setVisibility(View.VISIBLE);
                        }


                    }
                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("账单", ex.toString());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                refreshLayout.finishRefresh();
                refreshLayout.finishRefreshLoadMore();
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

    public void queryMoreBillByMonth(String month) {

        HashMap<String, String> map = new HashMap<>();
        map.put("pageIndex", String.valueOf(index));
        map.put("time", month);
        map.put("orderby", "id desc");
        map.put("cond", "{isDelete:1}");
        map.put("jf", "incomeUser|expenditureUser|photo");
        http.doCommonPost(null, MainUrl.queryBillUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                Log.e("账单", resultStr);

                if (!StringUtil.isBlank(resultStr)) {
                    try {
                        JSONObject obj = new JSONObject(resultStr);
                        //查询月份
                        String month = obj.getString("time");
                        billTxtday.setText(month);
                        //总支出
                        Double totAmount = obj.getDouble("totExpenditureAmount");
                        billTxtexpend.setText("总支出￥" + new DecimalFormat("######0.00").format(totAmount));

                        //总收入
                        Double totIncomeAmount = obj.getDouble("totIncomeAmount");
                        billTxtIncome.setText("总收入￥" + new DecimalFormat("######0.00").format(totIncomeAmount));

                        JSONArray resultList = obj.getJSONArray("resultList");

                        if (resultList != null && resultList.length() > 0) {
                            index++;

                            for (int i = 0; i < resultList.length(); i++) {
                                JSONObject bill = resultList.getJSONObject(i);
                                //账单 id
                                int id = bill.getInt("id");

                                //流水类型 {1：收入  2：支出}
                                int fType = bill.getInt("fType");

                                /**
                                 * 支出方帐号
                                 */

                                JSONObject expenditureUser = bill.getJSONObject("expenditureUser");
                                int expendId = expenditureUser.getInt("id");
                                String expendLogoStr = expenditureUser.getString("photo");
                                String expendLogo = null;
                                if (!StringUtil.isBlank(expendLogoStr)) {
                                    expendLogo = expenditureUser.getJSONObject("photo").getString("url");
                                }

                                ExpendUserBean eub = new ExpendUserBean();
                                eub.setId(expendId);
                                eub.setLogo(expendLogo);
                                /**
                                 * 收入方帐号
                                 */

                                JSONObject incomeUser = bill.getJSONObject("incomeUser");
                                int incomeId = expenditureUser.getInt("id");
                                String incomeLogoStr = expenditureUser.getString("photo");
                                String incomeLogo = null;
                                if (!StringUtil.isBlank(incomeLogoStr)) {
                                    incomeLogo = expenditureUser.getJSONObject("photo").getString("url");
                                }

                                IncomeUserBean iub = new IncomeUserBean();
                                iub.setId(incomeId);
                                iub.setLogo(incomeLogo);


                                //操作金额
                                double amount = bill.getDouble("amount");

                                //交易描述
                                String desc = bill.getString("desc_");

                                //交易时间
                                String insertTime = bill.getString("insertTime");

                                BillBean bb = new BillBean();
                                bb.setId(id);
                                bb.setfType(fType);
                                bb.setDesc(desc);
                                bb.setAmount(amount);
                                bb.setInsertTime(insertTime);
                                bb.setIncome(iub);
                                bb.setExpend(eub);
                                data.add(bb);

                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if(data.size()>0){
                            billLayout.setVisibility(View.VISIBLE);
                            billNoLayout.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        }else {
                            billLayout.setVisibility(View.GONE);
                            billNoLayout.setVisibility(View.VISIBLE);
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
                refreshLayout.finishRefresh();
                refreshLayout.finishRefreshLoadMore();
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

    @Override
    public int initLayout() {

        return R.layout.activity_bill;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.mine_bill_back, R.id.bill_Imgdate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mine_bill_back:
                finish();
                break;
            //弹出选择时间
            case R.id.bill_Imgdate:
                popMonthChoose();
                break;
        }
    }


    /**
     * 弹出时间选择
     */
    private void popMonthChoose() {
//        TimePickerView tpv = new TimePickerView(BillActivity.this, TimePickerView.Type.YEAR_MONTH);
//        Calendar calendar=Calendar.getInstance();
//        final int year=calendar.get(Calendar.YEAR);
//
//        tpv.setRange(2010,year);
//        tpv.setTitle("选择月份");
//        tpv.setCancelable(true);
//        tpv.setCancelText("取消");
//        tpv.setSubmitText("确认");
//        tpv.setTime(new Date());
//
//
//        tpv.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
//            @Override
//            public void onTimeSelect(Date date) {
//
//                boolean flag = CheckIsMoreCurrDate(date);
//                if(flag){
//
//                    DateFormat format = new SimpleDateFormat("yyyy-MM");
//                    currentMonth = format.format(date);
//                    queryBillByMonth(currentMonth);
//                }else {
//                    Toast.makeText(BillActivity.this, "请选择正确的月份！", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        tpv.show();


        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Calendar selectedDate = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();

        Calendar startDate = Calendar.getInstance();
        startDate.set(calendar.get(Calendar.YEAR) - 10,0,1);
        //时间选择器
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null

                if(flag){
                    DateFormat format = new SimpleDateFormat("yyyy-MM");
                    currentMonth = format.format(date);
                    queryBillByMonth(currentMonth);
                }else {
                    Toast.makeText(BillActivity.this, "请选择正确的月份！", Toast.LENGTH_SHORT).show();
                }
            }
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, false, false, false, false})
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedDate)
                .setTitleText("选择月份")
                .setRange(calendar.get(Calendar.YEAR)-10, calendar.get(Calendar.YEAR))
                .setRangDate(startDate,selectedDate)
                .setLabel("年","月","","","","")
                .isCenterLabel(true)//是否每项item都有label，false代表是
                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .setDecorView(null)
                .build();

        pvTime.show();
    }
    //判断选中的月份是否超过了当前的月份
    private boolean CheckIsMoreCurrDate(Date date) {

        //获取当前的月份
        Calendar calendar=Calendar.getInstance();
        int mouth=calendar.get(Calendar.MONTH)+1;
        int year=calendar.get(Calendar.YEAR);

        //获取用户选择的月份
        DateFormat userCheckMouch =new SimpleDateFormat("MM");
        String s=userCheckMouch.format(date).trim();
        int userCheckMouth=Integer.valueOf(s);

        //获取用户选择的年份
        DateFormat userCheckYear =new SimpleDateFormat("yyyy");
        String y=userCheckYear.format(date);
        int userCheckYears=Integer.valueOf(y);
        if(year>userCheckYears){
            //用户选择的年小于当前月
            return true;
        }else {
            if(userCheckMouth>mouth){
                Log.e("data",userCheckMouth+"--------"+mouth+"");
                return false;
            }else {
                return true;
            }
        }

    }

}
