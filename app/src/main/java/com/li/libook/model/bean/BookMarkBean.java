package com.li.libook.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Admin on 2018/3/7.
 */
@Entity
public class BookMarkBean {
    @Id
    private Long id;
    private String bookpath;
    private String text;
    private String progress;
    private String time;
    private long begin;
    @Generated(hash = 144281501)
    public BookMarkBean(Long id, String bookpath, String text, String progress,
            String time, long begin) {
        this.id = id;
        this.bookpath = bookpath;
        this.text = text;
        this.progress = progress;
        this.time = time;
        this.begin = begin;
    }
    @Generated(hash = 237936453)
    public BookMarkBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getBookpath() {
        return this.bookpath;
    }
    public void setBookpath(String bookpath) {
        this.bookpath = bookpath;
    }
    public String getText() {
        return this.text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getProgress() {
        return this.progress;
    }
    public void setProgress(String progress) {
        this.progress = progress;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public long getBegin() {
        return this.begin;
    }
    public void setBegin(long begin) {
        this.begin = begin;
    }
}
