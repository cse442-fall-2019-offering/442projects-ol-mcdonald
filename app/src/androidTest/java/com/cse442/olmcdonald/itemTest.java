package com.cse442.olmcdonald;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * JUnit test for item database pulling
 */
@RunWith(AndroidJUnit4.class)
public class itemTest {
    FirebaseFirestore item_db; //Item Database Instance

    @Test
    public void pullRandomItem(){
        item_db = FirebaseFirestore.getInstance();
        item_db.collection("crops")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            assertNotEquals(0,0);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //itemNotNullTestData(document);
                                Map<String,Object> map_data = document.getData();
                                assertNotNull(map_data.get("price"));
                                assertNotNull(map_data.get("amount"));
                                assertNotNull(map_data.get("delivery"));
                                assertNotNull(map_data.get("zipcode"));
                                assertNotNull(map_data.get("total"));
                                assertNotNull(map_data.get("harvest"));
                                assertNotNull(map_data.get("img"));
                                assertNotNull(map_data.get("name"));
                                assertNotNull(map_data.get("species"));
                                assertNotEquals("",map_data.get("seller"));
                            }
                        } else {
                        }
                    }
                });

    }

}

