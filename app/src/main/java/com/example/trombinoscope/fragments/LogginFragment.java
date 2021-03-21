package com.example.trombinoscope.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trombinoscope.HachageMDP;
import com.example.trombinoscope.MainActivity;
import com.example.trombinoscope.MySingleton;
import com.example.trombinoscope.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //Instances
    private Button btn_loggin;
    private Button btn_register;
    private EditText pseudo, password;
    private JSONObject js = new JSONObject();
    private CheckBox rememberCheckbox;
    private TextView forgotPw;
    private HachageMDP hash = new HachageMDP();
    private String pwHash;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LogginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LogginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LogginFragment newInstance(String param1, String param2) {
        LogginFragment fragment = new LogginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loggin, container, false);
        ((MainActivity)getActivity()).setDrawer_Locked();//Gestion du nav drawer
        ((MainActivity)getActivity()).getSupportActionBar().hide();//Gestion de la Toolbar
        btn_register = view.findViewById(R.id.btn_register);
        btn_loggin = view.findViewById(R.id.login);
        pseudo = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        rememberCheckbox = view.findViewById(R.id.rememberMe);
        forgotPw=view.findViewById(R.id.ForgotPw);

        SharedPreferences userValuesSave = this.getActivity().getSharedPreferences("userValuesSave", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userValuesSave.edit();

        // Recupération des données users si la checkbox a été cochée
        pseudo.setText(userValuesSave.getString("userName", ""));
        password.setText(userValuesSave.getString("userPw", ""));
        rememberCheckbox.setChecked(userValuesSave.getBoolean("CheckBox", false));

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate((R.id.action_logginFragment_to_signIn));
            }
        });

        forgotPw.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate((R.id.action_logginFragment_to_forgot_Pw));
            }
        });

        btn_loggin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String Pseudo = pseudo.getText().toString().trim();
                String Password = password.getText().toString().trim();

                // Saved values if checked checkbox
                if (rememberCheckbox.isChecked()) {
                    editor.putString("userName", Pseudo);
                    editor.putString("userPw", Password);
                    editor.putBoolean("CheckBox", true);

                    editor.commit();
                    Snackbar.make(btn_loggin, getResources().getString(R.string.Action_RememberMe_Checked), 1000).show();
                }
                else {
                    editor.clear();
                    editor.commit();
                }
                if (!Pseudo.isEmpty() || !Password.isEmpty()) {
                    pwHash=hash.hachageMDP(Password);
                    Login(view, Pseudo, Password);
                } else {
                    Snackbar.make(v, getResources().getString(R.string.Err_Saisie_Chp_Vide), 1000).show();
                }
            }
        });
        return view;
    }


    private void Login (View view, String pseudo, String password){
        MySingleton s = MySingleton.getInstance(getContext());
        String url = s.getUrl();
        update(pseudo, password);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("res").equals("true")) {
                        Navigation.findNavController(view).navigate(R.id.action_logginFragment_to_trombinoscopesList);
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
        s.addToRequestQueue(jsonObjectRequest);
    }

    private void update (String username, String password){
        try {
            js.put("request", "authentification");
            js.put("username", username);
            js.put("password", pwHash);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}