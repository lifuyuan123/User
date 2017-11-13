package com.example.mayikang.wowallet.modle.javabean;

/**
 * Created by lifuyuan on 2017/5/10.
 */

public class CollectioniBean {
    String title,content,iconurl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    @Override
    public String toString() {
        return "CollectioniBean{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", iconurl='" + iconurl + '\'' +
                '}';
    }
}
