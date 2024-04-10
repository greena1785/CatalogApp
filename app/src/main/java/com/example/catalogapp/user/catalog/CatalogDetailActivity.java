package com.example.catalogapp.user.catalog;

import static com.example.catalogapp.R.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CatalogDetailActivity extends AppCompatActivity {

    private ImageView bBack, pImage;
    private TextView pName, pPrice, pDesc;
//    private Button bWatchlist;
    private ImageButton bWatchlist;

    private FirebaseUser firebaseUser;

    private String productID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_catalog_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        pImage = findViewById(id.product_image);

        pName = findViewById(id.product_name);
        pDesc = findViewById(id.product_desc);
        pPrice = findViewById(id.product_price);
        bBack = findViewById(id.product_back);
        bWatchlist = findViewById(id.product_add_to_cart);

        Intent intent=getIntent();
        productID = intent.getStringExtra("product_id");
        final String productName = intent.getStringExtra("product_name");
        final String productImage = intent.getStringExtra("product_thumbnail");
        final String productDecs = intent.getStringExtra("product_desc");
        final String productPrice = intent.getStringExtra("product_price");

        SharedPreferences sharedPreferences = this.getSharedPreferences("login", MODE_PRIVATE);
//
//        if (sharedPreferences.getString("isLogin", "false").equals("yes")) {
//            Toast.makeText(this, "Not Logged In", Toast.LENGTH_SHORT).show();
//            bWatchlist.setVisibility(View.GONE);
//        } else {
//            bWatchlist.setVisibility(View.VISIBLE);
//            addToCart();
//        }
//
        bWatchlist.setVisibility(View.VISIBLE);
        addToCart();

        try {
            Picasso.get().load(productImage).placeholder(drawable.loading_shape).into(pImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        pName.setText(productName);
        pDesc.setText(productDecs);
        pPrice.setText(productPrice);

        bBack.setOnClickListener(v -> startActivity(new Intent(this, HomeActivity.class)));
    }

    private void addToCart() {

        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("cart").child(firebaseUser.getUid()).exists()) {
                    FirebaseDatabase.getInstance().getReference().child("cart").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot2) {
                            if (snapshot2.child(productID).exists()) {
                                bWatchlist.setBackgroundResource(drawable.cart_remove_button);
                                bWatchlist.setOnClickListener(view -> {
                                    FirebaseDatabase.getInstance().getReference().child("cart").child(firebaseUser.getUid()).child(productID).removeValue();
//                                    Toast.makeText(CatalogDetailActivity.this, "Book removed from watchlist.", Toast.LENGTH_SHORT).show();
                                });
                            } else {
                                bWatchlist.setBackgroundResource(drawable.cart_add_button);
                                bWatchlist.setOnClickListener(view -> {
                                    FirebaseDatabase.getInstance().getReference().child("cart").child(firebaseUser.getUid()).child(productID).setValue(true);
//                                    Toast.makeText(CatalogDetailActivity.this, "Book added to watchlist.", Toast.LENGTH_SHORT).show();
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    bWatchlist.setBackgroundResource(drawable.cart_add_button);
                    bWatchlist.setOnClickListener(view -> {
                        FirebaseDatabase.getInstance().getReference().child("cart").child(firebaseUser.getUid()).child(productID).setValue(true);
//                        Toast.makeText(CatalogDetailActivity.this, "Book added to watchlist.", Toast.LENGTH_SHORT).show();
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}