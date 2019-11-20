package com.cse442.olmcdonald;

import android.os.Bundle;
import android.os.Handler;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import static com.cse442.olmcdonald.ConstantClass.DB_CROPS;
import static com.cse442.olmcdonald.ConstantClass.DB_SELLER;
import static com.cse442.olmcdonald.ConstantClass.DB_USERNAME;
import static com.cse442.olmcdonald.ConstantClass.DB_USER_EMAIL;
import static com.cse442.olmcdonald.ConstantClass.DB_USER_FNAME;
import static com.cse442.olmcdonald.ConstantClass.DB_USER_LNAME;
import static com.cse442.olmcdonald.ConstantClass.DB_USER_NAME;
import static com.cse442.olmcdonald.ConstantClass.DB_USER_PHONE;
import static com.cse442.olmcdonald.ConstantClass.DB_USER_ZIP;


/**
 * Handles user registration
 *
 */
public class LoginPageActivity extends AppCompatActivity {
    ViewDialog viewDialog;
    String curName;
    EditText et_lname;
    EditText et_fname;
    EditText et_uname;
    EditText et_number;
    EditText et_zip;
    EditText et_email;
    Button but_submit;
    FirebaseUser user;
    FirebaseFirestore userDb;
    FirebaseFirestore itemDb;
    OnCompleteListener onCompleteListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        curName = "";
        itemDb = FirebaseFirestore.getInstance();
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
                curName = user.getDisplayName();
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
        et_lname = findViewById(R.id.et_lname);
        et_fname = findViewById(R.id.et_fname);
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
                        Toast.makeText(LoginPageActivity.this, "Updating Profile!", Toast.LENGTH_SHORT).show();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(et_uname.getText().toString())
                                .build();
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            updateUser();
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
                if(viewDialog.isShowing()) {
                    viewDialog.closeDialog();
                    Toast.makeText(LoginPageActivity.this, "Timeout! Try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        }, 10000);
    }

    /**
     * Check if the username is registered in the database
     * @param username The username input to be checked
     */
    public void checkUsername(final String username){
        userDb.collection(DB_USERNAME).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        viewDialog.closeDialog();
                        boolean track = true;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getData().get(DB_USER_NAME).equals(username)){
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
        userMap.put(DB_USER_ZIP, et_zip.getText().toString());
        userMap.put(DB_USER_NAME, et_uname.getText().toString());
        userMap.put(DB_USER_FNAME, et_fname.getText().toString());
        userMap.put(DB_USER_LNAME, et_lname.getText().toString());
        userMap.put(DB_USER_EMAIL, et_email.getText().toString());
        userMap.put(DB_USER_PHONE, et_number.getText().toString());
        userDb.collection(DB_USERNAME)
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

    /**
     * Update username of the user in the databse
     */
    private void updateUser(){
        userDb.collection(DB_USERNAME).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        viewDialog.closeDialog();
                        String id = "";
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getData().get(DB_USER_NAME).equals(curName)){
                                    id = document.getId();
                                }
                            }
                        }
                        if (!id.equals("")){
                            userDb.collection(DB_USERNAME).document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    updateUserdb();
                                    updateItemUsername();
                                }
                            });
                        } else{
                            et_uname.setError("Edit Error!");
                        }
                    }});
    }

    /**
     * Update all entries of the items to the new seller username
     */
    private void updateItemUsername(){
        viewDialog.showDialog();

        itemDb.collection(DB_CROPS)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                int ref = task.getResult().size();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    ref--;
                                    if(ref == 0){
                                        Map h = document.getData();
                                        h.put(DB_SELLER,user.getDisplayName());
                                        itemDb.collection((DB_CROPS)).document(document.getId()).update(h).addOnSuccessListener(new OnSuccessListener() {
                                            @Override
                                            public void onSuccess(Object o) {
                                                viewDialog.closeDialog();
                                                finish();
                                            }
                                        });
                                    }
                                    if(document.getData().get(DB_SELLER).equals(curName)){
                                        Map h = document.getData();
                                        h.put(DB_SELLER,user.getDisplayName());
                                       itemDb.collection((DB_CROPS)).document(document.getId()).update(h);
                                    }
                                }

                            }
                        }
                    });
        }
}
