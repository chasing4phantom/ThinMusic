package com.example.zhang.thinmusic.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.zhang.thinmusic.adapter.onClick;
import com.example.zhang.thinmusic.constants.Keys;
import com.example.zhang.thinmusic.model.Music;
import com.example.zhang.thinmusic.utils.AudioPlayer;
import com.example.zhang.thinmusic.utils.Bind;
import com.example.zhang.thinmusic.utils.MusicUtils;
import com.example.zhang.thinmusic.utils.PermissionReq;
import com.example.zhang.thinmusic.utils.ToastUtils;

import java.io.File;
import java.util.List;

/**
 * Created by zhang on 2018/3/25.
 */

public class LocalMusicFragment extends BaseFragment implements onClick,OnMoreClickListener{
    @Bind(R.id.local_music)
    private RecyclerView LocalMusic;

    private PlaylistAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.fragment_local_music,container,false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        LocalMusic = view.findViewById(R.id.local_music);
        LocalMusic.setLayoutManager(linearLayoutManager);
        adapter =new PlaylistAdapter(AppCache.get().getLocalMusicList());
        adapter.setOnClickListener(this);
        adapter.setonMoreClickListener(this);
        LocalMusic.setAdapter(adapter);
        return view;

    }
    @Override
    public void onActivityCreated(@Nullable Bundle saveInstanceState){
        super.onActivityCreated(saveInstanceState);
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
       /* @Override
        protected void setListener () {
            LocalMusic.setOnItemClickListener(this);
        }
        @Override
        public void onItemClick (AdapterView <?> parent, View view,int position,long id){
            Music music = AppCache.get().getLocalMusicList().get(position);
            Log.i(music.getPath(), "onItemClick: ");

            AudioPlayer.get().addAndPlay(music);
            ToastUtils.show("已添加到播放列表");
        }*/
       @Override
       public void onClick(final int position){
           Music music = AppCache.get().getLocalMusicList().get(position);
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
                        share(music);
                        break;
                    case 1:
                        delete(music);
                        break;
                    }

            });
            dialog.show();
        }


    //onMoreClick中的分享操作
    private void share(Music music){
        File file = new File(music.getPath());
        Intent sendIntent = new Intent();
        //android7.0后不能直接使用uri.fromFile()，要使用FileProvider
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(AppCache.get().context,"com.example.zhang.thinmusic.fileprovider",file));
        sendIntent.setType("audio/*");
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(sendIntent,"分享"));
    }

    //onMoreClick中的删除操作
    private void delete(final Music music){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        String title = music.getTitle();
        String msg="是否删除"+title +"?";
        dialog.setMessage(msg);
        dialog.setPositiveButton("删除",((dialog1, which) -> {
            File file = new File(music.getPath());
            if(file.delete()){
                AppCache.get().getLocalMusicList().remove(music);
                adapter.notifyDataSetChanged();
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.parse("file://".concat(music.getPath())));
                getContext().sendBroadcast(intent);
            }
        }));
        dialog.setNegativeButton("取消",null);
        dialog.show();
    }
        @Override
        public void onSaveInstanceState (Bundle outState ){
        RecyclerView.LayoutManager layoutManager = LocalMusic.getLayoutManager();
        if(layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;

            int position =linearLayoutManager.findFirstVisibleItemPosition();
            int offset = (LocalMusic.getChildAt(0) == null) ? 0 : LocalMusic.getChildAt(0).getTop();
            outState.putInt(Keys.LOCAL_MUSIC_POSITION, position);
            outState.putInt(Keys.LOCAL_MUSIC_OFFSET, offset);
        }
        }

        public void onRestoreInstanceState ( final Bundle savedInstanceState){
            LocalMusic.post(() -> {
                int position = savedInstanceState.getInt(Keys.LOCAL_MUSIC_POSITION);
                int offset = savedInstanceState.getInt(Keys.LOCAL_MUSIC_OFFSET);
                RecyclerView.LayoutManager layoutManager = LocalMusic.getLayoutManager();
                if(layoutManager instanceof LinearLayoutManager){
                    ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(position,offset);}
                    else {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                linearLayoutManager.scrollToPositionWithOffset(position,offset);
                }
            });
        }
    }

