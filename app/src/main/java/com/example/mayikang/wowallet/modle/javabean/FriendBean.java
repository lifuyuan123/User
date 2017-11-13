package com.example.mayikang.wowallet.modle.javabean;

/**
 * Created by lifuyuan on 2017/5/9.
 */

public class FriendBean {
    String name,title,money,Iconurl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getIconurl() {
        return Iconurl;
    }

    public void setIconurl(String iconurl) {
        Iconurl = iconurl;
    }

    @Override
    public String toString() {
        return "Bean{" +
                "name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", money='" + money + '\'' +
                ", Iconurl='" + Iconurl + '\'' +
                '}';
    }
}
