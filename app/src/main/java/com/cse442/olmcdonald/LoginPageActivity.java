package com.cse442.olmcdonald;

import android.net.Uri;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class LoginPageActivity extends AppCompatActivity {
    EditText et_lname;
    EditText et_fname;
    EditText et_uname;
    EditText et_number;
    EditText et_zip;
    EditText et_email;
    Button but_submit;
    FirebaseUser user;
    FirebaseFirestore user_db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user = FirebaseAuth.getInstance().getCurrentUser();
        user_db= FirebaseFirestore.getInstance();
        init_view();
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
    }

    /**
     * Initiate view in the layout
     */
    private void init_view() {
        et_lname = findViewById(R.id.et_fname);
        et_fname = findViewById(R.id.et_lname);
        et_uname = findViewById(R.id.et_username);
        et_number = findViewById(R.id.et_number);
        et_zip = findViewById(R.id.et_zip);
        et_email = findViewById(R.id.et_email);
        final EditText[] etArray = new EditText[6];
        etArray[0] = et_lname; etArray[1] = et_fname; etArray[2] = et_uname; etArray[3] = et_number;
        etArray[4] = et_zip; etArray[5] = et_email;
        but_submit = findViewById(R.id.but_sub);
        but_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean b_empty = true;
                for(EditText et : etArray){
                    if(et.getText().toString().equals("")){
                        b_empty = false;
                        et.setError("Field cannot be empty");
                    }
                }
                if(b_empty){
                    checkUsername(et_uname.getText().toString());
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(et_uname.getText().toString())
                            .build();
                    user.updateEmail(et_email.getText().toString());
                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                    }
                                }
                            });
                }
            }
        });
    }

    /**
     * Check if the username is registered in the database
     * @param username The username input to be checked
     */
    public void checkUsername(final String username){
        user_db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        boolean track = true;
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                              Log.d(ConstantClass.TAG, document.getId() + " => " + document.getData());
                                if(document.getData().get("username").equals(username)){
                                    track = false;
                                }
                            }
                        } else {
                           Log.w(ConstantClass.TAG, "Error getting documents.", task.getException());
                        }

                        if (track){
                            update_userdb();
                            finish();
                        } else{
                            et_uname.setError("Username taken!");
                        }
                    }
                });
    }

    /**
     * Update the dataabase with the data from the Text Fields
     */
    private void update_userdb() {
        Toast.makeText(this, "Adding New User", Toast.LENGTH_SHORT).show();
        Map<String, Object> user = new HashMap<>();
        user.put("zipcode", et_zip.getText().toString());
        user.put("username", et_uname.getText().toString());
        user.put("first_name", et_fname.getText().toString());
        user.put("last_name", et_lname.getText().toString());
        user.put("email", et_email.getText().toString());
        user.put("phone", et_number.getText().toString());
        user_db.collection("users")
                .add(user)
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
    }

    /**
     * Handle multiple Activity started
     */
    @Override
    protected void onResume() {
        super.onResume();
        if(user.getDisplayName()!=null){
            finish();
        }
    }
}
