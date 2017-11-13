package com.example.mayikang.wowallet.modle.javabean;

import java.io.Serializable;

/**
 * Created by mayikang on 17/5/24.
 */

public class StoreBean implements Serializable {
    private int id;
    private String name;
    private String logo;
    private int popularity;
    private String detail;
    private double longitude=0d;
    private double latitude=0d;
    private int type;//店铺类型 1.到店消费付款  2.线上交易（快递）


    private double score;

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getDetail() {
        return detail;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public StoreBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }
}
