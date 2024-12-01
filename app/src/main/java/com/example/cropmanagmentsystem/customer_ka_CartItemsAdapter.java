package com.example.cropmanagmentsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class customer_ka_CartItemsAdapter extends RecyclerView.Adapter<customer_ka_CartItemsAdapter.CartItemViewHolder> {

    private List<CartItem> cartItemList;
    private Context context;

    public customer_ka_CartItemsAdapter(List<CartItem> cartItemList, Context context) {
        this.cartItemList = cartItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_item, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        holder.cropName.setText(cartItem.getCropName());
        holder.quantity.setText("Qty: " + cartItem.getQuantity());
        holder.totalPrice.setText("₹" + cartItem.getTotalPrice());

        // Load the crop image from the URL into the ImageView using Glide
        Glide.with(context)
                .load(cartItem.getCropImage())  // Use cropImage URL
                .placeholder(R.drawable.b) // A default image if the URL is null or invalid
                .into(holder.cropImage);
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CartItemViewHolder extends RecyclerView.ViewHolder {
        TextView cropName, quantity, totalPrice;
        ImageView cropImage;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            cropName = itemView.findViewById(R.id.cropName);
            quantity = itemView.findViewById(R.id.quantity);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            cropImage = itemView.findViewById(R.id.cropImage); // Binding the ImageView
        }
    }
}
