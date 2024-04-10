package com.example.catalogapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.catalogapp.R;
import com.example.catalogapp.model.CatalogModel;
import com.example.catalogapp.user.catalog.CartActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final Context context;
    private final List<CatalogModel> list;

    public CartAdapter(Context context, List<CatalogModel> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartAdapter.CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        CatalogModel model = list.get(position);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        holder.itemName.setText(model.getName());

        holder.removeItem.setOnClickListener(v -> {
            FirebaseDatabase.getInstance().getReference().child("cart").child(firebaseUser.getUid()).child(String.valueOf(model.getId())).removeValue();
            context.startActivity(new Intent(context, CartActivity.class));
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        public ImageView removeItem;
        public TextView itemName;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            removeItem= itemView.findViewById(R.id.remove_item);
            itemName= itemView.findViewById(R.id.cart_item_name);
        }
    }
}
