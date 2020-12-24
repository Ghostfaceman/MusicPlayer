package com.cq.musicplayer.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cq.musicplayer.Activitys.Login_Activity;
import com.cq.musicplayer.MyUtility.UrlParseJsonUtil;
import com.cq.musicplayer.JavaBean.Song;
import com.cq.musicplayer.Adapter.MyAdapter;
import com.cq.musicplayer.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Fragment_Home extends Fragment {

    private RecyclerView recyclerView;
    private boolean index;
    private MyAdapter myAdapter;
    private List<Song> list = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_home,container,false);
        Bundle bundle = getArguments();
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        recyclerView = view.findViewById(R.id.recyclerView);
        //为RecyclerView设置数据和样式
        GridLayoutManager manager = new GridLayoutManager(container.getContext(),1);
        recyclerView.setLayoutManager(manager);
        initial_netWork(); //给list里面初始化数据
        Timer timer = new Timer();
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (index == true){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myAdapter = new MyAdapter(list);
                            recyclerView.setAdapter(myAdapter);
                            index = false;
                        }
                    });
                }
            }
        };
        timer.schedule(task,0,50);

        //设置刷新的控件的颜色
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        //为刷新控件设置监听
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                 refresh();
            }
        });

        return view;
    }


    private void refresh() {
        if (Login_Activity.isNetworkConnected(getContext())){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //重新初始化一遍
                            initial_netWork();
                            //通知适配器，数据改变了
                            myAdapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                }
            }).start();
        }else{
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //刷新结束，取消显示刷新进度
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getContext(), "网络似乎开了个小差~", Toast.LENGTH_SHORT).show();
        }
    }


    //网络加载歌曲：
    private void initial_netWork() {
        list.clear();
        new Thread(){
            @Override
            public void run() {
                for(int i = 15;i>0;i--) {
                    if (Login_Activity.isNetworkConnected(getContext())){
                        String jsonString = UrlParseJsonUtil.getWebString("https://api.uomg.com/api/rand.music?sort=热歌榜&format=json");
                        if (jsonString != null){
                            Song song = UrlParseJsonUtil.paseJsonObject(jsonString);
                            list.add(song);
                            if (i % 3 == 0){
                                index = true;
                            }
                        }
                    }
                }
            }
        }.start();
    }


}
