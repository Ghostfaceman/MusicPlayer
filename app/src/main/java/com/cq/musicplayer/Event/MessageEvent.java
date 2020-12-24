package com.cq.musicplayer.Event;

import com.cq.musicplayer.JavaBean.Song;

/**
 * 该类作为Event订阅者参数使用
 */
public class MessageEvent {
    private Song song;

    public MessageEvent(Song song) {
        this.song = song;
    }

    public Song getMessage() {
        return song;
    }

    public void setMessage(Song song) {
        this.song = song;
    }
}
