package com.example.zhang.thinmusic.model;


import java.io.Serializable;

public class NeteaseListInfo implements Serializable {
    private String title;

    private String coverUrl;
    private String count;

    private String type;//判断是否是歌单名称还是歌单分类
    private String id;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
