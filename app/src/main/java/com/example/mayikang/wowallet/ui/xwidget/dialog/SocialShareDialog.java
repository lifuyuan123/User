package com.example.mayikang.wowallet.ui.xwidget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.HashMap;


public class SocialShareDialog extends Dialog implements View.OnClickListener,UMShareListener{

    private Activity mActivity;
    private Context context;
    private HashMap<String,Object> data;
    UMWeb web;
    /**
     * Activity中创建
     * @param mActivity
     */
    public SocialShareDialog(Activity mActivity,HashMap<String,Object> data) {

        super(mActivity, R.style.social_share_dialog_style);
        this.mActivity = mActivity;
        this.context=mActivity;
        this.data=data;
        setCancelable(false);
    }

    public SocialShareDialog(Context context,HashMap<String,Object> data) {

        super(context, R.style.social_share_dialog_style);
        this.context= context;
        this.mActivity = (Activity) context;
        this.data=data;
        setCancelable(false);
    }
    /**
     * Fragment中创建
     * @param mFragment
     */
    public SocialShareDialog(Fragment mFragment) {
        super(mFragment.getActivity(), R.style.social_share_dialog_style);
        this.mActivity = mFragment.getActivity();
        this.context= mFragment.getActivity();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_social_share_dialog);
        initLocation();
        initView();
    }

    private void initLocation(){
        Window Window = this.getWindow();
        //显示位置
        Window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams params = Window.getAttributes();
        params.y = 20;
        Window.setAttributes(params);
    }

    private void initView(){

        findViewById(R.id.ll_wx).setOnClickListener(this);
        findViewById(R.id.ll_wx_timeline).setOnClickListener(this);
        findViewById(R.id.ll_qq).setOnClickListener(this);
        findViewById(R.id.ll_qq_zone).setOnClickListener(this);
        findViewById(R.id.ll_sina_wei_bo).setOnClickListener(this);
        findViewById(R.id.ll_sms).setOnClickListener(this);

        findViewById(R.id.rl_cancel).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_wx:
                Share(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.ll_wx_timeline:
                Share(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.ll_qq:
                Share(SHARE_MEDIA.QQ);
                break;
            case R.id.ll_qq_zone:
                Share(SHARE_MEDIA.QZONE);
                break;
            case R.id.ll_sina_wei_bo:
                Share(SHARE_MEDIA.SINA);
                break;
            case R.id.ll_sms:
                Share(SHARE_MEDIA.SMS);
                break;
            //取消
            case R.id.rl_cancel:
                dismiss();
                break;
        }


    }

    @Override
    public void dismiss() {
        super.dismiss();
        UMShareAPI.get(mActivity).release();
    }

    //分享
    private void Share(SHARE_MEDIA datas) {
      if(data.size()>0){
          int type= (int) data.get("type");
          switch (type){
              case 1://店铺分享
                  UMWeb store=new UMWeb("http://www.woqianbao8.com/shareStore.htm?storeId="+data.get("StoreId"));
                  store.setTitle(data.get("StoreName").toString());
                  UMImage thrub=new UMImage(mActivity,R.drawable.ic_share);
                  store.setDescription(data.get("StoreMsg").toString());
                  store.setThumb(thrub);
                  new ShareAction(mActivity).setPlatform(datas).withMedia(store).setCallback(this).share();
                  break;
              case 2:
                  UMWeb appMsg;
                  if(null!=UserAuthUtil.getCurrentUser()&&!StringUtil.isBlank(UserAuthUtil.getCurrentUser().getInvitationCode())){
                     appMsg=new UMWeb("http://www.woqianbao8.com/share_register.htm?iid="+UserAuthUtil.getCurrentUser().getInvitationCode());
                  }else {
                      appMsg=new UMWeb("http://www.woqianbao8.com/share_register.htm?iid=");
                  }
                  appMsg.setTitle("惠生活");
                  UMImage appInfo=new UMImage(mActivity,R.drawable.ic_share);
                  appMsg.setDescription("惠生活已经给您准备了一份大礼！点击下载App");
                  appMsg.setThumb(appInfo);
                  new ShareAction(mActivity).setPlatform(datas).withMedia(appMsg).setCallback(this).share();
                  break;
          }
      }
    }


    /***
     * 分享的回掉
     * @param share_media
     */
    @Override
    public void onStart(SHARE_MEDIA share_media) {
        //分享开始的回调
        Log.e("UMshare","onStart");
    }

    @Override
    public void onResult(SHARE_MEDIA share_media) {
        Log.e("UMshare","onResult"+share_media);
    }

    @Override
    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
        Log.e("UMshare","onError"+share_media+"---"+throwable.toString());
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media) {
        Log.e("UMshare","onCancel"+share_media);
    }
}
