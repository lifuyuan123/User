package com.example.mayikang.wowallet.ui.act;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.constant.requestcode.PermissionRequestCode;
import com.example.mayikang.wowallet.event.AreaEvent;
import com.example.mayikang.wowallet.event.PageSwitchEvent;
import com.example.mayikang.wowallet.presenter.IIndexPresenter;
import com.example.mayikang.wowallet.presenter.impl.IndexPresenterImpl;
import com.example.mayikang.wowallet.push.PushDealReceiver;
import com.igexin.sdk.PushManager;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.ui.widget.dialog.CommonDialog;

import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.SPFUtil;
import com.sctjsj.basemodule.base.util.permission.EasyPermissionsEx;
import com.sctjsj.basemodule.core.config.Tag;
import com.umeng.socialize.UMShareAPI;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;

import static com.example.mayikang.wowallet.constant.requestcode.PermissionRequestCode.HOME_REQUEST_CAMERA_PERMISSION;
import static com.example.mayikang.wowallet.constant.requestcode.PermissionRequestCode.INDEX_REQUEST_LOCATION_PERMISSION;

/**
 * 首页
 */
@Route(path = "/main/act/index")
public class IndexActivity extends BaseAppcompatActivity implements EasyPermissionsEx.PermissionCallbacks {

    /**
     * RadioButton
     **/
    @BindView(R.id.act_index_rb_home)
    RadioButton mRBHome;
    @BindView(R.id.act_index_rb_store)
    RadioButton mRBStore;
    @BindView(R.id.act_index_rb_friends)
    RadioButton mRBFriends;
    @BindView(R.id.act_index_rb_own)
    RadioButton mRBOwn;

    /**
     * presenter
     **/

    private IIndexPresenter indexPresenter;

