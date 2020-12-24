package com.cq.musicplayer;

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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.cq.musicplayer.myTool.PhoneUserBean;
import com.cq.musicplayer.myTool.QQUserBean;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import java.io.Serializable;

public class MainActivity3 extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private AutoCompleteTextView textQuery;
    private NavigationView navigationView;
    private Toolbar toolBar;
    private ImageView userHead;
    private TextView userName;
    private View nav_header;

    private BottomNavigationView bottomNavigationView;
    private Fragment fragmentA;
    private Fragment fragmentB;
    private Fragment FragmentC;
    private Fragment[] fragments;
    private int lastfragment;//用于记录上个选择的Fragment

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        //找到相应控件
        findView();

        //设置通知栏为Toolbar
        setNoticeBar(toolBar);

        //为左边导航栏中的条目设置点击事件
        leftDrawerViewOnclick();

        //获取登录界面传递过来的数据。
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null){
            Serializable serializable = bundle.getSerializable("UserBean");

            if (serializable instanceof QQUserBean){
                QQUserBean qq = (QQUserBean) serializable;
                String nickname = qq.getNickname();
                userName.setText(nickname);
                String src = qq.getFigureurl();
                Glide.with(this).load(src).into(userHead);

            }else if (serializable instanceof PhoneUserBean){
                PhoneUserBean phone = (PhoneUserBean) serializable;
                String phone1 = phone.getPhone();
                userName.setText("手机用户:" + phone1);
            }
        }else{
            //通过游客登录的方式进入的首页
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        initFragment();

    }

    private void initFragment() {
        fragmentA = new Fragment_A();
        fragmentB = new Fragment_B();
        FragmentC = new Fragment_C();
        fragments = new Fragment[]{fragmentA, fragmentB,FragmentC};
        lastfragment = 0;
        //设置底部导航栏默认选中主页！
        bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(1).getItemId());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment,fragmentA).show(fragmentA).commitAllowingStateLoss();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home_page: {
                        if (lastfragment != 0) {
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

    //切换Fragment
    private void switchFragment(int lastfragment, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastfragment]);//隐藏上个Fragment
        if (fragments[index].isAdded() == false) {
            transaction.add(R.id.frame_fragment, fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }


    //为左边导航栏中的条目设置点击事件
    private void leftDrawerViewOnclick() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Toast.makeText(MainActivity3.this, "暂未实现该功能", Toast.LENGTH_SHORT).show();
                //关闭导航栏
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void findView() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolBar = findViewById(R.id.toolbar);
        textQuery = findViewById(R.id.textQuery);
        nav_header = navigationView.getHeaderView(0);
        userHead = nav_header.findViewById(R.id.userHead);
        userName = nav_header.findViewById(R.id.userName);
    }

    private void setNoticeBar(Toolbar toolBar) {
        //把ToolBar设置为通知栏
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);          //让该按钮显示出来显示出来
            actionBar.setHomeAsUpIndicator(R.mipmap.user_center);      //设置该按钮的图片
        }
    }

/*
    private void initial() {
        list.clear();
        int j = 0;
        for (int i = 0; i < Music.musics.length;i++){
            JavaBean javaBean = new JavaBean();
            javaBean.setName(Music.musics[i]);
            if (j+1 < Music.picture.length){
                javaBean.setPicture(Music.picture[++j]);
            }
            list.add(javaBean);
        }
    }
*/

    //刷新用的数据初始化   (自己写的，把自己都整糊涂了，但能保证刷新的前面几个几乎不会重复！！)
    /*private void initial2() {
        list.clear();
        int j = 0;
        int i1 = -1,i2 = -1,i11 = -1,i22 = -1,i111 = -1,i222 = -1;
        Random random = new Random();
        for (int i = 0; i < 40;i++){
            JavaBean javaBean = new JavaBean();
            if (i % 2 == 0){
                i11 = i1;
                i22 = i2;
            }else if (i % 2 == 1){
                i111 = i1;
                i222 = i2;
            }
            i1 = ((j = random.nextInt(Music.musics.length)) != i1 && j != i11 && j != i111) ? j : (j = random.nextInt(Music.musics.length)) != i1 && j != i11 && j != i111 ? j : (j = random.nextInt(Music.musics.length)) != i1 && j != i11 && j != i111 ? j : random.nextInt(Music.musics.length);
            javaBean.setName(Music.musics[i1]);
            i2 = ((j = random.nextInt(Music.picture.length)) != i2 && j != i22 && j != i222) ? j : (j = random.nextInt(Music.picture.length)) != i2 && j != i22 && j != i222 ? j : (j = random.nextInt(Music.musics.length)) != i1 && j != i11 && j != i111 ? j : random.nextInt(Music.picture.length);
            javaBean.setPicture(Music.picture[i2]);
            list.add(javaBean);
        }
    }*/


    //创建Menu
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
                //搜索Music提示模拟数据
                String[] strings = new String[]{"Alan Walker _ Sabrina", "Charlie Puth", "Lemon", "Kelly Clarkson", "从你的全世界路过", "勇气", "告白之夜", "夏至未至", "夜空中最亮的星", "孤单心事", "小幸运 (Live)", "影子习惯", "拾忆", "明天，你好", "溯 (Reverse)", "那个男孩"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity3.this,R.layout.autotext_item,strings);
                textQuery.setAdapter(adapter);
                String s = textQuery.getText().toString();
                for (int i = 0; i < strings.length;i++){
                    if (strings[i].equals(s)){
                        Intent intent = new Intent(MainActivity3.this, Play_Page.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("name",s);
                        intent.putExtra("bundle",bundle);
                        MainActivity3.this.startActivity(intent);
                    }
                }
                textQuery.setText("");
                break;

          /*  case R.id.setting:
                Toast.makeText(this, "你点击了设置按钮", Toast.LENGTH_SHORT).show();
                break;
            case R.id.info:
                Toast.makeText(this, "你点击了信息按钮", Toast.LENGTH_SHORT).show();
                break;*/
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


