package com.example.zhang.thinmusic;

import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.example.zhang.thinmusic.Application.AppCache;
import com.example.zhang.thinmusic.model.Music;
import com.example.zhang.thinmusic.service.PlayService;
import com.example.zhang.thinmusic.utils.AudioPlayer;

/**
 * Created by zhang on 2018/4/2.
 */

public class MediaSessionManager {
    private static final String TAG = "MediaSessionManager";
    private static final long MEDIA_SESSION_ACTIONS = PlaybackStateCompat.ACTION_PLAY
            | PlaybackStateCompat.ACTION_PAUSE
            | PlaybackStateCompat.ACTION_PLAY_PAUSE
            |PlaybackStateCompat.ACTION_SKIP_TO_NEXT
            |PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
            | PlaybackStateCompat.ACTION_STOP
            |PlaybackStateCompat.ACTION_SEEK_TO;

    private PlayService playService;
    private MediaSessionCompat mediaSession;

    public static MediaSessionManager get(){return  SingletonHolder.instance;}

    private static class SingletonHolder{
        private static MediaSessionManager instance = new MediaSessionManager();
    }

    private MediaSessionManager(){

    }

    public void init(PlayService playService){
        this.playService = playService;
        setupMediaSession();
    }

    private void setupMediaSession(){
        mediaSession = new MediaSessionCompat(playService,TAG);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setCallback(callback);
        mediaSession.setActive(true);
    }
  /*  配置完成后，播放状态及音乐信息的更新*/
    public void updatePlaybackState() {
        int state = (AudioPlayer.get().isPlaying() || AudioPlayer.get().isPreparing()) ? PlaybackStateCompat.STATE_PLAYING : PlaybackStateCompat.STATE_PAUSED;
        mediaSession.setPlaybackState(
                new PlaybackStateCompat.Builder()
                        .setActions(MEDIA_SESSION_ACTIONS)
                        .setState(state, AudioPlayer.get().getAudioPosition(), 1)
                        .build());
    }
/*更新正在播放的音乐信息，切换歌曲时调用*/
    public void updateMetaData(Music music){
        if(music == null){
            mediaSession.setMetadata(null);
            return;
        }

        MediaMetadataCompat.Builder metaData = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE,music.getTitle())
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST,music.getArtist())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM,music.getAlbum())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST,music.getArtist())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION,music.getDuration());
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            metaData.putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, AppCache.get().getLocalMusicList().size());

        }
            mediaSession.setMetadata(metaData.build());
        }
        /*耳机多媒体按键监听*/
        private MediaSessionCompat.Callback callback = new MediaSessionCompat.Callback(){
            @Override
            public void onPlay(){AudioPlayer.get().playPause();}

            @Override
            public void onPause(){AudioPlayer.get().playPause();}

            @Override
            public void onSkipToNext(){AudioPlayer.get().next();}

            @Override
            public void onSkipToPrevious(){AudioPlayer.get().prev();}

            @Override
            public void onStop(){AudioPlayer.get().stopPlayer();}

            @Override
            public void onSeekTo(long position){AudioPlayer.get().seekTo((int) position);}
        };
}
