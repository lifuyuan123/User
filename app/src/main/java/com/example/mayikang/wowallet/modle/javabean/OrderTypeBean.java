package com.example.mayikang.wowallet.modle.javabean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lifuy on 2017/9/28.
 */

public class OrderTypeBean implements Serializable {

    private int type;//类型
    private int id;
    private String orderStoreName;  //店铺名
    private int orderStatus;//订单状态
    private int goodsAllConut;//商品总数量
    private double goodsTotal;//商品总价
    private List<GoodsBean> goodsBeanList;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderStoreName() {
        return orderStoreName;
    }

    public void setOrderStoreName(String orderStoreName) {
        this.orderStoreName = orderStoreName;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getGoodsAllConut() {
        return goodsAllConut;
    }

    public void setGoodsAllConut(int goodsAllConut) {
        this.goodsAllConut = goodsAllConut;
    }

    public double getGoodsTotal() {
        return goodsTotal;
    }

    public void setGoodsTotal(double goodsTotal) {
        this.goodsTotal = goodsTotal;
    }

    public List<GoodsBean> getGoodsBeanList() {
        return goodsBeanList;
    }

    public void setGoodsBeanList(List<GoodsBean> goodsBeanList) {
        this.goodsBeanList = goodsBeanList;
    }

    @Override
    public String toString() {
        return "OrderTypeBean{" +
                "type=" + type +
                ", id=" + id +
                ", orderStoreName='" + orderStoreName + '\'' +
                ", orderStatus=" + orderStatus +
                ", goodsAllConut=" + goodsAllConut +
                ", goodsTotal=" + goodsTotal +
                ", goodsBeanList=" + goodsBeanList +
                '}';
    }
}
