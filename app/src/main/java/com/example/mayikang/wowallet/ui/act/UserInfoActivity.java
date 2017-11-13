package com.example.mayikang.wowallet.ui.act;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.UserBean;
import com.example.mayikang.wowallet.ui.xwidget.dialog.PopupDialog;
import com.example.mayikang.wowallet.ui.xwidget.dialog.QrDialog;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

@Route(path = "/main/act/user/user_info")
public class UserInfoActivity extends BaseAppcompatActivity implements TakePhoto.TakeResultListener, InvokeListener {

    @BindView(R.id.act_user_ll_to_change_portrait)
    LinearLayout actUserLlToChangePortrait;
    @BindView(R.id.userInfo_back_rl)
    RelativeLayout userInfoBackRl;
    @BindView(R.id.userInfo_userName_ll)
    LinearLayout userInfoUserNameLl;
    @BindView(R.id.userInfo_phoneNumber_ll)
    LinearLayout userInfoPhoneNumberLl;
    @BindView(R.id.userInfo_Email_ll)
    LinearLayout userInfoEmailLl;
    @BindView(R.id.userInfo_qr_ll)
    LinearLayout userInfoQrLl;
    @BindView(R.id.userInfo_sex_ll)
    LinearLayout userInfoSexLl;
    @BindView(R.id.activity_mine_user_info)
    LinearLayout activityMineUserInfo;
    @BindView(R.id.user_info_userImg)
    CircleImageView userInfoUserImg;
    @BindView(R.id.user_info_userName)
    TextView userInfoUserName;
    @BindView(R.id.user_info_phoneNumber)
    TextView userInfoPhoneNumber;
    @BindView(R.id.user_info_emailAddress)
    TextView userInfoEmailAddress;
    @BindView(R.id.user_info_userSex)
    TextView userInfoUserSex;
    @BindView(R.id.user_info_realName)
    TextView userInfoRealName;
    @BindView(R.id.user_info_alipayNumber)
    TextView userInfoAlipayNumber;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.user_info_tv_Address)
    TextView userInfoTvAddress;
    private QrDialog mQrDialog;
    private UserBean bean;

    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private File file;
    private HttpServiceImpl service;
    private Bitmap bitmap;
    private Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String imgId = (String) msg.obj;
                    Log.e("message", imgId);
                    ChangUserIcon(imgId);
                    break;
                case 1:
                    userInfoUserImg.setImageBitmap(bitmap);
                    break;
                case 2:
                    UserBean bean = (UserBean) msg.obj;
                    init(bean);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserData();
    }

    //初始化数据
    private void init(UserBean bean) {
        Log.e("init", bean.toString() + UserAuthUtil.getUserId() + "");
       /* PicassoUtil.getPicassoObject().load(bean.getUrl())
                .resize(DpUtils.dpToPx(this, 80), DpUtils.dpToPx(this, 80))
                .error(R.mipmap.icon_default_portrait).into(userInfoUserImg);*/
        Glide.with(UserInfoActivity.this).load(bean.getUrl())
                .placeholder(R.drawable.ic_defult_load).crossFade()
                .error(R.mipmap.icon_load_faild)
                .into(userInfoUserImg);

        userInfoUserName.setText(bean.getUsername());
        userInfoPhoneNumber.setText(bean.getPhone());
        if (!StringUtil.isBlank(bean.getEmail())) {
            userInfoEmailAddress.setText(bean.getEmail());
        } else {
            userInfoEmailAddress.setText("暂未设置");
        }


        if (!StringUtil.isBlank(bean.getRealName())) {
            userInfoRealName.setText(bean.getRealName());
        } else {
            userInfoRealName.setText("暂未设置");
        }
        if (!StringUtil.isBlank(bean.getAlipayNumber())) {
            userInfoAlipayNumber.setText(bean.getAlipayNumber());
        } else {
            userInfoAlipayNumber.setText("暂未设置");
        }
        if (bean.getSex() == 1) {
            userInfoUserSex.setText("男");
        } else {
            userInfoUserSex.setText("女");
        }
    }

    //初始化其他的View
    private void initView() {
        mQrDialog = new QrDialog(this, R.style.Qr_dialog);
    }


    @Override
    public int initLayout() {
        return R.layout.activity_user_info;
    }

    @Override
    public void reloadData() {

    }


    @Override
    public void takeSuccess(TResult result) {
        bitmap = BitmapFactory.decodeFile(result.getImage().getPath());
        if (null != file) {
            UpUserImg();
        }
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }


    /**
     * 弹出头像修改提示框
     */
    public void createPop() {
        List<String> data = new ArrayList<>();
        data.add("拍照");
        data.add("手机相册");
        final PopupDialog popDialog = new PopupDialog(this, data);
        popDialog.setOnItemClickListener(new PopupDialog.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                switch (position) {
                    //拍照
                    case 0:
                        //裁剪参数
                        CropOptions cropOptions = new CropOptions.Builder().
                                setWithOwnCrop(false).create();
                        getTakePhoto().onPickFromCaptureWithCrop(getUri(), cropOptions);

                        break;
                    //手機相冊
                    case 1:
                        //裁剪参数
                        CropOptions cropOptions1 = new CropOptions.Builder()
                                .setWithOwnCrop(false).create();
                        getTakePhoto().onPickFromGalleryWithCrop(getUri(), cropOptions1);

                        break;
                    //取消
                    case 2:
                        popDialog.dismiss();
                        break;
                }
            }
        });
        popDialog.show();
    }


    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    /**
     * 图片保存路径
     *
     * @return
     */
    private Uri getUri() {
        file = new File(Environment.getExternalStorageDirectory(), "/wowallet/images/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        return imageUri;
    }

    /**
     * 页面跳转回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 权限申请回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //处理运行时权限
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }


    @OnClick({R.id.userInfo_back_rl, R.id.userInfo_userName_ll, R.id.userInfo_phoneNumber_ll, R.id.userInfo_Email_ll, R.id.userInfo_qr_ll,
            R.id.userInfo_sex_ll, R.id.activity_mine_user_info,
            R.id.act_user_ll_to_change_portrait, R.id.user_info_realNameLayout, R.id.user_info_alipayLayout,R.id.userInfo_lin_adress})
    public void onViewClickeds(View view) {
        switch (view.getId()) {
            case R.id.userInfo_back_rl:
                finish();
                break;
            case R.id.act_user_ll_to_change_portrait:
                createPop();
                break;
            case R.id.userInfo_userName_ll://修改用户名
                ARouter.getInstance().build("/main/act/ChangeUserNameActivity").navigation();
                break;
            case R.id.userInfo_phoneNumber_ll:
                ARouter.getInstance().build("/main/act/ChangeUserPhoneActivity").navigation();
                break;
            case R.id.userInfo_Email_ll:
                ARouter.getInstance().build("/main/act/ChangeUserEmailActivity").navigation();
                break;
            case R.id.userInfo_qr_ll:
                showQR();
                break;
            case R.id.userInfo_sex_ll:
                ARouter.getInstance().build("/main/act/ChangeUserSexActivity").navigation();
                break;
            case R.id.user_info_realNameLayout://修改真实姓名
                ARouter.getInstance().build("/main/act/ChangeRealNameActivity").navigation();
                break;
            case R.id.user_info_alipayLayout://修改支付宝号
                ARouter.getInstance().build("/main/act/ChangeUserAlipayActivity").navigation();
                break;
            //收货地址管理
            case R.id.userInfo_lin_adress:
                ARouter.getInstance().build("/main/act/AdressManager").navigation();
                break;
        }
    }


    private void showQR() {
        if (mQrDialog == null) {
            mQrDialog = new QrDialog(UserInfoActivity.this, R.style.Qr_dialog);
        }
        mQrDialog.setUser(UserAuthUtil.getCurrentUser());
        if (!mQrDialog.isShowing()) {
            mQrDialog.show();
        }
    }


    //上传用户选择的照片
    private void UpUserImg() {

        service.uploadFile(null, MainUrl.UpImgUrl, file, null, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("result", result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject obj = new JSONObject(result);
                        if (obj.getBoolean("result")) {
                            //上传成功
                            JSONArray arr = new JSONArray(obj.getString("resultData"));
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject data = arr.getJSONObject(i);
                                String ImgId = data.getInt("acyId") + "";
                                Log.e("ImgId", ImgId);
                                Message msg = new Message();
                                msg.obj = ImgId;
                                msg.what = 0;
                                handle.sendMessage(msg);
                            }
                        } else {
                            Toast.makeText(UserInfoActivity.this, "上传头像失败！", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("result_JSONException", e.toString());

                    }
                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("result_onError", ex.toString());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.e("result_onFinished", "onFinished");
                dismissLoading();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                showLoading(true, "上传中。。。");
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });
    }

    //修改用户头像
    private void ChangUserIcon(String imgId) {
        HashMap<String, String> body = new HashMap<>();
        body.put("id", String.valueOf(UserAuthUtil.getUserId()));
        body.put("photoId", imgId);
        service.doCommonPost(null, MainUrl.amendUserUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("message-s", result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        if (object.getBoolean("result")) {
                            Toast.makeText(UserInfoActivity.this, "修改头像成功！", Toast.LENGTH_SHORT).show();
                            Message mMessage = new Message();
                            mMessage.what = 1;
                            handle.sendMessage(mMessage);
                        } else {
                            Toast.makeText(UserInfoActivity.this, "修改头像失败！", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("message-JSONException", e.toString());
                    }
                }

            }

            @Override
            public void onError(Throwable ex) {
                Log.e("message-onError", ex.toString());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.e("message-onFinished", "onFinished");
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

    //获取用户信息
    private void getUserData() {
        HashMap<String, String> body = new HashMap<>();
        body.put("ctype", "user");
        body.put("id", UserAuthUtil.getUserId() + "");
        body.put("jf", "photo");
        service.doCommonPost(null, MainUrl.GetUserMessage, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("getUserData", result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        JSONObject data = object.getJSONObject("data");
                        UserBean bean = new UserBean();
                        bean.setUsername(data.getString("username"));
                        bean.setSex(data.getInt("sex"));
                        bean.setEmail(data.getString("email"));
                        bean.setPhone(data.getString("phone"));
                        bean.setRealName(data.getString("realName"));
                        bean.setAlipayNumber(data.getString("alipayNumber"));
                        bean.setUrl(data.getJSONObject("photo").getString("url"));
                        Message message = new Message();
                        message.what = 2;
                        message.obj = bean;
                        handle.sendMessage(message);
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

}
