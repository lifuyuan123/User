package com.example.mayikang.wowallet.ui.act;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.MyGridViewAdapter;
import com.example.mayikang.wowallet.adapter.SortAdapter;
import com.example.mayikang.wowallet.db.RegionDAO;
import com.example.mayikang.wowallet.event.AreaEvent;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.RegionInfo;
import com.example.mayikang.wowallet.modle.javabean.SortModel;
import com.example.mayikang.wowallet.ui.xwidget.view.ClearEditText;
import com.example.mayikang.wowallet.ui.xwidget.view.SideBar;
import com.example.mayikang.wowallet.util.CharacterParser;
import com.example.mayikang.wowallet.util.PinyinComparator;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.config.Tag;
import com.sctjsj.basemodule.core.http.NormalResponse;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.OnClick;

//选择城市
@Route(path = "/main/act/ChoiceCity")
public class ChoiceCityActivity extends BaseAppcompatActivity {
    private List<RegionInfo> provinceList;
    private List<RegionInfo> citysList;
    private List<String> provinces;
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    private ClearEditText mClearEditText;
    private List<RegionInfo> mReMenCitys;//热门城市列表
    private MyGridViewAdapter gvAdapter;
    private GridView mGridView;

    //汉字转换成拼音的类
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;
    private boolean isparent=false;

    //根据拼音来排列ListView里面的数据类
    private PinyinComparator pinyinComparator;

    private HttpServiceImpl service;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 0:
                    Bundle data = msg.getData();
                    String s= (String) data.get("data");
                    String address=data.getString("address");
                    LogUtil.e("locationhttp",s);
                    //解析数据
                    analysis(s,address);
                    dismissLoading();
                    break;
                case 1:
                    Log.e("choiceCity","EventBus");
                    EventBus.getDefault().post(new AreaEvent(true));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        initData();
        initViews();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_choice_city;
    }

    @Override
    public void reloadData() {

    }
    private void initData() {
        provinceList = RegionDAO.getProvencesOrCity(1);
        provinceList.addAll(RegionDAO.getProvencesOrCity(2));
        citysList = new ArrayList<RegionInfo>();
        mReMenCitys = new ArrayList<RegionInfo>();
        provinces = new ArrayList<String>();
        for (RegionInfo info : provinceList) {
            provinces.add(info.getName().trim());
        }
        mReMenCitys.add(new RegionInfo(2, 1,"北京"));
        mReMenCitys.add(new RegionInfo(25,1, "上海"));
        mReMenCitys.add(new RegionInfo(77,6, "深圳"));
        mReMenCitys.add(new RegionInfo(76, 6,"广州"));
        mReMenCitys.add(new RegionInfo(197, 14,"长沙"));
        mReMenCitys.add(new RegionInfo(343, 1,"天津"));

    }

    private void initViews() {
        View view = View.inflate(this, R.layout.head_city_list, null);
        mGridView = (GridView) view.findViewById(R.id.id_gv_remen);
        gvAdapter = new MyGridViewAdapter(this,mReMenCitys);
        mGridView.setAdapter(gvAdapter);
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                if(s.equals("热门")){
                    sortListView.setSelection(0);
                }
                int position = adapter.getPositionForSection(s.charAt(0));
                if(position != -1){
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        sortListView.addHeaderView(view);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
                String cityName=((SortModel)adapter.getItem(position-1)).getName();
                Toast.makeText(getApplication(), "您选择了"+cityName, Toast.LENGTH_SHORT).show();
                hideSoftInput(mClearEditText.getWindowToken());

                getLocation(mClearEditText.getText().toString().trim(),cityName);
            }
        });

