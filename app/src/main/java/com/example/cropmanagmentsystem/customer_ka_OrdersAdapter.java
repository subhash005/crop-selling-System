package com.example.cropmanagmentsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class customer_ka_OrdersAdapter extends RecyclerView.Adapter<customer_ka_OrdersAdapter.OrderViewHolder> {

    private List<Order> ordersList;
    private Context context;

    public customer_ka_OrdersAdapter(List<Order> ordersList, Context context) {
        this.ordersList = ordersList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = ordersList.get(position);
        holder.orderId.setText("Order Id : "+order.getOrderId());
        holder.totalAmount.setText("Total : ₹" + order.getTotalAmount());
        holder.paymentStatus.setText("Payment Status : "+order.getPaymentStatus());
        holder.deliveryStatus.setText("Delivery Status : "+order.getDeliveryStatus());

        // Set up nested RecyclerView for cart items
        customer_ka_CartItemsAdapter cartItemsAdapter = new customer_ka_CartItemsAdapter(order.getCartItems(), context);
        holder.cartItemsRecyclerView.setAdapter(cartItemsAdapter);
        holder.cartItemsRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        // Set click listener for the copy icon
        holder.copyIcon.setOnClickListener(v -> {
            // Get the order ID text
            String orderId = order.getOrderId();

            // Copy to clipboard
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Order ID", orderId);
            clipboard.setPrimaryClip(clip);

            // Show a Toast to notify the user
            Toast.makeText(context, "Order ID copied to clipboard", Toast.LENGTH_SHORT).show();
        });
    }


    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, totalAmount, paymentStatus, deliveryStatus;
        RecyclerView cartItemsRecyclerView;
        ImageView copyIcon;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            totalAmount = itemView.findViewById(R.id.totalAmount);
            paymentStatus = itemView.findViewById(R.id.paymentStatus);
            deliveryStatus = itemView.findViewById(R.id.deliveryStatus);
            cartItemsRecyclerView = itemView.findViewById(R.id.cartItemsRecyclerView);
            copyIcon = itemView.findViewById(R.id.copyIcon);  // Reference the copy icon
        }
    }

}