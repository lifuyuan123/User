package com.example.mayikang.wowallet.ui.act;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.intf.MainUrl;
import com.example.mayikang.wowallet.modle.javabean.MessageBean;
import com.sctjsj.basemodule.base.HttpTask.XProgressCallback;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.router_service.impl.HttpServiceImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
@Route(path = "/main/act/MessageInFoActivity")
public class MessageInFoActivity extends BaseAppcompatActivity {

    @BindView(R.id.message_info_backll)
    RelativeLayout messageInfoBackll;
    @BindView(R.id.message_info_titleTxt)
    TextView messageInfoTitleTxt;
    @BindView(R.id.message_info_dateTxt)
    TextView messageInfoDateTxt;
    @BindView(R.id.message_info_bodyTxt)
    TextView messageInfoBodyTxt;
    @Autowired(name = "key")
    int id;
    private HttpServiceImpl service;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                MessageBean bean= (MessageBean) msg.obj;
                if(null!=bean){
                    initView(bean);
                    setMsssageStatus();
                    Log.e("setMsssageonError","------");
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service= (HttpServiceImpl) ARouter.getInstance().build("/basemodule/service/http").navigation();
        ButterKnife.bind(this);
        getMessageData();
    }

    @Override
    public int initLayout() {
        return R.layout.activity_message_in_fo;
    }

    @Override
    public void reloadData() {

    }

    @OnClick(R.id.message_info_backll)
    public void onViewClicked() {
        finish();
    }

    //获取消息详情
    private void  getMessageData(){
        HashMap<String,String> body=new HashMap<>();
        body.put("ctype","message");
        body.put("cond","{id:"+id+"}");
        service.doCommonPost(null, MainUrl.basePageQueryUrl, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                if(!StringUtil.isBlank(result)){
                    try {
                        JSONObject object=new JSONObject(result);
                        JSONObject data=object.getJSONObject("data");
                        MessageBean bean=new MessageBean();
                        bean.setContent(data.getString("content"));
                        bean.setTitle(data.getString("title"));
                        bean.setInsert_time(data.getString("insertTime"));
                        Message message=new Message();
                        message.obj=bean;
                        message.what=0;
                        handler.sendMessage(message);
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


    //给控件设置数据
    private void initView(MessageBean bean){
        messageInfoTitleTxt.setText(bean.getTitle());
        messageInfoBodyTxt.setText(bean.getContent());
        messageInfoDateTxt.setText(bean.getInsert_time());
    }

    //修改数据的status状态
    private void setMsssageStatus(){
        HashMap<String,String> body=new HashMap<>();
        body.put("ctype","message");
        body.put("data","{id:"+id+",status:2}");
        service.doCommonPost(null, MainUrl.SetMessageStatus, body, new XProgressCallback() {
            @Override
            public void onSuccess(String result) {
                Log.e("setMsssageStatu",result.toString());
                if(!StringUtil.isBlank(result)){
                    //查看数据成功
                    Log.e("setMsssageStatus",result.toString());
                }
            }

            @Override
            public void onError(Throwable ex) {
                Log.e("setMsssageonError",ex.toString());
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
