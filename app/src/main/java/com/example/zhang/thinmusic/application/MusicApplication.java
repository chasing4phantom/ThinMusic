package com.example.zhang.thinmusic.application;

import android.app.Application;
import android.content.Intent;

import com.example.zhang.thinmusic.service.PlayService;
import com.example.zhang.thinmusic.storage.DBManager;
import com.facebook.stetho.Stetho;

/**自定义application
 * Created by zhang on 2018/3/27.
 */

public class MusicApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();

        AppCache.get().init(this);
        DBManager.get().init(this);
        Stetho.initializeWithDefaults(this);

        Intent intent = new Intent(this, PlayService.class);
        startService(intent);
    }

}
