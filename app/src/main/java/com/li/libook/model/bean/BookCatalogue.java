package com.li.libook.model.bean;
//import org.litepal.crud.DataSupport;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Lxq on 2016/4/9.
 */
@Entity
public class BookCatalogue {
    @Id
    private String bookname;
    private String bookpath;
    private String bookCatalogue;
    private long bookCatalogueStartPos;
    @Generated(hash = 1936877802)
    public BookCatalogue(String bookname, String bookpath, String bookCatalogue,
            long bookCatalogueStartPos) {
        this.bookname = bookname;
        this.bookpath = bookpath;
        this.bookCatalogue = bookCatalogue;
        this.bookCatalogueStartPos = bookCatalogueStartPos;
    }
    @Generated(hash = 1988414870)
    public BookCatalogue() {
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
    public String getBookCatalogue() {
        return this.bookCatalogue;
    }
    public void setBookCatalogue(String bookCatalogue) {
        this.bookCatalogue = bookCatalogue;
    }
    public long getBookCatalogueStartPos() {
        return this.bookCatalogueStartPos;
    }
    public void setBookCatalogueStartPos(long bookCatalogueStartPos) {
        this.bookCatalogueStartPos = bookCatalogueStartPos;
    }
}

