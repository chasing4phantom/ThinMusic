package com.example.zhang.thinmusic.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.zhang.thinmusic.Application.AppCache;
import com.example.zhang.thinmusic.R;
import com.example.zhang.thinmusic.activity.NetMusicfActivity;
import com.example.zhang.thinmusic.adapter.NetListAdapter;
import com.example.zhang.thinmusic.constants.Extras;
import com.example.zhang.thinmusic.constants.Keys;
import com.example.zhang.thinmusic.model.ListInfo;
import com.example.zhang.thinmusic.utils.Bind;

import java.util.List;

/**在线音乐排行榜列表
 * Created by zhang on 2018/4/13.
 */

public class NetlistFragment extends BaseFragment implements AdapterView.OnItemClickListener{
    @Bind(R.id.netlist)
    private ListView Playlist;

    private List<ListInfo> Songlists;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_net_list,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        Songlists = AppCache.get().getSongList();
        if(Songlists.isEmpty()){
            String[] titles = getResources().getStringArray(R.array.online_music_list_title);
            String[] types = getResources().getStringArray(R.array.online_music_list_type);
            for(int i =0;i< titles.length;i++){
                ListInfo info = new ListInfo();
                info.setTitle(titles[i]);
                info.setType(types[i]);
                Songlists.add(info);
            }
        }
        NetListAdapter adapter = new NetListAdapter(Songlists);
        Playlist.setAdapter(adapter);
    }

    @Override
    protected void setListener(){Playlist.setOnItemClickListener(this);}

    @Override
    public void onItemClick(AdapterView<?> parent,View view,int position,long id){
        ListInfo listInfo = Songlists.get(position);
        Intent intent = new Intent(getContext(),NetMusicfActivity.class);
        intent.putExtra(Extras.MUSIC_LIST_TYPE,listInfo);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        int position = Playlist.getFirstVisiblePosition();
        int offset = (Playlist.getChildAt(0)==null)? 0:Playlist.getChildAt(0).getTop();
        outState.putInt(Keys.PLAYLIST_POSITION,position);
        outState.putInt(Keys.PLAYLIST_OFFSET,offset);
    }

    public void onRestoreInstanceState(final Bundle savedInstanceState){
        Playlist.post(()->{
            int position = savedInstanceState.getInt(Keys.PLAYLIST_POSITION);
            int offset = savedInstanceState.getInt(Keys.PLAYLIST_OFFSET);
            Playlist.setSelectionFromTop(position,offset);
        });
    }
   }
