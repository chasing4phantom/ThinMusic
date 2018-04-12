package com.example.zhang.thinmusic.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.example.zhang.thinmusic.model.Music;

import java.io.File;

/**
 * Created by zhang on 2018/3/23.
 */

public class FileUtils {
    private static final String MP3 = ".mp3";
    private static final String LRC = ".lrc";

    private  static String getAppDir(){
        return Environment.getExternalStorageDirectory()+"/ThinMusic";
    }

    public static String getMusicDir(){
        String dir = getAppDir() +"/Music/";
        return mkdirs(dir);
    }

    public static String getLrcDir(){
        String dir = getAppDir() +"/Lyric/";
        return mkdirs(dir);
    }

    public static String getAlbumDir(){
        String dir = getAppDir()+ "/Album/";
        return  mkdirs(dir);
    }

    public static String getLogDir(){
        String dir = getAppDir()+ "/Log/";
        return mkdirs(dir);
    }

    public static String getRelativeMusicDir(){
        String dir = "ThinMusic/Music/";
        return mkdirs(dir);
    }

    public static String getCorpImagePath(Context context){
        return context.getExternalCacheDir()+"/corp.jpg";
    }

    public static String getLrcFilePath(Music music){
        if(music == null){
            return  null;
        }

        String lrcFilePath = getLrcDir() + getLrcFileName(music.getArtist(),music.getTitle());
        if(!exists(lrcFilePath)){
            lrcFilePath = music.getPath().replace(MP3,LRC);
            if(!exists(lrcFilePath)){
                lrcFilePath =null;
            }
        }
        return lrcFilePath;
    }

    private static String mkdirs(String dir){
        File file = new File(dir);
        if(!file.exists()){
            file.mkdirs();
        }
        return  dir;
    }

    private static boolean exists(String path){
        File file = new File(path);
        return file.exists();
    }

    public static String getMp3FileNme(String artist,String title){
        return getFileName(artist,title) + MP3;
    }
    public static String getLrcFileName(String artist,String title){
        return getFileName(artist,title) + LRC;
    }

    public static String getFileName(String artist,String title){

        if(TextUtils.isEmpty(artist)){
            artist = "未知歌手";
        }
        if(TextUtils.isEmpty(title)){
            title = "未知标题";
        }
        return artist + " - " + title;
    }

    public static String getArtistAndAlbum(String artist,String album){
        if(TextUtils.isEmpty(artist) && TextUtils.isEmpty(album)){
            return  "";
        }else if(!TextUtils.isEmpty(artist) && TextUtils.isEmpty(album)){
            return artist;
        }else if(TextUtils.isEmpty(artist) && !TextUtils.isEmpty(album)){
            return album;
        }else{

            return artist + " - " + album;
        }
    }
}
