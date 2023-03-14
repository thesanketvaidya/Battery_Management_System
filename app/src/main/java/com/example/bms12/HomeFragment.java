package com.example.bms12;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.TimeUnit;


public class HomeFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap mMap;
    Marker m;
    DatabaseReference bmsref,geoFenceref,tempRef,availableFences;
    static List<BMSData> BMS;
   static Vector<MyMarker> Markers;
    BMSData temp;
    boolean flag=true,geoQueryFlag=false,notifyFlag=false;
    List<String> Available;
    double radius,latitude,longitude;
    GeoFire  geoFire;
    GeoQuery geoQuery;
    GeoLocation centerOfFence;
    GeoQueryEventListener geoQueryEventListener;
    List<GeofenceObjects> geofenceObjects;
    List<GeoQuery> geoQueryList;
    List<BMSData> vehicles;
    MyMarker searchMarker;
    NotificationSender notificationSender;
    boolean firstTime=true;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Markers=new Vector();
        BMS=new ArrayList<>();
        Available=new ArrayList<>();
        geofenceObjects =new ArrayList<>();
        geoQueryList=new ArrayList<>();
        vehicles=new ArrayList<>();
       notificationSender=new NotificationSender(getActivity());
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("LOKK HERE MFS!!", Double.toString(distanceTravelled(new LatLng(19.8655013,75.3490081),
                new LatLng(19.859361,75.345876))));

