package com.example.cropmanagmentsystem;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class category_adapter extends RecyclerView.Adapter<category_adapter.ViewHolder> {
    private Context context;
    private List<Category> CategoryList;

    public category_adapter(Context context, List<Category> CategoryList) {
        this.CategoryList = CategoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public category_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull category_adapter.ViewHolder holder, int position) {
        Category cat = CategoryList.get(position);
        holder.category_card_name.setText(cat.getCategoryName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryDailog(cat);
            }
        });
    }

//    private void showCategoryDailog(Category category) {
//        View dailogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_category,null);
//        AlertDialog.Builder builder =new AlertDialog.Builder(context);
//        builder.setView(dailogView);
//        AlertDialog dialog=builder.create();
//        dialog.show();
//
//
//        EditText edit_category_name = dailogView.findViewById(R.id.edit_category_name);
//        Button btn_update_category=dailogView.findViewById(R.id.btn_update_category);
//        Button btn_delete_category = dailogView.findViewById(R.id.btn_delete_category);
//
//        edit_category_name.setText(category.getCategoryName());
//
//
//        btn_update_category.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String newCategoryName =edit_category_name.getText().toString();
//                if(!newCategoryName.isEmpty()){
//                    DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("categories").child(category.getCategoryId());
//                    categoryRef.child("categoryName").setValue(newCategoryName);
//                    dialog.dismiss();
//                    Toast.makeText(context, "Category Name is Upadted", Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(context, "Enter the Category Name", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        btn_delete_category.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("categories").child(category.getCategoryId());
//                categoryRef.removeValue();
//                dialog.dismiss();
//                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//
//    }


    private void showCategoryDailog(Category category) {
        if (category == null) {
            Toast.makeText(context, "Invalid category data", Toast.LENGTH_SHORT).show();
            return;  // Safeguard if category data is null
        }

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_category, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        EditText edit_category_name = dialogView.findViewById(R.id.edit_category_name);
        Button btn_update_category = dialogView.findViewById(R.id.btn_update_category);
        Button btn_delete_category = dialogView.findViewById(R.id.btn_delete_category);

        // Populate category name
        edit_category_name.setText(category.getCategoryName());

        btn_update_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newCategoryName = edit_category_name.getText().toString().trim();

                if (!newCategoryName.isEmpty()) {

                        DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("categories")
                                .child(category.getCategoryId());

                        // Update the category name in Firebase
                        categoryRef.child("categoryName").setValue(newCategoryName)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(context, "Category updated successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "Failed to update category", Toast.LENGTH_SHORT).show();
                                        }
                                        dialog.dismiss(); // Close the dialog regardless
                                    }
                                });
                } else {
                    Toast.makeText(context, "Enter the category name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_delete_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DatabaseReference categoryRef = FirebaseDatabase.getInstance().getReference("categories")
                            .child(category.getCategoryId());

                    // Remove category from Firebase
                    categoryRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Category deleted successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Failed to delete category", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss(); // Close the dialog regardless
                        }
                    });
                } catch (Exception e) {
                    // Handle unexpected errors
                    Toast.makeText(context, "An error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("CategoryDialog", "Error deleting category", e);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return CategoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView category_card_name; // Declare as a member variable

        public ViewHolder(View itemView) {
            super(itemView);
            category_card_name = itemView.findViewById(R.id.category_card_name); // Initialize after super call
        }
    }
}
