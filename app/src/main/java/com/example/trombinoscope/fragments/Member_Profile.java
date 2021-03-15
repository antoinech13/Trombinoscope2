package com.example.trombinoscope.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.trombinoscope.MainActivity;
import com.example.trombinoscope.MySingleton;
import com.example.trombinoscope.R;
import com.example.trombinoscope.dataStructure.Etudiant;
import com.example.trombinoscope.dataStructure.Trombi;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Member_Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Member_Profile extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    // mes variables

    // private EditText pseudo, password; pour plus tard pour la mise en propre
    private JSONObject js = new JSONObject();

    //les images view qui serviront de bouton
    private ImageView delete, edit_image,edit_profile;

    //les identifiants
    private ImageView img;
    private TextView nom,prenom,email;


    //les objets
    private Etudiant etu;

    private Trombi promo;
    private String idPromo;


    public Member_Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Member_Profile newInstance(String param1, String param2) {
        Member_Profile fragment = new Member_Profile();
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
        View view =  inflater.inflate(R.layout.fragment_member_profile, container, false);

        ((MainActivity)getActivity()).setDrawer_UnLocked(); //Gestion du nav drawer
        ((MainActivity)getActivity()).getSupportActionBar().show(); //Gestion de la toolbar

        // l objet etudiant
        etu = getArguments().getParcelable("Etu"); //etu a recuperer toutes les infos de l etudiant

        promo = getArguments().getParcelable("idTrombi"); // on a recuperer toutes les infos de promo

        Log.e("etu", String.valueOf(etu)); // test
        Log.e("promo", String.valueOf(promo)); //test

        idPromo = promo.getId(); //on assigne une variable idpromo

        Log.e("idpromo",idPromo);



        // Les variables par rapport au layout
        img = view.findViewById(R.id.img);

        nom = view.findViewById(R.id.nom);
        prenom = view.findViewById(R.id.prenom);
        email = view.findViewById(R.id.email);

        delete = view.findViewById(R.id.delete);
        edit_image = view.findViewById(R.id.edit_image);
        edit_profile = view.findViewById(R.id.edit_profile);

        //on recupere le valeurs
        img.setImageBitmap(etu.getImg());

        nom.setText(etu.getNom());
        prenom.setText(etu.getPrenom());
        email.setText(etu.getEMail());

        //methode delete pour supprimer un membre du trombi

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action quand on clique sur le btn delete
                deleteMember(v);
            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_profile_to_edit_profil);
            }

        });



        return view;
    }


    //Fonction supprimer un membre/ pk ca marche sur les anciens et pas sur les nouveaux membres ajouter
    private void deleteMember(View v){
        MySingleton s = MySingleton.getInstance(getContext());
        String url = s.getUrl();
        update();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("res").equals("true")) {// res= nom de la clé de la reponse fournie par flask !!! JsonObject doit etre converti en String
                        Snackbar.make(v, getResources().getString(R.string.Msg_Info_Member_Deleted), Snackbar.LENGTH_LONG).show();
                        Navigation.findNavController(v).navigate(R.id.action_profile_to_editTrombi,getArguments());//Retour à la page des membres

                    }
                    else
                        Snackbar.make(v, getResources().getString(R.string.Err_Member_Deleted), Snackbar.LENGTH_LONG).show();
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
            js.put("request", "deleteMember"); //il faudra que je mette un autre nom car la la requete s appelle adduser
            js.put("email_m", this.email.getText());
            js.put("id_trombi", idPromo); //il faudra qu il recuperer l id promo car on a besoin du mail et du promo pour delete la personne
        } catch (JSONException e) {
            e.printStackTrace();
            // Obligatoire avec jsonObject
        }
    }
}

/*

def deleteMember(request, content):
        con = mysql.connection
        cur = con.cursor()
        #Modif BD
        cur.execute("SELECT idM FROM Membres WHERE EmailM = %s AND idTrombi = %s",(content["email_m"],content["id_trombi"])) #requete pour selectionner l id membre
        idMembre=str(cur.fetchall()[0][0])
        print("idMembre"+idMembre)
        cur.execute("DELETE FROM Images WHERE idM = %s",(idMembre)) #requete pour supprimer l image
        cur.execute("DELETE FROM Membres WHERE EmailM = %s AND idTrombi = %s",(content["email_m"],content["id_trombi"])) #REQUETE pour supprimer le membre
        con.commit()
        return 'true'
 */