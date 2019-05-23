package com.geminy.suspensionclock;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);

        findViewById(R.id.button).setOnClickListener(v -> {


            Log.i(TAG, "editTex:" + editText.getText().getClass());
            if(TextUtils.isEmpty(editText.getText())) return;
            int clickSecond = Integer.valueOf(editText.getText().toString());
            if (clickSecond < 0 || clickSecond > 999) {
                Toast.makeText(MainActivity.this, "时间应在0到999之间", Toast.LENGTH_SHORT).show();
                return;
            }


            //当AndroidSDK>=23及Android版本6.0及以上时，需要获取OVERLAY_PERMISSION.
            //使用canDrawOverlays用于检查，下面为其源码。其中也提醒了需要在manifest文件中添加权限.
            /**
             * Checks if the specified context can draw on top of other apps. As of API
             * level 23, an app cannot draw on top of other apps unless it declares the
             * {@link android.Manifest.permission#SYSTEM_ALERT_WINDOW} permission in its
             * manifest, <em>and</em> the user specifically grants the app this
             * capability. To prompt the user to grant this approval, the app must send an
             * intent with the action
             * {@link android.provider.Settings#ACTION_MANAGE_OVERLAY_PERMISSION}, which
             * causes the system to display a permission management screen.
             *
             */
            if (Settings.canDrawOverlays(MainActivity.this)) {

//                Toast.makeText(MainActivity.this,"已开启Toucher",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, MainService.class);
//                intent.putExtra("clickSecond",clickSecond);
                Bundle bundle=new Bundle();
                bundle.putInt("clickSecond",clickSecond);
                intent.putExtras(bundle);
                startService(intent);
//                finish();
            } else {

                Toast.makeText(MainActivity.this, "需要取得权限以使用悬浮窗", Toast.LENGTH_SHORT).show();
                //若没有权限，提示获取.
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(intent);
//                finish();
            }


        });
    }
}
