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

        public String getLyric() {
            return lyric;
        }

        public void setLyric(String lyric) {
            this.lyric = lyric;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
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
