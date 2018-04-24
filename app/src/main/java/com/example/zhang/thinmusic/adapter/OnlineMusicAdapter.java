package com.example.zhang.thinmusic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhang.thinmusic.R;
import com.example.zhang.thinmusic.model.OnLineMusic;
import com.example.zhang.thinmusic.utils.Bind;
import com.example.zhang.thinmusic.utils.FileUtils;
import com.example.zhang.thinmusic.utils.ViewBinder;

import org.w3c.dom.Text;

import java.util.List;

/**在线音乐列表适配器
 * Created by zhang on 2018/4/18.
 */

public class OnlineMusicAdapter extends BaseAdapter {
    private List<OnLineMusic> mData;
    private OnMoreClickListener moreClickListener;

    public OnlineMusicAdapter(List<OnLineMusic> mData){this.mData = mData;}

    @Override
    public int getCount(){return mData.size();}

    @Override
    public Object getItem(int position){return  mData.get(position);}

    @Override
    public long getItemId(int position){return  position;}

    @Override
    public View getView(final  int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        if(convertView ==null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_music,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        OnLineMusic onLineMusic = mData.get(position);
        Glide.with(parent.getContext())
                .load(onLineMusic.getPic_small())
                .placeholder(R.drawable.default_cover)
                .error(R.drawable.default_cover)
                .into(holder.Cover);
        holder.Title.setText(onLineMusic.getTitle());
        String artist = FileUtils.getArtistAndAlbum(onLineMusic.getArtisit_name(),onLineMusic.getAlbum_title());
        holder.Artist.setText(artist);
        holder.More.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                moreClickListener.onMoreClick(position);
            }
        });
        holder.Divider.setVisibility(isShowDivider(position)? View.VISIBLE : View.GONE);
        return convertView;
    }

    private boolean isShowDivider(int position){return  position != mData.size()-1;}

    public void setMoreClickListener(OnMoreClickListener listener){
        moreClickListener = listener;
    }
    private static class ViewHolder{
        @Bind(R.id.iv_cover)
        private ImageView Cover;
        @Bind(R.id.tv_title)
        private TextView Title;
        @Bind(R.id.tv_artist)
        private TextView Artist;
        @Bind(R.id.iv_more)
        private ImageView More;
        @Bind(R.id.v_divider)
        private View Divider;

        public ViewHolder(View view){
            ViewBinder.bind(this,view);}
    }

}
