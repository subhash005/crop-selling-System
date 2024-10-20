package com.example.cropmanagmentsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    private Uri image_uri;
    private ProgressDialog progressDialog;


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

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Uploading Crop Data...");  // Message to display
        progressDialog.setCancelable(false);  // Prevent user from canceling the dialog


        // Load categories into spinner
        load_categories();

        button_add_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AddCrop", "Add Crop button clicked");
                saveCropData();
            }
        });

        cropProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent,10);
            }
        });
        return view;
    }

    //handling the image uri from intent image
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            image_uri = data.getData();
            try {
                // Display the selected image
                cropProfile.setImageURI(image_uri);
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Error loading image", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }



    private void saveCropData() {

        Log.d("SaveCropData", "saveCropData() called");

        String name_crop=input_name_crop.getText().toString();
        String description_crop=input_description_crop.getText().toString();
        String stock_crop=input_stock_crop.getText().toString();
        String price_crop=input_price_crop.getText().toString();
        String category_crop=input_category_crop.getSelectedItem().toString();
        boolean organic_crop=input_is_organic_crop.isChecked();

        //checking inputs value are blank or not
        if(name_crop.isEmpty() || description_crop.isEmpty()|| stock_crop.isEmpty()|| price_crop.isEmpty() ){
            Toast.makeText(getActivity(), "Fill all the details", Toast.LENGTH_SHORT).show();
            return;
        }

        int stock =Integer.parseInt(stock_crop);
        double price=Double.parseDouble(price_crop);

        uploadImageAndSaveCrop(name_crop,description_crop,category_crop,organic_crop,stock,price);
    }

    private void uploadImageAndSaveCrop(final String name_crop,final String description_crop,final String category_crop,final boolean organic_crop,final int stock,final double price) {
        // Check if the image URI is null before proceeding
        Log.d("uploadImageAndSaveCrop", "uploadImageAndSaveCrop() called");

        if (image_uri == null) {
            Toast.makeText(getActivity(), "Please select an image", Toast.LENGTH_SHORT).show();
            return;  // Don't proceed with upload if image isn't selected
        }
        // Show the ProgressDialog
        progressDialog.show();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference("CropImage").child(System.currentTimeMillis() + ".jpg");
        storageRef.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String image_url=uri.toString();
                        Log.d("uploadImageAndSaveCrop_onworking", "uploadImageAndSaveCrop()_onworking called");

                        saveCropFirebaseData(image_url,name_crop,description_crop,category_crop,organic_crop,stock,price);

                    }
                });
            }
        });
    }

    private void saveCropFirebaseData(String image_url,String name_crop, String description_crop, String category_crop, boolean organic_crop, int stock, double price){
        DatabaseReference cropRef=FirebaseDatabase.getInstance().getReference().child("Crops");
        String cropId = cropRef.push().getKey();
        Log.d("saveCropFirebaseData", "saveCropFirebaseData() called");

        Log.d("Firebase", "Crop ID: " + cropId);

        cropRef.child(cropId).setValue(new crops_model(image_url,name_crop,description_crop,category_crop,organic_crop,stock,price,cropId)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
//                Dismiss the ProgressDialog once the crop data is successfully uploaded
                progressDialog.dismiss();
                resetInputFields();
                Toast.makeText(getActivity(), "Crop Data is Added", Toast.LENGTH_SHORT).show();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.container_admin, new admin_home_freg()); // Replace with your fragment container ID
//                fragmentTransaction.addToBackStack(null); // Optional, if you want to add it to the backstack
//                fragmentTransaction.commit();



            }
        });


    }

    // Method to reset input fields
    private void resetInputFields() {
        input_name_crop.setText("");           // Clear crop name field
        input_description_crop.setText("");    // Clear crop description field
        input_price_crop.setText("");          // Clear price field
        input_stock_crop.setText("");          // Clear stock field
        input_category_crop.setSelection(0);   // Reset spinner to first category
        input_is_organic_crop.setChecked(false); // Uncheck organic checkbox
        cropProfile.setImageResource(R.drawable.camera); // Reset image to default
    }



    // loading the categoies in spinner
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
                // Ensure the activity context is not null before using it
                if (getActivity() != null) {
                    ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, categoryList);
                    categoryAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                    input_category_crop.setAdapter(categoryAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Faild to load category", Toast.LENGTH_SHORT).show();
            }
        });
    }


}