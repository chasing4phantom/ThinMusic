package com.example.zhang.thinmusic;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;


/**
 * Created by zhang on 2018/2/13.
 */

public class MusicPlayService extends Service {
    private String url;
    private MusicPlayerBinder musicPlayerBinder = new MusicPlayerBinder();

    public MediaPlayer mediaPlayer = new MediaPlayer() ;
    @Override
    public IBinder onBind(Intent intent) {
        return musicPlayerBinder;
    }


    class MusicPlayerBinder extends Binder {

        public void start(){
            if(mediaPlayer != null)
            {mediaPlayer.reset();
            initMusicPlayer(url);
            mediaPlayer.start();}
        }
        public void play(){
            if(!mediaPlayer.isPlaying()) {
               /* mediaPlayer.reset();
                initMusicPlayer(url);*/
                mediaPlayer.start();
            }/*else {
                mediaPlayer.start();
            }*/

        }
        public void pause(){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
            }

        }
        public void initMusicPlayer(String url){

            try{
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepare();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        public void getUrl(String url){
            MusicPlayService.this.url = url;

        }
        public int getProgress(){
            return mediaPlayer.getDuration();
        }
        public int getPlayPosition(){
            return mediaPlayer.getCurrentPosition();
        }
        public void seekToPosition(int msec){
            mediaPlayer.seekTo(msec);
        }
        public void closeMedia(){
            if(mediaPlayer!= null){
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
        }
    }

}
