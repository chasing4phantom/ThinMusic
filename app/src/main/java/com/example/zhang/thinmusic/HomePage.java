package com.example.zhang.thinmusic;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends AppCompatActivity {
    private List<navigation> navigationsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        initnavigation();
        navigationadapter adapter = new navigationadapter(HomePage.this,R.layout.navigation_item,
                navigationsList);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //navigation navigation = navigationsList.get(i);
                switch (i){
                    case 0:
                    {

                        Intent intent = new Intent(HomePage.this,Localmusiclist.class);
                        startActivity(intent);

                   }
                    break;
                    /*case 1:
                    {
                        Intent intent1 = new Intent(HomePage.this, Download.class);
                    startActivities(intent1);
                    }
                    break;
                    case 2:
                    {
                        Intent intent2 = new Intent(HomePage.this, MusicList.class);
                    startActivities(intent2);
                    }
                break;
                    case 3:
                    {
                        Intent intent3 = new Intent(HomePage.this, Favourite.class);
                        startActivities(intent3);
                    }
                    break;*/
                    default:
                        break;
                }
            }
        });
    }
    private  void initnavigation(){
        navigation localmusic = new navigation("本地音乐",R.drawable.localmusic);
        navigationsList.add(localmusic);
        navigation download = new navigation("下载管理",R.drawable.download);
        navigationsList.add(download);
        navigation musicList = new navigation("我的歌单",R.drawable.musiclist);
        navigationsList.add(musicList);
        navigation favourite = new navigation("我喜欢的音乐",R.drawable.favourite);
        navigationsList.add(favourite);
    }

}

