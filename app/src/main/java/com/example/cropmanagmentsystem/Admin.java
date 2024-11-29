package com.example.cropmanagmentsystem;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Admin extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);
        bottomNavigationView=findViewById(R.id.Bottom_Naivgation_admin);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id =item.getItemId();
                if(id==R.id.nav_home_admin){
                    load_admin_freg(new admin_home_freg(),false);
                } else if (id==R.id.nav_add_admin) {
                    load_admin_freg(new admin_addCrop_freg(),false);
                } else if (id==R.id.nav_customers_admin) {
                    load_admin_freg(new admin_customers_freg(),false);
                } else if (id==R.id.nav_profile_admin) {
                    load_admin_freg(new customer_profile(),false);
                } else {
                    //for order details
                    load_admin_freg(new AdminOrderFragment(),true);
                }
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_home_admin);

    }
    // Function to load fragment
    private void load_admin_freg(Fragment fragment, boolean addToBackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        // Replace the fragment in the container
        ft.replace(R.id.container_admin, fragment);

        // If flag is true, add the fragment transaction to the back stack
        if (addToBackStack) {
            ft.addToBackStack(null);  // Null means it will use the default back stack name
        }

        // Commit the transaction
        ft.commit();
    }
}