package com.example.zhang.thinmusic.http;

import android.util.Log;

import com.example.zhang.thinmusic.model.NeteaseLyric;
import com.example.zhang.thinmusic.model.NeteaseMusicList;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.OkHttpClient;

public class HttpClient2Netease {

    private static final String BASE_URL = "https://api.imjad.cn/cloudmusic/?";
    private static final String PARAM_TYPE="type";
    private static final String PARAM_ID="id";
    private static final String TPYE_GETPLAYLIST="playlist";
    private static final String TYPE_GETSONG="song";
    private static final String TYPE_GETLYRIC="lyric";
    private static final String TYPE_GETDETAIL="detail";
    private static final String TYPE_GETARTIST="artist";
    private static final String TYPE_SEARCH="search";
    private static final String TYPE_ALBUM="album";

/*    配置okhttpclient*/
    static {
    HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null,null,null);

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(sslParams.sSLSocketFactory,sslParams.trustManager)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15,TimeUnit.SECONDS)
            .writeTimeout(10,TimeUnit.SECONDS)

            .build();
    OkHttpUtils.initClient(okHttpClient);
}

    public static void getNeteaseMusicListInfo(String id,@NonNull final HttpCallback<NeteaseMusicList> callback){
        OkHttpUtils.get().url(BASE_URL)
                .addParams(PARAM_TYPE,TPYE_GETPLAYLIST)
                .addParams(PARAM_ID,id)
                .build()
                .execute(new JsonCallback<NeteaseMusicList>(NeteaseMusicList.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d("resonse:callback", "onError: ");
                        callback.onFail(e);
                    }

                    @Override
                    public void onResponse(NeteaseMusicList response, int id) {

                        Log.d("resonse:callback", "onResponse: "+response.getPlaylist().toString());
                        callback.onSuccess(response);
                    }
                    @Override
                    public void onAfter(int id){
                        callback.onFinish();}
                });


    }

    public static void getNeteaseMusicLyric(String id,@NonNull final HttpCallback<NeteaseLyric> callback){
        OkHttpUtils.get().url(BASE_URL)
                .addParams(PARAM_TYPE,TYPE_GETLYRIC)
                .addParams(PARAM_ID,id)
                .build()
                .execute(new JsonCallback<NeteaseLyric>(NeteaseLyric.class) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callback.onFail(e);
                    }

                    @Override
                    public void onResponse(NeteaseLyric response, int id) {
                        callback.onSuccess(response);
                    }//将回调解析为NeteaseLyric类型的对象

                    @Override
                    public void onAfter(int id){
                        callback.onFinish();
                    }
                });
    }

    //public static void searchNeteaseMusic(String keyword,@NonNull final HttpCallback<SearchResult>)
}
