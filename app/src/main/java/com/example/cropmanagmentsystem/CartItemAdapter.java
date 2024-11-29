package com.example.cropmanagmentsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;  // Make sure Glide is imported

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {
    private List<CartItem> cartItems;

    public CartItemAdapter(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_cart_item, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.tvCropName.setText(item.getCropName());
        holder.tvQuantity.setText("Quantity: " + item.getQuantity() + " kg");
        holder.tvPricePerKg.setText("Price/kg: " + item.getPricePerKg());
        holder.tvTotalPrice.setText("Total: " + item.getTotalPrice());
        Glide.with(holder.itemView.getContext())
                .load(item.getCropImage())
                .into(holder.imgCrop);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvCropName, tvQuantity, tvPricePerKg, tvTotalPrice;
        ImageView imgCrop;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCropName = itemView.findViewById(R.id.tvCropName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvPricePerKg = itemView.findViewById(R.id.tvPricePerKg);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            imgCrop = itemView.findViewById(R.id.imgCrop);
        }
    }
}