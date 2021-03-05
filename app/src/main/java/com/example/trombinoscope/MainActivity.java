package com.example.trombinoscope;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.trombinoscope.certificate.Certificate;
import com.example.trombinoscope.fragments.Nav_drawer_fragments.Contact_support;
import com.example.trombinoscope.fragments.Nav_drawer_fragments.Info;
import com.example.trombinoscope.fragments.Nav_drawer_fragments.User_profil;
import com.example.trombinoscope.view.MainViewModel;
import com.google.android.material.navigation.NavigationView;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private CookieStore cookieStore;
    private CookieManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Certificate.handleSSLHandshake();
        setContentView(R.layout.activity_main);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Nav Drawer
        drawer=findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //Cookies
        MainViewModel model = new ViewModelProvider(this).get(MainViewModel.class);
        cookieStore=model.getCookie();
        manager=model.getManager();
        CookieHandler.setDefault(manager);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_profil :
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new User_profil()).commit();
                break;
            case R.id.nav_a_propos :
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new Info()).commit();
                break;
            case R.id.nav_contact :
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new Contact_support()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void onTaskRemoved(Intent rootIntent) {
        cookieStore.removeAll();
    }

    //Methode Back dans navigation drawer
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }


}