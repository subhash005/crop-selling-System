package com.example.cropmanagmentsystem;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;


public class admin_customers_freg extends Fragment {

    private RecyclerView recyclerView;
    private admin_customer_view_adpater userAdapter;
    private List<Users> userList;
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
        userAdapter = new admin_customer_view_adpater((ArrayList<Users>) userList, getContext());

        // Set adapter
        recyclerView.setAdapter(userAdapter);

        // Firebase Database reference to "user" node
        userRef = FirebaseDatabase.getInstance().getReference("user");

        // Fetch users from Firebase
        fetchUsersFromFirebase();

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
                userAdapter.notifyDataSetChanged(); // Notify adapter that data has changed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "Failed to fetch data: " + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}