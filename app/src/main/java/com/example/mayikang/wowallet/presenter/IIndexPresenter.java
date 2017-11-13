package com.example.mayikang.wowallet.presenter;

/**
 * Created by mayikang on 17/5/8.
 */

public interface IIndexPresenter {
    //切换 Fragment
    public void replaceFragment(String frgName);
    //刷新首页的数据
    public void pullData();

}
