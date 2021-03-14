package com.example.trombinoscope.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

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
 * Use the {@link Sign_In_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Sign_In_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Instances
    private JSONObject js = new JSONObject();
    private Button register;
    private EditText nom, prenom, email, pseudo, pw, pwc ;
    private CheckBox checkBox;
    private TextView conditionUser;
    private HachageMDP hash = new HachageMDP();
    private String pwHash;

    public Sign_In_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Sign_In_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Sign_In_Fragment newInstance(String param1, String param2) {
        Sign_In_Fragment fragment = new Sign_In_Fragment();
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
        ((MainActivity)getActivity()).setDrawer_Locked();//Gestion du nav drawer
        ((MainActivity)getActivity()).getSupportActionBar().hide();//Gestion de la Toolbar

        //Association des éléments du layout
        register = view.findViewById(R.id.btnRegister);
        nom = view.findViewById(R.id.Name);
        prenom = view.findViewById(R.id.Prenom);
        email = view.findViewById(R.id.userEmail);
        pseudo = view.findViewById(R.id.Pseudo);
        pw = view.findViewById(R.id.Password);
        pwc = view.findViewById(R.id.PasswordConfirm);
        checkBox = view.findViewById(R.id.protect_data);
        conditionUser = view.findViewById(R.id.ConditionUser);

        conditionUser.setOnClickListener(new View.OnClickListener() {
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
                    Snackbar.make(v, getResources().getString(R.string.Err_Saisie_Chp_Vide), 1000).show();
                }
                else if (!Email.contains("@"))
                    Snackbar.make(v, getResources().getString(R.string.Err_Invalid_Email), 1000).show();
                else if (!(checkBox.isChecked())) {
                    Snackbar.make(v, getResources().getString(R.string.Err_Accept_Give_Data), 1000).show();
                }
                else {
                    if (!Pw.equals(Pwc))
                        Snackbar.make(v, getResources().getString(R.string.Err_Pw_Diff), 1000).show();
                    else{
                        //Tous les champs sont remplis correctement, enregistrement du nouvel utilisateur
                        pwHash = hash.hachageMDP(Pw);
                        addUser(v);
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("res").equals("true")) {// res= nom de la clé de la reponse fournie par flask !!! JsonObject doit etre converti en String
                        Snackbar.make(v, getResources().getString(R.string.Info_Msg_Inscription_valid), Snackbar.LENGTH_LONG).show();
                        Navigation.findNavController(v).navigate(R.id.action_signIn_to_logginFragment);//Retour à la page d'acceuil
                    }
                    else
                        Snackbar.make(v, getResources().getString(R.string.Err_Duplicated_Pw_Email), Snackbar.LENGTH_LONG).show();
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
            js.put("password", pwHash);
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