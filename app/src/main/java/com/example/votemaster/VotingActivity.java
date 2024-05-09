package com.example.votemaster;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class VotingActivity extends AppCompatActivity {

    private ImageView backhome;
    private ImageView imageView;
    private FirebaseAuth mAuth;
    private StorageReference storageRef;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private FirebaseUser currentUser;
    private TextView lengthTextView , heightTextview , resultsTextview;
    private Button cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_voting);
        backhome = findViewById(R.id.backhome); // Assuming "backhome" is an ImageView in your layout

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        db = FirebaseFirestore.getInstance();
        imageView = findViewById(R.id.imageViewVote);
        lengthTextView=findViewById(R.id.lengthID);
        heightTextview=findViewById(R.id.heightID);
        cal=findViewById(R.id.calculateID);
        resultsTextview=findViewById(R.id.results);







        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VotingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int L = Integer.parseInt(lengthTextView.getText().toString());
                int H = Integer.parseInt(heightTextview.getText().toString());
                int C = L * H;
                int V = 50;
                int B = C * V;
                int M = 12;
                int X = B / M;
                resultsTextview.setText( "Calculated votes of organization = " + X);
            }
        });



// Create a reference to the Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

// Create a reference to the directory where your images are located
        String directoryPath = "images/"; // Replace with your directory path

        StorageReference directoryRef = storageRef.child(directoryPath);




// List all items (images) in the directory
        directoryRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        List<StorageReference> items = listResult.getItems();

                        // Create a list to hold image metadata
                        List<ImageMetadata> imageMetadataList = new ArrayList<>();

                        for (StorageReference item : items) {
                            item.getMetadata()
                                    .addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                                        @Override
                                        public void onSuccess(StorageMetadata metadata) {
                                            imageMetadataList.add(new ImageMetadata(item, metadata));

                                            // If all metadata is retrieved, sort by creation time
                                            if (imageMetadataList.size() == items.size()) {
                                                Collections.sort(imageMetadataList, new Comparator<ImageMetadata>() {
                                                    @Override
                                                    public int compare(ImageMetadata item1, ImageMetadata item2) {
                                                        return Long.compare(item2.metadata.getCreationTimeMillis(), item1.metadata.getCreationTimeMillis());
                                                    }
                                                });

                                                if (!imageMetadataList.isEmpty()) {
                                                    // Get the download URL for the most recently added image
                                                    StorageReference mostRecentItem = imageMetadataList.get(0).storageReference;
                                                    mostRecentItem.getDownloadUrl()
                                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {
                                                                    String imageUrl = uri.toString();

                                                                    // Load and display the image in your UI using Picasso
                                                                    ImageView imageView = findViewById(R.id.imageViewVote); // Replace with the ID of your ImageView
                                                                    Picasso.get().load(imageUrl).into(imageView);
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception exception) {
                                                                    // Handle any errors that occurred during the download of the most recent image
                                                                }
                                                            });
                                                }
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle any errors that occurred while getting metadata
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors that occurred while listing items in the directory
                    }
                });
    }

    class ImageMetadata {
        StorageReference storageReference;
        StorageMetadata metadata;

        ImageMetadata(StorageReference storageReference, StorageMetadata metadata) {
            this.storageReference = storageReference;
            this.metadata = metadata;
        }
    }









}
