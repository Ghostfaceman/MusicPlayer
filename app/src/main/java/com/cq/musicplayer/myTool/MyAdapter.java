package com.cq.musicplayer.myTool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cq.musicplayer.MainActivity3;
import com.cq.musicplayer.Play_Page;
import com.cq.musicplayer.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context mContext;
    private List<JavaBean> list;

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
                JavaBean javaBean = list.get(adapterPosition);
                Intent intent = new Intent(mContext, Play_Page.class);
                Bundle bundle = new Bundle();
                bundle.putString("name",javaBean.getName());
                bundle.putString("picture",javaBean.getPicture());
                intent.putExtra("bundle",bundle);
                mContext.startActivity(intent);
            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        JavaBean javaBean = list.get(position);
        holder.textName.setText(javaBean.getName());
        Glide.with(mContext).load(javaBean.getPicture()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textName;
        private ImageView imageView;
        private CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_name);
            imageView = itemView.findViewById(R.id.imageView);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}


