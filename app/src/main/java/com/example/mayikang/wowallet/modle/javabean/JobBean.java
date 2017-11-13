package com.example.mayikang.wowallet.modle.javabean;

import java.io.Serializable;

/**
 * Created by mayikang on 17/5/25.
 */

public class JobBean implements Serializable {
    private int id;
    private String name;

    public JobBean() {
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
}
