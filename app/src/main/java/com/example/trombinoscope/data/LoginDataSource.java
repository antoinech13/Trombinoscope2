package com.example.trombinoscope.data;


import android.app.Application;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trombinoscope.data.model.LoggedInUser;
import com.example.trombinoscope.ui.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    private String url = "https://192.168.1.21:5000/";
    JSONObject js = new JSONObject();
    //Map<String, String> map;
    private String pseudo = "Admin";
    private String psw = "12345678";
    private boolean reponse;

    public void setFlag(boolean flag){
        this.reponse = flag;
    }

    public Result<LoggedInUser> login(String username, String password, LoginActivity ctx) {
        Log.d("yolo","FIRST");
        RequestQueue queue = Volley.newRequestQueue(ctx);

        try{
            js.put("request", "authentification");
            js.put("username", username);
            js.put("password", password);

            Log.d("yolo","SECOND");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("yolo","ici1");
        Log.d("yolo","ici");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    if(response.getString("res").equals("true")){
                        Log.d("res", "je suis dans le if");
                        setFlag(true);
                    }
                    else
                        setFlag(false);
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.d("yolo","erreur");
            }
        });



        Log.d("yolo","nik!!!!!");
        queue.add(jsonObjectRequest);
        //if(username.equals(this.pseudo) && password.equals(this.psw))
        if(this.reponse){

            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Admin");
            return new Result.Success<>(fakeUser);
        }
        else{
            return new Result.Error(new IOException("Error logging in"));
        }

        /*try {
            // TODO: handle loggedInUser authentication
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }*/
    }

    public void logout() {
        // TODO: revoke authentication
    }
}