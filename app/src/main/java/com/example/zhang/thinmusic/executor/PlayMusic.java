package com.example.zhang.thinmusic.executor;

import android.app.Activity;

import com.example.zhang.thinmusic.model.Music;
import com.example.zhang.thinmusic.utils.Preferences;

/**
 * Created by zhang on 2018/4/23.
 */

public abstract class PlayMusic implements IExecutor<Music> {
    private Activity activity;
    protected Music music;
    private int TotalStep;
    protected int Counter = 0;

    public PlayMusic(Activity activity,int totalStep){
        this.activity =activity;
        TotalStep = totalStep;
    }

    @Override
    public void execute(){checkNetwork();}

    private void checkNetwork(){
        getPlayInfoWrapper();
    }

    private void getPlayInfoWrapper(){
        onPrepare();
        getPlayInfo();
    }
    protected  abstract void getPlayInfo();

    protected void checkCounter(){
        Counter++;
        if(Counter == TotalStep){
            onExecuteSuccess(music);
        }
    }
}
