package com.example.cropmanagmentsystem;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class customer_cart extends Fragment {
    private RecyclerView cartRecyclerView;
    private TextView total_price;
    private List<Cart_Item_model> cartList;
    private Cart_recycleView_Adapter cartRecycleViewAdapter;
    private DatabaseReference cartRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_customer_cart, container, false);
        cartRecyclerView=view.findViewById(R.id.customer_recycler_view_cart);
        total_price=view.findViewById(R.id.text_total_price);

        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartList=new ArrayList<>();
        cartRecycleViewAdapter = new Cart_recycleView_Adapter(getContext(), cartList);
        cartRecyclerView.setAdapter(cartRecycleViewAdapter);

        loadCartData();

        return view;
    }

    private void loadCartData() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        cartRef = FirebaseDatabase.getInstance().getReference("cart").child(userId);

        // Fetch cart data
        cartRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                cartList.clear(); // Clear existing data to prevent duplication
                double totalPrice = 0;

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Cart_Item_model cart = data.getValue(Cart_Item_model.class); // Map data to CartModel
                    if (cart != null) {
                        cartList.add(cart);
                        totalPrice += cart.getTotalPrice(); // Assuming getTotalPrice() exists in CartModel
                    }
                }

                // Update total price and notify adapter
                total_price.setText("Total: Rs. " + String.format("%.2f", totalPrice));
                cartRecycleViewAdapter.notifyDataSetChanged();
            }
        });
    }
}