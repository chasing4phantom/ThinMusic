package com.example.zhang.thinmusic.executor;

import android.text.TextUtils;

import com.example.zhang.thinmusic.http.HttpCallback;
import com.example.zhang.thinmusic.http.HttpClient;
import com.example.zhang.thinmusic.model.Lrc;
import com.example.zhang.thinmusic.model.SearchMusic;
import com.example.zhang.thinmusic.utils.FileUtils;

/**搜索在线歌词
 * Created by zhang on 2018/4/24.
 */

public abstract class SearchLrc implements IExecutor<String>{
    private String artist;
    private String title;

    public SearchLrc(String artist,String title){
        this.artist = artist;
        this.title = title;
    }

    @Override
    public void execute(){
        onPrepare();
        searchLrc();
    }

    private void searchLrc(){
        HttpClient.searchMusic(title + "-" + artist, new HttpCallback<SearchMusic>() {
            @Override
            public void onSuccess(SearchMusic response) {
                if(response == null || response.getSong() == null ||response.getSong().isEmpty() ){
                    onFail(null);
                    return;
                }
                downloadLrc(response.getSong().get(0).getSongid());
            }

            @Override
            public void onFail(Exception e) {
                onExecuteFail(e);
            }
        });
    }


    private void downloadLrc(String songId){
        HttpClient.getLrc(songId, new HttpCallback<Lrc>() {
            @Override
            public void onSuccess(Lrc response) {
                if(response == null|| TextUtils.isEmpty(response.getLrcContent())){
                    onFail(null);
                    return;
                }
                String filePath = FileUtils.getLrcDir()+FileUtils.getFileName(artist,title);
                FileUtils.saveLrcFile(filePath,response.getLrcContent());
                onExecuteSuccess(filePath);
            }

            @Override
            public void onFail(Exception e) {
                onExecuteFail(e);
            }
        });
    }
}
