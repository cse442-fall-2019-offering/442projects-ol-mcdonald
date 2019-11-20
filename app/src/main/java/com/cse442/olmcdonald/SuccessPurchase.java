package com.cse442.olmcdonald;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SuccessPurchase extends AppCompatActivity {

    TextView tv_redirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_purchase);

        tv_redirect = findViewById(R.id.redirectlink);

        tv_redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent redirect = new Intent(SuccessPurchase.this, MainActivity.class);
                startActivity(redirect);
                finish();
            }
        });
    }
}
