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

public class voteEFFActivity extends AppCompatActivity {

    ImageView home;
    Button vote;
    EditText voteEditext;
    private ProgressDialog progressDialog;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_effactivity);

        home = findViewById(R.id.backhome);
        vote = findViewById(R.id.voteCodeEFF);
        voteEditext=findViewById(R.id.codeEditText);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(voteEFFActivity.this, VotingStationActivity.class);
                startActivity(intent1);
            }
        });

        vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vote();

            }
        });



    }

    private void vote() {

//        progressDialog.setMessage("Registration in progress");
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();

        String code=voteEditext.getText().toString();

        if (code.length()!=4){
            voteEditext.requestFocus();
            voteEditext.setError("Enter valid code to register !!!!!!!");
            return;
        }
        else{

            String userId = mAuth.getCurrentUser().getUid();
            storeUserDataInFirestore(userId, code);
            Intent intent = new Intent(voteEFFActivity.this,votedSplashScreen.class);
            startActivity(intent);
//            progressDialog.dismiss();


        }
    }

    private void storeUserDataInFirestore(String userId, String code) {


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a Map to store user data
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("code", code);


        // Reference to the "users" collection in Firestore
        DocumentReference userRef = db.collection("EFF").document(userId);
        // Set user data in Firestore
        userRef.set(userMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // User data added to Firestore successfully
//                        Toast.makeText(RegisterActivity.this, "Data added", Toast.LENGTH_SHORT).show();
                        // Proceed with further steps, if needed
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
//                        Toast.makeText(RegisterActivity.this, "Failed to store user data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
