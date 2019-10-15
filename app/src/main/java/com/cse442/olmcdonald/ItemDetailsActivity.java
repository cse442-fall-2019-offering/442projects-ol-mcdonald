package com.cse442.olmcdonald;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import com.cse442.olmcdonald.itemManager;

public class ItemDetailsActivity extends AppCompatActivity {
    TextView name;
    TextView species;
    TextView seller;
    TextView harvest;
    TextView zipcode;
    TextView delivery;
    TextView amount;
    TextView price;
    TextView total;
    ImageView image;
    Button buy;



    itemManager item_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);



        name = findViewById(R.id.Name);
        species = findViewById(R.id.Species);
        seller = findViewById(R.id.Seller);
        harvest = findViewById(R.id.Harvest);
        zipcode = findViewById(R.id.Zipcode);
        delivery = findViewById(R.id.Delivery);
        amount = findViewById(R.id.Amount);
        price = findViewById(R.id.Price);
        total = findViewById(R.id.Total);
        image = findViewById(R.id.Image);
        buy = findViewById(R.id.Buy);

        Intent intent = getIntent();
        //Item item = (Item) intent.getSerializableExtra("Item Selected");
        Item item = (Item) intent.getParcelableExtra("Item Selected");

        setDetails(item);
    }

    private void setDetails(Item item) {
        name.setText(item.getName());
        species.setText("Species: " + item.getSpecies());
        seller.setText("Seller: " + item.getSeller());
        harvest.setText("Harvest date: " + item.getHarvest_date());
        zipcode.setText("Crop's zipcode location: " + item.getZipcode());
        delivery.setText("Delivery distance: " + item.getDelivery_distance());
        amount.setText("Amount left to be sold: " + item.getAmount());
        price.setText("Price: " + item.getPrice());
        total.setText("per " + item.getTotal() + "unit(s)");

        item_manager = new itemManager();
        image.setImageBitmap((item.getImg_data()));
    }
}
