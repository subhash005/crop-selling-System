package com.example.cropmanagmentsystem;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link customer_profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class customer_profile extends Fragment {
    private Button logout;
    private ImageView imageView_profile;
    private TextView profile_name;
    private TextView profile_email;
    private TextView profile_addres;
    private TextView profile_number;
    private TextView profile_password;
    private FirebaseAuth auth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public customer_profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment customer_profile.
     */
    // TODO: Rename and change types and number of parameters
    public static customer_profile newInstance(String param1, String param2) {
        customer_profile fragment = new customer_profile();
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
        View v= inflater.inflate(R.layout.fragment_customer_profile, container, false);
         logout=v.findViewById(R.id.customer_logout);
         imageView_profile=v.findViewById(R.id.imageView_profile);
         profile_name=v.findViewById(R.id.profile_name);
         profile_email=v.findViewById(R.id.profile_email);
        profile_addres=v.findViewById(R.id.profile_addres);
         profile_number=v.findViewById(R.id.profile_number);
         profile_password=v.findViewById(R.id.profile_password);

        fetchUser_Profiledetail();



        auth = FirebaseAuth.getInstance();
        String very=auth.getCurrentUser().getUid();
        Log.d("verifying","this is "+ very);




        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        return v;
    }

    private void fetchUser_Profiledetail() {
        auth=FirebaseAuth.getInstance();
        if(auth.getCurrentUser() !=null){
            String userid=auth.getCurrentUser().getUid();
            DatabaseReference ref= FirebaseDatabase.getInstance().getReference("user").child(userid);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    String addres = snapshot.child("address").getValue(String.class);
                    profile_addres.setText(addres);
                    String mail = snapshot.child("mail").getValue(String.class);
                    profile_email.setText(mail);
                    String number = snapshot.child("number").getValue(String.class);
                    profile_number.setText(number);
                    String password = snapshot.child("password").getValue(String.class);
                    profile_password.setText(password);
                    String userName = snapshot.child("userName").getValue(String.class);
                    profile_name.setText(userName);

                    // Get and set profile image from Firebase Storage
                    String profileImageUrl = snapshot.child("profilepic").getValue(String.class);
                    if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
                        Glide.with(getActivity())
                                .load(profileImageUrl)
                                .circleCrop()  // This makes the image circular
                                .into(imageView_profile);
                        Toast.makeText(getActivity(), "image is loaded", Toast.LENGTH_SHORT).show();
                    } else {
                        imageView_profile.setImageResource(R.drawable.b);
                        Toast.makeText(getActivity(), "image is not loaded", Toast.LENGTH_SHORT).show();

                    }



                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    // Logout function
    private void logout() {
        FirebaseAuth.getInstance().signOut(); // Sign out the user
        Intent intent = new Intent(getActivity(), login.class); // Redirect to login
        startActivity(intent);
        getActivity().finish(); // Close the customer activity and all associated fragments
    }


}