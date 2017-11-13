package com.example.mayikang.wowallet.ui.act;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.fastjson.serializer.IntegerCodec;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.StoreBean;
import com.example.mayikang.wowallet.ui.xwidget.dialog.QRScanResponseDialog;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;

import io.github.xudaojie.qrcodelib.CaptureActivity;

@Route(path = "/main/act/qr_scan")
public class QRScanActivity extends CaptureActivity {
    private HttpServiceImpl http;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        http = (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
        restartPreview();
    }

    /**
     * 处理扫码结果
     *
     * @param resultString
     */
    @Override
    protected void handleResult(String resultString) {
        Log.e("result", resultString);
        if (resultString.isEmpty()) {
            return;
        }
        //扫描店铺二维码
        if(-1!=resultString.indexOf("storeId")){
            ScanStoreQrCode(resultString);
        }else if(-1!=resultString.indexOf("userId")){
            //扫描用户二维码
            ScanUserQrCode(resultString);
        }else {
            showAlert(resultString+"为非法字符串，暂时无法识别，请重新扫描二维码！");
        }






    }
    //扫描用户二维码
    private void ScanUserQrCode(String resultString) {
        //有邀请码 userId=223&iid=125874
        //没有邀请码  userId=223
        Log.e("扫描用户二维码","-----");
        //邀请码
        String iid="";
        //用户id
        String userId="";
        int mid=resultString.indexOf("&iid=");
        if(mid!=-1){
            iid=resultString.substring(mid+5,resultString.length());
            userId=resultString.substring(7,mid);
        }else {
            userId=resultString.substring(7,resultString.length());
        }
        Log.e("扫描用户二维码",userId);
       if(UserAuthUtil.isUserLogin()){
           ARouter.getInstance().build("/main/act/user/AddFriendMsg").
                   withString("id", userId).
                   navigation();

       }else {
           Toast.makeText(this, "请登录后操作！", Toast.LENGTH_SHORT).show();
           ARouter.getInstance().build("/main/act/login").
                   withString("inviteId", iid).
                   navigation();
       }

    }


    //扫店家二维码
    private void ScanStoreQrCode(String resultString){
        //http://woqianbao/share/storeId_123.htm?iid=123555
        //截取字符串，获取店铺 id 和邀请码
        int begin = resultString.indexOf("storeId_");
        int mid = resultString.indexOf(".htm?");
        int end = resultString.indexOf("iid=");
        String str1=null;
        String str2=null;
        try{
            //storeId
            str1 = resultString.substring(begin + 8, mid);
            //邀请码
            str2 = resultString.substring(end + 4, resultString.length());
            Log.e("字符串",str1+"-------"+str2);

        }catch (Exception e){
            showAlert(resultString+"为非法字符串，暂时无法识别，请重新扫描二维码！");
            return;
        }finally {
            //获取的 id 必须是数字类型
            if( StringUtil.isNumeric(str1) && StringUtil.isNumeric(str2) ){

                /*******************正常的业务处理 start**********************/
                //扫码之后未登录，跳转登录页，携带店铺 id 和邀请码
                if (!UserAuthUtil.isUserLogin()) {
                    Toast.makeText(this, "请登录后操作", Toast.LENGTH_SHORT).show();
                    ARouter.getInstance().build("/main/act/login").
                            withString("storeId", str1).
                            withString("inviteId", str2).
                            navigation();
                    return;
                }

                if (StringUtil.isNumeric(str1)) {
                    queryStoreInfo(str1);
                    return;
                }

                /*******************正常的业务处理 end**********************/

            }else {
                showAlert(resultString+"为非法字符串，暂时无法识别，请重新扫描二维码！");
                return;
            }

        }


    }


    private void queryStoreInfo(String id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("ctype", "store");
        map.put("id", id);
        map.put("jf", "storeLogo");

        http.doCommonPost(null, MainUrl.baseSingleQueryUrl, map, new XProgressCallback() {
            @Override
            public void onSuccess(String resultStr) {
                Log.e("店铺信息",resultStr.toString());
                if (!StringUtil.isBlank(resultStr)) {
                    try {
                        JSONObject obj = new JSONObject(resultStr);
                        JSONObject data = obj.getJSONObject("data");
                        final int storeId = data.getInt("id");
//                        String logo = data.getString("storeLogo");
//                        String storeLogo = null;
//                        if (!StringUtil.isBlank(logo)) {
//                            storeLogo = data.getJSONObject("storeLogo").getString("url");
//                        }
//                        String name = data.getString("name");
//                        StoreBean sb = new StoreBean();
//                        sb.setId(storeId);
//                        sb.setName(name);
//                        sb.setLogo(storeLogo);
//
//                        //查询完店铺信息之后显示弹出框，提示操作
//                        final QRScanResponseDialog dia = new QRScanResponseDialog(QRScanActivity.this);
//                        dia.setStoreBean(sb);
//                        dia.setOnClick(new QRScanResponseDialog.onClickIntf() {
//                            @Override
//                            public void clickCancel() {
//                                dia.dismiss();
//                                restartPreview();
//                            }
//
//                            @Override
//                            public void clickIntoStore() {
//                                ARouter.getInstance().build("/main/act/store").withInt("id", storeId).navigation();
//                            }
//
//                            @Override
//                            public void clickGotoPay() {
//                                ARouter.getInstance().build("/main/act/confirm_order").withInt("id", storeId).navigation();
//                            }
//                        });
//                        dia.show();
                        ARouter.getInstance().build("/user/main/act/confirm_order").withInt("id", storeId).withInt("Flag",1).navigation();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(QRScanActivity.this, "解析异常", Toast.LENGTH_SHORT).show();
                        restartPreview();
                    }
                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("店铺错误",ex.toString());
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


    private void showAlert(String str){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        AlertDialog dialog=builder.create();
        builder.setMessage(str);
        builder.setPositiveButton("重试", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

}
