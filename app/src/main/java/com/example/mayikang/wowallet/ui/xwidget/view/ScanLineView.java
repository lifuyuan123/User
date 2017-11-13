package com.example.mayikang.wowallet.ui.xwidget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class ScanLineView extends View {
    Paint paint = new Paint();

    private float density;// 屏幕密度
    private float width, height;// 控件的宽度高度

    public ScanLineView(Context context) {
        super(context);
        init();
    }

	/*public ScanLineView(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}*/

    public ScanLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ScanLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化画笔
     */
    private void init() {
        density = getResources().getDisplayMetrics().density;
        // 去锯齿
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#53d555"));
    }

    /**
     * 重绘测量
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;

        } else {
            width = (int) (250 * density);
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;

        } else {
            height = (int) (250 * density);
        }
        // Log.e("tag", " ------ width:"+width +"   height:"+height);
        setMeasuredDimension((int) this.width, (int) this.height);
        /**
         *第一个Color是扫描线的下半部分颜色，第二个Color是扫描线的上半部分颜色
         */
        Shader mShader = new LinearGradient( width/2.0f,height,width/2.0f, 0, new int[] {
                Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF")}, null,
                Shader.TileMode.CLAMP);
        paint.setShader(mShader);
    }

    /**
     * 绘制
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 为Paint设置渐变器

        // 绘制矩形
        canvas.drawRect(0, 0, width, height, paint);
    }
}
