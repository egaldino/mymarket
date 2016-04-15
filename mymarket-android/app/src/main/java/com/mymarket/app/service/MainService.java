package com.mymarket.app.service;

import android.app.IntentService;

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
