package com.tectibet.firestorechatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;
    private Button mRegBtn;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mName=findViewById(R.id.reg_name);
        mEmail=findViewById(R.id.reg_email);
        mPassword=findViewById(R.id.reg_password);
        mRegBtn=findViewById(R.id.log_btn);
        mAuth=FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        mToolbar=findViewById(R.id.reg_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name=mName.getText().toString();
                String email=mEmail.getText().toString();
                String password=mPassword.getText().toString();
                if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty()){
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String token = getSharedPreferences("TOKEN",MODE_PRIVATE).getString("key","");
                                Map<String,Object> map = new HashMap<>();
                                map.put("name",name);
                                map.put("fcm",token);
                                map.put("login",true);
                                mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                                        .set(map)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(RegisterActivity.this, "Account successfully created", Toast.LENGTH_SHORT).show();
                                                Intent intent=new Intent(RegisterActivity.this,HomeActivity.class);
                                                startActivity(intent);
                                            }
                                        });

                            }else{
                                Toast.makeText(RegisterActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(RegisterActivity.this, "Please fill empty field", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void signIn(View view) {
        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
    }
}
