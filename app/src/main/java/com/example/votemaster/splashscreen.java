package com.example.votemaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class splashscreen extends AppCompatActivity {

    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                openNewActivity();
                finish();
            }
        }, 5000 );
    }

    private void openNewActivity() {
       /* Intent intent = new Intent(splash_screen.this ,getstarted.class);
        startActivity(intent);*/

            Intent intent = new Intent(splashscreen.this,login.class);
            startActivity(intent);
            finish();



    }
}