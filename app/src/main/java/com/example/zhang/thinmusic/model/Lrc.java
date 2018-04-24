package com.example.zhang.thinmusic.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhang on 2018/4/17.
 */

public class Lrc {
    @SerializedName("lrcContent")
    private String lrcContent;

    public String getLrcContent(){return lrcContent;}

    public void setLrcContent(String lrcContent){this.lrcContent = lrcContent;}
}
