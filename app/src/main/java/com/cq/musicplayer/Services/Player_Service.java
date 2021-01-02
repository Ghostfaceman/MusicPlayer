package com.cq.musicplayer.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.cq.musicplayer.Event.PlayEvent;
import com.cq.musicplayer.player.MusicPlayer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class Player_Service extends Service {

    private static final String TAG = "hhhh";
    private MusicPlayer musicPlayer;




    @Override
    public void onCreate() {
        super.onCreate();
        //开启服务时，准备一个MediaPlayer用于播 放音乐
        musicPlayer = MusicPlayer.getPlayer();
        //注册订阅者
        EventBus.getDefault().register(this);
        Log.d(TAG, "服务已开启");


    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
 

    @Subscribe
    public void onEvent(PlayEvent playEvent){
        switch (playEvent.getAction()){
            case PLAY:
                MusicPlayer.getPlayer().setQueue(playEvent.getQueue(),playEvent.getIndex());
            break;
            case STOP:
                MusicPlayer.getPlayer().pause();
                break;
            case RESUME:
                MusicPlayer.getPlayer().resume();
                break;
            case NEXT:
                MusicPlayer.getPlayer().next();
                break;
            case LAST:
                MusicPlayer.getPlayer().last();
                break;
            case SEEK:
                MusicPlayer.getPlayer().setSeek(playEvent.getSeekTo());
                break;
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }





}