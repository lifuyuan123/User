package com.example.mayikang.wowallet.event;

/**
 * Created by mayikang on 17/6/8.
 */

public class PageSwitchEvent {
    //目标页面
    private String target;

    public PageSwitchEvent(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
