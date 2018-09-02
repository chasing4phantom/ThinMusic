package com.example.zhang.thinmusic.service;

import android.content.Context;
import android.media.AudioManager;

import com.example.zhang.thinmusic.utils.AudioPlayer;

public class AudioFocusManager implements AudioManager.OnAudioFocusChangeListener {
    private AudioManager audioManager;
    private boolean isPausedbyFocusLossTransient;

    public AudioFocusManager(Context context){
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public boolean requestAudioFocus(){
        return audioManager.requestAudioFocus(this,AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN)==
                AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
    }

    public void abandonAudioFocus(){audioManager.abandonAudioFocus(this);}

    @Override
    public void onAudioFocusChange(int focusChange){
        switch (focusChange){
            //重新获得焦点
            case AudioManager.AUDIOFOCUS_GAIN:
                if(isPausedbyFocusLossTransient){
                    //通话结束，恢复播放
                    AudioPlayer.get().startPlayer();
                }

                //恢复音量
                AudioPlayer.get().getMediaPlayer().setVolume(1f,1f);

                isPausedbyFocusLossTransient=false;
                break;
            //永久失去焦点，如被其他播放器抢占
            case AudioManager.AUDIOFOCUS_LOSS:
                AudioPlayer.get().pausePlayer();
                break;
            //短暂失去焦点，如来电,微博视频
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                AudioPlayer.get().pausePlayer(false);
                isPausedbyFocusLossTransient=true;
                break;
            //瞬间失去焦点，如短信通知等
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                AudioPlayer.get().getMediaPlayer().setVolume(0.5f,0.5f);
                break;
        }
    }
}
