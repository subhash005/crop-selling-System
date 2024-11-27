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
                R.array.sort_options,
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

                                // Optionally check for userId if you want to filter
                                if (order.getUserId().equals(currentUserId)) {
                                    ordersList.add(order);
                                }
                            }
                        }
                        orderAdapter = new OrderAdapter(ordersList);
                        rvOrders.setAdapter(orderAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(requireContext(), "Failed to load orders.", Toast.LENGTH_SHORT).show();
                    }
                });
    }




    private void displayAllOrders() {
        // No sorting applied, just notify the adapter
        if (orderAdapter != null) {
            orderAdapter.notifyDataSetChanged();
        }
    }

    private void sortByHighestTotalAmount() {
        Collections.sort(ordersList, (o1, o2) -> Double.compare(o2.getTotalAmount(), o1.getTotalAmount()));
        if (orderAdapter != null) {
            orderAdapter.notifyDataSetChanged();
        }
    }

    private void sortByNewestFirst() {
        Collections.sort(ordersList, (o1, o2) -> Long.compare(o2.getOrderDateTime(), o1.getOrderDateTime()));
        if (orderAdapter != null) {
            orderAdapter.notifyDataSetChanged();
        }
    }
}
