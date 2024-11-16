package com.example.cropmanagmentsystem;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link customer_crops#newInstance} factory method to
 * create an instance of this fragment.
 */
public class customer_crops extends Fragment {
    private RecyclerView recyclerView_cropdata;
    private customer_crop_recycleView_adapter customer_crop_recycleView_adapter;
    private List<crops_model> cropList=new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public customer_crops() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment customer_crops.
     */
    // TODO: Rename and change types and number of parameters
    public static customer_crops newInstance(String param1, String param2) {
        customer_crops fragment = new customer_crops();
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
        View view= inflater.inflate(R.layout.fragment_customer_crops, container, false);
        recyclerView_cropdata=view.findViewById(R.id.customer_recyclerView_cropdata);
        recyclerView_cropdata.setLayoutManager(new GridLayoutManager(getActivity(),2));
        customer_crop_recycleView_adapter = new customer_crop_recycleView_adapter(getActivity(),cropList);
        recyclerView_cropdata.setAdapter(customer_crop_recycleView_adapter);
        loadCropData();
        return view;
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
                    customer_crop_recycleView_adapter.notifyDataSetChanged(); // Notify adapter that data has changed

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}