package com.example.zhang.thinmusic.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;


import com.example.zhang.thinmusic.Mp3Info;
import com.example.zhang.thinmusic.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhang on 2018/1/31.
 */

public class MediaUtil {
    /*用于从数据库中查询歌曲信息，保存在list中*/
    public  static List<Mp3Info> getMp3Infos(Context context){
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        List<Mp3Info> mp3Infos = new ArrayList<Mp3Info>();
        for(int i = 0; i< cursor.getCount(); i++){
            cursor.moveToNext();
            Mp3Info mp3Info = new Mp3Info();
            long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
            if(isMusic !=0){
                mp3Info.setId(id);
                mp3Info.setTitle(title);
                mp3Info.setArtist(artist);
                mp3Info.setDuration(duration);
                mp3Info.setSize(size);
                mp3Info.setUrl(url);
                mp3Infos.add(mp3Info);
            }
        }
        return  mp3Infos;
    }
    /*向list集合中添加Map数据对象，每一个Map存放一首歌的所有属性*/
    public  static List<HashMap<String ,String>> getMusicMaps(
            List<Mp3Info> mp3Infos){
            List<HashMap<String, String>> mp3list = new ArrayList<HashMap<String, String>>();
            String check_music = String.valueOf(R.drawable.check);
            String music_menu = String.valueOf(R.drawable.music_menu);
            int i = 0;/*<span style="white-space:pre"> </span>*/
                for(Iterator iterator = mp3Infos.iterator(); iterator.hasNext();){
                i++;
                Mp3Info mp3Info = (Mp3Info) iterator.next();
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("number",String.valueOf(i));
                map.put("id",String.valueOf(mp3Info.getId()));
                map.put("title",String.valueOf(mp3Info.getTitle()));
                map.put("Artist",String.valueOf(mp3Info.getArtist()));
                map.put("duration",formatTime(mp3Info.getDuration()));
                map.put("size",String.valueOf(mp3Info.getSize()));
                map.put("url",mp3Info.getUrl());
                map.put("check_music",check_music);
                map.put("music_menu",music_menu);
                mp3list.add(map);
                }
                return  mp3list;
    }
    /*时间格式转化，将毫秒转化为分::秒*/
    public  static  String formatTime(long time){
        String min = time / (1000 * 60)+ "";
        String sec = time % (1000 * 60)+ "";
        if(min.length()<2){
            min = "0"+ time / (1000 * 60)+"";
        }else
        {
            min = time / (1000 * 60 ) +"";
        }
        if(sec.length()==4){
            sec = "0" +(time % (1000 * 60))+"";
        }else if(sec.length()==3){
            sec = "00"+(time % (1000 * 60))+"";
        }else if(sec.length()==2){
            sec = "000"+(time % (1000 * 60))+"";
        }else if(sec.length()==1){
            sec = "0000"+(time % (1000 * 60))+"";
        }
            return  min+ ":" + sec.trim().substring(0,2);
    }
}
