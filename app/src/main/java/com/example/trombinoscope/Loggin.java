package com.example.trombinoscope;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trombinoscope.certificate.Certificate;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;


public class Loggin extends AppCompatActivity {

    private Button btn_loggin;
    private EditText pseudo, password;
    private JSONObject js = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggin);
        Certificate.handleSSLHandshake();
        btn_loggin = findViewById(R.id.login);
        pseudo = findViewById(R.id.username);
        password = findViewById(R.id.password);

        btn_loggin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String Pseudo = pseudo.getText().toString().trim();
                String Password = password.getText().toString().trim();

                if (!Pseudo.isEmpty() || !Password.isEmpty()) {
                    Login(Pseudo, Password);

                } else {
                    Snackbar.make(v, "pls Enter valide values", 2000).show();
                }


            }
        });


    }

    private void Login(String pseudo, String password) {
        String url = "https://192.168.1.21:5000/";
        update(pseudo, password);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getString("res").equals("true")) {
                        finish();
                    }
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


        queue.add(jsonObjectRequest);
    }

    private void update(String username, String password) {
        try{
            js.put("request", "authentification");
            js.put("username", username);
            js.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




}