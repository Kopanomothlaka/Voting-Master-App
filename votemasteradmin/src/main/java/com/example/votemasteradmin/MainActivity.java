package com.example.votemasteradmin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int pic_id = 123;

    Button camera_open_id;
    ImageView click_image_id , checktable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camera_open_id = findViewById(R.id.camera_button);
        click_image_id = findViewById(R.id.click_image);
        checktable=findViewById(R.id.checkID);
        checktable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ResultsActivity.class);
                startActivity(intent);
            }
        });

        camera_open_id.setOnClickListener(v -> {
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera_intent, pic_id);
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == pic_id && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            // Define the desired width and height for the larger image
            int desiredWidth = photo.getWidth() * 5; // Increase the width by a factor (e.g., double)
            int desiredHeight = photo.getHeight() * 5; // Increase the height by a factor (e.g., double)

            // Create a larger Bitmap with the desired dimensions
            Bitmap largerBitmap = Bitmap.createScaledBitmap(photo, desiredWidth, desiredHeight, false);

            // Display the larger image
            click_image_id.setImageBitmap(largerBitmap);

            // Save the captured image to Firebase Storage
            saveImageToFirebaseStorage(largerBitmap);
        }
    }

    private void saveImageToFirebaseStorage(Bitmap bitmap) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Create a unique filename for the image (e.g., based on timestamp)
        String filename = "image_" + System.currentTimeMillis() + ".jpg";

        StorageReference imageRef = storageRef.child("images/" + filename);

        // Convert the Bitmap to a byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        // Upload the byte array to Firebase Storage
        UploadTask uploadTask = imageRef.putBytes(data);

        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Image upload is successful, now save metadata to Firestore
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                saveImageMetadataToFirestore(filename, imageUrl);
            });
        }).addOnFailureListener(exception -> {
            // Handle errors during image upload
            Toast.makeText(this, "Image upload failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void saveImageMetadataToFirestore(String filename, String imageUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference imagesCollection = db.collection("images");

        // Create a document with metadata
        Map<String, Object> imageMetadata = new HashMap<>();
        imageMetadata.put("filename", filename);
        imageMetadata.put("url", imageUrl);

        // Add the document to the "images" collection
        imagesCollection.add(imageMetadata)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Image metadata saved to Firestore", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error saving image metadata: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
