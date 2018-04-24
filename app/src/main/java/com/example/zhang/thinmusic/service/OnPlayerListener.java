package com.example.zhang.thinmusic.service;

import com.example.zhang.thinmusic.model.Music;

/**
 * 播放进度监听器
 * Created by zhang on 2018/3/19.
 */

public interface OnPlayerListener {
    void onChange(Music music);
    void onPlayerStart();
    void onPlayerPause();
    void onPublish(int progress);
    void onBufferingUpdate(int percent);
}
