package com.example.zhang.thinmusic.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhang.thinmusic.R;
import com.example.zhang.thinmusic.model.Music;
import com.example.zhang.thinmusic.utils.AudioPlayer;
import com.example.zhang.thinmusic.utils.Bind;
import com.example.zhang.thinmusic.utils.CoverLoader;
import com.example.zhang.thinmusic.utils.FileUtils;
import com.example.zhang.thinmusic.utils.ToastUtils;
import com.example.zhang.thinmusic.utils.ViewBinder;

import java.util.List;

/**
 * 本地音乐列表适配器
 * Created by zhang on 2018/3/22.
 */

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder>{
    private List<Music> musicList;
    private onClick viewlistener;
    private OnMoreClickListener listener;
    private boolean isPlaylist;
    public PlaylistAdapter(List<Music> musicList){this.musicList = musicList;}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int ViewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.musiclist_view_holder,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.musicView.setOnClickListener(v -> {

          if(viewlistener !=null){
              int position = holder.getAdapterPosition();
              viewlistener.onClick(position);
          }
        });
        holder.More.setOnClickListener(v -> {
            if(listener != null){
                int position = holder.getAdapterPosition();
                listener.onMoreClick(position);
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(PlaylistAdapter.ViewHolder holder, int position){
        holder.playing.setVisibility((isPlaylist && position == AudioPlayer.get().getPlayPosition()) ? View.VISIBLE:View.INVISIBLE);
        Music music =musicList.get(position);
        Bitmap cover = CoverLoader.get().loadThumb(music);
        holder.Cover.setImageBitmap(cover);
        holder.Title.setText(music.getTitle());
        String artist = FileUtils.getArtistAndAlbum(music.getArtist(),music.getAlbum());
        holder.Artist.setText(artist);
        holder.Divider.setVisibility(isShowDivider(position) ? View.VISIBLE : View.GONE);
    }
    @Override
    public int getItemCount(){
        return musicList.size();
    }
    private boolean isShowDivider(int position){return position!=musicList.size() -1;}

    public void setIsPlaylist(boolean isPlaylist) {
        this.isPlaylist = isPlaylist;
    }

    public void setOnClickListener(onClick listener){
        this.viewlistener =listener;
    }
    public void setOnMoreClickListener(OnMoreClickListener listener) {
        this.listener = listener;
    }
    public void  setonMoreClickListener(OnMoreClickListener listener){ this.listener = listener;}
    static class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.playing)
        private  View playing;
        @Bind(R.id.cover)
        private ImageView Cover;
        @Bind(R.id.title)
        private TextView Title;
        @Bind(R.id.artist)
        private TextView Artist;
        @Bind(R.id.more)
        private ImageView More;
        @Bind(R.id.divider)
        private View Divider;
        View musicView;

        public ViewHolder(View view){
            super(view);
            musicView =view;
            ViewBinder.bind(this,view);}
    }
}
