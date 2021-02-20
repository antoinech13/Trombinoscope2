package com.example.trombinoscope;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.trombinoscope.certificate.Certificate;

import java.net.CookieHandler;
import java.net.CookieManager;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Intent i = new Intent(this, Loggin.class);
        //startActivity(i);
        Certificate.handleSSLHandshake();
        setContentView(R.layout.activity_main);
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
    }

}