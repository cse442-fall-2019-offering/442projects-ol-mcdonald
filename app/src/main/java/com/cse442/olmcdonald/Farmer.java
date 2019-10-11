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
    private EditText amount;
    private EditText address;
    private EditText description;
    private EditText price;
    private EditText product;
    private Button submit;
    private Button upload;
    final EditText[] editArray = new EditText[6];

    FirebaseUser user;
    FirebaseFirestore user_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer);

        amount = (EditText) findViewById(R.id.Amount);
        address = (EditText) findViewById(R.id.Address);
        description = (EditText) findViewById(R.id.Description);
        price = (EditText) findViewById(R.id.Price);
        product = (EditText) findViewById(R.id.Product);
        submit = (Button) findViewById(R.id.SubmitBtn);

        user = FirebaseAuth.getInstance().getCurrentUser();
        user_db = FirebaseFirestore.getInstance();
        //upload = (Button) findViewById(R.id.Img_Up);


        editArray[0] = amount;
        editArray[1] = address;
        editArray[2] = description;
        editArray[3] = price;
        editArray[4] = product;

    }

    public void submit_button(View view) {


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b_empty = check_fields_empty();
                Map<String, Object> crop = new HashMap<>();
                crop.put("product", product.getText().toString());
                crop.put("price", price.getText().toString());
                crop.put("amount", amount.getText().toString());
                crop.put("zipcode", address.getText().toString());
                crop.put("description", description.getText().toString());
                if(b_empty) {
                    user_db.collection("crops")
                            .add(crop)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(ConstantClass.TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(ConstantClass.TAG, "Error adding document", e);
                                }
                            });

                    submit.setText("ADDED TO MARKET");
                    finish();
                }
            }
        });

    }

    public void upload_img(View view) {
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload.setText("DONE!");
            }
        });
    }

    public boolean check_fields_empty(){

        boolean b_empty = true;
        for(EditText et : editArray){
            if(et.getText().toString().equals("")){
                b_empty = false;
                et.setError("Field cannot be empty");
            }
        }
        return b_empty;
    }
}
