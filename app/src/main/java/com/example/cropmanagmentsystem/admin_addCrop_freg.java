package com.example.cropmanagmentsystem;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;


public class admin_addCrop_freg extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private adminAddCrop_freg_ViewPager_adapter fregAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_admin_add_crop_freg, container, false);
        tabLayout=view.findViewById(R.id.tablayout_admin);
        viewPager=view.findViewById(R.id.viewpager_admin);
        fregAdapter=new adminAddCrop_freg_ViewPager_adapter(getChildFragmentManager());
        viewPager.setAdapter(fregAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}