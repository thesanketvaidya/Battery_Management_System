package com.example.bms12;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Registration extends AppCompatActivity {
    private EditText emailId, password, password_confirm, verifyId, mobileNo, fullName;
    private Button reg_btn;
    private ProgressDialog dialog;

    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        emailId = findViewById(R.id.emailId);
        password = findViewById(R.id.password);
        password_confirm = findViewById(R.id.password_confirm);
        verifyId = findViewById(R.id.verifyId);
        mobileNo = findViewById(R.id.mobileNo);
        fullName = findViewById(R.id.fullName);
        reg_btn = findViewById(R.id.reg_btn);
        dialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();


        Toolbar myChildToolbar = findViewById(R.id.toolbar);
        myChildToolbar.setTitle("");
        setSupportActionBar(myChildToolbar);
        ImageView imageView = findViewById(R.id.back_arrow);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String e_mail = emailId.getText().toString().trim();
                final String name = fullName.getText().toString().trim();
                final String passwordOne = password.getText().toString().trim();
                final String passwordTwo = password_confirm.getText().toString().trim();
                final String id = verifyId.getText().toString().trim();
                final String mobile = mobileNo.getText().toString().trim();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(id) || TextUtils.isEmpty(mobile) || TextUtils.isEmpty(e_mail) || TextUtils.isEmpty(passwordOne) || TextUtils.isEmpty(passwordTwo)) {
                    Toast toast = Toast.makeText(getApplicationContext(), "All Fields Required!", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    if (passwordOne.equals(passwordTwo)) {
                        if (passwordOne.length() >= 6) {
                            dialog.setMessage("Adding User...");
                            dialog.show();
                            myRef = FirebaseDatabase.getInstance().getReference();
                            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child("Valid Id").child(id).exists() && dataSnapshot.child("Valid Id").child(id).getValue().toString().equals(e_mail))  {
                                        mAuth.createUserWithEmailAndPassword(e_mail, passwordOne).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), "Successfully Registered!", Toast.LENGTH_LONG).show();
                                                    mAuth.signInWithEmailAndPassword(e_mail, passwordOne);
                                                    myRef = FirebaseDatabase.getInstance().getReference();
                                                    AdminData dat = new AdminData(e_mail, name, mobile, id);
                                                    myRef.child("Admin Data").child(mAuth.getUid()).setValue(dat);
                                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                                    dialog.dismiss();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Problem Adding User!", Toast.LENGTH_LONG).show();
                                                    dialog.dismiss();
                                                }

                                            }
                                        });
                                    } else {
                                        verifyId.setText("");
                                        verifyId.setError("Enter a valid ID");
                                        dialog.dismiss();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }

                            });
                        } else {
                            password.setText("");
                            password_confirm.setText("");
                            password.setError("Password needs to be greater than 6 characters");
                            password_confirm.setError("Password needs to be greater than 6 characters");
                        }

                    } else {
                        password.setText("");
                        password_confirm.setText("");
                        password.setError("Passwords need to match!");
                        password_confirm.setError("Passwords need to match!");

                    }

                }

            }
        });
    }
}
