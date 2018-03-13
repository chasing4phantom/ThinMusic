package com.example.zhang.thinmusic;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.List;


public class Localmusiclist extends Activity {
    private ListView mMusiclist;//音乐列表
    private SimpleAdapter mAdapter;
    List<Mp3Info> mp3Infos = null;
    private  List<HashMap<String,Object>> mp3list;
    private  HashMap<String,Object> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localmusiclist);
        if(isGrantExternalRW(Localmusiclist.this) != true){
            return;
        }
        mMusiclist =(ListView) findViewById(R.id.musiclist);//为listview添加数据
        mMusiclist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Mp3Info mp3Info = mp3Infos.get(i);
                Log.d("i=", mp3Info.getUrl());
                Intent intent = new Intent();
                intent.setClass(Localmusiclist.this,MusicPlayerActivity.class);
                intent.putExtra("url",mp3Info.getUrl());
                startActivity(intent);
            }
        });
        mp3Infos = MediaUtil.getMp3Infos(getApplicationContext());//获取歌曲对象集合
        setListAdapter(MediaUtil.getMusicMaps(mp3Infos));//显示歌曲列表
    }
    /* 填充列表*/
    public  void setListAdapter(List<HashMap<String,String>> mp3list){
        mAdapter = new SimpleAdapter(this,mp3list,
                R.layout.musicitem,new String[]{"number","title","Artist","check_music","music_menu"},
                new int[]{R.id.number,R.id.music_title,R.id.music_Artist,R.id.check,R.id.music_menu});
        mMusiclist.setAdapter(mAdapter);
    }
    /*获得读取权限*/
    public  static  boolean isGrantExternalRW(Activity activity){
        if(activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            activity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            return  false;
        }
        return  true;
    }
}

