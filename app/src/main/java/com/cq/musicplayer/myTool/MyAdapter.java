package com.cq.musicplayer.myTool;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cq.musicplayer.MainActivity3;
import com.cq.musicplayer.Play_Page;
import com.cq.musicplayer.R;
import com.cq.musicplayer.musicApiUtil.model.Song;
import com.cq.musicplayer.player.MusicPlayer;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context mContext;
    private List<Song> list;

    public MyAdapter(List list){
        this.list = list;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {

        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);
        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //myViewHolder.getAdapterPosition()： 获取当前被点击的对象位置
                //使用ViewHolder对象调用getAdapterPosition（）方法可以拿到当前点击的控件位置！！
                int adapterPosition = myViewHolder.getAdapterPosition();
                MusicPlayer.setQueue(list,adapterPosition,false);
                Song song = list.get(adapterPosition);
                Intent intent = new Intent(mContext, Play_Page.class);
                Bundle bundle = new Bundle();
                bundle.putString("name",song.getName());
                bundle.putString("picture",song.getPicurl());
                bundle.putString("singer",song.getArtistsname());
                bundle.putString("musci_url",song.getUrl());
                bundle.putSerializable("song",song);
                intent.putExtra("bundle",bundle);
                mContext.startActivity(intent);
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyAdapter.MyViewHolder holder, int position) {
        Song song = list.get(position);
        holder.musicName.setText(song.getName());
        holder.singerName.setText(song.getArtistsname());
        //Glide.with(mContext).load(song.getPicurl()).into(holder.imageView);

        //设置监听！！
        Glide.with(mContext).load(song.getPicurl()).listener(new RequestListener<Drawable>() {
            @Override //当图片在加载时，会回调该方法
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.textView___.setText("正在正在加载...");
                return false;
            }

            @Override //当该图片加载完成时，会执行该方法
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.textView___.setVisibility(View.INVISIBLE);
                holder.progressBar.setVisibility(View.INVISIBLE);
                return false;
            }
        }).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private CardView cardView;
        private TextView singerName;
        private TextView musicName;
        private ProgressBar progressBar;
        private TextView textView___;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            cardView = itemView.findViewById(R.id.cardView);
            musicName = itemView.findViewById(R.id.text_music_name);
            singerName = itemView.findViewById(R.id.text_singer_name);
            progressBar = itemView.findViewById(R.id.progress);
            textView___ = itemView.findViewById(R.id.text___);
        }
    }
}