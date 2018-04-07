package com.example.zhang.thinmusic.fragments;


import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.audiofx.AudioEffect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.zhang.thinmusic.MusicPlayService;
import com.example.zhang.thinmusic.PlayModeEnum;
import com.example.zhang.thinmusic.R;
import com.example.zhang.thinmusic.adapter.PlaypageAdapter;
import com.example.zhang.thinmusic.model.Music;
import com.example.zhang.thinmusic.service.OnPlayerListener;
import com.example.zhang.thinmusic.utils.AudioPlayer;
import com.example.zhang.thinmusic.utils.Bind;
import com.example.zhang.thinmusic.utils.CoverLoader;
import com.example.zhang.thinmusic.utils.Preferences;
import com.example.zhang.thinmusic.utils.ScreenUtils;
import com.example.zhang.thinmusic.utils.SystemUtils;
import com.example.zhang.thinmusic.widget.AlbumCover;
import com.example.zhang.thinmusic.widget.IndicatorLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.zhang.thinmusic.utils.MediaUtil.formatTime;

/**
 * Created by zhang on 2018/3/26.
 */

public class PlayFragment extends BaseFragment implements View.OnClickListener,
        ViewPager.OnPageChangeListener,SeekBar.OnSeekBarChangeListener,OnPlayerListener{
    @Bind(R.id.content)
    private LinearLayout llContent;
    @Bind(R.id.play_page_bg)
    private ImageView PlayingBackground;
    @Bind(R.id.btn_back)
    private ImageView back;
    @Bind(R.id.title)
    private TextView title;
    @Bind(R.id.artist)
    private TextView artist;
    @Bind(R.id.play_page)
    private ViewPager play_page;
    @Bind(R.id.il_indicator)
    private IndicatorLayout ilIndicator;
    @Bind(R.id.seekbar_progress)
    private SeekBar seekBar;
    @Bind(R.id.playing_time)
    private TextView playing_time;
    @Bind(R.id.song_time)
    private TextView max_time;
    @Bind(R.id.play_mode)
    private ImageView mode;
    @Bind(R.id.play)
    private ImageView playorpause_btn;
    @Bind(R.id.play_prev)
    private ImageView play_prev;
    @Bind(R.id.play_next)
    private ImageView  play_next;
    private AlbumCover mAlbumCoverView;
    /*private LrcView mLrcViewSingle;
    private LrcView mLrcViewFull;*/

    private AudioManager audioManager;
    private List<View>  ViewPagerContent;
    private int lastProgress;
    private boolean isdraggingprogress;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_playing,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        initSystemBar();
        initViewPager();
        ilIndicator.create(ViewPagerContent.size());
        initPlayMode();
        onChangeImp1(AudioPlayer.get().getPlayMusic());
        AudioPlayer.get().addOnPlayListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    protected void setListener(){
        back.setOnClickListener(this);
        play_prev.setOnClickListener(this);
        play_next.setOnClickListener(this);
        playorpause_btn.setOnClickListener(this);
        mode.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        play_page.addOnPageChangeListener(this);
    }

    private void initSystemBar(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            int top = ScreenUtils.getStatusBarHeight();
            llContent.setPadding(0,top,0,0);
        }
    }

    private void initViewPager(){
        View coverView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_play_pagecover, null);

        mAlbumCoverView = coverView.findViewById(R.id.album_cover_view);
        mAlbumCoverView.initNeedle(AudioPlayer.get().isPlaying());

        ViewPagerContent = new ArrayList<>(1);
        ViewPagerContent.add(coverView);
        play_page.setAdapter(new PlaypageAdapter(ViewPagerContent));
    }
    private void initPlayMode(){
        int play_mode = Preferences.getPlayMode();
        mode.setImageLevel(play_mode);
    }
    @Override
    public void onChange(Music music){onChangeImp1(music);}

    @Override
    public void onPlayerStart(){
        playorpause_btn.setSelected(true);
        mAlbumCoverView.start();
    }
    @Override
    public void onPlayerPause(){
        playorpause_btn.setSelected(false);
        mAlbumCoverView.pause();
    }

    @Override
    public void onPublish(int progress){
        if(!isdraggingprogress){
            seekBar.setProgress(progress);
        }

    }//拖动进度条更新进度

    @Override
    public void onBufferingUpdate(int percent){
        seekBar.setSecondaryProgress(seekBar.getMax() * 100 / percent);
    }
    @Override
    public void  onClick(View v){
        switch (v.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.play_mode:
                switchPlayMode();
                break;

            case R.id.play_prev:
                prev();
                break;
            case R.id.play_next:
                next();
                break;
            case R.id.play:
                play();
                break;
        }
    }

    @Override
    public void onPageScrolled(int position,float positionOffset,int postitionOffsetPixels ){

    }

    @Override
    public void onPageSelected(int position){ilIndicator.setCurrent(position);}
    @Override
    public void onPageScrollStateChanged(int state){}

    @Override
    public void onProgressChanged(SeekBar seekBar1,int progress,boolean fromUser ){
        if(seekBar1 == seekBar){
            if(Math.abs(progress - lastProgress) >= DateUtils.SECOND_IN_MILLIS){
                playing_time.setText(formatTime(progress));
                lastProgress = progress;
            }
        }
    }//判断操作是否来自用户

    @Override
    public void onStartTrackingTouch(SeekBar seekBar1){
        if(seekBar1 == seekBar){
            isdraggingprogress = true;
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar1){
        if(seekBar1 == seekBar){
            isdraggingprogress = false;
            if(AudioPlayer.get().isPlaying() || AudioPlayer.get().isPausing()){
                int progress = seekBar1.getProgress();
                AudioPlayer.get().seekTo(progress);


            }
        }
    }
    /*@Override*/
    public boolean onPlayClick(long time){
        if(AudioPlayer.get().isPlaying() || AudioPlayer.get().isPausing()){
            AudioPlayer.get().seekTo((int)time);
            if(AudioPlayer.get().isPausing()){
                AudioPlayer.get().playPause();
            }
            return true;
        }
        return false;
    }
/*设置歌曲信息*/
    private void onChangeImp1(Music music){
        if(music == null){
            return;
        }
        title.setText(music.getTitle());
        artist.setText(music.getArtist());
        seekBar.setProgress((int) AudioPlayer.get().getAudioPosition());
        seekBar.setSecondaryProgress(0);
        seekBar.setMax((int)music.getDuration());
        lastProgress = 0;
        playing_time.setText("00:00");
        max_time.setText(formatTime(music.getDuration()));
        setCoverAndBg(music);
        //setLrc();
        if(AudioPlayer.get().isPlaying() || AudioPlayer.get().isPreparing()){
            playorpause_btn.setSelected(true);
            mAlbumCoverView.start();
        }else{
            playorpause_btn.setSelected(false);
            mAlbumCoverView.pause();
        }
    }

    private void play(){AudioPlayer.get().playPause();}

    private void next(){AudioPlayer.get().next();}

    private void prev(){AudioPlayer.get().prev();}

    /*切换播放模式*/
    private void switchPlayMode(){
        PlayModeEnum mode = PlayModeEnum.valueOf(Preferences.getPlayMode());
        switch (mode){
            case LOOP:
                mode = PlayModeEnum.SHUFFLE;
                break;
            case SHUFFLE:
                mode = PlayModeEnum.SINGLE;
                break;
            case SINGLE:
                mode = PlayModeEnum.LOOP;
                break;
        }
        Preferences.savePlayMode(mode.value());
        initPlayMode();
    }

    private void onBackPressed(){
        getActivity().onBackPressed();
        back.setEnabled(false);
        handler.postDelayed(()->back.setEnabled(true),300);
    }

    private void setCoverAndBg(Music music){
        mAlbumCoverView.setCoverBitmap(CoverLoader.get().loadRound(music));
        PlayingBackground.setImageBitmap(CoverLoader.get().loadBlur(music));

    }
    private String formatTime(long time){
        return SystemUtils.formatTime("mm:ss",time);
    }
    @Override
    public void onDestroy(){
        AudioPlayer.get().removeOnPlayListener(this);
        super.onDestroy();
    }
}

