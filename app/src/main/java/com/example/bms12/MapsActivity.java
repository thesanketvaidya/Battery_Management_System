package com.example.bms12;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker m;
    private SeekBar rad;
    private LatLng updatedCo_ordinate;
    private TextView displayRadius;
    Button save,active_fences;
    Circle c;
    TextView t;
    ImageView backArrow;
    FirebaseDatabase Instance;
    DatabaseReference geoFence,myref;
    List<Marker>  geoFenceMarkers;
    Marker temp;
    double geolattitude,geolongitude,georadius;
    String id;
    public static String forWho;
    //ArrayList<geocor> listItems;
    //geocor perm,temp;
    int flag;

    private DatabaseReference ref,forfence,temp1;
    private GeoQuery geoQuery;
    private GeoFire geoFire;
    private Toolbar toolbar;

    double latitude,longitude,radius;
    GeoLocation center;

    double radius1;
GeoLocation centerOfFence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getSupportActionBar();
        Log.d("FORWHOOOOOOOOOO", forWho);
        rad = findViewById(R.id.seekBar2);
        //  rad.setMax(350);
        //rad.setMin(20);
        displayRadius = findViewById(R.id.radiusTextview);
        initializer();



    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(19.8655093,75.2082946), 6));
        active_fences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Marker[] tempmarker = new Marker[1];
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        for(Marker child: geoFenceMarkers)
                        {
                            if(child.getPosition().equals(marker.getPosition()))
                            {
                                tempmarker[0] =child;
                            }
                        }
                        AlertDialog.Builder builder=new AlertDialog.Builder(MapsActivity.this);
                        builder.setMessage("Click below to get list of BMS boards status with respect to the GEOFENCE selected ").
                        setPositiveButton("GET LIST", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                              Intent i=new Intent(MapsActivity.this,Choose_vehicles_Activity.class);
                              i.putExtra("selected",tempmarker[0].getTitle());
                              i.putExtra("inside",false);
                              i.putExtra("outside",false);
                              startActivity(i);
                            }
                        });
                        AlertDialog alert=builder.create();
                        alert.setTitle(tempmarker[0].getTitle());
                        alert.show();

                        return false;
                    }
                });
                myref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot child:dataSnapshot.getChildren())
                        {           id=child.getKey();
                            geolattitude=(Double)child.child("lattitude").getValue();
                            geolongitude=(Double)child.child("longitude").getValue();
                            georadius=(Long)child.child("radius").getValue();

                            Log.d("fookingfences", id+" "+geolattitude+" "+geolongitude+" "+georadius);
                            LatLng latLng=new LatLng(geolattitude,geolongitude);
                            temp=mMap.addMarker(new MarkerOptions().position(latLng).title(id));
                            try {
                                Circle c = mMap.addCircle(new CircleOptions().center(latLng).strokeWidth(2).fillColor(
                                        Color.parseColor("#258AFC00")).radius(1000*georadius));
                            }
                            catch(Exception e)
                            {
                                Log.d("Exception", e.getMessage());
                            }
                            geoFenceMarkers.add(temp);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });
        setupListeners();
        //  fetchfence();
        //geoFire.setLocation("Nigga!",new GeoLocation(37.7832, -122.4056));
      /*  GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(37.7832, -122.4056), 1.0);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                Toast.makeText(MapsActivity.this, "Nigga inside!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onKeyExited(String key) {

                Toast.makeText(MapsActivity.this, "Nigga Gone!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

                geoFire.getLocation("Nigga!", new LocationCallback() {
                    @Override
                    public void onLocationResult(String key, GeoLocation location) {

                        Toast.makeText(MapsActivity.this, "Nigga Movin like anything!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            @Override
            public void onGeoQueryReady() {
            }
            @Override
            public void onGeoQueryError(DatabaseError error) {
            }
        });

        //geoFire.setLocation("Nigga!", new GeoLocation(37.7853889, -122.4056973));


*/
    }


    private void initializer()
    {

        toolbar=findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        save=findViewById(R.id.button);
        active_fences=findViewById(R.id.active_fences);


        backArrow=findViewById(R.id.back_arrow);
        rad=findViewById(R.id.seekBar2);

        //listItems=new ArrayList<geocor>();
        displayRadius=findViewById(R.id.radiusTextview);

        Instance=FirebaseDatabase.getInstance();
        myref=Instance.getReference("Geofence");
        geoFence=Instance.getReference("Geofence/"+forWho);
        geoFenceMarkers=new ArrayList<>();
//        ref = FirebaseDatabase.getInstance().getReference("active");
//        forfence = FirebaseDatabase.getInstance().getReference("geofence_details");
//
//
//        geoFire = new GeoFire(forfence.getParent().child("Geofence"));




    }
