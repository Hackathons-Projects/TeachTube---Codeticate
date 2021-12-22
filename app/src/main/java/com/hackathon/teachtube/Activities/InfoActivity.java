package com.hackathon.teachtube.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hackathon.teachtube.R;
import com.hackathon.teachtube.Utils.TinyDB;

public class InfoActivity extends AppCompatActivity {
    public static final String TAG = "InfoActivity";
    private Context context;
    private Toolbar toolbar;
    private TinyDB tinyDB;

   // private TextView developer_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        context = InfoActivity.this;
        tinyDB = new TinyDB(context);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("About Us");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}