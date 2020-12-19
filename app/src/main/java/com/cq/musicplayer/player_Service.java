package com.cq.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class player_Service extends Service {
    private static final String TAG = "player_Service";
    private MediaPlayer mediaPlayer = null;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        //开启服务时，准备一个MediaPlayer用于播放音乐
        mediaPlayer = new MediaPlayer();
        super.onCreate();
    }



    public class MyBinder extends Binder{
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void callpalyMusic(String musicname){
            palyMusic(musicname);
            Log.d(TAG, "callpalyMusic: " +"11111111111");
        }

        public void callpauseMusic(){
            pauseMusic();
        }

        public void callcontinuePlay(){
            continuePlay();
        }

        public void callseekTo(int position) {
            seekTo(position);
        }
    }



    //播放音乐的方法
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void palyMusic(String musicSrc){
            //每次开启音乐时，重置一下，避免多重BGM.....
            mediaPlayer.reset();
                try {
                    //设置资源
                    Toast.makeText(this, musicSrc, Toast.LENGTH_SHORT).show();
                    AssetFileDescriptor assetFileDescriptor = getAssets().openFd(musicSrc + ".mp3");
                    mediaPlayer.setDataSource(assetFileDescriptor);
                    //准备启动音乐
                    mediaPlayer.prepare();
                    //开始播放
                    mediaPlayer.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }

        //获取音乐的当前播放位置，和音乐总时长,1毫秒获取一次。然后把获取的信息发送给UI线程，对其处理
        final Timer timer = new Timer();
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                int currentPosition = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();

                //让进度条跟播放音乐的进度同步，通过Handler发送信息给UI线程，让UI线程更新Seekbar
                Message message = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putInt("duration",duration);
                bundle.putInt("currentPosition",currentPosition);
                message.setData(bundle);

                //发送信息
                Play_Page.handler.sendMessage(message);
            }
        };
        timer.schedule(task,0,100);

        //为MediaPlayer设置完成监听，完成后关闭定时任务的执行
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer){
                task.cancel();
                timer.cancel();
            }
        });

    }

    public void continuePlay(){
        //当音乐处于暂停状态时，再次调用start（）方法会继续播放
        mediaPlayer.start();
    }

    public void pauseMusic(){
        //暂停音乐
        mediaPlayer.pause();
    }

    //设置自定义播放位置
    public void seekTo(int position){
        mediaPlayer.seekTo(position);
    }

}