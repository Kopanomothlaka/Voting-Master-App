package com.example.votemaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class login extends AppCompatActivity {

    EditText etEmail, etPassword;
    TextView etForgetPass, etCreatePass;
    Button logbtn;
    FirebaseAuth auth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail=findViewById(R.id.emailID);
        etPassword=findViewById(R.id.passwordID);
        etForgetPass=findViewById(R.id.forgetpassID);
        etCreatePass=findViewById(R.id.createAccountID);
        logbtn=findViewById(R.id.logInId);

        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        etForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });
        etCreatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2=new Intent(login.this, RegisterActivity.class);
                startActivity(intent2);
            }
        });
        logbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });



    }

    private void loginUser() {
        String email=etEmail.getText().toString();
        String password=etPassword.getText().toString();
        if (email.isEmpty()){
            etEmail.requestFocus();
            etEmail.setError("THis filed cannot be empty");
            return;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.requestFocus();
            etEmail.setError("Invalid Email");

        }else if (password.length()<6) {
            etPassword.requestFocus();
            etPassword.setError("Password must have more than 6 characters");
            return;

        } else if (password.isEmpty()) {
            etPassword.requestFocus();
            etPassword.setError("This filed cannot be empty");
            return;

        }
        else {

            progressDialog.setMessage("Logging in.....");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            auth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        Intent taketomain = new Intent(login.this, MainActivity.class);
                        startActivity(taketomain);
                        finish();
                    }
                    else {

                        if (task.getException() instanceof FirebaseAuthInvalidUserException){
                            Toast.makeText(login.this, "Uer does not exist", Toast.LENGTH_SHORT).show();
                        }
                        else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException
                                && task.getException().getMessage().equals("The password is invalid or the user does not have a password.")) {
                            Toast.makeText(login.this, "Invalid password.",
                                    Toast.LENGTH_SHORT).show();
                        } else if (task.getException() instanceof FirebaseAuthUserCollisionException
                                && ((FirebaseAuthUserCollisionException) task.getException()).getErrorCode().equals("ERROR_EMAIL_ALREADY_IN_USE")) {
                            Toast.makeText(login.this, "Email is already taken.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                    progressDialog.dismiss();
                    
                }
            });



        }



    }
}