package com.geminy.suspensionclock;

import android.content.Intent;
import android.nfc.Tag;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import utils.CipherUtils;
import utils.GeneralUtils;
import utils.GetIMEIUtils;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);

        String presetMillisecondString = GeneralUtils.readData(this, "presetMillisecond");

        editText.setText(presetMillisecondString);


        findViewById(R.id.button).setOnClickListener(v -> {

            //检测是否已获取用户手机的IMEI
            HashMap<String, Object> map = GetIMEIUtils.getIMEI(this);
            boolean isAllowed = Boolean.parseBoolean(map.get("isAllowed").toString());
            if (!isAllowed) {
                Toast.makeText(MainActivity.this, "请授权以获取手机相关信息", Toast.LENGTH_SHORT).show();
                return;
            }

            String encryptText = CipherUtils.imeiTwiceEncrypt(MainActivity.this, "");
            String decryptText = CipherUtils.imeiTwiceDecrypt(MainActivity.this, encryptText);


            //检测用户输入是否为空
            if (TextUtils.isEmpty(editText.getText())) {
                Toast.makeText(MainActivity.this, "请输入预设时间", Toast.LENGTH_SHORT).show();
                return;
            }

            //获取用户输入的预设时间
            int presetMillisecond = Integer.valueOf(editText.getText().toString());

            //检测数据输入是否合法
            if (presetMillisecond < 0 || presetMillisecond > 999) {
                Toast.makeText(MainActivity.this, "时间应在0到999之间", Toast.LENGTH_SHORT).show();
                return;
            }

            //存储用户输入的合法数据
            GeneralUtils.writeData(this, "presetMillisecond", editText.getText().toString());


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

                Intent intent = new Intent(MainActivity.this, MainService.class);
                Bundle bundle = new Bundle();
                bundle.putInt("presetMillisecond", presetMillisecond);
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
