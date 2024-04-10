package com.example.catalogapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.catalogapp.R;
import com.example.catalogapp.admin.products.RemoveProductActivity;
import com.example.catalogapp.model.CatalogModel;
import com.example.catalogapp.user.catalog.CartActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder>  {

    private final Context context;
    private final List<CatalogModel> list;

    public ProductAdapter(Context context, List<CatalogModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
        return new ProductAdapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        CatalogModel model = list.get(position);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        holder.itemName.setText(model.getName());

        try {
            if (model.getImage() != null)
                Picasso.get().load(model.getImage()).placeholder(R.drawable.loading_shape).into(holder.productImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.removeItem.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();

            alertDialog.setTitle("Do you want to remover this product?");

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", (dialog, which) -> dialog.dismiss());

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", (dialog, which) -> {
                FirebaseDatabase.getInstance().getReference().child("products").child("products").child(String.valueOf(model.getId())).removeValue();
                Intent i = new Intent(context, RemoveProductActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            });

            alertDialog.show();

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        public ImageView removeItem;
        public TextView itemName;
        public ImageView productImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            removeItem= itemView.findViewById(R.id.delete_item);
            productImage= itemView.findViewById(R.id.product_thumbnail);
            itemName= itemView.findViewById(R.id.delete_item_name);
        }
    }
}
