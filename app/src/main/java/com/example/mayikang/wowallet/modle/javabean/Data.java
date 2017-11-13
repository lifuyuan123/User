package com.example.mayikang.wowallet.modle.javabean;

/**
 * Created by lifuyuan on 2017/5/9.
 */

public class Data {

    String name,distance,content,sold_number,iconurl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSold_number() {
        return sold_number;
    }

    public void setSold_number(String sold_number) {
        this.sold_number = sold_number;
    }

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    @Override
    public String toString() {
        return "Data{" +
                "name='" + name + '\'' +
                ", distance='" + distance + '\'' +
                ", content='" + content + '\'' +
                ", sold_number='" + sold_number + '\'' +
                ", iconurl='" + iconurl + '\'' +
                '}';
    }
}
