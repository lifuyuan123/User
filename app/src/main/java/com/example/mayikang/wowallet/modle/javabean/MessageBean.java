package com.example.mayikang.wowallet.modle.javabean;

import java.io.Serializable;

/**
 * Created by liuha on 2017/5/15.
 */

public class MessageBean implements Serializable {
    private int id;
    private String content;//内容
    private int status;//状态 1-未读  2-已读
    private int type;//类型 1-系统消息 2-交易消息  3-店铺通知
    private String title;
    private int deletestatus_to;//接收者测状态 1-删除 2-未删
    private String url;

    private  UserBean bean;
    private String  insert_time;//添加时间
    private UserBean userBean;

    public UserBean getBean() {
        return userBean;
    }

    public void setBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public MessageBean(int id, String content, int status, int type) {
        this.id = id;
        this.content = content;
        this.status = status;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public int getDeletestatus_to() {
        return deletestatus_to;
    }

    public void setDeletestatus_to(int deletestatus_to) {
        this.deletestatus_to = deletestatus_to;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInsert_time() {
        return insert_time;
    }

    public void setInsert_time(String insert_time) {
        this.insert_time = insert_time;
    }



    public MessageBean() {
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", type=" + type +
                '}';
    }
}
