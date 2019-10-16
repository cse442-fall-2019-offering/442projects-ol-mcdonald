package com.cse442.olmcdonald;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.cse442.olmcdonald.ConstantClass.DB_AMOUNT;
import static com.cse442.olmcdonald.ConstantClass.DB_CROPS;
import static com.cse442.olmcdonald.ConstantClass.DB_DELIVERY;
import static com.cse442.olmcdonald.ConstantClass.DB_HARVEST;
import static com.cse442.olmcdonald.ConstantClass.DB_IMG;
import static com.cse442.olmcdonald.ConstantClass.DB_NAME;
import static com.cse442.olmcdonald.ConstantClass.DB_PRICE;
import static com.cse442.olmcdonald.ConstantClass.DB_SELLER;
import static com.cse442.olmcdonald.ConstantClass.DB_SPECIES;
import static com.cse442.olmcdonald.ConstantClass.DB_TOTAL;
import static com.cse442.olmcdonald.ConstantClass.DB_ZIPCODE;

/**
 * Activity for FarmerActivity's products
 *
 */
public class FarmerActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE =1;
    EditText et_amount;
    EditText et_zip;
    EditText et_date;
    EditText et_price;
    EditText et_name;
    EditText et_total;
    EditText et_delivery;
    EditText et_species;
    Button but_submit;
    Button but_upload_img;
    ViewDialog viewDialog;

    final EditText[] editArray = new EditText[8];
    FirebaseUser user;
    FirebaseFirestore cropsDb;
    ImageView img_product;
    Uri img_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer);
        viewDialog = new ViewDialog(this);
        et_amount = findViewById(R.id.et_amount);
        et_zip = findViewById(R.id.et_zip);
        et_date = findViewById(R.id.et_date);
        et_price = findViewById(R.id.et_price);
        et_name = findViewById(R.id.et_name);
        et_total = findViewById(R.id.et_total);
        et_delivery = findViewById(R.id.et_delivery);
        et_species = findViewById(R.id.et_species);
        but_submit = findViewById(R.id.but_submit);
        img_product = findViewById(R.id.img_product);
        user = FirebaseAuth.getInstance().getCurrentUser();
        cropsDb = FirebaseFirestore.getInstance();
        but_upload_img = findViewById(R.id.but_upload_img);
        but_upload_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });
        but_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean not_empty = check_fields_empty();
                if (not_empty) {
                    viewDialog.showDialog();
                    addItem();
                }
            }
        });
        editArray[0] = et_amount;
        editArray[1] = et_zip;
        editArray[2] = et_date;
        editArray[3] = et_price;
        editArray[4] = et_name;
        editArray[5] = et_total;
        editArray[6] = et_delivery;
        editArray[7] = et_species;
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
        Bitmap decoded;
        Bitmap compressedBitmap;
        //Convert image to Base64 with compression
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), img_uri);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, out);
            decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
            compressedBitmap = Bitmap.createScaledBitmap(decoded, decoded.getWidth()/10,decoded.getHeight()/10, false);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error adding image!", Toast.LENGTH_SHORT).show();
            return;
        }
        String ba =itemManager.bitmapToBase64(compressedBitmap);
        Map<String, Object> crop = new HashMap<>();
        crop.put(DB_NAME, et_name.getText().toString());
        crop.put(DB_SELLER, user.getDisplayName());
        crop.put(DB_PRICE, et_price.getText().toString());
        crop.put(DB_AMOUNT, et_amount.getText().toString());
        crop.put(DB_ZIPCODE, et_zip.getText().toString());
        crop.put(DB_DELIVERY, et_delivery.getText().toString());
        crop.put(DB_HARVEST, et_date.getText().toString());
        crop.put(DB_SPECIES, et_date.getText().toString());
        crop.put(DB_TOTAL, et_total.getText().toString());
        crop.put(DB_IMG,ba);
        cropsDb.collection(DB_CROPS)
                .add(crop)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(FarmerActivity.this, "Added to Market", Toast.LENGTH_SHORT).show();
                        viewDialog.closeDialog();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FarmerActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        viewDialog.closeDialog();
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
