package com.example.zhang.thinmusic;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Objects;

/**
 * Created by zhang on 2018/2/13.
 */

public class MusicPlayerActivity extends Activity implements View.OnClickListener {
    private Button playorpause_btn;
    private ImageView imageView;

    public String url;
    private MusicPlayService.MusicPlayerBinder mBinder;

    private Handler mHandler = new Handler();
    private TextView playing_time,max_time;
    private SeekBar seekBar;
    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");

    Intent MediaServiceIntent;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);
        findView();
        bindButton();
        Intent intent = getIntent();
        url = intent.getStringExtra("url");

        MediaServiceIntent = new Intent(this,MusicPlayService.class);
        bindService(MediaServiceIntent, mServiceConnection,BIND_AUTO_CREATE);
    }
    public void findView(){
        playorpause_btn=(Button)findViewById(R.id.play_pause);

        playing_time = (TextView)findViewById(R.id.playing_time);
        max_time = (TextView)findViewById(R.id.song_time);
        seekBar = (SeekBar)findViewById(R.id.seekbar);
        imageView=(ImageView)findViewById(R.id.imageview);
    }
    private void bindButton(){
        playorpause_btn.setOnClickListener(this);

    }
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBinder = (MusicPlayService.MusicPlayerBinder) service;
            mBinder.getUrl(url);
            mBinder.start();
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
    public void onClick(View view){
        switch (view.getId()){
            case R.id.play_pause:
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
