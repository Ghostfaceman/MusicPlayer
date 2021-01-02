package com.cq.musicplayer.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.cq.musicplayer.Fragments.Fragment_Home;
import com.cq.musicplayer.Fragments.Fragment_More;
import com.cq.musicplayer.Fragments.Fragment_Self;
import com.cq.musicplayer.JavaBean.PhoneUserBean;
import com.cq.musicplayer.JavaBean.QQUserBean;
import com.cq.musicplayer.R;
import com.cq.musicplayer.Services.Player_Service;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import java.io.Serializable;

public class Main_MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private AutoCompleteTextView textQuery;
    private NavigationView navigationView;
    private Toolbar toolBar;
    private ImageView userHead;
    private TextView userName;
    private View nav_header;
    private BottomNavigationView bottomNavigationView;
    private Fragment fragment_Home;
    private Fragment fragment_More;
    private Fragment fragment_Self;
    private Fragment[] fragments;
    private int lastfragment;//用于记录上个选择的Fragment
    private Serializable userInfo;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent service = new Intent(getApplicationContext(), Player_Service.class);
        //提前开启服务
        startService(service);

        //找到相应控件
        findView();

        //获取登录界面传递进来的用户信息
        userInfo = getUserInfo();

        //把获取的信息解析后，加载到控件上。
        setUserInfo(userInfo);

        //设置通知栏为Toolbar
        setNoticeBar(toolBar);

        //为需要的控件设置点击事件
        setOnListener();

        //初始化Fragment
        initFragment();


    }


    /**
     *  把获取的信息解析后，加载到控件上。
     * @param userInfo
     */
    private void setUserInfo(Serializable userInfo) {
        if (userInfo != null){
            if (userInfo instanceof QQUserBean){
                QQUserBean qq = (QQUserBean)userInfo;
                userName.setText(qq.getNickname());
                Glide.with(this).load(qq.getFigureurl()).into(userHead);
            }else{
                PhoneUserBean phone = (PhoneUserBean)userInfo;
                userName.setText("手机用户:" + phone.getPhone());
            }
        }
    }

    /**
     *  获取登录界面传递过来的用户信息。
     */
    private Serializable getUserInfo() {
        Serializable serializable = null;
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null){
             serializable = bundle.getSerializable("UserBean");
        }else{
            //通过游客登录的方式进入的首页
        }
        return serializable;
    }


    /**
     *   //为需要的控件设置点击事件
     */
    private void setOnListener() {

        //为左边导航栏中的条目设置点击事件
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Toast.makeText(Main_MainActivity.this, "暂未实现该功能", Toast.LENGTH_SHORT).show();
                //关闭导航栏
                drawerLayout.closeDrawers();
                return true;
            }});

        //为底部的导航栏设置点击事件
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_page: {
                        if (lastfragment != 0) { //如果不是当前界面才进行替换操作
                            switchFragment(lastfragment, 0);
                            lastfragment = 0;
                        }
                        return true;
                    }
                    case R.id.find_more: {
                        if (lastfragment != 1) {
                            switchFragment(lastfragment, 1);
                            lastfragment = 1;
                        }
                        return true;
                    }
                    case R.id.self: {
                        if (lastfragment != 2) {
                            switchFragment(lastfragment, 2);
                            lastfragment = 2;
                        }
                        return true;
                    }
                }
                return false;
            }
        });

    }

    /**
     *   初始化Fragment
     */
    private void initFragment() {
        fragment_Home = new Fragment_Home();
        fragment_More = new Fragment_More();
        fragment_Self = new Fragment_Self();
        fragments = new Fragment[]{fragment_Home, fragment_More, fragment_Self};
        lastfragment = 0;
        //设置底部导航栏默认选中主页！
        bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(1).getItemId());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, fragment_Home).show(fragment_Home).commitAllowingStateLoss();
    }

    /**
     *   替换Fragment
      * @param lastfragment  上一个fragment的下标
     *  @param index         当前需要显示的fragment的下标
     */
    private void switchFragment(int lastfragment, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastfragment]);//隐藏上个Fragment
        if (!fragments[index].isAdded()) {
            transaction.add(R.id.frame_fragment, fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }


    /**
     * 找到控件
     */
    private void findView() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolBar = findViewById(R.id.toolbar);
        textQuery = findViewById(R.id.textQuery);
        nav_header = navigationView.getHeaderView(0);
        userHead = nav_header.findViewById(R.id.userHead);
        userName = nav_header.findViewById(R.id.userName);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    /**
     * 把ToolBar设置为通知栏
     * @param toolBar
     */
    private void setNoticeBar(Toolbar toolBar) {
        //把ToolBar设置为通知栏
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);          //让该按钮显示出来显示出来
            actionBar.setHomeAsUpIndicator(R.mipmap.user_center);      //设置该按钮的图片
        }
    }


    /**
     * 创建Menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolmenu,menu);
        return true;  //为true表示显示出来
    }


    //为菜单设置点击事件
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                //打开个人信息栏
                drawerLayout.openDrawer(GravityCompat.START);
                textQuery.setVisibility(View.INVISIBLE);  //取消搜索文本框
                break;
            case R.id.query:
                textQuery.setVisibility(View.VISIBLE);
                textQuery.setText("");
                break;

            case R.id.set:
                Toast.makeText(this, "你点击了设置按钮", Toast.LENGTH_SHORT).show();
                break;
            case R.id.info:
                Toast.makeText(this, "你点击了信息按钮", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 返回键(按下返回键相当于执行了Home键)
     */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //调用HOME键
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意，这个地方最重要，关于解释，自己google吧
        intent.addCategory(Intent.CATEGORY_HOME);
        this.startActivity(intent);
    }

}


