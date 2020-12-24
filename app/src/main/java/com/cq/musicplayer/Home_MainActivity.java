package com.cq.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home_MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment fragmentA;
    private Fragment fragmentB;
    private Fragment FragmentC;
    private Fragment[] fragments;
    private int lastfragment;//用于记录上个选择的Fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFragment();
    }

    private void initFragment() {
        fragmentA = new Fragment_A();
        fragmentB = new Fragment_B();
        FragmentC = new Fragment_C();
        fragments = new Fragment[]{fragmentB, fragmentA, FragmentC};
        lastfragment = 0;
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragment, fragmentB).show(fragmentB).commit();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(changeFragment);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener changeFragment = new BottomNavigationView.OnNavigationItemSelectedListener() {
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
    };


    //切换Fragment
    private void switchFragment(int lastfragment, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastfragment]);//隐藏上个Fragment
        if (fragments[index].isAdded() == false) {
            transaction.add(R.id.frame_fragment, fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();

    }



 /*   public boolean onKeyUp(int keyCode, KeyEvent event) {
        //在主界面，连击连两下退出程序
        if(keyCode==KeyEvent.KEYCODE_BACK){
            long secondTime=System.currentTimeMillis();
            if(secondTime-firstTime>800){
                Toast.makeText(Home_Activity.this,"再按一次返回键退出",Toast.LENGTH_SHORT).show();
                firstTime=secondTime;
                return true;
            }else{
                WelcomeActivity.activity.finish();
                System.exit(0);
            }
        }
        return super.onKeyUp(keyCode, event);
    }*/
}