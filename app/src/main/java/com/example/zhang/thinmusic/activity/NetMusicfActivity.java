package com.example.zhang.thinmusic.activity;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.zhang.thinmusic.executor.PlayOnlineMusic;
import com.example.zhang.thinmusic.R;
import com.example.zhang.thinmusic.adapter.OnMoreClickListener;
import com.example.zhang.thinmusic.adapter.OnlineMusicAdapter;
import com.example.zhang.thinmusic.constants.Extras;
import com.example.zhang.thinmusic.constants.LoadStateEnum;
import com.example.zhang.thinmusic.executor.ShareOnlineMusic;
import com.example.zhang.thinmusic.http.HttpCallback;
import com.example.zhang.thinmusic.http.HttpClient;
import com.example.zhang.thinmusic.model.ListInfo;
import com.example.zhang.thinmusic.model.Music;
import com.example.zhang.thinmusic.model.OnLineMusic;
import com.example.zhang.thinmusic.model.OnLineMusicList;
import com.example.zhang.thinmusic.utils.AudioPlayer;
import com.example.zhang.thinmusic.utils.Bind;
import com.example.zhang.thinmusic.utils.FileUtils;
import com.example.zhang.thinmusic.utils.ImageUtils;
import com.example.zhang.thinmusic.utils.ScreenUtils;
import com.example.zhang.thinmusic.utils.ToastUtils;
import com.example.zhang.thinmusic.utils.ViewUtils;
import com.example.zhang.thinmusic.widget.AutoLoadListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2018/4/18.
 */

public class NetMusicfActivity extends BaseActivity implements AdapterView.OnItemClickListener,OnMoreClickListener,AutoLoadListView.OnLoadListener{
    private static final int MUSIC_LIST_SIZE =20;

    @Bind(R.id.online_music_list)
    private AutoLoadListView online_music_list;
    @Bind(R.id.loading)
    private LinearLayout loading;
    @Bind(R.id.load_fail)
    private LinearLayout load_fail;

