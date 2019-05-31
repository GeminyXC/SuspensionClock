package com.geminy.suspensionclock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import utils.CipherUtils;
import utils.GeneralUtils;
import utils.GetIMEIUtils;

public class SettingActivity extends AppCompatActivity {

    private static final boolean isShowManage = false;

    Button manageButton;
    TextView registerTextView;
    EditText setttingEditText;
    Button setttingSubmitButton;
    TextView tipsTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //右上角管理按钮
        manageButton = findViewById(R.id.manageButton);
        //邀请码输入框
        setttingEditText = findViewById(R.id.setttingEditText);
        //提交按钮
        setttingSubmitButton = findViewById(R.id.setttingSubmitButton);
        //提示视图
        tipsTextView = findViewById(R.id.tipsTextView);
        //红色注册码视图
        registerTextView = findViewById(R.id.registerTextView);

        if (!isShowManage){
            manageButton.setVisibility(View.GONE);
        }

        //检测是否完成注册或者邀请码未失效
        String inviteCode =  GeneralUtils.readData(SettingActivity.this,"inviteCode");
        boolean isValid = CipherUtils.verifyInviteCode(SettingActivity.this,inviteCode);


        if (isValid) {

            String timeStampString =  CipherUtils.imeiTwiceDecrypt(SettingActivity.this, inviteCode,false);
            String dateString = GeneralUtils.getDateString(SettingActivity.this,timeStampString);

            setttingEditText.setVisibility(View.GONE);
            setttingSubmitButton.setVisibility(View.GONE);
//            tipsTextView.setVisibility(View.GONE);
            tipsTextView.setText("有效期至："+dateString);
            registerTextView.setText("已注册");

        } else {

            String registerString = CipherUtils.imeiOnceEncrypt(SettingActivity.this);
            registerTextView.setText(registerString);

        }


        setttingSubmitButton.setOnClickListener(v -> {

            String inputString = setttingEditText.getText().toString();

            if (TextUtils.isEmpty(inputString)) {
                Toast.makeText(SettingActivity.this, "请输入邀请码", Toast.LENGTH_SHORT).show();
                return;
            }

            String decryptIMEIString = CipherUtils.imeiTwiceDecrypt(SettingActivity.this, inputString,true);
            String imei = GeneralUtils.readData(SettingActivity.this, "imei");


            if (!decryptIMEIString.equals(imei)) {
                Toast.makeText(SettingActivity.this, "邀请码错误", Toast.LENGTH_SHORT).show();
                return;
            }


            GeneralUtils.writeData(SettingActivity.this, "inviteCode", inputString);

            String timeStampString =  CipherUtils.imeiTwiceDecrypt(SettingActivity.this, inputString,false);
            String dateString = GeneralUtils.getDateString(SettingActivity.this,timeStampString);

            setttingEditText.setVisibility(View.GONE);
            setttingSubmitButton.setVisibility(View.GONE);
//            tipsTextView.setVisibility(View.GONE);
            tipsTextView.setText("有效期至："+dateString);
            registerTextView.setText("已注册");


        });


        findViewById(R.id.backButton).setOnClickListener(v -> {

            finish();

        });

        manageButton.setOnClickListener(v->{

            Intent intent = new Intent(SettingActivity.this, ManageActivity.class);
            startActivity(intent);

        });


    }
}
