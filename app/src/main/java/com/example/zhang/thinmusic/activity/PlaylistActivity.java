package com.example.zhang.thinmusic.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.zhang.thinmusic.R;
import com.example.zhang.thinmusic.adapter.OnMoreClickListener;
import com.example.zhang.thinmusic.adapter.PlaylistAdapter;
import com.example.zhang.thinmusic.adapter.onClick;
import com.example.zhang.thinmusic.model.Music;
import com.example.zhang.thinmusic.service.OnPlayerListener;
import com.example.zhang.thinmusic.utils.AudioPlayer;
import com.example.zhang.thinmusic.utils.Bind;

/**播放列表
 * Created by zhang on 2018/3/22.
 */

public class PlaylistActivity extends BaseActivity implements onClick,OnMoreClickListener,OnPlayerListener {
    @Bind(R.id.playlist)
    private RecyclerView Playlist;

    private PlaylistAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_playlist);
    }

    @Override
    protected void onServiceBound(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        Playlist.setLayoutManager(layoutManager);
        adapter = new PlaylistAdapter(AudioPlayer.get().getMusicList());
        adapter.setIsPlaylist(true);
        adapter.setOnClickListener(this);
        adapter.setOnMoreClickListener(this);

        Playlist.setAdapter(adapter);
        //Playlist.setOnItemClickListener(this);
        AudioPlayer.get().addOnPlayListener(this);
    }

/*    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id){
        AudioPlayer.get().play(position);//触摸监听，点击后直接播放
    }*/

    @Override
    public void onClick(int position){
        AudioPlayer.get().play(position);
    }
    @Override
    public void onMoreClick(int position){
        String[] items = new String[]{"移除"};
        Music music = AudioPlayer.get().getMusicList().get(position);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(music.getTitle());
        dialog.setItems(items,(dialog1,which)->{
            AudioPlayer.get().delete(position);
            adapter.notifyDataSetChanged();//刷新每个item
        });
        dialog.show();
    }

    @Override
    public void onChange(Music music){adapter.notifyDataSetChanged();}

    @Override
    public void onPlayerStart(){

    }
    @Override
    public void onPlayerPause(){

    }
    @Override
    public void onPublish(int progress){

    }

    @Override
    public void onBufferingUpdate(int position){

    }
    @Override
    protected void onDestroy(){
        AudioPlayer.get().removeOnPlayListener(this);
        super.onDestroy();
    }
}

