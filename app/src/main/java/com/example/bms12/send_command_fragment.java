package com.example.bms12;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class send_command_fragment extends Fragment {
    private Button poweroffbattery,analyzebattery,resetsetting,reboot;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_command,container,false);
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        poweroffbattery = view.findViewById(R.id.poweroffbatterybtn);
        analyzebattery = view.findViewById(R.id.analyzebatterybtn);
        resetsetting = view.findViewById(R.id.resetsettingbtn);
        reboot = view.findViewById(R.id.rebootbtn);

        super.onViewCreated(view, savedInstanceState);

        poweroffbattery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        analyzebattery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        resetsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        reboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });





    }
}

