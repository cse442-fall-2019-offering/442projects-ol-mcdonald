package com.cse442.olmcdonald;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginPageActivity extends AppCompatActivity {

    private EditText ID;
    private EditText Password;
    private Button LoginButton;
    private Button SignUpButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ID = (EditText)findViewById(R.id.idEditText);
        Password = (EditText)findViewById(R.id.passPassword);
        LoginButton = (Button)findViewById(R.id.loginButton);
        SignUpButton = (Button)findViewById(R.id.signUpButton);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(ID.getText().toString(), Password.getText().toString());
            }
        });

        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPageActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    private void validate (String id, String userPassword) {
        if (id.equals("admin") && userPassword.equals("1234")) {
            // go to another activity (Sprint #5)
        } else {
            ID.setText("");
            Password.setText("");
        }
    }

}