//
//    void fetchfence()
//    {
//        forfence.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                latitude= Double.parseDouble(dataSnapshot.child("latitude").getValue().toString());
//                longitude= Double.parseDouble(dataSnapshot.child("longitude").getValue().toString());
//                radius= Double.parseDouble(dataSnapshot.child("radius").getValue().toString());
//
//                geoQuery=geoFire.queryAtLocation(new GeoLocation(latitude,longitude),radius);
//
//                Log.d("luls", latitude.toString()+" "+longitude.toString());
//
//                ref.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//                        for (DataSnapshot i : dataSnapshot.getChildren()) {
//                            geocor t = i.getValue(geocor.class);
//                            t.setId(i.getKey());
//                            Log.d("vbbbbbbbbbbbbbbbbb", t.toString());
//
//                            for (geocor g : listItems) {
//                                if (g.getId().equals(t.getId())) {
//                                    flag = 1;
//                                    g.setLattitude(t.getLattitude());
//                                    g.setLongitude(t.getLongitude());
//                                    break;
//                                }
//                            }
//                            if (flag != 1) {
//
//                                perm = new geocor(t);
//                                perm.setEntered("false");
//
//                                perm.setExited("false");
//                                listItems.add(perm);
//
//                            }
//
//
//                            flag = 0;
//                        }
//
//                        for (final geocor g : listItems) {
//                            geoFire.setLocation(g.getId(), new GeoLocation(g.getLattitude(), g.getLongitude()));
//
//
//                            Log.d("WADDUHEEEEEEEEEEEEECCK", g.toString());
//                        }
//
//                        geoQuery = geoFire.queryAtLocation(new GeoLocation(latitude, longitude), radius);
//                        //geoQuery=geoFire.queryAtLocation(center,radius);
//
//
//
//                        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
//                            @Override
//                            public void onKeyEntered(String key, GeoLocation location) {
//                                for (geocor g : listItems) {
//                                    if (g.getId().equals(key)) {
//                                        temp = g;
//                                        break;
//                                    }
//                                }
//
//                                if (temp != null && temp.getEntered().equals("false")) {
//
//                                    Toast.makeText(MapsActivity.this, key + " ENTERD", Toast.LENGTH_SHORT).show();
//                                    temp.setEntered("true");
//                                    temp.setExited("false");
//                                }
//                            }
//
//                            @Override
//                            public void onKeyExited(String key) {
//                                for (geocor g : listItems) {
//                                    if (g.getId().equals(key)) {
//                                        temp = g;
//                                        break;
//                                    }
//                                }
//
//                                if (temp != null && temp.getExited().equals("false")) {
//
//                                    Toast.makeText(MapsActivity.this, key + " EXITED", Toast.LENGTH_SHORT).show();
//                                    temp.setExited("true");
//                                    temp.setEntered("false");
//                                }
//                            }
//
//                            @Override
//                            public void onKeyMoved(String key, GeoLocation location) {
//
//                            }
//
//                            @Override
//                            public void onGeoQueryReady() {
//
//                            }
//
//                            @Override
//                            public void onGeoQueryError(DatabaseError error) {
//
//                            }
//                        });
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError error) {
//                        // Failed to read value
//                        Log.w("YO-Nigguh", "Failed to read value.", error.toException());
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }

    void setupListeners()
    {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("NANANA", updatedCo_ordinate.toString());

                if( updatedCo_ordinate!=null) {
                    radius = rad.getProgress();
                    centerOfFence=new GeoLocation(updatedCo_ordinate.latitude,updatedCo_ordinate.longitude);

                    geoFence.child("lattitude").setValue(centerOfFence.latitude);
                    geoFence.child("longitude").setValue(centerOfFence.longitude);
                    geoFence.child("radius").setValue(radius);

//                    DatabaseReference tempRef=Instance.getReference("Geofence Monitoring/All");
//                    geoFire=new GeoFire(tempRef);

//                    DatabaseReference myRef=Instance.getReference("BMS Boards");
//                    myRef.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            for(DataSnapshot t:dataSnapshot.getChildren())
//                            {
//                                BMSData temp=t.getValue(BMSData.class);
//                                temp.setId(t.getKey());
//                                Log.d("LOOOOOOOK", temp.toString());
//
//                                geoFire.setLocation(temp.getId(),new GeoLocation(temp.getLattitude(),temp.getLongitude()));
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    })      ;
//
//                   // geoFire.setLocation("BMS_1001",new GeoLocation(49.86,15.34));
//
//
//                    geoQuery=geoFire.queryAtLocation(centerOfFence,radius);
//                    geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
//                        @Override
//                        public void onKeyEntered(String key, GeoLocation location) {
//                            Toast.makeText(MapsActivity.this, key+" ENTERED!!", Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onKeyExited(String key) {
//                            Toast.makeText(MapsActivity.this, key+" EXITED!!", Toast.LENGTH_SHORT).show();
//
//                        }
//
//                        @Override
//                        public void onKeyMoved(String key, GeoLocation location) {
//
//                        }
//
//                        @Override
//                        public void onGeoQueryReady() {
//
//                        }
//
//                        @Override
//                        public void onGeoQueryError(DatabaseError error) {
//                            Toast.makeText(MapsActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
//                        }
//                    });

                    Toast.makeText(MapsActivity.this, "Geofence Setup Correctly!", Toast.LENGTH_SHORT).show();

                    Log.d("WATCH ME!", radius + " " + centerOfFence.toString());
                }
                else
                {
                    Toast.makeText(MapsActivity.this, "Tap on Map to plot CENTER and select RADIUS!", Toast.LENGTH_LONG).show();
                }

