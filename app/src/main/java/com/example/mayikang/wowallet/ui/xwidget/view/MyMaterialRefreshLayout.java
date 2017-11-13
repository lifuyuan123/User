package com.example.mayikang.wowallet.ui.xwidget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.cjj.MaterialRefreshLayout;

/**
 * Created by liuha on 2017/6/13.
 */

public class MyMaterialRefreshLayout extends MaterialRefreshLayout {
    public MyMaterialRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyMaterialRefreshLayout(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
