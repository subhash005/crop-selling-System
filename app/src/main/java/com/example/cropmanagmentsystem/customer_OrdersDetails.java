package com.example.cropmanagmentsystem;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class customer_OrdersDetails extends Fragment {

    private RecyclerView rvOrders;
    private OrderAdapter orderAdapter;
    private List<Order> ordersList = new ArrayList<>();
    private String currentUserId;
    private Spinner monthSpinner, yearSpinner, paymentStatusSpinner;
    private long selectedDateInMillis = 0; // Default to 0 for no date selected

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_orders_details, container, false);

        // Initialize views
        rvOrders = view.findViewById(R.id.rvOrders);
        monthSpinner = view.findViewById(R.id.monthSpinner);
        yearSpinner = view.findViewById(R.id.yearSpinner);
        paymentStatusSpinner = view.findViewById(R.id.paymentStatusSpinner); // Reference to the new spinner
        Button filterButton = view.findViewById(R.id.filterByDateButton);

        // Set up RecyclerView
        rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get the current user's ID
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Load orders from Firebase
        loadOrdersFromFirebase();

        // Set Spinner Listeners
        monthSpinner.setOnItemSelectedListener(new SpinnerListener());
        yearSpinner.setOnItemSelectedListener(new SpinnerListener());
        paymentStatusSpinner.setOnItemSelectedListener(new SpinnerListener()); // Set listener for payment status spinner

        // Set Filter Button Listener
        filterButton.setOnClickListener(v -> {
            filterOrders(); // Call filter method when button is clicked
        });

        return view;
    }

    private void loadOrdersFromFirebase() {
        FirebaseDatabase.getInstance().getReference("orders")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ordersList.clear();
                        for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                            String orderId = orderSnapshot.getKey();
                            Order order = orderSnapshot.getValue(Order.class);
                            if (order != null && order.getUserId().equals(currentUserId)) {
                                order.setOrderId(orderId);
                                ordersList.add(order);
                            }
                        }

                        filterOrders(); // Filter orders after loading
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(requireContext(), "Failed to load orders.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void filterOrders() {
        String selectedMonth = monthSpinner.getSelectedItem().toString();
        String selectedYear = yearSpinner.getSelectedItem().toString();
        String selectedPaymentStatus = paymentStatusSpinner.getSelectedItem().toString(); // Get selected payment status

        List<Order> filteredOrders = new ArrayList<>();

        for (Order order : ordersList) {
            // Month and Year Filter
            boolean matchesMonth = selectedMonth.equals("All Months") ||
                    getMonthName(Integer.parseInt(order.getOrderMonth()) - 1).equalsIgnoreCase(selectedMonth);
            boolean matchesYear = selectedYear.equals("All Years") || order.getOrderYear().equals(selectedYear);

            // Payment Status Filter
            boolean matchesPaymentStatus = selectedPaymentStatus.equals("Payments") ||
                    selectedPaymentStatus.equalsIgnoreCase(order.getPaymentStatus());

            boolean matchesDate = true;
            if (selectedDateInMillis != 0) {  // Checking if the selected date is not 0 (default value)
                long orderDateTime = order.getOrderDateTime();

                if (orderDateTime != 0) { // Assuming 0 means no date
                    matchesDate = isSameDay(orderDateTime, selectedDateInMillis);
                } else {
                    Log.w("customer_OrdersDetails", "Order missing date field: " + order.getOrderId());
                    matchesDate = false;
                }
            }

            if (matchesMonth && matchesYear && matchesPaymentStatus && matchesDate) {
                filteredOrders.add(order);
            }
        }

        if (orderAdapter == null) {
            orderAdapter = new OrderAdapter(filteredOrders);
            rvOrders.setAdapter(orderAdapter);
        } else {
            orderAdapter.updateOrders(filteredOrders); // Update the orders list and notify adapter
        }
    }

    private String getMonthName(int monthNumber) {
        String[] months = getResources().getStringArray(R.array.months_array);
        return months[monthNumber];
    }

    private boolean isSameDay(long date1Millis, long date2Millis) {
        java.util.Calendar cal1 = java.util.Calendar.getInstance();
        java.util.Calendar cal2 = java.util.Calendar.getInstance();
        cal1.setTimeInMillis(date1Millis);
        cal2.setTimeInMillis(date2Millis);

        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
                cal1.get(java.util.Calendar.MONTH) == cal2.get(java.util.Calendar.MONTH) &&
                cal1.get(java.util.Calendar.DAY_OF_MONTH) == cal2.get(java.util.Calendar.DAY_OF_MONTH);
    }

    private class SpinnerListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            filterOrders(); // Call filter on any spinner selection
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing
        }
    }
}
