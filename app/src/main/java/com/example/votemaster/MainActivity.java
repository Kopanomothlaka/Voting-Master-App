package com.example.votemaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest;


public class MainActivity extends AppCompatActivity {

    ImageView etLogOut;
    CardView Card , card2;
    Button registerButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        etLogOut=findViewById(R.id.logoutID);
        Card=findViewById(R.id.gotoID);
        card2=findViewById(R.id.goToVotingStation);



        etLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,login.class);
                startActivity(intent);
                finish();
            }
        });
        Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this, VotingActivity.class);
                startActivity(intent);

            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent votingS=new Intent(MainActivity.this, VotingStationActivity.class);
                startActivity(votingS);
            }
        });




    }


}