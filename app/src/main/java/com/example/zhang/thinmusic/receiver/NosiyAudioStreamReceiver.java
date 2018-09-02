package com.example.zhang.thinmusic.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.example.zhang.thinmusic.utils.AudioPlayer;

/*拔出耳机自动暂停*/
public class NosiyAudioStreamReceiver extends BroadcastReceiver {

    @Override
    public  void onReceive(Context context, Intent intent){
        AudioPlayer.get().playPause();
    }
}
