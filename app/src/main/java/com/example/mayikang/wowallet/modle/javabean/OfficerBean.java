package com.example.mayikang.wowallet.modle.javabean;

import java.io.Serializable;

/**
 * Created by liuha on 2017/5/15.
 */

public class OfficerBean implements Serializable {
    private int id;
    private String name;
    private JobBean jobBean;
    private String logo;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public OfficerBean() {
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

    public JobBean getJobBean() {
        return jobBean;
    }

    public void setJobBean(JobBean jobBean) {
        this.jobBean = jobBean;
    }
}
