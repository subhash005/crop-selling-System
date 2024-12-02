package com.example.cropmanagmentsystem;

import android.app.AlertDialog;
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
        Log.d("OrderAdapter", "Order ID: " + order.getOrderId()); // Debugging

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
        String paymentDateTime = order.getPaymentDateTime();
        if ("Pending".equals(paymentDateTime)) {
            holder.tvpaymentDateTime.setText("Payment Date: Pending");
        } else {
            try {
                long paymentTimestamp = Long.parseLong(paymentDateTime);
                Date paymentDate = new Date(paymentTimestamp);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String formattedPaymentDate = sdf.format(paymentDate);
                holder.tvpaymentDateTime.setText("Payment Date: " + formattedPaymentDate);
            } catch (NumberFormatException e) {
                Log.e("OrderAdapter", "Error parsing payment date time: " + e.getMessage());
                holder.tvpaymentDateTime.setText("Payment Date: Invalid date");
            }
        }

        // Set up Child RecyclerView for Cart items
        CartItemAdapter cartItemAdapter = new CartItemAdapter(order.getCartItems());
        holder.rvCartItems.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.rvCartItems.setAdapter(cartItemAdapter);

        // Add click listener for dialog functionality
        holder.itemView.setOnClickListener(v -> showOrderDetailsDialog(holder.itemView, order));
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
        this.orders = newOrders;
        notifyDataSetChanged();
    }

    private void showOrderDetailsDialog(View view, Order order) {
        // Inflate the dialog layout
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.customer_dialog_order_details, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        // Initialize dialog views
        TextView dialogOrderId = dialogView.findViewById(R.id.dialogOrderId);
        TextView dialogUserName = dialogView.findViewById(R.id.dialogUserName);
        TextView dialogUserNumber = dialogView.findViewById(R.id.dialogUserNumber);
        TextView dialogDeliveryAddress = dialogView.findViewById(R.id.dialogDeliveryAddress);
        TextView dialogTotalAmount = dialogView.findViewById(R.id.dialogTotalAmount);
        TextView dialogPaymentMethod = dialogView.findViewById(R.id.dialogPaymentMethod);
        TextView dialogPaymentStatus = dialogView.findViewById(R.id.dialogPaymentStatus);
        TextView dialogOrderDateTime = dialogView.findViewById(R.id.dialogOrderDateTime);
        TextView dialogDeliveryStatus = dialogView.findViewById(R.id.dialogDeliveryStatus);
        TextView dialogPaymentId = dialogView.findViewById(R.id.dialogPaymentId);
        RecyclerView dialogRvCartItems = dialogView.findViewById(R.id.dialogRvCartItems);

        // Set dialog data
        dialogOrderId.setText("Order ID: " + order.getOrderId());
        dialogUserName.setText("Name: " + order.getUserName());
        dialogUserNumber.setText("Phone no: " + order.getUserNumber());
        dialogDeliveryAddress.setText("Address: " + order.getDeliveryAddress());
        dialogTotalAmount.setText("Total amount: ₹" + order.getTotalAmount());
        dialogPaymentMethod.setText("Payment mode: " + order.getPaymentMethod());
        dialogPaymentStatus.setText("Payment Status: " + order.getPaymentStatus());
        dialogOrderDateTime.setText("Order Date: " + new Date(order.getOrderDateTime()).toString());
        dialogDeliveryStatus.setText("Delivery Status: " + order.getDeliveryStatus());
        dialogPaymentId.setText("Payment ID: " + order.getPaymentID());

        CartItemAdapter cartItemAdapter = new CartItemAdapter(order.getCartItems());
        dialogRvCartItems.setLayoutManager(new LinearLayoutManager(view.getContext()));
        dialogRvCartItems.setAdapter(cartItemAdapter);

        dialog.show();
    }
}
