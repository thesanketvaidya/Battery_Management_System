package com.example.bms12;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, popupClass.BottomSheetListener {
    static public int MapType = 0;

    private DrawerLayout drawerLayout;
    private DatabaseReference myRef,bmsref;
    private FirebaseAuth mAuth;
    private TextView fullName;
    private TextView email;
    public DataSnapshot child;
    static  public String focusOnfromSearchFragment="NULL";
    static List<BMSData> BMS;
    List<geocor> geocorList;
    BMSData temp1;
    BMSData temp;
    int count=0;
    FirebaseDatabase Instance;
    FirebaseFirestore Instance1;
    SharedPreferences sharedPreferences2;
    SharedPreferences.Editor edit2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences2=getApplicationContext().getSharedPreferences("yosh", Context.MODE_PRIVATE);
        edit2=sharedPreferences2.edit();
        Instance=FirebaseDatabase.getInstance();
        try{ Instance1=FirebaseFirestore.getInstance();}
        catch(Exception e){

        }
//        Map<String, Object> user = new HashMap<>();
//        user.put("first", "Ada");
//        user.put("last", "Lovelace");
//        user.put("born", 1815);

// Add a new document with a generated ID
//        Instance1.collection("users")
//                .add(user)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d("ololl", "DocumentSnapshot added with ID: " + documentReference.getId());
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w("ulull", "Error adding document", e);
//                    }
//                });


        BMS=new ArrayList<>();
        geocorList=new ArrayList<>();
        bmsref=Instance.getReference("BMS Boards");
//       try{ Instance1.collection("yosh").add("hoho");}
//       catch(Exception e){
//           Log.d("ola", e.getMessage());
//       }
//        Instance1.collection("BMS_1001")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d("Okay", document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.w("not okay", "Error getting documents.", task.getException());
//                        }
//                    }
//                });
        bmsref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child:dataSnapshot.getChildren())
                {
                    Log.d("Are hyo", child.getKey());
                     edit2.putString(child.getKey(),child.getKey());
                     edit2.apply();
                    temp=new BMSData();
                    temp.setId(sharedPreferences2.getString(dataSnapshot.getKey(),null));
                    BMS.add(temp);

                    Log.d("Are hyo", temp.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Map<String,?> keys=sharedPreferences2.getAll();
        for(Map.Entry<String,?> entry: keys.entrySet())
        {
            //Log.d("RAGNAROK77", entry.getValue().toString());
            temp=new BMSData();
            temp.setId(entry.getValue().toString());

            BMS.add(temp);
        }
        Log.d("bhikar dhnde", Integer.toString(BMS.size()));

        for (BMSData child:BMS)
        {
            Log.d("shimatta", child.toString());
            geocorList.add(new geocor(child.getId(),child.getLongitude(),child.getLattitude()));
        }
        Toolbar myChildToolbar = findViewById(R.id.toolbar);
        myChildToolbar.setTitle("");
        setSupportActionBar(myChildToolbar);

        drawerLayout = findViewById(R.id.drawer_layout);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, myChildToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_home);
       // Toast.makeText(getApplicationContext(), "shiararar", Toast.LENGTH_SHORT).show();

        FirebaseDatabase Instance=FirebaseDatabase.getInstance();
        myRef = Instance.getReference();
        bmsref=Instance.getReference("BMS Boards");

       // Log.d("00000hurrr", "onMapReady: "+LoginActivity.BMS.get(0).toString());
        Toast.makeText(getApplicationContext(),Integer.toString( BMS.size()), Toast.LENGTH_SHORT).show();
        mAuth = FirebaseAuth.getInstance();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fullName = findViewById(R.id.fullName);
                email = findViewById(R.id.email);
                email.setText(dataSnapshot.child("Admin Data").child(mAuth.getUid()).child("email").getValue().toString().trim());
                fullName.setText(dataSnapshot.child("Admin Data").child(mAuth.getUid()).child("name").getValue().toString().trim());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                BMSData temprory;
//                List<BMSData> temp=new ArrayList<>();
////                try {
////                    if (HomeFragment.Markers!=null&&HomeFragment.Markers.size() != 0 ) {
////                        for (MyMarker child : HomeFragment.Markers) {
////                            for (geocor g : geocorList) {
////                                if (child.getId().equals(g.getId())) {
////
////                                    g.setLattitude(child.getPreviousPosition().latitude);
////                                    g.setLongitude(child.getPreviousPosition().longitude);
////                                }
////                            }
////                        }
////                    }
////                }
////                catch(Exception e)
////                {
////                    Log.d("AYLA", e.getMessage());
////                }
//                int i=1;
//                while(true)
//                {
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    if(HomeFragment.Markers.size()!=0) {
//                        for (MyMarker child : HomeFragment.Markers) {
//                            geocor temp1=null;
//                            Log.d("Jamla", child.getId()+" "+child.currPosition.toString()+" "+child.getPreviousPosition().toString());
//                            for(geocor g:geocorList) {
//                                if (child.getId().equals(g.getId())) {
//                                    temp1=g;
//                                    //Log.d("Jamla", g.toString());
//                                    if(g.getLattitude()==0.0)
//                                    {
//                                        g.setLattitude(child.getPreviousPosition().latitude);
//                                        g.setLongitude(child.getPreviousPosition().longitude);
//                                    }
//                                }
//                            }
//                            //Log.d("HAO" ,temp1.toString());
//                            if(child.getPreviousPosition().equals(new LatLng(temp1.getLattitude(),temp1.getLongitude())))
//                            {
//                                // Log.d("jamla",child.getId()+ " Inactive");
//                                bmsref.child(child.getId()).child("status").setValue("Inactive");
//                            }
//                            else
//                            {
//                                temp1.setLattitude(child.getPreviousPosition().latitude);
//                                temp1.setLongitude(child.getPreviousPosition().longitude);
//                                bmsref.child(child.getId()).child("status").setValue("Active");
//                            }
//                        }
//                    }
////                    Log.d("whileloop",Integer.toString(i) );
////                    i++;
//                }
//            }
//        });

        @SuppressLint("StaticFieldLeak") AsyncTask statusThread=new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                BMSData temprory = null;
                List<BMSData> temp=new ArrayList<>();
//                try {
//                    if (HomeFragment.Markers!=null&&HomeFragment.Markers.size() != 0 ) {
//                        for (MyMarker child : HomeFragment.Markers) {
//                            for (geocor g : geocorList) {
//                                if (child.getId().equals(g.getId())) {
//
//                                    g.setLattitude(child.getPreviousPosition().latitude);
//                                    g.setLongitude(child.getPreviousPosition().longitude);
//                                }
//                            }
//                        }
//                    }
//                }
//                catch(Exception e)
//                {
//                    Log.d("AYLA", e.getMessage());
//                }
                int i=1;
                while(true)
                {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(HomeFragment.Markers.size()!=0) {
                        for (MyMarker child : HomeFragment.Markers) {
                            geocor temp1=null;
                            for(BMSData bmsData:BMS)
                            {
                                if(bmsData.getId().equals(child.getId()))
                                {
                                    temprory=bmsData;
                                }
                            }
                           // Log.d("Jamla", child.getId()+" "+child.currPosition.toString()+" "+child.getPreviousPosition().toString());
                            for(geocor g:geocorList) {

                                if (child.getId().equals(g.getId())) {
                                    temp1=g;
                                    //Log.d("Jamla", g.toString());
                                    if(g.getLattitude()==0.0)
                                    {
                                        g.setLattitude(child.getPreviousPosition().latitude);
                                        g.setLongitude(child.getPreviousPosition().longitude);
                                    }
                                }
                            }
                            //Log.d("HAO" ,temp1.toString());
                            if(child.getPreviousPosition().equals(new LatLng(temp1.getLattitude(),temp1.getLongitude())))
                            {
                                // Log.d("jamla",child.getId()+ " Inactive");
                                bmsref.child(child.getId()).child("status").setValue("Inactive");
                            }
                            else
                            {
                                temp1.setLattitude(child.getPreviousPosition().latitude);
                                temp1.setLongitude(child.getPreviousPosition().longitude);
                                bmsref.child(child.getId()).child("status").setValue("Active");
                            }
                            String output=null;
                            if(temprory.getBattery_Volt()>48)
                                output=output+" Overvoltage";
                            if(temprory.getBattery_Volt()<48)
                                output=output+" Undervoltage";
                            if(temprory.getBattery_Curr()>35)
                                output=output+" Overcurrent";
                            if(temprory.getBattery_Curr()>20)
                                output=output+" Overvoltage";



                        }
                    }
//                    Log.d("whileloop",Integer.toString(i) );
//                    i++;
                }

                }
        };


        @SuppressLint("StaticFieldLeak") AsyncTask historyThread=new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                SimpleDateFormat frmat=new SimpleDateFormat("dd-MM-yyyy");
                while(true) {

                    try {
                        Date d=new Date();
                        final String date=frmat.format(d.getTime());
                        Thread.sleep(2000);
                        //Log.d("ere",frmat.format(d.getTime()));

                        for( final MyMarker child:HomeFragment.Markers)
                        {
                          if(child.getId()!=null) {
                           //   Log.d(child.getId(), child.getCurrPosition().toString());
                              DocumentReference temp90 = Instance1.collection(child.getId()).document(date);
                              temp90.update("Geocordinates", FieldValue.arrayUnion(new GeoPoint(child.getPreviousPosition().latitude,
                                      child.getPreviousPosition().longitude)))

                                      .addOnFailureListener(new OnFailureListener() {
                                          @Override
                                          public void onFailure(@NonNull Exception e) {

                                              Map<String, Object> user = new HashMap<>();
                                              user.put("Id", child.getId());
                                              //   Geopoints.add(user);
                                              Instance1.collection(child.getId()).document(date).set(user);


                                          }
                                      });

                          }
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
//    a.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        b.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        historyThread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        statusThread.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);



    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                break;
            case R.id.nav_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SearchFragment()).commit();
                break;
            case R.id.nav_mis:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MISFragment()).commit();

                break;
            case R.id.nav_geo:
                MapsActivity.forWho="All";
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));

                break;
            case R.id.nav_logout:
//                Logout Code
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
            case R.id.nav_reg_map:
                MapType = 0;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                break;
            case R.id.nav_sat_map:
                MapType = 1;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();

                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    /*@Override
    public void onButtonClicked(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
*/
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onButtonClicked(String text) {
        // Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


}


