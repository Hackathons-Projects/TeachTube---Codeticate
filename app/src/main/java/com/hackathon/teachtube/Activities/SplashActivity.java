package com.hackathon.teachtube.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.hackathon.teachtube.R;
import com.hackathon.teachtube.Security.LoginActivity;
import com.hackathon.teachtube.Utils.Constants;
import com.hackathon.teachtube.Utils.TinyDB;

public class SplashActivity extends AppCompatActivity {

    private Context context;
    private TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        tinyDB = new TinyDB(context);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (tinyDB.getInt(Constants.LOGIN_FLAG) == 1) {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        },2000);


    }
}