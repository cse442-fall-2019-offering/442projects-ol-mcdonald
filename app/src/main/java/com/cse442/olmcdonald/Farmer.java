package com.cse442.olmcdonald;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Activity for Farmer's products
 *
 * @Author Jonathan
 */
public class Farmer extends AppCompatActivity {
    EditText amount;
    EditText address;
    EditText description;
    EditText price;
    EditText product;
    Button submit;
    Button upload;
    final EditText[] editArray = new EditText[6];
    FirebaseUser user;
    FirebaseFirestore user_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer);

        amount = findViewById(R.id.Amount);
        address = findViewById(R.id.Address);
        description = findViewById(R.id.Description);
        price = findViewById(R.id.Price);
        product = findViewById(R.id.Product);
        submit = (Button) findViewById(R.id.SubmitBtn);

        user = FirebaseAuth.getInstance().getCurrentUser();
        user_db = FirebaseFirestore.getInstance();
        upload = (Button) findViewById(R.id.img_Up);

        editArray[0] = amount;
        editArray[1] = address;
        editArray[2] = description;
        editArray[3] = price;
        editArray[4] = product;

    }

    /**
     * Once submit button is clicked it calls addProduct()
     * to add to database
     *
     * @param view
     */
    public void submit_button(View view) {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean not_empty = check_fields_empty();
                if (not_empty) {
                    addItem();
                }
            }
        });

    }

    /**
     * Uploads image of product to the database
     *
     * @param view
     */
    public void upload_img(View view) {
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload.setText("DONE!");
            }
        });
    }

    /**
     * Adds to the database for the product the user wants to sell
     */
    public void addItem() {
        Map<String, Object> crop = new HashMap<>();
        crop.put("product", product.getText().toString());
        crop.put("price", price.getText().toString());
        crop.put("amount", amount.getText().toString());
        crop.put("zipcode", address.getText().toString());
        crop.put("description", description.getText().toString());

        user_db.collection("crops")
                .add(crop)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(Farmer.this, "Added to Market", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Farmer.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    /**
     * Checks if fields are not empty
     *
     * @return b_empty
     */

    public boolean check_fields_empty() {
        boolean b_empty = true;
        for (EditText et : editArray) {
            if (et.getText().toString().equals("")) {
                b_empty = false;
                et.setError("Field cannot be empty");
            }
        }
        return b_empty;
    }
}