    private View Header;
    private ListInfo mListInfo;
    private OnLineMusicList onLineMusicList;
    private List<OnLineMusic> MusicList = new ArrayList<>();
    private OnlineMusicAdapter Adapter = new OnlineMusicAdapter(MusicList);
    private int Offset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_music);
    }

    @Override
    protected void onServiceBound(){
        mListInfo = (ListInfo) getIntent().getSerializableExtra(Extras.MUSIC_LIST_TYPE);
        setTitle(mListInfo.getTitle());

        initView();
        onLoad();
    }

    private void initView(){
        Header = LayoutInflater.from(this).inflate(R.layout.activity_online_music_list_header,null);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.dp2px(130));
        Header.setLayoutParams(params);
        online_music_list.addHeaderView(Header,null,false);
        online_music_list.setAdapter(Adapter);
        online_music_list.setOnLoadListener(this);
        ViewUtils.changeViewState(online_music_list,loading,load_fail,LoadStateEnum.LOADING);

        online_music_list.setOnItemClickListener(this);
        Adapter.setMoreClickListener(this);
    }

    private void getMusic(final int offset){
        HttpClient.getSongListInfo(mListInfo.getType(),MUSIC_LIST_SIZE,offset, new HttpCallback<OnLineMusicList>(){
            @Override
            public void onSuccess(OnLineMusicList response){
                online_music_list.onLoadComplete();
                onLineMusicList = response;
                if(offset == 0 && response == null){
                    ViewUtils.changeViewState(online_music_list,loading,load_fail, LoadStateEnum.LOAD_FAIL);
                    return;
                }else if(offset == 0){
                    initHeader();
                    ViewUtils.changeViewState(online_music_list,loading,load_fail,LoadStateEnum.LOAD_SUCCESS);
                }
                if(response ==null || response.getSong_list() == null || response.getSong_list().size() ==0){
                    online_music_list.setEnable(false);
                    return;
                }
                Offset +=MUSIC_LIST_SIZE;
                MusicList.addAll(response.getSong_list());
                Adapter.notifyDataSetChanged();

            }
            @Override
            public void onFail(Exception e){
                online_music_list.onLoadComplete();
                if(e instanceof RuntimeException){
                    //歌曲加载完成
                    online_music_list.setEnable(false);
                    return;
                }
                if(offset == 0){
                    ViewUtils.changeViewState(online_music_list,loading,load_fail,LoadStateEnum.LOAD_FAIL);

                }else{
                    ToastUtils.show("加载失败，请检查网络后重试");
                }
            }
        });
    }

    @Override
    public void onLoad(){getMusic(Offset);}

    @Override
    public void onItemClick(AdapterView<?> parent,View view,int position,long id){
        play((OnLineMusic) parent.getAdapter().getItem(position));
    }

    @Override
    public void onMoreClick(int position){
        final OnLineMusic onLineMusic = MusicList.get(position);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(MusicList.get(position).getTitle());
        String path = FileUtils.getMusicDir()+ FileUtils.getMp3FileNme(onLineMusic.getArtisit_name(),onLineMusic.getTitle());
        File file = new File(path);
        int itemsId = file.exists() ? R.array.online_music_dialog_without_download : R.array.online_music_dialog;
        dialog.setItems(itemsId,(dialog1,which)->{
            switch (which){
                case 0:
                    share(onLineMusic);
                    break;
                case 1:
                    download(onLineMusic);
                    break;
            }
        });
        dialog.show();
    }

    private void initHeader(){
        final ImageView HeaderBg = Header.findViewById(R.id.iv_header_bg);
        final ImageView Cover = Header.findViewById(R.id.iv_cover);
        TextView Title = Header.findViewById(R.id.tv_title);
        TextView UpdateDate = Header.findViewById(R.id.tv_update_date);
        TextView Comment = Header.findViewById(R.id.tv_comment);
        Title.setText(onLineMusicList.getBillboard().getName());
        UpdateDate.setText(getString(R.string.recent_update,onLineMusicList.getBillboard().getUpdate_date()));
        Comment.setText(onLineMusicList.getBillboard().getComment());
        Glide.with(this)
                .load(onLineMusicList.getBillboard().getPic_s640())
                .asBitmap()
                .placeholder(R.drawable.default_cover)
                .error(R.drawable.default_cover)
                .override(200,200)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Cover.setImageBitmap(resource);
                        HeaderBg.setImageBitmap(ImageUtils.blur(resource));
                    }
                });
    }

    private void play(OnLineMusic onLineMusic){
        new PlayOnlineMusic(this,onLineMusic){
            @Override
            public void onPrepare(){showProgress();}

            @Override
            public void onExecuteSuccess(Music music){
                cancelProgress();
                AudioPlayer.get().addAndPlay(music);
                ToastUtils.show("已添加到播放列表");
            }
            @Override
            public void onExecuteFail(Exception e){
                cancelProgress();
                ToastUtils.show("暂时无法播放");
            }
        }.execute();
    }

    private void share(final OnLineMusic onlineMusic){
        new ShareOnlineMusic(this,onlineMusic.getTitle(),onlineMusic.getSong_id()){
            @Override
            public void onPrepare(){showProgress();}

            @Override
            public void onExecuteSuccess(Void aVoid){cancelProgress();}

            @Override
            public void onExecuteFail(Exception e){cancelProgress();}
        }.execute();
    }

    private void download(final OnLineMusic onLineMusic){
/*        new DownloadOnlineMusicf(this,onLineMusic){
            @Override
            public void onProgress(){showProgress();}

            @Override
            public void onExecuteSuccess(Void aVoid){
                cancelProgress();
                ToastUtils.show(getString(R.string.now_download,onLineMusic.getTitle()));
            }

            @Override
            public void onExecuteFail(Exception e){
                cancelProgress();
                ToastUtils.show("暂时无法下载");
            }
        }.excute();*/
    }

}
