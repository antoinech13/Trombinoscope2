package com.example.trombinoscope.fragments.Nav_drawer_fragments;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.example.trombinoscope.MySingleton;
import com.example.trombinoscope.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class User_profil extends Fragment {

    private TextView lastname;
    private TextView firstname;
    private TextView email;
    private JSONObject js = new JSONObject();


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profil, container, false);

        lastname = view.findViewById(R.id.name_user);
        firstname = view.findViewById(R.id.firstname_user);
        email = view.findViewById(R.id.user_email);

        RequestUser();

        return view;
    }

    private void RequestUser() {
        MySingleton s = MySingleton.getInstance(getContext());
        String url = s.getUrl();
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
                    lastname.setText(response.getString("Nom"));
                    firstname.setText(response.getString("Prenom"));
                    email.setText(response.getString("Email"));
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
}
