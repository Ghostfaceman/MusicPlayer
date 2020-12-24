package com.cq.musicplayer.JavaBean;

import java.io.Serializable;

public class Song implements Serializable {
    private String name;//歌曲名.
    private String url; //歌曲下载地址
    private String picurl; //歌曲图片
    private String artistsname; //歌手

    public Song(String name, String url, String picurl, String artistsname) {
        this.name = name;
        this.url = url;
        this.picurl = picurl;
        this.artistsname = artistsname;
    }

    /**
     * 获取歌曲名
     * @return
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取歌曲图片地址
     * @return
     */
    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    /**
     * 获取歌手名
     * @return
     */
    public String getArtistsname() {
        return artistsname;
    }

    public void setArtistsname(String artistsname) {
        this.artistsname = artistsname;
    }
}
