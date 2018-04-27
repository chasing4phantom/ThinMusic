package com.example.zhang.thinmusic.executor;


import android.content.Context;
import android.content.Intent;

import com.example.zhang.thinmusic.R;
import com.example.zhang.thinmusic.http.HttpCallback;
import com.example.zhang.thinmusic.http.HttpClient;
import com.example.zhang.thinmusic.model.DownloadInfo;
import com.example.zhang.thinmusic.utils.ToastUtils;

import java.io.File;

/**
 * Created by zhang on 2018/4/27.
 */

public abstract class ShareOnlineMusic implements IExecutor<Void> {
    private Context context;
    private String title;
    private String songId;

    public ShareOnlineMusic(Context context, String title, String songId){
        this.context = context;
        this.title = title;
        this.songId = songId;
    }

    @Override
    public void execute(){
        onPrepare();
        share();
    }

    private void share(){
        HttpClient.getMusicDownloadInfo(songId, new HttpCallback<DownloadInfo>() {
            @Override
            public void onSuccess(DownloadInfo response) {
                if(response == null){
                    onFail(null);
                    return;
                }
                onExecuteSuccess(null);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_music, context.getString(R.string.app_name),
                        title, response.getBitrate().getFile_link()));
                context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)));

            }

            @Override
            public void onFail(Exception e) {
                onExecuteFail(e);
                ToastUtils.show("分享失败");

            }
        });
    }

}