    /**退出相关**/
    private boolean isExited = false;//标志是否已经退出
    private Handler mHandler= new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    isExited = false;
                    break;
            }
        }
    };

    /**
     * 定位相关
     **/
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = null;
    public AMapLocationClientOption mLocationClientOption = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EventBus.getDefault().register(this);
        /**初始化 indexPresenter**/
        if (indexPresenter == null) {
            indexPresenter = new IndexPresenterImpl(this, getSupportFragmentManager());
        }




        /**初始化布局**/
        initView();

        if(EasyPermissionsEx.hasPermissions(this,Manifest.permission.ACCESS_FINE_LOCATION)){
            initLocation();
        }else {
            EasyPermissionsEx.requestPermissions(this,"定位功能需要授予定位权限",INDEX_REQUEST_LOCATION_PERMISSION,Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if(!EasyPermissionsEx.hasPermissions(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            EasyPermissionsEx.requestPermissions(this,"需要开启读写权限",PermissionRequestCode.READ_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public int initLayout() {
        return R.layout.activity_index;
    }

    @Override
    public void reloadData() {

    }


    /**
     * 为 RadioButton 设置 selector
     *
     * @param mRadioButton
     * @param id
     */
    private void setDrawable(RadioButton mRadioButton, int id) {
        Drawable mDrawable = getResources().getDrawable(id);
        //设置图标大小
        mDrawable.setBounds(0, 0, 50, 50);
        mRadioButton.setCompoundDrawables(null, mDrawable, null, null);
    }

    private void initView() {
        setDrawable(mRBHome, R.drawable.x_act_index_nav_home_selector);
        setDrawable(mRBStore, R.drawable.x_act_index_nav_store_selector);
        setDrawable(mRBFriends, R.drawable.x_act_index_nav_friends_selector);
        setDrawable(mRBOwn, R.drawable.x_act_index_nav_own_selector);
    }



    //UM
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 点击 RB 切换 Frg
     **/
    @OnCheckedChanged({R.id.act_index_rb_home, R.id.act_index_rb_store,
            R.id.act_index_rb_friends, R.id.act_index_rb_own})
    public void indexNavRBCheckedChanged(RadioButton radioButton, boolean checked) {
        switch (radioButton.getId()) {
            //首页
            case R.id.act_index_rb_home:
                if (checked) {
                    indexPresenter.replaceFragment("HOME");
                }
                break;
            //店铺
            case R.id.act_index_rb_store:
                if (checked) {
                    indexPresenter.replaceFragment("STORE");
                }
                break;
            //朋友
            case R.id.act_index_rb_friends:
                if (checked) {
                    indexPresenter.replaceFragment("FRIENDS");
                }
                break;
            //我的
            case R.id.act_index_rb_own:
                if (checked) {
                    indexPresenter.replaceFragment("OWN");
                }
                break;
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        switch (requestCode) {

            case HOME_REQUEST_CAMERA_PERMISSION:
                ARouter.getInstance().build("/main/act/qr_scan").navigation();
                break;

            case INDEX_REQUEST_LOCATION_PERMISSION:
                initLocation();
                break;
        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        switch (requestCode) {
            case HOME_REQUEST_CAMERA_PERMISSION:
                Toast.makeText(this, "您已经禁止了相机权限,相关功能可能无法使用", Toast.LENGTH_SHORT).show();

                final CommonDialog dia = new CommonDialog(this);
                dia.setTitle("系统提醒");
                dia.setContent("你已经禁止了相机权限？立即开启");
                dia.setCancelClickListener("暂不开启", new CommonDialog.CancelClickListener() {
                    @Override
                    public void clickCancel() {
                        dia.dismiss();
                    }
                });
                dia.setConfirmClickListener("确认", new CommonDialog.ConfirmClickListener() {
                    @Override
                    public void clickConfirm() {
                        dia.dismiss();
                        startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", getPackageName(), null)));

                    }
                });
                dia.show();

                break;


            case INDEX_REQUEST_LOCATION_PERMISSION:
                Toast.makeText(this, "您已经禁止了定位权限,相关功能可能无法使用", Toast.LENGTH_SHORT).show();

                final CommonDialog dia1 = new CommonDialog(this);
                dia1.setTitle("系统提醒");
                dia1.setContent("你已经禁止了定位权限？立即开启");
                dia1.setCancelClickListener("暂不开启", new CommonDialog.CancelClickListener() {
                    @Override
                    public void clickCancel() {
                        dia1.dismiss();
                    }
                });
                dia1.setConfirmClickListener("确认", new CommonDialog.ConfirmClickListener() {
                    @Override
                    public void clickConfirm() {
                        dia1.dismiss();
                        startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", getPackageName(), null)));

                    }
                });
                dia1.show();


                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        EasyPermissionsEx.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 高德定位
     */
    public void initLocation() {

        mLocationClient = new AMapLocationClient(this);
        //初始化定位监听器
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    //获取定位错误码
                    int errorCode = aMapLocation.getErrorCode();
                    //定位成功
                    if (0 == errorCode) {

                        /**解析地址**/
                        String city = aMapLocation.getCity();
                        double longT=aMapLocation.getLongitude();
                        double lanT=aMapLocation.getLatitude();

                        /**保存位置信息到本地**/
                        SPFUtil.put(Tag.TAG_LONGITUDE,String.valueOf(longT));
                        SPFUtil.put(Tag.TAG_LATITUDE,String.valueOf(lanT));
                        SPFUtil.put(Tag.TAG_CITY,String.valueOf(city));

                    }
                }
            }
        };


        //定位客户端设置定位监听器
        mLocationClient.setLocationListener(mLocationListener);
        /**
         * 配置定位参数
         */

        mLocationClientOption = new AMapLocationClientOption();

        //设置定位模式：高精度定位模式
        mLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息
        mLocationClientOption.setNeedAddress(true);
        //设置是否指定位一次
        mLocationClientOption.setOnceLocation(false);
        //如果设置了只定位一次，将获取3s内经度最高的一次定位结果
        if (mLocationClientOption.isOnceLocation()) {
            mLocationClientOption.setOnceLocationLatest(true);
        }
        //设置是否强制刷新wifi
        mLocationClientOption.setWifiActiveScan(true);
        //设置是否允许模拟位置
        mLocationClientOption.setMockEnable(false);
        //设置定位时间间隔5min
        mLocationClientOption.setInterval(1000 * 60 * 5);
        mLocationClient.setLocationOption(mLocationClientOption);
        mLocationClient.startLocation();

    }

    /**
     * 重写返回键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //物理返回键值
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //连续点击两次返回键退出程序
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 退出程序
     */
    private void exit() {
        if (!isExited) {
            isExited = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            Message msg = new Message();
            msg.what = 0;
            mHandler.sendMessageDelayed(msg, 2000);
        } else {
            finish();

            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    @Subscribe
    public void onMainEvent(PageSwitchEvent event){

        if(event!=null){
            if("STORE".equals(event.getTarget())){
                if(indexPresenter!=null){
                    mRBHome.setChecked(false);
                    mRBFriends.setChecked(false);
                    mRBOwn.setChecked(false);
                    mRBStore.setChecked(true);
                    indexPresenter.replaceFragment("STORE");
                }
            }
        }
    }

    @Subscribe
    public void onMainEvent(AreaEvent event){

        Log.e("onMainEvent","收到消息");
        indexPresenter.pullData();
    }


}
