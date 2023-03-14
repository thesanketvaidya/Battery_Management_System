package com.example.bms12;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private EditText email, pass;
    private Button login, register;
    private TextView forgot_password;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        forgot_password = findViewById(R.id.forgot_password);
        register = findViewById(R.id.reg_btn);
        email = findViewById(R.id.emailId);
        pass = findViewById(R.id.password);
        login = findViewById(R.id.login_btn);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!=null)
        {
            //directly redirect to home page
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }
        dialog = new ProgressDialog(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e_mail = email.getText().toString().trim();
                String password = pass.getText().toString().trim();
                if (TextUtils.isEmpty(e_mail) || TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "All fields required!", Toast.LENGTH_LONG).show();
                } else {
                    dialog.setMessage("Logging In...");
                    dialog.show();
                    mAuth.signInWithEmailAndPassword(e_mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Logged in Successfully!", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                dialog.dismiss();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error Logging in!(check email and password)", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        }
                    });
                }

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Registration.class));
            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgotPassword.class));
            }
        });

    }
}

