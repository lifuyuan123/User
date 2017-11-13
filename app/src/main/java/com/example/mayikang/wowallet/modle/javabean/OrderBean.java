package com.example.mayikang.wowallet.modle.javabean;

import java.io.Serializable;

/**
 * Created by haohaoliu on 2017/6/29.
 * explain:订单的javabean
 */

public class OrderBean implements Serializable{
    private int id;
    private String buyerRemark;
    private int state;
    private double totalprice;
    private StoreBean bean;
    private String name;
    private String paytime;

    public OrderBean() {
    }

    public OrderBean(int id, String buyerRemark, int state, StoreBean bean) {
        this.id = id;
        this.buyerRemark = buyerRemark;
        this.state = state;
        this.bean = bean;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaytime() {
        return paytime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }

    public double getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(double totalprice) {
        this.totalprice = totalprice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBuyerRemark() {
        return buyerRemark;
    }

    public void setBuyerRemark(String buyerRemark) {
        this.buyerRemark = buyerRemark;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public StoreBean getBean() {
        return bean;
    }

    public void setBean(StoreBean bean) {
        this.bean = bean;
    }

    @Override
    public String toString() {
        return "OrderBean{" +
                "id=" + id +
                ", buyerRemark='" + buyerRemark + '\'' +
                ", state=" + state +
                ", bean=" + bean +
                '}';
    }
}