//       geofenceObjects.add(new GeofenceObjects("All"));
////        //OKIE DOKIE!!
//        geofenceObjects.add(new GeofenceObjects("BMS_1001"));
        final SharedPreferences sharedPreferences=getActivity().getApplicationContext().getSharedPreferences("MyPref",0);
        final SharedPreferences.Editor edit=sharedPreferences.edit();


        final FirebaseDatabase Instance=FirebaseDatabase.getInstance();
        DatabaseReference lulRef=Instance.getReference("Geofence");
        lulRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("CHYALA", "Madhi tr alo bho " + dataSnapshot.getValue());
                for (DataSnapshot child: dataSnapshot.getChildren())
                {
                    String temp=child.getKey();
                   edit.putString(child.getKey(),child.getKey());
                   edit.apply();

                    Log.d("CHYALA", child.getKey().toString());
                   //geofenceObjects.add(new GeofenceObjects(sharedPreferences.getString(dataSnapshot.getKey(),null)));
                    Log.d("CHYALA ",Integer.toString(geofenceObjects.size()));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Log.d("Holy Smokes",Integer.toString(geofenceObjects.size()));
        Map<String,?> keys=sharedPreferences.getAll();
        for(Map.Entry<String,?> entry: keys.entrySet())
        {
            Log.d("RAGNAROK", entry.getValue().toString());
            geofenceObjects.add(new GeofenceObjects(entry.getValue().toString()));
        }

        bmsref= Instance.getReference("BMS Boards");
       // Log.d("itha pahy re bho", geofenceObjects.get(0).toString());
        for(final GeofenceObjects tempChild: geofenceObjects)
        {

                Log.d("noobde", tempChild.toString());

            Log.d("kai badhir e",Integer.toString(geofenceObjects.size()));

            final double[] latitude = new double[1];
            final double[] longitude = new double[1];
            final double[] radius = new double[1];
            final GeoLocation[] centerOfFence = new GeoLocation[1];
            DatabaseReference tempRef=Instance.getReference("Geofence Monitoring/" + tempChild.getId());
            final GeoFire geoFire = new GeoFire(tempRef);
            DatabaseReference geoFenceref=Instance.getReference("Geofence/"+tempChild.getId());

//            tempChild.setGeoFire(geoFire);
//            tempChild.setGeoQuery(geoQuery);


            geoFenceref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()!=null) {
                        //Log.d("hoehoehoe", vehicles.get(0).toStringBoolean());
                        int index = 0;
                        latitude[0] = (double) dataSnapshot.child("lattitude").getValue();
                        longitude[0] = (double) dataSnapshot.child("longitude").getValue();
                        radius[0] = (long) dataSnapshot.child("radius").getValue();
                        centerOfFence[0] = new GeoLocation(latitude[0], longitude[0]);
//Bro You are going good
                        if (tempChild.isGeoQueryFlag()) {
                            index = geofenceObjects.indexOf(tempChild);
//                        Log.d("NOPE", "FOUND INDEX: "+index);
                            geoQuery = geoQueryList.get(index);
                            geoQueryList.remove(index);
//                        Log.d("NOPE", "FOUND GeoQuery Object: "+geoQuery.toString());

                            geoQuery.removeAllListeners();

                            Log.d("olalalal", "Removed the Listeners!! " + geoQuery.toString());


                        }
                        geoQuery = geoFire.queryAtLocation(centerOfFence[0], radius[0]);

                        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                            @Override
                            public void onKeyEntered(String key, GeoLocation location) {
                                    for(BMSData child:LoginActivity.BMS)
                                    {
                                        if(key.equals(child.getId()))
                                        {
                                            temp=child;
                                            break;
                                        }
                                    }

                                    if (tempChild.getId().equals("All")) {

                                        if (!temp.isAll_entered() || temp.isAll_exited()) {
                                            Log.d("YOSHHHHHHHHHH11", key + " ENTERED in " + tempChild.getId() + "\n" + geoQuery.toString());
                                            if(tempChild.notifyFlag) {
                                                notificationSender.sendNotification(key, "ENTERED", tempChild.getId());
                                            }
                                            temp.setAll_entered(true);
                                            temp.setAll_exited(false);

                                        }

                                    } else {
                                        if (!temp.isSelf_entered() || temp.isSelf_exited()) {
                                            Log.d("YOSHHHHHHHHHH11", key + " ENTERED in " + tempChild.getId() + "\n" + geoQuery.toString());
                                            if(tempChild.notifyFlag) {
                                                notificationSender.sendNotification(key, "ENTERED", tempChild.getId());
                                            }
                                            temp.setSelf_entered(true);
                                            temp.setSelf_exited(false);
                                        }
                                    }
                                    Log.d("close one after", temp.toStringBoolean());

                            }

                            @Override
                            public void onKeyExited(String key) {

                                for(BMSData child:LoginActivity.BMS)
                                {
                                    if(key.equals(child.getId()))
                                    {
                                        temp=child;
                                        break;
                                    }
                                }
                                Log.d("close one", temp.toStringBoolean());
                                   if(tempChild.getId().equals("All")) {
                                       Log.d("khali", tempChild.getId());
                                       if (!temp.isAll_exited()||temp.isAll_entered())
                                        {//  Toast.makeText(getActivity(), key + " EXITED from " + tempChild.getId(), Toast.LENGTH_SHORT).show();
                                           Log.d("YOSHHHHHHHHHH1", key + " EXITED from " + tempChild.getId() + "\n" +
                                                   geoQuery.toString());

                                           notificationSender.sendNotification(key, "EXITED", tempChild.getId());
                                           temp.setAll_exited(true);
                                           temp.setAll_entered(false);
                                       }
                                       }
                                    else {
                                       if (!temp.isSelf_exited()||temp.isSelf_entered()) {
                                           Log.d("YOSHHHHHHHHHH1", key + " EXITED from " + tempChild.getId() + "\n" +
                                                   geoQuery.toString());

                                           notificationSender.sendNotification(key, "EXITED", tempChild.getId());
                                           temp.setSelf_exited(true);
                                           temp.setSelf_entered(false);
                                       }
                                   }
                                Log.d("close one after", temp.toStringBoolean());
                            }

                            @Override
                            public void onKeyMoved(String key, GeoLocation location) {

                            }

                            @Override
                            public void onGeoQueryReady() {
                            tempChild.notifyFlag=true;
                            }

                            @Override
                            public void onGeoQueryError(DatabaseError error) {

                            }
                        });
                        tempChild.setGeoQueryFlag(true);
                        //  Log.d("THINKS SO!!", "onMapReady: "+geofenceObjects.get(0).getGeoQuery().getCenter());

                        geoQueryList.add(index, geoQuery);
                        Log.d("Kai Taan e", Integer.toString(geoQueryList.size()));

                    }
                    }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        tempRef=Instance.getReference("Geofence Monitoring/All");
        geoFire=new GeoFire(tempRef);

        bmsref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //mMap.clear();


                for(DataSnapshot child:dataSnapshot.getChildren())
                {
                   // Log.d("TAG", "MY HONOUR: "+child.getKey());
                    temp=new BMSData();
                    temp=child.getValue(BMSData.class);
                    temp.setId(child.getKey());
                    vehicles.add(temp);
                   // Log.d("GOTCHA:---", temp.toString());
                    if(flag) {
                        LatLng sydney = new LatLng(temp.getLattitude(), temp.getLongitude());
                        m = mMap.addMarker(new MarkerOptions().position(sydney).title(temp.getId()));
                        String s = "Current:- " + temp.getBattery_Curr() + "\n" +
                                "Lattitude:- " + temp.getLattitude() +
                                "Longitude:- " + temp.getLongitude();
                        m.setSnippet(s);
                        //m.showInfoWindow();
                        MyMarker tempMarker = new MyMarker(m, temp.getId(), new LatLng(temp.getLattitude(), temp.getLongitude())
                                , System.currentTimeMillis() / 1000);


                        Markers.add(tempMarker);


                        geoFire.setLocation(temp.getId(), new GeoLocation(temp.getLattitude(), temp.getLongitude()));
                        for (GeofenceObjects geofenceChild : geofenceObjects) {
                            if (temp.getId().equals(geofenceChild.getId())) {
                                DatabaseReference tempRef1 = Instance.getReference("Geofence Monitoring/" + temp.getId());
                                GeoFire geoFire1 = new GeoFire(tempRef1);
                                geoFire1.setLocation(temp.getId(), new GeoLocation(temp.getLattitude(), temp.getLongitude()));
                                break;
                            }
                        }

                        LatLng tempLatLng = null;
                        for (MyMarker temp : Markers) {
//                        Log.d("kaunbho", temp.toString());
                            if (temp.getId().equals(LoginActivity.focusOnfromSearchFragment) &&
                                    LoginActivity.focusOnfromSearchFragment != "NULL") {
                                Log.d("arehyo", temp.toString());

                                tempLatLng = temp.getMarker().getPosition();
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                        tempLatLng, 10
                                ));
                                LoginActivity.focusOnfromSearchFragment="NULL";
                                break;
                            }

                        }

                    }
                   // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(searchMarker.getMarker().getPosition(),10));

