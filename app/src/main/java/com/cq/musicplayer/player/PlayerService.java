package com.cq.musicplayer.player;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.cq.musicplayer.musicApiUtil.model.Song;

/**
 * 播放音乐服务
 */
public class PlayerService extends Service {

    @Override
    public IBinder onBind(Intent intent) {

        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


     public static class MyBinder extends Binder {
        public void play(Song song) {
            MusicPlayer.getPlayer().play(song);
        }
    }
}
