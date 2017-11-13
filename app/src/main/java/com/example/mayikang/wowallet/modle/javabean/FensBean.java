package com.example.mayikang.wowallet.modle.javabean;

/**
 * Created by lifuyuan on 2017/5/10.
 */

public class FensBean {
    private int id;
    private String phone,time,iconurl;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    @Override
    public String toString() {
        return "FensBean{" +
                "phone='" + phone + '\'' +
                ", time='" + time + '\'' +
                ", iconurl='" + iconurl + '\'' +
                '}';
    }
}
