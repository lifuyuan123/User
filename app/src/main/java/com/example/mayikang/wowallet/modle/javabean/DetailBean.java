package com.example.mayikang.wowallet.modle.javabean;

import android.content.SyncRequest;

import java.io.Serializable;

/**
 * Created by lifuyuan on 2017/5/10.
 */

public class DetailBean implements Serializable {
    private int afterAmount;//收入支出之后的
    private int amount;//那一次操作的金额
    private int id;//单id
    private String info;
    private String insertTime;
    private int type;//1 转账收入  2为返利收入 3余额消费支出 4转账支出

    public DetailBean(int afterAmount, int amount, int id, String info, String insertTime, int type) {
        this.afterAmount = afterAmount;
        this.amount = amount;
        this.id = id;
        this.info = info;
        this.insertTime = insertTime;
        this.type = type;
    }

    public DetailBean() {
    }

    public int getAfterAmount() {
        return afterAmount;
    }

    public void setAfterAmount(int afterAmount) {
        this.afterAmount = afterAmount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "DetailBean{" +
                "afterAmount=" + afterAmount +
                ", amount=" + amount +
                ", id=" + id +
                ", info='" + info + '\'' +
                ", insertTime='" + insertTime + '\'' +
                ", type=" + type +
                '}';
    }
}
