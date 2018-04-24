package com.example.zhang.thinmusic.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhang on 2018/4/16.
 */

public class OnLineMusicList {
    @SerializedName("song_list")
    private List<OnLineMusic> song_list;
    @SerializedName("billboard")
    private Billboard billboard;

    public List<OnLineMusic> getSong_list(){return song_list;}

    public void setSong_list(List<OnLineMusic> song_list){this.song_list = song_list;}

    public Billboard getBillboard(){return billboard;}

    public void setBillboard(Billboard billboard){this.billboard = billboard;}

    public static class Billboard{
        @SerializedName("update_data")
        private String update_data;
        @SerializedName("name")
        private String name;
        @SerializedName("comment")
        private String comment;
        @SerializedName("pic_s640")
        private String pic_s640;
        @SerializedName("pic_s444")
        private String pic_s444;
        @SerializedName("pic_s260")
        private String pic_s260;
        @SerializedName("pic_s210")
        private String pic_s210;

        public String getUpdate_data(){return update_data;}

        public void setUpdate_data(String update_data){this.update_data = update_data;}

        public String getName(){return name;}

        public void setName(String name){this.name = name;}

        public String getComment(){return comment;}

        public void setComment(String comment){this.comment = comment;}

        public String getPic_s640(){return pic_s640;}

        public void setPic_s640(String pic_s640){this.pic_s640 = pic_s640;}

        public String getPic_s444() {
            return pic_s444;
        }

        public void setPic_s444(String pic_s444) {
            this.pic_s444 = pic_s444;
        }

        public String getPic_s260(){return pic_s260;}

        public void setPic_s260(String pic_s260){this.pic_s260 = pic_s260;}

        public String getPic_s210() {
            return pic_s210;
        }

        public void setPic_s210(String pic_s210) {
            this.pic_s210 = pic_s210;
        }
    }
}
