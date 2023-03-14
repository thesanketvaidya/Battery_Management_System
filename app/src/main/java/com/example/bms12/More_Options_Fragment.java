package com.example.bms12;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class More_Options_Fragment extends Fragment {
    private Button sendcommandbtn,geofencebtn, batterydetailsbtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_more__options_,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        sendcommandbtn = view.findViewById(R.id.sendcommandbtn);
        geofencebtn = view.findViewById(R.id.geofencebtn);
        batterydetailsbtn = view.findViewById(R.id.batterydetailbtn);
        super.onViewCreated(view, savedInstanceState);

        //on sendcommand button click

        sendcommandbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment();

            }
        });



        geofencebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        Intent i=new Intent(getContext(),MapsActivity.class);
        startActivity(i);
            }
        });

        //on battery details
        batterydetailsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    public void openFragment(){
        send_command_fragment fragment = new send_command_fragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.send_command_fragment_container,fragment,"send_command").commit();

    }

}
