package com.example.cropmanagmentsystem;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
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
import java.util.Calendar;
import java.util.List;

public class AdminOrderFragment extends Fragment {

    private RecyclerView orderRecyclerView;
    private Spinner monthSpinner, yearSpinner;
    private EditText orderSearchEditText;
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
        orderSearchEditText = rootView.findViewById(R.id.orderSearchEditText);

        // Setup RecyclerView
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Adapter
        orderAdapter = new AdminOrderAdapter(getContext(), filteredOrders);
        orderRecyclerView.setAdapter(orderAdapter);

        // Set Spinner Listeners
        monthSpinner.setOnItemSelectedListener(new SpinnerListener());
        yearSpinner.setOnItemSelectedListener(new SpinnerListener());

        // Setup search functionality
        orderSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Optional: Add logic before the text is changed
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterOrders(); // Filter orders when the text changes
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Optional: Add logic after the text is changed
            }
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
        String searchQuery = orderSearchEditText.getText().toString().toLowerCase();
        String selectedMonth = monthSpinner.getSelectedItem().toString();
        String selectedYear = yearSpinner.getSelectedItem().toString();

        filteredOrders.clear();

        for (AdminOrder order : orders) {
            boolean matchesMonth = selectedMonth.equals("All Months") ||
                    getMonthName(Integer.parseInt(order.getOrderMonth())).equalsIgnoreCase(selectedMonth);
            boolean matchesYear = selectedYear.equals("All Years") || order.getOrderYear().equals(selectedYear);

            // Check if the order ID matches the search query (case-insensitive)
            boolean matchesSearchQuery = order.getOrderID().toLowerCase().contains(searchQuery);

            if (matchesMonth && matchesYear && matchesSearchQuery) {
                filteredOrders.add(order);
            }
        }

        orderAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Filtered " + filteredOrders.size() + " orders", Toast.LENGTH_SHORT).show();
    }

    private String getMonthName(int monthNumber) {
        String[] months = getResources().getStringArray(R.array.months_array);
        return months[monthNumber];
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
