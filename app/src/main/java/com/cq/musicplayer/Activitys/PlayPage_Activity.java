package com.cq.musicplayer.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cq.musicplayer.Event.PlayEvent;
import com.cq.musicplayer.JavaBean.Song;
import com.cq.musicplayer.Services.Player_Service;
import com.cq.musicplayer.player.ManagedMediaPlayer;
import com.cq.musicplayer.player.MusicPlayer;
import com.cq.musicplayer.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import static com.cq.musicplayer.Event.PlayEvent.Action.LAST;
import static com.cq.musicplayer.Event.PlayEvent.Action.NEXT;
import static com.cq.musicplayer.Event.PlayEvent.Action.PLAY;
import static com.cq.musicplayer.Event.PlayEvent.Action.RESUME;
import static com.cq.musicplayer.Event.PlayEvent.Action.SEEK;
import static com.cq.musicplayer.Event.PlayEvent.Action.STOP;
import static com.cq.musicplayer.player.ManagedMediaPlayer.Status.COMPLETED;
import static com.cq.musicplayer.player.ManagedMediaPlayer.Status.STARTED;

public class PlayPage_Activity extends AppCompatActivity{

    private static final int UPDATE_PROGRESS = 1;
    private static final int AUTO_NEXT = 2;
    private ImageButton  button_Pause;
    private ImageButton  button_Start;
    private SeekBar seekBar;
    private Bundle bundle;
    private ObjectAnimator objectAnimator;
    private ImageView imageView;
    private static ImageView imageView_back;
    private static TextView tv_name;
    private Song song;
    PlayEvent playEvent = new PlayEvent();




    private TextView tv_author;
    private TextView tv_end_time;

    @SuppressLint("ObjectAnimatorBinding")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_page);

        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //寻找控件
        findView();

        //设置进度条监听
        setOnListener();


        //获取上个活动传递过来的数据
        Intent intent = getIntent();
        bundle = intent.getBundleExtra("bundle");
        //拿到队列
        ArrayList<Song> list = (ArrayList<Song>) bundle.getSerializable("songs");
        //当前点击事件的下标
        int index = bundle.getInt("index");



        song = list.get(index);
        String name = song.getName();
        String picture = song.getPicurl();
        String author = song.getArtistsname();


        Glide.with(this).load(picture).into(imageView_back);
        tv_name.setText(name);
        tv_author.setText(author);



        objectAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 720f);
        //旋转不停顿
        objectAnimator.setInterpolator(new LinearInterpolator());
        //设置动画重复次数
        objectAnimator.setRepeatCount(99999);
        //旋转时长
        objectAnimator.setDuration(8000);
        //开始旋转
        objectAnimator.start();

        //播放
        playEvent.setAction(PLAY);
        playEvent.setIdex(index);
        playEvent.setQueue(list);
        EventBus.getDefault().post(playEvent);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    seekBar.post(new Runnable() {
                        @Override
                        public void run() {
                            if(MusicPlayer.getStatus() == STARTED) {
                                if (seekBar.getMax() != MusicPlayer.getPlayer().getDuration())
                                    seekBar.setMax(MusicPlayer.getPlayer().getDuration());
                                seekBar.setProgress(MusicPlayer.getPlayer().getCurrentPosition());
                            }

                        }
                    });
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }

    /**
     * 进度条监听回调
     */
    private void setOnListener() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * 拖动条停止拖动的时候调用
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                playEvent.setAction(SEEK);
                playEvent.setSeekTo(seekBar.getProgress());
                EventBus.getDefault().post(playEvent);
            }
            /**
             * 拖动条开始拖动的时候调用
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if(seekBar.getMax()!=MusicPlayer.getPlayer().getDuration())
                    seekBar.setMax(MusicPlayer.getPlayer().getDuration());
            }
            /**
             * 拖动条进度改变的时候调用
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

            }
        });

    }


    /**
     * 更新UI (如播放上一首 或者 下一首的更新)
     * @param song
     */
    public void onChangeUI(Song song) {
       // 更新界面
       tv_name.setText(song.getName());
       tv_author.setText(song.getArtistsname());
       Glide.with(this).load(song.getPicurl()).into(imageView_back);

        //初始化进度条
        seekBar.setProgress(0);
        if(MusicPlayer.getStatus() == STARTED) {
            //设置最大进度条
            seekBar.setMax(MusicPlayer.getPlayer().getDuration());
        }
}



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void findView() {
        button_Pause = findViewById(R.id.pause);
        button_Start = findViewById(R.id.start);
        seekBar = findViewById(R.id.seekbar);
        imageView = findViewById(R.id.image_player);
        imageView_back = findViewById(R.id.image_back);
        tv_name = findViewById(R.id.musicName);
        tv_author = findViewById(R.id.author);
        tv_end_time = findViewById(R.id.end_time);
    }


    public void myOnclick(View view){
        switch (view.getId()){
            //音乐开始键
            case R.id.start:
                objectAnimator.resume();
                //调用继续播放方法
                //iBinder.resume();
                playEvent.setAction(RESUME);
                EventBus.getDefault().post(playEvent);
                      //让开始键消失
                button_Start.setVisibility(View.GONE);
                     //让暂停键显示出来
                button_Pause.setVisibility(View.VISIBLE);

                break;

            //音乐暂停键（刚开始是显示的暂停键）
            case R.id.pause:
                objectAnimator.pause();  //让动画暂停
                     //调用服务里的暂停音乐方法
                //iBinder.pause();
                playEvent.setAction(STOP);
                EventBus.getDefault().post(playEvent);
                     //让暂停键消失
                button_Pause.setVisibility(View.GONE);
                     //让开始键显示出来
                button_Start.setVisibility(View.VISIBLE);

                break;

            //上一首
            case R.id.last:
                objectAnimator.resume();
                //让开始键消失
                button_Start.setVisibility(View.GONE);
                //让暂停键显示出来
                button_Pause.setVisibility(View.VISIBLE);
                //播放上一首音乐
                playEvent.setAction(LAST);
                EventBus.getDefault().post(playEvent);
                //更新Ui
                onChangeUI(MusicPlayer.getNowPlaying());

                break;

            //下一首
            case R.id.next:
                objectAnimator.resume();
                //让开始键消失
                button_Start.setVisibility(View.GONE);
                //让暂停键显示出来
                button_Pause.setVisibility(View.VISIBLE);

                //播放下一首音乐
                playEvent.setAction(NEXT);
                EventBus.getDefault().post(playEvent);
                //更新Ui
                onChangeUI(MusicPlayer.getNowPlaying());

                break;
        }
    }


}