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
    private TextView amount;
    private TextView address;
    private TextView description;
    private TextView price;
    private TextView product;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer);

        amount = (TextView) findViewById(R.id.Amount);
        address = (TextView) findViewById(R.id.Address);
        description = (TextView) findViewById(R.id.Description);
        price = (TextView) findViewById(R.id.Price);
        product = (TextView) findViewById(R.id.Product);
        submit = (Button) findViewById(R.id.SubmitBtn);

    }
    public void submit_button(View view) {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit.setText("ADDED TO MARKET!");
            }
        });

    }
}
