package com.example.cropmanagmentsystem;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link admin_addCrop_freg#newInstance} factory method to
 * create an instance of this fragment.
 */
public class admin_addCrop_freg extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private adminAddCrop_freg_ViewPager_adapter fregAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public admin_addCrop_freg() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment admin_addCrop_freg.
     */
    // TODO: Rename and change types and number of parameters
    public static admin_addCrop_freg newInstance(String param1, String param2) {
        admin_addCrop_freg fragment = new admin_addCrop_freg();
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
        View view =  inflater.inflate(R.layout.fragment_admin_add_crop_freg, container, false);
        tabLayout=view.findViewById(R.id.tablayout_admin);
        viewPager=view.findViewById(R.id.viewpager_admin);
        fregAdapter=new adminAddCrop_freg_ViewPager_adapter(getChildFragmentManager());
        viewPager.setAdapter(fregAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}