package com.example.mayikang.wowallet.modle.javabean;

import java.io.Serializable;

/**
 * Created by haohaoliu on 2017/7/10.
 * explain:套餐得javabean
 */

public class AgentBean implements Serializable{
    private int id;
    private String name;
    private double value;
    private int totNum;//剩余
    private int surplusNum;//总的代理店铺数
    private String insertTime;
    private boolean isCheck;
    private String storeState;
    private double allValue;
    private int agetState;
    private double totAmount;


    public AgentBean(String insertTime, int id, String name, double value, int totNum) {
        this.insertTime = insertTime;
        this.id = id;
        this.name = name;
        this.value = value;
        this.totNum = totNum;
    }


    public AgentBean() {
    }

    public double getAllValue() {
        return allValue;
    }

    public void setAllValue(double allValue) {
        this.allValue = allValue;
    }

    public int getAgetState() {
        return agetState;
    }

    public void setAgetState(int agetState) {
        this.agetState = agetState;
    }

    public double getTotAmount() {
        return totAmount;
    }

    public void setTotAmount(double totAmont) {
        this.totAmount = totAmont;
    }

    public String getStoreState() {
        return storeState;
    }

    public void setStoreState(String storeState) {
        this.storeState = storeState;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInsertTime() {
        return insertTime;
    }

    public int getSurplusNum() {
        return surplusNum;
    }

    public void setSurplusNum(int surplusNum) {
        this.surplusNum = surplusNum;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
    }

    public int getTotNum() {
        return totNum;
    }

    public void setTotNum(int totNum) {
        this.totNum = totNum;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AgentBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", value=" + value +
                ", totNum=" + totNum +
                ", surplusNum=" + surplusNum +
                ", insertTime='" + insertTime + '\'' +
                ", isCheck=" + isCheck +
                ", storeState='" + storeState + '\'' +
                ", allValue=" + allValue +
                '}';
    }
}

