package com.example.cropmanagmentsystem;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orders;

    public OrderAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        Log.d("OrderAdapter", "Order ID: " + order.getOrderId());  // Add this line to debug
        holder.tvOrderId.setText("Order ID: " + order.getOrderId());
        holder.tvUserName.setText("Name: " + order.getUserName());
        holder.tvUserNumber.setText("Phone: " + order.getUserNumber());
        holder.tvDeliveryAddress.setText("Address: " + order.getDeliveryAddress());
        holder.tvTotalAmount.setText("Total: ₹" + order.getTotalAmount());
        holder.tvPaymentMethod.setText("Payment: " + order.getPaymentMethod());
        holder.tvPaymentStatus.setText("Status: " + order.getPaymentStatus());
        holder.tvOrderDateTime.setText("Date: " + new Date(order.getOrderDateTime()).toString());
        holder.tvDeliveryStatus.setText("Delivery: " + order.getDeliveryStatus());

        // Set Child RecyclerView
        CartItemAdapter cartItemAdapter = new CartItemAdapter(order.getCartItems());
        holder.rvCartItems.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.rvCartItems.setAdapter(cartItemAdapter);
    }


    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvUserName, tvUserNumber, tvDeliveryAddress, tvTotalAmount, tvPaymentMethod, tvPaymentStatus, tvOrderDateTime, tvDeliveryStatus;
        RecyclerView rvCartItems;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserNumber = itemView.findViewById(R.id.tvUserNumber);
            tvDeliveryAddress = itemView.findViewById(R.id.tvDeliveryAddress);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvPaymentMethod = itemView.findViewById(R.id.tvPaymentMethod);
            tvPaymentStatus = itemView.findViewById(R.id.tvPaymentStatus);
            tvOrderDateTime = itemView.findViewById(R.id.tvOrderDateTime);
            tvDeliveryStatus = itemView.findViewById(R.id.tvDeliveryStatus);
            rvCartItems = itemView.findViewById(R.id.rvCartItems);
        }
    }
}
