package com.cse442.olmcdonald;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    private static final int RESULT_LOAD_IMAGE =1;
    EditText et_amount;
    EditText et_zip;
    EditText et_date;
    EditText et_price;
    EditText et_name;
    EditText et_total;
    Button but_submit;
    Button but_upload_img;

    final EditText[] editArray = new EditText[6];
    FirebaseUser user;
    FirebaseFirestore cropsDb;
    ImageView img_product;
    Uri img_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer);

        et_amount = findViewById(R.id.et_amount);
        et_zip = findViewById(R.id.et_zip);
        et_date = findViewById(R.id.et_date);
        et_price = findViewById(R.id.et_price);
        et_name = findViewById(R.id.et_name);
        et_total = findViewById(R.id.et_total);
        but_submit = findViewById(R.id.but_submit);

        user = FirebaseAuth.getInstance().getCurrentUser();
        cropsDb = FirebaseFirestore.getInstance();
        but_upload_img = findViewById(R.id.but_upload_img);


        editArray[0] = et_amount;
        editArray[1] = et_zip;
        editArray[2] = et_date;
        editArray[3] = et_price;
        editArray[4] = et_name;
        editArray[5] = et_total;

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
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                if(img_uri != null){
                    but_upload_img.setText("DONE!");
                }
                else{
                    Toast.makeText(FarmerActivity.this,"No File Selected", Toast.LENGTH_SHORT);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            img_uri= data.getData();
            img_product.setImageURI(img_uri);
        }
    }

    /**
     * Adds to the database for the et_name the user wants to sell
     */
    public void addItem() {

        img_product.setImageBitmap(itemManager.base64ToBitmap(img_product.toString()));
        Map<String, Object> crop = new HashMap<>();
        crop.put("name", et_name.getText().toString());
        crop.put("price", Float.valueOf(et_price.getText().toString()));
        crop.put("amount", Integer.valueOf(et_amount.getText().toString()));
        crop.put("zipcode", Integer.valueOf(et_zip.getText().toString()));
        crop.put("harvest", et_date.getText().toString());
        crop.put("total", Float.valueOf(et_total.getText().toString()));
        crop.put("img",img_product);


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
