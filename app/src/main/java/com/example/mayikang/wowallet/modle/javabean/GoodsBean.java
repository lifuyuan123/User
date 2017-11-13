package com.example.mayikang.wowallet.modle.javabean;

import java.io.Serializable;

/**
 * Created by lifuy on 2017/9/29.
 */

public class GoodsBean implements Serializable {

    private String goodsName;//商品名
    private String goodsUrl;//商品图片
    private String goodsColor;//商品颜色
    private double goodsPrice;//商品价格
    private int goodsCount;//商品数量

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsUrl() {
        return goodsUrl;
    }

    public void setGoodsUrl(String goodsUrl) {
        this.goodsUrl = goodsUrl;
    }

    public String getGoodsColor() {
        return goodsColor;
    }

    public void setGoodsColor(String goodsColor) {
        this.goodsColor = goodsColor;
    }

    public double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public int getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(int goodsCount) {
        this.goodsCount = goodsCount;
    }

    @Override
    public String toString() {
        return "GoodsBean{" +
                "goodsName='" + goodsName + '\'' +
                ", goodsUrl='" + goodsUrl + '\'' +
                ", goodsColor='" + goodsColor + '\'' +
                ", goodsPrice=" + goodsPrice +
                ", goodsCount=" + goodsCount +
                '}';
    }
}
