package com.example.cropmanagmentsystem;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class registration extends AppCompatActivity {
    private TextView LoginBtn_reg;
    private EditText Username_reg, Email_reg, Adress_reg, Number_reg, Password_reg;
    private Button Singup_reg;
    private CircleImageView Profile_reg;
    private Uri imageURI;
    private String image_uri;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private FirebaseStorage storage;

    private ProgressBar progressBar;
    private TextView progressText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        LoginBtn_reg = findViewById(R.id.btnRegLogin);
        Username_reg = findViewById(R.id.regUsername);
        Email_reg = findViewById(R.id.regEmail);
        Adress_reg = findViewById(R.id.regAdress);
        Number_reg = findViewById(R.id.regNumber);
        Password_reg = findViewById(R.id.regPassword);
        Singup_reg = findViewById(R.id.btnRegSingup);
        Profile_reg = findViewById(R.id.regProfile);
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progressText);


        LoginBtn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registration.this, login.class);
                startActivity(intent);
                finish();
            }
        });

        Singup_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = Username_reg.getText().toString();
                String email = Email_reg.getText().toString();
                String adress = Adress_reg.getText().toString();
                String number = Number_reg.getText().toString();
                String password = Password_reg.getText().toString();

                // Validate form fields
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(adress) || TextUtils.isEmpty(number) || TextUtils.isEmpty(password)) {
                    Toast.makeText(registration.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else if (!email.matches(emailPattern)) {
                    Email_reg.setError("Enter a valid email");
                } else if (number.length() != 10 || !number.matches("\\d{10}")) { // Validate phone number (simple 10-digit check)
                    Number_reg.setError("Enter a valid 10-digit phone number");
                } else if (password.length() < 6) {
                    Password_reg.setError("Password must be at least 6 characters");
                } else {

                    // Show the progress bar
                    findViewById(R.id.customProgressBar).setVisibility(View.VISIBLE);

                    // Set initial progress to 0
                    progressBar.setProgress(0);
                    progressText.setText("0%");

                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String id = task.getResult().getUser().getUid();

                                // Update progress bar
                                progressBar.setProgress(50);
                                progressText.setText("50%");

                                DatabaseReference reference = database.getReference().child("user").child(id);
                                StorageReference storageReference = storage.getReference().child("Upload").child(id);
                                if (imageURI != null) {
                                    storageReference.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String image_uri = uri.toString();  // URL of the uploaded image

                                                        // Create a new user object with all parameters
                                                        Users user = new Users(id, image_uri, email, username, password, adress, number);

                                                        // Save the user object to the Firebase Database
                                                        reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    // Update progress to 100% when done
                                                                    progressBar.setProgress(100);
                                                                    progressText.setText("100%");
                                                                    // Redirect to MainActivity after successful registration
                                                                    Intent intent = new Intent(registration.this, login.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                } else {
                                                                    Toast.makeText(registration.this, "error in creating Account", Toast.LENGTH_SHORT).show();

                                                                    findViewById(R.id.customProgressBar).setVisibility(View.GONE);

                                                                }
                                                            }
                                                        });
                                                    }
                                                });

                                            }
                                        }
                                    });
                                } else {
                                    image_uri = "https://firebasestorage.googleapis.com/v0/b/cropmanagmentsystem.appspot.com/o/b.png?alt=media&token=44c354ad-c875-423c-b2e3-adf335403370";
                                    Users user = new Users(id, image_uri, email, username, password, adress, number);
                                    reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Redirect to MainActivity after successful registration
                                                Intent intent = new Intent(registration.this, login.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(registration.this, "error in creating Account", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(registration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

                }
            }
        });

        //for getting image from gallery
        Profile_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Your Picture"), 10);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (data != null) {
                imageURI = data.getData();
                Profile_reg.setImageURI(imageURI);
            }
        }
    }
}
