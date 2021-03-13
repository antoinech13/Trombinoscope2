package com.example.trombinoscope.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile_trombi#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile_trombi extends Fragment {

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

    private Button delete, edit_image,edit_nom,edit_prenom,edit_mail; //les buttons
    private ImageView img;
    private TextView nom,prenom,email;

    private Etudiant etu; //mon objet etudiant qu il faut assigner

    private Trombi promo; //test
    private String idPromo;


    public Profile_trombi() {
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
    public static Profile_trombi newInstance(String param1, String param2) {
        Profile_trombi fragment = new Profile_trombi();
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

        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        ((MainActivity)getActivity()).setDrawer_UnLocked();
        // l objet etudiant
        etu = getArguments().getParcelable("Etu"); //etu a recuperer toutes les infos de l etudiant

        promo = getArguments().getParcelable("idTrombi"); // test
        Log.e("etu", String.valueOf(etu)); // test
        Log.e("promo", String.valueOf(promo)); //test

        idPromo = promo.getId();
        Log.e("idpromo",idPromo);



        // Les variables par rapport au layout
        img = view.findViewById(R.id.img);

        nom = view.findViewById(R.id.nom);
        prenom = view.findViewById(R.id.prenom); //on definit l image par rapport au layout
        email = view.findViewById(R.id.email);

        delete = view.findViewById(R.id.delete);
        edit_image = view.findViewById(R.id.edit_image);
        edit_nom = view.findViewById(R.id.edit_nom);
        edit_prenom = view.findViewById(R.id.edit_prenom);
        edit_mail = view.findViewById(R.id.edit_mail);

      

        img.setImageBitmap(etu.getImg()); //on recuperer l image

        nom.setText(etu.getNom()); //on recuperer le nom, get nom est une methode ds le fragment etudiant
        prenom.setText(etu.getPrenom());
        email.setText(etu.getEMail());

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action quand on clique sur le btn delete
                deleteMember(v);
            }
        });



        return view;
    }






    /*
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
                        Snackbar.make(v, "membre_supprimer", Snackbar.LENGTH_LONG).show(); // Trouver comment envoyer mail à l'utilisateur
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
     */
    // extract string ressources pour le snackbar
    // le snackbar sert a mettre un popup exemple l etudiant a etait delete

    //supprimer un membre
    private void deleteMember(View v){
        MySingleton s = MySingleton.getInstance(getContext());
        String url = s.getUrl();
        update();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("res").equals("true")) {// res= nom de la clé de la reponse fournie par flask !!! JsonObject doit etre converti en String
                        Snackbar.make(v, getResources().getString(R.string.Delete_member), Snackbar.LENGTH_LONG).show();
                        Navigation.findNavController(v).navigate(R.id.action_profile_to_editTrombi);//Retour à la page des membres
                    }
                    else
                        Snackbar.make(v, getResources().getString(R.string.Delete_member_invalid), Snackbar.LENGTH_LONG).show();
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

   /* def deleteMember(request, content): #fonctio sous flask
        con = mysql.connection
        cur = con.cursor()
        #Modif BD
        cur.execute("SELECT idM FROM Membres WHERE EmailM = %s AND idTrombi = %s",(content["email_m"],content["id_trombi"])) #requete pour selectionner l id membre
        idMembre = cur.fetchall()[0][0] # id membre est dans la dedans
        print(idMembre)
        cur.execute("DELETE FROM Images WHERE idM = %s",(idMembre)) #requete pour supprimer l image
        cur.execute("DELETE FROM Membres WHERE EmailM = %s AND idTrombi = %s",(content["email_m"],content["id_trombi"])) #REQUETE pour supprimer le membre
        con.commit()
        return 'true'
*/