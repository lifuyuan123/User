package com.example.mayikang.wowallet.modle.javabean;

/**
 * Created by haohaoliu on 2017/6/27.
 * explain:照片的javabean
 */

public class PhotoBean {
    private int id;
    private String url;
    private String type;


    public PhotoBean() {

    }

    public PhotoBean(String type) {
        this.type = type;
    }

    public PhotoBean(int id, String url ) {
        this.id = id;
        this.url = url;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
