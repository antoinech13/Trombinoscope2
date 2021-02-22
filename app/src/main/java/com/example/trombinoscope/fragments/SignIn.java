package com.example.trombinoscope.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trombinoscope.FtpConnection;
import com.example.trombinoscope.MySingleton;
import com.example.trombinoscope.R;
import com.example.trombinoscope.dataStructure.Trombi;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignIn#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignIn extends Fragment {
//BLABLABLA

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private JSONObject js = new JSONObject();

    private Button register;
    private EditText nom, prenom, email, pseudo, pw, pwc ;

    private CheckBox checkBox;
    private TextView ConditionUser;

    public SignIn() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignIn.
     */
    // TODO: Rename and change types and number of parameters
    public static SignIn newInstance(String param1, String param2) {
        SignIn fragment = new SignIn();
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
        View view =  inflater.inflate(R.layout.fragment_sign_in, container, false);
        register = view.findViewById(R.id.btnRegister);
        nom = view.findViewById(R.id.Name);
        prenom = view.findViewById(R.id.Prenom);
        email = view.findViewById(R.id.userEmail);
        pseudo = view.findViewById(R.id.Pseudo);
        pw = view.findViewById(R.id.Password);
        pwc = view.findViewById(R.id.PasswordConfirm);
        checkBox = view.findViewById(R.id.protect_data);
        ConditionUser = view.findViewById(R.id.ConditionUser);

        ConditionUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_signIn_to_userCondition);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            // Action quand on clique sur le btn register
            public void onClick(View v) {
                //Recupération des valeurs entrées dans les inputs
                String Nom = nom.getText().toString().trim();
                String Prenom = prenom.getText().toString().trim();
                String Email = email.getText().toString().trim();
                String Pseudo = pseudo.getText().toString().trim();
                String Pw = pw.getText().toString().trim();
                String Pwc = pwc.getText().toString().trim();

                //Verification que les champs sont remplis
                if (Nom.isEmpty() || Prenom.isEmpty() ||Pseudo.isEmpty() || Email.isEmpty()
                        || Pw.isEmpty() || Pwc.isEmpty()){
                    Snackbar.make(v, getResources().getString(R.string.Msg_err_saisie_SignIn), 1000).show();
                }
                else if (!(checkBox.isChecked())) {
                    Log.d("test check box", "la checkbox n'a pas été cochée");
                    Snackbar.make(v, getResources().getString(R.string.Msg_err_Accept_Give_Data), 1000).show();
                }

                else {
                    if (!Pw.equals(Pwc))
                        Snackbar.make(v, getResources().getString(R.string.Msg_Err_MDP_diff), 1000).show();
                    else{
                        Log.d(" 2eme test passé", " 2eme test passé");
                        addUser(v);
                        Log.d(" adduser executé", " adduser executé");
                    }
                }
                clear();
            }
        });
        return view;
    }

    //Ajouter un membre
    private void addUser(View v){
        MySingleton s = MySingleton.getInstance(getContext());
        String url = s.getUrl();
        update();
        Log.d(" update executé", " update executé");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("res").equals("true")) {// res= nom de la clé de la reponse fournie par flask !!! JsonObject doit etre converti en String
                        Snackbar.make(v, getResources().getString(R.string.Inscription_valid), Snackbar.LENGTH_LONG).show(); // Trouver comment envoyer mail à l'utilisateur
                        Log.d(" Je suis enregistré", " Je suis enregistré");
                        Navigation.findNavController(v).navigate(R.id.action_signIn_to_logginFragment);//Retour à la page d'acceuil

                    }
                    else
                        Snackbar.make(v, getResources().getString(R.string.SignIn_pseudo_email_invalid), Snackbar.LENGTH_LONG).show();
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
            js.put("request", "addUser");
            js.put("prenom",this.prenom.getText());
            js.put("nom",this.nom.getText());
            js.put("pseudo",this.pseudo.getText());
            js.put("email",this.email.getText());
            js.put("password",this.pw.getText());
        } catch (JSONException e) {
            e.printStackTrace();
            // Obligatoire avec jsonObject
        }
    }


    //Remise à 0 des variables d'instance
    private void clear() {
        this.nom.getText().clear();
        this.prenom.getText().clear();
        this.email.getText().clear();
        this.pseudo.getText().clear();
        this.pw.getText().clear();
        this.pwc.getText().clear();
    }
}