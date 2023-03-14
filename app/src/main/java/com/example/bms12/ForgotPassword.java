package com.example.bms12;

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
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private Button reset;
    private EditText email;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        email = findViewById(R.id.emailId);
        reset = findViewById(R.id.reset);

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

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String e_mail = email.getText().toString().trim();
                if (TextUtils.isEmpty(e_mail)) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Enter Email ID!", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.sendPasswordResetEmail(e_mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast toast = Toast.makeText(getApplicationContext(), "Successful! Please Check Email", Toast.LENGTH_LONG);
                                toast.show();
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), "Error! Please check Email", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }
                    });
                }
            }
        });
    }
}
