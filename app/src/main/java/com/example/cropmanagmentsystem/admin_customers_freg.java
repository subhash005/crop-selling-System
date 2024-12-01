package com.example.cropmanagmentsystem;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class admin_customers_freg extends Fragment {

    private RecyclerView recyclerView;
    private admin_customer_view_adpater userAdapter;
    private List<Users> userList;
    private List<Users> filteredUserList;  // List to hold filtered users
    private DatabaseReference userRef;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_customers_freg, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);

        // Set GridLayoutManager with 2 columns
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));  // 2 columns

        // Initialize ArrayList and Adapter
        userList = new ArrayList<>();
        filteredUserList = new ArrayList<>();  // Initialize the filtered list
        userAdapter = new admin_customer_view_adpater((ArrayList<Users>) filteredUserList, getContext());

        // Set adapter
        recyclerView.setAdapter(userAdapter);

        // Firebase Database reference to "user" node
        userRef = FirebaseDatabase.getInstance().getReference("user");

        // Fetch users from Firebase
        fetchUsersFromFirebase();

        // Setup the search functionality
        EditText searchCustomerName = view.findViewById(R.id.search_customer_name);
        searchCustomerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String query = editable.toString().toLowerCase();
                filterUserList(query);
            }
        });

        return view;
    }

    private void fetchUsersFromFirebase() {
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear(); // Clear previous data
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users user = dataSnapshot.getValue(Users.class); // Deserialize the data into Users object
                    if (user != null) { // Ensure user is not null before adding
                        userList.add(user); // Add each user to the list
                    }
                }
                filteredUserList.clear(); // Initially show all users
                filteredUserList.addAll(userList);
                userAdapter.notifyDataSetChanged(); // Notify adapter that data has changed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterUserList(String query) {
        filteredUserList.clear();
        if (query.isEmpty()) {
            filteredUserList.addAll(userList);  // If query is empty, show all users
        } else {
            for (Users user : userList) {
                // Check if the user's name contains the query (case-insensitive)
                if (user.getUserName().toLowerCase().contains(query)) {
                    filteredUserList.add(user);
                }
            }
        }
        userAdapter.notifyDataSetChanged(); // Notify the adapter to update the RecyclerView
    }
}
