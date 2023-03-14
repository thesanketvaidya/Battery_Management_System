package com.example.bms12;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class CommandActivity extends AppCompatActivity {
    private ImageView img_back;
    private FrameLayout fragmentContainer;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.send_command_fragment_container, new More_Options_Fragment(),"more_options");
        transaction.commit();



        img_back = findViewById(R.id.back_arrow);


        fragmentContainer = findViewById(R.id.send_command_fragment_container);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();

            }
        });


    }
}

