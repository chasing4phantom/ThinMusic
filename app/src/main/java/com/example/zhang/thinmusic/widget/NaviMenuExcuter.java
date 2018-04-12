package com.example.zhang.thinmusic.widget;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;


import com.example.zhang.thinmusic.R;
import com.example.zhang.thinmusic.activity.HomepageActivity;
import com.example.zhang.thinmusic.service.QuitTimer;
import com.example.zhang.thinmusic.utils.ToastUtils;

/**
 * Created by zhang on 2018/4/10.
 */

public class NaviMenuExcuter {
    private HomepageActivity activity;

    public NaviMenuExcuter(HomepageActivity activity) {
        this.activity = activity;
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_timer:
                timer();
                return true;
            case R.id.nav_about:
                 //startActivity(AboutActivity.class);
                return true;
        }
        return false;
    }
    private void startActivity(Class<?> classes){
        Intent intent = new Intent(activity,classes);
        activity.startActivity(intent);
    }

    private void timer(){
        new AlertDialog.Builder(activity)
                .setTitle("定时停止播放")
                .setItems(activity.getResources().getStringArray(R.array.timer_text),(dialog, which) ->{
                    int[] times = activity.getResources().getIntArray(R.array.timer_int);
                    StartTimer(times[which]);
                } )
                .show();
    }
    private void StartTimer(int minute){
        QuitTimer.get().start(minute *60 *1000);
        Log.d( "StartTimer: ",String.valueOf(minute));
        if(minute>0){
            ToastUtils.show(activity.getString(R.string.timer_set,String.valueOf(minute)));
        }else {
            ToastUtils.show("定时播放已取消");
        }

    }

}