package com.example.mayikang.wowallet.ui.xwidget.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.example.mayikang.wowallet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuha on 2017/5/9.
 */

public class CustomPopupWindou extends PopupWindow {
    private Context mContext;
    private LayoutInflater inflater;
    private int mWindousWidth,mWindousHigth;
    private View mView;
    private ListView mListView;
    //坐标的位置（x、y）
    private final int[] mLocation = new int[2];
    //实例化一个矩形
    private Rect mRect = new Rect();

    private List<String> data=new ArrayList<>();
    private PopupWindouAdapter adapter;

    public CustomPopupWindou(Context context,int width, int height) {
        super(context);
        this.mContext=context;

        inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //设置可以获取焦点
        setFocusable(true);
        //设置弹窗内可点击
        setTouchable(true);
        //设置弹窗外可点击
        setOutsideTouchable(true);
        getWindouLW();
        //设置弹窗的宽高
        setWidth(width);
        setHeight(height);
        mView=inflater.inflate(R.layout.custom_popupwindou_layout,null);
        setContentView(mView);
        initView();

    }
    //初始化View
    private void initView() {
        mListView= (ListView) mView.findViewById(R.id.custom_popupwindou_list);
        adapter=new PopupWindouAdapter();
        mListView.setAdapter(adapter);
    }

    //获取屏幕的宽高度
    private void getWindouLW() {
        mWindousHigth=mContext.getResources().getDisplayMetrics().heightPixels;
        mWindousWidth=mContext.getResources().getDisplayMetrics().widthPixels;
    }

    public  void show(View v){
        //获取点击屏幕坐标
        v.getLocationOnScreen(mLocation);
        //设置矩形的大小
        mRect.set(mLocation[0], mLocation[1], mLocation[0] + v.getWidth(),mLocation[1] + v.getHeight()-10);
        //显示弹窗的位置
        //showAtLocation(v, Gravity.RIGHT, mWindousWidth - 15- (getWidth()/2), mRect.bottom);
        showAsDropDown(v,-300,-10);


        Activity activity= (Activity) mContext;
       WindowManager.LayoutParams pr= activity.getWindow().getAttributes();
        pr.alpha=0.3f;
        activity.getWindow().setAttributes(pr);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        Activity activity= (Activity) mContext;
        WindowManager.LayoutParams pr= activity.getWindow().getAttributes();
        pr.alpha=1.0f;
        activity.getWindow().setAttributes(pr);

    }

    private class PopupWindouAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
           ViewHolder holder;
            if(null==convertView){
                holder=new ViewHolder();
                convertView=inflater.inflate(R.layout.popuwindou_list_item,null);
                holder.mTextView= (TextView) convertView.findViewById(R.id.popuwindou_item_txt);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }
            holder.mTextView.setText(data.get(position));
            return convertView;
        }

        class ViewHolder{
            TextView mTextView;
        }
    }

    //添加数据
    public void addData(List<String> datas){
        if(null!=datas&&datas.size()>0){
            data.addAll(datas);
            adapter.notifyDataSetChanged();
        }

    }
    //清除数据
    public void cleanData(){
        data.clear();
    }

}
