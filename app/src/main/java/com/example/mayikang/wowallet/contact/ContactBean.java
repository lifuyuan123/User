package com.example.mayikang.wowallet.contact;

import com.example.mayikang.wowallet.contact.IndexBar.bean.BaseIndexPinyinBean;

/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

public class ContactBean extends BaseIndexPinyinBean {
    private int id;
    private String logo;
    private String name;
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ContactBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public ContactBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getTarget() {
        return name;
    }
}
