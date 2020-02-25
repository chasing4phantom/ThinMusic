package com.example.zhang.thinmusic.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/*创建网易云音乐实体类*/
public class NeteaseMusic {
    @SerializedName("data")
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public static class Data{
        @SerializedName("id")
        private String id;
        @SerializedName("url")
        private String url;
        @SerializedName("br")
        private String br;//码率,默认设置了 999000 即最大码率,如果要 320k 则可设置为 320000,其他类推
        @SerializedName("name")
        private String name;//歌曲名
        @SerializedName("code")
        private String code;//校验码，200为成功
        @SerializedName("level")
        private String level;//音质
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getBr() {
            return br;
        }

        public void setBr(String br) {
            this.br = br;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }
    }

    @SerializedName("id")
    private String id;
    @SerializedName("url")
    private String url;
    @SerializedName("br")
    private String br;//码率,默认设置了 999000 即最大码率,如果要 320k 则可设置为 320000,其他类推
    @SerializedName("name")
    private String name;//歌曲名
    @SerializedName("code")
    private String code;//校验码，200为成功
    @SerializedName("level")
    private String level;//音质
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBr() {
        return br;
    }

    public void setBr(String br) {
        this.br = br;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @SerializedName("ar")
    private List<ArtistList> ar;

    public String getArtist(List<ArtistList> ar){
        String artist="";
        int size= ar.size();
        for(int i=0;i<size-1;i++){
           artist+=(ar.get(i).getName()+"/");
        }
        artist+=ar.get(size-1).getName();
        return artist;
    }

    private static class ArtistList{
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @SerializedName("al")
    private Album al;
    public String getAlbum_title(Album al){
        return al.name;
    }
    public String getAlbum_Id(Album al){
        return al.getId();
    }
    public String getCoverurl(){
        return al.getPicUrl();
    }
    private static class Album{
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("picUrl")
        private String picUrl;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }
    }

    @SerializedName("h")
    private HighQuality h;
    public static class HighQuality{
        @SerializedName("br")
        private String br;
        @SerializedName("size")
        private String size;
    }
    @SerializedName("m")
    private MiddleQuality m;
    public static class MiddleQuality{
        @SerializedName("br")
        private String br;
        @SerializedName("size")
        private String size;
    }
    @SerializedName("l")
    private LowQuality l;
    public static class LowQuality{
        @SerializedName("br")
        private String br;
        @SerializedName("size")
        private String size;
    }

    public List<ArtistList> getAr() {
        return ar;
    }


    public void setAr(List<ArtistList> ar) {
        this.ar = ar;
    }

    public Album getAl() {
        return al;
    }

    public void setAl(Album al) {
        this.al = al;
    }

    public HighQuality getH() {
        return h;
    }

    public void setH(HighQuality h) {
        this.h = h;
    }

    public MiddleQuality getM() {
        return m;
    }

    public void setM(MiddleQuality m) {
        this.m = m;
    }

    public LowQuality getL() {
        return l;
    }

    public void setL(LowQuality l) {
        this.l = l;
    }

}
