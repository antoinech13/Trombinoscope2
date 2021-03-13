package com.example.trombinoscope;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.trombinoscope.certificate.Certificate;
import com.example.trombinoscope.fragments.Nav_drawer_fragments.HideNavDrawer;
import com.example.trombinoscope.view.MainViewModel;
import com.google.android.material.navigation.NavigationView;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.util.List;


public class MainActivity extends AppCompatActivity implements HideNavDrawer {
    private DrawerLayout drawer;
    private CookieStore cookieStore;
    private CookieManager manager;
    private NavController navController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Certificate.handleSSLHandshake();
        setContentView(R.layout.activity_main);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Nav Controller
        navController=Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationView navigationView = findViewById(R.id.nav_view);
        NavigationUI.setupWithNavController(navigationView, navController);
        drawer=findViewById(R.id.drawer_layout);
        NavigationUI.setupActionBarWithNavController(this, navController, drawer);



        //deep link
        Uri uri = getIntent().getData();
        if(uri!=null)
        {
            String path = uri.toString();
            Toast.makeText(MainActivity.this,"Path ="+path,Toast.LENGTH_LONG).show();
            //redirection vers le fragment SignIn
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);  // Hostfragment
            NavInflater inflater = navHostFragment.getNavController().getNavInflater();
            NavGraph graph = inflater.inflate(R.navigation.nav_file);
            graph.setStartDestination(R.id.signIn);

            navHostFragment.getNavController().setGraph(graph);
        }

        //Cookies
        MainViewModel model = new ViewModelProvider(this).get(MainViewModel.class);
        cookieStore=model.getCookie();
        manager=model.getManager();
        CookieHandler.setDefault(manager);


    }

    @Override
    public boolean onSupportNavigateUp(){
        return NavigationUI.navigateUp(navController, drawer);
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

    public void setDrawer_Locked() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
    public void setDrawer_UnLocked() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }



}