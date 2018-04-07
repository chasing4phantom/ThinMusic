package com.example.zhang.thinmusic.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhang.thinmusic.R;
import com.example.zhang.thinmusic.model.Music;
import com.example.zhang.thinmusic.utils.AudioPlayer;
import com.example.zhang.thinmusic.utils.Bind;
import com.example.zhang.thinmusic.utils.CoverLoader;
import com.example.zhang.thinmusic.utils.FileUtils;
import com.example.zhang.thinmusic.utils.ViewBinder;

import java.util.List;

/**
 * 本地音乐列表适配器
 * Created by zhang on 2018/3/22.
 */

public class PlaylistAdapter extends BaseAdapter{
    private List<Music> musicList;
    private OnMoreClickListener listener;
    private boolean isPlaylist;
    public PlaylistAdapter(List<Music> musicList){this.musicList = musicList;}

    public void setIsPlaylist(boolean isPlaylist){this.isPlaylist = isPlaylist;}

    public void setOnMoreClickListener(OnMoreClickListener listener){this.listener = listener;}
    @Override
    public int getCount(){return musicList.size();}

    @Override
    public Object getItem(int postion){return musicList.get(postion);}

    @Override
    public long getItemId(int position){return  position;}

    @Override
    public View getView(final int position, View convertView, ViewGroup parent ){
        ViewHolder holder;
        /*优化listview加载*/
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.musiclist_view_holder,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);//将convertview存储在holder中
        }else {
            holder = (ViewHolder) convertView.getTag();//重新获取viewholder
        }
        holder.playing.setVisibility((isPlaylist && position == AudioPlayer.get().getPlayPosition()) ? View.VISIBLE:View.INVISIBLE);
        Music music = musicList.get(position);

       Bitmap cover = CoverLoader.get().loadThumb(music);
        holder.Cover.setImageBitmap(cover);
        holder.Title.setText(music.getTitle());
        String artist = FileUtils.getArtistAndAlbum(music.getArtist(),music.getAlbum());
        holder.Artist.setText(artist);
        holder.More.setOnClickListener(v ->{
            if(listener != null){
                listener.onMoreClick(position);
            }
        });
        holder.Divider.setVisibility(isShowDivider(position) ? View.VISIBLE : View.GONE);
        return convertView;

    }
    private boolean isShowDivider(int position){return position!=musicList.size() -1;}

    private static class ViewHolder{
        @Bind(R.id.playing)
        private  View playing;
        @Bind(R.id.cover)
        private ImageView Cover;
       /* @Bind(R.id.number)
        private TextView Number;*/
        @Bind(R.id.title)
        private TextView Title;
        @Bind(R.id.artist)
        private TextView Artist;
        @Bind(R.id.more)
        private ImageView More;
        @Bind(R.id.divider)
        private View Divider;

        public ViewHolder(View view){
            ViewBinder.bind(this,view);}
    }
}
