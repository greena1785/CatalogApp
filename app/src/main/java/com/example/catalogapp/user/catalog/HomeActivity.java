package com.example.catalogapp.user.catalog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.catalogapp.R;
import com.example.catalogapp.adapter.CatalogAdapter;
import com.example.catalogapp.authentication.UserLogInActivity;
import com.example.catalogapp.model.CatalogModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private CatalogAdapter adapter;
    private ArrayList<CatalogModel> list;
    private FloatingActionButton floatingActionButton;

    private DatabaseReference reference;

    RecyclerView recycler_home_page_catalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        reference = FirebaseDatabase.getInstance().getReference().child("products").child("products");

        recycler_home_page_catalog = findViewById(R.id.recycler_home_page_catalog);
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pull_to_refresh_catalog);

        recycler_home_page_catalog.setHasFixedSize(true);
        recycler_home_page_catalog.setLayoutManager(new LinearLayoutManager(this));

        floatingActionButton = findViewById(R.id.goto_cart_button);

        showNewAvailable();

        pullToRefresh.setOnRefreshListener(() -> {
            showNewAvailable();
            pullToRefresh.setRefreshing(false);
        });

        floatingActionButton.setOnClickListener(view -> {
//            Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, CartActivity.class));
        });

        ImageView logOut = findViewById(R.id.log_out2);

        logOut.setOnClickListener(v -> {

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, UserLogInActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

        });
    }

    private void showNewAvailable() {

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    CatalogModel model = dataSnapshot.getValue(CatalogModel.class);
                    list.add(0, model);
                }



                adapter = new CatalogAdapter(HomeActivity.this, list);
                adapter.notifyDataSetChanged();
                recycler_home_page_catalog.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}