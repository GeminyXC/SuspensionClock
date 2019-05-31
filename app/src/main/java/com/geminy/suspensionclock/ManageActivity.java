package com.geminy.suspensionclock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import utils.CipherUtils;

public class ManageActivity extends AppCompatActivity {

    Button manageButton;
    EditText validDaysEditText;
    EditText registerCodeEditText;
    Button createInviteCodeButton;
    TextView inviteCodeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        manageButton = findViewById(R.id.manageButton);
        validDaysEditText = findViewById(R.id.validDaysEditText);
        registerCodeEditText = findViewById(R.id.registerCodeEditText);
        createInviteCodeButton = findViewById(R.id.createInviteCodeButton);
        inviteCodeTextView = findViewById(R.id.inviteCodeTextView);


        createInviteCodeButton.setOnClickListener(v -> {

            float validDays = Float.parseFloat(validDaysEditText.getText().toString());

            if (validDays < 0 || validDays > 999) {
                Toast.makeText(ManageActivity.this, "请输入正确的有效期", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(registerCodeEditText.getText())) {
                Toast.makeText(ManageActivity.this, "请输入注册码", Toast.LENGTH_SHORT).show();
                return;

            }

            String inviteCode = CipherUtils.registerCodeEncrypt(ManageActivity.this, validDays, registerCodeEditText.getText().toString());

            inviteCodeTextView.setText(inviteCode);

        });


        manageButton.setOnClickListener(v -> {
            finish();
        });

    }
}
