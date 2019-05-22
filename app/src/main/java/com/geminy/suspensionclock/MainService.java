package com.geminy.suspensionclock;

import android.content.Intent;
import android.app.Service;
import android.os.IBinder;
import android.support.constraint.ConstraintLayout;
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
import android.widget.TextView;
import android.os.SystemClock;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class MainService extends Service {

    private static final String TAG = "MainService";

    ConstraintLayout toucherLayout;
    WindowManager.LayoutParams params;
    WindowManager windowManager;

    android.support.constraint.ConstraintLayout backView0;
    Button button0;
    TextView textView0;

    int count = 0;


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
        params.width = 300;
        params.height = 300;

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

        //浮动窗口按钮.
        button0 = (Button) toucherLayout.findViewById(R.id.button0);

        button0.setOnClickListener((v)->{

            long[] hints = new long[2];
            Log.i(TAG,"点击了");
            System.arraycopy(hints,1,hints,0,hints.length -1);
            hints[hints.length -1] = SystemClock.uptimeMillis();
            if (SystemClock.uptimeMillis() - hints[0] >= 700)
            {
                Log.i(TAG,"要执行");
                Toast.makeText(MainService.this,"连续点击两次以退出",Toast.LENGTH_SHORT).show();
            }else
            {
                Log.i(TAG,"即将关闭");
                stopSelf();
            }

        });



        button0.setOnTouchListener((v,event)->{

//            params.x = (int) event.getRawX() - 150;
//            params.y = (int) event.getRawY() - 150 - statusBarHeight;
//            windowManager.updateViewLayout(toucherLayout,params);
            return false;

        });






        textView0 = (TextView)toucherLayout.findViewById(R.id.textView0);



        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                textView0.post(new Runnable(){

                    @Override
                    public void run() {

//                        textView0.setText(count+"");
//                        count ++;

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss:SSS");
                        Date date = new Date(System.currentTimeMillis());
                        textView0.setText(simpleDateFormat.format(date));





                    }
                });

            }
        };

        timer.schedule(task,0,1);

    }

    @Override
    public void onDestroy()
    {
        if (button0 != null)
        {
            windowManager.removeView(toucherLayout);
        }
        super.onDestroy();
    }


}
