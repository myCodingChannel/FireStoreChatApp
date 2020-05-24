package com.tectibet.firestorechatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tectibet.firestorechatapp.adapter.UserRecyclerViewAdapter;
import com.tectibet.firestorechatapp.domain.Users;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;
    private Toolbar mToolbar;
    List<Users> usersList;
    RecyclerView userRecyclerView;
    private UserRecyclerViewAdapter userRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth =FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        mToolbar =findViewById(R.id.toolbar);
        userRecyclerView =findViewById(R.id.user_recycler);
        usersList=new ArrayList<>();

        userRecyclerView.setHasFixedSize(true);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        userRecyclerViewAdapter =new UserRecyclerViewAdapter(this,usersList);
        userRecyclerView.setAdapter(userRecyclerViewAdapter);
        setSupportActionBar(mToolbar);

        mStore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){
                        String docId= documentSnapshot.getId();
                        if(!mAuth.getCurrentUser().getUid().equals(docId)){
                            Users user = documentSnapshot.toObject(Users.class).withId(docId);
                            usersList.add(user);
                            userRecyclerViewAdapter.notifyDataSetChanged();
                        }

                    }
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logout){
                Map<String,Object> map = new HashMap<>();
                map.put("login",false);
                mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                        .update(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                mAuth.signOut();
                                startActivity(new Intent(HomeActivity.this,MainActivity.class));
                                finish();
                            }
                        });


            return true;
        }
        return false;
    }
}
