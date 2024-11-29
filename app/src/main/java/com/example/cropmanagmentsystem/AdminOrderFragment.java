package com.example.cropmanagmentsystem;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminOrderFragment extends Fragment {

    private RecyclerView orderRecyclerView;
    private AdminOrderAdapter orderAdapter;
    private List<AdminOrder> orders = new ArrayList<>();

    public AdminOrderFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_admin_order, container, false);

        // Initialize RecyclerView
        orderRecyclerView = rootView.findViewById(R.id.orderRecyclerView);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Fetch orders from Firebase
        fetchOrders();

        // Set the adapter
        orderAdapter = new AdminOrderAdapter(getContext(), orders);
        orderRecyclerView.setAdapter(orderAdapter);

        return rootView;
    }

    private void fetchOrders() {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orders.clear(); // Clear existing data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Get the orderID (indirectly from the key)
                    String orderID = snapshot.getKey();
                    Log.d("AdminOrderFragment", "Order ID: " + orderID);

                    // Get the order data
                    AdminOrder order = snapshot.getValue(AdminOrder.class);

                    // Set the orderID manually in the AdminOrder object
                    if (order != null) {
                        order.setOrderID(orderID); // Set the orderID here
                        orders.add(order);
                    }
                }
                // Notify the adapter that data has changed
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("AdminOrderFragment", "Error fetching orders: " + databaseError.getMessage());
            }
        });
    }
}
