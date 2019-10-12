package com.cse442.olmcdonald;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
 * Activity for FarmerActivity's products
 *
 * @Author Jonathan
 */
public class FarmerActivity extends AppCompatActivity {
    EditText et_amount;
    EditText et_address;
    EditText et_desc;
    EditText et_price;
    EditText et_name;
    Button but_submit;
    Button but_upload_img;
    final EditText[] editArray = new EditText[5];
    FirebaseUser user;
    FirebaseFirestore cropsDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer);

        et_amount = findViewById(R.id.et_amount);
        et_address = findViewById(R.id.et_address);
        et_desc = findViewById(R.id.et_desc);
        et_price = findViewById(R.id.et_price);
        et_name = findViewById(R.id.et_name);
        but_submit = findViewById(R.id.but_submit);

        user = FirebaseAuth.getInstance().getCurrentUser();
        cropsDb = FirebaseFirestore.getInstance();
        but_upload_img = findViewById(R.id.but_upload_img);

        editArray[0] = et_amount;
        editArray[1] = et_address;
        editArray[2] = et_desc;
        editArray[3] = et_price;
        editArray[4] = et_name;

    }

    /**
     * Once but_submit button is clicked it calls addProduct()
     * to add to database
     *
     * @param view
     */
    public void submit_button(View view) {
        but_submit.setOnClickListener(new View.OnClickListener() {
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
     * Uploads image of et_name to the database
     *
     * @param view
     */
    public void upload_img(View view) {
        but_upload_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                but_upload_img.setText("DONE!");
            }
        });
    }

    /**
     * Adds to the database for the et_name the user wants to sell
     */
    public void addItem() {
        Map<String, Object> crop = new HashMap<>();
        crop.put("name", et_name.getText().toString());
        crop.put("price", et_price.getText().toString());
        crop.put("amount", et_amount.getText().toString());
        crop.put("zipcode", et_address.getText().toString());
        crop.put("desc", et_desc.getText().toString());

        cropsDb.collection("crops")
                .add(crop)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(FarmerActivity.this, "Added to Market", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FarmerActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
