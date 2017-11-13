package com.example.mayikang.wowallet.modle.javabean;

import java.io.Serializable;

/**
 * Created by liuha on 2017/6/13.
 */

public class TransferBean implements Serializable {
    private int id;
    private int amount;//转账金额
    private String desc;
    private UserBean expenditureUser;//支出者
    private UserBean incomeUser;//收入者
    private int fType;//流水类型(1-收入，2-支出)
    private String insertTime;//转账时间
    private String remark;//备注

    public TransferBean(int id, int amount, String desc, UserBean expenditureUser, UserBean incomeUser, int fType, String insertTime, String remark) {
        this.id = id;
        this.amount = amount;
        this.desc = desc;
        this.expenditureUser = expenditureUser;
        this.incomeUser = incomeUser;
        this.fType = fType;
        this.insertTime = insertTime;
        this.remark = remark;
    }

    public TransferBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
    }

    public int getfType() {
        return fType;
    }

    public void setfType(int fType) {
        this.fType = fType;
    }

    public UserBean getIncomeUser() {
        return incomeUser;
    }

    public void setIncomeUser(UserBean incomeUser) {
        this.incomeUser = incomeUser;
    }

    public UserBean getExpenditureUser() {
        return expenditureUser;
    }

    public void setExpenditureUser(UserBean expenditureUser) {
        this.expenditureUser = expenditureUser;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "TransferBean{" +
                "id=" + id +
                ", amount=" + amount +
                ", desc=" + desc +
                ", expenditureUser=" + expenditureUser +
                ", incomeUser=" + incomeUser +
                ", fType=" + fType +
                ", insertTime='" + insertTime + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
