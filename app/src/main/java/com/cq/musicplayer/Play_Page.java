package com.cq.musicplayer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class Play_Page extends AppCompatActivity {

    private Button button1;
    private Button button2;
    private View button3;
    private static SeekBar seekBar;
    private static player_Service.MyBinder iBinder;
    private String name;

    // 接收更新后的进度条，用来更新音乐进度。
    public static Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle bundle = msg.getData();
            //获取发送过来的数据
            int currentPosition = bundle.getInt("currentPosition");
            int duration = bundle.getInt("duration");

            //为进度条设置当前进度和总进度
            seekBar.setMax(duration);
            seekBar.setProgress(currentPosition);

            //为精度条设置更改事件
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    int progress = seekBar.getProgress();
                    iBinder.callseekTo(progress);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play__page);

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        seekBar = findViewById(R.id.seekbar);

        //获取上个活动传递过来的数据
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        name = bundle.getString("name");

        Intent intent1 = new Intent(this,player_Service.class);  //首先通过startService的方法开启服务，保证该服务在后台长期运行
        startService(intent1);
        bindService(intent1,new MyConnection(),BIND_ABOVE_CLIENT);
    }

    class MyConnection implements ServiceConnection{

        //绑定成功
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onServiceConnected(ComponentName name1, IBinder service) {
            iBinder = (player_Service.MyBinder) service;
            iBinder.callpalyMusic(name);
        }

        //绑定失败
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void myOnclick(View view){
        switch (view.getId()){
            case R.id.button1:
                //调用服务里的播放音乐方法
                iBinder.callpalyMusic(name);
                break;

            case R.id.button2:
                //调用服务里的暂停音乐方法
                iBinder.callpauseMusic();
                break;

            case R.id.button3:
                //调用继续播放方法
                iBinder.callcontinuePlay();
                break;
        }
    }

}