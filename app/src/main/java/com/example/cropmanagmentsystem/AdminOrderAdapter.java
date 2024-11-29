package com.example.cropmanagmentsystem;

import android.content.Context;
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
        AdminOrder order = orders.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class AdminOrderViewHolder extends RecyclerView.ViewHolder {
        private TextView orderId, totalAmount, deliveryStatus, paymentMethod,orderdate;
        private TextView orderUserName,orderUserNumber,orderDeliveryAddress,orderPaymentStatus,orderpaymentDateTime,orderpayment_Id;
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

        }

        public void bind(AdminOrder order) {
            orderId.setText("Order ID : " + order.getOrderID()); // Access orderID using getter

            orderUserName.setText("Name : " + order.getUserName());
            orderUserNumber.setText("Number : " + order.getUserNumber());
            orderDeliveryAddress.setText("Delivery Address :  " + order.getDeliveryAddress());
            orderPaymentStatus.setText("payment status : " + order.getPaymentStatus());

            orderpayment_Id.setText("payment ID :  " + order.getPaymentID());


            totalAmount.setText("Total amount : " + order.getTotalAmount());
            deliveryStatus.setText("Delivery Status : " + order.getDeliveryStatus());
            paymentMethod.setText("Payment Method : " + order.getPaymentMethod());
            orderdate.setText("order date : " + new Date(order.getOrderDateTime()).toString());


            String paymentDateTime = order.getPaymentDateTime();

            if ("Pending".equals(paymentDateTime)) {
                // If paymentDateTime is "NA", display it as it is
                orderpaymentDateTime.setText("Payment Date: Pending");
            } else {
                try {
                    // If paymentDateTime is not "NA", proceed with parsing it as a timestamp
                    long paymentTimestamp = Long.parseLong(paymentDateTime);  // Convert string to long
                    Date paymentDate = new Date(paymentTimestamp);  // Create a Date object
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());  // Date format pattern
                    String formattedPaymentDate = sdf.format(paymentDate);  // Format the date
                    orderpaymentDateTime.setText("Payment Date : " + formattedPaymentDate);  // Set the formatted date
                } catch (NumberFormatException e) {
                    Log.e("OrderAdapter", "Error parsing payment date time: " + e.getMessage());
                    orderpaymentDateTime.setText("Payment Date: Invalid date");
                }
            }


            // Set up CartItem RecyclerView
            AdminCartItemAdapter cartItemAdapter = new AdminCartItemAdapter(context, order.getCartItems());
            cartRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            cartRecyclerView.setAdapter(cartItemAdapter);
        }
    }
}
