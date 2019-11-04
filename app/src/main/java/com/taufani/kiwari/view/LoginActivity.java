package com.taufani.kiwari.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.taufani.kiwari.R;
import com.taufani.kiwari.model.UserModel;
import com.taufani.kiwari.utilities.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    EditText mTextEmail;
    EditText mTextPassword;

    private FirebaseFirestore mFirestore;
    private List<UserModel> mUsers;
    private SessionManager mSession;

    public static final String TAG = "LoginKiwari";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSession = new SessionManager(this);
        mFirestore = FirebaseFirestore.getInstance();
        mUsers = new ArrayList<>();

        if (mSession.isLoggedIn()) navigate();

        mTextEmail = (EditText) findViewById(R.id.email_address);
        mTextPassword = (EditText) findViewById(R.id.password);

        Button buttonSignIn = (Button) findViewById(R.id.btn_sign_in);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });
    }

    private void validation() {
        mTextEmail.setError(null);
        mTextPassword.setError(null);

        String email = mTextEmail.getText().toString();
        String password = mTextPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            mTextPassword.setError(getString(R.string.error_field_required));
            focusView = mTextPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mTextEmail.setError(getString(R.string.error_field_required));
            focusView = mTextEmail;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            submit();
        }
    }

    private void submit() {
        mFirestore.collection("users")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot documentSnapshot) {
                        if (documentSnapshot.isEmpty()) {
                            return;
                        } else {
                            List<UserModel> users = documentSnapshot.toObjects(UserModel.class);
                            mUsers.addAll(users);

                            for (UserModel user : mUsers) {
                                if (mTextEmail.getText().toString().equals(user.getEmail())) {
                                    if (mTextPassword.getText().toString().equals(user.getPassword())) {
                                        String json = new Gson().toJson(user);
                                        mSession.putSharedPref("User", json);
                                        mSession.setLogin(true);

                                        navigate();
                                        return;
                                    } else {
                                        mTextPassword.setError(getString(R.string.error_incorrect_password));
                                        mTextPassword.requestFocus();
                                        return;
                                    }
                                }
                            }

                            mTextEmail.setError(getString(R.string.error_incorrect_email));
                            mTextEmail.requestFocus();
                        }
                    }
                });
    }

    private void navigate() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
