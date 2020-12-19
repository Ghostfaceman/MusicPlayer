package com.cq.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cq.musicplayer.myTool.JavaBean;
import com.cq.musicplayer.myTool.Music;
import com.cq.musicplayer.myTool.MyAdapter;
import com.cq.musicplayer.myTool.PhoneUserBean;
import com.cq.musicplayer.myTool.QQUserBean;
import com.google.android.material.navigation.NavigationView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity3 extends AppCompatActivity {

    private static final String TAG = "MainActivity3";
    private List<JavaBean> list = new ArrayList<>();;
    private DrawerLayout drawerLayout;
    private AutoCompleteTextView textQuery;
    private MyAdapter myAdapter;
    private NavigationView navigationView;
    private Toolbar toolBar;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView userHead;
    private TextView userName;

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

        //为RecyclerView设置数据和样式
        GridLayoutManager manager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(manager);
        initial(); //给list里面初始化数据
        myAdapter = new MyAdapter(list);
        recyclerView.setAdapter(myAdapter);

        //获取登录界面传递过来的数据。
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null){
            Serializable serializable = bundle.getSerializable("QQUserBean");
            if (serializable instanceof QQUserBean){
                QQUserBean qq = (QQUserBean) serializable;
                String nickname = qq.getNickname();
                userName.setText(nickname + "");
                String src = qq.getFigureurl();
                Glide.with(this).load(src).into(userHead);
            }else if (serializable instanceof PhoneUserBean){
                PhoneUserBean phone = (PhoneUserBean) serializable;
                String phone1 = phone.getPhone();
                userName.setText(phone1 + "");
            }
        }

        //设置刷新的控件的颜色
        swipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary);
        //为刷新控件设置监听
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    private void refresh() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //重新初始化一遍
                        initial();
                        //通知适配器，数据改变了
                        myAdapter.notifyDataSetChanged();
                        //刷新结束，取消显示刷新进度
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
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
        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        View inflate = LayoutInflater.from(getApplicationContext()).inflate(R.layout.navigation_head, null);
        userHead = inflate.findViewById(R.id.userHead);
        userName = inflate.findViewById(R.id.userName);
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

    private void initial() {
        list.clear();
        Random random = new Random();
        for (int i = 0; i < 50;i++){
            JavaBean javaBean = new JavaBean();
            int randomInt = random.nextInt(Music.musics.length);
            javaBean.setName(Music.musics[randomInt]);
            javaBean.setPicture("https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3252068375,2995823524&fm=26&gp=0.jpg");
            list.add(javaBean);
        }
    }

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
                String[] strings = new String[]{"Alan Walker",
                        "影子习惯",
                        "勇气", "从你的全世界路过",
                        "小幸运",
                        "米津玄師 (よねづ けんし)",
                        "孤单心事",};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity3.this,R.layout.autotext_item,strings);
                textQuery.setAdapter(adapter);
                String s = textQuery.getText().toString();
                for (int i = 0; i < strings.length;i++){
                    if (strings[i].equals(s)){
                        Intent intent = new Intent(MainActivity3.this, Play_Page.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("name",s);
                        intent.putExtra("bundle",bundle);
                        MainActivity3.this.startActivity(intent);
                    }else{
                        Toast.makeText(this, "抱歉，没有搜索到该音乐！~~", Toast.LENGTH_SHORT).show();
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