//                    Intent i=new Intent(getApplicationContext(),Choose_vehicles_Activity.class);
//                    startActivity(i);

            }
        });


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });








        rad.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int a=rad.getProgress();
                try {
                    displayRadius.setText(Integer.toString(a)+" KM");
                    if(m!=null)
                        m.remove();
                    if(c!=null)
                        c.remove();

                    m=mMap.addMarker(new MarkerOptions().position(updatedCo_ordinate));

                    c= mMap.addCircle(new CircleOptions().center(new LatLng(m.getPosition().latitude,m.getPosition().longitude)).strokeWidth(2).fillColor(
                            Color.parseColor("#258AFC00")).radius(1000*rad.getProgress()));

                    // Toast.makeText(MapsActivity.this, Integer.toString(a), Toast.LENGTH_SHORT).show();
                }
                catch(Exception e)
                {

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });







        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {

                if(m!=null)
                    m.remove();
                if(c!=null)
                    c.remove();
                updatedCo_ordinate=point;
                Log.d("WHOA WHOA", updatedCo_ordinate.toString());
                m=mMap.addMarker(new MarkerOptions().position(point));
                m.setTitle(m.getPosition().toString());
                //  t.setText("Lattitude Of the Center:-  "+m.getPosition().latitude+"\nLongitude Of The Center:-  "+m.getPosition().longitude);

                c= mMap.addCircle(new CircleOptions().center(new LatLng(m.getPosition().latitude,m.getPosition().longitude)).strokeWidth(2).fillColor(
                        Color.parseColor("#258AFC00")).radius(1000*rad.getProgress()));



                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 6));


            }
        });



    }

