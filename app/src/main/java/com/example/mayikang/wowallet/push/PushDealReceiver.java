package com.example.mayikang.wowallet.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mayikang.wowallet.ui.act.IndexActivity;
import com.example.mayikang.wowallet.ui.act.TransparentDialogActivity;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.sctjsj.basemodule.base.ui.act.BaseAppcompatActivity;
import com.sctjsj.basemodule.base.ui.widget.dialog.CommonDialog;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.MainLooper;
import com.sctjsj.basemodule.base.util.setup.SetupUtil;
import com.sctjsj.basemodule.core.event.PushEvent;

/**
 * Created by mayikang on 17/6/14.
 */

public class PushDealReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        LogUtil.e("接收到强制下线广播");



        if(intent!=null){

            String action=intent.getAction();

            if(!action.equals("com.wowallet.push.outline")){
                return;
            }

            Bundle b=intent.getBundleExtra("bundle");

            //2.判断 Index 是否存在于任务栈中
            if (!SetupUtil.isXInTaskStack(context, IndexActivity.class)) {
                LogUtil.e("强制下线", "index 不在任务栈中，不需要提示");
                return;
            }
            if(b!=null){
                Intent i = new Intent(context.getApplicationContext(), TransparentDialogActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtras(b);
                context.getApplicationContext().startActivity(i);
            }
        }

    }
}
