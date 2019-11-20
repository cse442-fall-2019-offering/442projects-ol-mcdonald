package com.cse442.olmcdonald;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.os.Bundle;
import android.view.MenuItem;

import static com.cse442.olmcdonald.ConstantClass.DB_CROPS;

public class Transactions extends AppCompatActivity {
    FirebaseUser user;
    FirebaseFirestore itemDb;
    ViewDialog vDialog;
    Button btn_listings;
    Button btn_transactions;
    ScrollView scrollDisplay;
    LinearLayout layout_Display;
    ArrayList<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        itemDb = FirebaseFirestore.getInstance();
        btn_listings = findViewById(R.id.btn_listings);
        btn_transactions = findViewById(R.id.btn_transactions);
        scrollDisplay = findViewById(R.id.scrollDisplay);
        layout_Display = findViewById(R.id.layout_display);
        vDialog = new ViewDialog(this);
        btn_listings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListings();
            }
        });

        Intent intent = getIntent();
        items =  intent.getParcelableExtra("Items");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
    }

    public void showListings() {
        scrollDisplay.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        layout_Display.setLayoutParams(linearParams);
        // ADD TO LAYOUT AND SET SCROLL VISIBLE
        for(Item i : items){
            if(i.getSeller().equals(user.getDisplayName())){
                TextView list = new TextView(Transactions.this);
                list.setSingleLine(false);
                list.setLayoutParams(new TableLayout.LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT, 1f));
                list.setText("Product: " + i.getName() + "\n"
                        + "Amount: " + i.getAmount() + "\n"
                        + "Price: " + i.getPrice());
                layout_Display.addView(list);
            }
        }
        scrollDisplay.addView(layout_Display);

    }

}
