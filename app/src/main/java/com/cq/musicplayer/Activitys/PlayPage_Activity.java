package com.cq.musicplayer.Activitys;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.cq.musicplayer.Event.PlayEvent;
import com.cq.musicplayer.JavaBean.Song;
import com.cq.musicplayer.MyUtility.player.MusicPlayer;
import com.cq.musicplayer.R;
import com.cq.musicplayer.Services.player_Service;

import org.greenrobot.eventbus.EventBus;

import static com.cq.musicplayer.Event.PlayEvent.Action.LAST;
import static com.cq.musicplayer.Event.PlayEvent.Action.NEXT;
import static com.cq.musicplayer.Event.PlayEvent.Action.RESUME;
import static com.cq.musicplayer.Event.PlayEvent.Action.STOP;

public class PlayPage_Activity extends AppCompatActivity {

    private ImageButton  button_Pause;
    private ImageButton  button_Start;
    private SeekBar seekBar;
    private Bundle bundle;
    private String name;
    private ObjectAnimator objectAnimator;
    private ImageView imageView;
    private String picture;
    private static ImageView imageView_back;
    private static TextView textView;
    private Song song;
    private String singer;
    private String musci_url;
    PlayEvent playEvent;

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
        name = bundle.getString("name");
        picture = bundle.getString("picture");
        singer = bundle.getString("singer");
        musci_url = bundle.getString("musci_url");
        song = (Song) bundle.getSerializable("song");
        Glide.with(this).load(picture).into(imageView_back);
        textView.setText(name);

        Intent intent1 = new Intent(this,player_Service.class);  //首先通过startService的方法开启服务，保证该服务在后台长期运行
        startService(intent1);


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
     * 进度条监听回调
     */
    private void setOnListener() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * 拖动条停止拖动的时候调用
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
            /**
             * 拖动条开始拖动的时候调用
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

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
       textView.setText(song.getName());
       Glide.with(this).load(song.getPicurl()).into(imageView_back);
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
        textView = findViewById(R.id.musicName);
    }


    public void myOnclick(View view){
        switch (view.getId()){
            //音乐开始键
            case R.id.start:
                objectAnimator.resume();
                //调用继续播放方法
                //iBinder.resume();
                playEvent.setAction(STOP);
                EventBus.getDefault().post(playEvent);
                //更新Ui
                onChangeUI(MusicPlayer.getNowPlaying());
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
                playEvent.setAction(RESUME);
                EventBus.getDefault().post(playEvent);
                //更新Ui
                onChangeUI(MusicPlayer.getNowPlaying());
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