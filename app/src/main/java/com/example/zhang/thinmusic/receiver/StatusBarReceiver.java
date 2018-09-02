package com.example.zhang.thinmusic.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.example.zhang.thinmusic.utils.AudioPlayer;

/**
 * Created by zhang on 2018/4/3.
 */

public class StatusBarReceiver extends BroadcastReceiver {
    public static final String ACTION_STATUS_BAR = "com.example.zhang.thinmusic.STATUS_BAR_ACTIONS";
    public static final String EXTRA = "extra";
    public static final String EXTRA_NEXT = "next";
    public static final String EXTRA_PLAY_PAUSE = "play_pause";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || TextUtils.isEmpty(intent.getAction())) {
            Log.d("fuck!", "onReceive: 77777777777777777777");
            return;
        }

        String extra = intent.getStringExtra(EXTRA);
        Log.d("extra", "onReceive: "+ extra);
        if (TextUtils.equals(extra, EXTRA_NEXT)) {
            AudioPlayer.get().next();
        } else if (TextUtils.equals(extra, EXTRA_PLAY_PAUSE)) {
            AudioPlayer.get().playPause();
        }
    }
}
