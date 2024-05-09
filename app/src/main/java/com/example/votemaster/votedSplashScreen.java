package com.example.votemaster;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class votedSplashScreen extends AppCompatActivity {

    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voted_splash_screen);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                openNewActivity();
                finish();
            }
        }, 1000 );
    }

    private void openNewActivity() {
       /* Intent intent = new Intent(splash_screen.this ,getstarted.class);
        startActivity(intent);*/

        Intent intent = new Intent(votedSplashScreen.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}