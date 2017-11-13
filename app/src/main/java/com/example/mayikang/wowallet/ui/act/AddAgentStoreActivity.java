package com.example.mayikang.wowallet.ui.act;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.ui.xwidget.dialog.PopupDialog;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/main/act/AddAgentStoreActivity")
public class AddAgentStoreActivity extends BaseAppcompatActivity implements TakePhoto.TakeResultListener, InvokeListener {

    @BindView(R.id.storemanage_iv_back)
    ImageView storemanageIvBack;
    @BindView(R.id.storemanage_linear_back)
    LinearLayout storemanageLinearBack;
    @BindView(R.id.storemanage_edt_store_name)
    EditText storemanageEdtStoreName;
    @BindView(R.id.storemanage_edt_brand_introduction)
    EditText storemanageEdtBrandIntroduction;
    @BindView(R.id.storemanage_iv_logo)
    ImageView storemanageIvLogo;
    @BindView(R.id.tv_to_choose)
    TextView tvToChoose;
    @BindView(R.id.storemanage_edt_adress)
    EditText storemanageEdtAdress;
    @BindView(R.id.storemanage_edt_phone)
    EditText storemanageEdtPhone;
    @BindView(R.id.storemanage_tv_save)
    TextView storemanageTvSave;

    String regStr = "1[3|5|7|8]\\d{9}";
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private HttpServiceImpl service;
    private Bitmap bitmap;
    private  File file;
    private String ImgId;
    private boolean Flag=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        setListener();

    }
    //设置监听
    private void setListener() {
        //设置输入电话得监听
        storemanageEdtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               if( JudgePhoneIsOrNot(s.toString())){
                   Flag=true;
               }else {
                   Flag=false;
                   storemanageEdtPhone.setError("输入的手机号码不合法！");
               }

            }
        });
    }



    //判断输入得电话号码是否正确
    private boolean JudgePhoneIsOrNot(String phone) {
        if(!StringUtil.isBlank(phone)&&validateStrFormat(regStr,phone)){
           return true;
        }else {
            return false;
        }
    }


    /**
     * 根据你的输入用正则表达式进行校验
     */
    private static boolean validateStrFormat(String regEx,String yourStr) {
        //1.构造一个模式
        Pattern pattern = Pattern.compile(regEx);
        //2.创建匹配器
        Matcher matcher = pattern.matcher(yourStr);
        //3.进行校验
        return matcher.matches();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_add_agent_store;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.storemanage_linear_back, R.id.tv_to_choose, R.id.storemanage_tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.storemanage_linear_back:
                finish();
                break;
            case R.id.tv_to_choose:
                createPop();
                break;
            case R.id.storemanage_tv_save:
                SubmitStoreMsg();
                break;
        }
    }


    //提交店铺信息
    private void SubmitStoreMsg() {
        if(Flag&&!StringUtil.isBlank(storemanageEdtStoreName.getText().toString())
                &&!StringUtil.isBlank(storemanageEdtBrandIntroduction.getText().toString())
                &&!StringUtil.isBlank(storemanageEdtAdress.getText().toString())){
            submitStoreMsgInServer();

        }else {
            Toast.makeText(this, "请输入完整得店铺信息！", Toast.LENGTH_SHORT).show();
        }
    }


    //获取新增店铺信息
    private void submitStoreMsgInServer() {
        HashMap<String,String> body=new HashMap<>();
        body.put("name",storemanageEdtStoreName.getText().toString());
        body.put("storeLogoId",ImgId);
        body.put("detail",storemanageEdtBrandIntroduction.getText().toString());
        body.put("storeAddress",storemanageEdtAdress.getText().toString());
        body.put("telephone",storemanageEdtPhone.getText().toString());
        service.doCommonPost(null, MainUrl.AddNewStore, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if(!StringUtil.isBlank(result)){
                    try {
                        Log.e("submitStoreMsgIn",result.toString());
                        JSONObject object=new JSONObject(result);
                        if(object.getBoolean("result")){
                            finish();
                        }else {
                            Toast.makeText(AddAgentStoreActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }



            @Override
            public void onError(Throwable ex) {
                Log.e("submitStoreMsgIn",ex.toString());

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



    @Override
    public void takeSuccess(TResult result) {
        bitmap = BitmapFactory.decodeFile(result.getImage().getPath());
        if(null!=file){
            StoreLogoImg();
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



    //上传
    private void StoreLogoImg(){
        service.uploadFile(null, MainUrl.UpImgUrl, file, null, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("result",result.toString());
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject obj=new JSONObject(result);
                        if(obj.getBoolean("result")){
                            //上传成功
                            JSONArray arr=new JSONArray(obj.getString("resultData"));
                            for (int i = 0; i <arr.length() ; i++) {
                                JSONObject data=arr.getJSONObject(i);
                                ImgId=data.getInt("acyId")+"";
                               if(null!=bitmap){
                                   storemanageIvLogo.setImageBitmap(bitmap);
                               }
                            }
                        }else {
                            Toast.makeText(AddAgentStoreActivity.this,"上传店家Logo失败！",Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("result_JSONException",e.toString());
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("result_onError",ex.toString());
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
                showLoading(true,"上传中。。。");
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });
    }
}
