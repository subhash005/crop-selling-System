package com.example.cropmanagmentsystem;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class customer_cart extends Fragment {
    private RecyclerView cartRecyclerView;
    private TextView total_price;
    private List<Cart_Item_model> cartList;
    private Cart_recycleView_Adapter cartRecycleViewAdapter;
    private DatabaseReference cartRef;
    private Button button_buy;
    private double totalPrice = 0; // To store the total price

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_cart, container, false);

        cartRecyclerView = view.findViewById(R.id.customer_recycler_view_cart);
        total_price = view.findViewById(R.id.text_total_price);
        button_buy = view.findViewById(R.id.button_buy);

        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartList = new ArrayList<>();
        cartRecycleViewAdapter = new Cart_recycleView_Adapter(getContext(), cartList);
        cartRecyclerView.setAdapter(cartRecycleViewAdapter);

        loadCartData();

        button_buy.setOnClickListener(v -> {
            for (Cart_Item_model cartItem : cartList) {
                String cropName = cartItem.getCropName();
                int cartQuantity = cartItem.getQuantity();
                checkStockAvailability(cropName, cartQuantity);
            }
        });

        return view;
    }

    private void checkStockAvailability(String cropName, int cartQuantity) {
        DatabaseReference cropsRef = FirebaseDatabase.getInstance().getReference("Crops");

        cropsRef.orderByChild("crop_name").equalTo(cropName).get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                for (DataSnapshot cropSnapshot : dataSnapshot.getChildren()) {
                    int cropStock = cropSnapshot.child("crop_stock").getValue(int.class);

                    if (cartQuantity <= cropStock) {
                        processPayment();
                        updateCropStock(cropSnapshot.getKey(), cropStock - cartQuantity);
                        return;
                    } else {
                        Toast.makeText(getContext(), "Insufficient stock for " + cropName, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            } else {
                Log.d("StockCheck", "No crop found with name: " + cropName);
            }
        }).addOnFailureListener(e -> Log.e("StockCheck", "Failed to fetch crop details", e));
    }

    private void processPayment() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Select Payment Method")
                .setItems(new String[]{"Cash on Delivery", "Online Payment"}, (dialog, which) -> {
                    String paymentStatus = "Not Paid";
                    String paymentMethod = "Cash on Delivery";
                    String paymentDateTime = "Pending";
                    String paymentID = "NA";

                    if (which == 0) {
                        // Cash on Delivery selected
                        updatePaymentStatus(paymentStatus, paymentMethod, paymentDateTime, paymentID);
                    } else {
                        // Online Payment selected, initiate Razorpay payment
                        PaymentNow();
                    }
                })
                .show();
    }


    private void updateCropStock(String cropId, int newStock) {
        DatabaseReference cropsRef = FirebaseDatabase.getInstance().getReference("Crops").child(cropId);

        cropsRef.child("crop_stock").setValue(newStock).addOnSuccessListener(aVoid -> Log.d("StockUpdate", "Stock updated successfully for crop ID: " + cropId))
                .addOnFailureListener(e -> Log.e("StockUpdate", "Failed to update stock for crop ID: " + cropId, e));
    }

    private void loadCartData() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        cartRef = FirebaseDatabase.getInstance().getReference("cart").child(userId);

        cartRef.get().addOnSuccessListener(dataSnapshot -> {
            cartList.clear();
            totalPrice = 0;

            for (DataSnapshot data : dataSnapshot.getChildren()) {
                Cart_Item_model cart = data.getValue(Cart_Item_model.class);
                if (cart != null) {
                    cartList.add(cart);
                    totalPrice += cart.getTotalPrice();
                }
            }

            total_price.setText("Total: Rs. " + String.format("%.2f", totalPrice));
            cartRecycleViewAdapter.notifyDataSetChanged();
        });
    }

    private void PaymentNow() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_QkibI9jBzMIk5v");

        double finalAmount = totalPrice * 100;

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Subhash");
            options.put("description", "Order Payment");
            options.put("currency", "INR");
            options.put("image", "http://example.com/image/rzp.jpg");
            options.put("theme.color", "#55983E");
            options.put("amount", finalAmount);
            options.put("prefill.email", "kanha.x@example.com");
            options.put("prefill.contact", "8279766744");

            checkout.open(requireActivity(), options);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error in payment initialization", Toast.LENGTH_SHORT).show();
        }
    }

    public void updatePaymentStatus(String paymentStatus, String paymentMethod, String paymentDateTime, String paymentID) {
        recordOrder(paymentMethod, paymentStatus, paymentDateTime, paymentID);
        clearCart();

        if (paymentStatus.equals("Not Paid")) {
            Toast.makeText(getContext(), "Order placed successfully with Cash on Delivery!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Order placed successfully with Online Payment!", Toast.LENGTH_SHORT).show();
        }
    }

    private void recordOrder(String paymentMethod, String paymentStatus, String paymentDateTime, String paymentID) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");
        String orderId = ordersRef.push().getKey();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user").child(userId);

        userRef.get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                String userName = dataSnapshot.child("userName").getValue(String.class);
                String userNumber = dataSnapshot.child("number").getValue(String.class);
                String deliveryAddress = dataSnapshot.child("address").getValue(String.class);

                List<Map<String, Object>> cartItems = new ArrayList<>();
                for (Cart_Item_model cartItem : cartList) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("cropName", cartItem.getCropName());
                    item.put("cropImage", cartItem.getCropImage());
                    item.put("quantity", cartItem.getQuantity());
                    item.put("pricePerKg", cartItem.getPricePerKg());
                    item.put("totalPrice", cartItem.getTotalPrice());
                    cartItems.add(item);
                }

                Map<String, Object> orderDetails = new HashMap<>();
                orderDetails.put("orderId", orderId);  // Add the orderId here
                orderDetails.put("userId", userId);
                orderDetails.put("userName", userName);
                orderDetails.put("userNumber", userNumber);
                orderDetails.put("deliveryAddress", deliveryAddress);
                orderDetails.put("cartItems", cartItems);
                orderDetails.put("totalAmount", totalPrice);
                orderDetails.put("paymentMethod", paymentMethod);
                orderDetails.put("paymentStatus", paymentStatus);
                orderDetails.put("paymentID", paymentID);
                orderDetails.put("paymentDateTime", paymentDateTime);
                orderDetails.put("deliveryStatus", "Pending");


                long timestamp = System.currentTimeMillis();
                Date orderDate = new Date(timestamp);
                SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
                String month = monthFormat.format(orderDate);
                String year = yearFormat.format(orderDate);
                // Save to Firebase
                orderDetails.put("orderDateTimes", timestamp);
                orderDetails.put("orderMonth", month);
                orderDetails.put("orderYear", year);


                // Save the order in Firebase
                if (orderId != null) {
                    ordersRef.child(orderId).setValue(orderDetails).addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show();
                        clearCart(); // Clear the cart after order placement
                    }).addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to record order. Please try again.", Toast.LENGTH_SHORT).show();
                    });
                }
            } else {
                Log.e("UserDetails", "User details not found in database");
            }
        }).addOnFailureListener(e -> {
            Log.e("UserDetails", "Failed to fetch user details", e);
        });
    }

    private void clearCart() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        cartRef = FirebaseDatabase.getInstance().getReference("cart").child(userId);

        cartRef.removeValue().addOnSuccessListener(aVoid -> {
            cartList.clear();
            totalPrice = 0;

            total_price.setText("Total: Rs. 0.00");
            cartRecycleViewAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to clear cart. Please try again.", Toast.LENGTH_SHORT).show());
    }
}