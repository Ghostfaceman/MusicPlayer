package com.cq.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ImageView i2;
    private ImageView i1;
    private ImageView i3;
    private ImageView i4;
    private ImageView image5;
    private ImageView imag6;
    private TextView textView;
    private TextView textView2;
    private ImageButton imageButton;
    private TimerTask task3;
    private TextView text_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //找到相应的控件
        findView();
        //设置动画效果
        AnimationSet[] sets = animationSets();
        //开始动画
        startAnimation(sets);
        //一些定时任务
        myTimerTask();
        //隐藏4个执行完毕后的音符
        myHide();
    }

    //开始动画
    private void startAnimation(AnimationSet[] sets) {
        i1.startAnimation(sets[0]);
        i2.startAnimation(sets[1]);
        i3.startAnimation(sets[2]);
        i4.startAnimation(sets[3]);
        image5.startAnimation(sets[4]);
    }

    //把设置的动画放入集合并返回
    private AnimationSet[] animationSets(){
            AnimationSet[] sets = new AnimationSet[5];

        //1
            AnimationSet set1 =  new AnimationSet(true);
            TranslateAnimation ta1 = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 0.41F);
            ta1.setDuration(2000); //设置动画执行的时间

            AlphaAnimation aa = new AlphaAnimation(1.0f, 0.0f);
            aa.setDuration(2000); //设置动画执行的时间
            aa.setRepeatCount(0); //设置重复的次数
            aa.setRepeatMode(Animation.START_ON_FIRST_FRAME);//设置动画执行的模式

            RotateAnimation ra = new RotateAnimation(0, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            ra.setDuration(1000); //设置动画执行的时间
            ra.setRepeatCount(1); //设置重复的次数
            ra.setRepeatMode(Animation.RELATIVE_TO_SELF);//设置动画执行的模式
            set1.addAnimation(ta1);
            set1.addAnimation(aa);
            set1.addAnimation(ra);

        //2
            AnimationSet set2 = new AnimationSet(true);
            TranslateAnimation ta2 = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,0 ,
                    Animation.RELATIVE_TO_PARENT,-0.42f,
                    Animation.RELATIVE_TO_PARENT,0 ,
                    Animation.RELATIVE_TO_PARENT, 0 );
            ta2.setDuration(2000); //设置动画执行的时间
            set2.addAnimation(ta2);
            set2.addAnimation(aa);
            set2.addAnimation(ra);

        //3
            AnimationSet set3 = new AnimationSet(true);
            TranslateAnimation ta3 = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 0.42f,
                    Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 0);
            ta3.setDuration(2000); //设置动画执行的时间

            set3.addAnimation(ta3);
            set3.addAnimation(aa);
            set3.addAnimation(ra);

        //4
            AnimationSet set4 = new AnimationSet(true);
            TranslateAnimation ta4 = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, -0.51F);
            ta4.setDuration(2000); //设置动画执行的时间

            set4.addAnimation(ta4);
            set4.addAnimation(aa);
            set4.addAnimation(ra);

        //5
            AnimationSet set5 = new AnimationSet(true);
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f,1f);
            alphaAnimation.setDuration(6000); //设置动画执行的时间
            alphaAnimation.setRepeatCount(1); //设置重复的次数
            alphaAnimation.setRepeatMode(Animation.RESTART);//设置动画执行的模式

            RotateAnimation ra1 = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            ra1.setDuration(3000); //设置动画执行的时间
            ra1.setRepeatCount(1); //设置重复的次数
            ra1.setRepeatMode(Animation.REVERSE);//设置动画执行的模式
            set5.addAnimation(alphaAnimation);
            set5.addAnimation(ra1);
        //添加到集合
        sets[0] = set1;
        sets[1] = set2;
        sets[2] = set3;
        sets[3] = set4;
        sets[4] = set5;

        return sets;
    }

    //隐藏4个执行完毕后的音符
    private void myHide(){
        i1.setVisibility(View.INVISIBLE);
        i2.setVisibility(View.INVISIBLE);
        i3.setVisibility(View.INVISIBLE);
        i4.setVisibility(View.INVISIBLE);
    }

    //找到相应控件
    private void findView(){
        i1 = findViewById(R.id.image1);
        i2 = findViewById(R.id.image2);
        i3 = findViewById(R.id.image3);
        i4 = findViewById(R.id.image4);
        text_content = findViewById(R.id.text_content);
        image5 = findViewById(R.id.image5);
        imag6 = findViewById(R.id.image6);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        imageButton = findViewById(R.id.imageJump);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                task3.cancel();
                finish();
            }
        });
    }


    //定时任务
    private void myTimerTask(){

        //显示本程序名称和版本号的定时任务
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setVisibility(View.VISIBLE);
                        textView2.setVisibility(View.VISIBLE);
                        text_content.setVisibility(View.VISIBLE);
                    }
                });
            }
        };

        //显示静态logo图标
        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imag6.setVisibility(View.VISIBLE);
                    }
                });
            }
        };

        //定时跳转到登录界面
        task3 = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };

        //启动定时任务
        Timer timer = new Timer();
        timer.schedule(task,3000);
        timer.schedule(task2,6000);
        timer.schedule(task3,7000);
    }

}