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

public class AdminCartItemAdapter extends RecyclerView.Adapter<AdminCartItemAdapter.AdminCartItemViewHolder> {

    private Context context;
    private List<AdminCartItem> cartItems;

    public AdminCartItemAdapter(Context context, List<AdminCartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public AdminCartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_cart_item, parent, false);
        return new AdminCartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminCartItemViewHolder holder, int position) {
        AdminCartItem cartItem = cartItems.get(position);
        holder.bind(cartItem);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class AdminCartItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView cropImage;
        private TextView cropName, quantity, pricePerKg, totalPrice;

        public AdminCartItemViewHolder(View itemView) {
            super(itemView);
            cropImage = itemView.findViewById(R.id.cropImage);
            cropName = itemView.findViewById(R.id.cropName);
            quantity = itemView.findViewById(R.id.quantity);
            pricePerKg = itemView.findViewById(R.id.pricePerKg);
            totalPrice = itemView.findViewById(R.id.totalPrice);
        }

        public void bind(AdminCartItem cartItem) {
            // Load the image using Glide or Picasso
            Glide.with(context).load(cartItem.getCropImage()).into(cropImage);
            cropName.setText(cartItem.getCropName());
            quantity.setText("Quantity: " + cartItem.getQuantity() + "kg");
            pricePerKg.setText("Price/kg: " + cartItem.getPricePerKg() + "");
            totalPrice.setText("Total Amount: " + cartItem.getTotalPrice());
        }
    }
}
