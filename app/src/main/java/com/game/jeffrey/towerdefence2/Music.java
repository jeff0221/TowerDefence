package com.game.jeffrey.towerdefence2;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Zonde on 03-10-2016.
 */
public class Music implements MediaPlayer.OnCompletionListener
{
    private MediaPlayer mediaPlayer;
    private boolean isPrepared = false;

    public Music(AssetFileDescriptor assetDescriptor)
    {
        mediaPlayer = new MediaPlayer();
        try
        {
            mediaPlayer.setDataSource(assetDescriptor.getFileDescriptor(),assetDescriptor.getStartOffset(),assetDescriptor.getLength());
            mediaPlayer.prepare();
            isPrepared = true;
            mediaPlayer.setOnCompletionListener(this);
        }
        catch(Exception e)
        {
            throw new RuntimeException("MediaPlayer could not load the music file");
        }
    }
    public void dispose()
    {
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
    }

    public boolean isLooping()
    {
        return mediaPlayer.isLooping();
    }

    public boolean isPlaying()
    {
        return mediaPlayer.isPlaying();
    }
    public boolean isStopped()
    {
        return !isPrepared;
    }
    public void pause()
    {
        if(mediaPlayer.isPlaying()) mediaPlayer.pause();
    }

    public void play()
    {
        if(mediaPlayer.isPlaying()) return;
        try
        {
            synchronized (this)
            {
                if(!isPrepared)
                {
                    mediaPlayer.prepare();
                    isPrepared = true;
                }
                mediaPlayer.start();
            }
        }
        catch(IllegalStateException e)
        {
            Log.d("Class: Music","We could not play() the bloody music!");
            e.printStackTrace();
        }
        catch(IOException e)
        {
            Log.d("Class: Music","We got an IO exception");
            e.printStackTrace();
        }
    }
    public void stop()
    {
        synchronized (this)
        {
            if (!isPrepared) return;
            mediaPlayer.stop();
            isPrepared = false;
        }
    }

    public void setLoop(boolean setAsLoop)
    {
        mediaPlayer.setLooping(setAsLoop);
    }
    public void setVolume(float volume)
    {
        mediaPlayer.setVolume(volume,volume);
    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {
        synchronized (this)
        {
            isPrepared = false;
        }
    }
}
