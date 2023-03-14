package com.example.bms12;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Choose_vehicles_Activity extends AppCompatActivity {
    LinearLayout l;
    ArrayList<geocor> listItems;

    CheckBox selectall;
    ImageView backArrow;
    FloatingActionButton Save;
    geocor perm;
    LayoutInflater inflater;
    private DatabaseReference ref;
    private GeoQuery geoQuery;
    private GeoFire geoFire;
    int flag;
    View item;
    TextView tempText;
    Toolbar toolbar;
    String selected;
    Switch inside,outside;
    boolean in,out;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_vehicles_);
        listItems = new ArrayList<geocor>();
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        l=findViewById(R.id.innerLayout);

        selected=getIntent().getStringExtra("selected");
        in=getIntent().getBooleanExtra("inside",false);
        out=getIntent().getBooleanExtra("outside",false);

        toolbar = findViewById(R.id.mytoolbar);
        backArrow = findViewById(R.id.back_arrow);

        inside=findViewById(R.id.switch1);
        outside=findViewById(R.id.switch2);
        inside.setChecked(in);
        outside.setChecked(out);
        setSupportActionBar(toolbar);

        if(selected.equals("All"))
        {
            for(BMSData temp:LoginActivity.BMS) {
                Log.d("Eikna bho", temp.toStringBoolean());
                if (temp.getId() != null) {
                    if (inside.isChecked()) {
                        if (temp.isAll_entered() && !temp.isAll_exited()) {
                            item = inflater.inflate(R.layout.vehicles_for_geofence, null);
                            tempText = item.findViewById(R.id.bmsid);
                            tempText.setText(temp.getId());
                            l.addView(item);
                        }
                    } else if (outside.isChecked()) {
                        if (temp.isAll_exited()) {
                            item = inflater.inflate(R.layout.vehicles_for_geofence, null);
                            tempText = item.findViewById(R.id.bmsid);
                            tempText.setText(temp.getId());
                            l.addView(item);
                        }
                    }
                }
            }
            }
        else
        {
            for(BMSData temp:LoginActivity.BMS) {

                if(selected.equals(temp.getId())) {
                    if (inside.isChecked()) {
                        if (temp.isSelf_entered() && !temp.isSelf_exited()) {
                            item = inflater.inflate(R.layout.vehicles_for_geofence, null);
                            tempText = item.findViewById(R.id.bmsid);
                            tempText.setText(temp.getId());
                            l.addView(item);
                        }
                    } else if (outside.isChecked()) {
                        if (temp.isSelf_exited()) {
                            item = inflater.inflate(R.layout.vehicles_for_geofence, null);
                            tempText = item.findViewById(R.id.bmsid);
                            tempText.setText(temp.getId());
                            l.addView(item);
                        }
                    }
                }
            }
        }

        inside.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("switched", String.valueOf(isChecked));
                Intent i = new Intent(Choose_vehicles_Activity.this,Choose_vehicles_Activity.class);
                i.putExtra("selected",selected);
                i.putExtra("inside",isChecked);
                i.putExtra("outside",!isChecked);
                finish();
                startActivity(i);

            }
        });
        outside.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("switched", String.valueOf(isChecked));
                Intent i = new Intent(Choose_vehicles_Activity.this,Choose_vehicles_Activity.class);
                i.putExtra("selected",selected);
                i.putExtra("outside",isChecked);
                i.putExtra("inside",!isChecked);
                finish();
                startActivity(i);

            }
        });



        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onResume() {

        super.onResume();
    }
}