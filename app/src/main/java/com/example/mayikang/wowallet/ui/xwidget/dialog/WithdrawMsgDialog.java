package com.example.mayikang.wowallet.ui.xwidget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mayikang.wowallet.R;

/**
 * Created by haohaoliu on 2017/7/19.
 * explain:提现成功详情信息得dialog
 */

public class WithdrawMsgDialog extends Dialog {
    private ImageView withdraw_msg_Icon;
    private TextView withdraw_msg_title;
    private TextView withdraw_msg_content;
    private Button withdraw_msg_btn;
    private WithdrawMsgOnclick callback;
    private LayoutInflater inflater;
    private Context mContext;

    public WithdrawMsgDialog(Context context) {
        super(context);
        this.mContext=context;
        this.inflater= (LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initView();
    }

    public WithdrawMsgDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext=context;
        this.inflater= (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initView();
    }

    //初始化
    private void initView(){
        View view=inflater.inflate(R.layout.dialog_withdraw_msg_layout,null);
        setContentView(view);
        findView(view);
        setListener();
    }





    //找控件
    private void findView(View view) {
        withdraw_msg_Icon= (ImageView) view.findViewById(R.id.withdraw_msg_Icon);
        withdraw_msg_title= (TextView) view.findViewById(R.id.withdraw_msg_title);
        withdraw_msg_content= (TextView) view.findViewById(R.id.withdraw_msg_content);
        withdraw_msg_btn= (Button) view.findViewById(R.id.withdraw_msg_btn);
    }
    //设置监听
    private void setListener() {
        withdraw_msg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=callback){
                    callback.onClick();
                }
            }
        });
    }

    //回调按钮的监听
   public interface WithdrawMsgOnclick{
        public void onClick();
    }

    public void onWithdrawMsgOnclick(WithdrawMsgOnclick callback){
        this.callback=callback;
    }

    //设置图片
    public void setImageResId(int id){
       if(null!=withdraw_msg_Icon){
           withdraw_msg_Icon.setImageResource(id);
       }
    }

    public void setTitle(String title){
        Log.e("with",title);
        withdraw_msg_title.setText(title+"");
    }

    public void setCountent(String countent){
        withdraw_msg_content.setText(countent+"");
    }

}
