package com.li.libook.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
@Entity
public class DownLoad {
    @Id
    private String id;
    private String path;
    private String name;
    private String progress;
    private String total;
    private int type;//0:未下载，1：暂停，2：正在下载，3：已下载
    @Generated(hash = 811801441)
    public DownLoad(String id, String path, String name, String progress,
            String total, int type) {
        this.id = id;
        this.path = path;
        this.name = name;
        this.progress = progress;
        this.total = total;
        this.type = type;
    }
    @Generated(hash = 89475367)
    public DownLoad() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPath() {
        return this.path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getProgress() {
        return this.progress;
    }
    public void setProgress(String progress) {
        this.progress = progress;
    }
    public String getTotal() {
        return this.total;
    }
    public void setTotal(String total) {
        this.total = total;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
}
