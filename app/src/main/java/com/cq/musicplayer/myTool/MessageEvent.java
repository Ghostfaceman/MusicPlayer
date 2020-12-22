package com.cq.musicplayer.myTool;

import com.cq.musicplayer.musicApiUtil.model.Song;

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
