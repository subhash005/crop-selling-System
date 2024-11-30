package com.example.cropmanagmentsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Dialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.AdminOrderViewHolder> {

    private Context context;
    private List<AdminOrder> orders;

    public AdminOrderAdapter(Context context, List<AdminOrder> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_order_item, parent, false);
        return new AdminOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminOrderViewHolder holder, int position) {
        AdminOrder order = orders.get(position); // Ensure `orders` is a valid list of `AdminOrder` objects.
        holder.bind(order); // Passing the `order` object to bind the data
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class AdminOrderViewHolder extends RecyclerView.ViewHolder {
        private TextView orderId, totalAmount, deliveryStatus, paymentMethod, orderdate;
        private TextView orderUserName, orderUserNumber, orderDeliveryAddress, orderPaymentStatus, orderpaymentDateTime, orderpayment_Id;
        private RecyclerView cartRecyclerView;

        public AdminOrderViewHolder(View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            totalAmount = itemView.findViewById(R.id.ordertotalAmount);
            deliveryStatus = itemView.findViewById(R.id.deliveryStatus);
            paymentMethod = itemView.findViewById(R.id.paymentMethod);
            cartRecyclerView = itemView.findViewById(R.id.cartRecyclerView);
            orderdate = itemView.findViewById(R.id.orderdate);

            orderUserName = itemView.findViewById(R.id.orderUserName);
            orderUserNumber = itemView.findViewById(R.id.orderUserNumber);
            orderDeliveryAddress = itemView.findViewById(R.id.orderDeliveryAddress);
            orderPaymentStatus = itemView.findViewById(R.id.orderPaymentStatus);
            orderpaymentDateTime = itemView.findViewById(R.id.orderpaymentDateTime);
            orderpayment_Id = itemView.findViewById(R.id.orderpayment_Id);

            // Set an OnClickListener for the item view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AdminOrder order = orders.get(getAdapterPosition()); // Get the clicked order item
                    showDialog(v.getContext(), order); // Pass the order object to show dialog
                }
            });
        }

        public void bind(AdminOrder order) {
            orderId.setText("Order ID : " + order.getOrderID());
            orderUserName.setText("Name : " + order.getUserName());
            orderUserNumber.setText("Number : " + order.getUserNumber());
            orderDeliveryAddress.setText("Delivery Address : " + order.getDeliveryAddress());
            orderPaymentStatus.setText("Payment Status : " + order.getPaymentStatus());
            orderpayment_Id.setText("Payment ID : " + order.getPaymentID());
            totalAmount.setText("Total Amount : " + order.getTotalAmount());
            deliveryStatus.setText("Delivery Status : " + order.getDeliveryStatus());
            paymentMethod.setText("Payment Method : " + order.getPaymentMethod());
            orderdate.setText("Order Date : " + new Date(order.getOrderDateTime()).toString());

            String paymentDateTime = order.getPaymentDateTime();
            if ("Pending".equals(paymentDateTime)) {
                orderpaymentDateTime.setText("Payment Date: Pending");
            } else {
                try {
                    long paymentTimestamp = Long.parseLong(paymentDateTime);
                    Date paymentDate = new Date(paymentTimestamp);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String formattedPaymentDate = sdf.format(paymentDate);
                    orderpaymentDateTime.setText("Payment Date : " + formattedPaymentDate);
                } catch (NumberFormatException e) {
                    orderpaymentDateTime.setText("Payment Date: Invalid date");
                }
            }

            // Set up CartItem RecyclerView
            AdminCartItemAdapter cartItemAdapter = new AdminCartItemAdapter(context, order.getCartItems());
            cartRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            cartRecyclerView.setAdapter(cartItemAdapter);
        }

        // Method to show dialog when an item is clicked
        private void showDialog(Context context, AdminOrder order) {
            // Create a new dialog
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_order_details); // Inflate the custom dialog layout
            dialog.setCancelable(true); // Allow dismissing the dialog

            // Set data to the dialog views
            TextView dialogOrderId = dialog.findViewById(R.id.orderId);
            TextView dialogUserName = dialog.findViewById(R.id.orderUserName);
            TextView dialogUserNumber = dialog.findViewById(R.id.orderUserNumber);
            TextView dialogDeliveryAddress = dialog.findViewById(R.id.orderDeliveryAddress);
            TextView dialogPaymentStatus = dialog.findViewById(R.id.orderPaymentStatus);
            TextView dialogTotalAmount = dialog.findViewById(R.id.ordertotalAmount);
            TextView dialogDeliveryStatus = dialog.findViewById(R.id.deliveryStatus);
            TextView dialogPaymentMethod = dialog.findViewById(R.id.paymentMethod);
            TextView dialogOrderDate = dialog.findViewById(R.id.orderdate);
            TextView dialogPaymentDateTime = dialog.findViewById(R.id.orderpaymentDateTime);
            TextView dialogPaymentId = dialog.findViewById(R.id.orderpayment_Id);

            // Set the values
            dialogOrderId.setText("Order ID: " + order.getOrderID());
            dialogUserName.setText("User Name: " + order.getUserName());
            dialogUserNumber.setText("User Number: " + order.getUserNumber());
            dialogDeliveryAddress.setText("Delivery Address: " + order.getDeliveryAddress());
            dialogPaymentStatus.setText("Payment Status: " + order.getPaymentStatus());
            dialogTotalAmount.setText("Total Amount: " + order.getTotalAmount());
            dialogDeliveryStatus.setText("Delivery Status: " + order.getDeliveryStatus());
            dialogPaymentMethod.setText("Payment Method: " + order.getPaymentMethod());
            dialogOrderDate.setText("Order Date: " + new Date(order.getOrderDateTime()).toString());

            String paymentDateTime = order.getPaymentDateTime();
            if ("Pending".equals(paymentDateTime)) {
                dialogPaymentDateTime.setText("Payment Date: Pending");
            } else {
                try {
                    long paymentTimestamp = Long.parseLong(paymentDateTime);
                    Date paymentDate = new Date(paymentTimestamp);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String formattedPaymentDate = sdf.format(paymentDate);
                    dialogPaymentDateTime.setText("Payment Date: " + formattedPaymentDate);
                } catch (NumberFormatException e) {
                    dialogPaymentDateTime.setText("Payment Date: Invalid date");
                }
            }
            dialogPaymentId.setText("Payment ID: " + order.getPaymentID());

            // Set up CartItem RecyclerView in the dialog
            AdminCartItemAdapter cartItemAdapter = new AdminCartItemAdapter(context, order.getCartItems());
            RecyclerView cartRecyclerView = dialog.findViewById(R.id.cartRecyclerView);
            cartRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            cartRecyclerView.setAdapter(cartItemAdapter);

            // Show the dialog
            dialog.show();
        }
    }
}
