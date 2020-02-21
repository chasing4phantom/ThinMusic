package com.example.zhang.thinmusic.service;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateUtils;


import com.example.zhang.thinmusic.application.AppCache;
import com.example.zhang.thinmusic.constants.Action;


/**
 * Created by zhang on 2018/4/10.
 */

public class QuitTimer {
    private Context context;
    private Handler handler;
    private long remaintime;
    private OnTimerListener listener;

    public interface OnTimerListener{
        void onTimer(long remain);
    }

    public static QuitTimer get(){
    return SingletonHolder.Instance;}

    private static class SingletonHolder{
        private static final QuitTimer Instance = new QuitTimer();
    }
    private QuitTimer(){}

    public void init(Context context){
        this.context = context.getApplicationContext();
        this.handler = new Handler(Looper.getMainLooper());
    }

    public void setOnTimerListener(OnTimerListener listener){
        this.listener = listener;
    }
    public void start(long millsecond){
        stop();
        if(millsecond>0){
            remaintime = millsecond + DateUtils.SECOND_IN_MILLIS;
            handler.post(quitRunnable);
        }
        else{
            remaintime = 0;
            if(listener !=null){
                listener.onTimer(remaintime);
            }
        }
    }
    public void stop(){handler.removeCallbacks(quitRunnable);}

    private Runnable quitRunnable =new Runnable() {
        @Override
        public void run() {
                remaintime -=DateUtils.SECOND_IN_MILLIS;
                if(remaintime >0){
                    if(listener !=null){
                        listener.onTimer(remaintime);
                    }
                    handler.postDelayed(this,DateUtils.SECOND_IN_MILLIS);
                }else{
                    AppCache.get().clearStack();
                    PlayService.startCommand(context, Action.ACTION_STOP);
                }
        }
    };

}
