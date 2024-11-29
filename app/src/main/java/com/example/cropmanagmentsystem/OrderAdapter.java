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

        // Setting text for order details
        holder.tvOrderId.setText("Order ID: " + order.getOrderId());
        holder.tvUserName.setText("Name: " + order.getUserName());
        holder.tvUserNumber.setText("Phone no: " + order.getUserNumber());
        holder.tvDeliveryAddress.setText("Address: " + order.getDeliveryAddress());
        holder.tvTotalAmount.setText("Total amount: ₹" + order.getTotalAmount());
        holder.tvPaymentMethod.setText("Payment mode: " + order.getPaymentMethod());
        holder.tvPaymentStatus.setText("Payment Status: " + order.getPaymentStatus());
        holder.tvOrderDateTime.setText("Order Date: " + new Date(order.getOrderDateTime()).toString());
        holder.tvDeliveryStatus.setText("Delivery Status: " + order.getDeliveryStatus());
        holder.payment_Id.setText("Payment ID: " + order.getPaymentID());

        // Format and display payment date
        String paymentDateTime = order.getPaymentDateTime();  // Get the payment date time

        if ("Pending".equals(paymentDateTime)) {
            // If paymentDateTime is "NA", display it as it is
            holder.tvpaymentDateTime.setText("Payment Date: Pending");
        } else {
            try {
                // If paymentDateTime is not "NA", proceed with parsing it as a timestamp
                long paymentTimestamp = Long.parseLong(paymentDateTime);  // Convert string to long
                Date paymentDate = new Date(paymentTimestamp);  // Create a Date object
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());  // Date format pattern
                String formattedPaymentDate = sdf.format(paymentDate);  // Format the date
                holder.tvpaymentDateTime.setText("Payment Date: " + formattedPaymentDate);  // Set the formatted date
            } catch (NumberFormatException e) {
                Log.e("OrderAdapter", "Error parsing payment date time: " + e.getMessage());
                holder.tvpaymentDateTime.setText("Payment Date: Invalid date");
            }
        }


        // Set up Child RecyclerView for Cart items
        CartItemAdapter cartItemAdapter = new CartItemAdapter(order.getCartItems());
        holder.rvCartItems.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.rvCartItems.setAdapter(cartItemAdapter);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvUserName, tvUserNumber, tvDeliveryAddress, tvTotalAmount, tvPaymentMethod, tvPaymentStatus, tvOrderDateTime, tvDeliveryStatus, payment_Id, tvpaymentDateTime;
        RecyclerView rvCartItems;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize all views
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
            payment_Id = itemView.findViewById(R.id.payment_Id);
            tvpaymentDateTime = itemView.findViewById(R.id.tvpaymentDateTime);
        }
    }
    public void updateOrders(List<Order> newOrders) {
        // Update the orders list
        this.orders = newOrders;
        // Notify the adapter that the data has changed
        notifyDataSetChanged();
    }
}
