package com.cse442.olmcdonald;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.cse442.olmcdonald.ConstantClass.DB_CROPS;
import static com.cse442.olmcdonald.ConstantClass.DEBUG_TAG;

public class PurchasePage extends AppCompatActivity {

    TextView tv_title1;
    TextView tv_title2;
    EditText et_address;
    EditText et_city;
    EditText et_zipcode;
    Button but_purchase;
    Item item;
    FirebaseFirestore cropsDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_page);
        item = (Item) getIntent().getParcelableExtra("item");
        cropsDb = FirebaseFirestore.getInstance();
        tv_title1 = findViewById(R.id.purchase1);
        tv_title2 = findViewById(R.id.purchase2);
        et_address = findViewById(R.id.address);
        et_city = findViewById(R.id.city);
        et_zipcode = findViewById(R.id.zipcode);
        but_purchase = findViewById(R.id.purchase);

        but_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (et_address.getText().length() > 0 && et_city.getText().length() > 0 && et_zipcode.getText().length() > 0) {
                    cropsDb.collection(DB_CROPS).document(item.getId())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(DEBUG_TAG, "DocumentSnapshot successfully deleted!");
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(DEBUG_TAG, "Error deleting document", e);
                                }
                            });
                    Intent finalPurchase = new Intent(PurchasePage.this, SuccessPurchase.class);
                    startActivity(finalPurchase);
                    finish();
                } else  {
                    if (et_address.getText().length() == 0) {
                        et_address.setError("Please fill in the address to proceed");
                    }
                    if (et_city.getText().length() == 0) {
                        et_city.setError("Please fill in the address to proceed");
                    }
                    if (et_zipcode.getText().length() == 0) {
                        et_zipcode.setError("Please fill in the address to proceed");
                    }

                }

            }
        });

    }
}
