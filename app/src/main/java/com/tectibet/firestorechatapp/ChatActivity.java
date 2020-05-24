package com.tectibet.firestorechatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.tectibet.firestorechatapp.adapter.ChatRecyclerAdapter;
import com.tectibet.firestorechatapp.domain.Chat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    String id ="";
    String fcm = "";
    boolean login=false;
    RecyclerView chatRecyclerView;
    EditText mMsgText;
    private ImageView mSendBtn;
    FirebaseFirestore mStore;
    FirebaseAuth mAuth;
    private List<Chat> mChatList;
    ChatRecyclerAdapter chatRecyclerAdapter;
    RequestQueue mQueue;
    String name= "";
    String sender_fcm="";
    public static boolean isOpen = true;
    public static final String URL = "https://fcm.googleapis.com/fcm/send";

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isOpen = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isOpen = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        id = getIntent().getStringExtra("id");
        fcm = getIntent().getStringExtra("fcm");
        login = getIntent().getBooleanExtra("login",false);
        isOpen = true;

        chatRecyclerView = findViewById(R.id.char_recycler  );
        mMsgText = findViewById(R.id.message_text  );
        mSendBtn = findViewById(R.id.send_btn  );
        mStore =FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mChatList=new ArrayList<>();
        chatRecyclerView.setHasFixedSize(true);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mQueue = Volley.newRequestQueue(this);
        chatRecyclerAdapter = new ChatRecyclerAdapter(this,mChatList);
        chatRecyclerView.setAdapter(chatRecyclerAdapter);
        fetchUserDetail();
        fetchMessage();


        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String msg = mMsgText.getText().toString();
                if(!msg.isEmpty()){
                    final Map<String,Object> map = new HashMap<>();
                    map.put("message",msg);
                    map.put("time_stamp",new Date());
                    map.put("from",mAuth.getCurrentUser().getUid());
                    map.put("to",id);
                    mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                            .collection("UserMessages").document(id).collection("Chat").add(map)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                       if(task.isSuccessful()){
                           mStore.collection("Users").document(id)
                                   .collection("UserMessages").document(mAuth.getCurrentUser()
                                   .getUid()).collection("Chat").add(map)
                                   .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                       @Override
                                       public void onComplete(@NonNull Task<DocumentReference> task) {
                                           if(task.isSuccessful()){
                                               JSONObject object = new JSONObject();
                                               JSONObject innerObject = new JSONObject();
                                               try {
                                                   innerObject.put("user_id",mAuth.getCurrentUser().getUid());
                                                   innerObject.put("user",name);
                                                   innerObject.put("message",msg);
                                                   innerObject.put("sender_fcm",sender_fcm);
                                                   object.put("data",innerObject);
                                                   object.put("to",fcm);
                                                   sendPushNotification(object);

                                               } catch (JSONException e) {
                                                   e.printStackTrace();
                                               }

                                               Toast.makeText(ChatActivity.this, "Message sent!", Toast.LENGTH_SHORT).show();
                                               mMsgText.setText("");
                                           }
                                       }
                                   });
                       }
                        }
                    });
                }
            }
        });



    }

    private void sendPushNotification(JSONObject object) {
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String ,String> map = new HashMap<>();
                map.put("authorization","key=AAAAr7pw2Rs:APA91bEkz861nGQa5ewarjz94ON_NxXnQ8vDkYIG7zZJs8aKt3y6kq15L3J5r07b2pjpF60eNtEG12VBSeNgqVSGLrB8LeZXwsX-XjWe_rLfabL3FFSL1l2bQ17fK0Zsx1yG-_3vJP82");
                map.put("Content-Type","application/json");
                return map;
            }
        };
        mQueue.add(objectRequest);
    }

    private void fetchUserDetail() {
        mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
           if(task.isSuccessful()){
               name = task.getResult().getString("name");
               sender_fcm = task.getResult().getString("fcm");
           }
            }
        });
    }

    private void fetchMessage() {
        mStore.collection("Users").document(mAuth.getCurrentUser().getUid())
                .collection("UserMessages")
                .document(id)
                .collection("Chat")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges()){
                            Chat chat =doc.getDocument().toObject(Chat.class);
                            mChatList.add(chat);
                            chatRecyclerAdapter.notifyDataSetChanged();

                        }
                    }
                });
    }
}
