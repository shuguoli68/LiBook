package com.li.libook.model.bean;

import android.os.Parcel;
import android.os.Parcelable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Administrator on 2015/12/27.
 */
@Entity
public class Book implements Parcelable {
    @Id
    private String bookname;
    private String bookpath;
    private long begin;
    private long readsection;
    private long total;
    private String charset;
    private int resource;
    private int state;
    private String section;
    private String update;
    private String end;
    @Generated(hash = 740829486)
    public Book(String bookname, String bookpath, long begin, long readsection,
            long total, String charset, int resource, int state, String section,
            String update, String end) {
        this.bookname = bookname;
        this.bookpath = bookpath;
        this.begin = begin;
        this.readsection = readsection;
        this.total = total;
        this.charset = charset;
        this.resource = resource;
        this.state = state;
        this.section = section;
        this.update = update;
        this.end = end;
    }
    @Generated(hash = 1839243756)
    public Book() {
    }
    public String getBookname() {
        return this.bookname;
    }
    public void setBookname(String bookname) {
        this.bookname = bookname;
    }
    public String getBookpath() {
        return this.bookpath;
    }
    public void setBookpath(String bookpath) {
        this.bookpath = bookpath;
    }
    public long getBegin() {
        return this.begin;
    }
    public void setBegin(long begin) {
        this.begin = begin;
    }
    public long getReadsection() {
        return this.readsection;
    }
    public void setReadsection(long readsection) {
        this.readsection = readsection;
    }
    public long getTotal() {
        return this.total;
    }
    public void setTotal(long total) {
        this.total = total;
    }
    public String getCharset() {
        return this.charset;
    }
    public void setCharset(String charset) {
        this.charset = charset;
    }
    public int getResource() {
        return this.resource;
    }
    public void setResource(int resource) {
        this.resource = resource;
    }
    public int getState() {
        return this.state;
    }
    public void setState(int state) {
        this.state = state;
    }
    public String getSection() {
        return this.section;
    }
    public void setSection(String section) {
        this.section = section;
    }
    public String getUpdate() {
        return this.update;
    }
    public void setUpdate(String update) {
        this.update = update;
    }
    public String getEnd() {
        return this.end;
    }
    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bookname);
        dest.writeString(this.bookpath);
        dest.writeLong(this.begin);
        dest.writeLong(this.readsection);
        dest.writeLong(this.total);
        dest.writeString(this.charset);
        dest.writeInt(this.resource);
        dest.writeInt(this.state);
        dest.writeString(this.section);
        dest.writeString(this.update);
        dest.writeString(this.end);
    }

    protected Book(Parcel in) {
        this.bookname = in.readString();
        this.bookpath = in.readString();
        this.begin = in.readLong();
        this.readsection = in.readLong();
        this.total = in.readLong();
        this.charset = in.readString();
        this.resource = in.readInt();
        this.state = in.readInt();
        this.section = in.readString();
        this.update = in.readString();
        this.end = in.readString();
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
