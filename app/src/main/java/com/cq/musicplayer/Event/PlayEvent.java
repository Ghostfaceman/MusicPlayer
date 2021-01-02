package com.cq.musicplayer.Event;


import com.cq.musicplayer.JavaBean.Song;

import java.util.List;

public class PlayEvent {
    //用户行为获取
    public enum Action {
        PLAY, STOP, RESUME, NEXT, LAST, SEEK
    }

    private Action mAction;
    private Song mSong;
    private List<Song> mQueue;
    private int seekTo;
    private int index;

    public int getIndex() {
        return index;
    }

    public void setIdex(int index) {
        this.index = index;
    }

    public Song getSong() {
        return mSong;
    }

    public void setSong(Song song) {
        mSong = song;
    }

    public Action getAction() {
        return mAction;
    }

    public void setAction(Action action) {
        mAction = action;
    }

    /**
     * 获取音乐列表
     * @return
     */
    public List<Song> getQueue() {
        return mQueue;
    }

    public void setQueue(List<Song> queue) {
        mQueue = queue;
    }

    /**
     * 当前音乐进度
     * @return
     */
    public int getSeekTo() {
        return seekTo;
    }

    public void setSeekTo(int seekTo) {
        this.seekTo = seekTo;
    }
}