//                    else
//                    {
//                        for (MyMarker tempChild:Markers)
//                        {
//                            if(temp.getId().equals(tempChild.getId()))
//                            {
//                                String s="Current:- "+temp.getBattery_Curr() + "\n" +
//                                        "Lattitude:- "+ temp.getLattitude()+
//                                        "Longitude:- "+temp.getLongitude();
//                                Long before=tempChild.getTime();
//
//                                Long after=System.currentTimeMillis()/1000;
//                                LatLng prevPos=tempChild.getPreviousPosition();
//                                Log.d("PREVIOUS", Double.toString(prevPos.latitude)+Double.toString(prevPos.longitude));
//                                tempChild.getMarker().remove();
//                                LatLng sydney = new LatLng(temp.getLattitude(), temp.getLongitude());
//                                tempChild.setMarker(mMap.addMarker(new MarkerOptions().position(sydney).title(temp.getId())));
//                                tempChild.setPreviousPosition(sydney);
//                                tempChild.getMarker().setSnippet(s);
//                                tempChild.setTime(after);
//                                double time=after-before;
//                                double distance=distanceTravelled(prevPos,sydney);
//                                double speed=distance/time;
//                                speed=speed*5/18;
////                                Log.d("PREVIOUS TIME",  Long.toString(before));
////                                Log.d("CURRENT TIME",  Long.toString(after));
////                                Log.d("TIME ELAPSED", Double.toString(time));
////                                Log.d("DISTANCE", Double.toString(distance));
////                                Log.d("SPEED", Double.toString(speed));
//
//
//                                break;
//
//                            }
//                        }
//                    }
                    // BMS.add(temp);

                }
                flag=false;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        if(!LoginActivity.focusOnfromSearchFragment.equals("NULL"))
