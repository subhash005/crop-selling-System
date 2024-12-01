package com.example.cropmanagmentsystem;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class admin_customer_view_adpater extends RecyclerView.Adapter<admin_customer_view_adpater.UserViewHolder> {

    private ArrayList<Users> userList;
    private Context context;

    public admin_customer_view_adpater(ArrayList<Users> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the customer_list_card layout
        View view = LayoutInflater.from(context).inflate(R.layout.customer_list_card, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        // Bind user data to views
        Users user = userList.get(position);
        holder.userName.setText(user.getUserName().trim());
        holder.userMail.setText(user.getMail().trim());
        holder.userAdress.setText(user.getAddress().trim());
        holder.userNumber.setText(user.getNumber().trim());

        // Load user profile pic from Firebase storage URL using Glide or Picasso
        Glide.with(context)
                .load(user.getProfilepic())
                .placeholder(R.drawable.b) // Default placeholder image
                .into(holder.userProfilePic);

        // Set OnClickListener for item click to show options dialog
        holder.itemView.setOnClickListener(v -> showAlertDialog(user));
    }

    @Override
    public int getItemCount() {
        return userList.size(); // Return the total number of users
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView userProfilePic;
        TextView userName, userMail, userNumber, userAdress;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userProfilePic = itemView.findViewById(R.id.userProfilePic);
            userName = itemView.findViewById(R.id.userName);
            userMail = itemView.findViewById(R.id.userMail);
            userNumber = itemView.findViewById(R.id.userNumber);
            userAdress = itemView.findViewById(R.id.userAdress);
        }
    }

    // Method to show AlertDialog with options when a user item is clicked
    private void showAlertDialog(Users user) {
        // Create an AlertDialog with options
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select an Action");

        // Add options to the dialog
        builder.setItems(new String[]{"View Order Details", "View Customer Details"}, (dialog, which) -> {
            switch (which) {
                case 0:
                    // View Order Details - Open the order details activity for this user
                    Intent intent = new Intent(context, customer_ka_OrderDetailsActivity.class);
                    intent.putExtra("userId", user.getUserId());
                    context.startActivity(intent);
                    break;
                case 1:
                    // View Customer Details - Show a dialog with customer details
                    viewCustomerDetails(user);
                    break;
            }
        });

        // Make the dialog cancelable (clicking outside or back button closes it)
        builder.setCancelable(true);
        builder.show();
    }

    // Method to show a custom dialog displaying customer details
    private void viewCustomerDetails(Users user) {
        // Inflate the dialog view
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dailog_view_customers, null);

        // Initialize the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        builder.setCancelable(true); // Make the dialog dismissible
        // Create the dialog
        AlertDialog dialog = builder.create();

        // Show the dialog
        dialog.show();

        // Find views in the dialog layout
        ImageView profilePic = dialogView.findViewById(R.id.viewUserProfilePic);
        TextView userName = dialogView.findViewById(R.id.viewUserName);
        TextView userMail = dialogView.findViewById(R.id.viewUserMail);
        TextView userNumber = dialogView.findViewById(R.id.viewUserNumber);
        TextView userAddress = dialogView.findViewById(R.id.viewUserAdress);
        Button closeDialog = dialogView.findViewById(R.id.viewCloseDialog);

        // Set user data in dialog
        userName.setText(user.getUserName());
        userMail.setText(user.getMail());
        userNumber.setText(user.getNumber());
        userAddress.setText(user.getAddress());

        // Load user profile picture
        Glide.with(context)
                .load(user.getProfilepic())
                .placeholder(R.drawable.b)
                .into(profilePic);

        // Close button action
        closeDialog.setOnClickListener(v -> dialog.dismiss());
    }
}