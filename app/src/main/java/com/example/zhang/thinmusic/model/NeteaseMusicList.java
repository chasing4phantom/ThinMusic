package com.example.zhang.thinmusic.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/*定义歌单实体类*/
public class NeteaseMusicList {

    @SerializedName("playlist")
    private PlayList playlist;
    @SerializedName("code")
    private String code;//校验码，200==true

    public PlayList getPlaylist() {
        return playlist;
    }

    public void setPlaylist(PlayList playlist) {
        this.playlist = playlist;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class PlayList{
        @SerializedName("CreateTime")
        private String createTime;
        @SerializedName("coverImgId")
        private String coverImgId;
        @SerializedName("coverImgUrl")
        private String coverImgUrl;
        @SerializedName("updateTime")
        private String updateTime;
        @SerializedName("trackCount")
        private String trackCount;
        @SerializedName("playCount")
        private String playCount;
        @SerializedName("description")
        private String description;
        @SerializedName("name")
        private String name;
        @SerializedName("id")
        private String id;
        @SerializedName("tracks")
        private List<NeteaseMusic> tracks;

        @SerializedName("creator")
        private Creator creator;

        public static class Creator{
            @SerializedName("pronvince")
            String province;
            @SerializedName("avatarUrl")
            String avatarUrl;//作者头像
            @SerializedName("nickname")
            String nickname;//昵称
            @SerializedName("signature")
            String signature;
            @SerializedName("backgroundUrl")
            String backgroundUrl;
        }



        public Creator getCreator() {
            return creator;
        }

        public void setCreator(Creator creator) {
            this.creator = creator;
        }

        public List<NeteaseMusic> getTracks() {
            return tracks;
        }

        public void setTracks(List<NeteaseMusic> tracks) {
            this.tracks = tracks;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCoverImgId() {
            return coverImgId;
        }

        public void setCoverImgId(String coverImgId) {
            this.coverImgId = coverImgId;
        }

        public String getCoverImgUrl() {
            return coverImgUrl;
        }

        public void setCoverImgUrl(String coverImgUrl) {
            this.coverImgUrl = coverImgUrl;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getTrackCount() {
            return trackCount;
        }

        public void setTrackCount(String trackCount) {
            this.trackCount = trackCount;
        }

        public String getPlayCount() {
            return playCount;
        }

        public void setPlayCount(String playCount) {
            this.playCount = playCount;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }


}
