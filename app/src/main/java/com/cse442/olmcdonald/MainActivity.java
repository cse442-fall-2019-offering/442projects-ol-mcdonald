package com.cse442.olmcdonald;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    LinearLayout peachview, blueberryview,tomatoview,spinachview,appleview,cherryview,figview,grapeview,orangeview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ZipActivity zipActivity = new ZipActivity(this);
        zipActivity.show();

        FloatingActionButton fob = findViewById(R.id.float_add);
        fob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Farmer.class);
                startActivity(intent);
            }
        });

        peachview=findViewById(R.id.peachview);
        blueberryview=findViewById(R.id.blueberryview);
        tomatoview=findViewById(R.id.tomatoview);
        spinachview=findViewById(R.id.spinachview);
        appleview=findViewById(R.id.appleview);
        cherryview=findViewById(R.id.cherryview);
        figview=findViewById(R.id.figview);
        grapeview=findViewById(R.id.grapeview);
        orangeview=findViewById(R.id.orangeview);

        peachview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(MainActivity.this,peachActivity.class) ;
               startActivity(intent);
            }
        });
        blueberryview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,blueberryActivity.class) ;
                startActivity(intent);
            }
        });

        tomatoview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,tomatoActivity.class) ;
                startActivity(intent);
            }
        });

        spinachview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,spinachActivity.class) ;
                startActivity(intent);
            }
        });

        appleview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,appleActivity.class) ;
                startActivity(intent);
            }
        });

        cherryview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,cherryActivity.class) ;
                startActivity(intent);
            }
        });

        figview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,figpage.class) ;
                startActivity(intent);
            }
        });

        grapeview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,grapeActivity.class) ;
                startActivity(intent);
            }
        });

        orangeview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,orangeActivity.class) ;
                startActivity(intent);
            }
        });




    }
}
