package com.example.zhang.thinmusic.storage;

import android.content.Context;

import com.example.zhang.thinmusic.storage.greendao.DaoMaster;
import com.example.zhang.thinmusic.storage.greendao.DaoSession;
import com.example.zhang.thinmusic.storage.greendao.MusicDao;

import org.greenrobot.greendao.database.Database;

/**
 * 封装musicdao，操作音乐文件
 * Created by zhang on 2018/3/20.
 */

public class DBManager {
    private  static final String DB_NAME = "database";
    private MusicDao musicDao;
    //单例模式
    public static DBManager get(){return  SingletonHolder.instance;}

    private static  class  SingletonHolder{
        private static DBManager instance = new DBManager();
    }

    public void init(Context context){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context,DB_NAME);
        Database db = helper.getWritableDb();
        DaoSession daoSession = new DaoMaster(db).newSession();
        musicDao = daoSession.getMusicDao();
    }//数据库初始化
    private DBManager(){

    }

    public MusicDao getMusicDao(){
        return musicDao;
    }
}
