package com.example.mayikang.wowallet.event;

/**
 * Created by mayikang on 17/6/1.
 */

public class NormalOpEvent {

    //操作后结果 1、成功  2、失败
    private int opStatus;

    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getOpStatus() {
        return opStatus;
    }

    public void setOpStatus(int opStatus) {
        this.opStatus = opStatus;
    }

    public NormalOpEvent(int opStatus) {

        this.opStatus = opStatus;
    }

    public NormalOpEvent(int opStatus, int index) {
        this.opStatus = opStatus;
        this.index = index;
    }
}
