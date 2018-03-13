package com.example.administrator.lab3;

import android.app.Service;
import android.bluetooth.BluetoothClass;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

/**
 * Created by Administrator on 2017/11/22.
 */

public class MusicService extends Service{
    public static MediaPlayer mp = new MediaPlayer();
    public int[] currentPosition;
    public IBinder mBinder = new MyBinder();
    public MusicService()
    {
        try
        {
            mp.setDataSource(Environment.getExternalStorageDirectory() + "/melt.mp3");
            mp.prepare();
            mp.setLooping(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return mBinder;
    }

    public class MyBinder extends Binder {
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException{
            switch (code)
            {
                case 101://play_button
                    if(!mp.isPlaying())
                        mp.start();
                    else
                        mp.pause();

                    if(mp.isPlaying()) reply.writeInt(1);
                    else if(!mp.isPlaying()) reply.writeInt(0);
                    break;
                case 102://stop_button
                    if(mp.isPlaying())
                        mp.pause();
                    mp.seekTo(0);
                    break;
                case 103://quit_button
                    if(mp!=null)
                    {
                        mp.stop();
                        try
                        {
                            mp.prepare();
                            mp.seekTo(0);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 104://refrashed return data
                    currentPosition = new int[]{0,0,0};
                    currentPosition[0] = mp.getCurrentPosition();
                    currentPosition[1] = mp.getDuration();

                    if(mp.isPlaying())
                        currentPosition[2]=1;
                    else if(!mp.isPlaying())
                        currentPosition[2]=0;
                    reply.writeIntArray(currentPosition);
                    break;
                case 105://seek_bar
                    int max = mp.getDuration();
                    int setPosition = data.readInt();

                    if(setPosition>=0 && setPosition<=max)
                    {
                        mp.seekTo(setPosition);
                    }
                    break;
            }
            return super.onTransact(code,data,reply,flags);
        }
    }



}
