package com.cse442.olmcdonald;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * Handles user registration
 *
 */
public class LoginPageActivity extends AppCompatActivity {
    ViewDialog viewDialog;
    EditText et_lname;
    EditText et_fname;
    EditText et_uname;
    EditText et_number;
    EditText et_zip;
    EditText et_email;
    Button but_submit;
    FirebaseUser user;
    FirebaseFirestore userDb;
    OnCompleteListener onCompleteListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userDb = FirebaseFirestore.getInstance();
        initView();
        viewDialog = new ViewDialog(this);
        if(user!=null) {
            if (user.getEmail() != null) {
                et_email.setText(user.getEmail());
            }
            if (user.getPhoneNumber()!= null) {
                et_number.setText(user.getPhoneNumber());
            }
            if (user.getDisplayName() != null) {
                et_uname.setText(user.getDisplayName());
            }
        }
        onCompleteListener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(et_uname.getText().toString())
                        .build();
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    checkUsername(et_uname.getText().toString());
                                }
                            }
                        });
            }
        };
    }

    /**
     * Initiate view in the layout
     */
    private void initView() {
        et_lname = findViewById(R.id.et_fname);
        et_fname = findViewById(R.id.et_lname);
        et_uname = findViewById(R.id.et_username);
        et_number = findViewById(R.id.et_number);
        et_zip = findViewById(R.id.et_zip);
        et_email = findViewById(R.id.et_email);
        if(user.getPhoneNumber()!=null){
            et_number.setEnabled(false);
        }
        final EditText[] etArray = new EditText[6];
        etArray[0] = et_lname; etArray[1] = et_fname; etArray[2] = et_uname; etArray[3] = et_number;
        etArray[4] = et_zip; etArray[5] = et_email;
        but_submit = findViewById(R.id.but_sub);
        but_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewDialogShow();
                viewDialog.showDialog();
                boolean b_empty = true;
                for (EditText et : etArray) {
                    if (et.getText().toString().equals("")) {
                        b_empty = false;
                        et.setError("Field cannot be empty");
                        viewDialog.closeDialog();
                    }
                }
                if (b_empty) {
                    if (user.getEmail() == null) {
                        user.updateEmail(et_email.getText().toString()).addOnCompleteListener(onCompleteListener);
                    } else{
                        Toast.makeText(LoginPageActivity.this, "Updating Username!", Toast.LENGTH_SHORT).show();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(et_uname.getText().toString())
                                .build();
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            checkUsername(et_uname.getText().toString());
                                        }
                                    }});
                    }
                }
            }
        });
    }

    /**
     * Handle no internet connection or unable to update database
     */
    private void viewDialogShow() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewDialog.closeDialog();
                Toast.makeText(LoginPageActivity.this, "Timeout! Try again later.", Toast.LENGTH_SHORT).show();
            }
        }, 10000);
    }

    /**
     * Check if the username is registered in the database
     * @param username The username input to be checked
     */
    public void checkUsername(final String username){
        userDb.collection("users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        viewDialog.closeDialog();
                        boolean track = true;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getData().get("username").equals(username)){
                                    track = false;
                                }
                            }
                        }
                        if (track){
                            updateUserdb();
                            finish();
                        } else{
                            et_uname.setError("Username taken!");
                        }
                    }});
    }


    /**
     * Update the database with the data from the Text Fields
     */
    private void updateUserdb() {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("zipcode", et_zip.getText().toString());
        userMap.put("username", et_uname.getText().toString());
        userMap.put("first_name", et_fname.getText().toString());
        userMap.put("last_name", et_lname.getText().toString());
        userMap.put("email", et_email.getText().toString());
        userMap.put("phone", et_number.getText().toString());
        userDb.collection("users")
                .add(userMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }


}
