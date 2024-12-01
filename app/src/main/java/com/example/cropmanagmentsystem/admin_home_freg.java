package com.example.cropmanagmentsystem;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
    private TextView noCropsMessage;  // TextView to show when no crops are available
    private EditText searchCropName;  // EditText for searching crops by name

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_home_freg, container, false);

        // Initialize the RecyclerView for crops
        recyclerView_cropdata = view.findViewById(R.id.recyclerView_cropdata);
        recyclerView_cropdata.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adminCropRecyclerAdapter = new admin_crop_recycler_adapter(getActivity(), filteredCropList);
        recyclerView_cropdata.setAdapter(adminCropRecyclerAdapter);
        loadCropData();

        // Initialize Spinner and TextView for no crops message
        spinnerCategory = view.findViewById(R.id.spinner_category);
        noCropsMessage = view.findViewById(R.id.no_crops_message);

        // Initialize the search EditText
        searchCropName = view.findViewById(R.id.search_crop_name);
        searchCropName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                filterCropsByName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Load the categories into the spinner
        loadCategories();

        return view;
    }

    private void loadCategories() {
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("categories");
        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> categoryNames = new ArrayList<>();
                categoryNames.add("All Categories"); // Default category
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    Category category = categorySnapshot.getValue(Category.class);
                    if (category != null) {
                        categoryNames.add(category.getCategoryName());
                    }
                }

                // Ensure we have a valid context before creating the ArrayAdapter
                if (getActivity() != null) {
                    ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_spinner_item, categoryNames);
                    categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategory.setAdapter(categoryAdapter);

                    // Set Spinner item selection listener to filter crops
                    spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            String selectedCategory = parentView.getItemAtPosition(position).toString();
                            filterCropsByCategory(selectedCategory);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // Do nothing
                        }
                    });
                }
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
        if (selectedCategory.equals("All Categories")) {
            // If "All Categories" is selected, show all crops
            filteredCropList.addAll(cropList);
        } else {
            // Otherwise, filter crops by selected category
            for (crops_model crop : cropList) {
                if (crop.getCrop_category().equalsIgnoreCase(selectedCategory)) {
                    filteredCropList.add(crop);
                }
            }
        }

        // Check if any crops were found
        if (filteredCropList.isEmpty()) {
            noCropsMessage.setVisibility(View.VISIBLE);
        } else {
            noCropsMessage.setVisibility(View.GONE);
        }

        adminCropRecyclerAdapter.notifyDataSetChanged();
    }

    private void filterCropsByName(String query) {
        filteredCropList.clear();

        if (query.isEmpty()) {
            // If the search query is empty, show all crops
            filteredCropList.addAll(cropList);
        } else {
            // Otherwise, filter crops by name
            for (crops_model crop : cropList) {
                if (crop.getCrop_name().toLowerCase().contains(query.toLowerCase())) {
                    filteredCropList.add(crop);
                }
            }
        }

        // Check if there are any crops matching the search criteria
        if (filteredCropList.isEmpty()) {
            noCropsMessage.setVisibility(View.VISIBLE); // Show "No crops available" message
        } else {
            noCropsMessage.setVisibility(View.GONE); // Hide the "No crops available" message
        }

        // Notify the adapter to update the RecyclerView
        adminCropRecyclerAdapter.notifyDataSetChanged();
    }
}
