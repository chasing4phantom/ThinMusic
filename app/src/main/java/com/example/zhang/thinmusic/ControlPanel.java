package com.example.zhang.thinmusic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.zhang.thinmusic.activity.PlaylistActivity;
import com.example.zhang.thinmusic.model.Music;
import com.example.zhang.thinmusic.service.OnPlayerListener;
import com.example.zhang.thinmusic.utils.AudioPlayer;
import com.example.zhang.thinmusic.utils.Bind;
import com.example.zhang.thinmusic.utils.CoverLoader;
import com.example.zhang.thinmusic.utils.ViewBinder;

import org.w3c.dom.Text;

/**
 * 播放界面底部playbar
 * Created by zhang on 2018/3/27.
 */

public class ControlPanel implements View.OnClickListener,OnPlayerListener {
    @Bind(R.id.play_bar_cover)
    private ImageView playbarcover;
    @Bind(R.id.play_bar_title)
    private TextView playbartitle;
    @Bind(R.id.play_bar_artist)
    private TextView playbarartist;
    @Bind(R.id.play_bar_next)
    private ImageView playbarnext;
    @Bind(R.id.play_bar_play)
    private ImageView playbarplay;
    @Bind(R.id.play_bar_playlist)
    private ImageView playbarplaylist;
    @Bind(R.id.progressbar_play_bar)
    private ProgressBar progressBar;

    public ControlPanel(View view){
        ViewBinder.bind(this,view);
        playbarplay.setOnClickListener(this);
        playbarnext.setOnClickListener(this);
        playbarplaylist.setOnClickListener(this);
        onChange(AudioPlayer.get().getPlayMusic());
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.play_bar_play:
                AudioPlayer.get().playPause();
                break;
            case R.id.play_bar_next:
                AudioPlayer.get().next();
                break;
            case R.id.play_bar_playlist:
                Context context = playbarplaylist.getContext();
                Intent intent = new Intent(context, PlaylistActivity.class);
                context.startActivity(intent);
                break;
        }
    }

    @Override
    public void onChange(Music music){
        if(music == null){
            return;
        }
        Bitmap cover = CoverLoader.get().loadThumb(music);
        playbarcover.setImageBitmap(cover);
        playbarartist.setText(music.getArtist());
        playbartitle.setText(music.getTitle());
        playbarplay.setSelected(AudioPlayer.get().isPlaying() || AudioPlayer.get().isPreparing());
        progressBar.setMax((int)music.getDuration());
        progressBar.setProgress((int)AudioPlayer.get().getAudioPosition());
    }

    @Override
    public void onPlayerStart(){playbarplay.setSelected(true);}

    @Override
    public void onPlayerPause(){playbarplay.setSelected(false);}

    @Override
    public void onPublish(int progress){progressBar.setProgress(progress);}

    @Override
    public  void onBufferingUpdate(int percent){}

}
