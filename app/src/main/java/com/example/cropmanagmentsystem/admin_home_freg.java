package com.example.cropmanagmentsystem;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

public class admin_home_freg extends Fragment {
    private RecyclerView recyclerView_cropdata;
    private admin_crop_recycler_adapter adminCropRecyclerAdapter;
    private List<crops_model> cropList = new ArrayList<>();
    private List<crops_model> filteredCropList = new ArrayList<>();  // List for filtered crops

    // Spinner for Category selection
    private Spinner spinnerCategory;

    private RecyclerView recyclerView_categorydata;
    private category_adapter category_adapter;
    private List<Category> categoryList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_home_freg, container, false);
        recyclerView_cropdata = view.findViewById(R.id.recyclerView_cropdata);
        recyclerView_cropdata.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adminCropRecyclerAdapter = new admin_crop_recycler_adapter(getActivity(), filteredCropList);
        recyclerView_cropdata.setAdapter(adminCropRecyclerAdapter);
        loadCropData();

        // Initialize Spinner
        spinnerCategory = view.findViewById(R.id.spinner_category);

        // 2nd RecyclerView for Categories
        recyclerView_categorydata = view.findViewById(R.id.recyclerView_categorydata);
        recyclerView_categorydata.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        category_adapter = new category_adapter(getActivity(), categoryList);
        recyclerView_categorydata.setAdapter(category_adapter);
        loadCategories();

        return view;
    }

    private void loadCategories() {
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("categories");
        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                List<String> categoryNames = new ArrayList<>();
                categoryNames.add("All Categories"); // Default category

                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    Category category = categorySnapshot.getValue(Category.class);
                    if (category != null) {
                        categoryList.add(category);
                        categoryNames.add(category.getCategoryName());
                    }
                }

                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_item, categoryNames);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCategory.setAdapter(categoryAdapter);

                // Set Spinner item selection listener to filter crops
                spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        String selectedCategory = parentView.getItemAtPosition(position).toString();
                        Log.d("Category", "Selected Category: " + selectedCategory); // Debugging log
                        filterCropsByCategory(selectedCategory);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // Do nothing
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void loadCropData() {
        DatabaseReference cropRef = FirebaseDatabase.getInstance().getReference("Crops");
        cropRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cropList.clear();
                for (DataSnapshot cropSnapshot : snapshot.getChildren()) {
                    crops_model crop = cropSnapshot.getValue(crops_model.class);
                    if (crop != null) {
                        cropList.add(crop);
                    }
                }
                // Initially, set the filteredCropList to show all crops
                filteredCropList.clear();
                filteredCropList.addAll(cropList);
                adminCropRecyclerAdapter.notifyDataSetChanged(); // Notify adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void filterCropsByCategory(String selectedCategory) {
        filteredCropList.clear();
        Log.d("Filter", "Filtering crops by: " + selectedCategory); // Debugging log
        if (selectedCategory.equals("All Categories")) {
            // If "All Categories" is selected, show all crops
            filteredCropList.addAll(cropList);
        } else {
            // Otherwise, filter crops by selected category
            for (crops_model crop : cropList) {
                Log.d("Filter", "Crop category: " + crop.getCrop_category()); // Debugging log
                if (crop.getCrop_category().equalsIgnoreCase(selectedCategory)) {
                    filteredCropList.add(crop);
                }
            }
        }

        // Update the RecyclerView with filtered data
        adminCropRecyclerAdapter.notifyDataSetChanged();
    }

}
