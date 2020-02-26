package com.example.zhang.thinmusic.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.zhang.thinmusic.R;
import com.example.zhang.thinmusic.adapter.NeteaseListAdapter;
import com.example.zhang.thinmusic.application.AppCache;
import com.example.zhang.thinmusic.constants.Keys;
import com.example.zhang.thinmusic.model.NeteaseListInfo;
import com.example.zhang.thinmusic.utils.Bind;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NeteaseListFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    @Bind(R.id.netease_list)
    private ListView NeteasePlayList;

     private List<List<NeteaseListInfo>> SongLists;
     private List<NeteaseListInfo> songlistclone = new ArrayList<>();
     private List<NeteaseListInfo> SongList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_netease_list,container,false);
    }

    @Override
    public void onActivityCreated(@NonNull Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        SongLists = AppCache.get().getNeteaseSongLists();
        if(SongLists.isEmpty()) {
            String[] category = getResources().getStringArray(R.array.netease_musiclist_category);
            String[] titles = getResources().getStringArray(R.array.netease_music_list_title);
            String[] listsid = getResources().getStringArray(R.array.netease_music_list_id);
            int flag = 0;
            for (int i = 0; i < category.length; i++) {
                List<NeteaseListInfo> listclone = new ArrayList<>();
                songlistclone.clear();
                for (int j = flag; j < listsid.length; j++) {

                    if (listsid[j].equals("#")) {
                        flag=j+1;
                        break;

                    } else  {
                        NeteaseListInfo info = new NeteaseListInfo();

                        info.setTitle(titles[j]);
                        info.setId(listsid[j]);
                        SongList.add(info);

                    }
                }
                Log.d("SongList", "onActivityCreated: "+SongList.get(0).getTitle()+"   "+SongList.get(1).getTitle()+"    "+SongList.get(2).getTitle());
                listclone.addAll(SongList);
                SongLists.add(listclone);
                SongList.clear();
            }

        }

        NeteaseListAdapter adapter = new NeteaseListAdapter(SongLists);

        NeteasePlayList.setAdapter(adapter);
    }


    @Override
    protected void setListener(){NeteasePlayList.setOnItemClickListener(this);}

    @Override
    public void onItemClick(AdapterView<?> parent,View view,int position,long id){
/*        NeteaseListInfo neteaseListInfo = SongLists.get(position).get(position);
        Intent intent = new Intent(getContext(),NeteaseMusicActivity.class);
        intent.putExtra(Extras.NETEASE_LIST_ID,neteaseListInfo);
        startActivity(intent);*/
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        //int position = NeteasePlayList.getFirstVisiblePosition();
        //int offset
        //outState.putInt(Keys.PLAYLIST_POSITION,position);
        //outState.putInt(offset);

    }

    public void onRestoreInstanceState(final Bundle savedInstancedState){
/*        NeteasePlayList.post(()->{
            int position = savedInstancedState.getInt(Keys.PLAYLIST_POSITION);
            //int offset
        });*/
    }

}
