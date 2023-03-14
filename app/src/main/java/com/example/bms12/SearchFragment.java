package com.example.bms12;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SearchFragment extends Fragment
{
    Button search;
    ListView mylistview;
    EditText searchkey;
    List<String> vehicles;
    FirebaseDatabase Instance;
    DatabaseReference dbref;
    SharedPreferences sharedPreferences1;
    SharedPreferences.Editor edit1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search,container,false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        search=getActivity().findViewById(R.id.Search);
        searchkey=getActivity().findViewById(R.id.searchKey);
        mylistview=getActivity().findViewById(R.id.mylist);
        vehicles=new ArrayList<>();
        Instance=FirebaseDatabase.getInstance();
        dbref=Instance.getReference("BMS Boards");
        sharedPreferences1=getActivity().getApplicationContext().getSharedPreferences("yosh",Context.MODE_PRIVATE);
        edit1=sharedPreferences1.edit();
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child:dataSnapshot.getChildren())
                {
                    edit1.putString(child.getKey(),child.getKey());
                    edit1.apply();
                   // vehicles.add(sharedPreferences1.getString(dataSnapshot.getKey(),null));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Map<String,?> keys=sharedPreferences1.getAll();
        for(Map.Entry<String,?> entry: keys.entrySet())
        {
            Log.d("RAGNAROK77", entry.getValue().toString());
           vehicles.add(entry.getValue().toString());
        }


        ArrayAdapter adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,vehicles);
        mylistview.setAdapter(adapter);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp=searchkey.getText().toString();
                if(!temp.equals("")) {

                    LoginActivity.focusOnfromSearchFragment = searchkey.getText().toString();
                    assert getFragmentManager() != null;
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new HomeFragment()).commit();
                }
                else
                {
                    Toast.makeText(getActivity(), "Nigga! Fill in the input box!", Toast.LENGTH_SHORT).show();

                }

                }
        });

        mylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              //  Toast.makeText(getActivity(), ((TextView)view).getText().toString(), Toast.LENGTH_SHORT).show();
                LoginActivity.focusOnfromSearchFragment =  ((TextView)view).getText().toString();
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
            }
        });
    }
}
