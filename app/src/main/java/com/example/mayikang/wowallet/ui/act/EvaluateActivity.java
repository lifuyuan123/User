package com.example.mayikang.wowallet.ui.act;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.arouter.utils.TextUtils;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.EvaluateAdapter;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.PhotoBean;
import com.github.ornolfr.ratingview.RatingView;
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
import com.sctjsj.basemodule.base.ui.widget.dialog.sweet.PopListDialog;
import com.sctjsj.basemodule.base.ui.widget.rv.WrapGridLayoutManager;
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

/***
 * 评价页面
 */
@Route(path = "/user/main/act/EvaluateActivity")
public class EvaluateActivity extends BaseAppcompatActivity implements TakePhoto.TakeResultListener, InvokeListener, EvaluateAdapter.EvaluateAdapterCallBack {

    @BindView(R.id.evaluate_back_rl)
    RelativeLayout evaluateBackRl;
    @BindView(R.id.evaluate_sure_txt)
    TextView evaluateSureTxt;
    @BindView(R.id.evaluate_edt)
    EditText evaluateEdt;
    @BindView(R.id.evaluate_recycleView)
    RecyclerView evaluateRecycleView;
    @BindView(R.id.evaluate_Img)
    ImageView evaluateImg;
    @BindView(R.id.evaluate_ll)
    LinearLayout evaluateLl;
    @BindView(R.id.activity_evaluate)
    LinearLayout activityEvaluate;
    @BindView(R.id.reatingView)
    RatingView reatingView;
    @BindView(R.id.evaluate_state)
    TextView evaluateState;
    @BindView(R.id.evaluate_store_name)
    TextView evaluate_store_name;
    private boolean flag = false;
    private EvaluateAdapter adapter;
    @Autowired(name = "key")
    int id = 1;
    @Autowired(name = "storeId")
    String storeId="";

    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private String filePath = null;
    private HttpServiceImpl service;
    private List<PhotoBean> data = new ArrayList<>();
    private int isAnonymous = -1;
    private List<PhotoBean> datacopy = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        initView();
        setListener();
    }

    private void setListener() {
        reatingView.setOnRatingChangedListener(new RatingView.OnRatingChangedListener() {
            @Override
            public void onRatingChange(float oldRating, float newRating) {
                switch ((int) newRating) {
                    case 1:
                        evaluateState.setText("非常差");
                        break;
                    case 2:
                        evaluateState.setText("较差");
                        break;
                    case 3:
                        evaluateState.setText("一般");
                        break;
                    case 4:
                        evaluateState.setText("较好");
                        break;
                    case 5:
                        evaluateState.setText("非常好");
                        break;
                }
            }
        });
    }

    //初始化数据
    private void initView() {
        PhotoBean bean = new PhotoBean();
        bean.setType("local");
        data.add(bean);
        adapter = new EvaluateAdapter(data, this);
        adapter.setListener(this);
        evaluateRecycleView.setLayoutManager(new WrapGridLayoutManager(this,2));
        evaluateRecycleView.setAdapter(adapter);
       if(storeId!=null){
           evaluate_store_name.setText(storeId);
       }
    }

    @Override
    public int initLayout() {
        return R.layout.activity_evaluate;
    }

    @Override
    public void reloadData() {

    }

    @OnClick({R.id.evaluate_back_rl, R.id.evaluate_ll,R.id.evaluate_sure_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.evaluate_back_rl:
                finish();
                break;
            case R.id.evaluate_ll:
                if (flag) {
                    flag = false;
                    isAnonymous = 1;
                    evaluateImg.setImageResource(R.drawable.ic_nocheck);
                } else {
                    isAnonymous = 2;
                    flag = true;
                    evaluateImg.setImageResource(R.drawable.ic_check);
                }
                break;
            case R.id.evaluate_sure_txt:
               if(!StringUtil.isBlank(evaluateEdt.getText().toString())){
                    addStoreComment();
               }
                break;
        }
    }


    @Override
    public void addImg() {

        final PopListDialog dia = new PopListDialog(EvaluateActivity.this);
        dia.setPopListCallback(new PopListDialog.PopListCallback() {
            @Override
            public void callCamera() {
                dia.dismiss();
                //裁剪参数
                CropOptions cropOptions = new CropOptions.Builder().
                        setWithOwnCrop(false).create();
                getTakePhoto().onPickFromCaptureWithCrop(getUri(), cropOptions);
            }

            @Override
            public void callGallery() {
                dia.dismiss();
                //裁剪参数
                CropOptions cropOptions1 = new CropOptions.Builder()
                        .setWithOwnCrop(false).create();

                getTakePhoto().
                        onPickFromGalleryWithCrop(getUri(), cropOptions1);
            }
        });
        if (dia != null && !dia.isShowing()) {
            dia.show();
        }

    }

    @Override
    public void delImg(int position) {
        data.remove(position);
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void takeSuccess(TResult result) {
        Log.e("takeSuccess", result.toString());
        if (result != null) {
            //图片存储路径
            String path = result.getImage().getPath();
            filePath = path;
            UpFile(new File(path));
        }
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }


    /**
     * 申请权限
     *
     * @param invokeParam
     * @return
     */
    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //创建TakePhoto实例
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 图片保存路径
     *
     * @return
     */
    public Uri getUri() {
        File file = new File(Environment.getExternalStorageDirectory(), "/wowallet/images/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        return imageUri;
    }


    //上传图片
    private void UpFile(File file) {
        service.uploadFile(null, MainUrl.uploadFileUrl, file, null, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("UpFile", result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        if (object.getBoolean("result")) {
                            String str = object.getString("resultData");
                            if (!StringUtil.isBlank(str)) {
                                JSONArray array = new JSONArray(str);
                                if (array != null && array.length() > 0) {
                                    //上传后的 acyid
                                    int acyId = array.getJSONObject(0).getInt("acyId");
                                    PhotoBean photo = new PhotoBean("photo");
                                    photo.setUrl(filePath);
                                    photo.setId(acyId);
                                    data.add(data.size() - 1, photo);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        } else {
                            Toast.makeText(EvaluateActivity.this, "上传图片失败！", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("onError",ex.toString());

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
                showLoading(true, "上传照片中...");
            }

            @Override
            public void onLoading(long total, long current) {

            }
        });
    }


    //上传用户评价
    public void addStoreComment() {
        HashMap<String, String> body = new HashMap<>();
        body.put("content", evaluateEdt.getText().toString().trim());
        body.put("isAnonymous", isAnonymous + "");
        int a= (int) reatingView.getRating();
        body.put("score",  a+"");
        if (data.size() > 1) {
            StringBuffer buffer = new StringBuffer();
            cleanData();
            for (int i = 0; i < datacopy.size(); i++) {
                if (i == datacopy.size() - 1) {
                    buffer.append(datacopy.get(i).getId());
                } else {
                    buffer.append(datacopy.get(i).getId() + ",");
                }
            }
            body.put("photoIds", buffer.toString());
        }
        body.put("ofId", id + "");

        service.doCommonPost(null, MainUrl.addStoreComment, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("addStoreComment", result.toString());
                if (!StringUtil.isBlank(result)) {
                    try {
                        JSONObject jsobj = new JSONObject(result);
                        if (jsobj.getBoolean("result")) {
                            Toast.makeText(EvaluateActivity.this, jsobj.getString("msg"), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(EvaluateActivity.this, jsobj.getString("msg"), Toast.LENGTH_SHORT).show();
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

    //清除数据
    private void cleanData() {
        datacopy.clear();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getType().equals("photo")) {
                datacopy.add(data.get(i));
            }
        }
    }

}
