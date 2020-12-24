package com.cq.musicplayer.Activitys;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.cq.musicplayer.R;
import java.util.Timer;
import java.util.TimerTask;

public class Animation_MainActivity extends AppCompatActivity {

    private ImageView image2;
    private ImageView image1;
    private ImageView image3;
    private ImageView image4;
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
        setContentView(R.layout.activity_animation);

        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //找到相应的控件
        findView();

        //给需要设置监听的控件，设置监听。
        setOnListener();

        //设置动画效果
        AnimationSet[] sets = animationSets();

        //开始动画
        startAnimation(sets);

        //一些定时任务
        myTimerTask();

        //隐藏4个执行完毕后的音符
        myHide();
    }


    /**
     * 该方法的主要作用：
     *      给需要设置监听的控件，设置监听。
     */
    private void setOnListener() {

        //点击该按钮，直接跳转到登录界面  ，不用等待
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Animation_MainActivity.this, Login_Activity.class);
                startActivity(intent);
                task3.cancel();
                finish();
            }});

    }

    /**
        开始执行动画
     */
    private void startAnimation(AnimationSet[] sets) {
        image1.startAnimation(sets[0]);
        image2.startAnimation(sets[1]);
        image3.startAnimation(sets[2]);
        image4.startAnimation(sets[3]);
        image5.startAnimation(sets[4]);
    }


    /*
        把需要实现的动画效果，放入集合并返回。
    */
    private AnimationSet[] animationSets(){
        AnimationSet[] sets = new AnimationSet[]{
               new AnimationSet(true),
               new AnimationSet(true),
               new AnimationSet(true),
               new AnimationSet(true),
               new AnimationSet(true),};

        //实现从深到透明（公共的，都使用到了）
        AlphaAnimation aa = new AlphaAnimation(1.0f, 0.0f);
        aa.setDuration(2000); //设置动画执行的时间
        aa.setRepeatCount(0); //设置重复的次数
        aa.setRepeatMode(Animation.START_ON_FIRST_FRAME);//设置动画执行的模式

        //----------------1：
                //实现移动效果
                TranslateAnimation ta1 = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0.41F);
                ta1.setDuration(2000); //设置动画执行的时间

                //把动画效果设置到动画集合里
                sets[0].addAnimation(ta1);
                sets[0].addAnimation(aa);

        //----------------2：
                //实现移动效果
                TranslateAnimation ta2 = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,0 , Animation.RELATIVE_TO_PARENT,-0.42f, Animation.RELATIVE_TO_PARENT,0 , Animation.RELATIVE_TO_PARENT, 0 );
                ta2.setDuration(2000); //设置动画执行的时间

                 //把动画效果设置到动画集合里
                sets[1].addAnimation(ta2);
                sets[1].addAnimation(aa);

        //----------------3：
                //实现移动效果
                 TranslateAnimation ta3 = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0.42f, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0);
                 ta3.setDuration(2000); //设置动画执行的时间

                //把动画效果设置到动画集合里
                sets[2].addAnimation(ta3);
                sets[2].addAnimation(aa);

        //----------------4：
                //实现移动效果
                TranslateAnimation ta4 = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, -0.51F);
                ta4.setDuration(2000); //设置动画执行的时间

                //把动画效果设置到动画集合里
                sets[3].addAnimation(ta4);
                sets[3].addAnimation(aa);

        //----------------5：
               //实现透明效果
                AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f,1f);
                alphaAnimation.setDuration(6000); //设置动画执行的时间
                alphaAnimation.setRepeatCount(1); //设置重复的次数
                alphaAnimation.setRepeatMode(Animation.RESTART);//设置动画执行的模式

                //实现旋转
                RotateAnimation ra1 = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                ra1.setDuration(3000); //设置动画执行的时间
                ra1.setRepeatCount(1); //设置重复的次数
                ra1.setRepeatMode(Animation.REVERSE);//设置动画执行的模式

                //把动画效果设置到动画集合里
                sets[4].addAnimation(alphaAnimation);
                sets[4].addAnimation(ra1);

        return sets;
    }

    //隐藏4个执行完毕后的音符
    private void myHide(){
        image1.setVisibility(View.INVISIBLE);
        image2.setVisibility(View.INVISIBLE);
        image3.setVisibility(View.INVISIBLE);
        image4.setVisibility(View.INVISIBLE);
    }

    /**
     *  找到相应控件
     */
    private void findView(){
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        image4 = findViewById(R.id.image4);
        text_content = findViewById(R.id.text_content);
        image5 = findViewById(R.id.image5);
        imag6 = findViewById(R.id.image6);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        imageButton = findViewById(R.id.imageJump);
    }


    /*
    *   一些定时任务
    * */
    private void myTimerTask(){

        //定时显示本程序名称和版本号
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
            }};

        //定时显示静态logo图标
        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imag6.setVisibility(View.VISIBLE);
                    }
                });
            }};

        //定时跳转到登录界面
        task3 = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(Animation_MainActivity.this, Login_Activity.class);
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