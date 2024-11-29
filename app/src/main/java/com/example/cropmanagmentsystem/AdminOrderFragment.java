package com.example.cropmanagmentsystem;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminOrderFragment extends Fragment {

    private RecyclerView orderRecyclerView;
    private Spinner monthSpinner, yearSpinner;
    private AdminOrderAdapter orderAdapter;
    private List<AdminOrder> orders = new ArrayList<>();
    private List<AdminOrder> filteredOrders = new ArrayList<>();
    private long selectedDateInMillis = 0; // Default to 0 for no date selected

    public AdminOrderFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_admin_order, container, false);

        // Bind views
        orderRecyclerView = rootView.findViewById(R.id.orderRecyclerView);
        monthSpinner = rootView.findViewById(R.id.monthSpinner);
        yearSpinner = rootView.findViewById(R.id.yearSpinner);

        // Setup RecyclerView
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Adapter
        orderAdapter = new AdminOrderAdapter(getContext(), filteredOrders);
        orderRecyclerView.setAdapter(orderAdapter);

        // Set Spinner Listeners
        monthSpinner.setOnItemSelectedListener(new SpinnerListener());
        yearSpinner.setOnItemSelectedListener(new SpinnerListener());

        // Setup filter button listener
        rootView.findViewById(R.id.filterByDateButton).setOnClickListener(v -> {
            // You can replace this with logic to get the selected date in millis
            // Example: selectedDateInMillis = getDateInMillisFromDatePicker();
            filterOrders();
        });

        // Fetch orders from Firebase
        fetchOrders();

        return rootView;
    }

    private void fetchOrders() {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orders.clear(); // Clear existing data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String orderID = snapshot.getKey();
                    AdminOrder order = snapshot.getValue(AdminOrder.class);

                    if (order != null) {
                        order.setOrderID(orderID);
                        orders.add(order);
                    }
                }
                filterOrders(); // Filter orders after fetching
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("AdminOrderFragment", "Error fetching orders: " + databaseError.getMessage());
            }
        });
    }

    private void filterOrders() {
        String selectedMonth = monthSpinner.getSelectedItem().toString();
        String selectedYear = yearSpinner.getSelectedItem().toString();

        filteredOrders.clear();

        for (AdminOrder order : orders) {
            boolean matchesMonth = selectedMonth.equals("All Months") ||
                    getMonthName(Integer.parseInt(order.getOrderMonth())).equalsIgnoreCase(selectedMonth);
            boolean matchesYear = selectedYear.equals("All Years") || order.getOrderYear().equals(selectedYear);

            boolean matchesDate = true;
            if (selectedDateInMillis != 0) {  // Checking if the selected date is not 0 (default value)
                long orderDateTime = order.getOrderDateTime(); // Primitive long value

                // Check if orderDateTime is valid (not zero or custom invalid value)
                if (orderDateTime != 0) { // Assuming 0 means no date
                    matchesDate = isSameDay(orderDateTime, selectedDateInMillis);
                } else {
                    Log.w("AdminOrderFragment", "Order missing date field: " + order.getOrderID());
                    matchesDate = false;
                }
            }

            if (matchesMonth && matchesYear && matchesDate) {
                filteredOrders.add(order);
            }
        }

        orderAdapter.notifyDataSetChanged();
        //Toast.makeText(getContext(), "Filtered " + filteredOrders.size() + " orders", Toast.LENGTH_SHORT).show();
    }

    private String getMonthName(int monthNumber) {
        String[] months = getResources().getStringArray(R.array.months_array);
        return months[monthNumber];
    }

    private boolean isSameDay(long date1Millis, long date2Millis) {
        // You can implement this function using Java's Calendar or Date classes
        // Here's a simple implementation:
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
            filterOrders();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing
        }
    }
}
