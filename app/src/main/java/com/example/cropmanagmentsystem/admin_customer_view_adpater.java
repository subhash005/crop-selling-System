package com.example.cropmanagmentsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
        holder.userName.setText(user.getUserName());
        holder.userMail.setText(user.getMail());

        // Load user profile pic from Firebase storage URL using Glide or Picasso
        Glide.with(context)
                .load(user.getProfilepic())
                .placeholder(R.drawable.b) // Default placeholder image
                .into(holder.userProfilePic);
    }

    @Override
    public int getItemCount() {
        return userList.size(); // Return the total number of users
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView userProfilePic;
        TextView userName, userMail;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userProfilePic = itemView.findViewById(R.id.userProfilePic);
            userName = itemView.findViewById(R.id.userName);
            userMail = itemView.findViewById(R.id.userMail);
        }
    }
}