//void setupGeofence()
//    {
//        if(m!=null) {
//            center= new GeoLocation(m.getPosition().latitude, m.getPosition().longitude);
//            radius1 = rad.getProgress();
//            final DatabaseReference myref = FirebaseDatabase.getInstance().getReference("Geofence");
//            geoFire = new GeoFire(myref);
//            //geoFire=new GeoFire(FirebaseDatabase.getInstance().getReference("ref"));
//
//            ref.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//
//                    for (DataSnapshot i : dataSnapshot.getChildren()) {
//                        geocor t = i.getValue(geocor.class);
//                        t.setId(i.getKey());
//                        Log.d("vbbbbbbbbbbbbbbbbb", t.toString());
//
//                        for (geocor g : listItems) {
//                            if (g.getId().equals(t.getId())) {
//                                flag = 1;
//                                g.setLattitude(t.getLattitude());
//                                g.setLongitude(t.getLongitude());
//                                break;
//                            }
//                        }
//                        if (flag != 1) {
//
//                            perm = new geocor(t);
//                            perm.setEntered("false");
//
//                            perm.setExited("false");
//                            listItems.add(perm);
//
//                        }
//
//
//                        flag = 0;
//                    }
//
//                    for (final geocor g : listItems) {
//                        geoFire.setLocation(g.getId(), new GeoLocation(g.getLattitude(), g.getLongitude()));
//
//
//                        Log.d("WADDUHEEEEEEEEEEEEECCK", g.toString());
//                    }
//
//                    //geoQuery = geoFire.queryAtLocation(new GeoLocation(19.75, 75.28), 5.0);
//                    geoQuery=geoFire.queryAtLocation(center,radius);
////                    myref.getParent().child("geofence_details").child("latitude").setValue(19.75);
////                    myref.getParent().child("geofence_details").child("longitude").setValue(75.28);
////                    myref.getParent().child("geofence_details").child("radius").setValue(5.0);
//                      myref.getParent().child("geofence_details").child("latitude").setValue(center.latitude);
//                      myref.getParent().child("geofence_details").child("longitude").setValue(center.longitude);
//                      myref.getParent().child("geofence_details").child("radius").setValue(radius1);
//
//
//                    geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
//                        @Override
//                        public void onKeyEntered(String key, GeoLocation location) {
//                            for (geocor g : listItems) {
//                                if (g.getId().equals(key)) {
//                                    temp = g;
//                                    break;
//                                }
//                            }
//
//                            if (temp != null && temp.getEntered().equals("false")) {
//
//                                Toast.makeText(MapsActivity.this, key + " ENTERD", Toast.LENGTH_SHORT).show();
//                                temp.setEntered("true");
//                                temp.setExited("false");
//                            }
//                        }
//
//                        @Override
//                        public void onKeyExited(String key) {
//                            for (geocor g : listItems) {
//                                if (g.getId().equals(key)) {
//                                    temp = g;
//                                    break;
//                                }
//                            }
//
//                            if (temp != null && temp.getExited().equals("false")) {
//
//                                Toast.makeText(MapsActivity.this, key + " EXITED", Toast.LENGTH_SHORT).show();
//                                temp.setExited("true");
//                                temp.setEntered("false");
//                            }
//                        }
//
//                        @Override
//                        public void onKeyMoved(String key, GeoLocation location) {
//
//                        }
//
//                        @Override
//                        public void onGeoQueryReady() {
//
//                        }
//
//                        @Override
//                        public void onGeoQueryError(DatabaseError error) {
//
//                        }
//                    });
//
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError error) {
//                    // Failed to read value
//                    Log.w("YO-Nigguh", "Failed to read value.", error.toException());
//                }
//            });
//        }
//        else
//        {
//            Toast.makeText(this, "Make Sure You Plot the CENTER First!", Toast.LENGTH_SHORT).show();
//        }
//    }
}
