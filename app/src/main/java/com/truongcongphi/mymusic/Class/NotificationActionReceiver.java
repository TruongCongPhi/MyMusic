package com.truongcongphi.mymusic.Class;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.truongcongphi.mymusic.Activity.NotificationUtils;
import com.truongcongphi.mymusic.Activity.PlaySongActivity;

public class NotificationActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case NotificationUtils.ACTION_PREVIOUS:
                    // Xử lý sự kiện nhấn nút previous tại đây
                    PlaySongActivity.getInstance().previousSong();
                    break;

                case NotificationUtils.ACTION_PLAY_PAUSE:
                    // Xử lý sự kiện nhấn nút play/pause tại đây
                    PlaySongActivity.getInstance().togglePlayPause();
                    break;
                case NotificationUtils.ACTION_NEXT:
                    // Xử lý sự kiện nhấn nút next tại đây
                    PlaySongActivity.getInstance().nextSong();
                    break;
            }
        }
    }

}
