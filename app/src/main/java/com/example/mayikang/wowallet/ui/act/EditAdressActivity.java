package com.example.mayikang.wowallet.ui.act;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.services.route.Path;
import com.bigkoo.pickerview.OptionsPickerView;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.modle.javabean.AdressBean;
import com.example.mayikang.wowallet.modle.javabean.ProvincialCityBean;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.SPFUtil;
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

@Route(path = "/main/act/EditAdress")
//编辑，添加收货地址
public class EditAdressActivity extends BaseAppcompatActivity {

    @BindView(R.id.tv_add_edit_adress)
    TextView tvAddEditAdress;
    @BindView(R.id.edt_addadress_name)
    EditText edtAddadressName;
    @BindView(R.id.edt_addadress_phone)
    EditText edtAddadressPhone;
    @BindView(R.id.tv_adress)
    TextView tvAdress;
    @BindView(R.id.edt_postalcode)
    EditText edtPostalcode;
    @BindView(R.id.edt_adress)
    EditText edtAdress;
    @Autowired(name="bean")
    AdressBean bean;

    private boolean isLoaded = false;
    private HttpServiceImpl service;
    private int areaId=-1;//区域id
    private ArrayList<ProvincialCityBean> options11 = new ArrayList<>();
    private ArrayList<ArrayList<String>> options22 = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options33 = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<ProvincialCityBean.CityBean.AreaBean>>> options3id = new ArrayList<>();
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 4:
                    isLoaded=true;
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        initData();
        if(bean!=null){
            tvAddEditAdress.setText("编辑收货地址");
            edtAddadressName.setText(bean.getName());
            edtAddadressPhone.setText(bean.getPhone());
            tvAdress.setText(bean.getAdressRegion());
            edtPostalcode.setText(bean.getPostalcode());
            edtAdress.setText(bean.getAdress());
        }else {
            tvAddEditAdress.setText("新增收货地址");
        }
    }

    private void initData() {
            //获取省下列表
            if ( SPFUtil.get("city", "none").equals("none")) {
                getProvinceList();
            } else {
                String result = (String) SPFUtil.get("city", "none");
                getCity(result);
            }
        }

    @Override
    public int initLayout() {
        return R.layout.activity_edit_adress;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.storemanage_linear_back, R.id.rela_choice_adress, R.id.lin_adress_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //返回
            case R.id.storemanage_linear_back:
                finish();
                break;
            //选择省市区
            case R.id.rela_choice_adress:
                if(isLoaded){
                    //关闭输入法
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(EditAdressActivity.this.getCurrentFocus().getWindowToken()
                            , InputMethodManager.HIDE_NOT_ALWAYS);
                    //城市选择器
                    choiceAdress();
                }
                break;
            //保存
            case R.id.lin_adress_save:
                break;
        }
    }

    //选择地址
    private void choiceAdress() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                tvAdress.setText(options11.get(options1).getPickerViewText() + "-"
                        + options22.get(options1).get(options2) + "-"
                        + options33.get(options1).get(options2).get(options3));
                areaId = options3id.get(options1).get(options2).get(options3).getId();
            }
        })
                .setTitleText("选择地址")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();
        pvOptions.setPicker(options11, options22, options33);//三级选择器
        pvOptions.show();

    }

    //获取省下列表
    private void getProvinceList() {
        String s="http://118.123.22.190:8003/tjsj_dmcl/areaJson.htm";
        HashMap<String, String> map = new HashMap<>();
        service.doCommonPost(null,s, map, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("ProvinceonSuccess", result.toString());
                if (!StringUtil.isBlank(result) && result != null) {
                    SPFUtil.put("city", result);
                    //解析地址json
                    getCity(result);
                }
            }
            @Override
            public void onError(Throwable ex) {
                LogUtil.e("ProvinceonError", ex.toString());
            }
            @Override
            public void onCancelled(Callback.CancelledException cex) {}
            @Override
            public void onFinished() {
                dismissLoading();
            }
            @Override
            public void onWaiting() {}
            @Override
            public void onStarted() {
                showLoading(false,"加载中...");}
            @Override
            public void onLoading(long total, long current) {}
        });
    }

    //解析地址json
    private void getCity(String result) {
        try {
            JSONObject object = new JSONObject(result);
            boolean results = object.getBoolean("result");
            if (results) {
                JSONArray array = object.getJSONArray("resultData");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object1 = array.getJSONObject(i);
                    ProvincialCityBean bean = new ProvincialCityBean();
                    List<ProvincialCityBean.CityBean> cityBeanList = new ArrayList<ProvincialCityBean.CityBean>();
                    String name = object1.getString("areaName");
                    int id = object1.getInt("id");
                    JSONArray array1 = object1.getJSONArray("children");
                    for (int j = 0; j < array1.length(); j++) {
                        JSONObject object2 = array1.getJSONObject(j);
                        ProvincialCityBean.CityBean bean1 = new ProvincialCityBean.CityBean();
                        List<ProvincialCityBean.CityBean.AreaBean> areaBeanList = new ArrayList<ProvincialCityBean.CityBean.AreaBean>();
                        String name1 = object2.getString("areaName");
                        int id1 = object2.getInt("id");
                        JSONArray array2 = object2.getJSONArray("children");
                        for (int k = 0; k < array2.length(); k++) {
                            JSONObject object3 = array2.getJSONObject(k);
                            ProvincialCityBean.CityBean.AreaBean areaBean = new ProvincialCityBean.CityBean.AreaBean();
                            String name2 = object3.getString("areaName");
                            int id2 = object3.getInt("id");
                            areaBean.setName(name2);
                            areaBean.setId(id2);
                            areaBeanList.add(areaBean);
                        }
                        bean1.setName(name1);
                        bean1.setId(id1);
                        bean1.setAreaBeanList(areaBeanList);
                        cityBeanList.add(bean1);
                    }
                    bean.setName(name);
                    bean.setId(id);
                    bean.setCityBeanList(cityBeanList);
                    options11.add(bean);
                    Message message=Message.obtain();
                    message.what=4;
                    mHandler.sendMessage(message);
                }
                analysisi(options11);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtil.e("ProvinceJSONException", e.toString());
        }
    }

    private void analysisi(ArrayList<ProvincialCityBean> options11) {
        for (int i = 0; i < options11.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
            ArrayList<ArrayList<ProvincialCityBean.CityBean.AreaBean>> Province_AreaList_id = new ArrayList<>();//该省的所有地区列表（第三极）
            for (int c = 0; c < options11.get(i).getCityBeanList().size(); c++) {//遍历该省份的所有城市
                String CityName = options11.get(i).getCityBeanList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表
                ArrayList<ProvincialCityBean.CityBean.AreaBean> City_AreaList_id = new ArrayList<>();//该城市的所有地区列表
                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (options11.get(i).getCityBeanList().get(c).getAreaBeanList() == null
                        || options11.get(i).getCityBeanList().get(c).getAreaBeanList().size() == 0) {
                    ProvincialCityBean.CityBean.AreaBean bean = new ProvincialCityBean.CityBean.AreaBean();
                    bean.setName("");
                    bean.setId(-1);
                    City_AreaList.add("");
                    City_AreaList_id.add(bean);
                } else {

                    for (int d = 0; d < options11.get(i).getCityBeanList().get(c).getAreaBeanList().size(); d++) {//该城市对应地区所有数据
                        String AreaName = options11.get(i).getCityBeanList().get(c).getAreaBeanList().get(d).getName();
                        int id = options11.get(i).getCityBeanList().get(c).getAreaBeanList().get(d).getId();
                        ProvincialCityBean.CityBean.AreaBean bean = new ProvincialCityBean.CityBean.AreaBean();
                        bean.setName(AreaName);
                        bean.setId(id);
                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                        City_AreaList_id.add(bean);
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据

                Province_AreaList_id.add(City_AreaList_id);
            }

            /**
             * 添加城市数据
             */
            options22.add(CityList);

            /**
             * 添加地区数据
             */
            options33.add(Province_AreaList);
            options3id.add(Province_AreaList_id);
        }
    }


}
