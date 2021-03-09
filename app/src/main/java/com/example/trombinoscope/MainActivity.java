package com.example.trombinoscope;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.trombinoscope.certificate.Certificate;
import com.example.trombinoscope.fragments.Nav_drawer_fragments.Contact_support;
import com.example.trombinoscope.fragments.Nav_drawer_fragments.Info;
import com.example.trombinoscope.fragments.Nav_drawer_fragments.User_profil;
import com.example.trombinoscope.fragments.UserCondition;
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
        //deep link
        Uri uri = getIntent().getData();
        if(uri!=null)
        {
            Log.e("Deep link", "Deep link ici");
            String path = uri.toString();
            Toast.makeText(MainActivity.this,"Path ="+path,Toast.LENGTH_LONG).show();
            //redirection vers le fragment SignIn
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);  // Hostfragment
            NavInflater inflater = navHostFragment.getNavController().getNavInflater();
            NavGraph graph = inflater.inflate(R.navigation.nav_file);
            graph.setStartDestination(R.id.signIn);

            navHostFragment.getNavController().setGraph(graph);
        }

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
            case R.id.nav_conditionuser :
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_conditionuser, new UserCondition()).commit();
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