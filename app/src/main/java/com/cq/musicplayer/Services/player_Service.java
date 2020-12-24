package com.cq.musicplayer.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.cq.musicplayer.Event.PlayEvent;
import com.cq.musicplayer.MyUtility.player.MusicPlayer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class player_Service extends Service {

    private MusicPlayer musicPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        //开启服务时，准备一个MediaPlayer用于播 放音乐
        musicPlayer = MusicPlayer.getPlayer();
        //注册订阅者
        EventBus.getDefault().register(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
 

    @Subscribe
    public void onEvent(PlayEvent playEvent){
        switch (playEvent.getAction()){
            case PLAY:
                MusicPlayer.getPlayer().setQueue(playEvent.getQueue(),0);
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