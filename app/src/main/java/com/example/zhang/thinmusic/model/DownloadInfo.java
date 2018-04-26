package com.example.zhang.thinmusic.model;

import com.google.gson.annotations.SerializedName;

/**定义实体类
 * Created by zhang on 2018/4/17.
 */

public class DownloadInfo {
    @SerializedName("bitrate")
    private Bitrate bitrate;//比特率

    public Bitrate getBitrate(){return bitrate;}

    public void setBitrate(Bitrate bitrate){this.bitrate = bitrate;}

    public static class Bitrate{
        @SerializedName("file_duration")
        private int file_duration;
        @SerializedName("file_link")
        private String file_link;

        public int getFile_duration(){return file_duration;}

        public void setFile_duration(int file_duration){this.file_duration = file_duration;}

        public String getFile_link(){return file_link;}

        public void setFile_link(String file_link){this.file_link = file_link;}

    }
}
