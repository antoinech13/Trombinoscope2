package com.example.trombinoscope.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.trombinoscope.HachageMDP;
import com.example.trombinoscope.MainActivity;
import com.example.trombinoscope.MySingleton;
import com.example.trombinoscope.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Reset_Pw#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Reset_Pw extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private JSONObject js = new JSONObject();
    private Button btn_Reset;
    private EditText pw, pwc ;
    private String email;
    private HachageMDP hash = new HachageMDP();
    private String pwHash;

    public Reset_Pw() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Reset_Pw.
     */
    // TODO: Rename and change types and number of parameters
    public static Reset_Pw newInstance(String param1, String param2) {
        Reset_Pw fragment = new Reset_Pw();
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
        View view = inflater.inflate(R.layout.fragment_reset__pw, container, false);
        ((MainActivity)getActivity()).setDrawer_Locked();
        btn_Reset=view.findViewById(R.id.reset);
        pw= view.findViewById(R.id.reset_Pw);
        pwc= view.findViewById(R.id.reset_Pw_Confirm);
        email = getArguments().getString("Email"); // Récupération de l'émail de Forgot_PW
        btn_Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            // Action quand on clique sur le btn register
            public void onClick(View v) {
                //Recupération des valeurs entrées dans les inputs
                String Pw = pw.getText().toString().trim();
                String Pwc = pwc.getText().toString().trim();

                //Verification que les champs sont remplis
                if ( Pw.isEmpty() || Pwc.isEmpty())
                    Snackbar.make(v, getResources().getString(R.string.Msg_err_saisie_SignIn), 1000).show();
                else if (!Pw.equals(Pwc))
                    Snackbar.make(v, getResources().getString(R.string.Msg_Err_MDP_diff), 1000).show();
                else {
                    pwHash = hash.hachageMDP(Pw);
                    resetPw(v);
                }


            }
        });

        return  view;
    }

    //Reinitialiser le mdp
    private void resetPw(View v){
        MySingleton s = MySingleton.getInstance(getContext());
        String url = s.getUrl();
        update();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("res").equals("true")) {// res= nom de la clé de la reponse fournie par flask !!! JsonObject doit etre converti en String
                        Snackbar.make(v, getResources().getString(R.string.reset_Pw_valid), Snackbar.LENGTH_LONG).show(); // Trouver comment envoyer mail à l'utilisateur
                        Navigation.findNavController(v).navigate(R.id.action_reset_Pw_to_logginFragment);//Retour à la page d'acceuil
                    }
                    else
                        Snackbar.make(v, getResources().getString(R.string.Err_Reset_Pw), Snackbar.LENGTH_LONG).show();
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

    //Envoi les variables a flask
    public void update(){
        try {
            js.put("request", "resetPw");
            js.put("password",pwHash);
            js.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
            // Obligatoire avec jsonObject
        }
    }




}