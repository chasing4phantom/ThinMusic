package com.example.zhang.thinmusic.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.zhang.thinmusic.R;
import com.example.zhang.thinmusic.http.HttpCallback;
import com.example.zhang.thinmusic.http.HttpClient;
import com.example.zhang.thinmusic.model.ListInfo;
import com.example.zhang.thinmusic.model.OnLineMusic;
import com.example.zhang.thinmusic.model.OnLineMusicList;
import com.example.zhang.thinmusic.utils.Bind;
import com.example.zhang.thinmusic.utils.ViewBinder;

import java.util.List;

/**网络音乐歌单适配器
 * Created by zhang on 2018/4/13.
 */

public class NetListAdapter extends BaseAdapter{
    private static final int TYPE_PROFILE = 0;
    private static final int TYPE_MUSIC_LIST =1;
    private Context mContext;
    private List<ListInfo> mdata;

    public NetListAdapter(List<ListInfo> data ){mdata = data;}

    @Override
    public int getCount(){return mdata.size();}

    @Override
    public Object getItem(int position){return mdata.get(position);}

    @Override
    public long getItemId(int position){return  position;}

    @Override
    public boolean isEnabled(int position){return getItemViewType(position) == TYPE_MUSIC_LIST;}

    @Override
    public int getItemViewType(int position){
        if(mdata.get(position).getType().equals("#")){
            return TYPE_PROFILE;
        }else{
            return TYPE_MUSIC_LIST;
        }
    }

    @Override
    public int getViewTypeCount(){return  2;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
    mContext = parent.getContext();
    ViewHolderProfile holderProfile;
    ViewHolderMusicList holderMusicList;
    ListInfo listInfo = mdata.get(position);
    int itemViewType = getItemViewType(position);
    switch (itemViewType){
        case TYPE_PROFILE:
            if(convertView ==null){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.view_holder_info_profile,parent,false);
                holderProfile =new ViewHolderProfile(convertView);
                convertView.setTag(holderProfile);
            }else {
                holderProfile = (ViewHolderProfile) convertView.getTag();
            }
            holderProfile.profile.setText(listInfo.getTitle());
            break;
        case TYPE_MUSIC_LIST:
            if(convertView == null){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.listinfo_view_holder,parent,false);
                holderMusicList = new ViewHolderMusicList(convertView);
                convertView.setTag(holderMusicList);
            }else{
                holderMusicList = (ViewHolderMusicList) convertView.getTag();
            }
            getMusicListInfo(listInfo,holderMusicList);
            holderMusicList.Divider.setVisibility(isShowDivider(position)? View.VISIBLE : View.GONE);
            break;
    }
    return convertView;
    }
    private boolean isShowDivider(int position){return position!= mdata.size()-1;}

    private void getMusicListInfo(final ListInfo listInfo,final ViewHolderMusicList holderMusicList){
        if(listInfo.getCoverUrl() ==null){
            holderMusicList.Music1.setTag(listInfo.getTitle());
            holderMusicList.Cover.setImageResource(R.drawable.default_cover);
            holderMusicList.Music1.setText("1.加载中...");
            holderMusicList.Music2.setText("2.加载中...");
            holderMusicList.Music3.setText("3.加载中...");
            Log.d( "typeid",listInfo.getType());
            HttpClient.getSongListInfo(listInfo.getType(),3,0,new HttpCallback<OnLineMusicList>(){
                @Override
                public void onSuccess(OnLineMusicList response){
                    Log.d("net_response", "onSuccess: "+response.toString());
                    if(response == null || response.getSong_list() == null){
                        return;
                    }
                    if(!listInfo.getTitle().equals(holderMusicList.Music1.getTag()))
                    {
                        return;
                    }
                    parse(response,listInfo);
                    setData(listInfo,holderMusicList);
                }
                @Override
                public void onFail(Exception e){}
            });
        } else{
            holderMusicList.Music1.setTag(null);
            setData(listInfo,holderMusicList);
        }
    }

    private void parse(OnLineMusicList response,ListInfo listInfo){
        List<OnLineMusic> onLineMusics = response.getSong_list();
        listInfo.setCoverUrl(response.getBillboard().getPic_s260());
        if(onLineMusics.size()>=1){
            listInfo.setMusic1(mContext.getString(R.string.song_list_item_title_1,
                    onLineMusics.get(0).getTitle(),onLineMusics.get(0).getArtisit_name()));

        }else{
            listInfo.setMusic1("");
        }
        if(onLineMusics.size()>=2){
            listInfo.setMusic2(mContext.getString(R.string.song_list_item_title_2,
                    onLineMusics.get(1).getTitle(),onLineMusics.get(1).getArtisit_name()));
        }else{
            listInfo.setMusic2("");
        }
        if(onLineMusics.size()>=3){
            listInfo.setMusic3(mContext.getString(R.string.song_list_item_title_3,
                    onLineMusics.get(2).getTitle(),onLineMusics.get(2).getArtisit_name()));
        }else{
            listInfo.setMusic3("");
        }
    }

    private void setData(ListInfo listInfo, ViewHolderMusicList holderMusicList){
        holderMusicList.Music1.setText(listInfo.getMusic1());
        holderMusicList.Music2.setText(listInfo.getMusic2());
        holderMusicList.Music3.setText(listInfo.getMusic3());
        Glide.with(mContext)
                .load(listInfo.getCoverUrl())
                .placeholder(R.drawable.default_cover)
                .error(R.drawable.default_cover)
                .into(holderMusicList.Cover);
    }

    private static class ViewHolderProfile{
        @Bind(R.id.profile)
        private TextView profile;

        public ViewHolderProfile(View view){
            ViewBinder.bind(this,view);}
    }

    private static class ViewHolderMusicList{
        @Bind(R.id.iv_cover)
        private ImageView Cover;
        @Bind(R.id.music_1)
        private TextView Music1;
        @Bind(R.id.music_2)
        private TextView Music2;
        @Bind(R.id.music_3)
        private TextView Music3;
        @Bind(R.id.divider)
        private View Divider;

        public ViewHolderMusicList(View view){ViewBinder.bind(this,view);}
    }
}
