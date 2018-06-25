package com.example.shubhang.ecommerce.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shubhang.ecommerce.R;
import com.example.shubhang.ecommerce.model.products;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductListThumbnailAdapter extends RecyclerView.Adapter<ProductListThumbnailAdapter.CustomViewHolder> {

    private List<products> dataList;

    public ProductListThumbnailAdapter(List<products> dataList){
        this.dataList = dataList;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        TextView txtTitle,inStock,price;
        private ImageView coverImage;

        CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            inStock = mView.findViewById(R.id.inStock);
            price = mView.findViewById(R.id.price);
            txtTitle = mView.findViewById(R.id.title);
            coverImage = mView.findViewById(R.id.coverImage);
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.product_thumbnail, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        products p  = dataList.get(position);
        holder.inStock.setText(p.isIn_stock() ? "IN STOCK" : "NOT IN STOCK");
        holder.price.setText(p.getCurrency()+String. valueOf(p.getPrice()));
        holder.txtTitle.setText(p.getName().replaceAll("[|?*<\":>+\\[\\]/']", ""));
        Picasso.get().load(p.getImages().get(0)).into(holder.coverImage);
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }
}