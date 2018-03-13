package com.example.administrator.lab3;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView textView_current;
    private TextView textView_total;
    private TextView textView_status;
    private Button button_play;
    private Button button_stop;
    private Button button_quit;
    private ImageView imageView_pic;
    private SeekBar seekBar_progess;
    private MusicService ms;
    private IBinder mBinder;
    private ObjectAnimator mObjAnim;
    private static String[] ps;
    private ServiceConnection sc;
    //public SimpleDateFormat time = new SimpleDateFormat("mm:ss");
    public static boolean hasPermission = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView_current = (TextView)findViewById(R.id.textView_current);
        textView_total = (TextView)findViewById(R.id.textView_total);
        textView_status = (TextView)findViewById(R.id.textView_status);
        button_play = (Button)findViewById(R.id.button_play);
        button_quit = (Button)findViewById(R.id.button_quit);
        button_stop = (Button)findViewById(R.id.button_stop);
        seekBar_progess = (SeekBar)findViewById(R.id.seekbar_progress);
        imageView_pic = (ImageView)findViewById(R.id.imageView_pic);
        mObjAnim = new ObjectAnimator();

        ps = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ms = new MusicService();

        seekBar_progess.setProgress(0);

        verifyStrongPermissions(MainActivity.this);

        serviceConnect();
        bindService();

        setButton();
        setSeekBar();
        setAnimate();
        mThread.start();

        Toast.makeText(MainActivity.this,"Create",Toast.LENGTH_SHORT).show();

//        if(mBinder==null)
//            Toast.makeText(MainActivity.this,"isNull",Toast.LENGTH_SHORT).show();
    }


    public void setAnimate()
    {
        mObjAnim = ObjectAnimator.ofFloat(imageView_pic,"rotation", 0, 359);
        mObjAnim.setDuration(20000);
        mObjAnim.setInterpolator(new LinearInterpolator());
        mObjAnim.setRepeatCount(ObjectAnimator.INFINITE);
        mObjAnim.setRepeatMode(ObjectAnimator.RESTART);
        mObjAnim.start();
        mObjAnim.pause();

    }
    public void setSeekBar()
    {
        seekBar_progess.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setProgress(progress);
        }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try
                {
                    int progress = seekBar.getProgress();
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    data.writeInt(progress);
                    mBinder.transact(105,data,reply,0);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    public void serviceConnect()
    {
        sc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.d("service","connected");
                mBinder = service;
                //Toast.makeText(MainActivity.this,"connected",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                sc = null;
                //Toast.makeText(MainActivity.this,"disconnected",Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void bindService()
    {
        Intent intent = new Intent(MainActivity.this,MusicService.class);
        startService(intent);
        bindService(intent,sc, Context.BIND_AUTO_CREATE);
    }

    public void setButton()
    {
        button_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    int code = 101;
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    mBinder.transact(code,data,reply,0);

                    int status = reply.readInt();

                    if(status == 1)
                    {
                        button_play.setText("pause");
                        if(mObjAnim.isPaused())
                            mObjAnim.resume();
                        else
                            mObjAnim.start();
                    }
                    else if(status == 0)
                    {
                        button_play.setText("play");
                        mObjAnim.pause();
                    }
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }
            }
        });

        button_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mObjAnim.end();
                try
                {
                    mObjAnim.end();
                    button_play.setText("play");
                    int code = 102;
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    mBinder.transact(code,data,reply,0);
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }
            }
        });

        button_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    int code = 103;
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    mBinder.transact(code,data,reply,0);
                    unbindService(sc);
                    sc = null;
                    try
                    {
                        MainActivity.this.finish();
                        System.exit(0);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                catch (RemoteException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    final Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch(msg.what)
            {
                case 123:
                    try
                    {
                        Parcel data = Parcel.obtain();
                        Parcel reply = Parcel.obtain();
                        mBinder.transact(104,data,reply,0);

                        int progress[] = new int[]{0,0,0};
                        reply.readIntArray(progress);

                        //Toast.makeText(MainActivity.this,ms.mp.getCurrentPosition(),Toast.LENGTH_SHORT).show();

                        seekBar_progess.setProgress(progress[0]);
                        seekBar_progess.setMax(progress[1]);

                        if(progress[2]==0)
                        {
                            textView_status.setText("Pause");
                            button_play.setText("PLAY");
                            if(mObjAnim.isStarted())
                                mObjAnim.pause();
                        }
                        else if(progress[2]==1)
                        {
                            textView_status.setText("Playing");
                            button_play.setText("PAUSE");
                            if(mObjAnim.isPaused())
                                mObjAnim.start();
                        }


                        textView_current.setText(String.valueOf(progress[0]));
                        textView_total.setText(String.valueOf(progress[1]));

                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };


    Thread mThread = new Thread()
    {
      @Override
      public void run()
      {
          while(true)
          {
              try
              {
                  Thread.sleep(100);
              }
              catch (InterruptedException e)
              {
                  e.printStackTrace();
              }
              if(sc != null && hasPermission == true)
                  mHandler.obtainMessage(123).sendToTarget();
          }
      }
    };



    public static void verifyStrongPermissions(Activity activity)
    {
        try
        {
            int permission = ActivityCompat.checkSelfPermission(activity,"android.permission.READ_EXTERNAL_STORAGE");
            if(permission != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(activity, ps ,1);
            }
            else
            {
                hasPermission = true;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[],int[] grantResults)
    {
        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            //do what you want
        }
        else
        {
            System.exit(0);
        }
        return;
    }


}
