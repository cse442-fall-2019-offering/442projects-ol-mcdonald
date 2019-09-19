package com.cse442.olmcdonald;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {

    private EditText ID;
    private EditText Password;
    private Button SignUpButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        ID = (EditText)findViewById(R.id.idEditText);
        Password = (EditText)findViewById(R.id.passPassword);
        SignUpButton = (Button)findViewById(R.id.signUpButton);

        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if ID is available
                // if not, give a message
                // if yes, create an account in the database
            }
        });
    }

}
