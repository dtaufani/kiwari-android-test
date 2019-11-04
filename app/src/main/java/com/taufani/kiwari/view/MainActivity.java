package com.taufani.kiwari.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.taufani.kiwari.R;
import com.taufani.kiwari.adapter.ChatAdapter;
import com.taufani.kiwari.model.ChatModel;
import com.taufani.kiwari.model.UserModel;
import com.taufani.kiwari.utilities.SessionManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    EditText mTextMessage;

    private CollectionReference mCollectionReference;
    private List<ChatModel> mChatModels;
    private ChatAdapter mAdapter;
    private SessionManager mSession;

    public static final String TAG = "ChatRoomKiwari";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTextMessage = (EditText) findViewById(R.id.message);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        mCollectionReference = FirebaseFirestore.getInstance()
                .collection("chats")
                .document("threads")
                .collection("jarjit@mail.com-ismail@mail.com"); // it should improve
        mChatModels = new ArrayList<>();
        mSession = new SessionManager(this);
        mAdapter = new ChatAdapter(this, mChatModels);
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCollectionReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (documentSnapshots.isEmpty()) {
                    return;
                } else {
                    List<ChatModel> chats = documentSnapshots.toObjects(ChatModel.class);
                    mChatModels.clear();
                    mChatModels.addAll(chats);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sign_out) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void submit() {
        String message = mTextMessage.getText().toString().trim();
        if (message != null && !message.isEmpty()) {
            UserModel user = new Gson().fromJson(mSession.getSharedPref("User"), UserModel.class);
            ChatModel chat = new ChatModel();
            chat.setSenderName(user.getName());
            chat.setSenderEmail(user.getEmail());
            chat.setMessage(message);
            chat.setCreatedDate(new Date());

            mCollectionReference.add(chat).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    mTextMessage.setText("");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Message not saved", e);
                }
            });
        }
    }

    private void logout() {
        mSession.setLogin(false);
        mSession.removeSharedPref("User");

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