//		SourceDateList = filledData(getResources().getStringArray(R.array.date));
        SourceDateList = filledData(provinceList);

        // 根据a-z进行排序源数据
        Collections.sort(SourceDateList, pinyinComparator);
        adapter = new SortAdapter(this, SourceDateList);
        sortListView.setAdapter(adapter);


        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
        //根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String cityName = mReMenCitys.get(position).getName();
                Toast.makeText(ChoiceCityActivity.this, cityName, Toast.LENGTH_SHORT).show();
                hideSoftInput(mClearEditText.getWindowToken());

                getLocation(mClearEditText.getText().toString().trim(),cityName);

            }
        });
    }

    /**
     * 为ListView填充数据
     * @param date
     * @return
     */
    private List<SortModel> filledData(List<RegionInfo> date){
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for(int i=0; i<date.size(); i++){
            SortModel sortModel = new SortModel();
            sortModel.setName(date.get(i).getName());
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(date.get(i).getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if(sortString.matches("[A-Z]")){
                sortModel.setSortLetters(sortString.toUpperCase());
            }else{
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     * @param filterStr
     */
    private void filterData(String filterStr){
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if(TextUtils.isEmpty(filterStr)){
            filterDateList = SourceDateList;
        }else{
            if(!provinces.contains(filterStr)){
                filterDateList.clear();
                for(SortModel sortModel : SourceDateList){
                    String name = sortModel.getName();
                    if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
                        filterDateList.add(sortModel);
                    }
                }
                isparent=false;
            }else{
                filterDateList.clear();
                for(int i = 0;i<provinceList.size();i++){
                    String name = provinceList.get(i).getName();
                    if(name.equals(filterStr)){
                        filterDateList.addAll(filledData(RegionDAO.getProvencesOrCityOnParent(provinceList.get(i).getId())));
                    }
                }
                isparent=true;
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

    //多种隐藏软件盘方法的其中一种
    protected void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    @OnClick(R.id.linear_back)
    public void onViewClicked() {
        hideSoftInput(mClearEditText.getWindowToken());
        finish();
    }

    //获取位置信息
    private void getLocation(final String parent,final String address){
        showLoading(false,"返回中...");
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                String jsonByInternet="";
                if(isparent){
                    jsonByInternet = getJsonByInternet(parent+address);
                }else {
                    jsonByInternet = getJsonByInternet(address);
                }
                Message message=Message.obtain();
                Bundle bundle=new Bundle();
                bundle.putString("data",jsonByInternet);
                bundle.putString("address",address);
                message.setData(bundle);
                message.what=0;
                handler.sendMessage(message);
            }
        };
        new Thread(runnable).start();
    }

    //获取逆地理编码数据
    public static String getJsonByInternet(String address){
        try {
            URL url = new URL("http://restapi.amap.com/v3/geocode/geo?address="+address+"&output=JSON&key=b14706e1f77cbfed86a8e90a760f955c");
            //打开连接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            if(200 == urlConnection.getResponseCode()){
                //得到输入流
                InputStream is =urlConnection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while(-1 != (len = is.read(buffer))){
                    baos.write(buffer,0,len);
                    baos.flush();
                }
                return baos.toString("utf-8");
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    //解析数据
    private void analysis(String s,String address) {
        if(!StringUtil.isBlank(s)){
            try {
                JSONObject object=new JSONObject(s);
                String info=object.getString("info");
                int status=object.getInt("status");
                if(info.equals("OK")&&status==1){
                    JSONArray array=object.getJSONArray("geocodes");
                    JSONObject o=array.getJSONObject(0);
                    String location=o.getString("location");
                    //区域编码
//                    int adcode=o.getInt("adcode");

                    int indext=location.indexOf(",");
                    String log=(String)location.subSequence(0,indext);
                    String lag = (String)location.subSequence(indext + 1, location.length());
                    LogUtil.e("location","log-"+log+"    lag-"+lag);

                    /**保存位置信息到本地**/
                    SPFUtil.put(Tag.TAG_LONGITUDE,log);
                    SPFUtil.put(Tag.TAG_LATITUDE,lag);
                    SPFUtil.put(Tag.TAG_CITY,address);
                    handler.sendEmptyMessage(1);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                LogUtil.e("locationJSONException",e.toString());
            }
        }

    }
}
