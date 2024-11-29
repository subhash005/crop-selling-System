package com.example.cropmanagmentsystem;

import static android.app.Activity.RESULT_OK;
import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class admin_crop_recycler_adapter extends RecyclerView.Adapter<admin_crop_recycler_adapter.ViewHolder> {
    private Context context;
    private List<crops_model> cropList;
    private Uri image_uri;
    private CircleImageView edit_crop_image;
    private static final int PICK_IMAGE_REQUEST = 1;


    admin_crop_recycler_adapter(Context context ,List<crops_model> cropList){
        this.context=context;
        this.cropList=cropList;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.crop_cardview,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        crops_model crop = cropList.get(position);
        holder.crop_name.setText(crop.getCrop_name());
        holder.crop_category.setText(crop.getCrop_category());
        holder.crop_description.setText(crop.getCrop_description());
        holder.crop_price.setText(String.valueOf(crop.getCrop_price()));
        holder.crop_stock.setText(String.valueOf(crop.getCrop_stock()));
        holder.crop_isOrganic.setText(crop.isCrop_isOrganic() ? "Organic" : "Not Organic"); // Use isCrop_isOrganic()

        Glide.with(context).load(crop.getCrop_pic()).into(holder.crop_pic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDeleteDialog(crop);
            }
        });
    }

    private void showEditDeleteDialog(@NonNull crops_model crop) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dailog_edit_crop, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        EditText edit_crop_name = dialogView.findViewById(R.id.edit_crop_name);
        Spinner edit_crop_category = dialogView.findViewById(R.id.edit_crop_category);
        EditText edit_crop_description = dialogView.findViewById(R.id.edit_crop_description);
        EditText edit_crop_price = dialogView.findViewById(R.id.edit_crop_price);
        EditText edit_crop_stock = dialogView.findViewById(R.id.edit_crop_stock);
        CheckBox edit_crop_isOrganic = dialogView.findViewById(R.id.edit_crop_isOrganic);
        edit_crop_image = dialogView.findViewById(R.id.edit_crop_image);
        Button btn_edit_update_crop = dialogView.findViewById(R.id.btn_edit_update_crop);
        Button btn_edit_Delete_crop = dialogView.findViewById(R.id.btn_edit_Delete_crop);

        // Load current crop data into the fields
        edit_crop_name.setText(crop.getCrop_name());
        edit_crop_description.setText(crop.getCrop_description());
        edit_crop_price.setText(String.valueOf(crop.getCrop_price()));
        edit_crop_stock.setText(String.valueOf(crop.getCrop_stock()));
        edit_crop_isOrganic.setChecked(crop.isCrop_isOrganic());

        // Load image into CircleImageView
        Glide.with(context).load(crop.getCrop_pic()).into(edit_crop_image);

        // Load categories into spinner and set the selected category
        load_categories(edit_crop_category, crop.getCrop_category());

        // Image selection
        edit_crop_image.setOnClickListener(v -> {
            openImagePicker();
        });


        // Update crop details on clicking the update button
        btn_edit_update_crop.setOnClickListener(v -> {
            // Get the updated values from the fields
            String name_crop = edit_crop_name.getText().toString();
            String description_crop = edit_crop_description.getText().toString();
            String category_crop = edit_crop_category.getSelectedItem().toString();
            boolean organic_crop = edit_crop_isOrganic.isChecked();
            double price = Double.parseDouble(edit_crop_price.getText().toString());
            int stock = Integer.parseInt(edit_crop_stock.getText().toString());

            // Check if all necessary fields are filled
            if (name_crop.isEmpty() || description_crop.isEmpty() || edit_crop_price.getText().toString().isEmpty() ||
                    edit_crop_stock.getText().toString().isEmpty()) {
                Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Handle image update
            if (image_uri != null) {
                uploadImageToFirebase(image_uri, crop, name_crop, description_crop, category_crop, organic_crop, price, stock, dialog);
            } else {
                // No new image selected, update other details only
                updateCropInDatabase(crop.getCrop_pic(), crop, name_crop, description_crop, category_crop, organic_crop, price, stock, dialog);
            }
        });

        // Deleting the crops
        btn_edit_Delete_crop.setOnClickListener(v -> {
            DatabaseReference cropRef = FirebaseDatabase.getInstance().getReference("Crops").child(crop.getCropId());
            cropRef.removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Crop deleted successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Failed to delete crop", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    // Image selection
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        ((Activity) context).startActivityForResult(intent, 100); // Use a constant for the request code
    }


    private void uploadImageToFirebase(Uri imageUri, crops_model crop, String name_crop, String description_crop, String category_crop, boolean organic_crop, double price, int stock, AlertDialog dialog) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("crop_images/" + crop.getCropId());
        storageRef.putFile(imageUri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Get the URL of the uploaded image
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    updateCropInDatabase(imageUrl, crop, name_crop, description_crop, category_crop, organic_crop, price, stock, dialog);
                });
            } else {
                Toast.makeText(context, "Image upload failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateCropInDatabase(String imageUrl, crops_model crop, String name_crop, String description_crop, String category_crop, boolean organic_crop, double price, int stock, AlertDialog dialog) {
        DatabaseReference cropRef = FirebaseDatabase.getInstance().getReference("Crops").child(crop.getCropId());
        crops_model updatedCrop = new crops_model(imageUrl, name_crop, description_crop, category_crop, organic_crop, stock, price, crop.getCropId());

        cropRef.setValue(updatedCrop).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(context, "Crop updated successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(context, "Failed to update crop", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Handle the result of image selection
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            image_uri = data.getData(); // Get the image URI
            edit_crop_image.setImageURI(image_uri); // Update the CircleImageView with the selected image
        }
    }




    //Loading the categories in the spinner
    private void load_categories(Spinner edit_crop_category, String selectedCategory) {
        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("categories");
        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> categoryList = new ArrayList<>();
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    String cat = categorySnapshot.child("categoryName").getValue(String.class);
                    categoryList.add(cat);
                }

                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, categoryList);
                categoryAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                edit_crop_category.setAdapter(categoryAdapter);

                // Set the spinner to the current crop's category
                int spinnerPosition = categoryAdapter.getPosition(selectedCategory);
                if (spinnerPosition != -1) {
                    edit_crop_category.setSelection(spinnerPosition);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "Failed to load category", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return cropList.size();
    }

    public void updateCropList(List<crops_model> updatedList) {
        this.cropList = updatedList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView crop_pic;
        TextView crop_name ,crop_category,crop_description,crop_price,crop_stock,crop_isOrganic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            crop_name=itemView.findViewById(R.id.crop_name);
            crop_category=itemView.findViewById(R.id.crop_category);
            crop_description=itemView.findViewById(R.id.crop_description);
            crop_price=itemView.findViewById(R.id.crop_price);
            crop_stock=itemView.findViewById(R.id.crop_stock);
            crop_isOrganic=itemView.findViewById(R.id.crop_isOrganic);
            crop_pic=itemView.findViewById(R.id.crop_pic);
        }
    }
}