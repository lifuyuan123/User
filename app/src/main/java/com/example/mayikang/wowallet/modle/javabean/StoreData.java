package com.example.mayikang.wowallet.modle.javabean;

import java.util.List;

/**
 * Created by mayikang on 17/5/9.
 */

public class StoreData {
    //视图类型
    private int viewType;

    //banner 数据列表
    private List<BannerBean> bannerBeanList;

    public StoreData(int viewType) {
        this.viewType = viewType;
    }


    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
