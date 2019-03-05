package com.li.libook.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ShelfBean {
    String title;
    String author;
    String img;
    @Id
    String url;
    String update;
    String lastTime;
    @Generated(hash = 318892420)
    public ShelfBean(String title, String author, String img, String url,
            String update, String lastTime) {
        this.title = title;
        this.author = author;
        this.img = img;
        this.url = url;
        this.update = update;
        this.lastTime = lastTime;
    }
    @Generated(hash = 218333862)
    public ShelfBean() {
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getAuthor() {
        return this.author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getImg() {
        return this.img;
    }
    public void setImg(String img) {
        this.img = img;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getUpdate() {
        return this.update;
    }
    public void setUpdate(String update) {
        this.update = update;
    }
    public String getLastTime() {
        return this.lastTime;
    }
    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }
}
