package com.example.votemaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText etResetPass;
    Button resetPassBtn;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        etResetPass=findViewById(R.id.resetemailID);
        resetPassBtn=findViewById(R.id.resetPassBtnID);
        auth=FirebaseAuth.getInstance();

        resetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassUser();
            }
        });

    }

    private void resetPassUser() {

        String email=etResetPass.getText().toString();
        if (email.isEmpty()){
            etResetPass.requestFocus();
            etResetPass.setError("THis filed cannot be empty");
            return;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etResetPass.requestFocus();
            etResetPass.setError("Invalid Email");
        }
        else {
            // Update user's password
            auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        // if isSuccessful then done message will be shown
                        // and you can change the password
                        Toast.makeText(ResetPasswordActivity.this,"Email sent",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(ResetPasswordActivity.this, login.class);
                        startActivity(intent);

                    }
                    else {
                        Toast.makeText(ResetPasswordActivity.this,"Error Occurred .Please try again later",Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }
}