package com.example.mayikang.wowallet.modle.javabean;

import com.hymane.expandtextview.ExpandTextView;

import java.io.Serializable;

/**
 * Created by liuha on 2017/5/12.
 */

public class BillBean implements Serializable {
    private int id;
    private int fType;//交易类型 1-收入 2-支出
    private String desc;//交易描述

    private double amount;//操作金额
    private String insertTime;//交易时间

    private IncomeUserBean income;
    private ExpendUserBean expend;

    public IncomeUserBean getIncome() {
        return income;
    }

    public void setIncome(IncomeUserBean income) {
        this.income = income;
    }

    public ExpendUserBean getExpend() {
        return expend;
    }

    public void setExpend(ExpendUserBean expend) {
        this.expend = expend;
    }

    public BillBean() {
    }


    public String getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getfType() {
        return fType;
    }

    public void setfType(int fType) {
        this.fType = fType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


}