//        {MyMarker marker = null;
//            for(MyMarker temp:Markers)
//            {
//                if(temp.getId().equals(LoginActivity.focusOnfromSearchFragment))
//                {
//                    marker=temp;
//                    break;
//                }
//            }
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPreviousPosition(),15));
//        }

             bmsref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                BMSData temp;
                temp=dataSnapshot.getValue(BMSData.class);
                temp.setId(dataSnapshot.getKey());

                Log.d("UPpppppp", tempRef.toString());

                Log.d("UPpppppp", temp.toString());

                DatabaseReference myref=Instance.getReference("Geofence Monitoring/"+"All");
                GeoFire myGeofire=new GeoFire(myref);
                myGeofire.setLocation(temp.getId(),new GeoLocation(temp.getLattitude(),temp.getLongitude()));
                for (GeofenceObjects geoFenceChild:geofenceObjects) {

                    if(temp.getId().equals(geoFenceChild.getId()))
                    {
                        DatabaseReference myref1 = Instance.getReference("Geofence Monitoring/" + temp.getId());
                        GeoFire myGeofire1 = new GeoFire(myref1);
                        myGeofire1.setLocation(temp.getId(), new GeoLocation(temp.getLattitude(), temp.getLongitude()));
                    }
                }

                for (MyMarker tempChild:Markers)
                {


                    if(dataSnapshot.getKey().equals(tempChild.getId()))
                    {
                        Long before=tempChild.getTime();
                        Long after=System.currentTimeMillis()/1000;
                        LatLng prevPos=tempChild.getPreviousPosition();

                        tempChild.getMarker().remove();
                        Log.d("PREVIOUS", Double.toString(prevPos.latitude)+"  "+Double.toString(prevPos.longitude));


                        LatLng sydney = new LatLng(temp.getLattitude(), temp.getLongitude());
                        tempChild.setMarker(mMap.addMarker(new MarkerOptions().position(sydney).title(dataSnapshot.getKey())));
                            tempChild.setCurrPosition(new LatLng(tempChild.getMarker().getPosition().latitude,tempChild.getMarker().getPosition().longitude));


                        tempChild.setPreviousPosition(sydney);

                        Log.d("CURRENT", Double.toString(sydney.latitude)+"  "+Double.toString(sydney.longitude));


                        tempChild.getMarker().setSnippet("Current:- "+temp.getBattery_Curr()+"\n"
                                + "Lattitude:- "+ temp.getLattitude()
                                + "Longitude:- "+temp.getLongitude());

                        tempChild.setTime(after);
                        double time=after-before;
                        double distance=distanceTravelled(prevPos,sydney);
                        double speed=distance/time;
                        speed=speed*5/18;


                        String Direction=travellingDirection(prevPos,sydney);
//
//                        Log.d("PREVIOUS TIME",  Long.toString(before));
//                        Log.d("CURRENT TIME",  Long.toString(after));
                        Log.d("TIME ELAPSED", Double.toString(time));
                        Log.d("DISTANCE", Double.toString(distance));
                        Log.d("SPEED", Double.toString(speed));
                        Log.d("DIRECTION", Direction);


                        break;

                    }
                }
                LatLng tempLatLng = null;
