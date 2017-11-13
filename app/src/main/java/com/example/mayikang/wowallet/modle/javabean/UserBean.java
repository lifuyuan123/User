package com.example.mayikang.wowallet.modle.javabean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by mayikang on 17/5/23.
 */

public class UserBean implements Serializable {

    private int id;

    private String phone;

    private String username;
    private String email;
    private int sex;
    private String url;
    private String realName;
    private int userFans;
    private int agent;
    private int isShareMaker;
    private String alipayNumber;
    private String invitationCode;


    //是否设置了支付密码

    private int hasPayPassword=0;

    private String insert_time;

    public String getInsert_time() {
        return insert_time;
    }

    public int getUserFans() {
        return userFans;
    }

    public void setUserFans(int userFans) {
        this.userFans = userFans;
    }

    public void setInsert_time(String insert_time) {
        this.insert_time = insert_time;
    }

    public boolean isHasPayPassword() {
        return hasPayPassword==0?false:true;
    }

    public void setHasPayPassword(boolean hasPayPassword) {
        if(hasPayPassword){
            this.hasPayPassword = 1;
        }else {
            this.hasPayPassword = 0;
        }

    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public String getAlipayNumber() {
        return alipayNumber;
    }

    public void setAlipayNumber(String alipayNumber) {
        this.alipayNumber = alipayNumber;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getAgent() {
        return agent;
    }

    public int getIsShareMaker() {
        return isShareMaker;
    }

    public void setIsShareMaker(int isShareMaker) {
        this.isShareMaker = isShareMaker;
    }

    public void setAgent(int agent) {
        this.agent = agent;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }




    public UserBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", sex=" + sex +
                ", url='" + url + '\'' +
                ", realName='" + realName + '\'' +
                '}';
    }
}
