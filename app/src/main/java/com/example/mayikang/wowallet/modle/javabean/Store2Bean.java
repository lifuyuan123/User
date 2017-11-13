package com.example.mayikang.wowallet.modle.javabean;

import java.io.Serializable;

/**
 * Created by lifuy on 2017/9/25.
 */

public class Store2Bean implements Serializable {

    private int id;
    private String IconUrl;
    private String title;
    private Double price;//价格
    private int count;//数量
    private int type;//free shipping   包邮与不包邮
    private boolean ischeck;//是否选中

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIconUrl() {
        return IconUrl;
    }

    public void setIconUrl(String iconUrl) {
        IconUrl = iconUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean ischeck() {
        return ischeck;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
    }

    @Override
    public String toString() {
        return "Store2Bean{" +
                "id=" + id +
                ", IconUrl='" + IconUrl + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", count=" + count +
                ", type=" + type +
                ", ischeck=" + ischeck +
                '}';
    }
}
