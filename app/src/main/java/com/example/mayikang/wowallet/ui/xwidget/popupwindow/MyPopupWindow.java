package com.example.mayikang.wowallet.ui.xwidget.popupwindow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mayikang.wowallet.R;

/**
 * Created by XiaoHaoWit on 2017/9/26.
 */

public class MyPopupWindow extends PopupWindow {
    private  Context mContext;
    private LayoutInflater inflater;
    private View root;

    //里面的控件
    private RelativeLayout my_popuWindow_cancel;
    private RelativeLayout my_popuWindow_trouble;
    private TextView my_popuWindow_storeMsg;
    private LinearLayout my_popuWindow_weixinLayout;
    private ImageView my_popuWindow_weixin_check;
    private LinearLayout my_popuWindow_alipay_layout;
    private ImageView my_popuWindow_alipay_check;



    public MyPopupWindow(Context context) {
        super(context);
        this.mContext=context;
        this.inflater= (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        init();
        setContentView(root);
    }
    //初始化
    private void init() {
        root=inflater.inflate(R.layout.layout_my_popuwindow,null);
        my_popuWindow_cancel= (RelativeLayout) root.findViewById(R.id.my_popuWindow_cancel);
        my_popuWindow_trouble= (RelativeLayout) root.findViewById(R.id.my_popuWindow_trouble);
        my_popuWindow_storeMsg= (TextView) root.findViewById(R.id.my_popuWindow_storeMsg);

        my_popuWindow_weixinLayout= (LinearLayout) root.findViewById(R.id.my_popuWindow_weixinLayout);
        my_popuWindow_weixin_check= (ImageView) root.findViewById(R.id.my_popuWindow_weixin_check);

        my_popuWindow_alipay_layout= (LinearLayout) root.findViewById(R.id.my_popuWindow_alipay_layout);
        my_popuWindow_alipay_check= (ImageView) root.findViewById(R.id.my_popuWindow_alipay_check);

    }
}
