package com.example.zhang.thinmusic.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.zhang.thinmusic.Application.AppCache;
import com.example.zhang.thinmusic.R;
import com.example.zhang.thinmusic.adapter.OnMoreClickListener;
import com.example.zhang.thinmusic.adapter.PlaylistAdapter;
import com.example.zhang.thinmusic.constants.Keys;
import com.example.zhang.thinmusic.model.Music;
import com.example.zhang.thinmusic.utils.AudioPlayer;
import com.example.zhang.thinmusic.utils.Bind;
import com.example.zhang.thinmusic.utils.MusicUtils;
import com.example.zhang.thinmusic.utils.PermissionReq;
import com.example.zhang.thinmusic.utils.ToastUtils;

import java.util.List;

/**
 * Created by zhang on 2018/3/25.
 */

public class LocalMusicFragment extends BaseFragment implements AdapterView.OnItemClickListener, OnMoreClickListener{
    @Bind(R.id.local_music)
    private ListView LocalMusic;

    private PlaylistAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        return inflater.inflate(R.layout.fragment_local_music,container,false);

    }
    @Override
    public void onActivityCreated(@Nullable Bundle saveInstanceState){
        super.onActivityCreated(saveInstanceState);

        adapter = new PlaylistAdapter(AppCache.get().getLocalMusicList());

        adapter.setOnMoreClickListener(this);
        LocalMusic.setAdapter(adapter);
        if(AppCache.get().getLocalMusicList().isEmpty()){
            scanMusic(null);
        }
    }

   // @Subscribe(tags = {@Tag(RxBusTags.SCAN_MUSIC)})
    public void scanMusic(Object object) {
        LocalMusic.setVisibility(View.GONE);
        PermissionReq.with(this)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .result(new PermissionReq.Result() {
            @SuppressLint("StaticFieldLeak")
                    @Override
            public void onGranted () {
                new AsyncTask<Void, Void, List<Music>>() {
                    @Override
                    protected List<Music> doInBackground(Void... params) {
                        return MusicUtils.scanMusic(getContext());
                    }

                    @Override
                    protected void onPostExecute(List<Music> musicList) {
                        AppCache.get().getLocalMusicList().clear();
                        AppCache.get().getLocalMusicList().addAll(musicList);
                        LocalMusic.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                    }
                }.execute();

            }
            @Override
            public void onDenied () {
                ToastUtils.show("没有存储空间权限，无法扫描");
                LocalMusic.setVisibility(View.GONE);
            }
        }).request();
    }
        @Override
        protected void setListener () {
            LocalMusic.setOnItemClickListener(this);
        }
        @Override
        public void onItemClick (AdapterView <?> parent, View view,int position,long id){
            Music music = AppCache.get().getLocalMusicList().get(position);
            Log.i(music.getPath(), "onItemClick: ");

            AudioPlayer.get().addAndPlay(music);
            ToastUtils.show("已添加到播放列表");
        }
        @Override
        public void onMoreClick ( final int position){
            Music music = AppCache.get().getLocalMusicList().get(position);
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle(music.getTitle());
            dialog.setItems(R.array.local_music_dialog, (dialog1, which) -> {
                switch (which) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }

            });
            dialog.show();
        }

        @Override
        public void onSaveInstanceState (Bundle outState ){
            int position = LocalMusic.getFirstVisiblePosition();
            int offset = (LocalMusic.getChildAt(0) == null) ? 0 : LocalMusic.getChildAt(0).getTop();
            outState.putInt(Keys.LOCAL_MUSIC_POSITION, position);
            outState.putInt(Keys.LOCAL_MUSIC_OFFSET, offset);

        }

        public void onRestoreInstanceState ( final Bundle savedInstanceState){
            LocalMusic.post(() -> {
                int position = savedInstanceState.getInt(Keys.LOCAL_MUSIC_POSITION);
                int offset = savedInstanceState.getInt(Keys.LOCAL_MUSIC_OFFSET);
                LocalMusic.setSelectionFromTop(position, offset);
            });
        }
    }

