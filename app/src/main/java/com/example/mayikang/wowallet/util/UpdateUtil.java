package com.example.mayikang.wowallet.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;


import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mayikang.wowallet.application.MyApp;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.sctjsj.basemodule.base.HttpTask.XDownloadCallback;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.widget.dialog.CommonDialog;
import com.sctjsj.basemodule.base.ui.widget.dialog.sweet.SweetAlertDialog;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;
import java.io.File;
import java.util.HashMap;


/**
 * Created by Chris-Jason on 2016/10/19.
 */
public class UpdateUtil {
    private String TAG="UpdateUtil";
    private static MyApp app;
    private static UpdateUtil instance;
    private static Activity context;
    //    下载文件保存路径
    private String downloadFilePath= Environment.getExternalStorageDirectory().getPath()+"/ebangbuyer/download";
    private String downLoadUrl;
    private AlertDialog.Builder builder,progressBuilder;
    private AlertDialog dialog,progressDialog;

    private AlertDialog.Builder downBuilder;
    private static AlertDialog downDialog;
    private ProgressDialog numberProgressBar;
    private HttpServiceImpl service= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();



    public static UpdateUtil getInstance(Activity c){
        if(instance==null){
            instance=new UpdateUtil();
        }
        context=c;
        app= (MyApp) context.getApplicationContext();
        return instance;
    }


    /**
     * 检查服务端最新 app版本
     */
    //主动检查版本
    public  void checkVersion(){
        HashMap<String,String> body=new HashMap<>();
        body.put("appTerminal","1");
        service.doCommonPost(null, MainUrl.UpApp, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG,result.toString());
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject object=new JSONObject(result);
                        if(object.getBoolean("result")){
                            if(!StringUtil.isBlank(object.getString("resultData"))){
                                JSONObject resultData=new JSONObject(object.getString("resultData"));
                                int isUpdate=resultData.getInt("isUpdate");//是否通过审核 1-是  2-否
                                int  appVersionId=resultData.getInt("appVersionId");//内部版本号
                                String contents=resultData.getString("contents");//更新内容
                                String downLoadUrl=resultData.getString("downUrl");//下載app的url
                                String versionId=resultData.getString("versionId");
                                int  isForce=resultData.getInt("isForce");//是否强制更新1-是 2-否
                                Log.e("-------------",getLocalVersion()+"");
                                if(isUpdate==1&&appVersionId>getLocalVersion()){
                                    switch (isForce){
                                        case 1:
                                            alertImportant(versionId,contents,downLoadUrl);
                                            break;
                                        case 2:
                                            alert(versionId,contents,downLoadUrl);
                                            break;
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("upAppjson",e.toString());
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

    //手动检查版本
    public  void checkVersionManual(){

        HashMap<String,String> body=new HashMap<>();
        body.put("appTerminal","1");
        service.doCommonPost(null, MainUrl.UpApp, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG,result.toString());
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject object=new JSONObject(result);
                        if(object.getBoolean("result")){
                            if(!StringUtil.isBlank(object.getString("resultData"))){
                                JSONObject resultData=new JSONObject(object.getString("resultData"));
                                int isUpdate=resultData.getInt("isUpdate");//是否通过审核 1-是  2-否
                                int  appVersionId=resultData.getInt("appVersionId");//内部版本号
                                String contents=resultData.getString("contents");//更新内容
                                String downLoadUrl=resultData.getString("downUrl");//下載app的url
                                String versionId=resultData.getString("versionId");
                                int  isForce=resultData.getInt("isForce");//是否强制更新1-是 2-否
                                if(isUpdate==1&&appVersionId>getLocalVersion()){
                                    switch (isForce){
                                        case 1:
                                            alertImportant(versionId,contents,downLoadUrl);
                                            break;
                                        case 2:
                                            alert(versionId,contents,downLoadUrl);
                                            break;
                                    }
                                }else {
                                    //主动检查更新时
                                    AlertDialog.Builder bb=new AlertDialog.Builder(context);
                                    bb.setTitle("检查更新");
                                    bb.setMessage("当前已是最新版本，无需更新");
                                    bb.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    bb.show();
                                }
                            }
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
     * 获取当前安装的版本号
     * @return
     */
    private int getLocalVersion(){
        int version=-1;
        try {
            version= context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }


    /**
     * 提示安装apk
     * @param context
     * @param path
     */
    private void installApp(Context context, String path){
        File file=new File(path);
        Intent intent=new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 下载app
     * @param url
     */
    private void downloadApk(String url){
        checkSavePath();
        final String  filePath=downloadFilePath+"/x.apk";
        service.downloadFile(null, url, null, filePath, new XDownloadCallback() {
            @Override
            public void onDownloadComplete(final File result) {
                Log.e("downloadApk","onDownloadComplete");
                downDialog.dismiss();
                    if(null==result){
                        final CommonDialog dialog=new CommonDialog(context);
                        dialog.setTitle("下载失败");
                        dialog.setContent("程序下载过程中遭遇未知异常！");
                        dialog.setOneButton();
                        dialog.setCancelClickListener("关闭", new CommonDialog.CancelClickListener() {
                            @Override
                            public void clickCancel() {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                final CommonDialog dialog=new CommonDialog(context);
                dialog.setTitle("下载完成");
                dialog.setContent("立即安装最新版本");
                dialog.setOneButton();
                dialog.setCancelClickListener("安装", new CommonDialog.CancelClickListener() {
                    @Override
                    public void clickCancel() {
                        installApp(context,result.getAbsolutePath());
                    }
                });
                dialog.show();
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("downloadApk",ex.toString());

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                numberProgressBar.dismiss();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                if(null==numberProgressBar){
                    numberProgressBar=new ProgressDialog(context);
                    numberProgressBar.setTitle("下载中");
                    numberProgressBar.setCancelable(false);
                    numberProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    numberProgressBar.setMax(100);
                }

            }

            @Override
            public void onDownloading(long total, long current) {
                double a=Double.valueOf(total);
                double b=Double.valueOf(current);
                double c=b/a;
                java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#");
                final int p=Integer.valueOf(df.format(c*100));
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        numberProgressBar.setProgress(p);
                    }
                });

            }
        });
    }

    /**
     * 检查是否存在保存的文件夹
     */
    private void checkSavePath(){
        //创建下载文件夹
        File file=new File(downloadFilePath);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    private void alert(String version,String con, final String url){
        Log.e(TAG," dialog.show()");
        final CommonDialog dialog=new CommonDialog(context);
        dialog.setTitle("更新提示");
        dialog.setContent("最新版本："+version+"\n"+"更新内容："+con);
        dialog.setConfirmClickListener("立即更新", new CommonDialog.ConfirmClickListener() {
            @Override
            public void clickConfirm() {
                dialog.dismiss();
                downloadApk(url);
            }
        });
        dialog.setCancelClickListener("下次再说", new CommonDialog.CancelClickListener() {
            @Override
            public void clickCancel() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 重大更新
     * @param con
     * @param url
     */
    private void alertImportant(String version,String con, final String url){


        final CommonDialog dialog=new CommonDialog(context);
        dialog.setTitle("重大更新提示");
        dialog.setContent("最新版本："+version+"\n"+"更新内容："+con);
        dialog.setOneButton();
        dialog.setCancelClickListener("立即更新", new CommonDialog.CancelClickListener() {
            @Override
            public void clickCancel() {
                dialog.dismiss();
                downloadApk(url);
            }
        });
        dialog.show();
    }


}
