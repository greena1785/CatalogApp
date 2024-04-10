package com.example.catalogapp.admin.products;

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

import com.example.catalogapp.R;
import com.example.catalogapp.adapter.CartAdapter;
import com.example.catalogapp.adapter.ProductAdapter;
import com.example.catalogapp.admin.AdminHomeActivity;
import com.example.catalogapp.model.CatalogModel;
import com.example.catalogapp.user.catalog.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RemoveProductActivity extends AppCompatActivity {

    private ProductAdapter adapter;
    private ArrayList<CatalogModel> list;

    private ImageView bBack;
    private TextView noItem;

    private DatabaseReference reference;

    RecyclerView recycler_product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_product);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        reference = FirebaseDatabase.getInstance().getReference();

        noItem = findViewById(R.id.no_item_product);

        recycler_product = findViewById(R.id.product_recycler_view);
        recycler_product.setHasFixedSize(true);
        recycler_product.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new ProductAdapter(this, list);
        recycler_product.setAdapter(adapter);

        showProducts();

        bBack = findViewById(R.id.remove_item_back);

        bBack.setOnClickListener(v -> startActivity(new Intent(this, AdminHomeActivity.class)));

    }


    private void showProducts() {

        final List<String> savedIds = new ArrayList<>();
        final int[] total = {0};

        reference.child("products").child("products").addValueEventListener(new ValueEventListener() {
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