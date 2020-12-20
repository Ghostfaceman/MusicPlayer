package com.cq.musicplayer.myTool;

public class JavaBean{

    private int id;  //歌曲的Id
    private String name;   //音乐名字
    private String picture;  //背景图片

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
