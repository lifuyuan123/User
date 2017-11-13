package com.example.mayikang.wowallet.modle.javabean;

import android.graphics.Bitmap;

/**
 * Created by mayikang on 17/5/18.
 */

public class ImageBean {
    private int id;
    private String url;
    private double height;
    private double width;



    public ImageBean() {

    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
