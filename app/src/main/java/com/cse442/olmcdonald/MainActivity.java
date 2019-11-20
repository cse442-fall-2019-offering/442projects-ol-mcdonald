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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

import static com.cse442.olmcdonald.ConstantClass.DB_CROPS;
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
    EditText et_zipcode;
    Button but_zipcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gridview = findViewById(R.id.gridview);
        tv_marketword = findViewById(R.id.tv_marketword);
        ct_layout = findViewById(R.id.ct_layout);
        itemDb = FirebaseFirestore.getInstance();
        itemArrayList = new ArrayList<>();
        readItemFirebase(false);
        tv_username = findViewById(R.id.txt_username);
        et_zipcode=findViewById(R.id.et_zipcode);
        user = FirebaseAuth.getInstance().getCurrentUser();
        but_zipcode=findViewById(R.id.but_zipcode);
        itemAdapter = new ItemAdapter(this,itemArrayList);
        gridview.setAdapter(itemAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent cropDetails = new Intent(MainActivity.this, ItemDetailsActivity.class);
                Item selectedCrop = itemArrayList.get(position);
                cropDetails.putExtra("Item Selected", selectedCrop);
                startActivity(cropDetails);
            }
        });

        FloatingActionButton fob = findViewById(R.id.fab);
        fob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FarmerActivity.class);
                startActivity(intent);
            }
        });
        but_zipcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zipcodestrings= et_zipcode.getText().toString();
                if(zipcodestrings.equals("")){
                    readItemFirebase(false);

                }else {
                    readItemFirebase(true);
                }

            }
        });
    }

    /**
     * it filters the item in the array by the range of the user typed in zipcode
     */
    private void zipCodeFinder(){
        for(int i=itemArrayList.size()-1;i>=0;i--){
            String zipcodestring= et_zipcode.getText().toString();
            int zipcode=Integer.parseInt(zipcodestring);
            if((zipcode+20)<itemArrayList.get(i).getZipcode() ||  (zipcode-20)>itemArrayList.get(i).getZipcode()){
                itemArrayList.remove(i);
            }
        }
        itemAdapter.notifyDataSetChanged();
    }

    /**
     *
     * Read the crops from the database and populate listView
     * @param reset true to reset the item with zipcode filter
     */
    private void readItemFirebase(final boolean reset) {
        itemDb.collection(DB_CROPS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                           itemArrayList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Item i = new Item(document);
                                itemArrayList.add(i);
                            }
                            if(reset){
                                zipCodeFinder();
                            }
                            itemAdapter.notifyDataSetChanged();
                        }
                        if(!itemArrayList.isEmpty()){
                            ct_layout.removeView(tv_marketword);
                        }
                        if(gridview.getAdapter()==null) {
                            gridview.setAdapter(itemAdapter);
                        }
                    }
                });
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
            case R.id.refresh:
                readItemFirebase(false);
                return true;
            case R.id.setting:
                return true;
            case R.id.login_out:
                FirebaseAuth.getInstance().signOut();
                finish();
                return true;
            case R.id.editProfile:
                Intent intent = new Intent(MainActivity.this,LoginPageActivity.class);
                startActivity(intent);
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
        readItemFirebase(false);
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
