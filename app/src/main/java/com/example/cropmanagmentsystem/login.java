package com.example.cropmanagmentsystem;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
EditText email ,password;
Button logbtn;
FirebaseAuth auth;
String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
TextView registration_log;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        auth = FirebaseAuth.getInstance();

        // Check if the user is already logged in
        if (auth.getCurrentUser() != null) {
            // Get the current user ID
            String userId = auth.getCurrentUser().getUid();

            // Redirect based on user ID
            if (userId.equals("U8sTOwcbLehCcUWRx6MCBbZMnBC3")) {
                // Redirect admin user
                Intent intent = new Intent(login.this, Admin.class);
                startActivity(intent);
                finish();
            } else {
                // Redirect regular user
                Intent intent = new Intent(login.this, customer.class);
                startActivity(intent);
                finish();
            }
        }


        setContentView(R.layout.activity_login);

        email=findViewById(R.id.logEmail);
        password=findViewById(R.id.logpassword);
        logbtn=findViewById(R.id.logButton);
        registration_log=findViewById(R.id.btnLogSignup);

        registration_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(login.this,registration.class);
                startActivity(i);
                finish();
            }
        });


        auth=FirebaseAuth.getInstance();

        logbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString();
                String Password = password.getText().toString();


                if((TextUtils.isEmpty(Email))){
                    Toast.makeText(login.this,"Enter The Email",Toast.LENGTH_SHORT).show();
                } else if ((TextUtils.isEmpty(Password))){
                    Toast.makeText(login.this,"Enter The Password",Toast.LENGTH_SHORT).show();
                } else if (!Email.matches(emailPattern)) {
                    email.setError("Give Proper Email");
                } else if (password.length()<6) {
                    password. setError ("Give Password More Then Six Characters");
                    //Toast.makeText(login.this,"Give Password More Then Six Characters",Toast.LENGTH_SHORT).show();
                }else {
                    auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Get the current user
                                String userId = auth.getCurrentUser().getUid();

                                if (userId.equals("U8sTOwcbLehCcUWRx6MCBbZMnBC3")) {
                                    // If the user is the admin
                                    Intent intent = new Intent(login.this, Admin.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // For all other users
                                    Intent intent = new Intent(login.this, customer.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                Toast.makeText(login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }



            }
        });


    }
}