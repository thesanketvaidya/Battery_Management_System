package com.example.bms12;

import android.app.Application;

public class currentDeviceDetailFragment extends Application {
    int current;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }
}
