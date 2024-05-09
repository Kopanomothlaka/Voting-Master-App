package com.example.votemaster;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class VoteANCActivity extends AppCompatActivity {

    ImageView home;
    Button voteAnc;
    EditText voteEditextAnc;
    private ProgressDialog progressDialog;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_ancactivity);

        home=findViewById(R.id.backhome);
        voteAnc=findViewById(R.id.voteCodeAnc);
        voteEditextAnc=findViewById(R.id.codeEditTextAnc);
        mAuth = FirebaseAuth.getInstance();


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(VoteANCActivity.this,VotingStationActivity.class);
                startActivity(intent1);
            }
        });
        voteAnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userVote();
            }

        });



    }

    private void userVote() {
        String code=voteEditextAnc.getText().toString();

        if (code.length()!=4){
            voteEditextAnc.requestFocus();
            voteEditextAnc.setError("Enter valid code to register !!!!!!!");
            return;
        }
        else{

            String userId = mAuth.getCurrentUser().getUid();
            storeUserDataInFirestore(userId, code);
            Intent intent = new Intent(VoteANCActivity.this,votedSplashScreen.class);
            startActivity(intent);
//            progressDialog.dismiss();


        }

    }

    private void storeUserDataInFirestore(String userId, String code) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("code", code);

        DocumentReference userRef = db.collection("ANC").document(userId);

        userRef.set(userMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Code to run when the operation is successful
                        // Add your code here
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Code to run when the operation fails
                        // Add your code here
                    }
                });



    }
}