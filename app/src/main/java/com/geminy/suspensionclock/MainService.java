package com.geminy.suspensionclock;

import android.content.Intent;
import android.app.Service;
import android.content.res.Resources;
import android.os.IBinder;
import android.support.constraint.ConstraintLayout;
import android.util.TypedValue;
import android.view.WindowManager;
import android.util.Log;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.os.SystemClock;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class MainService extends Service {

    private static final String TAG = "MainService";

    ConstraintLayout toucherLayout;

    WindowManager windowManager;
    WindowManager.LayoutParams params;


    android.support.constraint.ConstraintLayout backView0;//悬浮窗背景
    TextView textView0;//时间显示
    ProgressBar progressBar0;//进度条

    Calendar calendar = Calendar.getInstance();



    int millisecond = 0;


    //状态栏高度.
    int statusBarHeight = -1;

    //不与Activity进行绑定.
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.i(TAG,"MainService Created");
        createToucher();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void createToucher()
    {
        //赋值WindowManager&LayoutParam.
        params = new WindowManager.LayoutParams();
        windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        //设置type.系统提示型窗口，一般都在应用程序窗口之上.
        //Android8.0行为变更，对8.0进行适配https://developer.android.google.cn/about/versions/oreo/android-8.0-changes#o-apps
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1)
        {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        //设置效果为背景透明.
        params.format = PixelFormat.RGBA_8888;
        //设置flags.不可聚焦及不可使用按钮对悬浮窗进行操控.
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        //设置窗口初始停靠位置.
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.x = 0;
        params.y = 0;

        //设置悬浮窗口长宽数据.
        params.width = 500;
        params.height = 200;

        LayoutInflater inflater = LayoutInflater.from(getApplication());
        //获取浮动窗口视图所在布局.
        toucherLayout = (ConstraintLayout) inflater.inflate(R.layout.toucherlayout,null);
        //添加toucherlayout
        windowManager.addView(toucherLayout,params);

        Log.i(TAG,"toucherlayout-->left:" + toucherLayout.getLeft());
        Log.i(TAG,"toucherlayout-->right:" + toucherLayout.getRight());
        Log.i(TAG,"toucherlayout-->top:" + toucherLayout.getTop());
        Log.i(TAG,"toucherlayout-->bottom:" + toucherLayout.getBottom());

        //主动计算出当前View的宽高信息.
        toucherLayout.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);

        //用于检测状态栏高度.
        int resourceId = getResources().getIdentifier("status_bar_height","dimen","android");
        if (resourceId > 0)
        {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        Log.i(TAG,"状态栏高度为:" + statusBarHeight);


        backView0 = (android.support.constraint.ConstraintLayout)toucherLayout.findViewById(R.id.backView0);
        backView0.setOnTouchListener((v,event)->{

            params.x = (int) event.getRawX() - 150;
            params.y = (int) event.getRawY() - 150 - statusBarHeight;
            windowManager.updateViewLayout(toucherLayout,params);
            return false;

        });




        textView0 = (TextView)toucherLayout.findViewById(R.id.textView0);
        progressBar0 = (ProgressBar)toucherLayout.findViewById(R.id.progressBar0);




        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                textView0.post(new Runnable(){

                    @Override
                    public void run() {


                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
                        long timeStamp = System.currentTimeMillis();
                        Date date = new Date(timeStamp);
                        textView0.setText(simpleDateFormat.format(date));

                        //获取当前毫秒数
                        //1.时间戳截取后三位即为毫秒数
                        String mill= timeStamp+"";
                        String m= mill.substring(mill.length()-3);

                        //2.时间戳对1000取余即为毫秒数
                        millisecond = new Long((timeStamp % 1000)).intValue();

                        Log.i(TAG,"millisecond--1:" + millisecond);
                        Log.i(TAG,"millisecond--2:" + simpleDateFormat.format(date));
                        Log.i(TAG,"millisecond--3:" + m);
                        progressBar0.setProgress(millisecond);




                    }
                });

            }
        };

        timer.schedule(task,0,1);

    }





}
