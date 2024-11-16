package com.example.cropmanagmentsystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class customer_profile extends Fragment {
    private Button logout;
    private ImageView imageView_profile;
    private TextView profile_name;
    private TextView profile_email;
    private TextView profile_addres;
    private TextView profile_number;
    private TextView profile_password;
    private FirebaseAuth auth;
    private CircleImageView choose_image_newProfile;

    private Button editProfileButton;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customer_profile, container, false);
        logout = v.findViewById(R.id.customer_logout);
        imageView_profile = v.findViewById(R.id.imageView_profile);
        profile_name = v.findViewById(R.id.profile_name);
        profile_email = v.findViewById(R.id.profile_email);
        profile_addres = v.findViewById(R.id.profile_addres);
        profile_number = v.findViewById(R.id.profile_number);
        profile_password = v.findViewById(R.id.profile_password);

        fetchUser_Profiledetail();

        auth = FirebaseAuth.getInstance();
        String very = auth.getCurrentUser().getUid();
        Log.d("verifying", "this is " + very);

        editProfileButton = v.findViewById(R.id.user_profile_edit);
        editProfileButton.setOnClickListener(v1 -> showEditProfileDialog());

        logout.setOnClickListener(v1 -> logout());

        return v;
    }

    private void fetchUser_Profiledetail() {
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            String userid = auth.getCurrentUser().getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user").child(userid);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String address = snapshot.child("address").getValue(String.class);
                    profile_addres.setText(address);
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
                    } else {
                        imageView_profile.setImageResource(R.drawable.b);
                        Toast.makeText(getActivity(), "image is not loaded", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("FirebaseError", error.getMessage());
                }
            });
        }
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), login.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void showEditProfileDialog() {
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit_profile, null);
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setTitle("Edit Profile")
                .setCancelable(true)
                .create();

        EditText editUsername = dialogView.findViewById(R.id.edit_username);
        EditText editEmail = dialogView.findViewById(R.id.edit_email);
        EditText editAddress = dialogView.findViewById(R.id.edit_address);
        EditText editNumber = dialogView.findViewById(R.id.edit_number);
        EditText editPassword = dialogView.findViewById(R.id.edit_password);
        choose_image_newProfile = dialogView.findViewById(R.id.choose_image_newProfile);
        Button buttonSave = dialogView.findViewById(R.id.button_save);

        // Set existing user data to dialog fields
        editUsername.setText(profile_name.getText().toString());
        editEmail.setText(profile_email.getText().toString());
        editAddress.setText(profile_addres.getText().toString());
        editNumber.setText(profile_number.getText().toString());
        editPassword.setText(profile_password.getText().toString());

        choose_image_newProfile.setOnClickListener(v -> chooseImage());

        buttonSave.setOnClickListener(v -> {
            String newUsername = editUsername.getText().toString();
            String newEmail = editEmail.getText().toString();
            String newAddress = editAddress.getText().toString();
            String newNumber = editNumber.getText().toString();
            String newPassword = editPassword.getText().toString();

            updateUserProfile(newUsername, newEmail, newAddress, newNumber, newPassword);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void updateUserProfile(String username, String email, String address, String number, String password) {
        String userId = auth.getCurrentUser().getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user").child(userId);

        // Update user details in the database
        ref.child("userName").setValue(username);
        ref.child("mail").setValue(email);
        ref.child("address").setValue(address);
        ref.child("number").setValue(number);
        ref.child("password").setValue(password);

        // If there's an image selected, upload it to Firebase Storage
        if (imageUri != null) {
            uploadImageToFirebase(userId, username, email, address, number, password);
        } else {
            // If no image is selected, show a success message
            Toast.makeText(getActivity(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
        }

        // Re-authenticate user with new credentials (current email and entered password)
        FirebaseUser user = auth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password); // Use the current email and entered password

        user.reauthenticate(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Proceed with updating email and password
            } else {
                // Log the error to understand the issue
                Log.e("ReauthenticationError", "Error: " + task.getException().getMessage());
                Toast.makeText(getActivity(), "Reauthentication failed", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void uploadImageToFirebase(String userId, String username, String email, String address, String number, String password) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("profile_pics").child(userId + ".jpg");
        storageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL of the uploaded image
                    storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user").child(userId);
                        ref.child("profilepic").setValue(imageUrl); // Update the profile image URL in the database

                        // Now call the updateProfile method again with the new image URL
                        ref.child("userName").setValue(username);
                        ref.child("mail").setValue(email);
                        ref.child("address").setValue(address);
                        ref.child("number").setValue(number);
                        ref.child("password").setValue(password);




                        // Show a success message
                        Toast.makeText(getActivity(), "Profile image updated successfully", Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle any errors during the image upload
                    Toast.makeText(getActivity(), "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();

            // Ensure that choose_image_newProfile is initialized
            if (choose_image_newProfile != null) {
                Glide.with(getActivity())
                        .load(imageUri)  // Load the selected image URI
                        .circleCrop()    // This makes the image circular
                        .into(choose_image_newProfile);
            } else {
                Log.e("Image Load Error", "choose_image_newProfile is null.");
            }
        }
    }
}
