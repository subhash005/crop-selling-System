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

public class customer_crops extends Fragment {

    private RecyclerView recyclerView_cropdata;
    private customer_crop_recycleView_adapter customer_crop_recycleView_adapter;
    private List<crops_model> cropList = new ArrayList<>();
    private List<crops_model> filteredList = new ArrayList<>();

    private Spinner spinnerCategoryFilter;
    private androidx.appcompat.widget.SearchView editTextCropName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_crops, container, false);

        recyclerView_cropdata = view.findViewById(R.id.customer_recyclerView_cropdata);
        spinnerCategoryFilter = view.findViewById(R.id.spinner_category_filter);
        editTextCropName = view.findViewById(R.id.searchview_crop_name);

        recyclerView_cropdata.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        customer_crop_recycleView_adapter = new customer_crop_recycleView_adapter(getActivity(), filteredList);
        recyclerView_cropdata.setAdapter(customer_crop_recycleView_adapter);

        loadCropData();
        setupCategoryFilter();
        setupNameFilter();

        return view;
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
                // Initially show all crops
                filteredList.clear();
                filteredList.addAll(cropList);
                customer_crop_recycleView_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Failed to load crops: " + error.getMessage());
            }
        });
    }

    private void setupCategoryFilter() {
        DatabaseReference cropRef = FirebaseDatabase.getInstance().getReference("Crops");
        List<String> categories = new ArrayList<>();
        categories.add("All"); // Default option

        cropRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot cropSnapshot : snapshot.getChildren()) {
                    String category = cropSnapshot.child("crop_category").getValue(String.class);
                    if (category != null && !categories.contains(category)) {
                        categories.add(category);
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categories);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCategoryFilter.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Failed to load categories: " + error.getMessage());
            }
        });

        spinnerCategoryFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = categories.get(position);
                filterCrops(selectedCategory, editTextCropName.getQuery().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupNameFilter() {
        editTextCropName.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String selectedCategory = spinnerCategoryFilter.getSelectedItem().toString();
                filterCrops(selectedCategory, newText);
                return true;
            }
        });
    }

    private void filterCrops(String category, String query) {
        filteredList.clear();

        for (crops_model crop : cropList) {
            boolean matchesCategory = category.equals("All") || (crop.getCrop_category() != null && crop.getCrop_category().equalsIgnoreCase(category));
            boolean matchesQuery = query.isEmpty() || (crop.getCrop_name() != null && crop.getCrop_name().toLowerCase().contains(query.toLowerCase()));

            if (matchesCategory && matchesQuery) {
                filteredList.add(crop);
            }
        }

        customer_crop_recycleView_adapter.notifyDataSetChanged();
    }
}
