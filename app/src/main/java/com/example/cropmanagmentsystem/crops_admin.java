package com.example.cropmanagmentsystem;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link crops_admin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class crops_admin extends Fragment {
    private EditText input_name_crop,input_description_crop ,input_price_crop,input_stock_crop;
    private Spinner input_category_crop;
    private CheckBox input_is_organic_crop;
    private Button button_add_crop;
    private CircleImageView cropProfile;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public crops_admin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment crops_admin.
     */
    // TODO: Rename and change types and number of parameters
    public static crops_admin newInstance(String param1, String param2) {
        crops_admin fragment = new crops_admin();
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
        View view =inflater.inflate(R.layout.fragment_crops_admin, container, false);
        input_name_crop=view.findViewById(R.id.input_name_crop);
        input_description_crop=view.findViewById(R.id.input_description_crop);
        input_price_crop=view.findViewById(R.id.input_price_crop);
        input_stock_crop=view.findViewById(R.id.input_stock_crop);
        input_category_crop=view.findViewById(R.id.input_category_crop);
        input_is_organic_crop=view.findViewById(R.id.input_is_organic_crop);
        button_add_crop=view.findViewById(R.id.button_add_crop);
        cropProfile=view.findViewById(R.id.cropProfile);

        // Load categories into spinner
        load_categories();


        return view;
    }

    private void load_categories(){
        DatabaseReference categoyRef= FirebaseDatabase.getInstance().getReference("categories");
        categoyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> categoryList = new ArrayList<>();
                for (DataSnapshot categorySnapshot: snapshot.getChildren()){
                    String cat=categorySnapshot.child("categoryName").getValue(String.class);
                    categoryList.add(cat);
                }
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,categoryList);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                input_category_crop.setAdapter(categoryAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Faild to load category", Toast.LENGTH_SHORT).show();
            }
        });
    }

}