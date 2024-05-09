package com.example.votemasteradmin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference ancCollection = db.collection("ANC");
        CollectionReference effCollection = db.collection("EFF");//

        ProgressDialog progressDialog = new ProgressDialog(ResultsActivity.this);
        progressDialog.setMessage("Loading..."); // Set your message
        progressDialog.setCancelable(false); // Prevent the user from dismissing the dialog

        progressDialog.show(); // Show the ProgressDialog

        ancCollection.get();

        ancCollection.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        progressDialog.dismiss();
                        // Create a new TableRow
                        TableRow headerRow = new TableRow(ResultsActivity.this);
                        headerRow.setLayoutParams(new TableRow.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT));



                        // Add the header row to the TableLayout
                        TableLayout tableLayout = findViewById(R.id.table);
                        tableLayout.addView(headerRow);

                        // Loop through the documents and add each "code" value to a new column
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String code = document.getString("code");

                            // Create a new TableRow
                            TableRow newRow = new TableRow(ResultsActivity.this);

                            // Create a TextView for the "code" field
                            TextView codeTextView = new TextView(ResultsActivity.this);
                            codeTextView.setText(code);

                            // Add the "code" TextView to the TableRow
                            newRow.addView(codeTextView);

                            // Add the TableRow to the TableLayout
                            tableLayout.addView(newRow);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                    }
                });

        effCollection.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        ancCollection.get();
                        // Create a new TableRow
                        TableRow headerRow = new TableRow(ResultsActivity.this);
                        headerRow.setLayoutParams(new TableRow.LayoutParams(
                                TableRow.LayoutParams.MATCH_PARENT,
                                TableRow.LayoutParams.WRAP_CONTENT));



                        // Add the header row to the TableLayout
                        TableLayout tableLayout = findViewById(R.id.table2);
                        tableLayout.addView(headerRow);

                        // Loop through the documents and add each "code" value to a new column
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String code = document.getString("code");

                            // Create a new TableRow
                            TableRow newRow = new TableRow(ResultsActivity.this);

                            // Create a TextView for the "code" field
                            TextView codeTextView = new TextView(ResultsActivity.this);
                            codeTextView.setText(code);

                            // Add the "code" TextView to the TableRow
                            newRow.addView(codeTextView);

                            // Add the TableRow to the TableLayout
                            tableLayout.addView(newRow);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                    }
                });

    }
}