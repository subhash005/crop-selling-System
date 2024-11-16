package com.example.cropmanagmentsystem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class customer_crop_recycleView_adapter extends RecyclerView.Adapter<customer_crop_recycleView_adapter.ViewHolder> {
    private Context context;
    private List<crops_model> cropList;

    //variable delcaring for cart dialogBox
    private ImageView customer_cart_dialogBox_cropImage;
    private TextView customer_cart_dialogBox_cropName,customer_cart_dialog_textView_total_price;
    private EditText customer_cart_dialogBox_quantityKg;
    private Button customer_cart_dialogBox_addToCartButton;

    customer_crop_recycleView_adapter(Context context ,List<crops_model> cropList){
        this.context=context;
        this.cropList=cropList;

    }
    @NonNull
    @Override
    public customer_crop_recycleView_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.crop_cardview,parent,false);
        customer_crop_recycleView_adapter.ViewHolder viewHolder=new customer_crop_recycleView_adapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull customer_crop_recycleView_adapter.ViewHolder holder, int position) {
        crops_model crop = cropList.get(position);
        holder.crop_name.setText(crop.getCrop_name());
        holder.crop_category.setText(crop.getCrop_category());
        holder.crop_description.setText(crop.getCrop_description());
        holder.crop_price.setText(String.valueOf(crop.getCrop_price()));
        holder.crop_stock.setText(String.valueOf(crop.getCrop_stock()));
        holder.crop_isOrganic.setText(crop.isCrop_isOrganic() ? "Organic" : "Not Organic"); // Use isCrop_isOrganic()
        Glide.with(context).load(crop.getCrop_pic()).into(holder.crop_pic);

        // Show dialog when a crop item is clicked
        holder.itemView.setOnClickListener(view -> {
            showAddToCartDialog(crop);
        });


    }

    private void showAddToCartDialog(crops_model crop) {
        final Dialog dialog =new Dialog(context);
        dialog.setContentView(R.layout.dialog_add_to_cart);

        // Set the crop image and name in the dialog
         customer_cart_dialogBox_cropImage = dialog.findViewById(R.id.customer_cart_dialog_crop_image);
         customer_cart_dialogBox_cropName = dialog.findViewById(R.id.customer_cart_dialog_crop_name);
         customer_cart_dialogBox_quantityKg = dialog.findViewById(R.id.customer_cart_dialog_quantity_kg);
         customer_cart_dialog_textView_total_price = dialog.findViewById(R.id.customer_cart_dialog_textView_total_price);
        customer_cart_dialogBox_addToCartButton = dialog.findViewById(R.id.customer_cart_dialog_add_to_cart_button);

        customer_cart_dialogBox_cropName.setText(crop.getCrop_name());
        // Set the maximum stock value as the hint in the quantity input field
        int maxStock = crop.getCrop_stock();
        customer_cart_dialogBox_quantityKg.setHint("Max Available : " + maxStock + " kg");

        // Load image using Glide with error handling
        Glide.with(context)
                .load(crop.getCrop_pic()) // Crop image URL
                .placeholder(R.drawable.leaf2) // Placeholder while loading
                .error(R.drawable.b) // Error image if loading fails
                .into(customer_cart_dialogBox_cropImage);


// Set a listener to update total price as user enters quantity
        customer_cart_dialogBox_quantityKg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String quantityStr = s.toString().trim();
                if (!quantityStr.isEmpty()) {
                    try {
                        int quantity = Integer.parseInt(quantityStr);
                        double totalPrice = crop.getCrop_price() * quantity;
                        customer_cart_dialog_textView_total_price.setText(String.format("Total Price: Rs. %.2f", totalPrice));
                    } catch (NumberFormatException e) {
                        customer_cart_dialog_textView_total_price.setText("Invalid input");
                    }
                } else {
                    customer_cart_dialog_textView_total_price.setText("Total Price: Rs. 0");
                }
            }
        });


        // Show dialog
        dialog.show();

        customer_cart_dialogBox_addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantitytext=customer_cart_dialogBox_quantityKg.getText().toString().trim();
                if(!quantitytext.isEmpty()){
                    // Check if the quantity is within the available stock
                    int quantity=Integer.parseInt(quantitytext);
                    int stockAvailbal=crop.getCrop_stock();
                    if(quantity<=stockAvailbal){
                        double totalPrice = crop.getCrop_price() * quantity; // Calculate total price
                        saveToCart(crop,quantity,totalPrice);
                        Toast.makeText(context, "Added to cart!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }else {
                        Toast.makeText(context, "Quantity exceeds stock available!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Enter a quantity!", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    private void saveToCart(crops_model crop , int quantity , double totalPrice){
        // geting the firebase userId
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference cartref = FirebaseDatabase.getInstance().getReference("cart").child(userId);

        // crearting new Cart id for new cart
        String cartId=cartref.push().getKey();


        // Create cart entry with crop details, quantity, and total price
        HashMap<String, Object> cartEntry = new HashMap<>();
        cartEntry.put("cropId", crop.getCropId());
        cartEntry.put("cropName", crop.getCrop_name());
        cartEntry.put("cropImage", crop.getCrop_pic());
        cartEntry.put("quantity", quantity);
        cartEntry.put("pricePerKg", crop.getCrop_price());
        cartEntry.put("totalPrice", totalPrice);

        // Save to Firebase
        cartref.child(cartId).setValue(cartEntry)
                .addOnSuccessListener(aVoid -> Log.d("Cart", "Item added successfully"))
                .addOnFailureListener(e -> Log.d("Cart", "Failed to add item: " + e.getMessage()));

    }

    @Override
    public int getItemCount() {
        return cropList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView crop_pic;
        TextView crop_name ,crop_category,crop_description,crop_price,crop_stock,crop_isOrganic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            crop_name=itemView.findViewById(R.id.crop_name);
            crop_category=itemView.findViewById(R.id.crop_category);
            crop_description=itemView.findViewById(R.id.crop_description);
            crop_price=itemView.findViewById(R.id.crop_price);
            crop_stock=itemView.findViewById(R.id.crop_stock);
            crop_isOrganic=itemView.findViewById(R.id.crop_isOrganic);
            crop_pic=itemView.findViewById(R.id.crop_pic);
        }
    }
}
