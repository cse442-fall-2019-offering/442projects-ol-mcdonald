package com.cse442.olmcdonald;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static com.cse442.olmcdonald.ConstantClass.RC_SIGN_IN;

/**
 * MainActivity handles marketplace
 *
 */
public class MainActivity extends AppCompatActivity {
    TextView tv_username;
    TextView tv_marketword;
    FirebaseUser user;
    FirebaseFirestore itemDb;
    ArrayList<Item> itemArrayList;
    ItemAdapter itemAdapter;
    ConstraintLayout ct_layout;
    GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        gridview = findViewById(R.id.gridview);
        tv_marketword = findViewById(R.id.tv_marketword);
        ct_layout = findViewById(R.id.ct_layout);
        setSupportActionBar(toolbar);
        itemDb = FirebaseFirestore.getInstance();
        itemArrayList = readItemFirebase();
        tv_username = findViewById(R.id.txt_username);
        user = FirebaseAuth.getInstance().getCurrentUser();

        itemAdapter = new ItemAdapter(this,itemArrayList);
        gridview.setAdapter(itemAdapter);

        FloatingActionButton fob = findViewById(R.id.fab);
        fob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FarmerActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Read the crops from the database
     * @return ArrayList of items
     */
    private ArrayList<Item> readItemFirebase() {
        final ArrayList<Item> retVal = new ArrayList<>();
        itemDb.collection("crops")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Item i = new Item(document);
                                retVal.add(i);
                                itemAdapter.notifyDataSetChanged();

                            }
                        } else {
                        }
                        if(!retVal.isEmpty()){
                            ct_layout.removeView(tv_marketword);
                        }
                    }
                });
        return retVal;
    }

    /**
     * Initiate login activity
     */
    private void start_login(){
        List<AuthUI.IdpConfig> providers = Arrays.asList(
             //   new AuthUI.IdpConfig.EmailBuilder().build(),
                //new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build());
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Successfully signed in
            } else {

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.setting:
                return true;
            case R.id.login_out:
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Override resume to check if user is login, if not initiate login
     */
    @Override
    protected void onResume() {
        super.onResume();
        user = FirebaseAuth.getInstance().getCurrentUser();
        readItemFirebase();
        if(user==null) {
            Toast.makeText(this, "Welcome, Please Sign up", Toast.LENGTH_SHORT).show();
            start_login();
        } else  {
            String name = user.getDisplayName();
            String phone = user.getPhoneNumber();
            if (name == null || name.equals("") || phone == null || phone.equals("")) {
                Intent intent = new Intent(MainActivity.this, LoginPageActivity.class);
                startActivity(intent);
            } else {
                tv_username.setText("Welcome " + name + "!");
            }
        }
    }
}
