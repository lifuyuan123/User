package com.example.mayikang.wowallet.ui.xwidget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.renderscript.Double2;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.event.AfterInputPayPWDEvent;
import com.example.mayikang.wowallet.modle.javabean.UserBean;
import com.example.mayikang.wowallet.ui.act.TransferActivity;
import com.example.mayikang.wowallet.ui.xwidget.view.SecurityCodeView;
import com.example.mayikang.wowallet.util.QRCodeUtil;
import com.example.mayikang.wowallet.util.UserAuthUtil;
import com.sctjsj.basemodule.base.util.DpUtils;
import com.sctjsj.basemodule.base.util.LogUtil;
import com.sctjsj.basemodule.base.util.StringUtil;
import com.sctjsj.basemodule.core.img_load.PicassoUtil;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;



public class BalancePayDialog extends Dialog implements SecurityCodeView.InputCompleteListener{

    private Context context;
    private double balance;
    private String amount;
    private PayListener listener;

    //支付金额
    private TextView tvAmount;
    //余额
    private TextView tvBalance;

    //密码输入框
    SecurityCodeView securityCodeView;


    public BalancePayDialog(Context context) {
        super(context,R.style.transfer_dialog);
        this.context=context;
    }

    public BalancePayDialog(Context context, int themeResId) {
        super(context, R.style.transfer_dialog);
        this.context=context;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    //设置余额
    public void setBalance(double balance) {
        this.balance = balance;
    }

    //设置支付金额
    public void setAmount(String amount) {
        this.amount = amount;
    }

    //初始化视图
    private void initView() {
        setContentView(R.layout.dialog_balance_pay_layout);

        securityCodeView= (SecurityCodeView) findViewById(R.id.security_code_view);
        securityCodeView.setInputCompleteListener(this);
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tvAmount= (TextView) findViewById(R.id.tv_amount);
        tvBalance= (TextView) findViewById(R.id.tv_balance);

        tvAmount.setText("￥"+amount);
        tvBalance.setText("余额（剩￥"+new DecimalFormat("######0.00").format(balance)+"）");


        //点击之后 判断是否有支付密码
        findViewById(R.id.tv_to_set_pay_pwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build("/main/act/user/setting").navigation();
            }
        });

    }


    @Override
    public void inputComplete() {

        if(balance<=0d || balance< Double.valueOf(amount)){
            Toast.makeText(context, "余额不足", Toast.LENGTH_LONG).show();
            dismiss();
            return;
        }

        //本地校验后密码输入完成
        if(listener!=null){
            listener.onPWDInput(securityCodeView.getEditContent());
        }
        dismiss();
    }

    @Override
    public void deleteContent(boolean isDelete) {

    }



    public void setOnPayListener(PayListener listener){
        this.listener=listener;
    }

    public interface PayListener{
        void onPWDInput(String pwd);
    }

}
