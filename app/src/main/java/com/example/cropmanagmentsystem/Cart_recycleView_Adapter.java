package com.example.cropmanagmentsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Cart_recycleView_Adapter extends RecyclerView.Adapter<Cart_recycleView_Adapter.ViewHolder> {
    private Context context;

    public Cart_recycleView_Adapter(Context context, List<Cart_Item_model> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    private List<Cart_Item_model> cartList;
    @NonNull
    @Override
    public Cart_recycleView_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.cart_item_cardview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Cart_recycleView_Adapter.ViewHolder holder, int position) {
        Cart_Item_model cart=cartList.get(position);
        holder.cartItemName.setText(cart.getCropName());
        holder.cartItemQuantity.setText("Quantity : " +cart.getQuantity() +" Kg");
        holder.cartItemTotalPrice.setText("Price: Rs. "+cart.getTotalPrice());
        Glide.with(context).load(cart.getCropImage()).into(holder.cartItemImage);

        holder.removeItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("cart").child(userId).child(cart.getCropId());
                cartRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Item removed", Toast.LENGTH_SHORT).show();
                        // Remove the item from the list and notify adapter
                        cartList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, cartList.size());
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cartItemImage;
        TextView cartItemName, cartItemQuantity, cartItemTotalPrice;
        ImageButton removeItemButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cartItemImage = itemView.findViewById(R.id.customer_image_cart_item);
            cartItemName = itemView.findViewById(R.id.customer_text_cart_item_name);
            cartItemQuantity = itemView.findViewById(R.id.customer_text_cart_item_quantity);
            cartItemTotalPrice = itemView.findViewById(R.id.customer_text_cart_item_price);
            removeItemButton = itemView.findViewById(R.id.customer_button_remove_item);

        }
    }
}
