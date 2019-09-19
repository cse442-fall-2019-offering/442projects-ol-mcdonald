package com.cse442.olmcdonald;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * Activity for Farmer's products
 *
 * @Author Jonathan
 */
public class Farmer extends AppCompatActivity {
    TextView amount;
    TextView address;
    TextView description;
    TextView price;
    TextView product;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer);

        amount = findViewById(R.id.Amount);
        address = findViewById(R.id.Address);
        description = findViewById(R.id.Description);
        price = findViewById(R.id.Price);
        product = findViewById(R.id.Product);
        submit = findViewById(R.id.SubmitBtn);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    submit.setText("Added to market!");
            }
        });


    }
}
