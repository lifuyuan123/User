package com.example.mayikang.wowallet.modle.javabean;

import java.io.Serializable;

/**
 * Created by liuha on 2017/5/16.
 */

public class IndentBean implements Serializable {
    private int id;
    private String finishTime; //完成时间
    private String insertTime;//生产时间
    private int disValue;//优惠金额
    private String name;//订单号
    private int num;//商品量
    private double payValue;//支付金额
    private String paytime;//支付时间
    private int state;  //订单状态
    private int  storeId;//店铺id
    private String storeName;//店铺名字
    private String storeLogoUrl;//店铺logo
    private int type;//订单类型
    private int rebateValue;//优惠金额
    private int isEva;//是否品论
    private int rebateFlag;//是否返利
    private String buyerRemark;//购买备注
    private int flag;//1返利  2下级提成
    private double postAmount;

    public int getIsEva() {
        return isEva;
    }

    public void setIsEva(int isEva) {
        this.isEva = isEva;
    }

    public int getRebateFlag() {
        return rebateFlag;
    }

    public void setRebateFlag(int rebateFlag) {
        this.rebateFlag = rebateFlag;
    }

    public int getRebateValue() {
        return rebateValue;
    }

    public void setRebateValue(int rebateValue) {
        this.rebateValue = rebateValue;
    }

    public IndentBean(int id, String finishTime, String insertTime, int disValue, String name, int num, int payValue, String paytime, int state, int storeId, String storeName, String storeLogoUrl) {
        this.id = id;
        this.finishTime = finishTime;
        this.insertTime = insertTime;
        this.disValue = disValue;
        this.name = name;
        this.num = num;
        this.payValue = payValue;
        this.paytime = paytime;
        this.state = state;
        this.storeId = storeId;
        this.storeName = storeName;
        this.storeLogoUrl = storeLogoUrl;
    }

    public IndentBean() {
    }

    public double getPostAmount() {
        return postAmount;
    }

    public void setPostAmount(double postAmount) {
        this.postAmount = postAmount;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStoreLogoUrl() {
        return storeLogoUrl;
    }

    public void setStoreLogoUrl(String storeLogoUrl) {
        this.storeLogoUrl = storeLogoUrl;
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

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
    }

    public int getDisValue() {
        return disValue;
    }

    public void setDisValue(int disValue) {
        this.disValue = disValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getPayValue() {
        return payValue;
    }

    public void setPayValue(double payValue) {
        this.payValue = payValue;
    }

    public String getPaytime() {
        return paytime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    @Override
    public String toString() {
        return "IndentBean{" +
                "id=" + id +
                ", finishTime='" + finishTime + '\'' +
                ", insertTime='" + insertTime + '\'' +
                ", disValue=" + disValue +
                ", name='" + name + '\'' +
                ", num=" + num +
                ", payValue=" + payValue +
                ", paytime='" + paytime + '\'' +
                ", state=" + state +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", storeLogoUrl='" + storeLogoUrl + '\'' +
                '}';
    }
}
