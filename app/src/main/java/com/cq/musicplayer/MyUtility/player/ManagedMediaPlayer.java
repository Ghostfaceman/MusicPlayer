package com.cq.musicplayer.MyUtility.player;

import android.media.MediaPlayer;

import java.io.IOException;

/**
 * 加播放音乐的扩展,管理媒体播放器
 */
public class ManagedMediaPlayer extends MediaPlayer implements MediaPlayer.OnCompletionListener {

    //状态枚举
    public enum Status {
        //空闲、初始化、启动、暂停、停止、完成
        IDLE, INITIALIZED, STARTED, PAUSED, STOPPED, COMPLETED
    }

    //播放的状态
    private Status mState;

    //初始化完成的监听
    private OnCompletionListener mOnCompletionListener;

    /**
     * 媒体播放器,默认闲置状态
     */
    public ManagedMediaPlayer() {
        super();
        //媒体播放器初始状态闲置
        mState = Status.IDLE;
        super.setOnCompletionListener(this);
    }

    /**
     * 设置数据源
     * @param path
     * @throws IOException
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws IllegalStateException
     */
    @Override
    public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        super.setDataSource(path);
        mState = Status.INITIALIZED;
    }

    /**
     * 启动
     */
    @Override
    public void start() {
        super.start();
        mState = Status.STARTED;
    }

    /**
     * 设置播放完成监听器
     * @param listener
     */
    @Override
    public void setOnCompletionListener(OnCompletionListener listener) {
        this.mOnCompletionListener = listener;
    }


    /**
     * 完成
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        mState = Status.COMPLETED;
        if (mOnCompletionListener != null) {
            mOnCompletionListener.onCompletion(mp);
        }
    }

    /**
     * 停止播放并刷新状态
     * @throws IllegalStateException
     */
    @Override
    public void stop() throws IllegalStateException {
        super.stop();
        mState = Status.STOPPED;
    }

    /**
     * 暂停播放并刷新状态
     * @throws IllegalStateException
     */
    @Override
    public void pause() throws IllegalStateException {
        super.pause();
        mState = Status.PAUSED;
    }

    /**
     * 获取媒体播放器的状态
     * @return
     */
    public Status getState() {
        return mState;
    }

    /**
     * 设置媒体播放器为完成状态
     * @return
     */
    public boolean isComplete() {
        return mState == Status.COMPLETED;
    }



}