//                for (MyMarker temp1 : Markers) {
////                        Log.d("kaunbho", temp.toString());
//                    if (temp1.getId().equals(LoginActivity.focusOnfromSearchFragment) &&
//                            LoginActivity.focusOnfromSearchFragment.equals(dataSnapshot.getKey())
//                      ) {
//                        Log.d("arehyo", temp1.toString());
//
//                        tempLatLng = temp1.getMarker().getPosition();
//                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                                tempLatLng, 10
//                        ));
//                        break;
//                    }
//
//                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




       Log.d("ohohhoohh", Integer.toString(Available.size()));
        if (LoginActivity.MapType != 0)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
       // mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
       // MapType=getArguments().getInt("MapType");
       // Toast.makeText(getContext(), Integer.toString(MapType), Toast.LENGTH_SHORT).show();
       // Log.d("hurrr", "onMapReady: "+LoginActivity.BMS.get(0).toString());


        LatLng sydney = new LatLng(-34, 151);
        m=  mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        m.setFlat(true);
        m.setSnippet("yohoho");
        m.showInfoWindow();

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

               popupClass showPopoup=new popupClass();
                Log.d("MARKER Position", marker.getPosition().latitude +" " +marker.getPosition().longitude);

                for(MyMarker tempMarker:Markers)
               {
                   if(tempMarker.getId().equals(marker.getTitle()))
                   {
                       MapsActivity.forWho=tempMarker.getId();
                       Log.d("Found It", tempMarker.getId());
                   }
               }


                showPopoup.show(getActivity().getSupportFragmentManager(),"myPopup");

                return false;
            }
        });

    }

    public static HomeFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type",type);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public String travellingDirection(LatLng start,LatLng end)
    {
        Double lat1 = start.latitude;
        Double lat2 = end.latitude;
        Double lon1 = start.longitude;
        Double lon2 = end.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double y=Math.sin(dLon)*Math.cos(lat2);
        double x=Math.cos(lat1)*Math.sin(lat2)-
                Math.sin(lat1)*Math.cos(lat2)*Math.cos(dLon);
        double brng=Math.atan2(y,x);
        brng=Math.toDegrees(brng);
        if(brng<0)
            brng=brng+360;
        String directions[]={ "N","NNE","NE","ENE","E","ESE","SE","SSE","S","SSW","SW","WSW"
        ,"W","WNW","NW","NNW","N"};


        double index=((brng%360)/22.5)+0.5;
        int i=(int)index;
        return directions[i];
    }

    public double distanceTravelled(LatLng start,LatLng end)
    {
        double R = 6372800; // Radious of the earth
        Double lat1 = start.latitude;
        Double lat2 = end.latitude;
        Double lon1 = start.longitude;
        Double lon2 = end.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;

    }
    private static Double toRad(Double value) {
        return value * Math.PI / 180;
    }

//    public String CHANNEL_ID;
//    public void createChannel()
//    {
//        CharSequence channelName=CHANNEL_ID;
//        String channelDesc="channelDesc";
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//        {
//            int importance=NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel=new NotificationChannel(CHANNEL_ID,channelName,importance);
//            channel.setDescription(channelDesc);
//            NotificationManager notificationManager=getActivity().getSystemService(NotificationManager.class);
//            assert  notificationManager!=null;
//            NotificationChannel currChannel=notificationManager.getNotificationChannel(CHANNEL_ID);
//            if(currChannel==null)
//                notificationManager.createNotificationChannel(channel);
//        }
//    }
//    public void sendNotification(String bms_id,String event,String geofence_id)
//    {
//        String message = null;
//        if(event.equals("ENTERED"))
//        {
//            message=bms_id+" "+event+" in "+geofence_id+".";
//        }
//        else if(event.equals("EXITED"))
//        {
//            message=bms_id+" "+event+" from "+geofence_id+".";
//        }
//
//        CHANNEL_ID="YOSH1002";
////assert getActivity()!=null;
//        createChannel();
//        Log.d("yoho", "sendNotification: Insdie thhe method boiii");
//        NotificationCompat.Builder builder=new NotificationCompat.Builder(getActivity(),CHANNEL_ID).
//                setSmallIcon(R.drawable.ic_message).
//                setContentTitle("GEOFENCE UPDATE").
//                setContentText(message).
//                setPriority(NotificationCompat.PRIORITY_DEFAULT).
//                setAutoCancel(true);
//
////        Intent intent = new Intent(this,Main2Activity.class);
////        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////        intent.putExtra("message","yohohohhohohhhohoh");
////
////        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
////
////        builder.setContentIntent(pendingIntent);
//        NotificationManagerCompat notificationManager=NotificationManagerCompat.from(getActivity());
//        int notificationId=(int)(System.currentTimeMillis()/4);
//        notificationManager.notify(notificationId,builder.build());
//
////        NotificationManager notificationManager=(NotificationManager) getSystemService(
////                Context.NOTIFICATION_SERVICE);
////
////        //notificationManager.notify();
////        notificationManager.notify(1,builder.build());
//    }
}

