package com.example.trombinoscope;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.trombinoscope.certificate.Certificate;
import com.example.trombinoscope.view.MainViewModel;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;


public class MainActivity extends AppCompatActivity {

    private CookieStore cookieStore;
    private CookieManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Certificate.handleSSLHandshake();
        setContentView(R.layout.activity_main);
        MainViewModel model = new ViewModelProvider(this).get(MainViewModel.class);
        cookieStore=model.getCookie();
        manager=model.getManager();
        CookieHandler.setDefault(manager);
    }


    public void onTaskRemoved(Intent rootIntent) {
        cookieStore.removeAll();
    }
}