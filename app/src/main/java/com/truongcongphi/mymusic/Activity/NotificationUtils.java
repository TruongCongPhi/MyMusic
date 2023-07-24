package com.truongcongphi.mymusic.Activity;

import static com.truongcongphi.mymusic.Activity.PlaySongActivity.songArrayList;

import android.Manifest;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.truongcongphi.mymusic.Class.NotificationActionReceiver;
import com.truongcongphi.mymusic.Class.Song;
import com.truongcongphi.mymusic.R;

public class NotificationUtils extends Application {

    // Khai báo các action cho các nút trong notification
    public static final String ACTION_PREVIOUS = "com.truongcongphi.mymusic.ACTION_PREVIOUS";
    public static final String ACTION_PLAY_PAUSE = "com.truongcongphi.mymusic.ACTION_PLAY_PAUSE";
    public static final String ACTION_NEXT = "com.truongcongphi.mymusic.ACTION_NEXT";

    // Hàm tạo thông báo
    public static void createNotification(Context context, Song song) {
        int notificationId = 1;
        String channelId = "my_channel_id";
        String channelName = "My Channel";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Kiểm tra phiên bản Android, vì từ Android 8.0 trở lên cần tạo kênh thông báo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Tạo PendingIntent để khi người dùng nhấn vào thông báo sẽ mở ứng dụng
        Intent intent = new Intent(context, PlaySongActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Tạo thông báo
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.musicnote)
                .setContentTitle(song.getSongName())
                .setContentText(TextUtils.join(", ", song.getSingerName()))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Tạo và gán các PendingIntent cho các action
        PendingIntent previousPendingIntent = createActionPendingIntent(context, ACTION_PREVIOUS);
        PendingIntent playPausePendingIntent = createActionPendingIntent(context, ACTION_PLAY_PAUSE);
        PendingIntent nextPendingIntent = createActionPendingIntent(context, ACTION_NEXT);

        // Tạo các nút action trong notification
        NotificationCompat.Action previousAction = new NotificationCompat.Action.Builder(
                R.drawable.icon_previous, "Previous", previousPendingIntent).build();
        NotificationCompat.Action playPauseAction = new NotificationCompat.Action.Builder(
                R.drawable.play, "Play/Pause", playPausePendingIntent).build();
        NotificationCompat.Action nextAction = new NotificationCompat.Action.Builder(
                R.drawable.icon_skip_next, "Next", nextPendingIntent).build();

        // Thêm các action vào notification
        notificationBuilder.addAction(previousAction);
        notificationBuilder.addAction(playPauseAction);
        notificationBuilder.addAction(nextAction);

        // Hiển thị notification
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    private static PendingIntent createActionPendingIntent(Context context, String action) {
        Intent intent = new Intent(context, NotificationActionReceiver.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
