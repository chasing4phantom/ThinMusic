package com.example.zhang.thinmusic.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;


import com.example.zhang.thinmusic.Application.Notifier;
import com.example.zhang.thinmusic.constants.Action;
import com.example.zhang.thinmusic.utils.AudioPlayer;
import com.example.zhang.thinmusic.MediaSessionManager;

/**
 * 音乐后台服务
 * Created by zhang on 2018/3/20.
 */

public class PlayService extends Service {
    public class PlayBinder extends Binder{
        public PlayService getService(){return  PlayService.this;}
    }

    @Override
    public void onCreate(){
      super.onCreate();
      AudioPlayer.get().init(this);
      MediaSessionManager.get().init(this);
      QuitTimer.get().init(this);
      Notifier.get().init(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent){return  new PlayBinder();}

    public  static void startCommand(Context context,String action){
        Intent intent = new Intent(context,PlayService.class);
        intent.setAction(action);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId){
        if(intent != null && intent.getAction()!= null){
            switch (intent.getAction()){
                case Action.ACTION_STOP:
                    stop();
                    break;
            }
        }
        return START_NOT_STICKY;
    }

    private void stop(){
        AudioPlayer.get().stopPlayer();
        Notifier.get().cancelAll();
        QuitTimer.get().stop();
    }
}
