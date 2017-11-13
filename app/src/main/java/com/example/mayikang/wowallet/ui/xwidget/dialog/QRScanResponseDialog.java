package com.example.mayikang.wowallet.ui.xwidget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.modle.javabean.StoreBean;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;

import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class QRScanResponseDialog extends Dialog implements View.OnClickListener {

    private Activity mActivity;
    private Context context;
    private  onClickIntf listener;
    private StoreBean sb;
    private CircleImageView civ;
    private TextView tvName;
    /**
     * Activity中创建
     * @param mActivity
     */
    public QRScanResponseDialog(Activity mActivity) {

        super(mActivity, R.style.social_share_dialog_style);
        this.mActivity = mActivity;
        setCancelable(false);
    }

    public QRScanResponseDialog(Context context) {

        super(context, R.style.social_share_dialog_style);
        this.context= context;
        setCancelable(false);
    }
    /**
     * Fragment中创建
     * @param mFragment
     */
    public QRScanResponseDialog(Fragment mFragment) {
        super(mFragment.getActivity(), R.style.social_share_dialog_style);
        this.mActivity = mFragment.getActivity();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_qr_scan_response_dialog);
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
        civ= (CircleImageView) findViewById(R.id.civ);
        tvName= (TextView) findViewById(R.id.tv_store_name);

        if(sb!=null){
            PicassoUtil.getPicassoObject().load(sb.getLogo()).
                    resize(DpUtils.dpToPx(mActivity,80),DpUtils.dpToPx(mActivity,80)).
                    error(R.mipmap.icon_load_faild).into(civ);
            tvName.setText(sb.getName());
        }

        findViewById(R.id.rl_cancel).setOnClickListener(this);
        findViewById(R.id.rl_into_store).setOnClickListener(this);
        findViewById(R.id.rl_goto_pay).setOnClickListener(this);



    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rl_cancel:
                if(listener!=null){
                    listener.clickCancel();
                }
                break;

            case R.id.rl_into_store:
                if(listener!=null){
                    listener.clickIntoStore();
                }

                break;

            case R.id.rl_goto_pay:
                if(listener!=null){
                    listener.clickGotoPay();
                }
                break;

        }


    }


    public interface onClickIntf{
        void clickCancel();
        void clickIntoStore();
        void clickGotoPay();
    }

    public  void setOnClick(onClickIntf click){
        listener=click;
    }

    public void setStoreBean(StoreBean sb){
        this.sb=sb;
    }

}
