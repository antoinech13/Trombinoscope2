package com.example.trombinoscope.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.trombinoscope.MySingleton;
import com.example.trombinoscope.R;
import com.example.trombinoscope.dataStructure.Etudiant;
import com.example.trombinoscope.dataStructure.Trombi;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link edit_profil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class edit_profil extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //instances

    private TextView nom,prenom,email;
    private TextView newNom,newPrenom,newEmail;
    private Button valider;
    private JSONObject js = new JSONObject();

    private String idPromo;

    //les objets
    private Etudiant etu;

    private Trombi promo;

    public edit_profil() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment edit_profil.
     */
    // TODO: Rename and change types and number of parameters
    public static edit_profil newInstance(String param1, String param2) {
        edit_profil fragment = new edit_profil();
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
        View view =  inflater.inflate(R.layout.fragment_edit_profil, container, false);

        // l objet etudiant
        etu = getArguments().getParcelable("Etu"); //etu a recuperer toutes les infos de l etudiant

        promo = getArguments().getParcelable("idTrombi"); // on a recuperer toutes les infos de promo

        Log.e("etu", String.valueOf(etu)); // test
        Log.e("promo", String.valueOf(promo)); //test

        idPromo = promo.getId(); //on assigne une variable idpromo

        Log.e("idpromo",idPromo);

        // Les variables par rapport au layout
        newNom = view.findViewById(R.id.new_name);
        newPrenom = view.findViewById(R.id.new_prenom);
        newEmail = view.findViewById(R.id.new_mail);
        valider = view.findViewById(R.id.subprofile);

        nom = view.findViewById(R.id.name);
        prenom = view.findViewById(R.id.prenom);
        email = view.findViewById(R.id.mail);

        //recuperation des anciennes valeurs

        nom.setText(etu.getNom());
        prenom.setText(etu.getPrenom());
        email.setText(etu.getEMail());

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action quand on clique sur le btn delete
                editMember(v);
            }
        });


        return view;
    }





    //Fonction supprimer un membre/ pk ca marche sur les anciens et pas sur les nouveaux membres ajouter
    private void editMember(View v){
        MySingleton s = MySingleton.getInstance(getContext());
        String url = s.getUrl();
        update();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("res").equals("true")) {// res= nom de la clé de la reponse fournie par flask !!! JsonObject doit etre converti en String
                        Snackbar.make(v, getResources().getString(R.string.Msg_Info_Member_edit), Snackbar.LENGTH_LONG).show();
                        Navigation.findNavController(v).navigate(R.id.action_edit_profil_to_editTrombi,getArguments());//Retour à la page des membres
                    }
                    else
                        Snackbar.make(v, getResources().getString(R.string.Err_Member_edit), Snackbar.LENGTH_LONG).show();
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


    //fonction updade la base de donnee
    public void update(){
        try {
            js.put("request", "editMember"); //il faudra que je mette un autre nom car la la requete s appelle adduser
            js.put("email_m", this.email.getText());
            js.put("id_trombi", idPromo); //il faudra qu il recuperer l id promo car on a besoin du mail et du promo pour delete la personne
            js.put("newName", newNom.getText());
            js.put("newPrenom", newPrenom.getText());
            js.put("newEmail", newEmail.getText());
        } catch (JSONException e) {
            e.printStackTrace();
            // Obligatoire avec jsonObject
        }
    }



}
