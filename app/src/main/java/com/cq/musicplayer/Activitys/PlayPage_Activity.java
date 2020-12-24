package com.cq.musicplayer.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.cq.musicplayer.JavaBean.Song;
import com.cq.musicplayer.Event.MessageEvent;
import com.cq.musicplayer.R;
import com.cq.musicplayer.Services.player_Service;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class PlayPage_Activity extends AppCompatActivity {

    private ImageButton  button_Pause;
    private ImageButton  button_Start;
    private static SeekBar seekBar;
    private Bundle bundle;
    private static player_Service.MyBinder iBinder;
    private String name;
    private ObjectAnimator objectAnimator;
    private ImageView imageView;
    private String picture;
    private static ImageView imageView_back;
    private static TextView musicName;
    private String singer;
    private String musci_url;
    private Song song;
    private TextView singerName;

    @SuppressLint("ObjectAnimatorBinding")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_page);

        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //寻找控件
        findView();

        //为需要的控件设置监听
        setOnListener();

        // 注册订阅者
        EventBus.getDefault().register(this);

        //获取上个活动传递过来的数据
        getSongData();

        //为控件赋值
        Glide.with(this).load(picture).into(imageView_back);
        musicName.setText(name);
        singerName.setText(singer);

        //开启并绑定服务
        startServiceAndBind();

        //设置动画
        setAnimation();
    }


    /**
     * 用来接收其他线程发送的信息
     */
    public static Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            if (msg.what == 0){
                int duration = bundle.getInt("duration");
                //为seekBar设置总进度
                seekBar.setMax(duration);
            }
            int currentPosition = bundle.getInt("CurrentPosition");
            //为seekBar设置当前进度。
            seekBar.setProgress(currentPosition);
        }
    };


    /**
     *   开启并绑定服务
     */
    private void startServiceAndBind() {
        Intent intent = new Intent(this, player_Service.class);  //首先通过startService的方法开启服务，保证该服务在后台长期运行
        startService(intent);

        //第二个参数，是一个回调类，当绑定服务后回调该方法。
        bindService(intent, new ServiceConnection() {
                //绑定成功
                @Override @RequiresApi(api = Build.VERSION_CODES.N)
                public void onServiceConnected(ComponentName name1, IBinder service) {
                    iBinder = (player_Service.MyBinder) service;
                    iBinder.callpalyMusic(song);
                }
                //绑定失败
                @Override
                public void onServiceDisconnected(ComponentName name) {
                }}, BIND_ABOVE_CLIENT);
    }

    private void getSongData() {
        Intent intent = getIntent();
        bundle = intent.getBundleExtra("bundle");
        name = bundle.getString("name");
        picture = bundle.getString("picture");
        singer = bundle.getString("singer");
        musci_url = bundle.getString("musci_url");
        song = (Song) bundle.getSerializable("song");
    }

    /**
     * 设置动画
     */
    private void setAnimation() {
        objectAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 720f);
        //旋转不停顿
        objectAnimator.setInterpolator(new LinearInterpolator());
        //设置动画重复次数
        objectAnimator.setRepeatCount(99999);
        //旋转时长
        objectAnimator.setDuration(8000);
        //开始旋转
        objectAnimator.start();
    }


    /**
     *   为需要的控件设置监听
     */
    private void setOnListener() {

        //为seekBar设置回调监听，当进度条改变，就会回调这几个方法
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                iBinder.setProgress(progress);
            }
        });

    }

    /**
     * 当订阅者那边有调用post，就会执行该方法
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeUI(MessageEvent messageEvent) {
        // 更新界面
        Song song = messageEvent.getMessage();
        musicName.setText(song.getName());
        singerName.setText(song.getArtistsname());
        Glide.with(this).load(song.getPicurl()).into(imageView_back);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销订阅者
        EventBus.getDefault().unregister(this);
    }


    /**
     * 找到控件
     */
    private void findView() {
        button_Pause = findViewById(R.id.pause);
        button_Start = findViewById(R.id.start);
        seekBar = findViewById(R.id.seekbar);
        imageView = findViewById(R.id.image_player);
        imageView_back = findViewById(R.id.image_back);
        musicName = findViewById(R.id.musicName);
        singerName = findViewById(R.id.singerName);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void myOnclick(View view){
        switch (view.getId()){
            //音乐开始键
            case R.id.start:
                objectAnimator.resume();
                //让开始键消失
                button_Start.setVisibility(View.GONE);
                //让暂停键显示出来
                button_Pause.setVisibility(View.VISIBLE);
                //调用继续播放方法
                iBinder.callcontinuePlay();
                break;

            //音乐暂停键（刚开始是显示的暂停键）
            case R.id.pause:
                objectAnimator.pause();  //让动画暂停
                //让暂停键消失
                button_Pause.setVisibility(View.GONE);
                //让开始键显示出来
                button_Start.setVisibility(View.VISIBLE);
                //调用服务里的暂停音乐方法
                iBinder.callpauseMusic();
                break;

            //上一首
            case R.id.last:
                objectAnimator.resume();
                //让开始键消失
                button_Start.setVisibility(View.GONE);
                //让暂停键显示出来
                button_Pause.setVisibility(View.VISIBLE);

                iBinder.last_Music(); //播发上一首音乐
                break;

            //下一首
            case R.id.next:
                objectAnimator.resume();
                //让开始键消失
                button_Start.setVisibility(View.GONE);
                //让暂停键显示出来
                button_Pause.setVisibility(View.VISIBLE);

                iBinder.next_Music();  //播发下一首音乐
                break;
        }
    }

}