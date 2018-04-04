package com.example.zhang.thinmusic;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.zhang.thinmusic.utils.ScreenUtils;

import java.text.SimpleDateFormat;
import java.util.Objects;



/**
 * Created by zhang on 2018/2/13.
 */

public class MusicPlayerActivity extends Activity implements View.OnClickListener {
    private ImageView playorpause_btn,play_prev,play_next;
    private ImageView imageView;

    public String url,title1,artist1;
    private MusicPlayService.MusicPlayerBinder mBinder;

    private Handler mHandler = new Handler();
    private TextView playing_time,max_time,title,artist;
    private SeekBar seekBar;
    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");

    private int flag = 4;
    private LinearLayout llContent;
    Intent MediaServiceIntent;

    @Override
    public void onCreate(Bundle savedInstanceState){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        }
        super.onCreate(savedInstanceState);
       /* initSystemBar();*/
        setContentView(R.layout.fragment_playing);
        findView();
        setListener();
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        title1 = intent.getStringExtra("title");
        artist1 = intent.getStringExtra("artist");
        MediaServiceIntent = new Intent(this,MusicPlayService.class);
        bindService(MediaServiceIntent, mServiceConnection,BIND_AUTO_CREATE);
    }
    public void findView(){
        title = (TextView)findViewById(R.id.title);
        artist = (TextView)findViewById(R.id.artist);
        playorpause_btn = (ImageView)findViewById(R.id.play);
        play_prev = (ImageView)findViewById(R.id.play_prev);
        play_next = (ImageView)findViewById(R.id.play_next);
        playing_time = (TextView)findViewById(R.id.playing_time);
        max_time = (TextView)findViewById(R.id.song_time);
        seekBar = (SeekBar)findViewById(R.id.seekbar_progress);
        imageView=(ImageView)findViewById(R.id.imageview);
        llContent = (LinearLayout)findViewById(R.id.content);

    }
    private void setListener(){
        playorpause_btn.setOnClickListener(this);
        play_prev.setOnClickListener(this);
        play_next.setOnClickListener(this);

    }
   /* private void initSystemBar(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            int top = ScreenUtils.getStatusBarHeight();
            llContent.setPadding(0,top,0,0);
        }
    }*/
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBinder = (MusicPlayService.MusicPlayerBinder) service;
            mBinder.getUrl(url);
            mBinder.start();
            title.setText(title1);
            artist.setText(artist1);
            seekBar.setMax(mBinder.getProgress());
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                    //判断操作是否来自用户
                    if(fromUser){
                        mBinder.seekToPosition(seekBar.getProgress());
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            mHandler.post(mRunnable);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
    @Override
    /*public void onClick(View view){
        switch (view.getId()){
            case R.id.play:
            {
                if(Objects.equals(playorpause_btn.getText().toString(), "暂停"))
                {
                    mBinder.pause();
                    playorpause_btn.setText("播放");
                }else
                {
                    mBinder.play();
                    playorpause_btn.setText("暂停");
                }
            }
            break;
        }
    }*/
    public void onClick(View view){
        switch (view.getId()){
            case R.id.play:
                if(flag == Constant.isPlaying) {
                    mBinder.play();
                    flag = Constant.isPause;
                }else
                    if(flag == Constant.isPause) {
                    mBinder.pause();
                    flag = Constant.isPlaying;
                    }
                break;
        }
    }

    @Override
    public void onDestroy(){

        mHandler.removeCallbacks(mRunnable);//释放更新ui的runnable
        mBinder.closeMedia();//释放播放器资源
        unbindService(mServiceConnection);//释放链接
        super.onDestroy();
    }
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            seekBar.setProgress(mBinder.getPlayPosition());
            playing_time.setText(time.format(mBinder.getPlayPosition()));
            max_time.setText(time.format(mBinder.getProgress()));
            mHandler.postDelayed(mRunnable,1000);
        }
    };//更新进度条ui


}
