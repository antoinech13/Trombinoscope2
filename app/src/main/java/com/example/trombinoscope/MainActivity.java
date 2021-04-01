package com.example.trombinoscope;

import androidx.annotation.NonNull;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.trombinoscope.certificate.Certificate;
import com.example.trombinoscope.fragments.Nav_drawer_fragments.HideNavDrawer;
import com.example.trombinoscope.view.MainViewModel;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;


public class MainActivity extends AppCompatActivity implements HideNavDrawer {
    private DrawerLayout drawer;
    private CookieStore cookieStore;
    private CookieManager manager;
    private NavController navController;
    private JSONObject js = new JSONObject();
    private TextView userName, userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Certificate.handleSSLHandshake();
        setContentView(R.layout.activity_main);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic__menu);

        //Nav Controller
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationView navigationView = findViewById(R.id.nav_view);
        NavigationUI.setupWithNavController(navigationView, navController);
        drawer = findViewById(R.id.drawer_layout);
        navigationView.setItemIconTintList(null);

        //Nav header
        View headerView = navigationView.getHeaderView(0);
            // get user name and email textViews
            userName = headerView.findViewById(R.id.name_user);
            userEmail = headerView.findViewById(R.id.email_user);

        //Home
        ImageView home = headerView.findViewById(R.id.home_user);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);  // Hostfragment
                NavInflater inflater = navHostFragment.getNavController().getNavInflater();
                NavGraph graph = inflater.inflate(R.navigation.nav_file);
                graph.setStartDestination(R.id.trombinoscopesList);
                navHostFragment.getNavController().setGraph(graph);
            }
        });

        //Parametrage ouverture drawer sur click
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
                Log.e("RequestUser","avant");
                RequestUser();
            }
        });

        //Paramètrage de la recherche dans la toolbar
        SearchView search = toolbar.findViewById(R.id.toolbarSearch);
        search.setVisibility(View.INVISIBLE);
        search.setQueryHint("Recherche..");
        search.setImeOptions(EditorInfo.IME_ACTION_DONE);

        //Deconnexion
        (navigationView.getMenu().findItem(R.id.logout)).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                logout();
                return false;
            }
        });


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
        if (drawer != null)
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
    public void setDrawer_UnLocked() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view =  inflater.inflate(R.layout.nav_header, container, false);
        return view;
    }

    private void RequestUser() {
        MySingleton s = MySingleton.getInstance(getApplicationContext());
        String url = s.getUrl();
        Log.e("RequestUser","passé");
        //demander la requete
        try {
            js.put("request","UserProfil");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //dans le js mettre le type de requete que je veux
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //clé de mon last name
                    userName.setText(response.getString("Nom"));
                    userEmail.setText(response.getString("Email"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        s.addToRequestQueue(jsonObjectRequest);
    }


    public void logout() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);  // Hostfragment
        NavInflater inflater = navHostFragment.getNavController().getNavInflater();
        NavGraph graph = inflater.inflate(R.navigation.nav_file);
        graph.setStartDestination(R.id.logginFragment);
        navHostFragment.getNavController().setGraph(graph);
        cookieStore.removeAll();

    }
}