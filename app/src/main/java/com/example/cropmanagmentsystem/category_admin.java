package com.example.cropmanagmentsystem;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link category_admin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class category_admin extends Fragment {
    private EditText categoryNameInput;
    private Button addCategoryButton;
    private DatabaseReference categoryRef;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public category_admin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment category_admin.
     */
    // TODO: Rename and change types and number of parameters
    public static category_admin newInstance(String param1, String param2) {
        category_admin fragment = new category_admin();
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
        View view = inflater.inflate(R.layout.fragment_category_admin, container, false);
        addCategoryButton=view.findViewById(R.id.addCategoryButton);
        categoryNameInput=view.findViewById(R.id.categoryNameInput);

        categoryRef= FirebaseDatabase.getInstance().getReference().child("categories");
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categoryName=categoryNameInput.getText().toString();
                if(!categoryName.isEmpty()){
                    String categoryId = categoryRef.push().getKey();
                    categoryRef.child(categoryId).setValue(new Category(categoryId,categoryName));
                    Toast.makeText(getActivity(), "Category added!", Toast.LENGTH_SHORT).show();
                    categoryNameInput.setText(""); // Clear the input field to stay on the same fragment
                }else {
                    Toast.makeText(getActivity(), "Please enter a category name", Toast.LENGTH_SHORT).show();

                }
            }
        });





        return view;
    }
}