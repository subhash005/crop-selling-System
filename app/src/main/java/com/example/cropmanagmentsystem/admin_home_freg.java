package com.example.cropmanagmentsystem;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link admin_home_freg#newInstance} factory method to
 * create an instance of this fragment.
 */
public class admin_home_freg extends Fragment {
    private RecyclerView recyclerView_cropdata;
    private admin_crop_recycler_adapter adminCropRecyclerAdapter;
    private List<crops_model> cropList=new ArrayList<>();


    //2nd recycler view
    private RecyclerView recyclerView_categorydata;
    private category_adapter category_adapter;
    private List<Category> categoryList=new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public admin_home_freg() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment admin_home_freg.
     */
    // TODO: Rename and change types and number of parameters
    public static admin_home_freg newInstance(String param1, String param2) {
        admin_home_freg fragment = new admin_home_freg();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_admin_home_freg, container, false);
        recyclerView_cropdata=view.findViewById(R.id.recyclerView_cropdata);
        recyclerView_cropdata.setLayoutManager(new GridLayoutManager(getActivity(),2));
        adminCropRecyclerAdapter = new admin_crop_recycler_adapter(getActivity(),cropList);
        recyclerView_cropdata.setAdapter(adminCropRecyclerAdapter);
        loadCropData();


        //2nd recycler view
        recyclerView_categorydata=view.findViewById(R.id.recyclerView_categorydata);
        recyclerView_categorydata.setLayoutManager(new GridLayoutManager(getActivity(),4));
        category_adapter= new category_adapter(getActivity(),categoryList);
        recyclerView_categorydata.setAdapter(category_adapter);
        loadCategories();

        return view;
    }

    private void loadCategories() {
        DatabaseReference categoryRef=FirebaseDatabase.getInstance().getReference("categories");
        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                for (DataSnapshot categorySnapshot: snapshot.getChildren()){
                    Category category= categorySnapshot.getValue(Category.class);
                    if(category!=null){
                        categoryList.add(category);
                        Log.d("Category", "Category: " + category.getCategoryName());  // Check if categories are being retrieved
                    }
                    category_adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadCropData() {
        DatabaseReference cropRef= FirebaseDatabase.getInstance().getReference("Crops");
        cropRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cropList.clear();
                for(DataSnapshot cropSnapshot : snapshot.getChildren()){
                    crops_model crop = cropSnapshot.getValue(crops_model.class);
                    if(crop!= null){
                        cropList.add(crop);
                    }
                    adminCropRecyclerAdapter.notifyDataSetChanged(); // Notify adapter that data has changed

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}