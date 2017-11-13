package com.example.mayikang.wowallet.event;

/**
 * Created by mayikang on 17/6/12.
 */

public class AfterInputPayPWDEvent {
    /**
     * 1:取消
     * 2:成功
     */
    private int op;

    /**
     * 转账金额
     */
    private double payAmount;

    private String payPwd;

    public String getPayPwd() {
        return payPwd;
    }

    public void setPayPwd(String payPwd) {
        this.payPwd = payPwd;
    }

    public double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(double payAmount) {
        this.payAmount = payAmount;
    }


    public AfterInputPayPWDEvent(int op, double payAmount, String payPwd) {
        this.op = op;
        this.payAmount = payAmount;
        this.payPwd = payPwd;
    }

    public int getOp() {
        return op;
    }

    public void setOp(int op) {
        this.op = op;
    }
}
