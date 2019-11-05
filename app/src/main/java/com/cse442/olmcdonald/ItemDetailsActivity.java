package com.cse442.olmcdonald;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ItemDetailsActivity extends AppCompatActivity {
    TextView name;
    TextView species;
    TextView seller;
    TextView harvest;
    TextView zipcode;
    TextView delivery;
    TextView amount;
    TextView total_price;
    ImageView image;
    Button buy;
    FloatingActionButton fab;
    FirebaseUser user;

    itemManager item_manager;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        fab = findViewById(R.id.fab_edit);
        user = FirebaseAuth.getInstance().getCurrentUser();
        name = findViewById(R.id.Name);
        species = findViewById(R.id.Species);
        seller = findViewById(R.id.Seller);
        harvest = findViewById(R.id.Harvest);
        zipcode = findViewById(R.id.Zipcode);
        delivery = findViewById(R.id.Delivery);
        amount = findViewById(R.id.Amount);
        total_price = findViewById(R.id.TotalPrice);
        image = findViewById(R.id.Image);
        buy = findViewById(R.id.Buy);

        Intent intent = getIntent();
        final Item item = (Item) intent.getParcelableExtra("Item Selected");

        setDetails(item);
        if(user.getDisplayName().equals(item.getSeller())) {
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ItemDetailsActivity.this,FarmerActivity.class);
                    intent.putExtra("item",item);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private void setDetails(Item item) {
        name.setText(item.getName());
        species.setText("Species: " + item.getSpecies());
        seller.setText("Seller: " + item.getSeller());
        harvest.setText("Harvest date: " + item.getHarvest_date());
        zipcode.setText("Crop's zipcode location: " + item.getZipcode());
        delivery.setText("Delivery distance: " + item.getDelivery_distance());
        amount.setText("Amount left to be sold: " + item.getAmount());
        total_price.setText("Price: " + item.getPrice() + "per " + item.getTotal() + "unit(s)");
        item_manager = new itemManager();
        image.setImageBitmap((item.getImg_data()));
    }
}
