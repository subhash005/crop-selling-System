package com.example.cropmanagmentsystem;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.Collections;
import java.util.List;

public class customer_OrdersDetails extends Fragment {

    private RecyclerView rvOrders;
    private Spinner spinnerSort;
    private OrderAdapter orderAdapter;
    private List<Order> ordersList = new ArrayList<>();
    private List<Order> filteredOrdersList = new ArrayList<>();  // List to hold filtered orders
    private String currentUserId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_orders_details, container, false);

        // Initialize views
        spinnerSort = view.findViewById(R.id.spinnerSort);
        rvOrders = view.findViewById(R.id.rvOrders);

        // Set up RecyclerView
        rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get the current user's ID
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Load orders from Firebase
        loadOrdersFromFirebase();

        // Set up Spinner adapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.sort_options_admin,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(adapter);

        // Handle Spinner item selection
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // "All Orders"
                        displayAllOrders();
                        break;
                    case 1: // "Highest Total Amount"
                        sortByHighestTotalAmount();
                        break;
                    case 2: // "Newest First"
                        sortByNewestFirst();
                        break;
                    case 3: // "Paid Orders"
                        displayPaidOrders();
                        break;
                    case 4: // "Not Paid Orders"
                        displayNotPaidOrders();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Set default selection to "All Orders"
        spinnerSort.setSelection(0);

        return view; // Return the root view
    }

    private void loadOrdersFromFirebase() {
        FirebaseDatabase.getInstance().getReference("orders")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ordersList.clear();
                        for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                            // Get orderId from snapshot key (orderSnapshot.getKey())
                            String orderId = orderSnapshot.getKey();

                            // Get the Order object from the snapshot
                            Order order = orderSnapshot.getValue(Order.class);
                            if (order != null) {
                                // Set the orderId in the Order object
                                order.setOrderId(orderId);
                                Log.d("Orders", "Order Date Time: " + order.getOrderDateTime());
                                Log.d("OrderAdapter", "Payment Date Time (raw): " + order.getPaymentDateTime());  // Log the raw value

                                // Optionally check for userId if you want to filter
                                if (order.getUserId().equals(currentUserId)) {
                                    ordersList.add(order);
                                }
                            }
                        }
                        filteredOrdersList.addAll(ordersList);  // Initially, all orders are shown

                        // Initialize the adapter only after data is loaded
                        if (orderAdapter == null) {
                            orderAdapter = new OrderAdapter(filteredOrdersList);
                            rvOrders.setAdapter(orderAdapter);
                        } else {
                            orderAdapter.notifyDataSetChanged();  // Notify adapter if already set
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(requireContext(), "Failed to load orders.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayAllOrders() {
        filteredOrdersList.clear();
        filteredOrdersList.addAll(ordersList);  // Restore the full list

        // Ensure the adapter is set before notifying
        if (orderAdapter != null) {
            orderAdapter.notifyDataSetChanged();
        }
    }

    private void sortByHighestTotalAmount() {
        Collections.sort(filteredOrdersList, (o1, o2) -> Double.compare(o2.getTotalAmount(), o1.getTotalAmount()));

        // Ensure the adapter is set before notifying
        if (orderAdapter != null) {
            orderAdapter.notifyDataSetChanged();
        }
    }

    private void sortByNewestFirst() {
        Collections.sort(filteredOrdersList, (o1, o2) -> Long.compare(o2.getOrderDateTime(), o1.getOrderDateTime()));

        // Ensure the adapter is set before notifying
        if (orderAdapter != null) {
            orderAdapter.notifyDataSetChanged();
        }
    }

    private void displayPaidOrders() {
        List<Order> paidOrders = new ArrayList<>();
        for (Order order : ordersList) {
            if ("Paid".equalsIgnoreCase(order.getPaymentStatus())) {
                paidOrders.add(order);
            }
        }
        filteredOrdersList.clear();
        filteredOrdersList.addAll(paidOrders);

        // Ensure the adapter is set before notifying
        if (orderAdapter != null) {
            orderAdapter.notifyDataSetChanged();
        }
    }

    private void displayNotPaidOrders() {
        List<Order> notPaidOrders = new ArrayList<>();
        for (Order order : ordersList) {
            if (!"Paid".equalsIgnoreCase(order.getPaymentStatus())) {
                notPaidOrders.add(order);
            }
        }
        filteredOrdersList.clear();
        filteredOrdersList.addAll(notPaidOrders);

        // Ensure the adapter is set before notifying
        if (orderAdapter != null) {
            orderAdapter.notifyDataSetChanged();
        }
    }

}
