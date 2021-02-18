package com.example.trombinoscope.fragments;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.trombinoscope.FtpConnection;
import com.example.trombinoscope.ItemClickSupport;
import com.example.trombinoscope.MySingleton;
import com.example.trombinoscope.R;
import com.example.trombinoscope.adapter.EtuAdapter;
import com.example.trombinoscope.dataStructure.Etudiant;
import com.example.trombinoscope.dataStructure.Trombi;
import com.example.trombinoscope.adapter.TrombiAdapter;
import com.example.trombinoscope.view.TrombinoscopesViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditTrombi#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditTrombi extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<Etudiant> etudiants;
    private EtuAdapter adapter;
    private RecyclerView recyclerView;
    private JSONObject js = new JSONObject();
    private Trombi promo;
    public FtpConnection co;
    Button add;

    public EditTrombi() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditTrombi.
     */
    // TODO: Rename and change types and number of parameters
    public static EditTrombi newInstance(String param1, String param2) {
        EditTrombi fragment = new EditTrombi();
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

        View view =  inflater.inflate(R.layout.fragment_edit_trombi, container, false);
        add = view.findViewById(R.id.ajouter);
        this.recyclerView = view.findViewById(R.id.EtuRecyclerView);
        this.promo = getArguments().getParcelable("Trombi");
        //this.configureRecyclerView();
        update();
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here
            requestEtu();
        }

        add.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(
                        R.id.action_editTrombi_to_addToTrombi, getArguments());
                //finish();
            }
        });


        return view;
    }


   private void configureRecyclerView(){
        // 3.1 - Reset list
        this.etudiants = new ArrayList<>();

        // 3.2 - Create adapter passing the list of users
        this.adapter = new EtuAdapter(this.etudiants);
        // 3.3 - Attach the adapter to the recyclerview to populate items
        this.recyclerView.setAdapter(this.adapter);
        // 3.4 - Set layout manager to position the items
        this.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));

    }

    private void requestEtu(){
        MySingleton s = MySingleton.getInstance(getContext());
        String url = s.getUrl();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                configureRecyclerView();
                try {
                    JSONArray Nom = response.getJSONArray("Nom");
                    JSONArray Prenom = response.getJSONArray("Prenom");
                    JSONArray Img = response.getJSONArray("Img");
                    JSONArray Email = response.getJSONArray("Email");

                    for(int i = 0; i < Prenom.length(); i++){
                        Log.d("huip", Img.getString(i));
                    }

                    co = new FtpConnection();

                    for(int i = 0; i < Nom.length(); i++){
                        etudiants.add(new Etudiant(Nom.getString(i), Prenom.getString(i), Img.getString(i), co.getImage(Img.getString(i)), Email.getString(i)));
                    }
                    adapter.notifyDataSetChanged();

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

    private void update(){
        String id = this.promo.getId();

        try {
            js.put("request", "etu");

            js.put("idpromo", id);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}