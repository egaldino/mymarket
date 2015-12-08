package com.bastosbf.app.msearch.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class MainService extends IntentService {
    protected Properties properties;

    public MainService(String name) {
        super(name);
        try {
            InputStream is = getBaseContext().getAssets().open("mymarket.properties");
            properties = new Properties();
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
