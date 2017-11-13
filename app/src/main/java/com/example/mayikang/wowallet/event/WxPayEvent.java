package com.example.mayikang.wowallet.event;

/**
 * Created by haohaoliu on 2017/7/20.
 * explain:微信支付的事件
 */

public class WxPayEvent {
    //0：成功  -1错误 -2取消
    private  int state;

    public WxPayEvent(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
