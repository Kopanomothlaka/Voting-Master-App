package com.example.votemaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class VotingStationActivity extends AppCompatActivity {

    CardView card1, card2;
    ImageView home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting_station);

        card1=findViewById(R.id.gotoVoteEFF);
        card2=findViewById(R.id.gotoVoteANC);
        home=findViewById(R.id.backhome);


        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(VotingStationActivity.this,voteEFFActivity.class);
                startActivity(intent1);
            }
        });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2=new Intent(VotingStationActivity.this, VoteANCActivity.class);
                startActivity(intent2);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3=new Intent(VotingStationActivity.this,MainActivity.class);
                startActivity(intent3);
            }
        });
    }
}