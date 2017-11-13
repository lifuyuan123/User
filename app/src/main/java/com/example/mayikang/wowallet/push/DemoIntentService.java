package com.example.mayikang.wowallet.push;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.ui.act.MessageListActivity;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.sctjsj.basemodule.core.event.PushBean;
import com.google.gson.Gson;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.event.PushEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;


public class DemoIntentService extends GTIntentService {

    public DemoIntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {

    }

    @Override
    public void onReceiveMessageData(final Context context, GTTransmitMessage msg) {
        if (msg == null) {
            return;
        }

        if (!PushManager.getInstance().getClientid(this).equals(msg.getClientId())) {
            return;
        }

        byte[] b = msg.getPayload();

        if (b.length <= 0) {
            return;
        }

        final String data = new String(msg.getPayload());
        LogUtil.e("接收到了推送");



        if (!StringUtil.isBlank(data)) {
            final PushBean pb=new Gson().fromJson(data,PushBean.class);
            /**
             * 系统级推送
             */
            if(1==pb.getType()){
                //强制下线
                 if(1==pb.getState()){
                     new UserAuthUtil().doLogout(context, new UserAuthUtil.LogInOutListener() {
                         @Override
                         public void onSuccess() {
                             Log.e("onSuccess","-----------------");
                             PushEvent event=new PushEvent();
                             event.setPb(pb);

                             Intent intent=new Intent("com.wowallet.push.outline");
                             Bundle bundle=new Bundle();
                             bundle.putSerializable("event", (Serializable) event);
                             intent.putExtra("bundle",bundle);
                             context.sendBroadcast(intent);
                         }

                         @Override
                         public void onFailed() {
                             Log.e("onFailed","-----------------");
                             PushEvent event=new PushEvent();
                             event.setPb(pb);
                             EventBus.getDefault().post(event);
                         }

                         @Override
                         public void onError() {
                             Log.e("onError","-----------------");
                             PushEvent event=new PushEvent();
                             event.setPb(pb);
                             EventBus.getDefault().post(event);
                         }
                     });
                 }

                 //强制更新
                 if(2==pb.getState()){

                 }
            }

            /**
             * 业务推送
             */
            if(2==pb.getType()){

                //添加好友请求
                if(3==pb.getState()){
                    String content=pb.getContent();

                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        mBuilder.
                                setAutoCancel(true).
                                setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.icon_app_start_logo)).
                                setContentTitle("好友请求").
                                setContentText(content).
                                setSmallIcon(R.mipmap.icon_small_message_white).
                                setDefaults(Notification.DEFAULT_ALL);

                        //点击的意图ACTION是跳转到Intent
                        Intent resultIntent = new Intent(this,MessageListActivity.class);
                        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        mBuilder.setContentIntent(pendingIntent);
                        mNotificationManager.notify(1, mBuilder.build());
                    }
                }


            //频台推送到用户
            if(4==pb.getState()){
                String content=pb.getContent();

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                mBuilder.
                        setAutoCancel(true).
                        setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.icon_app_start_logo)).
                        setContentTitle("平台消息").
                        setContentText(content).
                        setSmallIcon(R.mipmap.icon_small_message_white).
                        setDefaults(Notification.DEFAULT_ALL);

                //点击的意图ACTION是跳转到Intent
                Intent resultIntent = new Intent(this,MessageListActivity.class);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(pendingIntent);
                mNotificationManager.notify(1, mBuilder.build());
            }
        }


    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
    }
}