package com.example.catalogapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.catalogapp.authentication.AdminLogInActivity;
import com.example.catalogapp.authentication.UserLogInActivity;
import com.example.catalogapp.authentication.UserRegisterActivity;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button btnUserReg = findViewById(R.id.btn_user_reg);
        Button btnUserLogIn = findViewById(R.id.btn_user_login);
        Button btnAdminLogIn = findViewById(R.id.btn_admin_login);

        btnUserReg.setOnClickListener(this);
        btnUserLogIn.setOnClickListener(this);
        btnAdminLogIn.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_user_reg:
                startActivity(new Intent(this, UserRegisterActivity.class));
                break;

            case R.id.btn_user_login:
                startActivity(new Intent(this, UserLogInActivity.class));
                break;

            case R.id.btn_admin_login:
                startActivity(new Intent(this, AdminLogInActivity.class));
                break;
        }
    }
}