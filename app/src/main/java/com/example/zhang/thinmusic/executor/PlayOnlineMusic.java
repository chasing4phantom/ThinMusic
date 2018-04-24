package com.example.zhang.thinmusic.executor;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.example.zhang.thinmusic.executor.PlayMusic;
import com.example.zhang.thinmusic.http.HttpCallback;
import com.example.zhang.thinmusic.http.HttpClient;
import com.example.zhang.thinmusic.model.DownloadInfo;
import com.example.zhang.thinmusic.model.Music;
import com.example.zhang.thinmusic.model.OnLineMusic;
import com.example.zhang.thinmusic.utils.FileUtils;

import java.io.File;

/**
 * Created by zhang on 2018/4/20.
 */

public abstract class PlayOnlineMusic extends PlayMusic {
    private OnLineMusic onLineMusic;
    public PlayOnlineMusic(Activity activity,OnLineMusic onLineMusic){
        super(activity,3);
        this.onLineMusic = onLineMusic;
    }

    @Override
    protected void getPlayInfo(){
        String artist = onLineMusic.getArtisit_name();
        String title = onLineMusic.getTitle();

        music = new Music();
        music.setType(Music.Type.ONLINE);
        music.setTitle(title);
        music.setArtist(artist);
        music.setAlbum(onLineMusic.getAlbum_title());

        //下载歌词
        String lrcFileName = FileUtils.getLrcFileName(artist,title);
        File lrcFile = new File(FileUtils.getLrcDir()+ lrcFileName);
        if(!lrcFile.exists() && !TextUtils.isEmpty(onLineMusic.getLrclink())){
            downloadLrc(onLineMusic.getLrclink(), lrcFileName);
        }else{
            Counter++;
        }

        //下载封面
        String albumFileName = FileUtils.getAlbumFileName(artist,title);
        File albumFile = new File(FileUtils.getAlbumDir(),albumFileName);
        String picUrl = onLineMusic.getPic_big();
        if(TextUtils.isEmpty(picUrl)){
            picUrl = onLineMusic.getPic_small();
        }
        if(!albumFile.exists()&& !TextUtils.isEmpty(picUrl)){
            downloadAlbum(picUrl,albumFileName);
        }else{
            Counter++;
        }
        music.setCoverPath(albumFile.getPath());

        //获取歌曲播放链接
        HttpClient.getMusicDownloadInfo(onLineMusic.getSong_id(),new HttpCallback<DownloadInfo>(){
            @Override
            public void onSuccess(DownloadInfo response){
                if(response == null|| response.getBitrate() == null){
                    onFail(null);
                    return;
                }

                music.setPath(response.getBitrate().getFile_link());
                music.setDuration(response.getBitrate().getFile_duration() * 1000);

                checkCounter();
            }

            @Override
            public void onFail(Exception e){onExecuteFail(e);}
        });
    }

    private void downloadLrc(String url,String fileName){
        HttpClient.downloadFile(url, FileUtils.getLrcDir(), fileName, new HttpCallback<File>() {
            @Override
            public void onSuccess(File file) {

            }

            @Override
            public void onFail(Exception e) {

            }

            @Override
            public void onFinish(){checkCounter();}
        });
    }

    private void downloadAlbum(String picUrl,String fileName){
        HttpClient.downloadFile(picUrl, FileUtils.getAlbumDir(), fileName, new HttpCallback<File>() {
            @Override
            public void onSuccess(File file) {

            }

            @Override
            public void onFail(Exception e) {

            }
            @Override
            public void onFinish(){checkCounter();}
        });
    }
}
