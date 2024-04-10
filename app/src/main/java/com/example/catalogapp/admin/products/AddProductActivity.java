package com.example.catalogapp.admin.products;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.catalogapp.R;
import com.example.catalogapp.admin.AdminHomeActivity;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {

    private ImageView bookImageView;

    private EditText bookTitle;
    private EditText author;
    private EditText price;

    private String sAuthor;
    private String sBookTitle;
    private String sPrice;

    private StorageTask<UploadTask.TaskSnapshot> uploadTask;

    private DatabaseReference reference;
    private FirebaseStorage storage;

    private Uri mImageUri;
    String downloadUrl = "";
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        storage = FirebaseStorage.getInstance();

        reference = FirebaseDatabase.getInstance().getReference();

        pd = new ProgressDialog(this);

        MaterialCardView addImage = findViewById(R.id.add_image);
        bookImageView = findViewById(R.id.book_image);


        bookTitle = findViewById(R.id.book_title);
        author = findViewById(R.id.author_name);

        price = findViewById(R.id.price);

        Button uploadNotice = findViewById(R.id.upload_notice_button);
        Button cancel = findViewById(R.id.cancel_upload);

        addImage.setOnClickListener(v -> CropImage.activity().setCropShape(CropImageView.CropShape.RECTANGLE).start(AddProductActivity.this));

        uploadNotice.setOnClickListener(v -> validateData());

        cancel.setOnClickListener(v -> startActivity(new Intent(AddProductActivity.this, AdminHomeActivity.class)));

    }

    private void validateData() {

        sBookTitle = bookTitle.getText().toString();
        sAuthor = author.getText().toString();
        sPrice = price.getText().toString();

        if (sBookTitle.isEmpty()) {
            bookTitle.setError("Required");
            bookTitle.requestFocus();
        } else if (sAuthor.isEmpty()) {
            author.setError("Required");
            author.requestFocus();
        }  else if (sPrice.isEmpty()) {
            price.setError("Required");
            price.requestFocus();
        }
        else if (mImageUri == null) {
            price.setError("Select Image");
            price.requestFocus();
        }
        else {
            uploadBookImage();
//            uploadData();
        }
    }

    private void uploadBookImage() {
        pd.setMessage("Uploading...");
        pd.show();
        String uniqueString = UUID.randomUUID().toString();

        final StorageReference referenceForProfile = storage.getReference().child("products").child(uniqueString+".jpeg");

        uploadTask = referenceForProfile.putFile(mImageUri);
        uploadTask.addOnCompleteListener(AddProductActivity.this, task -> {
            if (task.isSuccessful()) {
                uploadTask.addOnSuccessListener(taskSnapshot -> referenceForProfile.getDownloadUrl().addOnSuccessListener(uri -> {
                    downloadUrl = String.valueOf(uri);
                    uploadData();
                }));
            } else {
                pd.dismiss();
                Toast.makeText(AddProductActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadData() {
        int min = 1000;
        int max = 9999;
        int generatedID = (int)(Math.random()*(max-min+1)+min);

        DatabaseReference dbRef = reference.child("products").child("products");
        final String uniqueKey = dbRef.push().getKey();

        HashMap<String, Object> map = new HashMap<>();
        map.put("id", generatedID);
        map.put("name", sBookTitle);
        map.put("desc", sAuthor);
        map.put("color", "#FFFFFF");
        map.put("price", Integer.parseInt(sPrice));
        map.put("image", downloadUrl);

        assert uniqueKey != null;
        dbRef.child(String.valueOf(generatedID)).setValue(map).addOnSuccessListener(unused -> {
            pd.dismiss();
            Toast.makeText(AddProductActivity.this, "Product Uploaded Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddProductActivity.this, AdminHomeActivity.class));
            finish();
        }).addOnFailureListener(e -> {
            pd.dismiss();
            Toast.makeText(AddProductActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddProductActivity.this, AdminHomeActivity.class));
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            assert result != null;

            mImageUri = result.getUri();
            bookImageView.setImageURI(mImageUri);
            bookImageView.setVisibility(View.VISIBLE);

        } else {
            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
        }
    }
}