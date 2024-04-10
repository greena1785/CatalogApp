
package com.example.catalogapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.catalogapp.R;
import com.example.catalogapp.model.CatalogModel;
import com.example.catalogapp.user.catalog.CatalogDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.CatalogViewHolder> {

    private final Context context;
    private final List<CatalogModel> list;

    public CatalogAdapter(Context context, List<CatalogModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CatalogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.catalog_item, parent, false);
        return new CatalogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogViewHolder holder, int position) {

        CatalogModel model = list.get(position);

        String price = "$ " + model.getPrice();
        String id = String.valueOf(model.getId());

        holder.catalogName.setText(model.getName());
        holder.catalogDesc.setText(model.getDesc());
        holder.catalogPrice.setText(price);

        try {
            if (model.getImage() != null)
                Picasso.get().load(model.getImage()).placeholder(R.drawable.loading_shape).into(holder.catalogThumbnail);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.container.setOnClickListener(view -> {
            Intent i = new Intent(context , CatalogDetailActivity.class);

            i.putExtra("product_id" ,id);
            i.putExtra("product_name" ,model.getName());
            i.putExtra("product_desc",model.getDesc());
            i.putExtra("product_price",price);
            i.putExtra("product_thumbnail",model.getImage());

            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class CatalogViewHolder extends RecyclerView.ViewHolder {

        public ImageView catalogThumbnail;
        public LinearLayout container;
        public TextView catalogName,catalogDesc,catalogPrice;


        public CatalogViewHolder(@NonNull View itemView) {
            super(itemView);

            container= itemView.findViewById(R.id.catalog_container);
            catalogThumbnail= itemView.findViewById(R.id.catalog_thumbnail);
            catalogName= itemView.findViewById(R.id.catalog_name);
            catalogDesc= itemView.findViewById(R.id.catalog_desc);
            catalogPrice= itemView.findViewById(R.id.catalog_price);
        }
    }
}
