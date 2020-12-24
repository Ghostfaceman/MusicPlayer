package com.cq.musicplayer.Services;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.cq.musicplayer.Activitys.PlayPage_Activity;
import com.cq.musicplayer.JavaBean.Song;
import com.cq.musicplayer.MyUtility.player.MusicPlayer;

import java.util.Timer;
import java.util.TimerTask;

public class player_Service extends Service {

    private MusicPlayer musicPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        //开启服务时，准备一个MediaPlayer用于播 放音乐
        musicPlayer = MusicPlayer.getPlayer();
        super.onCreate();
    }


    public class MyBinder extends Binder{
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void paly(Song song){
            musicPlayer.play(song);
        }

        public void pause(){
            MusicPlayer.getPlayer().pause();
        }

        public void resume(){
            MusicPlayer.getPlayer().resume();
        }

        public void next(){
            MusicPlayer.getPlayer().next();
        }

        public void last(){
            MusicPlayer.getPlayer().last();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}