package com.example.bms12;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class popupClass extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.bottom_popup,container,false);
        Button History,Details,More;
        History=v.findViewById(R.id.History);
        History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("Clicked History!");
                Intent i=new Intent(getContext(),History.class);
                startActivity(i);
                dismiss();
            }
        });

        Details=v.findViewById(R.id.Details);
        Details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("Clicked Details");
                Intent i=new Intent(getContext(),DeviceDetailsActivity.class);
                startActivity(i);
                dismiss();
            }
        });

        More=v.findViewById(R.id.More);
        More.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("Clicked More!");
             Intent i=new Intent(getContext(),CommandActivity.class);
                startActivity(i);
                dismiss();
            }
        });


        return v;
    }
    public interface BottomSheetListener
    {
        void onButtonClicked(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (BottomSheetListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()+" must Implement BottomSheetListener!");
        }
    }
}
