package com.example.catalogapp.user.catalog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.catalogapp.R;
import com.example.catalogapp.adapter.CartAdapter;
import com.example.catalogapp.model.CatalogModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private CartAdapter adapter;
    private ArrayList<CatalogModel> list;

    private ImageView bBack;
    private TextView pPrice, noItem;
    private Button btnBuy;

    private FirebaseUser firebaseUser;

    private DatabaseReference reference;

    RecyclerView recycler_cart_page_catalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference();

        pPrice = findViewById(R.id.cart_items_price);
        noItem = findViewById(R.id.no_item);

        recycler_cart_page_catalog = findViewById(R.id.cart_recycler_view);
        recycler_cart_page_catalog.setHasFixedSize(true);
        recycler_cart_page_catalog.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new CartAdapter(this, list);
        recycler_cart_page_catalog.setAdapter(adapter);

        showCartItem();

        bBack = findViewById(R.id.cart_back);
        btnBuy = findViewById(R.id.cart_buy);

        bBack.setOnClickListener(v -> startActivity(new Intent(this, HomeActivity.class)));

        btnBuy.setOnClickListener(v -> {
            Toast.makeText(this, "Buying not supported yet!", Toast.LENGTH_SHORT).show();
        });
    }

    private void showCartItem() {

        final List<String> savedIds = new ArrayList<>();
        final int[] total = {0};

        reference.child("cart").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    savedIds.add(dataSnapshot.getKey());
                }

                FirebaseDatabase.getInstance().getReference().child("products").child("products").addValueEventListener(new ValueEventListener() {
                    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            CatalogModel data = dataSnapshot.getValue(CatalogModel.class);

                            for (String id : savedIds) {
                                assert data != null;
                                if (String.valueOf(data.getId()).equals(id)) {
                                    list.add(data);
                                    total[0] += data.getPrice();
                                }
                            }
                        }

                        if (total[0] == 0) {
                            noItem.setVisibility(View.VISIBLE);
                        }
                        pPrice.setText("$" + total[0]);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}