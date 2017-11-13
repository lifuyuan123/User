package com.example.mayikang.wowallet.ui.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ListView;

import com.example.mayikang.wowallet.R;
import com.example.mayikang.wowallet.adapter.MyShopsAdapter;
import com.example.mayikang.wowallet.modle.javabean.Data;
import com.example.mayikang.wowallet.modle.javabean.StoreBean;
import com.sctjsj.basemodule.base.util.ListViewUtil;

import java.util.ArrayList;
import java.util.List;

import q.rorbin.qrefreshlayout.QRefreshLayout;
import q.rorbin.qrefreshlayout.RefreshHandler;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by lifuyuan on 2017/5/9.
 */

public class ComprehensiveFragment extends Fragment {
    private ListView listView;
    private MyShopsAdapter adapter;
    private QRefreshLayout qRefreshLayout;
    private List<StoreBean> list=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView= (ListView) view.findViewById(R.id.fragment_data_listview);
        qRefreshLayout= (QRefreshLayout) view.findViewById(R.id.friend_qrefresh);
        initdata();
        listView.setAdapter(adapter);
        ListViewUtil.setListViewHeightBasedOnChildren(listView);
        qRefreshLayout.setLoadMoreEnable(true);
        qRefreshLayout.setRefreshHandler(new RefreshHandler() {
            @Override
            public void onRefresh(QRefreshLayout refresh) {
                qRefreshLayout.refreshComplete();
            }

            @Override
            public void onLoadMore(QRefreshLayout refresh) {
              qRefreshLayout.LoadMoreComplete();
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState){
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:    //当停止滚动时
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:    //滚动时
                        //隐藏软键盘
                        ((InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:   //手指抬起，但是屏幕还在滚动状态

                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void initdata() {

        adapter=new MyShopsAdapter(list,getActivity());
    }
}
