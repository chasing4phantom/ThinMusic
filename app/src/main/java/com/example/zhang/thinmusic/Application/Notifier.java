package com.example.zhang.thinmusic.Application;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.zhang.thinmusic.R;
import com.example.zhang.thinmusic.activity.HomepageActivity;
import com.example.zhang.thinmusic.constants.Extras;
import com.example.zhang.thinmusic.model.Music;
import com.example.zhang.thinmusic.receiver.StatusBarReceiver;
import com.example.zhang.thinmusic.service.PlayService;
import com.example.zhang.thinmusic.utils.FileUtils;

/**
 * Created by zhang on 2018/4/3.
 */

public class Notifier {
    private static final int NOTIFIACTION_ID= 0x111;
    private PlayService playService;
    private NotificationManager notificationManager;

    public static Notifier get(){return SingletonHolder.instance;}

    private static class SingletonHolder{
        private static Notifier instance = new Notifier();
    }

    private Notifier(){}

    public void init(PlayService playService){
        this.playService = playService;
        notificationManager = (NotificationManager) playService.getSystemService(Context.NOTIFICATION_SERVICE);

    }

    public void showPlay(Music music){
        if(music == null){
            return;
        }
        playService.startForeground(NOTIFIACTION_ID, buildNotification(playService, music, true) );
    }

    public void showPause(Music music){
        if(music == null){
            return;
        }
        playService.stopForeground(false);
        notificationManager.notify(NOTIFIACTION_ID,buildNotification(playService,music,false));
    }

    public void cancelAll(){notificationManager.cancelAll();}

    private Notification buildNotification(Context context,Music music,boolean isPlaying){
        Intent intent = new Intent(context, HomepageActivity.class);
        intent.putExtra(Extras.EXTRA_NOTIFICATION, true);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder =new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.notification)
                .setCustomContentView(getRemoteViews(context,music,isPlaying));
        return builder.build();
    }

    private RemoteViews getRemoteViews(Context context,Music music,boolean isPlaying){
        String title = music.getTitle();
        String subtitle = FileUtils.getArtistAndAlbum(music.getArtist(),music.getAlbum());

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.notification);

        remoteViews.setTextViewText(R.id.tv_title, title);
        remoteViews.setTextViewText(R.id.tv_subtitle, subtitle);


        Intent playIntent = new Intent(StatusBarReceiver.ACTION_STATUS_BAR);
        playIntent.putExtra(StatusBarReceiver.EXTRA, StatusBarReceiver.EXTRA_PLAY_PAUSE);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(context, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setImageViewResource(R.id.iv_play_pause,R.drawable.play_bar_btn_pause);
        remoteViews.setOnClickPendingIntent(R.id.iv_play_pause, playPendingIntent);

        Intent nextIntent = new Intent(StatusBarReceiver.ACTION_STATUS_BAR);
        nextIntent.putExtra(StatusBarReceiver.EXTRA, StatusBarReceiver.EXTRA_NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context, 1, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setImageViewResource(R.id.iv_next, R.drawable.play_bar_btn_next);
        remoteViews.setOnClickPendingIntent(R.id.iv_next, nextPendingIntent);
        return remoteViews;
    }


}
