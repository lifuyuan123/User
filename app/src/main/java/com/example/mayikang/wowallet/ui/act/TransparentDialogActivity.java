package com.example.mayikang.wowallet.ui.act;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.sctjsj.basemodule.base.ui.widget.dialog.CommonDialog;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.setup.SetupUtil;
import com.sctjsj.basemodule.core.event.PushEvent;

/**
 * 透明弹框 activity  theme=dialog
 */
public class TransparentDialogActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //去除这个Activity的标题栏
        setContentView(R.layout.activity_transparent_dialog);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if (b != null) {
            PushEvent event = (PushEvent) b.getSerializable("event");

            if (event != null) {

                String content = event.getPb().getContent();
                final CommonDialog dialog = new CommonDialog(this);
                dialog.setTitle("下线通知");
                String str = null;
                if (content.contains("ANDROID") || content.contains("android")) {
                    str = "ANDROID";
                }

                if (content.contains("IOS") || content.contains("ios")) {
                    str = "IOS";
                }
                dialog.setContent("您的沃钱包帐号在另一台" + str + "设备上登录。如非本人操作，密码可能已泄露。");
                dialog.setCancelable(false);
                dialog.setCancelClickListener("退出", new CommonDialog.CancelClickListener() {
                    @Override
                    public void clickCancel() {
                        dialog.dismiss();
                        finish();

                    }
                });

                dialog.setConfirmClickListener("重新登录", new CommonDialog.ConfirmClickListener() {
                    @Override
                    public void clickConfirm() {
                        dialog.dismiss();
                        finish();
                        ARouter.getInstance().build("/main/act/login").navigation();
                    }
                });

                dialog.show();
            }


        }

    }
}
