package com.example.zhang.thinmusic.utils;

import android.content.Context;
import android.media.MediaPlayer;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;


import com.example.zhang.thinmusic.PlayModeEnum;
import com.example.zhang.thinmusic.model.Music;
import com.example.zhang.thinmusic.service.OnPlayerListener;
import com.example.zhang.thinmusic.storage.DBManager;
import com.example.zhang.thinmusic.MediaSessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**mediaplayer封装
 * Created by zhang on 2018/3/19.
 */

public class AudioPlayer {
    private static final int STATE_IDLE = 0;
    private static final int STATE_PREPARING = 1;
    private static final int STATE_PLAYING = 2;
    private static final int STATE_PAUSE = 3;

    private static final long TIME_UPDATE = 300L;

    private Context context;
    private MediaPlayer mediaPlayer;
    private Handler handler;
    private List<Music> musicList;
    private  final List<OnPlayerListener> listeners = new ArrayList<>();
    private int state = STATE_IDLE;

    //单例模式
    public static AudioPlayer get(){return SingletonHolder.instance;}

    private static class SingletonHolder{
        private static AudioPlayer instance = new AudioPlayer();
    }

    private AudioPlayer(){}

    //播放器初始化
    public void init(Context context) {
        this.context = context.getApplicationContext();
        musicList = DBManager.get().getMusicDao().queryBuilder().build().list();
        mediaPlayer = new MediaPlayer();
        handler = new Handler(Looper.getMainLooper());
        mediaPlayer.setOnCompletionListener(mp ->next());
        mediaPlayer.setOnPreparedListener(mp ->{
            if(isPreparing()){
                startPlayer();
            }
        });
        mediaPlayer.setOnBufferingUpdateListener((mp, percent)->{
            for (OnPlayerListener listener : listeners){
                listener.onBufferingUpdate(percent);
            } //监听网络音乐缓冲百分比
        });
    }

    public void addOnPlayListener(OnPlayerListener listener){
        if(!listeners.contains(listener)){
            listeners.add(listener);
        }
    }
    public void removeOnPlayListener(OnPlayerListener   listener){
        listeners.remove(listener);
    }

    public  void addAndPlay(Music music){
        int position = musicList.indexOf(music);
        if(position < 0){
            musicList.add(music);
            DBManager.get().getMusicDao().insert(music);
            position = musicList.size() - 1;
        }
        Log.i(String.valueOf(music.getFileName()), "addAndPlay: ");
        play(position);
    }
    public void play(int position){
        if(musicList.isEmpty()){
            return;
        }
        if(position <0){
            position = musicList.size() - 1;
        }else if(position >= musicList.size()){
            position = 0;
        }

        setPlayPosition(position);
        Log.d("position = ", String.valueOf(position));
        Music music = getPlayMusic();
        Log.d(music.getFileName(), "play() returned: " + getPlayMusic());
        try{
            mediaPlayer.reset();
            mediaPlayer.setDataSource(music.getPath());
            mediaPlayer.prepareAsync();
            state = STATE_PREPARING;
            for(OnPlayerListener listener :listeners){
                listener.onChange(music);
            }
            /*Notifier.get().showPlay(music);*/
            MediaSessionManager.get().updateMetaData(music);
            MediaSessionManager.get().updatePlaybackState();
        } catch(IOException e){
            e.printStackTrace();
            ToastUtils.show("当前歌曲无法播放");
            }

    }

    public void delete(int position){
        int playPosition = getPlayPosition();
        Music music = musicList.remove(position);
        DBManager.get().getMusicDao().delete(music);
        if(playPosition > position){
            setPlayPosition(playPosition - 1);
        }else if(playPosition == position){
            if(isPlaying()|| isPreparing()){
                setPlayPosition(playPosition -1);
                next();
            }else{
                stopPlayer();
                for(OnPlayerListener listener : listeners){
                    listener.onChange(getPlayMusic());
                }
            }
        }
    }

