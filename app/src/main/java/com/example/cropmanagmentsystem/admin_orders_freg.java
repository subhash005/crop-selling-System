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

public class admin_orders_freg extends Fragment {

    private RecyclerView rvOrders;
    private Spinner spinnerSort;
    private OrderAdapter orderAdapter;
    private List<Order> ordersList = new ArrayList<>(); // List to display
    private List<Order> allOrdersList = new ArrayList<>(); // Backup of all orders

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_orders_freg, container, false);

        // Initialize views
        spinnerSort = view.findViewById(R.id.spinnerSort);
        rvOrders = view.findViewById(R.id.rvOrders);

        // Set up RecyclerView
        rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));

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

        // Set default selection to "All Orders"
        spinnerSort.setSelection(2); // Index 2 corresponds to "All Orders"

        // Handle Spinner item selection
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    sortByHighestTotalAmount();
                } else if (position == 1) {
                    sortByNewestFirst();
                } else if (position == 2) {
                    showAllOrders();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Default to showing all orders
                showAllOrders();
            }
        });

        return view;
    }

    private void loadOrdersFromFirebase() {
        FirebaseDatabase.getInstance().getReference("orders")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ordersList.clear();
                        allOrdersList.clear();
                        for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                            Order order = orderSnapshot.getValue(Order.class);
                            if (order != null) {
                                ordersList.add(order); // Add to display list
                                allOrdersList.add(order); // Add to backup list
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

    private void showAllOrders() {
        ordersList.clear();
        ordersList.addAll(allOrdersList); // Reset the list to all orders
        if (orderAdapter != null) {
            orderAdapter.notifyDataSetChanged();
        }
    }
}
