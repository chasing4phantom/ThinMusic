package com.example.zhang.thinmusic.model;

import com.google.gson.annotations.SerializedName;

public class NeteaseLyric {
    @SerializedName("code")
    private String code;
    @SerializedName("lrc")
    private Lrc lrc;

    public static class Lrc{
        @SerializedName("lyric")
        private String lyric;
        @SerializedName("version")
        private String version;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Lrc getLrc() {
        return lrc;
    }

    public void setLrc(Lrc lrc) {
        this.lrc = lrc;
    }
}
