package com.example.trombinoscope;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.trombinoscope.certificate.Certificate;

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
        cookieStore = new CookieManager().getCookieStore();;
        manager = new CookieManager( cookieStore, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(manager);
    }


    public void onTaskRemoved(Intent rootIntent) {
        cookieStore.removeAll();
    }
}