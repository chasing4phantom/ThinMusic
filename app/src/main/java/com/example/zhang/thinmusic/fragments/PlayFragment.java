package com.example.zhang.thinmusic.fragments;


import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.zhang.thinmusic.PlayModeEnum;
import com.example.zhang.thinmusic.R;
import com.example.zhang.thinmusic.adapter.PlaypageAdapter;
import com.example.zhang.thinmusic.executor.SearchLrc;
import com.example.zhang.thinmusic.http.HttpCallback;
import com.example.zhang.thinmusic.http.HttpClient2Netease;
import com.example.zhang.thinmusic.model.Music;
import com.example.zhang.thinmusic.model.NeteaseLyric;
import com.example.zhang.thinmusic.service.OnPlayerListener;
import com.example.zhang.thinmusic.utils.AudioPlayer;
import com.example.zhang.thinmusic.utils.Bind;
import com.example.zhang.thinmusic.utils.CoverLoader;
import com.example.zhang.thinmusic.utils.FileUtils;
import com.example.zhang.thinmusic.utils.Preferences;
import com.example.zhang.thinmusic.utils.ScreenUtils;
import com.example.zhang.thinmusic.utils.SystemUtils;
import com.example.zhang.thinmusic.utils.ToastUtils;
import com.example.zhang.thinmusic.widget.AlbumCover;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.wcy.lrcview.LrcView;

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
    private LrcView mLrcView;


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
        Bundle bundle = getArguments();
        initSystemBar();
        initViewPager();
        initPlayMode();
        if(bundle!=null &&bundle.getString("path")!=null){
            onChange(AudioPlayer.get().handlepath(bundle.getString("path")));

        }else {
            onChangeImp1(AudioPlayer.get().getPlayMusic());
        }
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
        View lrcview = LayoutInflater.from(getContext()).inflate(R.layout.fragment_play_lrcview,null);

        mAlbumCoverView = coverView.findViewById(R.id.album_cover_view);
        mAlbumCoverView.initNeedle(AudioPlayer.get().isPlaying());

        mLrcView = lrcview.findViewById(R.id.lrc_view);
        //mLrcView.setOnPlayClickListener(this::onPlayClick);
        mLrcView.setDraggable(true,this::onPlayClick);
        ViewPagerContent = new ArrayList<>(2);
        ViewPagerContent.add(coverView);
        ViewPagerContent.add(lrcview);
        play_page.setAdapter(new PlaypageAdapter(ViewPagerContent));
    }
    private void initPlayMode(){
        int play_mode = Preferences.getPlayMode();
        mode.setImageLevel(play_mode);
    }

    @Override
    public void onChange(Music music){

        onChangeImp1(music);}

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
        }//拖动进度条更新进度

        if(mLrcView.hasLrc()){
            mLrcView.updateTime(progress);
        }//歌词滚动
    }

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
    public void onPageSelected(int position){}
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

                if(mLrcView.hasLrc()){
                    mLrcView.updateTime(progress);
                }

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
        Log.d( "onChangeImpl: ",String.valueOf(music.getDuration()));
        setCoverAndBg(music);
        setLrc(music);
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
                ToastUtils.show("切换为随机播放");
                break;
            case SHUFFLE:
                mode = PlayModeEnum.SINGLE;
                ToastUtils.show("切换为单曲循环");
                break;
            case SINGLE:
                mode = PlayModeEnum.LOOP;
                ToastUtils.show("切换为列表循环");
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

    private void setLrc(final Music music){
        if(music.getType()==Music.Type.LOCAL){
            String lrcpath= FileUtils.getLrcFilePath(music);
            if(!TextUtils.isEmpty(lrcpath)){
                loadLrc(lrcpath);
            }else {
                new SearchLrc(music.getArtist(),music.getTitle()){
                    @Override
                    public void onPrepare(){
                        play_page.setTag(music);

                        loadLrc("");
                        setLrcLabel("正在搜索歌词：");

                    }
                    @Override
                    public void onExecuteSuccess(@NonNull String lrcPath){
                        if(play_page.getTag()!=music){
                            return;
                        }
                        //清除tag
                        play_page.setTag(null);

                        loadLrc(lrcPath);
                        setLrcLabel("暂无歌词");
                    }
                    @Override
                    public void onExecuteFail(Exception e){
                        if(play_page.getTag() !=music){
                            return;
                        }

                        //清除tag
                        play_page.setTag(null);
                        setLrcLabel("暂无歌词");
                    }
                }.execute();
            }
        }
        else if(music.getType() == Music.Type.ONLINE){
            String lrcPath= FileUtils.getLrcDir()+FileUtils.getLrcFileName(music.getArtist(),music.getTitle());
            //Log.d( "setLrc: ",lrcPath);
            loadLrc(lrcPath);
        }else if(music.getType()==Music.Type.NETEASE){
            HttpClient2Netease.getNeteaseMusicLyric(Long.toString(music.getSongId()), new HttpCallback<NeteaseLyric>() {
                @Override
                public void onSuccess(NeteaseLyric neteaseLyric) {
                    String lrc = neteaseLyric.getLrc().getLyric();
                    //Log.d("setLrc", "onSuccess: "+lrc);
                    load_Lrc(lrc);
                }

                @Override
                public void onFail(Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void loadLrc(String path){
        File file =new File(path);
        mLrcView.loadLrc(file);
    }
    private void load_Lrc(String lrc){
        mLrcView.loadLrc(lrc);
    }

    private void setLrcLabel(String label){
        mLrcView.setLabel(label);
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