    public  void playPause(){
        if(isPreparing()){
            stopPlayer();
        }else if(isPlaying()){
            pausePlayer();
        }else if(isPausing()){
            startPlayer();
        }else {
            play(getPlayPosition());
        }
    }

    public  void startPlayer(){
        if(!isPreparing() && !isPausing()){
            return;
        }
        mediaPlayer.start();
        state = STATE_PLAYING;
        handler.post(mPublishRunnable);
        /*Notifier.get().showPlay(getPlayMusic());*/
        MediaSessionManager.get().updatePlaybackState();
        for(OnPlayerListener listener : listeners){
            listener.onPlayerStart();

        }
    }
    public void pausePlayer(){pausePlayer(true);}

    public void pausePlayer(boolean abandAudioFocus) {
        if(!isPlaying()){
            return;
        }
        mediaPlayer.pause();
        state = STATE_PAUSE;
        handler.removeCallbacks(mPublishRunnable);
        //Notifier.get().showPause(getPlayMusic());
        MediaSessionManager.get().updatePlaybackState();
        for(OnPlayerListener listener : listeners){
            listener.onPlayerPause();
        }
    }

    public void stopPlayer(){
        if(isIdle()){
            return;
        }

        pausePlayer();
        mediaPlayer.reset();
        state = STATE_IDLE;
    }

    public void next(){
        if(musicList.isEmpty()){
            return;
        }

        PlayModeEnum mode = PlayModeEnum.valueOf(Preferences.getPlayMode());
        switch (mode){
            case  SHUFFLE:
                play(new Random().nextInt(musicList.size()));
                break;
            case SINGLE:
                play(getPlayPosition());
                break;
            case LOOP:
            default:
                play(getPlayPosition() + 1);
                break;
        }
    }

    public void prev(){
        if(musicList.isEmpty()){
            return;
        }
        PlayModeEnum mode = PlayModeEnum.valueOf(Preferences.getPlayMode());
        switch (mode){
            case SHUFFLE:
                play(new Random().nextInt(musicList.size()));
                break;
            case SINGLE:
                play(getPlayPosition());
                break;
            case LOOP:
            default:
                play(getPlayPosition() - 1);
                break;
        }
    }

    public  void seekTo(int msec){
        if(isPlaying() || isPausing()){
            mediaPlayer.seekTo(msec);
            MediaSessionManager.get().updatePlaybackState();
            for(OnPlayerListener listener : listeners){
                listener.onPublish(msec);
            }
        }
    }

    //子进程刷新播放进度条
    private Runnable mPublishRunnable = new Runnable() {
        @Override
        public void run() {
            if(isPlaying()){
                for(OnPlayerListener listener :listeners){
                    listener.onPublish(mediaPlayer.getCurrentPosition());
                }
            }
        handler.postDelayed(this, TIME_UPDATE);
        }
    };

    public int getAudioSessionId(){
        return  mediaPlayer.getAudioSessionId();
    }
    public long getAudioPosition(){
        if(isPlaying()|| isPausing()){
            return mediaPlayer.getCurrentPosition();
        }else{
            return 0;
        }
    }
    public Music getPlayMusic(){
        if(musicList.isEmpty()){
            return null;
        }
        return  musicList.get(getPlayPosition());
    }

    public MediaPlayer getMediaPlayer(){return mediaPlayer;}

    public List<Music> getMusicList(){return  musicList;}

    public boolean isPlaying(){return  state == STATE_PLAYING;}

    public boolean isPreparing(){return  state == STATE_PREPARING;}

    public boolean isPausing(){return  state == STATE_PAUSE;}

    public boolean isIdle(){return  state == STATE_IDLE;}

    public int getPlayPosition(){
        int position = Preferences.getPlayPosition();
        if(position < 0 || position >= musicList.size()){
            position = 0;
            Preferences.savePlayPosition (position);
        }
        return  position;
    }

    private  void  setPlayPosition(int position){Preferences.savePlayPosition(position);}
}
