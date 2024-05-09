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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText etFullName, etIDnumber ,etEmail,etPassword,etConfirmPass;
    Button registerBTN;
    FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etFullName=findViewById(R.id.fullnameet);
        etIDnumber=findViewById(R.id.idnumber);
        etEmail=findViewById(R.id.email);
        etPassword=findViewById(R.id.password);
        etConfirmPass=findViewById(R.id.confirmpass);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        registerBTN=findViewById(R.id.registerID);

        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }

            private void registerUser() {
                String name=etFullName.getText().toString();
                String id=etIDnumber.getText().toString();
                String email=etEmail.getText().toString();
                String password=etPassword.getText().toString();
                String confirmPassword=etConfirmPass.getText().toString();


                if (name.length()==0){
                    etFullName.requestFocus();
                    etFullName.setError("Enter full name !");
                    return;
                }
                else if (name.matches("[a-zA-Z]")) {
                    etFullName.requestFocus();
                    etFullName.setError("Enter only alphabetical characters");
                    return;

                }

                else if (id.isEmpty()) {
                    etIDnumber.requestFocus();
                    etIDnumber.setError("Enter ID Number");
                    return;
                }


                else if (id.length() != 13) {
                    etIDnumber.requestFocus();
                    etIDnumber.setError("ID Number should have 13 digits");
                    return;
                }


                else if (!id.matches("\\d+")) {
                    etIDnumber.requestFocus();
                    etIDnumber.setError("ID Number should contain only digits");
                    return;
                }
                else if (email.isEmpty()) {
                    etEmail.requestFocus();
                    etEmail.setError("Enter Email");
                    return;
                }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    etEmail.requestFocus();
                    etEmail.setError("Invalid Email");
                    return;
                }

                else if (password.isEmpty() ) {
                    etPassword.requestFocus();
                    etPassword.setError("Field cannot be empty");
                    return;

                } else if (password.length()<6) {
                    etPassword.requestFocus();
                    etPassword.setError("Your password must have more 6 characters");
                    return;

                } else if (!confirmPassword.equals(password)) {
                    etConfirmPass.requestFocus();
                    etConfirmPass.setError("Passwords don't match");

                }
                else {
                    progressDialog.setMessage("Registration in progress");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    mAuth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                   /* users.getReference("lee")
                            .setValue(new UserClass("",""));*/



                            if(task.isSuccessful()){

                                String userId = mAuth.getCurrentUser().getUid();
                                storeUserDataInFirestore(userId, name,id,email);

                                //take to main activity
                                Intent mainActivity = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(mainActivity);
                                finish();
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, "Registration failed please try again ", Toast.LENGTH_SHORT).show();
                            }
                            progressDialog.dismiss();

                        }
                    });


                }



            }
        });



    }

    private void storeUserDataInFirestore(String userId, String name, String id, String email) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a Map to store user data
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("id",id);
        userMap.put("email", email);

        // Reference to the "users" collection in Firestore
        DocumentReference userRef = db.collection("users").document(userId);
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